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

/**
 * A possible move in flip puzzle.
 * 
 * @author Philip Diffenderfer
 *
 */
public enum FlipMove
{
	UP		( 0, -1),
	DOWN	( 0,  1),
	LEFT	(-1,  0),
	RIGHT	( 1,  0);
	
	public final int dx;
	public final int dy;
	
	private FlipMove(int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
}
