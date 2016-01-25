package pl.pw.wsd.wsdparking.agent;

import pl.pw.wsd.wsdparking.city.City;
import pl.pw.wsd.wsdparking.city.CityMap;

public class MobileAppAgentParams {
    private City city;
    private CityMap map;

    public MobileAppAgentParams(City city, CityMap map) {
        this.city = city;
        this.map = map;
    }

    public City getCity() {
        return city;
    }

    public CityMap getMap() {
        return map;
    }
}
