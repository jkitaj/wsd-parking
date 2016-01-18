package pl.pw.wsd.wsdparking;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import pl.pw.wsd.wsdparking.city.City;
import pl.pw.wsd.wsdparking.city.CityMap;
import pl.pw.wsd.wsdparking.city.CityMapLoader;
import pl.pw.wsd.wsdparking.gui.View;

public class SecondaryContainer {

    public static void main(String[] args) {
        String name = Thread.currentThread().getName();
        System.out.println(name);

        CityMap cityMap = new CityMapLoader().loadFromFile("/map.txt");
        City city = new City(cityMap);

//        AgentContainer container = createContainer();
//        Params params = new Params(city, cityMap);
//        String agentClassName = MobileAppAgent.class.getName();
//        for (int i = 0; i < 5; i++) {
//            String nickname = "Agent" + i;
//            try {
//                AgentController controller = container.createNewAgent(nickname, agentClassName, new Object[]{ params });
//                city.addAgent(controller.getName());
//                controller.start();
//            } catch (StaleProxyException e) {
//                e.printStackTrace();
//            }
//        }

        View view = new View(cityMap);
        view.show();
        // TODO: start refreshing view
    }

    private static AgentContainer createContainer() {
        Runtime runtime = Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        return runtime.createAgentContainer(profile);
    }
}
