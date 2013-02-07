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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.magnos.solver.AbstractState;
import org.magnos.solver.State;



/**
 * The state of a Peg Solitaire.  
 *  
 * @author Philip Diffenderfer
 * 
 */
public class PegsBoard extends AbstractState<PegsMove> 
{
	
	public static final boolean HOLE = false;
	public static final boolean PEG = true;

	private static final PegsPoint DIRS[] = {
		new PegsPoint(-1,  0),	// LEFT
		new PegsPoint( 1,  0),	// RIGHT
		new PegsPoint( 0, -1),	// TOP
		new PegsPoint( 0,  1),	// BOTTOM
		new PegsPoint(-1, -1),	// TOP LEFT
		new PegsPoint( 1,  1),	// BOTTOM RIGHT
		new PegsPoint( 1, -1),	// TOP RIGHT
		new PegsPoint(-1,  1),	// BOTTOM LEFT 
	};
	
	private int pegs;
	private boolean board[][];
	private PegsBoardType type;

	private PegsBoard()
	{
	}

	public PegsBoard( PegsBoardType type, boolean board[][] )
	{
		this.type = type;
		this.board = board;
		this.pegs = 0;
		
		for (PegsPoint p : type.places)
		{
			if (board[p.y][p.x] == PEG)
			{
				pegs++;
			}
		}
	}

	public int getHoleCount()
	{
		return type.places.length;
	}

	public int getPegCount()
	{
		return pegs;
	}

	public boolean is( boolean value, int x, int y )
	{
		return (type.on( x, y ) && board[y][x] == value);
	}

	@Override
	public Iterator<PegsMove> getMoves()
	{
		List<PegsMove> moves = new ArrayList<PegsMove>();
		for (PegsPoint p : type.places)
		{
			// If this is a peg...
			if (board[p.y][p.x] == PEG)
			{
				// Check all directions for a peg then a hole to jump into.
				for (int i = 0; i < type.shape; i++)
				{
					int mx = p.x + DIRS[i].x;
					int my = p.y + DIRS[i].y;
					int hx = p.x + DIRS[i].x * 2;
					int hy = p.y + DIRS[i].y * 2;
					
					if (is( PEG, mx, my ) && is( HOLE, hx, hy ))
					{
						moves.add( new PegsMove( p.x, p.y, hx, hy ) );
					}
				}
			}
		}
		return moves.iterator();
	}

	@Override
	public void addMove( PegsMove move )
	{
		// Source and center places are empty, target is now filled with source.
		board[move.sy][move.sx] = HOLE;
		board[move.cy][move.cx] = HOLE;
		board[move.ty][move.tx] = PEG;
		pegs--;
	}

	@Override
	public State<PegsMove> getCopy()
	{
		PegsBoard copy = new PegsBoard();
		copy.pegs = pegs;
		copy.type = type;
		copy.board = new boolean[board.length][];
		for (int i = 0; i < board.length; i++)
		{
			copy.board[i] = Arrays.copyOf( board[i], board[i].length );
		}
		return copy;
	}

	@Override
	public boolean isSolution()
	{
		return (pegs == 1);
	}

	@Override
	public Object getHash()
	{
		return new PegsHash( type, board );
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < board.length; y++)
		{
			for (int x = 0; x < board[y].length; x++)
			{
				if (x > 0)
				{
					sb.append( ' ' );
				}

				sb.append( board[y][x] ? 'O' : '-' );
			}
			sb.append( '\n' );
		}

		return sb.toString();
	}

}
