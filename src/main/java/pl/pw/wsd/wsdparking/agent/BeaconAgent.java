package pl.pw.wsd.wsdparking.agent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.pw.wsd.wsdparking.Constants;
import pl.pw.wsd.wsdparking.city.City;
import pl.pw.wsd.wsdparking.city.Position;

import java.util.List;

public class BeaconAgent extends Agent {

    public static class Params {
        private final List<Position> parkingPositions;
        private final City city;

        public Params(List<Position> parkingPositions, City city) {
            this.parkingPositions = parkingPositions;
            this.city = city;
        }
    }

    private List<Position> parkingPositions;
    private City city;

    @Override
    protected void setup() {
        System.out.println("Beacon " + getName() + "started");
        initFromArguments();
        startSendingBeaconInfo();
    }

    private void startSendingBeaconInfo() {
        addBehaviour(new TickerBehaviour(this, Constants.SEND_BEACON_INFO_INTERVAL_MILLIS) {
            @Override
            protected void onTick() {
                BeaconInfo beaconInfo = prepareBeaconInfo();
                List<String> agentNames = city.getMobileAppAgentsInBluetoothRange(getName());
                send(buildMessage(beaconInfo, agentNames));
            }
        });
    }

    public static boolean isBeaconMessage(ACLMessage msg) {
        return ACLMessage.INFORM == msg.getPerformative()
                && Constants.BEACON_ONTOLOGY.equals(msg.getOntology());
    }

    public static BeaconInfo extractBeaconInfo(ACLMessage msg) {
        return new Gson().fromJson(msg.getContent(), BeaconInfo.class);
    }

    private ACLMessage buildMessage(BeaconInfo beaconInfo, List<String> receiverNames) {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        for (String name : receiverNames) {
            message.addReceiver(new AID(name, AID.ISGUID));
        }
        message.setOntology(Constants.BEACON_ONTOLOGY);
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        message.setContent(gson.toJson(beaconInfo));
        return message;
    }

    private BeaconInfo prepareBeaconInfo() {
        BeaconInfo beaconInfo = new BeaconInfo();
        for(Position pos : parkingPositions) {
            beaconInfo.addInfo(pos, city.isOccupied(pos));
        }
        beaconInfo.setTimeStamp(System.currentTimeMillis());
        return beaconInfo;
    }

    private void initFromArguments() {
        Object[] args = getArguments();
        if(args != null && args.length > 0) {
            Params params = (Params) args[0];
            this.parkingPositions = params.parkingPositions;
            this.city = params.city;
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("Beacon " + getName() + " finished");
    }
}
