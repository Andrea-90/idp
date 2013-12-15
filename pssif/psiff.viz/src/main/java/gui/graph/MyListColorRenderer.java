package gui.graph;

import graph.model.NodeType;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MyListColorRenderer extends DefaultListCellRenderer  
	{  
	private static final long serialVersionUID = 1L;
	
		//private HashMap theChosen = new HashMap();
	    private HashMap<NodeType, Color> colorMapper;

	    public Component getListCellRendererComponent( JList list,  
	    		Object value, int index, boolean isSelected,  
	            boolean cellHasFocus )  
	    {  
	        super.getListCellRendererComponent( list, value, index,  
	                isSelected, cellHasFocus );  
	        
	       /* if( isSelected )  
	        {  
	            theChosen.put( value, "chosen" );  
	        }  */
	        
	        //System.out.println("getListCellRendererComponent ");
	        
	        NodeType t = (NodeType) value;
	       // System.out.println(t);
	        if( colorMapper.containsKey( t ) )  
	        {  
	            Color c = colorMapper.get(t);
	           // System.out.println("Color "+c);
	            
	            setBackground(c);
	        }  


	        return( this );  
	    }
	    
	    public void setColor (NodeType type , Color c)
	    {
	    	 System.out.println("Put Color "+c);
	    	this.colorMapper.put(type, c);
	    }
	    
	    public HashMap<NodeType, Color> getColorMapping()
	    {
	    	return this.colorMapper;
	    }
	    
	    public MyListColorRenderer()
	    {
	    	super();
	    	colorMapper = new HashMap<NodeType, Color>();
	    	
	    }
	    
	    public void setColors(HashMap<NodeType,Color> map)
	    {
	    	this.colorMapper = map;	
	    }
}
