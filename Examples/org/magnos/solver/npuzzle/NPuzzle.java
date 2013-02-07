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

package org.magnos.solver.npuzzle;

import java.util.List;

import org.magnos.solver.Solver;
import org.magnos.solver.npuzzle.NPuzzleBoard;
import org.magnos.solver.npuzzle.NPuzzleMove;



public class NPuzzle 
{

	public static void main(String[] args) 
	{
		new NPuzzle();
	}
	
	public NPuzzle() 
	{
		int[][] board;
		
		board = new int[][] {
				{1, 5, 2},
				{7, 4, 3},
				{8, 0, 6},
		};
		solve(board);
		
	}
	
	private void solve(int[][] board) 
	{
		NPuzzleBoard maze = new NPuzzleBoard(board);

		Solver<NPuzzleMove> solver = new Solver<NPuzzleMove>();
		
		solver.setInitialState(maze);
		// States will never be revisited
		solver.setRevisitStates(false);
		// Set to false to find all paths
		solver.setMinimalMoves(true);
		// Set to true so solutions can be backtracked
		solver.setSaveParent(false);
		// Restrict the minimal number of moves, I'm assuming the given board will take less.
		solver.setMaxDepth(20);
		// We only want a single solution.
		solver.setMaxSolutions(1);
		
		solver.solve();

		System.out.format("Solved in %d ms\n", solver.getSolveTime());
		System.out.format("States created: %d\n", solver.getStatesCreated());
		System.out.format("States visited: %d\n", solver.getStatesVisited());
		System.out.format("States duplicated: %d\n", solver.getStatesDuplicates());
		System.out.format("States pooled: %d\n", solver.getUniqueStates());
		System.out.format("States branched: %d\n", solver.getStatesDeviated());
		
		List<NPuzzleBoard> solutions = solver.getSolutions();
		
		System.out.format("Solutions: (%d) {\n", solutions.size());
		for (int i = 0; i < solutions.size(); i++) {
			System.out.format("Solution #%d took %d move(s).\n", i + 1, solutions.get(i).getDepth());
		}
		System.out.println("}");
		System.out.println();
	}
	
}
