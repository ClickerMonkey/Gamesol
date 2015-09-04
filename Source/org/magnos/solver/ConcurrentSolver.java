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
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Finds solutions of a given initial state with multiple threads.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <M>
 *        The Move type.
 */
public class ConcurrentSolver<M> extends Solver<M>
{

	// The number of threads to spawn to find a solution.
	protected int workers;
	
	// A lock to ensure the solutions ArrayList doesn't get mangled by multiple threads.
	protected ReentrantLock solutionLock;
	
	// The amount of time to wait until the threads decide there are no states to execute.
	protected long timeout = 10;
	
	// The unit for the timeout variable.
	protected TimeUnit timeoutUnit = TimeUnit.MILLISECONDS;
	
	/**
	 * Instantiates a new ConcurrentSolver.
	 */
	public ConcurrentSolver(int workers)
	{
		this.workers = workers;
		this.solutionLock = new ReentrantLock();
	}
	
	/**
	 * Set the number of threads to spawn to find a solution.
	 * 
	 * @param workers The number of threads to spawn to find a solution.
	 */
	public void setWorkers( int workers )
	{
		this.workers = workers;
	}

	/**
	 * Solves the initial state with the given properties and saves any solutions
	 * to a list.
	 */
	public void solve()
	{
		solveStart = System.currentTimeMillis();

		// The current state of the solver.
		SolverState state = new SolverState();
		
		// Clear solutions
		solutions = new ArrayList<State<M>>();
		
		// Clear initial state
		initial.setParent( null );
		initial.setDepth( 0 );

		// Add the initial state in, and mark it as visited.
		state.states.add( initial );
		if (!revisitStates)
		{
			state.visited.add( initial.getHash() );
		}
		
		// Create & Run the Threads
		for (int i = 0; i < workers; i++)
		{
			new Thread( new SolverThread( state ) ).run();
		}
		
		// Wait until they're finished
		try
		{
			state.finishLatch.await();
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException( e );
		}
		
		// Update the statistics
		statesCreated = state.statesCreated.get();
		statesVisited = state.statesVisited.get();
		statesDuplicated = state.statesDuplicated.get();
		statesDeviated = state.statesDeviated.get();
		statesShort = state.statesShort.get();

		solveEnd = System.currentTimeMillis();
	}
	
	private class SolverState
	{
		public final AtomicBoolean solved;
		public final AtomicLong statesCreated, statesVisited, statesDuplicated, statesDeviated, statesShort;
		public final AtomicInteger maxDepth;
		public final ConcurrentDeque<State<M>> states;
		public final Set<Object> visited;
		public final CountDownLatch finishLatch;
		
		public SolverState()
		{
			this.solved = new AtomicBoolean( false );
			this.finishLatch = new CountDownLatch( workers );
			this.states = new ConcurrentDeque<State<M>>( breadthFirst, timeout, timeoutUnit );
			this.visited = Collections.newSetFromMap( new ConcurrentHashMap<Object, Boolean>() );
			this.statesCreated = new AtomicLong();
			this.statesVisited = new AtomicLong();
			this.statesDuplicated = new AtomicLong();
			this.statesDeviated = new AtomicLong();
			this.statesShort = new AtomicLong();
			this.maxDepth = new AtomicInteger( ConcurrentSolver.this.maxDepth );
		}
	}
	
	private class SolverThread implements Runnable
	{
		private final SolverState solver;
		
		public SolverThread(SolverState solver)
		{
			this.solver = solver;
		}
		
		public void run()
		{
			// While states exist in the pool...
			while (!solver.solved.get())
			{
				// Take the next state from the pool...
				State<M> current = solver.states.poll();
				
				// If there wasn't a next state, assume there are no more states.
				if ( current == null ) 
				{
					break;
				}
				
				solver.statesVisited.incrementAndGet();

				// If the current state is a solution...
				if (current.isSolution())
				{
					solutionLock.lock();
					
					// If this is supposed to find the minimal movement solutions
					// then check if this solution has a lower depth. If it does
					// have a lower depth then clear existing solutions and update
					// the new max depth.
					if (minimalMoves && solver.maxDepth.get() > current.getDepth())
					{
						solutions.clear();

						solver.maxDepth.set( current.getDepth() );
					}
					
					// Add the solution to the list.
					solutions.add( current );
					
					// If we found the number of solutions we were looking for then
					// don't search for any more solutions.
					if (solutions.size() == maxSolutions)
					{
						solver.solved.set( true );
					}
					
					solutionLock.unlock();
				}
				// Only branch if the depth of this state is less then maxDepth;
				else if (current.getDepth() < solver.maxDepth.get())
				{
					// If the state has a number of remaining moves and it's not going to get there in time, skip it.
					int remaining = current.getRemainingMoves();
					if ( remaining != -1 && current.getDepth() + remaining > solver.maxDepth.get() )
					{
						solver.statesShort.incrementAndGet();
						
						continue;
					}
					
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
						solver.statesCreated.incrementAndGet();

						// If the solver doesn't care about traversing duplicate
						// states...
						if (revisitStates)
						{
							solver.states.add( next );
						}
						// The solver must avoid traversing duplicate states.
						else
						{
							// Create the hash for this state.
							Object hash = next.getHash();
							
							// If this state has not yet been visited.
							if (!solver.visited.contains( hash ))
							{
								// Add the state to the pool and mark it as visited.
								solver.states.add( next );
								solver.visited.add( hash );
							}
							else
							{
								solver.statesDuplicated.incrementAndGet();
							}
						}
					}
					
					while (moveCount-- > 0)
					{
						solver.statesDeviated.incrementAndGet();
					}
				}
			}
			
			solver.finishLatch.countDown();
		}
	}
	
	

}
