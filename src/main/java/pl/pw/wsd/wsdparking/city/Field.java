package pl.pw.wsd.wsdparking.city;

public class Field {
    private final FieldType type;
    private final Position position;    // needed?

    public Field(FieldType type, Position position) {
        this.type = type;
        this.position = position;
    }

    public FieldType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }
}
