package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.CityMap;
import pl.pw.wsd.wsdparking.city.Field;
import pl.pw.wsd.wsdparking.city.FieldType;
import pl.pw.wsd.wsdparking.city.Position;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {

    private final CityMap cityMap;

    public DrawingPanel(CityMap map) {
        this.cityMap = map;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCityMap((Graphics2D) g);
    }

    private void drawCityMap(Graphics2D g) {
        int fieldSize = getHeight() / (cityMap.getHeight() + 2);
        int sideMargin = (getWidth() - (cityMap.getWidth() * fieldSize)) / 2;
        for(int col=0; col<cityMap.getWidth(); col++) {
            for(int row=0; row<cityMap.getHeight(); row++) {
                Field field = cityMap.get(new Position(col, row));
                g.setColor(colorForField(field.getType()));
                int x = col * fieldSize + sideMargin;
                int y = row * fieldSize + fieldSize;
                g.fillRect(x, y, fieldSize, fieldSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, fieldSize, fieldSize);
            }
        }
    }

    private Color colorForField(FieldType type) {
        if(type.equals(FieldType.PARKING)) {
            return Color.GREEN;
        } else {
            return Color.WHITE;
        }
    }
}