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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.magnos.solver.AbstractState;
import org.magnos.solver.State;


/**
 * The state of an N-Puzzle.
 * 
 * @author Philip Diffenderfer
 *
 */
public class NPuzzleBoard extends AbstractState<NPuzzleMove>
{

	private int openX;
	private int openY;
	private int prevX;
	private int prevY;
	private int width;
	private int height;
	private int[][] board;

	private NPuzzleBoard()
	{
	}

	public NPuzzleBoard( int[][] board )
	{
		this.width = board[0].length;
		this.height = board.length;
		this.board = board;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (board[y][x] == 0)
				{
					this.openX = x;
					this.openY = y;
					this.prevX = x;
					this.prevY = y;
					break;
				}
			}
		}
	}

	public boolean exists( int x, int y )
	{
		return !(x < 0 || y < 0 || x >= width || y >= height) && (x != prevX || y != prevY);
	}

	@Override
	public Iterator<NPuzzleMove> getMoves()
	{
		List<NPuzzleMove> moves = new ArrayList<NPuzzleMove>();
		
		if (exists( openX + 1, openY ))
		{
			moves.add( new NPuzzleMove( openX + 1, openY ) );
		}
		if (exists( openX - 1, openY ))
		{
			moves.add( new NPuzzleMove( openX - 1, openY ) );
		}
		if (exists( openX, openY + 1 ))
		{
			moves.add( new NPuzzleMove( openX, openY + 1 ) );
		}
		if (exists( openX, openY - 1 ))
		{
			moves.add( new NPuzzleMove( openX, openY - 1 ) );
		}
		
		return moves.iterator();
	}

	@Override
	public void addMove( NPuzzleMove move )
	{
		board[openY][openX] = board[move.y][move.x];
		board[move.y][move.x] = 0;
		prevX = openX;
		prevY = openY;
		openX = move.x;
		openY = move.y;
	}

	@Override
	public State<NPuzzleMove> getCopy()
	{
		NPuzzleBoard copy = new NPuzzleBoard();
		copy.openX = openX;
		copy.openY = openY;
		copy.prevX = prevX;
		copy.prevY = prevY;
		copy.width = width;
		copy.height = height;
		copy.board = new int[height][];
		
		for (int y = 0; y < height; y++)
		{
			copy.board[y] = Arrays.copyOf( board[y], width );
		}
		
		return copy;
	}

	@Override
	public boolean isSolution()
	{
		int value = 1;
		int size = width * height;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (board[y][x] != value)
				{
					return false;
				}
				
				value = (value + 1) % size;
			}
		}
		
		return true;
	}

	@Override
	public Object getHash()
	{
		return new NPuzzleHash( board );
	}

}
