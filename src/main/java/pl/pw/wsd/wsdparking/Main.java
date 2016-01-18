package pl.pw.wsd.wsdparking;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import pl.pw.wsd.wsdparking.agent.MobileAppAgent;
import pl.pw.wsd.wsdparking.agent.Params;
import pl.pw.wsd.wsdparking.city.City;
import pl.pw.wsd.wsdparking.city.CityMap;
import pl.pw.wsd.wsdparking.city.CityMapLoader;
import pl.pw.wsd.wsdparking.gui.View;

public class Main {

    public static void main(String[] args) {
        runMainContainer();

        City city = new City(new CityMapLoader().loadFromFile("/map.txt"));
        startAgents(city, getAgentContainer());

        View view = new View(city);
        view.show();
        // TODO: start refreshing view
    }

    private static void startAgents(City city, AgentContainer container) {
        Params params = new Params(city, new CityMap(city.getMap()));
        String agentClassName = MobileAppAgent.class.getName();
        for (int i = 0; i < 5; i++) {
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