package pl.pw.wsd.wsdparking.graph;

import java.util.List;

public class Graph {
	private List<Vertex> vertexes;
	private List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		super();
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public void setVertexes(List<Vertex> vertexes) {
		this.vertexes = vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public Vertex getVertex(int x, int y){
		Vertex key = new Vertex(x, y);
		for(Vertex v : vertexes)
			if(v.equals(key))
				return v;
		return null;
	}
}
