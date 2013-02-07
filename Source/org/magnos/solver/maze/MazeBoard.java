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

package org.magnos.solver.maze;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.magnos.solver.AbstractState;
import org.magnos.solver.State;


/**
 * The state of a Maze.
 * 
 * @author Philip Diffenderfer
 *
 */
public class MazeBoard extends AbstractState<MazeMove>
{

	private static final int DIRS[][] = {
		{ 1, 0 }, // RIGHT
		{ -1, 0 }, // LEFT
		{ 0, 1 }, // BOTTOM
		{ 0, -1 }, // TOP
		{ 1, 1 }, // BOTTOM RIGHT
		{ -1, -1 }, // TOP LEFT
		{ -1, 1 }, // BOTTOM LEFT
		{ 1, -1 }, // TOP RIGHT
	};

	// The maze
	private MazeBoardType type;
	
	// The current position
	private int x, y;

	public MazeBoard( int x, int y, MazeBoardType type )
	{
		this.x = x;
		this.y = y;
		this.type = type;
	}

	@Override
	public Iterator<MazeMove> getMoves()
	{
		List<MazeMove> moves = new ArrayList<MazeMove>();
		
		for (byte i = 0; i < type.direction; i++)
		{
			int[] p = DIRS[i];
			int dx = x + p[0];
			int dy = y + p[1];
			
			if (type.free( dx, dy ))
			{
				moves.add( new MazeMove( i ) );
			}
		}
		
		return moves.iterator();
	}

	@Override
	public State<MazeMove> getCopy()
	{
		return new MazeBoard( x, y, type );
	}

	@Override
	public void addMove( MazeMove move )
	{
		x += DIRS[move.dir][0];
		y += DIRS[move.dir][1];
		type.maze[y][x] = false;
	}

	@Override
	public boolean isSolution()
	{
		return (x == type.tx && y == type.ty);
	}

	@Override
	public Object getHash()
	{
		return null;
	}

}
