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

package org.magnos.solver.maze;

import java.util.List;

import org.magnos.solver.Solver;
import org.magnos.solver.maze.MazeBoard;
import org.magnos.solver.maze.MazeBoardType;
import org.magnos.solver.maze.MazeMove;



public class Maze 
{

	public static void main(String[] args) 
	{
		new Maze();
	}
	
	public Maze() 
	{
		final boolean X = false;
		final boolean _ = true;
		
		boolean[][] maze = {
				{_, _, X, X, X, X, X, X, X, _, X},
				{X, _, X, _, X, _, _, _, X, _, _},
				{X, _, X, _, _, _, X, _, X, X, _},
				{X, _, _, _, X, _, X, _, _, X, _},
				{X, X, X, _, X, _, X, X, _, X, _},
				{X, _, _, _, X, _, _, X, _, X, _},
				{X, _, X, X, _, X, _, X, _, X, _},
				{X, _, X, _, _, X, _, X, X, X, _},
				{X, _, _, _, X, _, _, _, _, _, _},
				{X, X, X, X, X, X, X, X, X, X, X},
		};
		solve(0, 0, new MazeBoardType(9, 0, MazeBoardType.AXIS, maze));
		
	}
	
	private void solve(int x, int y, MazeBoardType type) 
	{
		MazeBoard maze = new MazeBoard(x, y, type);

		Solver<MazeMove> solver = new Solver<MazeMove>();
		
		solver.setInitialState(maze);
		// States will never be revisited
		solver.setRevisitStates(true);
		// Set to true to find the shortest path
		solver.setMinimalMoves(false);
		// Set to true so solutions can be backtracked
		solver.setSaveParent(false);
		
		solver.solve();

		System.out.format("Solved in %d ms\n", solver.getSolveTime());
		System.out.format("States created: %d\n", solver.getStatesCreated());
		System.out.format("States visited: %d\n", solver.getStatesVisited());
		System.out.format("States duplicated: %d\n", solver.getStatesDuplicates());
		System.out.format("States pooled: %d\n", solver.getUniqueStates());
		System.out.format("States branched: %d\n", solver.getStatesDeviated());
		
		List<MazeBoard> solutions = solver.getSolutions();
		
		System.out.format("Solutions: (%d) {\n", solutions.size());
		for (int i = 0; i < solutions.size(); i++) {
			System.out.format("Solution #%d took %d move(s).\n", i + 1, solutions.get(i).getDepth());
		}
		System.out.println("}");
		System.out.println();
	}
	
}
