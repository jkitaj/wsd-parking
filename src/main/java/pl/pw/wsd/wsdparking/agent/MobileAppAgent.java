package pl.pw.wsd.wsdparking.agent;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import pl.pw.wsd.wsdparking.city.*;

import java.util.HashSet;
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
		System.out.println("Agent " + getName() + "started");

		initFromArguments();
		target = map.getRandomPosition(FieldType.BUILDING);
		findNewTargetParking();

		startMoving();
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
		System.out.println("Agent " + getName() + " finished");
	}
}