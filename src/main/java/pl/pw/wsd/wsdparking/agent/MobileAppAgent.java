package pl.pw.wsd.wsdparking.agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.pw.wsd.wsdparking.city.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MobileAppAgent extends Agent {

	private static final long MOVE_AGENT_INTERVAL_MILLIS = 7000;
	private City city;
	private CityMap map;

	private Position target;
	private Position targetParking;
	private Set<Position> attemptedParkingFields = new HashSet<>();
	private Path pathToTargetParking;

	@Override
	protected void setup() {
        	System.out.println("MobileAppAgent " + getName() + "started");

		initFromArguments();
		target = map.getRandomPosition(FieldType.BUILDING);
		findNewTargetParking();

		startMoving();
        startReceivingMessages();
    }

    private void startReceivingMessages() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage message = receive();
                if(message != null) {
//                    System.out.println(getName() + " received message: " + message);
                    processMessage(message);
                } else {
                    block();
                }
            }
        });
    }

    private void processMessage(ACLMessage message) {
        if(BeaconAgent.isBeaconMessage(message)) {
            BeaconInfo info = BeaconAgent.extractBeaconInfo(message);
//            System.out.println(getName() + " Received beacon info: " + info);
            updateCityMap(info);
        }
    }

    private void updateCityMap(BeaconInfo info) {
        Map<Position, Boolean> parkingLots = info.getParkingLots();
        for (Map.Entry<Position, Boolean> entry : parkingLots.entrySet()) {
            Field field = map.get(entry.getKey());
            assertParkingField(field);
            if(field.getTimeStamp() < info.getTimeStamp()) {
                field.setTimeStamp(info.getTimeStamp());
                field.setOccupied(entry.getValue());
            }
        }
    }

    private void assertParkingField(Field field) {
        if(!FieldType.PARKING.equals(field.getType())) {
            throw new IllegalArgumentException("Received info from beacon about non-parking field: " + field);
        }
    }	

	private void startMoving() {
		addBehaviour(new TickerBehaviour(this, MOVE_AGENT_INTERVAL_MILLIS) {
			@Override
			protected void onTick() {
				System.out.println("pathToTargetParking=" + pathToTargetParking);
				if (pathToTargetParking != null && !pathToTargetParking.isEmpty()) {
					boolean success = city.move(getName(), pathToTargetParking.popNextPosition());
					if (success) {
						if (pathToTargetParking.isEmpty()) {
							// udało się zająć miejsce parkingowe
							// TODO
						}
					} else { // miejsce zajęte
						attemptedParkingFields.add(targetParking);
						findNewTargetParking();
					}
				}
			}
		});
	}

	private void findNewTargetParking() {
		targetParking = map.getNearestPosition(target, FieldType.PARKING, attemptedParkingFields);
		System.out.println("targetParking= "+targetParking);
		if (targetParking != null) {
			Position myPosition = city.getMobileAppAgentPosition(getName());
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
	}
}