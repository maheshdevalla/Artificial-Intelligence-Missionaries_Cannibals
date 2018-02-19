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
import java.util.Arrays;

// for the first part of the assignment, you might not extend UUSearchProblem,
//  since UUSearchProblem is incomplete until you finish it.

public class CannibalProblem extends UUSearchProblem{

	// the following are the only instance variables you should need.
	//  (some others might be inherited from UUSearchProblem, but worry
	//  about that later.)

	private int goalm, goalc, goalb;
	private int totalMissionaries, totalCannibals; 

	public CannibalProblem(int sm, int sc, int sb, int gm, int gc, int gb) {
		// I (djb) wrote the constructor; nothing for you to do here.

		startNode = new CannibalNode(sm, sc, 1, 0);
		goalm = gm;
		goalc = gc;
		goalb = gb;
		totalMissionaries = sm;
		totalCannibals = sc;
		
	}
	
	// node class used by searches.  Searches themselves are implemented
	//  in UUSearchProblem.
	private class CannibalNode implements UUSearchNode {

		// do not change BOAT_SIZE without considering how it affect
		// getSuccessors. 
		
		private final static int BOAT_SIZE = 2;
	
		// how many missionaries, cannibals, and boats
		// are on the starting shore
		private int[] state; 
		
		// how far the current node is from the start.  Not strictly required
		//  for search, but useful information for debugging, and for comparing paths
		private int depth;  

		public CannibalNode(int m, int c, int b, int d) {
			state = new int[3];
			this.state[0] = m;
			this.state[1] = c;
			this.state[2] = b;
			
			depth = d;

		}
		public boolean areMissionariesSafe()
		{
			
				if( state[0] > 0 && state[0] < state[1]) // If number of missionaries are less than number of cannibals cannibals, then return from the function 
					return false;
				else if( state[0] < 0 || state[1] < 0) //If provided with wrong input (Missionaries or cannibals less than zero)
					return false;
				else if((totalMissionaries-state[0]) > 0 && (totalMissionaries-state[0]) < (totalCannibals-state[1])) // If missionaries less than cannibals
					return false;
				else if(state[0] > totalMissionaries || state[1] > totalCannibals)// invalid number of missionaries and cannibals
					return false;
				else 
					return true;// if missionaries are greater than cannibals
		}

		public ArrayList<UUSearchNode> getSuccessors() {

			// add actions (denoted by how many missionaries and cannibals to put
			// in the boat) to current state. 

			// You write this method.  Factoring is usually worthwhile.  In my
			//  implementation, I wrote an additional private method 'isSafeState',
			//  that I made use of in getSuccessors.  You may write any method
			//  you like in support of getSuccessors.
			
			
			int boatDirection,totalMissionaries,totalCannibals;			
			ArrayList<UUSearchNode> nextnode = new ArrayList<>();
			
		 //The position of boat is changed if it moved from one bank to other bank
			boatDirection=(state[2]==0)?1:0;
			
			// Moving  from one bank to the other bank of the river.
			for(int missionaries = 0; missionaries <= BOAT_SIZE; missionaries++)
			{
				for(int cannibals = 0; cannibals <= BOAT_SIZE - missionaries; cannibals++) 
				{
					if(boatDirection==0)// reducing the missionaries and cannibals from first bank after they are moved to other bank  
					{
						totalMissionaries = state[0] - missionaries;
						totalCannibals = state[1] - cannibals;
					}
					else
					{
						totalMissionaries = state[0] + missionaries;
						totalCannibals = state[1] + cannibals;
					}
					CannibalNode nextreferencenode = new CannibalNode(totalMissionaries,totalCannibals,boatDirection,depth);
					if(cannibals+missionaries!= 0)
						if(nextreferencenode.areMissionariesSafe())
							nextnode.add(nextreferencenode);
				}
			}
			return nextnode;

		}
		
		@Override
		public boolean goalTest() {
			// you write this method.  (It should be only one line long.)
			// Checking if the goal is reached or not
			return (state[0] == goalm && state[1] == goalc && state[2] == goalb);

		}

		

		// an equality test is required so that visited lists in searches
		// can check for containment of states
		@Override
		public boolean equals(Object other) {
			return Arrays.equals(state, ((CannibalNode) other).state);
		}

		@Override
		public int hashCode() {
			return state[0] * 100 + state[1] * 10 + state[2];
		}

		@Override
		public String toString() {
			// you write this method
			return "["+state[0]+", "+state[1]+", "+state[2]+"]";	
		}	
        //You might need this method when you start writing 
        //(and debugging) UUSearchProblem.
        
		@Override
		public int getDepth() {
			return depth;
		}
		

	}
	

}