package pl.pw.wsd.wsdparking.city;

public class Field {
    private final FieldType type;

    // pola do wykorzystania przez agenty
    private long timeStamp;     // aktualność informacji
    private boolean occupied;   // czy zajęte (w przypadku parkingu)

    public Field(FieldType type) {
        this.type = type;
    }

    public FieldType getType() {
        return type;
    }

	@Override
	public String toString() {
		return "Field [type=" + type + ", timeStamp=" + timeStamp + ", occupied=" + occupied + "]";
	}
    
}
