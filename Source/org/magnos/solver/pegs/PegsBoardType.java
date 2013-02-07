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

import java.util.ArrayList;
import java.util.List;


public class PegsBoardType
{

	public static final int SHAPE_HEXAGON = 6;
	public static final int SHAPE_SQUARE = 8;

	private static final boolean _ = true;
	private static final boolean X = false;

	public static final boolean[][] MASK_FRENCH = {
		{ X, X, _, _, _, X, X },
		{ X, _, _, _, _, _, X },
		{ _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _ },
		{ X, _, _, _, _, _, X },
		{ X, X, _, _, _, X, X },
	};

	public static final boolean[][] MASK_GERMAN = {
		{ X, X, X, _, _, _, X, X, X },
		{ X, X, X, _, _, _, X, X, X },
		{ X, X, X, _, _, _, X, X, X },
		{ _, _, _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _, _, _ },
		{ X, X, X, _, _, _, X, X, X },
		{ X, X, X, _, _, _, X, X, X },
		{ X, X, X, _, _, _, X, X, X },
	};

	public static final boolean[][] MASK_BELL = {
		{ X, X, _, _, _, X, X, X },
		{ X, X, _, _, _, X, X, X },
		{ X, X, _, _, _, X, X, X },
		{ _, _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _, _ },
		{ X, X, _, _, _, X, X, X },
		{ X, X, _, _, _, X, X, X },
	};

	public static final boolean[][] MASK_ENGLISH = {
		{ X, X, _, _, _, X, X },
		{ X, X, _, _, _, X, X },
		{ _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _ },
		{ _, _, _, _, _, _, _ },
		{ X, X, _, _, _, X, X },
		{ X, X, _, _, _, X, X },
	};

	public static final boolean[][] MASK_DIAMOND = {
		{ X, X, X, X, _, X, X, X, X },
		{ X, X, X, _, _, _, X, X, X },
		{ X, X, _, _, _, _, _, X, X },
		{ X, _, _, _, _, _, _, _, X },
		{ _, _, _, _, _, _, _, _, _ },
		{ X, _, _, _, _, _, _, _, X },
		{ X, X, _, _, _, _, _, X, X },
		{ X, X, X, _, _, _, X, X, X },
		{ X, X, X, X, _, X, X, X, X },
	};

	public static final boolean[][] MASK_TRIANGLE = {
		{ _, X, X, X, X },
		{ _, _, X, X, X },
		{ _, _, _, X, X },
		{ _, _, _, _, X },
		{ _, _, _, _, _ },
	};

	public final int width;
	public final int height;
	public final int shape;
	public final boolean[][] mask;
	public final PegsPoint[] places;

	public PegsBoardType( int shape, boolean[][] mask )
	{
		this.shape = shape;
		this.mask = mask;
		this.height = mask.length;
		this.width = mask[0].length;

		List<PegsPoint> placeList = getPlaces();

		this.places = new PegsPoint[placeList.size()];

		for (int i = 0; i < placeList.size(); i++)
		{
			this.places[i] = placeList.get( i );
		}
	}

	private List<PegsPoint> getPlaces()
	{
		List<PegsPoint> placeList = new ArrayList<PegsPoint>();
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < height; x++)
			{
				if (mask[y][x] == _)
				{
					placeList.add( new PegsPoint( x, y ) );
				}
			}
		}
		return placeList;
	}

	public boolean on( int x, int y )
	{
		return !(x < 0 || y < 0 || x >= width || y >= height) && (mask[y][x] == _);
	}

}
