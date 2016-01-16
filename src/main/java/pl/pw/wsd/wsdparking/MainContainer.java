package pl.pw.wsd.wsdparking;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainContainer {

    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Properties properties = new ExtendedProperties();
        properties.setProperty("gui", "true");
        ProfileImpl profile = new ProfileImpl(properties);
        AgentContainer container = runtime.createMainContainer(profile);
        try {
            container.start();
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
