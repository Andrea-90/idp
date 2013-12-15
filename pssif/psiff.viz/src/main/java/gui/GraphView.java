package gui;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.model.ConnectionType;
import graph.model.MyEdge;
import graph.model.MyNode;
import graph.model.NodeType;
import gui.graph.GraphVisualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GraphView {
	private JPanel parent;
	private GraphVisualization graph;
	
	private JLabel nodename;
	private JLabel nodetype;
	private JList<String> nodeattributes;
	private JCheckBox nodeDetails;
	private JButton nodeHighlight;
	private JButton collapseExpand;
	private JButton typeFilter;
	
	private Dimension screenSize;
	
	public GraphView()
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) (screenSize.width*0.9);
		int y = (int) (screenSize.height*0.9);
		if (nodeDetails==null)
			graph = new GraphVisualization(new Dimension(x,y),true);
		else
			graph = new GraphVisualization(new Dimension(x,y),nodeDetails.isSelected());
		
		addNodeChangeListener();
	}
	
	public JPanel getGraphPanel()
	{
		int betweenComps =15;
		int betweenLabelandComp=10;
		
		parent = new JPanel();
        parent.setLayout(new BorderLayout());
		
		JPanel graphpanel = new JPanel();
		graphpanel.setBackground(Color.YELLOW);
		
		
		VisualizationViewer<MyNode, MyEdge> vv = graph.getVisualisationViewer();
		
		graphpanel.add(vv);
		
		parent.add(graphpanel,BorderLayout.CENTER);
		
		JPanel information = new JPanel();
		information.setBackground(Color.GREEN);
		int x = (int) (screenSize.width*0.1);
		int y = (int) (screenSize.height);
		Dimension d = new Dimension(x,y);
		information.setMaximumSize(d);
		information.setMinimumSize(d);
		information.setPreferredSize(d);
		information.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		
		int i = -1;
		
		parent.add(information, BorderLayout.EAST);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		JLabel lblNodeName = new JLabel("Node Name");
		c.gridy = (i++);
		information.add(lblNodeName,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		
		nodename = new JLabel("");
		c.gridy = (i++);
		information.add(nodename,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		JLabel lblNodeType = new JLabel("Node Type");
		c.gridy = (i++);
		information.add(lblNodeType,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		nodetype = new JLabel("");
		c.gridy = (i++);
		information.add(nodetype,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		JLabel lblNodeAttributes = new JLabel("Node Attributes");
		c.gridy = (i++);
		information.add(lblNodeAttributes,c);
		
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenLabelandComp),c);
		
		
		nodeattributes = new JList<String>();
		String[] a = new String[]{};
		
		nodeattributes.setListData(a);
		nodeattributes.setVisibleRowCount(5);
		int scrolly = (int)( y*0.1);

		JScrollPane jScrollPane = new JScrollPane(nodeattributes);
		//jScrollPane.setViewportView(nodeattributes);
		jScrollPane.setPreferredSize(new Dimension(x, scrolly));
		jScrollPane.setMaximumSize(new Dimension(x, scrolly));
		jScrollPane.setMinimumSize(new Dimension(x, scrolly));
		
		c.gridy = (i++);
		information.add(jScrollPane,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		JLabel lblVisDetails = new JLabel("Visualization Details");
		c.gridy = (i++);
		information.add(lblVisDetails,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		nodeDetails = new JCheckBox();
		nodeDetails.setText("Node Details");
		c.gridy = (i++);
		information.add(nodeDetails,c);
		nodeDetails.setSelected(true);
		graph.setNodeVisualisation(true);
		nodeDetails.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent item) {			
				JCheckBox parent = (JCheckBox)item.getSource();
				if (parent.isSelected())
				{
					graph.setNodeVisualisation(true);
					// checkbox selected
					System.out.println("Test");
				}
				else
				{
					// checkbox not selected
					System.out.println("Test2");
					graph.setNodeVisualisation(false);
				}
				
			}
		});
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		nodeHighlight = new JButton("Select Highlighted Nodes");
		nodeHighlight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseHighlightNodes();
			}
		});
		c.gridy = (i++);
		information.add(nodeHighlight,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		collapseExpand = new JButton("Collapse/Expand Node");
		collapseExpand.setEnabled(false);
		collapseExpand.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (graph.isExpandable())
				{
					graph.ExpandNode(nodeDetails.isSelected());
					collapseExpand.setText("Collapse Node");
				}
				
				if (graph.isCollapsable())
				{
					graph.collapseNode();
					collapseExpand.setText("Expand Node");
				}
			}
		});
		c.gridy = (i++);
		information.add(collapseExpand,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		typeFilter = new JButton("Select Node & Edge Types");
		typeFilter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseEdgeAndNodeTypes();
			}
		});
		c.gridy = (i++);
		information.add(typeFilter,c);
		c.gridy = (i++);
		information.add(Box.createVerticalStrut(betweenComps),c);
		
		
		
		
		return parent;
	}

	public GraphVisualization getGraph() {
		return graph;
	}
	
	public void resetGraph()
	{
		FRLayout<MyNode, MyEdge> l = new FRLayout<MyNode, MyEdge>(graph.getGraph());
    	graph.getVisualisationViewer().setGraphLayout(l);
    	
    	graph.getVisualisationViewer().repaint();
	}
	
	private void updateSidebar(String nodeName, NodeType nodeType, List<String> attributes)
	{
		if (nodeName==null)
			this.nodename.setText("");
		else
			this.nodename.setText(nodeName);
		if (nodeType==null)
			this.nodetype.setText("");
		else
			this.nodetype.setText(nodeType.getName());
		if(attributes==null)
		{
			String[] s = new String[0];
			this.nodeattributes.setListData(s);
		}
		else
		{
			String[] s = new String[attributes.size()];
			
			int counter=0;
			for (String a : attributes)
			{
				s[counter]=a;
				counter++;
			}
			this.nodeattributes.setListData(s);
		}
		
		if (graph.isCollapsable())
		{
			collapseExpand.setText("Collapse Node");
		}
		
		if (graph.isExpandable())
		{
			collapseExpand.setText("Expand Node");
		}
		
		if (!graph.isCollapsable() && !graph.isExpandable())
		{
			collapseExpand.setText("Collapse/Expand Node");
			collapseExpand.setEnabled(false);
		}
		else
			collapseExpand.setEnabled(true);
		
	}
	
	private void addNodeChangeListener()
	{
		final PickedState<MyNode> pickedState = graph.getVisualisationViewer().getPickedVertexState();

		pickedState.addItemListener(new ItemListener() {

		    @Override
		    public void itemStateChanged(ItemEvent e) {
		        Object subject = e.getItem();

		        if (subject instanceof MyNode) {
		        	MyNode vertex = (MyNode) subject;
		            if (pickedState.isPicked(vertex)) 
		              updateSidebar(vertex.getName(), vertex.getNodeType(), vertex.getAttributes());
		            else
		            	updateSidebar(null, null,null);
		        }
		        else
		        	updateSidebar(null, null,null);
		        
		    }
		});
		Set<MyNode> s =pickedState.getPicked();
		if (s==null)
		{
			updateSidebar(null, null,null);
		}
	}
	
	private void chooseHighlightNodes()
	{
		ConnectionType[] edgePossibilities = ConnectionType.values();
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		final JPanel EdgePanel = new JPanel(new GridLayout(0, 1));
		
		LinkedList<ConnectionType> already = graph.getHighlightNodes();
		int allcounter =0;
		for (ConnectionType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (already!=null && already.contains(attr))
			{
				choice.setSelected(true);
				allcounter++;
			}
			else
				choice.setSelected(false);
			EdgePanel.add(choice);
		}
		
		
		JScrollPane scrollEdges = new JScrollPane(EdgePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		final JCheckBox selectAllEdges = new JCheckBox("Select all Edge Types");
	    

		selectAllEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
	        if (selectAllEdges.isSelected())
	        {
	          Component[] attr = EdgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(true);
	            }
	          }
	        }
	        else
	        {
	          Component[] attr = EdgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(false);
	            }
	          }
	        }
	      }
	    });
	    
	    // set Select all edges Checkbox to true if all where selected
	    if (allcounter!=0 && allcounter ==already.size() )
	    	selectAllEdges.setSelected(true);
		
		
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);

		c.gridx = 1;
		c.gridy = 1;
		allPanel.add(scrollEdges,c);
		
		c.gridx = 1;
		c.gridy = 2;
		allPanel.add(selectAllEdges,c);
		
		JComponent[] inputs = new JComponent[] {
				allPanel
    	};
		
		LinkedList<ConnectionType> res = new LinkedList<ConnectionType>();
		
		allPanel.setPreferredSize(new Dimension(200,500));
		allPanel.setMaximumSize(new Dimension(200,500));
		allPanel.setMinimumSize(new Dimension(200,500));

		int dialogResult = JOptionPane.showConfirmDialog(null, inputs, "Choose the highlighted Edge types", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		
    		// get all the values of the edges
    		Component[] attr = EdgePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		System.out.println("test");
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			System.out.print(a.getText()+"  ");
        			System.out.println(a.isSelected());
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 System.out.println(a.getText());
        				 ConnectionType b = ConnectionType.getValue(a.getText());
        				 res.add(b);
        			 }
        				
        		}	
        	}
    	}
    	graph.setHighlightNodes(res);

	}
	
	private void chooseEdgeAndNodeTypes()
	{
		ConnectionType[] edgePossibilities = ConnectionType.values();
		NodeType[] nodePossibilities = NodeType.values();
		
		
		JPanel allPanel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		final JPanel NodePanel = new JPanel(new GridLayout(0, 1));
		
		int nodecounter=0;
		for (NodeType attr : nodePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (graph.getVizNodes().contains(attr))
			{
				choice.setSelected(true);
				nodecounter++;
			}
			else
				choice.setSelected(false);
			NodePanel.add(choice);
		}
		
		final JPanel EdgePanel = new JPanel(new GridLayout(0, 1));
		
		int edgecounter =0;
		for (ConnectionType attr : edgePossibilities)
		{
			JCheckBox choice = new JCheckBox(attr.getName());
			if (graph.getVizEdges().contains(attr))
			{
				choice.setSelected(true);
				edgecounter++;
			}
			else
				choice.setSelected(false);
			EdgePanel.add(choice);
		}
		JScrollPane scrollNodes = new JScrollPane(NodePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollNodes.setPreferredSize(new Dimension(200, 400));
			    
	    JScrollPane scrollEdges = new JScrollPane(EdgePanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollEdges.setPreferredSize(new Dimension(200, 400));
		
		final JCheckBox selectAllNodes = new JCheckBox("Select all Node Types");
	    
	    selectAllNodes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
		      {
		        if (selectAllNodes.isSelected())
		        {
		          Component[] attr = NodePanel.getComponents();
		          for (Component tmp : attr) {
		            if ((tmp instanceof JCheckBox))
		            {
		              JCheckBox a = (JCheckBox)tmp;
		              
		              a.setSelected(true);
		            }
		          }
		        }
		        else
		        {
		          Component[] attr = NodePanel.getComponents();
		          for (Component tmp : attr) {
		            if ((tmp instanceof JCheckBox))
		            {
		              JCheckBox a = (JCheckBox)tmp;
		              
		              a.setSelected(false);
		            }
		          }
		        }
		      }
	    });
	    final JCheckBox selectAllEdges = new JCheckBox("Select all Edge Types");
	    
	    selectAllEdges.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
	      {
	        if (selectAllEdges.isSelected())
	        {
	          Component[] attr = EdgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(true);
	            }
	          }
	        }
	        else
	        {
	          Component[] attr = EdgePanel.getComponents();
	          for (Component tmp : attr) {
	            if ((tmp instanceof JCheckBox))
	            {
	              JCheckBox a = (JCheckBox)tmp;
	              
	              a.setSelected(false);
	            }
	          }
	        }
	      }
	    });
		
	    if (nodecounter== nodePossibilities.length)
	    	selectAllNodes.setSelected(true);
	    else
	    	selectAllNodes.setSelected(false);
	    
	    if (edgecounter== edgePossibilities.length)
	    	selectAllEdges.setSelected(true);
	    else
	    	selectAllEdges.setSelected(false);
	    
		c.gridx = 0;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Node Types"),c);
		c.gridx = 1;
		c.gridy = 0;
		allPanel.add(new JLabel("Choose Connection Types"),c);
	    c.gridx = 0;
	    c.gridy = 1;
	    allPanel.add(scrollNodes, c);
	    c.gridx = 1;
	    c.gridy = 1;
	    allPanel.add(scrollEdges, c);
	    c.gridx = 0;
	    c.gridy = 2;
	    allPanel.add(selectAllNodes, c);
	    c.gridx = 1;
	    c.gridy = 2;
	    allPanel.add(selectAllEdges, c);
		
		
		allPanel.setPreferredSize(new Dimension(400,500));
		allPanel.setMaximumSize(new Dimension(400,500));
		allPanel.setMinimumSize(new Dimension(400,500));
		
		int dialogResult = JOptionPane.showConfirmDialog(null, allPanel, "Choose the Edge and Node types", JOptionPane.DEFAULT_OPTION);
    	
    	if (dialogResult==0)
    	{
    		LinkedList<NodeType> selectedNodes = new LinkedList<NodeType>();
    		// get all the values of the Nodes
        	Component[] attr = NodePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 NodeType b = NodeType.getValue(a.getText());
        				 selectedNodes.add(b);
        			 }
        				
        		}	
        	}
        	
        	LinkedList<ConnectionType> selectedEdges = new LinkedList<ConnectionType>();
    		// get all the values of the edges
        	attr = EdgePanel.getComponents();       	
        	for (Component tmp :attr)
        	{
        		if ((tmp instanceof JCheckBox))
        		{
        			JCheckBox a = (JCheckBox) tmp;
        			
        			// compare which ones where selected
        			 if (a.isSelected())
        			 {
        				 ConnectionType b = ConnectionType.getValue(a.getText());
        				 selectedEdges.add(b);
        			 }
        				
        		}	
        	}
    		
        	graph.applyNodeAndEdgeFilter(selectedNodes, selectedEdges);
	}
	}
	
}
