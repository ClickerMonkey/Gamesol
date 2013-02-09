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

package org.magnos.solver.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.magnos.solver.AbstractState;
import org.magnos.solver.State;



public class SudokuBoard extends AbstractState<SudokuMove> implements Cloneable
{

	private int cellSize;
	private int size;
	private int filled;
	private short[][] board;

	private SudokuBoard()
	{
	}

	public SudokuBoard( int n, short[][] initialBoard )
	{
		cellSize = n;
		size = n * n;
		board = initialBoard;
		filled = 0;
		
		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				if (board[y][x] > 0)
				{
					filled++;
				}
			}
		}
	}

	@Override
	public Iterator<SudokuMove> getMoves()
	{
		List<SudokuMove> moves = new ArrayList<SudokuMove>();

		// rows[x] are all numbers that exist in row x
		byte rows[][] = new byte[size][size];
		// cols[x] are all numbers that exist in column x
		byte cols[][] = new byte[size][size];
		// quad[x] are all numbers that exist in quadrant x
		byte quad[][] = new byte[size][size];

		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				// If a number is here on the board...
				int number = board[y][x];

				if (number-- > 0)
				{
					int quadIndex = ((y / cellSize) * cellSize) + (x / cellSize);

					// Add this to its row, column, and quadrant
					rows[y][number] = 0x1;
					cols[x][number] = 0x1;
					quad[quadIndex][number] = 0x1;
				}
			}
		}

		// The location of the cell with the fewest number of possible numbers
		short minX = 0;
		short minY = 0;

		// The total possible numbers at this cell
		int minCount = size;

		// when min[x] = 0 then (x + 1) can be placed here
		byte min[] = new byte[size];

		byte turn[] = new byte[size];

		for (short y = 0; y < size; y++)
		{
			for (short x = 0; x < size; x++)
			{
				// If this cell is empty...
				if (board[y][x] == 0)
				{
					// The index of this cells quadrant
					int quadIndex = ((y / cellSize) * cellSize) + (x / cellSize);

					// The total available numbers to place here
					int maxCount = size;

					for (int i = 0; i < size; i++)
					{
						// Mark this as 1 if i exists in the row, column, or quadrant.
						turn[i] = (byte)(rows[y][i] | cols[x][i] | quad[quadIndex][i]);
						maxCount -= turn[i];
					}

					// If nothing can be put in this cell, no moves can be made
					if (maxCount == 0)
					{
						return null;
					}

					// If this cell has less moves....
					if (maxCount < minCount)
					{
						// Update min move
						minCount = maxCount;
						minX = x;
						minY = y;

						for (int i = 0; i < size; i++)
						{
							min[i] = turn[i];
						}
					}

				}
			}
		}

		for (short i = 1; i <= size; i++)
		{
			// If this number can be placed...
			if (min[i - 1] == 0)
			{
				// Add the move as a possibility.
				moves.add( new SudokuMove( minX, minY, i ) );
			}
		}

		return moves.iterator();
	}

	@Override
	public State<SudokuMove> getCopy()
	{
		SudokuBoard copy = new SudokuBoard();
		copy.size = size;
		copy.filled = filled;
		copy.cellSize = cellSize;
		copy.board = new short[size][];

		for (int i = 0; i < size; i++)
		{
			copy.board[i] = Arrays.copyOf( board[i], size );
		}

		return copy;
	}

	@Override
	public void addMove( SudokuMove move )
	{
		board[move.y][move.x] = move.number;
		filled++;
	}

	@Override
	public boolean isSolution()
	{
		return (filled == size * size);
	}

	@Override
	public Object getHash()
	{
		throw new RuntimeException();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				if (x > 0)
				{
					sb.append( " " );
				}
				
				sb.append( board[y][x] );
			}
			sb.append( "\n" );
		}
		return sb.toString();
	}

}
