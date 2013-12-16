package gui;

import graph.model.NodeType;
import gui.graph.MyListColorRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Model;

public class Main {
	
	private static MatrixView matrix;
	private static JFrame frame;
	private static GraphView graphView;
	
	public static void main(String[] args) {
		
		Model m = new Model();
		m.MockData();

		matrix = new MatrixView();
		graphView = new GraphView();

		frame = new JFrame("Product Service Integration - Integration Framework ---- Visualisation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Standart start with Graph
		frame.getContentPane().add(graphView.getGraphPanel());
		
		
		frame.setJMenuBar(createMenu());
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth()/4;
		int height = gd.getDisplayMode().getHeight()/4;
		
		width= width*3;
		height = height*3;
		
		frame.setPreferredSize(new Dimension(width, height));
		frame.setState(Frame.MAXIMIZED_BOTH);
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private static JMenuBar createMenu()
	{
		// PICKING or TRANSFORMING Mode
		JMenuBar menuBar = new JMenuBar();
		JMenu modeMenu = graphView.getGraph().getAbstractModalGraphMouse().getModeMenu(); // Obtain mode menu from the mouse
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null); 
		modeMenu.setPreferredSize(new Dimension(80,20));
		
		// Which Visualization
		JMenu visualisationMenu = new JMenu("Visualisation");
		JMenuItem graphItem = new JMenuItem("Graph");
		graphItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				frame.getContentPane().add(graphView.getGraphPanel());
				
				frame.pack();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
			}
		});
		visualisationMenu.add(graphItem);
		visualisationMenu.setIcon(null);
		
		
		JMenuItem matrixItem = new JMenuItem("Matrix");
		matrixItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				frame.getContentPane().add(matrix.getVisualization());
				
				
				frame.pack();
				//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				frame.repaint();
			}
		});
		visualisationMenu.add(matrixItem);
		visualisationMenu.setIcon(null);
		
		
		// Reset the Graph or Matrix
		JMenu resetMenu = new JMenu("Reset");
		JMenuItem resetGraph = new JMenuItem("Reset Graph");
		resetGraph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphView.resetGraph();
			}
		});
		resetMenu.add(resetGraph);
		JMenuItem resetMatrix = new JMenuItem("Reset Matrix");
		resetMatrix.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean res = matrix.chooseNodes();
				
				if (res)
				{
					frame.getContentPane().removeAll();
					frame.getContentPane().add(matrix.getVisualization());
					
					
					frame.pack();
					//frame.setExtendedState(Frame.MAXIMIZED_BOTH);
					frame.repaint();
				}
			}
		});
		resetMenu.add(resetMatrix);
		resetMenu.setIcon(null);
		
		// Color Options
		JMenu colorMenu = new JMenu("Colors");
		JMenuItem colorNodes = new JMenuItem("Node color");
		colorNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pickNodeColor();
			}
		});
		colorMenu.add(colorNodes);
		
		// Add all to the menuBar
		menuBar.add(modeMenu);
		menuBar.add(visualisationMenu);
		menuBar.add(resetMenu);
		menuBar.add(colorMenu);
		
		return menuBar;
	}
	
	
	private static void pickNodeColor()
	{
		JPanel bannerPanel = new JPanel(new GridLayout());;
 
       final JList<NodeType> list = new JList<NodeType>( NodeType.values() );  
        
       final MyListColorRenderer colorlistener = new MyListColorRenderer();
       colorlistener.setColors(graphView.getGraph().getNodeColorMapping());
       
        list.setCellRenderer(colorlistener);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane sp = new JScrollPane( list ); 
        bannerPanel.add(sp);
		
        //Set up color chooser for setting text color
        final JColorChooser tcc = new JColorChooser();
        tcc.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				NodeType type = list.getSelectedValue();
				Color newColor = tcc.getColor();
				 
				if (type!=null)
				{
					colorlistener.setColor(type, newColor);
					list.repaint();
				}
			}
		});
        
        bannerPanel.add(tcc);
        
        int dialogResult = JOptionPane.showConfirmDialog(null, bannerPanel, "Choose Nodes Colors", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		HashMap<NodeType, Color > colors = colorlistener.getColorMapping();
    		graphView.getGraph().setNodeColorMapping(colors);
    		
    	}
	}
}



  
