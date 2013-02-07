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

package org.magnos.solver;

/**
 * An abstract state which implements the depth and parent state.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <M>
 *        The move type.
 */
public abstract class AbstractState<M> implements State<M>
{

	// The depth of the state
	private int depth;

	// The parent state
	private State<M> parent;

	@Override
	public int getDepth()
	{
		return depth;
	}

	@Override
	public State<M> getParent()
	{
		return parent;
	}

	@Override
	public void setDepth( int depth )
	{
		this.depth = depth;
	}

	@Override
	public void setParent( State<M> parent )
	{
		this.parent = parent;
	}

}
