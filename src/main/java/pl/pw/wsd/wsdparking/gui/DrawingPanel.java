package pl.pw.wsd.wsdparking.gui;

import pl.pw.wsd.wsdparking.Constants;
import pl.pw.wsd.wsdparking.city.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

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
		drawMobileAppAgents(g, city.getMobileAppAgents());
//		drawBeaconAgents(g, city.getBeaconAgentPositions()); // for debugging only
//		drawBeaconRanges(g, city.getBeaconAgentPositions());
	}

	private void initializeDimensions(CityMap cityMap) {
		fieldSize = getHeight() / (cityMap.getHeight() + 2);
		topBottomMargin = fieldSize;
		leftRightMargin = (getWidth() - (cityMap.getWidth() * fieldSize)) / 2;
	}

	private void drawMobileAppAgents(Graphics2D g, Map<String, Position> agents) {
		for (Entry<String, Position> pair : agents.entrySet()) {
			g.setColor(Color.RED);
			int x = pair.getValue().getX() * fieldSize + leftRightMargin;
			int y = pair.getValue().getY() * fieldSize + topBottomMargin;
			g.fillRect(x, y, fieldSize, fieldSize);
			g.setColor(Color.BLACK);
			//String shortName = pair.getKey().substring(0, pair.getKey().indexOf("@"));
			//g.drawString(shortName, pair.getValue().getX()* fieldSize + fieldSize/4 + leftRightMargin, pair.getValue().getY()* fieldSize + fieldSize/2 + topBottomMargin);
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

	private void drawBeaconRanges(Graphics2D g, Collection<Position> positions) {
		for (Position position : positions) {
			g.setColor(Color.GREEN);
			int radius = fieldSize * Constants.BLUETOOTH_RANGE_IN_METERS / Constants.MAP_FIELD_SIZE_IN_METERS;
			int diameter = 2 * radius;
			int x = position.getX() * fieldSize + leftRightMargin + (fieldSize / 2);
			int y = position.getY() * fieldSize + topBottomMargin + (fieldSize / 2);
			g.draw(new Arc2D.Double(x - radius, y - radius, diameter, diameter, 0, 360, Arc2D.OPEN));
		}
	}

	private void drawMap(Graphics2D g, CityMap cityMap) {
		for (int col = 0; col < cityMap.getWidth(); col++) {
			for (int row = 0; row < cityMap.getHeight(); row++) {
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
		if (type.equals(FieldType.PARKING)) {
			return Color.YELLOW;
		} else if (type.equals(FieldType.BUILDING)) {
			return Color.BLUE;
		} else {
			return Color.WHITE;
		}
	}
}