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

package org.magnos.solver.pegs;

import java.util.List;

import org.magnos.solver.Solver;
import org.magnos.solver.pegs.PegsBoard;
import org.magnos.solver.pegs.PegsBoardType;
import org.magnos.solver.pegs.PegsMove;



public class PegGame {

	public static void main(String[] args) {
		new PegGame();
	}
	
	public static final boolean _ = false;
	public static final boolean O = true;
	
	public PegGame() {
		
		// http://www.danobrien.ws/PegBoard.html
		
		boolean[][] board;
		
		board = new boolean[][] {
				{O},
				{O, O},
				{_, O, O},
				{O, O, O, O},
				{O, O, O, O, O},
		};
		solve(5, board, new PegsBoardType(PegsBoardType.SHAPE_HEXAGON, PegsBoardType.MASK_TRIANGLE));
		
	}
	
	private void solve(int n, boolean[][] board, PegsBoardType type) {
		PegsBoard initial = new PegsBoard(type, board);

		System.out.format("Initial Board {\n", n, n);
		System.out.print(initial);
		System.out.println("}");
		
		Solver<PegsMove> solver = new Solver<PegsMove>();
		solver.setInitialState(initial);
		// Change to false to get only unique numbers (max 4)
		solver.setRevisitStates(true);
		// Change to true to get the quickest solutions only
		solver.setMinimalMoves(false);
		solver.solve();

		System.out.format("Solved in %d ms\n", solver.getSolveTime());
		System.out.format("States created: %d\n", solver.getStatesCreated());
		System.out.format("States visited: %d\n", solver.getStatesVisited());
		System.out.format("States duplicated: %d\n", solver.getStatesDuplicates());
		System.out.format("States pooled: %d\n", solver.getUniqueStates());
		System.out.format("States branched: %d\n", solver.getStatesDeviated());
		
		List<PegsBoard> solutions = solver.getSolutions();
		System.out.format("%d solution(s)\n", solutions.size());
	}
	
}
