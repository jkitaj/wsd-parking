package pl.pw.wsd.wsdparking.agent;

import pl.pw.wsd.wsdparking.city.Position;

import java.util.HashMap;
import java.util.Map;

public class BeaconInfo {
    private Map<Position, Boolean> parkingLots = new HashMap<>();
    private long timeStamp;

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Map<Position, Boolean> getParkingLots() {
        return parkingLots;
    }

    public void addInfo(Position position, boolean occupied) {
        parkingLots.put(position, occupied);
    }

    @Override
    public String toString() {
        return "BeaconInfo{" +
                "parkingLots=" + parkingLots +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
