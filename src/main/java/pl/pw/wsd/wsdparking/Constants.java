package pl.pw.wsd.wsdparking;

public class Constants {

	public final static boolean USE_INFO_FROM_BEACON = true;
	public final static String NAME_OF_FILE = "/map-big.txt";
	public final static double NUMBER_OF_AGENT_PARAMETR = 1.25;
	
	public static final long MOVE_AGENT_INTERVAL_MILLIS = 250;
	public static final long STOP_ON_PARKING_INTERVAL_MILLIS = 2500;
	
	public static final long SEND_INFO_INTERVAL_MILLIS = 500;
	public static final String BEACON_ONTOLOGY = "beacon-messaging";
	
    // TODO: ustalić wartości
    public static final int MAP_FIELD_SIZE_IN_METERS = 2;   // ile metrów ma 1 kratka mapy
    public static final int BLUETOOTH_RANGE_IN_METERS = 20;

    private Constants() {}
}
