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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.magnos.solver.AbstractState;
import org.magnos.solver.State;

/**
 * The state of a Flip Puzzle.
 * 
 * @author Philip Diffenderfer
 *
 */
public class FlipPuzzle extends AbstractState<FlipMove>
{	
	public static final int TILE_NONE = 0;
	public static final int TILE_FLIPPED = 1;
	public static final int TILE_FINAL = 2;
	
	// True if tile exists, false for out of bounds
	private boolean[][] traversable;
	
	// True if flipped correctly, false otherwise
	private boolean[][] board;
	
	// The number of remaining tiles that need to be flipped
	private int remaining;
	
	// Where the cursor currently exists
	private int cursorX, cursorY;
	
	/**
	 * Creates a new FlipPuzzle given the cursor position and a map where 0 is 
	 * not traversable, 1 is a tile in the flipped state and 2 is a tile in the 
	 * final flipped state.
	 * 
	 * @param cursorX The column the cursor is in.
	 * @param cursorY The row the cursor is in.
	 * @param map A 2d dimensional map of tile states.
	 */
	public FlipPuzzle(int cursorX, int cursorY, int[][] map)
	{
		this.cursorX = cursorX;
		this.cursorY = cursorY;
		
		int height = map.length;
		int width = map[ 0 ].length;
		
		this.board = new boolean[ height ][ width ];
		this.traversable = new boolean[ height ][ width ];
		this.remaining = 0;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				this.board[y][x] = ( map[y][x] == TILE_FINAL );
				this.traversable[y][x] = ( map[y][x] != TILE_NONE );
				
				if ( map[y][x] == TILE_FLIPPED )
				{
					this.remaining++;
				}
			}
		}
	}
	
	/**
	 * Creates a clone of a Flip Puzzle.
	 * 
	 * @param cursorX
	 * @param cursorY
	 * @param traversable
	 * @param board
	 * @param remaining
	 */
	private FlipPuzzle(int cursorX, int cursorY, boolean[][] traversable, boolean[][] board, int remaining)
	{
		this.cursorX = cursorX;
		this.cursorY = cursorY;
		this.traversable = traversable;
		this.board = board;
		this.remaining = remaining;
	}
	
	@Override
	public boolean isSolution()
	{
		return remaining == 0;
	}

	@Override
	public Iterator<FlipMove> getMoves()
	{
		ArrayList<FlipMove> moves = new ArrayList<FlipMove>();
		
		for (FlipMove fm : FlipMove.values())
		{
			if ( isTraversable( fm.dx, fm.dy ) )
			{
				moves.add( fm );
			}
		}
		
		return moves.iterator();
	}
	
	private boolean isTraversable(int dx, int dy)
	{
		final boolean[][] t = traversable;
		final int rx = cursorX + dx;
		final int ry = cursorY + dy;
		
		return ry >= 0 && ry < t.length && rx >= 0 && rx < t[ ry ].length && t[ ry ][ rx ];
	}

	@Override
	public State<FlipMove> getCopy()
	{
		final int height = board.length;
		final int width = board[ 0 ].length;
		final boolean[][] copy = new boolean[ height ][ width ];
		
		for (int y = 0; y < height; y++)
		{
			copy[y] = Arrays.copyOf( board[y], width );
		}
		
		return new FlipPuzzle(cursorX, cursorY, traversable, copy, remaining);
	}

	@Override
	public void addMove( FlipMove move )
	{
		cursorX += move.dx;
		cursorY += move.dy;
		
		if ( board[ cursorY ][ cursorX ] = !board[ cursorY ][ cursorX ] )
		{
			remaining--;
		}
		else
		{
			remaining++;
		}
	}

	@Override
	public Object getHash()
	{
		return new FlipHash( cursorX, cursorY, traversable, board );
	}
	
	@Override
	public int getRemainingMoves()
	{
		return remaining;
	}
	
	@Override
	public String toString()
	{
		final int height = board.length;
		final int width = board[ 0 ].length;
		final StringBuilder sb = new StringBuilder( height * (width + 1) );
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if ( !traversable[y][x] )
				{
					sb.append(' ');
				}
				else if ( board[y][x] )
				{
					sb.append('X');
				}
				else
				{
					sb.append('O');
				}
			}
			
			sb.append('\n');
		}
		
		return sb.toString();
	}

}
