package pl.pw.wsd.wsdparking.agent;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import pl.pw.wsd.wsdparking.city.*;

import java.util.HashSet;
import java.util.Set;

public class MobileAppAgent extends Agent {

    private static final long MOVE_AGENT_INTERVAL_MILLIS = 700;
    private City city;
    private CityMap map;

    private Field target;
    private Field targetParking;
    private Set<Field> attemptedParkingFields = new HashSet<>();
    private Path pathToTargetParking;

    @Override
    protected void setup() {
        System.out.println("Agent " + getName() + "started");

        initFromArguments();
        target = map.getRandomField(FieldType.STREET);
        findNewTargetParking();

        startMoving();
    }

    private void startMoving() {
        addBehaviour(new TickerBehaviour(this, MOVE_AGENT_INTERVAL_MILLIS) {
            @Override
            protected void onTick() {
                if(pathToTargetParking != null && !pathToTargetParking.isEmpty()) {
                    boolean success = city.move(getName(), pathToTargetParking.popNextPosition());
                    if(success) {
                        if(pathToTargetParking.isEmpty()) {
                            // udało się zająć miejsce parkingowe
                            // TODO
                        }
                    } else {  // miejsce zajęte
                        attemptedParkingFields.add(targetParking);
                        findNewTargetParking();
                    }
                }
            }
        });
    }

    private void findNewTargetParking() {
        targetParking = map.getNearestField(target, FieldType.PARKING, attemptedParkingFields);
        Position myPosition = city.getAgentPosition(getName());
        pathToTargetParking = map.getShortestPath(myPosition, targetParking.getPosition());
    }

    private void initFromArguments() {
        Object[] args = getArguments();
        if(args != null && args.length > 0) {
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