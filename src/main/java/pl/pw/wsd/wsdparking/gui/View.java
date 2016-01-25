package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.city.City;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View {

	private static final int REFRESH_INTERVAL_MILLIS = 500;
	
	private final City city;
	private DrawingPanel drawingPanel;
	private Timer timer;

	public View(City city) {
		this.city = city;
	}

	public void show() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = setupFrame();
			timer = new Timer(REFRESH_INTERVAL_MILLIS, e -> SwingUtilities.invokeLater(drawingPanel::repaint));
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					timer.stop();
				}
			});
			timer.start();
		});
	}

	private JFrame setupFrame() {
		JFrame frame = new JFrame("WSD - Miejsca parkingowe");
		drawingPanel = new DrawingPanel(city);
		frame.add(drawingPanel);
		frame.setSize(900, 600);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		return frame;
	}
}