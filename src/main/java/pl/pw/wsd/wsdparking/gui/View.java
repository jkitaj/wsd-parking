package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.City;

import javax.swing.*;

public class View {

    private final City city;
    private DrawingPanel drawingPanel;

    public View(City city) {
        this.city = city;
    }

    public void show() {
        JFrame frame = new JFrame("WSD - Miejsca parkingowe");
        drawingPanel = new DrawingPanel(city);
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
