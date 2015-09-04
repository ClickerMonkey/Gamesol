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

import java.util.BitSet;

/**
 * A hash for a Flip Puzzle - to avoid duplicate states.
 * 
 * @author Philip Diffenderfer
 *
 */
public class FlipHash
{
	
	private BitSet board;
	private int cursorX, cursorY;
	
	public FlipHash(int cursorX, int cursorY, boolean[][] traversable, boolean[][] board)
	{
		this.cursorX = cursorX;
		this.cursorY = cursorY;
		this.board = new BitSet();
		
		final int height = board.length;
		final int width = board[ 0 ].length;
		
		int tiles = 0;
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if ( traversable[y][x] )
				{
					this.board.set( tiles++, board[y][x] );
				}
			}
		}
	}

	@Override
	public int hashCode()
	{
		return cursorX ^ ~cursorY ^ board.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
		FlipHash other = (FlipHash)o;
		
		return other.cursorX == cursorX &&
			    other.cursorY == cursorY &&
			    other.board.equals( board );
	}
	
}
