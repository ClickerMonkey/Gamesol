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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.magnos.solver.AbstractState;
import org.magnos.solver.State;


import static org.magnos.solver.tetravex.TetravexPiece.*;


public class TetravexBoard extends AbstractState<TetravexMove>
{

	private TetravexPiece[] pieces;

	private boolean[] placed;
	private int width;
	private int height;
	private int placedCount;
	private short[][] board;

	private TetravexBoard()
	{
	}

	public TetravexBoard( int width, int height, TetravexPiece[] pieces, short[][] board )
	{
		this.width = width;
		this.height = height;
		this.pieces = pieces;
		this.board = board;
		this.placedCount = 0;
		this.placed = new boolean[width * height];
		
		for (int i = 0; i < pieces.length; i++)
		{
			pieces[i].index = i;
		}
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (board[y][x] >= 0)
				{
					placedCount++;
					placed[board[y][x]] = true;
				}
			}
		}
	}

	public boolean exists( int x, int y )
	{
		return !(x < 0 || y < 0 || x >= width || y >= height);
	}

	public boolean alone( int x, int y )
	{
		return (
				(!exists( x - 1, y ) || board[y][x - 1] == EMPTY) &&
				(!exists( x + 1, y ) || board[y][x + 1] == EMPTY) &&
				(!exists( x, y - 1 ) || board[y - 1][x] == EMPTY) && 
				(!exists( x, y + 1 ) || board[y + 1][x] == EMPTY)
		);
	}

	public boolean isPiece( int x, int y )
	{
		return (exists( x, y ) && board[y][x] != EMPTY);
	}

	private TetravexPiece getIdeal( int x, int y )
	{
		TetravexPiece ideal = new TetravexPiece( EMPTY, EMPTY, EMPTY, EMPTY );
		
		if (isPiece( x - 1, y ))
		{
			ideal.s[LEFT] = pieces[board[y][x - 1]].s[RIGHT];
		}
		if (isPiece( x + 1, y ))
		{
			ideal.s[RIGHT] = pieces[board[y][x + 1]].s[LEFT];
		}
		if (isPiece( x, y - 1 ))
		{
			ideal.s[TOP] = pieces[board[y - 1][x]].s[BOTTOM];
		}
		if (isPiece( x, y + 1 ))
		{
			ideal.s[BOTTOM] = pieces[board[y + 1][x]].s[TOP];
		}
		
		return ideal;
	}

	@Override
	public Iterator<TetravexMove> getMoves()
	{
		List<TetravexMove> moves = new ArrayList<TetravexMove>();

		// Handle the case of an empty board
		if (placedCount == 0)
		{
			for (int i = 0; i < placed.length; i++)
			{
				moves.add( new TetravexMove( i, 0, 0 ) );
			}
			return moves.iterator();
		}

		// In worst case there will be size^2 number of matches, which means
		// the board is empty.
		int minMatches = width * height + 1;

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				// If this place is empty but NOT alone (has no neighbors)
				if (board[y][x] == EMPTY && !alone( x, y ))
				{
					// Determine the ideal piece. The ideal piece can have
					// negative sides noting that anything can match this side
					TetravexPiece ideal = getIdeal( x, y );
					// Find all remaining pieces that can fit here
					List<TetravexPiece> matches = new ArrayList<TetravexPiece>();
					
					for (int i = 0; i < placed.length; i++)
					{
						if (!placed[i] && pieces[i].match( ideal ))
						{
							matches.add( pieces[i] );
						}
					}
					
					// If the number of matches for this is the next best
					// position then update the number of moves.
					if (matches.size() < minMatches)
					{
						minMatches = matches.size();
						moves.clear();
						
						for (TetravexPiece p : matches)
						{
							moves.add( new TetravexMove( p.index, x, y ) );
						}
					}
				}
			}
		}
		
		return moves.iterator();
	}

	@Override
	public void addMove( TetravexMove move )
	{
		placed[move.pieceIndex] = true;
		board[move.y][move.x] = (short)move.pieceIndex;
		placedCount++;
	}

	@Override
	public State<TetravexMove> getCopy()
	{
		TetravexBoard copy = new TetravexBoard();
		copy.width = width;
		copy.height = height;
		copy.pieces = pieces;
		copy.placedCount = placedCount;
		copy.placed = Arrays.copyOf( placed, placed.length );
		copy.board = new short[height][];
		
		for (int i = 0; i < height; i++)
		{
			copy.board[i] = Arrays.copyOf( board[i], board[i].length );
		}
		
		return copy;
	}

	@Override
	public boolean isSolution()
	{
		return (placedCount == width * height);
	}

	@Override
	public Object getHash()
	{
		return null;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				sb.append( "+-----" );
			}
			sb.append( "+\n" );

			for (int x = 0; x < width; x++)
			{
				sb.append( "|" );
				if (board[y][x] >= 0)
				{
					TetravexPiece p = pieces[board[y][x]];
					sb.append( "\\ " ).append( p.s[TOP] ).append( " /" );
				}
				else
				{
					sb.append( "     " );
				}
			}
			sb.append( "|\n" );

			for (int x = 0; x < width; x++)
			{
				sb.append( "|" );
				if (board[y][x] >= 0)
				{
					TetravexPiece p = pieces[board[y][x]];
					sb.append( p.s[LEFT] ).append( " X " ).append( p.s[RIGHT] );
				}
				else
				{
					sb.append( "     " );
				}
			}
			sb.append( "|\n" );

			for (int x = 0; x < width; x++)
			{
				sb.append( "|" );
				if (board[y][x] >= 0)
				{
					TetravexPiece p = pieces[board[y][x]];
					sb.append( "/ " ).append( p.s[BOTTOM] ).append( " \\" );
				}
				else
				{
					sb.append( "     " );
				}
			}
			sb.append( "|\n" );
		}

		for (int x = 0; x < width; x++)
		{
			sb.append( "+-----" );
		}
		sb.append( "+\n" );

		return sb.toString();
	}

}
