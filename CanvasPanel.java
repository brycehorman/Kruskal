import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
public class CanvasPanel extends JPanel{
	
	LinkedList vertices = null;
	LinkedList edges = null;
	LinkedList edgeWeights = null;
	Kruskal parent = null;

	public CanvasPanel(Kruskal _parent){
		super();
		parent = _parent;
		vertices = parent.vertices;
		edges = parent.edges;
		edgeWeights = parent.edgeWeights;

	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.red);
		Point currentVertex = null;
		Point prevVertex = null;
		ListIterator iter = vertices.listIterator(0);
		for(int i = 0; i < vertices.size(); ++i){
			prevVertex = currentVertex;
			currentVertex = (Point) iter.next();
			g.fillOval(currentVertex.x - parent.NODE_RADIUS, currentVertex.y - parent.NODE_RADIUS, 2*parent.NODE_RADIUS, 2*parent.NODE_RADIUS);
			g.drawString(""+(i+1), currentVertex.x + 2*parent.NODE_RADIUS, currentVertex.y);
		}
		ListIterator edgeiter = edges.listIterator(0);
		for(int j = 0; j < edges.size(); ++j){
			Point[] points = (Point[])edgeiter.next();
			Point start = points[0];
			Point end = points[1];
			g.drawLine(start.x, start.y, end.x, end.y);
			g.drawString("w= "+edgeWeights.get(j), ((start.x + end.x)/2)+10, ((start.y + end.y)/2)+10);

		}


	}
}