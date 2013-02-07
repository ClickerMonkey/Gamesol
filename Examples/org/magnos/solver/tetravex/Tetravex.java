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

package org.magnos.solver.tetravex;

import java.util.List;

import org.magnos.solver.Solver;
import org.magnos.solver.tetravex.TetravexBoard;
import org.magnos.solver.tetravex.TetravexMove;
import org.magnos.solver.tetravex.TetravexPiece;



public class Tetravex 
{

	public static void main(String[] args) 
	{
		new Tetravex();
	}
	
	public Tetravex()
	{
		TetravexPiece[] pieces;
		short[][] board;
		
		
		pieces = new TetravexPiece[4];
		pieces[0] = new TetravexPiece(0, 0, 3, 1);
		pieces[1] = new TetravexPiece(0, 1, 4, 0);
		pieces[2] = new TetravexPiece(3, 0, 0, 2);
		pieces[3] = new TetravexPiece(4, 2, 4, 0);
		board = new short[][] {
				{-1, -1},
				{-1, -1},
		};
		solve(2, 2, pieces, board);
		
		
		pieces = new TetravexPiece[9];
		pieces[0] = new TetravexPiece(5, 6, 3, 3);
		pieces[1] = new TetravexPiece(1, 6, 5, 5);
		pieces[2] = new TetravexPiece(7, 5, 2, 1);
		pieces[3] = new TetravexPiece(7, 4, 5, 6);
		pieces[4] = new TetravexPiece(5, 3, 4, 2);
		pieces[5] = new TetravexPiece(3, 7, 6, 3);
		pieces[6] = new TetravexPiece(2, 2, 5, 1);
		pieces[7] = new TetravexPiece(5, 4, 9, 9);
		pieces[8] = new TetravexPiece(4, 3, 2, 4);
		board = new short[][] {
				{-1, -1, -1},
				{-1, -1, -1},
				{-1, -1, -1},
		};
		solve(3, 3, pieces, board);
		

		// http://www.kristen-arnesen.com/Filer/downloads/TetraVex.JPG
		
		pieces = new TetravexPiece[16];
		pieces[0] = new TetravexPiece(4, 3, 4, 5);
		pieces[1] = new TetravexPiece(4, 4, 1, 4);
		pieces[2] = new TetravexPiece(1, 5, 0, 1);
		pieces[3] = new TetravexPiece(0, 4, 1, 2);
		pieces[4] = new TetravexPiece(4, 1, 2, 0);
		pieces[5] = new TetravexPiece(2, 2, 0, 0);
		pieces[6] = new TetravexPiece(2, 5, 3, 5);
		pieces[7] = new TetravexPiece(1, 5, 2, 4);
		pieces[8] = new TetravexPiece(3, 5, 0, 5);
		pieces[9] = new TetravexPiece(3, 5, 1, 4);
		pieces[10] = new TetravexPiece(0, 3, 4, 3);
		pieces[11] = new TetravexPiece(0, 2, 3, 5);
		pieces[12] = new TetravexPiece(3, 5, 3, 4);
		pieces[13] = new TetravexPiece(0, 5, 4, 5);
		pieces[14] = new TetravexPiece(1, 4, 4, 3);
		pieces[15] = new TetravexPiece(4, 4, 3, 4);
		board = new short[][] {
				{-1, -1, -1, -1},
				{-1,  0,  1, -1},
				{-1,  2,  3, -1},
				{-1,  4,  5, -1}
		};
		solve(4, 4, pieces, board);
	}
	
	private void solve(int width, int height, TetravexPiece[] pieces, short[][] board)
	{
		TetravexBoard tetravex = new TetravexBoard(width, height, pieces, board);

		Solver<TetravexMove> solver = new Solver<TetravexMove>();
		solver.setInitialState(tetravex);
		solver.setRevisitStates(true);
		solver.solve();

		System.out.format("Solved in %d ms\n", solver.getSolveTime());
		System.out.format("States created: %d\n", solver.getStatesCreated());
		System.out.format("States visited: %d\n", solver.getStatesVisited());
		System.out.format("States duplicated: %d\n", solver.getStatesDuplicates());
		System.out.format("States pooled: %d\n", solver.getUniqueStates());
		System.out.format("States branched: %d\n", solver.getStatesDeviated());
		
		List<TetravexBoard> solutions = solver.getSolutions();
		
		System.out.format("Solutions (%d) {\n", solutions.size());
		for (TetravexBoard b : solutions) {
			System.out.println(b);
		}
		System.out.println("}");
		System.out.println();
	}
	
}
