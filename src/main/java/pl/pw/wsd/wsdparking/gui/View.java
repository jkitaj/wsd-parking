package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.City;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class View {

	private final int REFRESH_INTERVAL_MILLIS = 500;
	
	private final City city;
	private DrawingPanel drawingPanel;

	public View(City city) {
		this.city = city;
	}

	public void show() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("WSD - Miejsca parkingowe");
				drawingPanel = new DrawingPanel(city);
				frame.add(drawingPanel);
        frame.setSize(900, 600);
				frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	public void redraw() {
		new Timer(REFRESH_INTERVAL_MILLIS, new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						drawingPanel.repaint();
					}
				});
			}
		}).start();	
	}

}
