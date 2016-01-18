package pl.pw.wsd.wsdparking.city;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CityMap {

    private Map<Position, Field> fields = new HashMap<>();
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void set(Position position, FieldType type) {
        width = Math.max(width, position.getX() + 1);
        height = Math.max(height, position.getY() + 1);
        fields.put(position, new Field(type, position));
    }

    public Field get(Position position) {
        return Optional.of(fields.get(position))
                .orElseThrow(() -> new RuntimeException("No field for position: " + position));
    }

    public Field getRandomField(FieldType type) {
        return null;
    }

    public Field getNearestField(Field source, FieldType type, Set<Field> excludeFields) {
        return null;
    }

    public Path getShortestPath(Position from, Position to) {
        return null;
    }
}