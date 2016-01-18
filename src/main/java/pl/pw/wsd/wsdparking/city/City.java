package pl.pw.wsd.wsdparking.city;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class City {

    private final CityMap map;
    private Map<String, Position> mobileAppAgents = new HashMap<>();

    public City(CityMap map) {
        this.map = map;
    }

    public Position getMobileAppAgentPosition(String agentName) {
        return mobileAppAgents.get(agentName);
    }

    public void addMobileAppAgent(String agentName) {
        System.out.println("Add agent: " + agentName);
        Position position = map.getRandomPosition(FieldType.STREET);
        mobileAppAgents.put(agentName, position);
    }

    public CityMap getMap() {
        return map;
    }

    public Collection<Position> getMobileAppAgentsPositions() {
        return mobileAppAgents.values();
    }

    public boolean move(String agentName, Position newPosition) {
        return false;
    }
}
