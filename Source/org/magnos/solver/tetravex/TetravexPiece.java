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

public class TetravexPiece
{

	public static final int EMPTY = -1;
	public static final int LEFT = 0;
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;

	public int[] s = new int[4];
	public int index;

	public TetravexPiece( int left, int top, int right, int bottom )
	{
		s[LEFT] = left;
		s[TOP] = top;
		s[RIGHT] = right;
		s[BOTTOM] = bottom;
	}

	public int get( int index )
	{
		return s[index & 3];
	}

	public boolean match( TetravexPiece ideal )
	{
		if (ideal.s[LEFT] != EMPTY && ideal.s[LEFT] != s[LEFT])
		{
			return false;
		}
		if (ideal.s[TOP] != EMPTY && ideal.s[TOP] != s[TOP])
		{
			return false;
		}
		if (ideal.s[TOP] != EMPTY && ideal.s[TOP] != s[TOP])
		{
			return false;
		}
		if (ideal.s[BOTTOM] != EMPTY && ideal.s[BOTTOM] != s[BOTTOM])
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder( 40 );
		sb.append( "+-----+\n" );
		sb.append( "|\\ " ).append( s[TOP] ).append( " /|\n" );
		sb.append( "|" ).append( s[LEFT] ).append( " X " ).append( s[RIGHT] ).append( "|\n" );
		sb.append( "|/ " ).append( s[BOTTOM] ).append( " \\|\n" );
		sb.append( "+-----+\n" );
		return sb.toString();
	}

}
