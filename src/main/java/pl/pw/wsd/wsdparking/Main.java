package pl.pw.wsd.wsdparking;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import pl.pw.wsd.wsdparking.agent.BeaconAgent;
import pl.pw.wsd.wsdparking.agent.MobileAppAgent;
import pl.pw.wsd.wsdparking.agent.Params;
import pl.pw.wsd.wsdparking.city.*;
import pl.pw.wsd.wsdparking.gui.View;

import java.util.List;

public class Main {
	
    public static void main(String[] args) {
    	SaveStats.clearStat();
        runMainContainer();

        City city = new City(new CityMapLoader().loadFromFile(Constants.NAME_OF_FILE));
        AgentContainer agentContainer = getAgentContainer();
        if(Constants.USE_INFO_FROM_BEACON)
        	startBeaconAgents(city, agentContainer);
        startMobileAppAgents(city, agentContainer);

        View view = new View(city);
        view.show();
        view.redraw();
    }

    private static void startBeaconAgents(City city, AgentContainer agentContainer) {
        CityMap map = city.getMap();
        List<Region> regions = map.divideIntoRegions(5);
        int cnt = 0;
        for (Region region : regions) {
            Position beaconPosition = region.center();
            List<Position> parkingLots = map.getFieldsFromRegion(region, FieldType.PARKING);
            if(!parkingLots.isEmpty()) {
                String nickname = "Beacon" + (++cnt);
                BeaconAgent.Params params = new BeaconAgent.Params(parkingLots, city);
                try {
                    AgentController controller = agentContainer.createNewAgent(
                            nickname, BeaconAgent.class.getName(), new Object[]{ params });
                    city.addBeaconAgent(controller.getName(), beaconPosition);
                    controller.start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void startMobileAppAgents(City city, AgentContainer container) {
        Params params = new Params(city, new CityMap(city.getMap()));
        String agentClassName = MobileAppAgent.class.getName();
        int parkingFieldsCount = city.getMap().countFields(FieldType.PARKING);
        int agentsCount = (int) (parkingFieldsCount * Constants.NUMBER_OF_AGENTS_PARAMETER);
        for (int i = 0; i < agentsCount; i++) {
            String nickname = "Agent" + i;
            try {
                AgentController controller = container.createNewAgent(nickname, agentClassName, new Object[]{ params });
                city.addMobileAppAgent(controller.getName());
                controller.start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
        }
    }

    private static AgentContainer getAgentContainer() {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        return runtime.createAgentContainer(profile);
    }

    private static void runMainContainer() {
        Runtime runtime = Runtime.instance();
        Properties properties = new ExtendedProperties();
        properties.setProperty("gui", "true");
        ProfileImpl profile = new ProfileImpl(properties);
        AgentContainer container = runtime.createMainContainer(profile);
        try {
            container.start();
        } catch (ControllerException e1) {
            e1.printStackTrace();
        }
    }
}