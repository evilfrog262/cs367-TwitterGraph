///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            GraphAnalyser.java
// Files:            BasicGraph.java, Graphnode.java
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
import java.io.*;

/**
 * Reads input from a text file and adds nodes to a BasicGraph.  Then displays
 * statistics and information about the resulting graph.
 *
 * @author Will Kraus and Kristin Cox
 */
public class GraphAnalyser {
	
	/**
	  * Reads input from a text file and adds nodes to a BasicGraph.  Then 
	  * displays statistics and information about the resulting graph.
	  */
	public static void main (String[] args) {
		//input text file
		File inFile;
		//Scanner to read text file
		Scanner stdin = null;
		//Graph
		BasicGraph graph = new BasicGraph();
		
		//check for proper number of arguments
		if (args.length != 1) {
			System.out.println("Usage: java GraphAnalyzer fileName");
			System.exit(0);
		}
		try {
			inFile = new File(args[0]);
			stdin = new Scanner(inFile);
		} catch (FileNotFoundException ex) {
			System.out.println("Error: Cannot access input file");
			System.exit(0);
		}
		
		//store the username that comes first alphabetically
		String firstUser = null;
		
        // Process the input file line by line
              
        while (stdin.hasNext()) {
            //next line in the file
        	String line = stdin.nextLine();
            //array of words in the line
        	List<String> words = parseLine(line);
            //compute first user
            if (firstUser == null || words.get(0).compareTo(firstUser) < 0) {
            	firstUser = words.get(0);
			}
            //add first node, the user
            graph.addNode(words.get(0));
            //add all the followers and edges pointing to them
            for (int i = 1; i < words.size(); i++) {
            	graph.addNode(words.get(i)); 
            	graph.addEdge(words.get(0),words.get(i));
            }
           

        } // end while 
        
        //print statistics
		System.out.println("Number of users: " + graph.size());
		System.out.println("Number of follows links: " + numEdges(graph));
		System.out.println("DFS visit order: " + graph.dfs(firstUser));
		System.out.println("BFS visit order: " + graph.bfs(firstUser));
		System.out.println("Most followers: " + mostFollowers(graph));
		System.out.println("No followers: " + noFollowers(graph));
		System.out.println("Don't follow anyone: " + notFollowing(graph));
		System.out.println("Receive most tweets: " + receiveMost(graph));
		List<String> cycleList = cycles(graph);
		System.out.println("Tweet reaches self: " + cycleList.size() + " " 
				+ cycleList);
		List<String> mostReached = mostReached(graph);
		int usersReached = graph.dfs(mostReached.get(0)).size();
		System.out.println("Most users reached: " + usersReached + " " 
				+ mostReached);
		List<String> mostFollowers = mostFollowers(graph);
		System.out.println("Minutes to get tweet: " + minutes(graph, 
				mostFollowers.get(0)));
	}	
	
	/**
	  * Returns number of nodes in the longest path starting at the node
	  * with the most followers.
	  *
	  * @param the graph to be analyzed
	  * @return number of nodes/minutes
	  */
	private static int minutes(BasicGraph graph, String start) {
		Iterator<Graphnode> iter = graph.iterator();
		//node that has most followers
		Graphnode mostFollowers = null;
		//length of longest path from start node
		int minutes = 0;
		//find node with most followers
		String most = mostFollowers(graph).get(0);
		while (iter.hasNext()) {
			Graphnode next = iter.next();
			if (next.getData().equals(most)) {
				mostFollowers = next;
			}
		}
		//new iterator
		iter = graph.iterator();
		//look at all paths from start node to all other nodes
		while(iter.hasNext()){
			List<String> path = graph.shortestPath(mostFollowers.getData(),
			 		                               iter.next().getData());
			//update length of minutes
			if(path != null && path.size() > minutes) {
				minutes = path.size();
			}
		}
		minutes++;
		return minutes;
	}
	
	/**
	  * Returns number of edges in the graph
	  *
	  * @param the graph to be analyzed
	  * @return number of edges in the graph
	  */
	private static int numEdges (BasicGraph graph) {
		Iterator<Graphnode> iter = graph.iterator();
		int edges = 0;
		while(iter.hasNext()){
			edges += iter.next().getSuccessors().size();
		}
		return edges;
	}
	
	/**
	  * Returns list of users who receive the most tweets, or have the 
	  * highest in-degree
	  *
	  * @param the graph to be analyzed
	  * @return list of users who receive the most tweets
	  */
	private static List<String> receiveMost(BasicGraph graph) {
		//list of all nodes in graph
		ArrayList<Graphnode> nodes = new ArrayList<Graphnode>();
		Iterator<Graphnode> iter = graph.iterator();
		//list of users that receive the most tweets
		ArrayList<String> receivers = new ArrayList<String>();
		//get a list of all nodes in the graph
		while (iter.hasNext()) {
			nodes.add(iter.next());
		}
		//create a list to keep track of the in-degree of each node
		int[] inDegree = new int[nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			//every time a node has an arrow to it, increment its in-Degree
			for (Graphnode g : nodes.get(i).getSuccessors()) {
				inDegree[nodes.indexOf(g)]++;
			}
		}
		//largest number of tweets received
		int largest = 0;
		for (int i = 0; i < inDegree.length; i++) {
			//make new list if new largest value is found
			if (inDegree[i] > largest) {
				receivers = new ArrayList<String>();
				receivers.add(nodes.get(i).getData());
				largest = inDegree[i];
			}
			//if equal, add to existing list
			else if (inDegree[i] == largest) {
				receivers.add(nodes.get(i).getData());
			}
		}
		return receivers;
	}
	
