package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class DrawingPanel extends JPanel {

    private final City city;
    private int fieldSize;
    private int leftRightMargin;
    private int topBottomMargin;

    public DrawingPanel(City city) {
        this.city = city;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCity((Graphics2D) g);
    }

    private void drawCity(Graphics2D g) {
        CityMap cityMap = city.getMap();
        initializeDimensions(cityMap);
        drawMap(g, cityMap);
        drawMobileAppAgents(g, city.getMobileAppAgentsPositions());
//        drawBeaconAgents(g, city.getBeaconAgentPositions());  // for debugging only
    }

    private void initializeDimensions(CityMap cityMap) {
        fieldSize = getHeight() / (cityMap.getHeight() + 2);
        topBottomMargin = fieldSize;
        leftRightMargin = (getWidth() - (cityMap.getWidth() * fieldSize)) / 2;
    }

    private void drawMobileAppAgents(Graphics2D g, Collection<Position> positions) {
        for (Position position : positions) {
            g.setColor(Color.RED);
            int x = position.getX() * fieldSize + leftRightMargin;
            int y = position.getY() * fieldSize + topBottomMargin;
            g.fillRect(x, y, fieldSize, fieldSize);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, fieldSize, fieldSize);
        }
    }

    private void drawBeaconAgents(Graphics2D g, Collection<Position> positions) {
        for (Position position : positions) {
            g.setColor(Color.BLACK);
            int x = position.getX() * fieldSize + leftRightMargin;
            int y = position.getY() * fieldSize + topBottomMargin;
            g.fillRect(x, y, fieldSize, fieldSize);
        }
    }

    private void drawMap(Graphics2D g, CityMap cityMap) {
        for(int col=0; col<cityMap.getWidth(); col++) {
            for(int row=0; row<cityMap.getHeight(); row++) {
                Field field = cityMap.get(new Position(col, row));
                g.setColor(colorForField(field.getType()));
                int x = col * fieldSize + leftRightMargin;
                int y = row * fieldSize + topBottomMargin;
                g.fillRect(x, y, fieldSize, fieldSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, fieldSize, fieldSize);
            }
        }
    }

    private Color colorForField(FieldType type) {
        if(type.equals(FieldType.PARKING)) {
            return Color.YELLOW;
        } else if(type.equals(FieldType.BUILDING)){
            return Color.BLUE;
        } else {
        	return Color.WHITE;
        }
    }
}