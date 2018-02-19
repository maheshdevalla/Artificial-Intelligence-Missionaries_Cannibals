/*
 * COSC:176 Artificial Intelligence.
 * Assignment 1 : Missionaries and Cannibals Problem.
 * Author : Mahesh Devalla
 * Submission Date : 09/20/2016
 * Acknowledgement : Professor Devin Balkcom for providing the stubs.
 * 
 */
package cannibals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class UUSearchProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected UUSearchNode startNode;
	
	protected interface UUSearchNode {
		public ArrayList<UUSearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
	}

	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.
	
	public List<UUSearchNode> breadthFirstSearch(){
		resetStats();
		
		// You will write this method
		HashMap<UUSearchNode,UUSearchNode> node = new HashMap<>();
		Queue<UUSearchNode> queue = new LinkedList<>(); // Queue implementation of LinkedList (parent reference is used to stored child object)
		HashSet<UUSearchNode> visitedNodes = new HashSet<>(); // HashSet to store the visited nodes
		
		queue.add(startNode);
		visitedNodes.add(startNode);
		incrementNodeCount();
		
		while(queue.isEmpty()==false) 
		{
			UUSearchNode currentnode = queue.remove();
			if (currentnode.goalTest())
				return backchain(currentnode, node);

			ArrayList<UUSearchNode> al = currentnode.getSuccessors();
			for (UUSearchNode iterator : al) 
			{
				if(!visitedNodes.contains(iterator)) 
				{
					node.put(iterator, currentnode);
					queue.add(iterator);
					visitedNodes.add(iterator);
					incrementNodeCount();
				}
			}
			updateMemory(visitedNodes.size());
		}
		return null;

	}
	
	// backchain should only be used by bfs, not the recursive dfs
	private List<UUSearchNode> backchain(UUSearchNode node,HashMap<UUSearchNode, UUSearchNode> visited) {
		// you will write this method
		if(visited.containsKey(node)==false)
			return null;
		List<UUSearchNode> listOfNodes = new LinkedList<>();
		listOfNodes.add(node);
		while(visited.containsKey(node))
		{
			UUSearchNode prev = visited.get(node);
			if(!prev.equals(startNode)) 
				listOfNodes.add(prev); 
			node = prev;
		}
		return listOfNodes;
	}

	public List<UUSearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats(); 
		// You will write this method
		HashMap<UUSearchNode,Integer> visitedNodes = new HashMap<>();
		visitedNodes.put(startNode,0);
		return dfsrm(startNode, visitedNodes, 0, maxDepth);

	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<UUSearchNode> dfsrm(UUSearchNode currentNode, HashMap<UUSearchNode, Integer> visited, 
			int depth, int maxDepth) {
		
		// keep track of states; these calls charge for the current node	
		updateMemory(visited.size());
		incrementNodeCount();
		// you write this method.  Comments *must* clearly show the 
		//  "base case" and "recursive case" that any recursive function has.
		
		if(currentNode.goalTest())// Testing and returning nodes(sates)
		{
			List<UUSearchNode> listOfNodes = new LinkedList<>();
			return listOfNodes;
		}
		else if(depth>=maxDepth) // If depth exceeded beyond the limit then return 
		{
			return null;
		}
		for(UUSearchNode uusnloop: currentNode.getSuccessors()) 
		{
			if(!visited.containsKey(uusnloop) || visited.get(uusnloop) >= depth)
			{
				visited.put(uusnloop,depth+1);
				List<UUSearchNode> states = (LinkedList<UUSearchNode>) dfsrm(uusnloop, visited,depth+1,maxDepth);
				if(states!=null)
				{
					states.add(uusnloop);
					return states;
				}
			}
		}
		return null;
	}
	
	
	// set up the iterative deepening search, and make use of dfsrpc
	public List<UUSearchNode> IDSearch(int maxDepth) {
		resetStats();
		for(int i = 0; i < maxDepth; i++)
		{
			HashSet<UUSearchNode> path = new HashSet<>();
			path.add(startNode);
			List<UUSearchNode> states = dfsrpc(startNode, path, 0, i);

			if(states!=null)
				return states;
		}
		return null;
	}

	// set up the depth-first-search (path-checking version), 
	//  but call dfspc to do the real work
	public List<UUSearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		
		// I wrote this method for you.  Nothing to do.

		HashSet<UUSearchNode> currentPath = new HashSet<>();
		
		return dfsrpc(startNode, currentPath, 0, maxDepth);

	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<UUSearchNode> dfsrpc(UUSearchNode currentNode, HashSet<UUSearchNode> currentPath,
			int depth, int maxDepth) {

		// you write this method
		incrementNodeCount();
		updateMemory(currentPath.size());
		if(currentNode.goalTest())
		{
			List<UUSearchNode> list = new LinkedList<>();
			return list;
		}
		else if(depth>=maxDepth) // If Depth increased more than given depth
		{
			return null;
		}

		/*
		 * Recursion Case
		 */
		for(UUSearchNode UUSNloop: currentNode.getSuccessors()){
			if(!currentPath.contains(UUSNloop))
			{
				currentPath.add(UUSNloop);
				List<UUSearchNode> listOfNodes =  dfsrpc(UUSNloop, currentPath, depth + 1, maxDepth); 

				if(listOfNodes!=null) // Add node to loop if recursion returns not null
				{
					listOfNodes.add(UUSNloop);
					return listOfNodes;
				}
				else
				{
					currentPath.remove(UUSNloop); 
				}
			}
		}
		return null;
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}