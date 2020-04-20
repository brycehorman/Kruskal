import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import java.lang.NumberFormatException;
public class Kruskal extends JFrame implements ActionListener, MouseListener{
	
	final int NODE_RADIUS = 5;
	private int count = 0;
	private JLabel label;
	private JFrame frame;
	private JPanel panel;
	private JPanel buttonPanel;
	private JPanel edgeButtons;
	private JButton clearButton;
	private JButton mstButton;
	private JButton addEdge;
	private CanvasPanel canvas;
	LinkedList<Point> vertices;
	LinkedList<Point[]> edges;
	private Point startVertex;
	private Point endVertex;
	private JTextField startText;
	private JTextField weightText;
	private JTextField endText;
	private JButton textButton;
	LinkedList<Integer> edgeWeights;


	public Kruskal(){
		//create a frame
		super("Kruskal");
		vertices = new LinkedList<>();
		edges = new LinkedList<>();
		edgeWeights = new LinkedList<>();
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
		contentPane.add(edgeButtons);
		
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
			startVertex = null;
			endVertex = null;
			canvas.repaint();
		}
		else if (buttonIdentifier.equals("computeMST")){

		}
		else if(buttonIdentifier.equals("addText")){
			String one = startText.getText();
			String two = endText.getText();
			String three = weightText.getText();
			if(isParsable(one) && isParsable(two) && isParsable(three)){
				setVertices(Integer.parseInt(one), Integer.parseInt(two));
				if(startVertex != endVertex){
					int weight = Integer.parseInt(three);
					Point[] points = {startVertex, endVertex};
					edgeWeights.add(weight);
					edges.add(points);
					startText.setText("");
					endText.setText("");
					weightText.setText("");
					canvas.repaint();
				}
				
			}
			
			
		}
	}

	public void mouseClicked(MouseEvent e) {
		Point click_point = e.getPoint();
		vertices.add(click_point);
		if(vertices.size() == 1){
			startVertex = click_point;
			endVertex = click_point;
		}
		canvas.repaint();
    }

    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

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
		edgeButtons = new JPanel();
		edgeButtons.setMinimumSize(panelSize);
		edgeButtons.setPreferredSize(panelSize);
		edgeButtons.setMaximumSize(panelSize);
		edgeButtons.setLayout(new BoxLayout(edgeButtons,
						    BoxLayout.X_AXIS));
		edgeButtons.
		    setBorder(BorderFactory.
			      createCompoundBorder(BorderFactory.
						   createLineBorder(Color.red),
						   edgeButtons.getBorder()));
    }
    public void createCanvas(){
		//create a canvas panel
		canvas = new CanvasPanel(this);
		canvas.addMouseListener(this);
		Dimension canvasSize = new Dimension(700,500);
		canvas.setMinimumSize(canvasSize);
		canvas.setPreferredSize(canvasSize);
		canvas.setMaximumSize(canvasSize);
		canvas.setBackground(Color.black);
    }
    public void addText(){
    	//make the clear button
    	Dimension buttonSize = new Dimension(175, 50);
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
		//make text box for start vertex of edge
		JLabel l1 = new JLabel("Start Vertex: ");
		startText = new JTextField(4);
		startText.setMinimumSize(buttonSize);
		startText.setPreferredSize(buttonSize);
		startText.setMaximumSize(buttonSize);
		startText.setAlignmentX(Component.CENTER_ALIGNMENT);
		//make text box for end vertex of edge
		JLabel l2 = new JLabel("End Vertex: ");
		endText = new JTextField(4);
		endText.setMinimumSize(buttonSize);
		endText.setPreferredSize(buttonSize);
		endText.setMaximumSize(buttonSize);
		endText.setAlignmentX(Component.CENTER_ALIGNMENT);
		//make text box for edge weight
		JLabel l3 = new JLabel("Weight: ");
		weightText = new JTextField(4);
		weightText.setMinimumSize(buttonSize);
		weightText.setPreferredSize(buttonSize);
		weightText.setMaximumSize(buttonSize);
		weightText.setAlignmentX(Component.CENTER_ALIGNMENT);
		//make button to create edge
		textButton = new JButton("Add Edge");
    	textButton.setMinimumSize(buttonSize);
		textButton.setPreferredSize(buttonSize);
		textButton.setMaximumSize(buttonSize);
		textButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		textButton.setActionCommand("addText");
		textButton.addActionListener(this);
		//add buttons to top button panel
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(clearButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(mstButton);
		buttonPanel.add(Box.createHorizontalGlue());
		//add buttons and text fields to lower button panel
		edgeButtons.add(Box.createHorizontalGlue());
		edgeButtons.add(l1);
		edgeButtons.add(startText);
		edgeButtons.add(Box.createHorizontalGlue());
		edgeButtons.add(l2);
		edgeButtons.add(endText);
		edgeButtons.add(Box.createHorizontalGlue());
		edgeButtons.add(l3);
		edgeButtons.add(weightText);
		edgeButtons.add(Box.createHorizontalGlue());
		edgeButtons.add(textButton);
		edgeButtons.add(Box.createHorizontalGlue());
    }
    
    public void setVertices(int one, int two){
    	ListIterator iter = vertices.listIterator(0);
    	if(one <= vertices.size() && one > 0 && two <= vertices.size() && two > 0){
    		for(int i = 0; i < vertices.size(); i++){
    		Point next = (Point)iter.next();
    		if(i == one-1){
    			startVertex = next;
    			if(one == two){
    				endVertex = next;
    			}
    		}
    		else if(i == two-1){
    			endVertex = next;
    			if(one == two){
    				startVertex = next;
    			}
    		}
    	}
    	}
    	
    }
	public boolean isParsable(String input){
	    try{
	        Integer.parseInt(input);
	        return true;
	    }catch(NumberFormatException e){
	        return false;
	    }
	}
}