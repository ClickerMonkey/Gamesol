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

/**
 * A Maze with walls, a target position, and possible moving directions.
 * 
 * @author Philip Diffenderfer
 *
 */
public class MazeBoardType
{

	public static final int AXIS = 4;
	public static final int DIAGONAL = 8;

	/**
	 * What directions the solver can move, either AXIS (4) or DIAGONAL (8).
	 */
	public int direction;
	
	/**
	 * Maze walls where true is a wall and false is a walkway.
	 */
	public boolean[][] maze;
	
	/**
	 * Target position in maze.
	 */
	public int tx, ty;

	public MazeBoardType( int tx, int ty, int direction, boolean[][] maze )
	{
		this.tx = tx;
		this.ty = ty;
		this.direction = direction;
		this.maze = maze;
	}

	public boolean free( int x, int y )
	{
		return !(x < 0 || y < 0 || x >= maze[0].length || y >= maze.length) && (maze[y][x] == true);
	}

}
