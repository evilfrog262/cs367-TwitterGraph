///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  GraphAnalyser.java
// File:             Graphnode.java
// Semester:         Spring 2012
//
// Author:           Kristin Cox
// CS Login:         kcox
// Lecturer's Name:  Beck Hasti
//
//                   PAIR PROGRAMMERS COMPLETE THIS SECTION
// Pair Partner:     Will Kraus
// CS Login:         kraus
// Lecturer's Name:  Beck Hasti
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.*;

/**
 * Methods to access and modify a graphnode that contains a String and a list 
 * of adjacent nodes
 *
 * @author Will Kraus and Kristin Cox
 */
public class Graphnode implements Comparable {
	//data that is held in the graphnode
	private String data;
	//true if node has been visited
	private boolean visited;
	//list of nodes that are successors
	private List<Graphnode> adjList;

	/**
	  * Constructs a new graphnode with the given data, sets it to unvisited,
	  * and gives it an empty list to hold its successors.
	  *
	  * @param String data that the node holds
	  */
	public Graphnode(String data) {
		if (data == null) {
			throw new IllegalArgumentException();	
		}
		this.data = data;
		this.visited = false;
		this.adjList = new ArrayList<Graphnode> ();
	}
	
	/**
	  * Implements the Comparable interface method indirectly through the
	  * String class compareTo method.  One node is compared to another by
	  * the data it holds.
	  *
	  * @param Object to be compared with the graphnode data
	  * @return an int indicating how it compared
	  */
	public int compareTo(Object other) {
		Graphnode that = (Graphnode)other;
		return this.data.compareTo(that.getData());
		
	}
	
	/**
	  * Returns the data of the given node.
	  * 
	  * @return String data of the given node
	  */
	public String getData() {
		return this.data;
	}
	
	/**
	  * Returns a boolean indicating whether or not the node has been
	  * visited.
	  * 
	  * @return true if visited, false otherwise
	  */
	public boolean getVisited() {
		return this.visited;
	}
	
	/**
	  * Returns a list of the successors of the given node.
	  * 
	  * @return List<Graphnode> of the nodes successors
	  */
	public List<Graphnode> getSuccessors() {
		return this.adjList;
	}
	
	/**
	  * Set whether a node has been visited
	  * 
	  * @param boolean true if the node is visited, false if not visited
	  */
	public void setVisited(boolean bool) {
		this.visited = bool;
	}

	/**
	  * Add a new successor to the ordered list of successors.
	  * 
	  * @param Graphnode to add to the successor list
	  */
	public void addSuccessor(Graphnode n) {
		int index = 0;
		//if list is empty, add at beginning
		if (this.adjList.size() == 0) {
			this.adjList.add(n);
			return;
		}
		else {				
			//insert node where it belongs in sorted order
			for (Graphnode g : adjList) {
				if (g.getData().compareTo(n.getData()) > 0) {
					this.adjList.add(index,n);
					return;
				}
				index++;
				if (index >= adjList.size()) {
					adjList.add(n);
					return;
				}
			}
		}
	}

}
