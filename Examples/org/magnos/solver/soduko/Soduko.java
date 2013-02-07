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

package org.magnos.solver.soduko;

import java.util.List;

import org.magnos.solver.Solver;
import org.magnos.solver.soduko.SodukoBoard;
import org.magnos.solver.soduko.SodukoMove;



public class Soduko 
{

	public static void main(String[] args)
	{
		new Soduko();
	}
	
	public Soduko() 
	{
		// http://www.easton.me.uk/sudoku/sizes.php
		// http://www.chessandpoker.com/sudoku-strategy-guide.html
		// TODO: implement lone numbers and advanced crosshatching
		
		short[][] board;
		
		board = new short[][] {
				{0, 0, 0,/**/ 7, 0, 0,/**/ 0, 9, 0},
				{0, 0, 9,/**/ 0, 3, 0,/**/ 0, 6, 0},
				{8, 0, 0,/**/ 6, 0, 0,/**/ 4, 3, 2},
				/* ****************************** */
				{7, 0, 0,/**/ 0, 0, 3,/**/ 6, 0, 0},
				{0, 2, 0,/**/ 0, 7, 0,/**/ 0, 5, 0},
				{0, 0, 8,/**/ 5, 0, 0,/**/ 0, 0, 7},
				/* ****************************** */
				{9, 8, 1,/**/ 0, 0, 7,/**/ 0, 0, 6},
				{0, 4, 0,/**/ 0, 9, 0,/**/ 2, 0, 0},
				{0, 6, 0,/**/ 0, 0, 5,/**/ 0, 0, 0}};
		solve(3, board);
		
		
		board = new short[][] {
				{0, 0, 0,/**/ 0, 5, 3,/**/ 6, 0, 0},
				{0, 3, 0,/**/ 1, 0, 0,/**/ 0, 0, 0},
				{1, 0, 5,/**/ 0, 7, 0,/**/ 0, 9, 0},
				/* ****************************** */
				{0, 0, 0,/**/ 2, 0, 0,/**/ 7, 5, 0},
				{0, 8, 0,/**/ 0, 0, 0,/**/ 0, 3, 0},
				{0, 5, 4,/**/ 0, 0, 1,/**/ 0, 0, 0},
				/* ****************************** */
				{0, 6, 0,/**/ 0, 2, 0,/**/ 5, 0, 8},
				{0, 0, 0,/**/ 0, 0, 6,/**/ 0, 2, 0},
				{0, 0, 3,/**/ 4, 1, 0,/**/ 0, 0, 0}};
		solve(3, board);
		
		
		board = new short[][] {
				{0, 0,/**/ 4, 0},
				{1, 0,/**/ 0, 0},
				/* *********** */
				{0, 0,/**/ 0, 0},
				{0, 1,/**/ 0, 0}};
		solve(2, board);
		
		
//		board = new short[][] {
//				{00, 00, 00,  2,  3,  1, 14, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//				{00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00},
//		};
//		solve(4, board);
	}
	
	private void solve(int n, short[][] board) 
	{
		SodukoBoard soduko = new SodukoBoard(n, board);

		System.out.format("Initial %d x %d Board {\n", n, n);
		System.out.print(soduko);
		System.out.println("}");
		
		Solver<SodukoMove> solver = new Solver<SodukoMove>();
		solver.setInitialState(soduko);
		// True since there can never be duplicate states
		solver.setRevisitStates(true);
		solver.solve();

		System.out.format("Solved in %d ms\n", solver.getSolveTime());
		System.out.format("States created: %d\n", solver.getStatesCreated());
		System.out.format("States visited: %d\n", solver.getStatesVisited());
		System.out.format("States duplicated: %d\n", solver.getStatesDuplicates());
		System.out.format("States pooled: %d\n", solver.getUniqueStates());
		System.out.format("States branched: %d\n", solver.getStatesDeviated());
		
		List<SodukoBoard> solutions = solver.getSolutions();
		
		System.out.format("Solutions (%d) {\n", solutions.size());
		for (int i = 0; i < solutions.size(); i++) {
			if (i > 0) System.out.println("AND ");
			System.out.print(solutions.get(i));
		}
		System.out.println("}");
		System.out.println();
	}
	
}
