package pl.pw.wsd.wsdparking.city;

import java.util.*;

public class CityMap {

    private Map<Position, Field> fields = new HashMap<>();
    private int width;
    private int height;

    public CityMap() {}

    public CityMap(CityMap cityMap) {
        // TODO: create copy
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void set(Position position, FieldType type) {
        width = Math.max(width, position.getX() + 1);
        height = Math.max(height, position.getY() + 1);
        fields.put(position, new Field(type));
    }

    public Field get(Position position) {
        return Optional.of(fields.get(position))
                .orElseThrow(() -> new RuntimeException("No field for position: " + position));
    }

    public Position getRandomPosition(FieldType type) {
        Random random = new Random();
        while(true) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Position position = new Position(x, y);
            Field field = fields.get(position);
            if(field.getType().equals(type)) {
                return position;
            }
        }
    }

    public Position getNearestPosition(Position source, FieldType type, Set<Position> excludePosition) {
        return null;
    }

    public Path getShortestPath(Position from, Position to) {
        return null;
    }
}