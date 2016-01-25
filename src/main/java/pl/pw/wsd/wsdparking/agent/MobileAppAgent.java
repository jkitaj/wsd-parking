package pl.pw.wsd.wsdparking.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.pw.wsd.wsdparking.Constants;
import pl.pw.wsd.wsdparking.SaveStats;
import pl.pw.wsd.wsdparking.city.*;

import java.util.*;

import static pl.pw.wsd.wsdparking.city.FieldType.PARKING;

public class MobileAppAgent extends Agent {

	private City city;
	private CityMap map;

	private Position target;
	private Position targetParking;
	private Set<Position> attemptedParkingFields = new HashSet<>();
	private Path pathToTargetParking;

	private List<Integer> stats = new ArrayList<>();

	// time start of looking free place to park
	private Long timestamp;

	@Override
	protected void setup() {
		System.out.println("MobileAppAgent " + getName() + "started");

		initFromArguments();
		lookForNewTarget();

		if(Constants.USE_INFO_FROM_OTHER_AGENTS || Constants.USE_INFO_FROM_BEACON) {
			startReceivingMessages();
		}
		if(Constants.USE_INFO_FROM_OTHER_AGENTS) {
			startSendingMapOverWifiDirect();
		}
		startMoving();
	}

	private void lookForNewTarget() {
		target = map.getRandomPosition(FieldType.BUILDING);
		timestamp = System.currentTimeMillis();
		findNewTargetParking();
	}

	private void startReceivingMessages() {
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() {
				ACLMessage message = receive();
				if (message != null) {
					//System.out.println(getName() + " received message: " + message);
					processMessage(message);
				} else {
					block();
				}
			}
		});
	}

	private void processMessage(ACLMessage message) {
		if (BeaconAgent.isBeaconMessage(message)) {
			BeaconInfo info = BeaconAgent.extractBeaconInfo(message);
//			System.out.println(getName() + " Received beacon info: " + info);
			updateCityMap(info);
		} else if(isWiFiDirectMessage(message)) {
			CityMap otherMap = new Gson().fromJson(message.getContent(), CityMap.class);
//			System.out.println(getName() + " Received map from other agent: " + otherMap);
			updateCityMap(otherMap);
		}
	}

	private void updateCityMap(BeaconInfo info) {
		Map<Position, Boolean> parkingLots = info.getParkingLots();
		for (Map.Entry<Position, Boolean> entry : parkingLots.entrySet()) {
			Field field = map.get(entry.getKey());
			assertParkingField(field);
			if (field.getTimeStamp() < info.getTimeStamp()) {
				field.setTimeStamp(info.getTimeStamp());
				field.setOccupied(entry.getValue());
				map.updateMap(entry.getKey(), field);
			}
		}
	}

	private void updateCityMap(CityMap otherMap) {
		assertCorrectDimensions(otherMap);
		for (int x=0; x<map.getWidth(); x++) {
			for(int y=0; y<map.getHeight(); y++) {
				Position position = new Position(x, y);
				Field myField = map.get(position);
				Field otherField = otherMap.get(position);
				if(PARKING.equals(myField.getType()) && PARKING.equals(otherField.getType())) {
					if(otherField.getTimeStamp() > myField.getTimeStamp()) {
						myField.setTimeStamp(otherField.getTimeStamp());
						myField.setOccupied(otherField.isOccupied());
						map.updateMap(position, myField);	// this is unnecessary but let's do this just in case :)
					}
				}
			}
		}
	}

	private void assertCorrectDimensions(CityMap otherMap) {
		if(!CityMap.haveEqualDimensions(map, otherMap)) {
			throw new IllegalArgumentException("Received map with dimensions: width=" + otherMap.getWidth()
					+ " height=" + otherMap.getHeight() + " which are different than those of known map: width="
					+ map.getWidth() + " height=" + map.getHeight());
		}
	}

	private void assertParkingField(Field field) {
		if (!PARKING.equals(field.getType())) {
			throw new IllegalArgumentException("Received info from beacon about non-parking field: " + field);
		}
	}

	private void startMoving() {
		addBehaviour(new TickerBehaviour(this, Constants.MOVE_AGENT_INTERVAL_MILLIS) {
			@Override
			protected void onTick() {
				if (pathToTargetParking != null && !pathToTargetParking.isEmpty()) {
					if (Constants.USE_INFO_FROM_BEACON && map.get(targetParking).isOccupied()) {
						attemptedParkingFields.add(targetParking);
						findNewTargetParking();
					}
					boolean success = city.move(getName(), pathToTargetParking.popNextPosition());
					if (pathToTargetParking.getPositionOnPath().isEmpty()) {
						if (success) {
							try {
								long timeToFindParking = System.currentTimeMillis() - timestamp;
								System.out.println("Time to find parking by agent " + getName() + " was "
										+ timeToFindParking / 1000 + " sec.");
								stats.add(new Long(timeToFindParking).intValue());
								Thread.sleep(Constants.STOP_ON_PARKING_INTERVAL_MILLIS);
							} catch (InterruptedException ex) {
								Thread.currentThread().interrupt();
							}
							lookForNewTarget();
						} else { // miejsce zajete
							attemptedParkingFields.add(targetParking);
							findNewTargetParking();
						}
					}
				}
			}
		});
	}

	private void findNewTargetParking() {
		targetParking = map.getNearestPosition(target, PARKING, attemptedParkingFields);
		if (targetParking != null) {
			Position myPosition = city.getMobileAppAgentPosition(getName());
			if (targetParking.equals(myPosition)) {
				// when nearest parking place is current place
				return;
			}
			pathToTargetParking = map.getShortestPath(myPosition, targetParking);
		}
	}

	private void initFromArguments() {
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			Params params = (Params) args[0];
			city = params.getCity();
			map = params.getMap();
		}
	}

	private void startSendingMapOverWifiDirect() {
		addBehaviour(new TickerBehaviour(this, Constants.SEND_MAP_INTERVAL_MILLIS) {
			@Override
			protected void onTick() {
				List<String> receivers = city.getMobileAppAgentsInWiFiDirectRange(getName());
				ACLMessage msg = buildMessage(receivers);
				send(msg);
			}
		});
	}

	private ACLMessage buildMessage(List<String> receivers) {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setOntology(Constants.MAP_EXCHANGE_ONTOLOGY);
		for(String r : receivers) {
            msg.addReceiver(new AID(r, AID.ISGUID));
        }
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
		msg.setContent(gson.toJson(map));
		return msg;
	}

	private boolean isWiFiDirectMessage(ACLMessage msg) {
		return ACLMessage.INFORM == msg.getPerformative()
				&& msg.getOntology().equals(Constants.MAP_EXCHANGE_ONTOLOGY);
	}

	@Override
	protected void takeDown() {
		System.out.println("MobileAppAgent " + getName() + " finished");
		calcAndShowStats();
	}

	private void calcAndShowStats() {
		int sum = 0;
		for (Integer i : stats) {
			sum += i;
		}
		SaveStats.saveToFile((((double) sum / stats.size() / 1000) + "").replace('.', ','));
		System.out.println("Average time to park of agent "+getName()+" was "+ (double)sum/stats.size()/1000 + " sec.");
	}
}