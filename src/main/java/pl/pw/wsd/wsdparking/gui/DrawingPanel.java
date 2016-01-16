package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.CityMap;

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
        draw((Graphics2D) g);   // TODO: draw city map
    }

    private void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 100, 100);
    }
}