import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import java.lang.NumberFormatException;
import java.awt.geom.Line2D;
public class Kruskal extends JFrame implements ActionListener, MouseListener, MouseMotionListener{
	
	final int NODE_RADIUS = 5;
	private int count = 0;
	private JLabel label;
	private JFrame frame;
	private JPanel panel;
	private JPanel buttonPanel;
	private JButton clearButton;
	private JButton mstButton;
	private JButton addEdge;
	private JButton addNode;
	private JButton delete;
	private JTextField enterWeight;
	private JButton editWeight;
	private CanvasPanel canvas;
	LinkedList<Point> vertices;
	LinkedList<Point[]> edges;
	LinkedList<Integer> edgeWeights;
	Point selectedV1;
	Point clickedV1;
	Point clickedV2;
	Point[] selectedE1;
	Point[] clickedE1;
	Graph graph; 
	LinkedList<Point[]> theMST;
	

	enum State{
		NODE, ADD_EDGE_1, ADD_EDGE_2, DELETE, EDIT, MST;
	}
	State state;
	private boolean onVertex;
	private boolean onEdge;

	public Kruskal(){
		//create a frame
		super("Kruskal");
		vertices = new LinkedList<>();
		edges = new LinkedList<>();
		edgeWeights = new LinkedList<>();
		graph = new Graph(vertices.size(), edges.size());
		theMST = new LinkedList<>();
		onVertex = false;
		onEdge = false;
		selectedV1 = null;
		clickedV1 = null;
		clickedV2 = null;
		selectedE1 = null;
		state = State.NODE;
		//create a content pane
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		//create the canvas
		createCanvas();
		//Create the two button panels
		createPanels();
		//create buttons and text fields and add them to panel
		addText();
		//add the panels and canvas to the frame
		
		contentPane.add(canvas);
		contentPane.add(buttonPanel);
		
	}

	public static void main(String[] args){
		Kruskal project = new Kruskal();
		project.addWindowListener(new WindowAdapter(){
			public void
				windowClosing(WindowEvent e){
					System.exit(0);
				}
		});
		project.pack();
		project.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		String buttonIdentifier = e.getActionCommand();
		if(buttonIdentifier.equals("clear")){
			vertices.clear();
			edges.clear();
			edgeWeights.clear();
			selectedV1 = null;
			clickedV1 = null;
			state = State.NODE;
			enterWeight.setText("");
			canvas.repaint();
		}
		else if (buttonIdentifier.equals("computeMST")){
			if(state != State.MST){
				state = State.MST;
				canvas.repaint();
			}

		}
		else if (buttonIdentifier.equals("addEdge")){
			if(state != State.ADD_EDGE_1 || state != State.ADD_EDGE_2){
				state = State.ADD_EDGE_1;
				canvas.repaint();
			}
		}
		else if(buttonIdentifier.equals("addNode")){
			if(state != State.NODE){
				state = State.NODE;
				selectedV1 = null;
				clickedV1 = null;
				canvas.repaint();
			}
		}

		else if(buttonIdentifier.equals("delete")){
			if(state != State.DELETE){
				state = State.DELETE;
				selectedV1 = null;
				clickedV1 = null;
				selectedE1 = null;
				clickedE1 = null;
				canvas.repaint();
			}
		}
		else if(buttonIdentifier.equals("editWeight")){
			if(state != State.EDIT){
				state = State.EDIT;
				selectedE1 = null;
				clickedE1 = null;
				canvas.repaint();
			}
		}
	
	}

