package graph.model2;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tum.pssif.core.PSSIFConstants;
import de.tum.pssif.core.metamodel.Attribute;
import de.tum.pssif.core.metamodel.DataType;
import de.tum.pssif.core.metamodel.PrimitiveDataType;
import de.tum.pssif.core.model.Node;
import de.tum.pssif.core.metamodel.NodeType;
import de.tum.pssif.core.util.PSSIFOption;
import de.tum.pssif.core.util.PSSIFValue;


public class MyNode2{
	private Node node;
	private String name;
	private int size;
	private MyNodeType type;
	private boolean detailedOutput;
	
	private static int limit = 15;
	private static int heightlimit = 2;
	
	
	public MyNode2(Node node, MyNodeType type) {
		this.node = node;
		this.type = type;
		
		Attribute nodeName = type.getType().findAttribute(PSSIFConstants.BUILTIN_ATTRIBUTE_NAME);
		
		if (nodeName.get(node)!=null)
		{
			PSSIFValue value = nodeName.get(node).getOne();
			name = value.asString();
			
		}
		else
			name ="Name not available";
	}
	
	private List<String> calcAttr()
	{
		List<String> attributes = new LinkedList<String>();
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			
			PSSIFValue value=null;
			
			if (current.get(node)!=null && current.get(node).isOne())
				value = current.get(node).getOne();
			
			String attrValue="";
			if (value !=null)
				attrValue = String.valueOf(value.getValue());
			String attrUnit = current.getUnit().getName();
			
			String res;
			
			if (attrUnit.equals("none"))
				res = attrName+" = "+attrValue+" : "+((PrimitiveDataType)current.getType()).getName();
			else
				res = attrName+" = "+attrValue+" in "+attrUnit+ " : "+((PrimitiveDataType)current.getType()).getName();
			
			if (current.get(node)!=null && attrValue.length()>0)
				attributes.add(res);
		}
		
		return attributes;
	}
	
	public LinkedList<LinkedList<String>> getAttributes()
	{
		LinkedList<LinkedList<String>> attributes = new LinkedList<LinkedList<String>>();
		
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			LinkedList<String> currentAttr = new LinkedList<String>();
			
			String attrName = current.getName();
			
			currentAttr.add(attrName);
			
			PSSIFValue value=null;
			
			if (current.get(node)!=null && current.get(node).isOne())
				value = current.get(node).getOne();
			
			String attrValue="";
			if (value !=null)
				attrValue = String.valueOf(value.getValue());
			
			currentAttr.add(attrValue);
			String attrUnit = current.getUnit().getName();
			currentAttr.add(attrUnit);
			
			currentAttr.add(((PrimitiveDataType)current.getType()).getName());
			
			attributes.add(currentAttr);
		}
		
		return attributes;
	}
	
	public boolean updateAttribute(String attributeName, Object value)
	{		
		DataType attrType = type.getType().findAttribute(attributeName).getType();
		
		if (attrType.equals(PrimitiveDataType.BOOLEAN))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.BOOLEAN.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
				
				return true;
			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.DATE))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.DATE.fromObject(value);
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
				
				return true;
			}
			catch (IllegalArgumentException e)
			{
				System.out.println(e.getMessage());
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.DECIMAL))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.DECIMAL.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
				
				return true;
			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.INTEGER))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.INTEGER.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
				
				return true;
			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		if (attrType.equals(PrimitiveDataType.STRING))
		{
			try 
			{
				PSSIFValue res = PrimitiveDataType.STRING.fromObject(value);
				
				type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(res));
				
				return true;
			}
			catch (IllegalArgumentException e)
			{
				return false;
			}
		}
		
		type.getType().findAttribute(attributeName).set(node, PSSIFOption.one(PSSIFValue.create(value)));
		return true;
	}
	
	public HashMap<String, Attribute> getAttributesHashMap()
	{
		 HashMap<String, Attribute> res = new  HashMap<String, Attribute>();
		
		Collection<Attribute> attr = type.getType().getAttributes();
		
		for (Attribute current : attr)
		{
			String attrName = current.getName();
			
			res.put(attrName, current);
		}
		
		return res;
	}

	
	/*private void setSize()
	{
		int temp = name.length() / limit;
		
		if (temp >0)
			size = temp;
		
		
		for (String s : attributes)
		{
			temp = s.length() / limit;
			
			if (temp >0 && temp > size)
				size = temp;
		}
		
		if (attributes.size() > heightlimit)
		{
			if (size == 0)
				size =2;
		}
			
	
	}*/
	
	public void update()
	{
		//setSize();
	}
	
	/**
	 * Get all the Informations about the Node. Should only be used in the GraphVisualization
	 * @return a HTML String with all the node informations
	 */
	public String getNodeInformations()
	{
		String output="";
		if (detailedOutput)
		{
			output ="<table border=\"0\">";
			output+=" <tr> ";
			output+= "<th> <h3>&lt;&lt; "+type.getName()+" >> <br>"+name+"</h3> </th>";
			output+=  " </tr> ";
			output+=" <tr> ";
			output+= "<td> <b>Attributes </b></td>";
			output+=  " </tr> ";
			for (String s : calcAttr())
			{
				output+=" <tr> ";
				output+= "<td> "+s+" </td>";
				output+=  " </tr> ";
			}
			
			output+=" </table>";
		}
		else
		{
			output+="<h3>&lt;&lt; "+type.getName()+" >> <br>"+name+"</h3>";
		}
		
		return output;
	}
	
/**
 * Pretty printed Name
 * @return a html name
 */
	public String getName()
	{
		String res = name.replaceAll("&lt;", "<");
		res = res.replaceAll("<br>", "");
		
		return res;
	}
	
	/**
	 * Actual name value
	 * @return the name
	 */
	public String getRealName()
	{
		return name;
	}
	
	/**
	 * Set actual name value
	 */
	public void setRealName(String name)
	{
		this.name=name;
	}

	public int getSize() {
		return size;
	}

	public MyNodeType getNodeType() {
		return type;
	}

	public boolean isDetailedOutput() {
		return detailedOutput;
	}

	public void setDetailedOutput(boolean detailedOutput) {
		this.detailedOutput = detailedOutput;
	}
	
	public boolean equals (Object n)
	{
		if (n instanceof Node)
		{
			Node tmp = (Node) n;
			return this.node.equals(tmp);
		}
		
		return false;
	}

	public Node getNode() {
		return node;
	}
}
