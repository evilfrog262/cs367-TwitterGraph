///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  GraphAnalyser.java
// File:             BasicGraph.java
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
 * The BasicGraph class represents a directed graph with string labels for nodes. 
 * It contains methods to modify and obtain information about a graph that is 
 * made of a TreeSet.
 *
 * @author Will Kraus and Kristin Cox
 */
public class BasicGraph {
	//treeset to hold all the Graphnodes contained in the graph
	private TreeSet<Graphnode> treeGraph;
	//number of nodes in the graph
	private int numItems;
	
	/**
	  * Constructs an empty basic graph
	  */	
	public BasicGraph() {
		this.treeGraph = new TreeSet<Graphnode>();
		this.numItems = 0;
	}

	/**
	  * Adds a node with the given label to the graph. If a node with this
	  * label is already in the graph, the graph is unchanged and false is
	  * returned. Otherwise (i.e., if there previously had not been a node
	  * with this label and a new node with this label is added to the 
	  * graph), true is returned. 
	  *
	  * @param String label data contained in the new node
	  * @return false if graph already contains the node, true otherwise
	  * @throws IllegalArgumentException if label is null 
	  */
	public boolean addNode(String label) {
		if (label == null) {
			throw new IllegalArgumentException();	
		}
		Graphnode n = new Graphnode(label);
		boolean added = treeGraph.add(n);
		if (added) {
			numItems++;
			return true;
		}
		return false;
	}
	
	/**
	  * Add to the graph the specified directed edge from the node with the 
	  * label sourceLabel to the node with the label targetLabel. If the edge 
	  * is already in the graph, the graph is unchanged and false is returned. 
	  * Otherwise (i.e., if edge is not already in the graph), true is returned 
	  *
	  * @param String sourceLabel data contained in the source node
	  * @param String targetLabel data contained in the target node
	  * @return false if graph already contains the edge, true otherwise
	  * @throws IllegalArgumentException if either label is null or if there
	  * are not nodes in the graph with the given labels
	  */
	public boolean addEdge(String sourceLabel, String targetLabel) {
		if (sourceLabel == null || targetLabel == null) {
			throw new IllegalArgumentException();
		}
		//get source and target nodes
		Graphnode source = getNode(sourceLabel);
		Graphnode target = getNode(targetLabel);
		if (source == null || target == null) {
			throw new IllegalArgumentException();
		}
		//if edge already exists, don't add
		if (source.getSuccessors().contains(target)) {
			return false;
		}
		//else add
		source.addSuccessor(target);
		return true;
	}
	
	/**
	  * Checks the graph for a node with the given label.
	  *
	  * @return true if node is found, false otherwise
	  * @throws IllegalArgumentException if label is null 
	  */
	public boolean hasNode(String label) {
		if (label == null) {
			throw new IllegalArgumentException();	
		}
		Graphnode n = getNode(label);
		return treeGraph.contains(n);
	}
	
	/**
	  * Checks the graph for an edge between nodes with the 
	  * given labels.
	  *
	  * @return true if edge is found, false otherwise
	  * @throws IllegalArgumentException if either label is null or 
	  * if there are no nodes in the graph with the given labels 
	  */
	public boolean hasEdge(String sourceLabel, String targetLabel) {
		if (sourceLabel == null || targetLabel == null) {
			throw new IllegalArgumentException();
		}
		//get source and target node
		Graphnode source = getNode(sourceLabel);
		Graphnode target = getNode(targetLabel);
		if (source == null || target == null) {
			throw new IllegalArgumentException();
		}
		//see if target is a successor of source
		return source.getSuccessors().contains(target);
	}
	
	/**
	  * Return a list of node labels in the order they are visited using a
	  * depth-first search starting at the node with the given label. Whenever
	  * a node has multiple successors, the successors are visited in 
	  * alphabetical order. 
	  *
	  * @param startLabel label of the start node
	  * @return list of the node labels if DFS order
	  * @throws IllegalArgumentException if startlabel is null or 
	  * if there is no node in the graph with this label 
	  */
	public List<String> dfs(String startLabel) {
		if (startLabel == null) {
			throw new IllegalArgumentException();	
		}
		unVisit();		
		return auxDFS(startLabel);
	}
	
