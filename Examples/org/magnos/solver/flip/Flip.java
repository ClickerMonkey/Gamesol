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

package org.magnos.solver.flip;

import java.util.List;

import org.magnos.solver.ConcurrentSolver;
import org.magnos.solver.Solver;
import org.magnos.solver.State;



public class Flip 
{
	
	public static void main(String[] args) 
	{
		new Flip();
	}
	
	public Flip() 
	{
		int[][] board;

		// Hard Level 1 (183ms)
		board = new int[][] {
			{0, 0, 1, 1, 1, 0, 0},
			{0, 0, 1, 0, 1, 0, 0},
			{1, 1, 2, 1, 1, 1, 1},
			{1, 0, 1, 0, 1, 0, 1},
			{1, 1, 1, 0, 1, 1, 1},
			{0, 0, 1, 0, 1, 0, 0},
			{0, 0, 1, 1, 1, 0, 0}
		};
		
		solve(board, 2, 2, 30, 1);

		// Hard Level 2 (135ms)
		board = new int[][] {
			{1, 2, 1, 1, 1, 1, 1},
			{1, 0, 1, 0, 1, 0, 1},
			{1, 1, 1, 0, 2, 1, 1},
			{0, 0, 1, 0, 1, 0, 0},
			{1, 1, 2, 1, 2, 1, 1},
			{1, 0, 1, 0, 1, 0, 1},
			{1, 1, 1, 0, 1, 1, 1}
		};
		
		solve(board, 1, 0, 38, 1);
		
		// Hard Level 4 (13ms)
		board = new int[][] {
			{1, 1, 1, 1, 1},
			{1, 0, 2, 0, 0},
			{1, 1, 2, 1, 1},
			{1, 0, 2, 0, 1},
			{1, 1, 2, 1, 1}
		};
		
		solve(board, 0, 4, 22, 1);
		
		// Hard Level 21 (174ms)
		board = new int[][] {
			{0, 1, 1, 2, 1, 1, 0},
			{1, 1, 0, 2, 0, 1, 1},
			{1, 0, 0, 2, 0, 0, 1},
			{1, 1, 1, 2, 1, 1, 1},
			{0, 0, 0, 1, 0, 0, 1},
			{0, 0, 0, 1, 0, 1, 1},
			{0, 0, 0, 1, 1, 1, 0}
		};
		
		solve(board, 3, 3, 32, 1);
		
		// Hard Level 22 (399ms)
		board = new int[][] {
			{0, 1, 2, 1, 1, 1, 0},
			{2, 1, 2, 1, 2, 1, 2},
			{0, 0, 1, 0, 1, 0, 0},
			{0, 0, 1, 0, 1, 0, 0},
			{2, 1, 2, 0, 2, 1, 2},
			{0, 0, 1, 1, 1, 0, 0},
			{0, 0, 2, 1, 2, 0, 0},
			{0, 0, 0, 2, 0, 0, 0}
		};
		
		solve(board, 3, 7, 27, 1);

		// Hard Level 23 (hasn't finished yet)
		board = new int[][] {
			{1, 1, 1, 1, 1, 0, 0},
			{1, 1, 1, 1, 1, 1, 0},
			{1, 1, 0, 1, 0, 1, 0},
			{1, 1, 0, 2, 1, 1, 1},
			{1, 1, 0, 1, 0, 1, 1},
			{1, 1, 1, 1, 1, 0, 1},
			{1, 1, 1, 1, 1, 1, 1},
			{1, 1, 0, 0, 1, 0, 0}
		};
		
		solve(board, 3, 3, 45, 8);
	}
	
	private void solve(int[][] board, int cx, int cy, int ideal, int workers) 
	{
		FlipPuzzle flip = new FlipPuzzle( cx, cy, board );
		
		Solver<FlipMove> solver = workers <= 1 ? new Solver<FlipMove>() : new ConcurrentSolver<FlipMove>( workers );
		
		solver.setInitialState(flip);
		// Depth-First-Search
		solver.setBreadthFirst(false);
		// States will never be revisited
		solver.setRevisitStates(false);
		// Set to false to find all paths
		solver.setMinimalMoves(true);
		// Set to true so solutions can be backtracked
		solver.setSaveParent(true);
		// Restrict the minimal number of moves, I'm assuming the given board will take less.
		solver.setMaxDepth(ideal);
		// We only want a single solution.
		solver.setMaxSolutions(1);
		
		solver.solve();

		System.out.format("Solved in %d ms\n", solver.getSolveTime());
		System.out.format("States created: %d\n", solver.getStatesCreated());
		System.out.format("States visited: %d\n", solver.getStatesVisited());
		System.out.format("States duplicated: %d\n", solver.getStatesDuplicates());
		System.out.format("States pooled: %d\n", solver.getUniqueStates());
		System.out.format("States branched: %d\n", solver.getStatesDeviated());
		System.out.format("States too short: %d\n", solver.getStatesShort());
		
		List<FlipPuzzle> solutions = solver.getSolutions();
		
		System.out.format("Solutions: (%d) {\n", solutions.size());
		for (int i = 0; i < solutions.size(); i++) {
			System.out.format("Solution #%d took %d move(s).\n", i + 1, solutions.get(i).getDepth());
			print( solutions.get(i) );
		}
		System.out.println("}");
		System.out.println();
	}
	
	private void print(State<FlipMove> flip)
	{
		if ( flip.getParent() != null )
		{
			print( flip.getParent() );
		}
				
		System.out.println( flip );
	}
	
}
