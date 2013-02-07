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

import java.util.Arrays;


public class PegsHash
{

	private int[] hashes;
	private int hash;

	public PegsHash( PegsBoardType type, boolean[][] board )
	{
		hashes = new int[(type.places.length >> 5) + 1];
		
		int hashIndex = 0;
		int k = 0;
		
		for (PegsPoint p : type.places)
		{
			hashes[hashIndex] |= (board[p.y][p.x] ? 1 : 0) << k;
			
			if (++k == 32)
			{
				k = 0;
				hashIndex++;
			}
		}

		hash = hashes[0];
		
		for (int i = 1; i < hashes.length; i++)
		{
			hash ^= hashes[i];
		}
	}

	@Override
	public int hashCode()
	{
		return hash;
	}

	@Override
	public boolean equals( Object o )
	{
		if (o instanceof PegsHash)
		{
			PegsHash h = (PegsHash)o;
			return (hash == h.hash && Arrays.equals( hashes, h.hashes ));
		}
		
		return false;
	}

}