	/**
	  * Return a list of node labels in the order they are visited using a
	  * breadth-first search starting at the node with the given label.
	  * Whenever a node has multiple successors, the successors are visited
	  * in alphabetical order.  
	  *
	  * @param startLabel label of the start node
	  * @return list of the node labels if BFS order
	  * @throws IllegalArgumentException if startlabel is null or 
	  * if there is no node in the graph with this label 
	  */
	public List<String> bfs(String startLabel) {
		if (startLabel == null) {
			throw new IllegalArgumentException();	
		}
		unVisit();
		//queue to perform bfs
		List<Graphnode> queue = new LinkedList<Graphnode>();
		//list of data of nodes in bfs order
		List<String> myList = new ArrayList<String>();
		//get starting node
		Graphnode n = getNode(startLabel);
		if (n == null) {
			throw new IllegalArgumentException();
		}
		//visit node and add to queue as well as bfs list
		n.setVisited(true);
		myList.add(n.getData());
		queue.add(n);
		//go through queue
		while(!queue.isEmpty()) {
			Graphnode k = queue.remove(0);
			//visit all unvisited successors and add to queue
			for (Graphnode g : k.getSuccessors()) {
				if (!g.getVisited()) {
					g.setVisited(true);
					myList.add(g.getData());
					queue.add(g);
				}
			}
		}
		return myList;
	}
	
	/**
	  * Find the shortest path from a start node to a finish node. Whenever 
	  * a node has multiple successors, the successors are visited in 
	  * alphabetical order. Returns the complete list of node labels along 
	  * the path, with the start node label appearing first and the finish 
	  * node label appearing last.   
	  *
	  * @param startLabel label of the start node
	  * @param finishLabel label of the finish node
	  * @return sequence of nodes along the path, or null if there is no 
	  * such path
	  * @throws IllegalArgumentException if startlabel or finishLabel is null
	  */
	public List<String> shortestPath(String startLabel, String finishLabel) {
		if (startLabel == null || finishLabel == null) {
			throw new IllegalArgumentException();
		}
		List<String> myList = auxShort(startLabel, finishLabel);
		//if the returned list has length 0, there is no path
		if (myList.size() == 0) {
			return null;
		}
		else {
			return myList;
		}		
	}
	
	/**
	  * Find a cyclic path that starts and ends at the given node. Whenever a 
	  * node has multiple successors, the successors are visited in 
	  * alphabetical order. Returns the complete list of node labels along the
	  * path, with the start node label appearing first and last. The path, if 
	  * it is found, is non-trivial, i.e., it contains at least one edge. If 
	  * there is not a cyclic path that starts and ends at the given node, null 
	  * is returned.   
	  *
	  * @param startLabel label of the start node
	  * @return sequence of nodes along the cyclic path, starting and ending at
	  * the start node, or null if there is no such path
	  * @throws IllegalArgumentException if startlabel or finishLabel is null
	  */
	public List<String> cyclicPath(String startLabel) {
		if (startLabel == null) {
			throw new IllegalArgumentException();	
		}
		//list of data of nodes in the cyclic path
		List<String> myList = null;
		unVisit();
		//get and visit starting node
		Graphnode start = getNode(startLabel);
		start.setVisited(true);
		//visit all of its successors
		for (Graphnode n : start.getSuccessors()) {
			myList = shortestPath(n.getData(),startLabel);
			//checks if there is a path from those nodes to the start
			if (myList != null) {
				myList.add(0,startLabel);
				return myList;
			}			
		}	
		return null;	
	}
	
	/**
	  * Creates an iterator over the nodes in the graph.
	  *
	  * @return an iterator over the nodes in the graph
	  */
	public Iterator<Graphnode> iterator() {
		return treeGraph.iterator();
	}
	
	/**
	  * Return labels of immediate successors of the given node in
	  * alphabetical order. 
	  *
	  * @param label of the start node
	  * @return labels of immediate neighbors of the start node in
	  * 	alphabetical order
	  * @throws IllegalArgumentException if label is null or if there
	  * 	is no node in the graph with the given label
	  */
	public List<String> successors(String label) {
		if (label == null) {
			throw new IllegalArgumentException();	
		}
		//get graphnode whose successors are wanted
		Graphnode n = getNode(label);
		if (n == null) {
			throw new IllegalArgumentException();
		}
		//list of successors
		List<String> myList = new ArrayList<String>();
		for (Graphnode g: n.getSuccessors()) {
			myList.add(g.getData());
		}
		return myList;
	}
	
