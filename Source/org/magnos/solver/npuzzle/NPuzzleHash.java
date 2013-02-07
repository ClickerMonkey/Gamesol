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

public class NPuzzleHash
{

	private int hash;
	private int[][] board;

	public NPuzzleHash( int[][] board )
	{
		this.board = board;
	}

	@Override
	public int hashCode()
	{
		return hash;
	}

	@Override
	public boolean equals( Object o )
	{
		if (o instanceof NPuzzleHash)
		{
			NPuzzleHash h = (NPuzzleHash)o;
			for (int y = 0; y < board.length; y++)
			{
				for (int x = 0; x < board[y].length; x++)
				{
					if (board[y][x] != h.board[y][x])
					{
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

}