	public void mouseClicked(MouseEvent e) {
		Point click_point = e.getPoint();
		String w = "";
		int count = 0;
		if(onVertex && state == State.DELETE){
			vertices.remove(selectedV1);
		}
		else if(onVertex && state == State.ADD_EDGE_1){
			state = State.ADD_EDGE_2;
			clickedV1 = selectedV1;
		}
		else if(onVertex && state == State.ADD_EDGE_2){
			state = State.ADD_EDGE_1;
			if(clickedV1 != selectedV1){
				clickedV2 = selectedV1;
				Point[] points = {clickedV1, clickedV2};
				edges.add(points);
				graph.edge[count].from = clickedV1;
				graph.edge[count].to = clickedV2;
				count++;
				w = enterWeight.getText();
				if(isParsable(w)){
					edgeWeights.add(Integer.parseInt(w));
					graph.edge[count].weight = Integer.parseInt(w);
				}
				else
					edgeWeights.add(1);
					graph.edge[count].weight = 1;
			}
			else{
				clickedV1 = null;
			}
		}
		else if (state == State.NODE){
			vertices.add(click_point);
		}
		else if(onEdge && state == State.DELETE){
			edges.remove(selectedE1);
			edgeWeights.remove(edges.indexOf(selectedE1));
		}
		else if(onEdge && state == State.EDIT){
			if(clickedE1 != selectedE1){
			String w2 = enterWeight.getText();
				if(isParsable(w2)){
					edgeWeights.remove(edges.indexOf(selectedE1));
					edgeWeights.add(edges.indexOf(selectedE1), Integer.parseInt(w2));
				}
			}
			else
				clickedE1 = null;
		}
		else if(state == State.MST){
			theMST = graph.getMST();
		}
		canvas.repaint();
    }

    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e){
    	if(onVertex){
    		selectedV1 = null;
    		onVertex = false;
    		canvas.repaint();
		}
		else if(onEdge){
			selectedE1 = null;
			onEdge = false;
			canvas.repaint();
		}
    	Point mousePoint = e.getPoint();
    	Point v1 = mouseonPoint(mousePoint);
    	Point[] e1 = mouseonEdge(mousePoint);
    	if(v1 != null && (state == State.ADD_EDGE_1 || state == State.ADD_EDGE_2 || state == State.DELETE)){
    		selectedV1 = v1;
    		onVertex = true;
    		canvas.repaint();
		}
		if(e1 != null && (state == State.DELETE || state == State.EDIT)){
			selectedE1 = e1;
			onEdge = true;
			canvas.repaint();
		}
    }

    public void createPanels(){
    	buttonPanel = new JPanel();
		Dimension panelSize = new Dimension(700, 75);
		buttonPanel.setMinimumSize(panelSize);
		buttonPanel.setPreferredSize(panelSize);
		buttonPanel.setMaximumSize(panelSize);
		buttonPanel.setLayout(new BoxLayout(buttonPanel,
						    BoxLayout.X_AXIS));
		buttonPanel.
		    setBorder(BorderFactory.
			      createCompoundBorder(BorderFactory.
						   createLineBorder(Color.red),
						   buttonPanel.getBorder()));
		
    }
    public void createCanvas(){
		//create a canvas panel
		canvas = new CanvasPanel(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		Dimension canvasSize = new Dimension(700,500);
		canvas.setMinimumSize(canvasSize);
		canvas.setPreferredSize(canvasSize);
		canvas.setMaximumSize(canvasSize);
		canvas.setBackground(Color.black);
    }
    public void addText(){
    	//make the clear button
		Dimension buttonSize = new Dimension(90, 50);
		Dimension buttonSize2 = new Dimension(40, 30);
		clearButton = new JButton("Clear");
		clearButton.setFont(new Font("Arial", Font.PLAIN, 12));
    	clearButton.setMinimumSize(buttonSize);
		clearButton.setPreferredSize(buttonSize);
		clearButton.setMaximumSize(buttonSize);
		clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(this);

		//make the mst button
		mstButton = new JButton("Compute MST");
		mstButton.setFont(new Font("Arial", Font.PLAIN, 12));
    	mstButton.setMinimumSize(buttonSize);
		mstButton.setPreferredSize(buttonSize);
		mstButton.setMaximumSize(buttonSize);
		mstButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		mstButton.setActionCommand("computeMST");
		mstButton.addActionListener(this);

		//make the add edge button
		addEdge = new JButton("Edge Mode");
		addEdge.setFont(new Font("Arial", Font.PLAIN, 12));
    	addEdge.setMinimumSize(buttonSize);
		addEdge.setPreferredSize(buttonSize);
		addEdge.setMaximumSize(buttonSize);
		addEdge.setAlignmentX(Component.CENTER_ALIGNMENT);
		addEdge.setActionCommand("addEdge");
		addEdge.addActionListener(this);

		//make the node button
		addNode = new JButton("Node Mode");
		addNode.setFont(new Font("Arial", Font.PLAIN, 12));
    	addNode.setMinimumSize(buttonSize);
		addNode.setPreferredSize(buttonSize);
		addNode.setMaximumSize(buttonSize);
		addNode.setAlignmentX(Component.CENTER_ALIGNMENT);
		addNode.setActionCommand("addNode");
		addNode.addActionListener(this);

		// Make delete button
		delete = new JButton("Delete");
		delete.setFont(new Font("Arial", Font.PLAIN, 12));
    	delete.setMinimumSize(buttonSize);
		delete.setPreferredSize(buttonSize);
		delete.setMaximumSize(buttonSize);
		delete.setAlignmentX(Component.CENTER_ALIGNMENT);
		delete.setActionCommand("delete");
		delete.addActionListener(this);

		// Make edit weight button
		editWeight = new JButton("Edit Weight");
		editWeight.setFont(new Font("Arial", Font.PLAIN, 12));
		editWeight.setMinimumSize(buttonSize);
		editWeight.setPreferredSize(buttonSize);
		editWeight.setMaximumSize(buttonSize);
		editWeight.setAlignmentX(Component.CENTER_ALIGNMENT);
		editWeight.setActionCommand("editWeight");
		editWeight.addActionListener(this);

		//make weight text field
		enterWeight = new JTextField();
		enterWeight.setMinimumSize(buttonSize2);
		enterWeight.setPreferredSize(buttonSize2);
		enterWeight.setMaximumSize(buttonSize2);
		enterWeight.setAlignmentX(Component.CENTER_ALIGNMENT);
		//add label for weight
		JLabel weightText = new JLabel("Input weight: ");
		weightText.setFont(new Font("Arial", Font.PLAIN, 12));
		
		//add buttons to top button panel
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(clearButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(mstButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(addEdge);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(addNode);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(delete);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(editWeight);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(weightText);
		buttonPanel.add(enterWeight);
		buttonPanel.add(Box.createHorizontalGlue());
    }
    
	public boolean isParsable(String input){
	    try{
	        Integer.parseInt(input);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}

	public Point mouseonPoint(Point p){
		ListIterator iter = vertices.listIterator(0);
		for(int i = 0; i < vertices.size(); i++){
			Point v1 = (Point)iter.next();
			if(p.x > v1.x - NODE_RADIUS && p.x < v1.x + NODE_RADIUS && 
				p.y > v1.y - NODE_RADIUS && p.y < v1.y + NODE_RADIUS){
				return v1;
			}
		}
		return null;
	}
	public Point[] mouseonEdge(Point p){
		ListIterator iter = edges.listIterator(0);
		for(int i = 0; i < edges.size(); i++){
			Point[] e = (Point[])iter.next();
			Point e1 = e[0];
			Point e2 = e[1];
			
			// distance from line to mouse point
			double distance = Line2D.ptSegDist(e1.x, e1.y, e2.x, e2.y, p.x, p.y);
			
			// if close to mouse point
			if (distance < 3) {
				return e;
			}
		}
		return null;
	}



	class Graph{ 

        // Class to represent an edge in graph
        class Edge implements Comparable<Edge>{ 
			Point from, to;
            int weight; 
    
            // Comparator for sorting edges by weight 
            public int compareTo(Edge compareEdge) 
            { 
                return this.weight - compareEdge.weight; 
            } 
        }; 
    
        // Class to represent a subset for union-find 
        class Subset{ 
			int rank; 
			Point parent;
        }; 
    
        int V, E;    
        Edge edge[]; 
    
        // Creates a graph with V vertices and E edges 
        Graph(int v, int e){ 
            V = v; 
            E = e; 
            edge = new Edge[E]; 
            for (int i = 0; i < e; ++i){
                edge[i] = new Edge(); 
            }
        } 
    
        // Finds set of an element 
        Point find(Subset subsets[], int i) 
        { 
            if (subsets[i].parent != i){
                subsets[i].parent = find(subsets, subsets[i].parent); 
            }
    
            return subsets[i].parent; 
        } 
    
        // Finds union of two sets 
        void Union(Subset subsets[], int x, int y){ 
            int xroot = find(subsets, x); 
            int yroot = find(subsets, y); 
    
            // Put smaller rank tree under root of high rank tree 
            if (subsets[xroot].rank < subsets[yroot].rank) 
                subsets[xroot].parent = yroot; 
            else if (subsets[xroot].rank > subsets[yroot].rank) 
                subsets[yroot].parent = xroot; 
    
            // If same rank, then make one as root and increment by one
            else
            { 
                subsets[yroot].parent = xroot; 
                subsets[xroot].rank++; 
            } 
        } 
    
        // Find MST using Kruskal's algorithm 
        LinkedList<Point[]> getMST() {

            Edge result[] = new Edge[V];  
            int j = 0;  
            int i = 0; 

            for (i = 0; i < V; ++i){
                result[i] = new Edge();
            } 
    
            // Sort all edges in non-decreasing order by weight
            Arrays.sort(edge); 
    
            Subset subsets[] = new Subset[V]; 
            for(i = 0; i < V; ++i){
                subsets[i] = new Subset(); 
            }
    
            // Create V subsets with single elements 
            for (int v = 0; v < V; ++v) 
            { 
                subsets[v].parent = v; 
                subsets[v].rank = 0; 
            } 
    
            i = 0;  
    
            while (j < V - 1){ 
                Edge next = new Edge(); 
                next = edge[i++]; 
    
                int x = find(subsets, next.from); 
                int y = find(subsets, next.to); 
    
                // If edge does't cause cycle, include in result 
                if (x != y){ 
                    result[j++] = next; 
                    Union(subsets, x, y); 
                } 
            } 
			
			LinkedList<Point[]> mst = new LinkedList<Point[]>();
			Point[] mstPoints = new Point[j];
			for (i = 0; i < j; ++i){
				mstPoints[0] = result[i].from;
				mstPoints[1] = result[i].to;
				mst.add(mstPoints);
			}

			return mst;
			
        }

    }
    
	
}