	/**
	  * Returns list of users who reach the most other users
	  *
	  * @param the graph to be analyzed
	  * @return list of users who reach the most other users
	  */
	private static List<String> mostReached(BasicGraph graph) {
		Iterator<Graphnode> iter = graph.iterator();
		//list of nodes that reach the most other users indirectly
		List<String> reached = new ArrayList<String>();
		//largest number of users reached
		int largest = 0;
		//do a dfs on each node, result is nodes reached
		while (iter.hasNext()) {
			Graphnode n = iter.next();
			List<String> dfs = graph.dfs(n.getData());
			//check for new largest value and update
			if (dfs.size() > largest) {
				reached = new ArrayList<String>();
				reached.add(n.getData());
				largest = dfs.size();
			}
			//add to existing list
			else if (dfs.size() == largest) {
				reached.add(n.getData());
			}			
		}
		return reached;
	}
	
	/**
	  * Returns list of users who will receive their own tweets, or
	  * nodes that have a cyclic path
	  *
	  * @param the graph to be analyzed
	  * @return list of nodes with a cyclic path
	  */
	private static List<String> cycles(BasicGraph graph) {
		Iterator<Graphnode> iter = graph.iterator();
		//list of users who receive own tweets
		List<String> cycles = new ArrayList<String>();
		//check if there is a cyclic path for all nodes
		while(iter.hasNext()) {
			Graphnode n = iter.next();
			//if there is, add to list
			if (graph.cyclicPath(n.getData()) != null) {
				cycles.add(n.getData());
			}
		}
		return cycles;
	}
	
	/**
	  * Returns the number of users who don't follow anyone
	  *
	  * @param the graph to be analyzed
	  * @return number of users who don't follow anyones 
	  */
	private static int notFollowing(BasicGraph graph) {
		//list of nodes who don't follow anyone
		List<Graphnode> nodes = new ArrayList<Graphnode>();
		Iterator<Graphnode> iter = graph.iterator();
		//add all nodes to the list
		while (iter.hasNext()) {
			nodes.add(iter.next());
		}
		//new iterator
		iter = graph.iterator();
		//remove nodes that are following others (they are someone's successor)
		while (iter.hasNext()) {
			Graphnode n = iter.next();
			for (Graphnode g : n.getSuccessors()) {
				if (nodes.contains(g)) {
					nodes.remove(g);
				}
			}
		}
		return nodes.size();
	}
	
	/**
	  * Returns a list of the usernames of the users with the most followers
	  * 
	  * @param the graph to be analyzed
	  * @return a list of usernames 
	  */
	private static List<String> mostFollowers(BasicGraph graph) {
		//list of nodes with most followers
		List<String> most = new ArrayList<String>();
		//number of most followers
		int mostFollowers = 0;
		Iterator<Graphnode> iter = graph.iterator();
		//check number of followers each node has
		while (iter.hasNext()) {
			Graphnode n = iter.next();
			//if more than current max, make new list 
			if (n.getSuccessors().size() > mostFollowers) {
				mostFollowers = n.getSuccessors().size();
				most = new ArrayList<String>();
				most.add(n.getData());
			}
			//if equal, add to list
			else if (n.getSuccessors().size() == mostFollowers) {
				most.add(n.getData());
			}
		}
		return most;
	}
	
	/**
	  * Counts the number of users with no followers
	  *
	  * @param the graph to be analyzed
	  * @return the number of users with no followers
	  */
	private static int noFollowers(BasicGraph graph) {
		//number of nodes with no followers
		int noFollowers = 0;
		Iterator<Graphnode> iter = graph.iterator();
		//check which nodes have no successors
		while (iter.hasNext()) {
			Graphnode n = iter.next();
			if (n.getSuccessors().size() == 0) {
				noFollowers++;
			}
		}
		return noFollowers;
	}
	
	 /**
     * Parses the given line into an array of words.
     */
    private static List<String> parseLine(String line) {
    	String[] tokens = line.split("[:,]");
        ArrayList<String> words = new ArrayList<String>();
        for (int i = 0; i < tokens.length; i++) {  // for each word
            
            // find index of first digit/letter
            boolean done = false; 
            int first = 0;
            String word = tokens[i];
            while (first < word.length() && !done) {
                if (Character.isDigit(word.charAt(first)) ||
                    Character.isLetter(word.charAt(first)))
                    done = true;
                else first++;
            }
            
            // find index of last digit/letter
            int last = word.length()-1;
            done = false;
            while (last > first && !done) {
                if (Character.isDigit(word.charAt(last)) ||
                        Character.isLetter(word.charAt(last)))
                        done = true;
                    else last--;
            }
            
            // trim from beginning and end of string so that is starts and
            // ends with a letter or digit
            word = word.substring(first, last+1);
  
            // make sure there is at least one letter in the word
            done = false;
            first = 0;
            while (first < word.length() && !done)
                if (Character.isLetter(word.charAt(first)) || 
                		Character.isDigit(word.charAt(first)))
                    done = true;
                else first++;           
            if (done)
                words.add(word);
        }
        
        return words;
    }
}
