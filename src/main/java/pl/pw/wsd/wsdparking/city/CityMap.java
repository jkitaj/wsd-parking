package pl.pw.wsd.wsdparking.city;

import java.util.*;
import java.util.Map.Entry;

import pl.pw.wsd.wsdparking.graph.DijkstraAlgorithm;
import pl.pw.wsd.wsdparking.graph.Edge;
import pl.pw.wsd.wsdparking.graph.Graph;
import pl.pw.wsd.wsdparking.graph.Vertex;

public class CityMap {

	private Map<Position, Field> fields = new HashMap<>();
	private int width;
	private int height;

	public CityMap() {
	}

	public CityMap(CityMap cityMap) {
		this.fields.putAll(cityMap.fields);
		this.height = cityMap.height;
		this.width = cityMap.width;
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
		while (true) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			Position position = new Position(x, y);
			Field field = fields.get(position);
			if (field.getType().equals(type)) {
				return position;
			}
		}
	}

	public Position getNearestPosition(Position source, FieldType type, Set<Position> excludePosition) {
		System.out.println("source=" + source + ", type" + type + ", exclude " + excludePosition);
		TreeMap<Double, Position> tMap = new TreeMap<>();
		for (Position p : this.fields.keySet()) {
			if (this.get(p).getType() == type && !excludePosition.contains(p)) {
				Double distance = Math
						.sqrt(Math.pow(source.getX() - p.getX(), 2) + Math.pow(source.getY() - p.getY(), 2));
				tMap.put(distance, p);
			}
		}
		if (!tMap.isEmpty()) {
			return tMap.get(tMap.firstKey());
		}
		return null;
	}

	public Path getShortestPath(Position from, Position to) {
		System.out.println("Looking for path from '" + from + "' to '" + to + "' ...");
		Graph graph = prepareGraphFromMap(this);
		DijkstraAlgorithm alg = new DijkstraAlgorithm(graph);
		Vertex source = new Vertex(from.getX(), from.getY());
		Vertex target = new Vertex(to.getX(), to.getY());
		alg.execute(source, target);
		LinkedList<Vertex> path = alg.getPath(target);
		return convertToStreetPath(path);
	}

	private Graph prepareGraphFromMap(CityMap map) {
		List<Vertex> vertexes = new ArrayList<>();
		List<Edge> edges = new ArrayList<>();
		Iterator<Entry<Position, Field>> it = map.fields.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Position, Field> pair = (Map.Entry<Position, Field>) it.next();
			Position p = pair.getKey();
			Field f = pair.getValue();
			if (f.getType().equals(FieldType.STREET) || f.getType().equals(FieldType.PARKING)) {
				Vertex v = new Vertex(p.getX(), p.getY());
				if (!vertexes.contains(v))
					vertexes.add(v);

				//we don't need to check trace from parking
				if (!f.getType().equals(FieldType.PARKING)) {
					// check up field - if it is street add it to graph
					Field up = checkPosition(map, p.getX(), p.getY() - 1);
					addToGraph(up, p.getX(), p.getY() - 1, v, vertexes, edges);

					// check right field - if it is street add it to graph
					Field right = checkPosition(map, p.getX() + 1, p.getY());
					addToGraph(right, p.getX() + 1, p.getY(), v, vertexes, edges);

					// check down field - if it is street add it to graph
					Field down = checkPosition(map, p.getX(), p.getY() - 1);
					addToGraph(down, p.getX(), p.getY() - 1, v, vertexes, edges);

					// check left field - if it is street add it to graph
					Field left = checkPosition(map, p.getX() - 1, p.getY());
					addToGraph(left, p.getX() - 1, p.getY(), v, vertexes, edges);
				}
			}
		}
		Graph g = new Graph(vertexes, edges);
		return g;
	}

	private Field checkPosition(CityMap map, int x, int y) {
		try {
			return map.get(new Position(x, y));
		} catch (RuntimeException e) {
			return null;
		}
	}

	private void addToGraph(Field f, int x, int y, Vertex base, List<Vertex> vertexs, List<Edge> edges) {
		if (f != null && (f.getType().equals(FieldType.STREET) || f.getType().equals(FieldType.PARKING))) {
			Vertex v = new Vertex(x, y);
			Edge e = new Edge(base, v, 1);
			Edge eRev = new Edge(v, base, 1);
			if (!vertexs.contains(v))
				vertexs.add(v);
			if (!edges.contains(e)){
				edges.add(e);
				edges.add(eRev);
			}
		}
	}

	private Path convertToStreetPath(LinkedList<Vertex> path) {
		List<Position> postList = new ArrayList<>();
		for (Vertex v : path) {
			Position p = new Position(v.getX(), v.getY());
			postList.add(p);
		}
		System.out.println("Path to parking=" + new Path(postList));
		return new Path(postList);
	}
}