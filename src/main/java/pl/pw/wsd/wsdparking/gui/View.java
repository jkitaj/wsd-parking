package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.CityMap;

import javax.swing.*;

public class View {

    private final CityMap cityMap;
    private DrawingPanel drawingPanel;

    public View(CityMap map) {
        cityMap = map;
    }

    public void show() {
        JFrame frame = new JFrame("WSD - Miejsca parkingowe");
        drawingPanel = new DrawingPanel(cityMap);
        frame.add(drawingPanel);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public void redraw() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                drawingPanel.repaint();
            }
        });
    }
}
