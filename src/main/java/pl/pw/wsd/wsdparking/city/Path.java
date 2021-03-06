package pl.pw.wsd.wsdparking.city;

import java.util.List;

public class Path {

	private List<Position> positionOnPath;

	public Path(List<Position> positionOnPath) {
		super();
		this.positionOnPath = positionOnPath;
	}

	public List<Position> getPositionOnPath() {
		return positionOnPath;
	}

	public void setPositionOnPath(List<Position> positionOnPath) {
		this.positionOnPath = positionOnPath;
	}

	public boolean isEmpty() {
		return false;
	}

	public Position popNextPosition() {
		if (!positionOnPath.isEmpty()) {
			Position first = positionOnPath.get(0);
			positionOnPath.remove(0);
			return first;
		}
		return null;
	}

	@Override
	public String toString() {
		return "Path [positionOnPath=" + positionOnPath + "]";
	}
}
