/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * Finds solutions of a given initial state.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <M>
 *        The Move type.
 */
public class Solver<M>
{

	// Save the path to each solution?
	private boolean saveParent = false;

	// Solve using a breadth-first-search?
	private boolean breadthFirst = false;

	// Find the solution with the minimal number of moves?
	private boolean minimalMoves = false;

	// Can we revisit a state? Or should we keep track to avoid repitition.
	private boolean revisitStates = false;

	// The maximum number of solutions to search for.
	private int maxSolutions = Integer.MAX_VALUE;

	// The maximum number of moves that can be made for a solution.
	private int maxDepth = Integer.MAX_VALUE;

	// The initial state to solve from.
	private State<M> initial;

	// The list of solutions.
	private List<State<M>> solutions;

	/** Statistics **/

	// How many states were visited in the last solution?
	private long statesVisited;

	// How many states were created total?
	private long statesCreated;

	// How many states were reached in different paths?
	private long statesDuplicated;

	// How many branches were there?
	private long statesDeviated;

	// When solve started.
	private long solveStart;

	// When solve ended.
	private long solveEnd;

	/**
	 * Instantiates a new Solver.
	 */
	public Solver()
	{
	}

	/**
	 * Set whether to save the path to each solution.
	 * 
	 * @param saveParent
	 *        Save the path to each solution?
	 */
	public void setSaveParent( boolean saveParent )
	{
		this.saveParent = saveParent;
	}

	/**
	 * Set the maximum number of solutions to search for.
	 * 
	 * @param maxSolutions
	 *        The maximum number of solutions to search for.
	 */
	public void setMaxSolutions( int maxSolutions )
	{
		this.maxSolutions = maxSolutions;
	}

	/**
	 * Set the maximum number of moves that can be made for a solution.
	 * 
	 * @param maxDepth
	 *        The maximum number of moves that can be made for a solution.
	 */
	public void setMaxDepth( int maxDepth )
	{
		this.maxDepth = maxDepth;
	}

	/**
	 * Set the initial state to solve from.
	 * 
	 * @param initial
	 *        The initial state to solve from.
	 */
	public void setInitialState( State<M> initial )
	{
		this.initial = initial;
	}

	/**
	 * Set whether to save the path to each solution?
	 * 
	 * @param breadthFirst
	 *        Save the path to each solution?
	 */
	public void setBreadthFirst( boolean breadthFirst )
	{
		this.breadthFirst = breadthFirst;
	}

	/**
	 * Set whether to Find the solution with the minimal number of moves.
	 * 
	 * @param minimalMoves
	 *        Find the solution with the minimal number of moves?
	 */
	public void setMinimalMoves( boolean minimalMoves )
	{
		this.minimalMoves = minimalMoves;
	}

	/**
	 * Set whether we can revisit a state? Or should we keep track to avoid
	 * repitition.
	 * 
	 * @param revisitStates
	 *        Can we revisit a state? Or should we keep track to avoid
	 *        repitition.
	 */
	public void setRevisitStates( boolean revisitStates )
	{
		this.revisitStates = revisitStates;
	}

	/**
	 * How many states were visited in the last solution.
	 * 
	 * @return How many states were visited in the last solution.
	 */
	public long getStatesVisited()
	{
		return statesVisited;
	}

	/**
	 * How many states were created total.
	 * 
	 * @return How many states were created total.
	 */
	public long getStatesCreated()
	{
		return statesCreated;
	}

	/**
	 * How many states were reached in different paths.
	 * 
	 * @return How many states were reached in different paths.
	 */
	public long getStatesDuplicates()
	{
		return statesDuplicated;
	}

	/**
	 * How many branches were there.
	 * 
	 * @return How many branches were there.
	 */
	public long getStatesDeviated()
	{
		return statesDeviated;
	}

	/**
	 * How many unique states were created.
	 * 
	 * @return How many unique states were created.
	 */
	public long getUniqueStates()
	{
		return statesCreated - statesDuplicated;
	}

	/**
	 * Returns the total elapsed solve time in milliseconds.
	 * 
	 * @return Returns the total elapsed solve time in milliseconds.
	 */
	public long getSolveTime()
	{
		return solveEnd - solveStart;
	}

	/**
	 * Returns the list of solutions.
	 * 
	 * @param <S>
	 *        The state type.
	 * @return The list of solutions from the last invocation of solve.
	 */
	@SuppressWarnings ("unchecked" )
	public <S extends State<M>> List<S> getSolutions()
	{
		return (List<S>)solutions;
	}

	/**
	 * Solves the initial state with the given properties and saves any solutions
	 * to a list.
	 */
	public void solve()
	{
		solveStart = System.currentTimeMillis();

		// Instantiate the pool of states and the set of visited states.
		Deque<State<M>> states = new Deque<State<M>>( breadthFirst );
		HashSet<Object> visited = new HashSet<Object>();

		// Clear solutions and statistics
		solutions = new ArrayList<State<M>>();
		statesCreated = 1;
		statesVisited = 0;
		statesDuplicated = 0;
		statesDeviated = 0;

		// Clear initial state
		initial.setParent( null );
		initial.setDepth( 0 );

		// Add the initial state in, and mark it as visited.
		states.add( initial );
		if (!revisitStates)
		{
			visited.add( initial.getHash() );
		}

		// Copy the maxDepth, since this number may change.
		int maxDepth = this.maxDepth;

		// While states exist in the pool...
		while (!states.isEmpty())
		{
			// Take the next state from the pool...
			State<M> current = states.poll();
			statesVisited++;

			// If the current state is a solution...
			if (current.isSolution())
			{
				// If this is supposed to find the minimal movement solutions
				// then check if this solution has a lower depth. If it does
				// have a lower depth then clear existing solutions and update
				// the new max depth.
				if (minimalMoves && maxDepth > current.getDepth())
				{
					solutions.clear();
					maxDepth = current.getDepth();
				}
				// Add the solution to the list.
				solutions.add( current );
				// If we found the number of solutions we were looking for then
				// don't search for any more solutions.
				if (solutions.size() == maxSolutions)
				{
					break;
				}
			}
			// Only branch if the depth of this state is less then maxDepth;
			else if (current.getDepth() < maxDepth)
			{
				// Retrieve all possible moves in this state.
				Iterator<M> moves = current.getMoves();
				int moveCount = 0;

				// If moves is null, then skip this
				if (moves == null)
				{
					continue;
				}

				// While possible moves exist...
				while (moves.hasNext())
				{
					moveCount++;
					// Take the next move, copy the current state, and apply the
					// next move to the state.
					M move = moves.next();
					State<M> next = current.getCopy();
					next.addMove( move );

					// Set the depth of the next state.
					next.setDepth( current.getDepth() + 1 );
					// If we're to save the solution path then set parent.
					if (saveParent)
					{
						next.setParent( current );
					}
					statesCreated++;

					// If the solver doesn't care about traversing duplicate
					// states...
					if (revisitStates)
					{
						states.add( next );
					}
					// The solver must avoid traversing duplicate states.
					else
					{
						// Create the hash for this state.
						Object hash = next.getHash();
						// If this state has not yet been visited.
						if (!visited.contains( hash ))
						{
							// Add the state to the pool and mark it as visited.
							states.add( next );
							visited.add( hash );
						}
						else
						{
							statesDuplicated++;
						}
					}
				}
				if (moveCount > 1)
				{
					statesDeviated += moveCount - 1;
				}
			}
		}
		solveEnd = System.currentTimeMillis();
	}

}
