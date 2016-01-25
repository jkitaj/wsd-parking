package pl.pw.wsd.wsdparking;

public class Constants {

	public final static boolean USE_INFO_FROM_BEACON = true;
	public final static boolean USE_INFO_FROM_OTHER_AGENTS = true;

	public final static String NAME_OF_MAP_RESOURCE_FILE = "/map.txt";
	public final static double NUMBER_OF_AGENTS_PARAMETER = 1.5;

	// mobile app agents
	public static final long MOVE_AGENT_INTERVAL_MILLIS = 250;
	public static final long STOP_ON_PARKING_INTERVAL_MILLIS = 2500;
	public static final long SEND_MAP_INTERVAL_MILLIS = 3000;
	public static final String MAP_EXCHANGE_ONTOLOGY = "wifi-direct-map-exchange";

	// beacons
	public static final int BEACON_REGION_SIZE = 5; // bok kwadratu stanowiącego obszar podlegający beaconowi
	public static final long SEND_BEACON_INFO_INTERVAL_MILLIS = 500;
	public static final String BEACON_ONTOLOGY = "beacon-messaging";

    // TODO: ustalić wartości
    public static final int MAP_FIELD_SIZE_IN_METERS = 2;   // ile metrów ma 1 kratka mapy
    public static final int BLUETOOTH_RANGE_IN_METERS = 20;
	// theoretically up to 100m but see discussion: http://stackoverflow.com/questions/24586003/range-of-distances-in-which-wi-fi-direct-works
    public static final int WIFI_DIRECT_RANGE_IN_METERS = 60;

    public final static String NAME_OF_FILE_WITH_STATS = "stat.txt";

	private Constants() {}
}
