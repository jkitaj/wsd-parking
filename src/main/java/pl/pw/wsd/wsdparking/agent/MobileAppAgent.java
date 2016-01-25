package pl.pw.wsd.wsdparking.agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.pw.wsd.wsdparking.Constants;
import pl.pw.wsd.wsdparking.SaveStats;
import pl.pw.wsd.wsdparking.city.City;
import pl.pw.wsd.wsdparking.city.CityMap;
import pl.pw.wsd.wsdparking.city.Field;
import pl.pw.wsd.wsdparking.city.FieldType;
import pl.pw.wsd.wsdparking.city.Path;
import pl.pw.wsd.wsdparking.city.Position;

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

		startMoving();
		startReceivingMessages();
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
			System.out.println(getName() + " Received beacon info: " + info);
			updateCityMap(info);
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

	private void assertParkingField(Field field) {
		if (!FieldType.PARKING.equals(field.getType())) {
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
		targetParking = map.getNearestPosition(target, FieldType.PARKING, attemptedParkingFields);
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