	/**
	  * Returns the number of nodes in the graph.
	  *
	  * @return number of nodes
	  */
	public int size() {
		return this.numItems;
	}
	
	/**
	  * Return true if and only if the graph has size 0 (no nodes or edges)
	  *
	  * @return true if and only if the graph is empty
	  */
	public boolean isEmpty() {
		return this.numItems == 0;
	}
	
	/**
	  * Search the graph and return a node with the given string label.
	  *
	  * @param String label data contained in the node to be found
	  * @return the node if found, otherwise null 
	  */
	private Graphnode getNode(String str) {
		//go through all nodes and see if one contains a matching string
		for (Graphnode n : treeGraph) {
			if (n.getData().equals(str)) {
				return n;
			}
		}
		return null;
	}
	
	/**
	  * Mark all nodes in the graph as unvisited.
	  *
	  */
	private void unVisit() {
		for (Graphnode n : treeGraph) {
			n.setVisited(false);
		}
	}
	
	/**
	  * Return a list of node labels in the order they are visited using a
	  * depth-first search starting at the node with the given label. Whenever
	  * a node has multiple successors, the successors are visited in 
	  * alphabetical order. 
	  *null;	
	}
	  * @param startLabel label of the start node
	  * @return list of the node labels if DFS orderbase case
	  * @throws IllegalArgumentException if there is no node in the graph with
	  * the given label
	  */
	private List<String> auxDFS(String startLabel) {
		//list of data in nodes in dfs order
		List<String> myList = new ArrayList<String>();
		//get starting node
		Graphnode n = getNode(startLabel);
		if (n == null) {
			throw new IllegalArgumentException();
		}
		//visit first node and add to dfs list
		n.setVisited(true);
		myList.add(n.getData());
		//recursively visit all unvisited successors
		for (Graphnode g : n.getSuccessors()) {
			if (!g.getVisited()) {
				myList.addAll(auxDFS(g.getData()));
			}
		}
		return myList;
	}
	
	/**quals(startLabel));
	  * Find the shortest path from a start node to a finish node. Whenever 
	  * a node has multiple successors, the successors are visited in 
	  * alphabetical order. Returns the complete list of node labels along 
	  * the path, with the start node label appearing first and the finish 
	  * node label appearing last.   
	  *
	  * @param startLabel label of the start node
	  * @param finishLabel label of the finish node
	  * @return sequence of nodes along the path, or empty list if there is no 
	  * such path
	  * @throws IllegalArgumentException if there is no node in the graph with 
	  * this label 
	  */
	private List<String> auxShort(String startLabel, String finishLabel) {
		//get starting and target nodes
		Graphnode n = getNode(startLabel);
		Graphnode target = getNode(finishLabel);
		if (n == null || target == null) {
			throw new IllegalArgumentException();
		}
		//list of data of nodes in the shortest path
		List<String> nodeList = new ArrayList<String>();
		n.setVisited(true);
		//base case: if the target is a successor of the node, add those nodes
		if(n.getSuccessors().contains(target)){
			nodeList.add(finishLabel);
			nodeList.add(0, startLabel);
			return nodeList;
		}
		//if you are at the target, add it
		if (n.getData().equals(finishLabel)) {
			nodeList.add(finishLabel);
			return nodeList;
		}	
		//if the node has no successors, return
		if (n.getSuccessors().size() == 0) {
			return nodeList;
		}
		//recursively visit all unvisited successors
		for (Graphnode g: n.getSuccessors()) {
			if (!g.getVisited()) {
				nodeList.addAll(auxShort(g.getData(),finishLabel));
			}
		}
		//add the starting node at the beginning of the list
		if(nodeList.size() != 0 && !nodeList.contains(startLabel)){
			nodeList.add(0, startLabel);
		}
		return nodeList;		
	}
	
}
