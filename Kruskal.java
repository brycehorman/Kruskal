import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import java.lang.NumberFormatException;
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
	private JButton deleteButton;
	private JTextField enterWeight;
	private CanvasPanel canvas;
	LinkedList<Point> vertices;
	LinkedList<Point[]> edges;
	LinkedList<Integer> edgeWeights;
	Point selectedV1;
	Point clickedV1;
	Point clickedV2;
	Point[] selectedE1;
	Point[] clickedE1;

	enum State{
		NODE, ADD_EDGE_1, ADD_EDGE_2, DELETE;
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
		onVertex = false;
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
		else if(buttonIdentifier.equals("deleteButton")){
			if(state != State.DELETE){
				state = State.DELETE;
				selectedV1 = null;
				clickedV1 = null;
				canvas.repaint();
			}
		}
		
	}

	public void mouseClicked(MouseEvent e) {
		Point click_point = e.getPoint();
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
				String w = enterWeight.getText();
				if(isParsable(w)){
					edgeWeights.add(Integer.parseInt(w));
				}
				else
					edgeWeights.add(0);
			}
			else{
				clickedV1 = null;
			}
			


		}
		else if (state == State.NODE){
			vertices.add(click_point);
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
    	Point mousePoint = e.getPoint();
    	Point v1 = mouseonPoint(mousePoint);
    	Point[] e1 = mouseonEdge(mousePoint);
    	if(v1 != null && (state == State.ADD_EDGE_1 || state == State.ADD_EDGE_2 || state == State.DELETE)){
    		selectedV1 = v1;
    		onVertex = true;
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
    	Dimension buttonSize = new Dimension(100, 50);
    	clearButton = new JButton("Clear");
    	clearButton.setMinimumSize(buttonSize);
		clearButton.setPreferredSize(buttonSize);
		clearButton.setMaximumSize(buttonSize);
		clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(this);
		//make the mst button
		mstButton = new JButton("Compute MST");
    	mstButton.setMinimumSize(buttonSize);
		mstButton.setPreferredSize(buttonSize);
		mstButton.setMaximumSize(buttonSize);
		mstButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		mstButton.setActionCommand("computeMST");
		mstButton.addActionListener(this);
		//make the add edge button
    	addEdge = new JButton("Edge Mode");
    	addEdge.setMinimumSize(buttonSize);
		addEdge.setPreferredSize(buttonSize);
		addEdge.setMaximumSize(buttonSize);
		addEdge.setAlignmentX(Component.CENTER_ALIGNMENT);
		addEdge.setActionCommand("addEdge");
		addEdge.addActionListener(this);
		//make the node button
		addNode = new JButton("Node Mode");
    	addNode.setMinimumSize(buttonSize);
		addNode.setPreferredSize(buttonSize);
		addNode.setMaximumSize(buttonSize);
		addNode.setAlignmentX(Component.CENTER_ALIGNMENT);
		addNode.setActionCommand("addNode");
		addNode.addActionListener(this);
		//Make delete button
		deleteButton = new JButton("Delete Mode");
    	deleteButton.setMinimumSize(buttonSize);
		deleteButton.setPreferredSize(buttonSize);
		deleteButton.setMaximumSize(buttonSize);
		deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteButton.setActionCommand("deleteButton");
		deleteButton.addActionListener(this);
		//make weight text field
		enterWeight = new JTextField();
		enterWeight.setMinimumSize(buttonSize);
		enterWeight.setPreferredSize(buttonSize);
		enterWeight.setMaximumSize(buttonSize);
		enterWeight.setAlignmentX(Component.CENTER_ALIGNMENT);
		//add label for weight
		JLabel weightText = new JLabel("Input weight here: ");
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
		buttonPanel.add(deleteButton);
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
			double m = (e1.y - e2.y) / (e1.x - e2.x);
			if(p.y - e1.y == m*(p.x - e1.x)){
				return e;
			}
		}
		return null;
	}
}