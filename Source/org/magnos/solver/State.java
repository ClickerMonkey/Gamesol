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

import java.util.Iterator;


/**
 * The basic interface for implementing Game State.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <M>
 *        The class which represents a move.
 */
public interface State<M>
{

	/**
	 * Returns true if this state is a solution to the problem, else false.
	 */
	public boolean isSolution();

	/**
	 * Returns a list of all possible moves for this state.
	 */
	public Iterator<M> getMoves();

	/**
	 * Gets an exact copy of this State.
	 */
	public State<M> getCopy();

	/**
	 * Applies the given Move to this State.
	 * 
	 * @param move
	 *        The move to apply.
	 */
	public void addMove( M move );

	/**
	 * Sets the parent state.
	 * 
	 * @param parent
	 *        The parent of this state.
	 */
	public void setParent( State<M> parent );

	/**
	 * Returns the parent state.
	 */
	public State<M> getParent();

	/**
	 * Sets the depth of this state.
	 * 
	 * @param depth
	 *        The depth of this state.
	 */
	public void setDepth( int depth );

	/**
	 * Returns the depth of this state.
	 */
	public int getDepth();

	/**
	 * Returns the hash of this State. The hash object returned must have the
	 * hashCode() and equals() methods implemented to accurately compare against
	 * another hash object. If state A and B are the same state, then their hash
	 * objects will having matching hashCode() values and A.equals(B) will return
	 * true (same applies for B.equals(A)).
	 */
	public Object getHash();
	
	/**
	 * Tries to estimate the minimum number of remaining moves until the state
	 * is solved. If this can't be calculated then -1 should be returned. This
	 * is especially useful when you set a maximum search depth in a Solver
	 * and you want to weed out states that don't have a chance being solvable
	 * in time.
	 * 
	 * @return
	 */
	public int getRemainingMoves();

}
