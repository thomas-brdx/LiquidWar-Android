/*
 Copyright (C) 2013 Thomas Bardoux, Christophe Venevongsos

 This file is part of Liquid Wars Android

 Liquid Wars Android is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Liquid Wars Android is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package fr.umlv.tbcv.liquidwar.logic;

import java.util.List;

/**
 * The Map of the game, or the grid.
 */
public interface LiquidMap {

    /**
     * Charge a map from a file.
     * (File structure for map still undecided, so for now the maps are hardcoded)
     */
    public void loadMap();

    /**
     * Sets an empty block on the grid at the specified coordinates
     * @param coord Coordinates to set as empty
     */
	public void clear (Coordinates coord) ;
    /**
     * Sets an obstacle on the grid at the specified coordinates
     * @param coord The obstacle coordinates
     */
	public void putObstacle (Coordinates coord) ;
    /**
     * Sets a fighter on the grid at the specified coordinates
     * @param f The fighter to move
     */
	public void putSoldier (Coordinates coord, Fighter f);

    public int getWidth() ;
    public int getHeight();
    public int countObstacles();

    public boolean isEmpty (Coordinates pos) ;
    public boolean hasFighter (Coordinates pos) ;
    public boolean hasObstacle (Coordinates pos) ;
    public boolean hasObstacle (int x, int y) ;

    public List<Coordinates> getNeighbors (Coordinates cell) ;
}
