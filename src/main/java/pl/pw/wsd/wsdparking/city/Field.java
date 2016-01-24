package pl.pw.wsd.wsdparking.city;

public class Field {
    private final FieldType type;

    // pola do wykorzystania przez agenty
    private long timeStamp;     // aktualność informacji
    private boolean occupied;   // czy zajęte (w przypadku parkingu)

    public Field(FieldType type) {
        this.type = type;
    }

    public Field(Field field) {
        type = field.getType();
        timeStamp = field.getTimeStamp();
        occupied = field.isOccupied();
    }

    public FieldType getType() {
        return type;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public String toString() {
        return "Field{" +
                "type=" + type +
                ", timeStamp=" + timeStamp +
                ", occupied=" + occupied +
                '}';
    }
}
