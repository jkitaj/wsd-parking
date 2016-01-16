package pl.pw.wsd.wsdparking.city;

public class City {

    private final CityMap map;

    public City(CityMap map) {
        this.map = map;
    }

    public Position getAgentPosition(String agentName) {
        return null;
    }

    public void addAgent(String agentName) {
        System.out.println("Add agent: " + agentName);
        // assign random position
    }

    public boolean move(String agentName, Position newPosition) {
        return false;
    }
}
