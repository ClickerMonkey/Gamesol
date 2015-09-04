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

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;


/**
 * A Deque with a togglable FIFO option.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <E>
 *        The element type.
 */
public class ConcurrentDeque<E> extends LinkedBlockingDeque<E>
{

	// First-In-First-Out?
	private final boolean fifo;
	
	// Timeout information
	private final long timeout;
	private final TimeUnit unit;

	/**
	 * Instantiates a new Deque.
	 * 
	 * @param fifo
	 *        True if this Deque is first-in-first-out, or false if this Deque is
	 *        first-in-last-out.
	 */
	public ConcurrentDeque( boolean fifo, long timeout, TimeUnit unit )
	{
		this.fifo = fifo;
		this.timeout = timeout;
		this.unit = unit;
	}

	@Override
	public E poll()
	{
		try
		{
			return (fifo ? pollFirst( timeout, unit ) : pollLast( timeout, unit ));
		}
		catch (InterruptedException e)
		{
			return null;
		}
	}

	@Override
	public boolean add( E element )
	{
		return (fifo ? offerLast( element ) : offerFirst( element ));
	}

}
