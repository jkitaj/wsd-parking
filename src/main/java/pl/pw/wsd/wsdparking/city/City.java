package pl.pw.wsd.wsdparking.city;

import pl.pw.wsd.wsdparking.Constants;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class City {

	private final CityMap map;
	private Map<String, Position> mobileAppAgents = new HashMap<>();
	private Map<String, Position> beaconAgents = new HashMap<>();

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

	public Map<String, Position> getMobileAppAgents() {
		return mobileAppAgents;
	}

	public boolean move(String agentName, Position newPosition) {
		if (newPosition != null) {
			if (isOccupied(newPosition))
				return false;
			else {
				mobileAppAgents.put(agentName, newPosition);
				return true;
			}
		}
		return true;
	}

	public void addBeaconAgent(String beaconName, Position beaconPosition) {
		System.out.println("Add beacon: " + beaconName);
		beaconAgents.put(beaconName, beaconPosition);
	}

	public boolean isOccupied(Position position) {
		return mobileAppAgents.values().contains(position);
	}

	public List<String> getMobileAppAgentsInBluetoothRange(String beaconName) {
		Position beaconPosition = beaconAgents.get(beaconName);
		if (beaconPosition == null) {
			throw new IllegalArgumentException("No beacon for name: " + beaconName);
		}
		return mobileAppAgentsInRange(beaconPosition, Constants.BLUETOOTH_RANGE_IN_METERS);
	}

	public List<String> getMobileAppAgentsInWiFiDirectRange(String mobileAppAgentName) {
		Position agentPosition = mobileAppAgents.get(mobileAppAgentName);
		if (agentPosition == null) {
			throw new IllegalArgumentException("No mobile app agent for name: " + mobileAppAgentName);
		}
		return mobileAppAgentsInRange(agentPosition, Constants.WIFI_DIRECT_RANGE_IN_METERS);
	}

	private List<String> mobileAppAgentsInRange(Position position, int rangeInMeters) {
		return mobileAppAgents.entrySet().stream()
				.filter(entry -> distanceInMeters(position, entry.getValue()) <= rangeInMeters)
				.map(Map.Entry::getKey).collect(Collectors.toList());
	}

	private double distanceInMeters(Position p1, Position p2) {
		int xDiff = p1.getX() - p2.getX();
		int yDiff = p1.getY() - p2.getY();
		return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff)) * Constants.MAP_FIELD_SIZE_IN_METERS;
	}

	public Collection<Position> getBeaconAgentPositions() {
		return beaconAgents.values();
	}
	
}
