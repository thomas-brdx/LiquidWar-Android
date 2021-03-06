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

public class SimpleArmies implements Armies {
	
	public static final int fighterNumber = 300 ;
	private Fighter[] fighters ;
	private int[] fightersPosition ;
    private int nbArmies = 1 ;
	LiquidSimpleMap lwmap ;
	
	
	public SimpleArmies ( LiquidSimpleMap lwmap , int nbArmies ) {
		this.nbArmies = nbArmies ;
		this.lwmap = lwmap ;

		fighters = new SimpleFighter[ fighterNumber ] ;
		initArmy() ;

		// 2 slots ( one for X, the other for Y ) for each fighter
		fightersPosition = new int[fighterNumber * 2] ;
	}

    /**
     * Initializes the position of every fighter
     * Every fighter should be regrouped with its friends
     * from the same Army in a corner of the map.
     */
	private void initArmy() {
		int j  = 3 ;
		int fakeWidth = 20 ;
        Coordinates tmp = new Coordinates() ;

		for ( int i = 0 ; i < fighterNumber/2 ; i++ ) {
            tmp.setCoordinates(i%(fakeWidth+1) , j);
            if(lwmap.hasObstacle(tmp)) { continue ; }
			fighters[i] = new SimpleFighter(i+1,0);
			fighters[i].getPosition().setX( i% (fakeWidth + 1 )) ;
			fighters[i].getPosition().setY(  j ) ;

//			Log.e("FighterPos", "Fighter init at" + fighters[i].getPosition() ) ;

			if ( i >= fakeWidth && i % fakeWidth == 0 ) {
				j++ ;
			}
		}

        j = lwmap.getHeight() - 3 ;
        for ( int i = fighterNumber/2 ; i < fighterNumber ; i++ ) {
            tmp.setCoordinates(i%(fakeWidth+1) , j);
            if(lwmap.hasObstacle(tmp)) { continue ; }
            fighters[i] = new SimpleFighter(i+1,1);
            fighters[i].getPosition().setX( i% (fakeWidth + 1 )) ;
            fighters[i].getPosition().setY( j ) ;

//			Log.e("FighterPos", "Fighter init at" + fighters[i].getPosition() ) ;

            if ( i >= fakeWidth && i % fakeWidth == 0 ) {
                j-- ;
            }
        }
	}

	@Override
	public void move() {
		for ( Fighter f : fighters) {
            if(f != null) {
			    f.move( lwmap, fighters ) ;
            }
		}
	}

	
	/* GETTER / SETTERS */

    @Override
	public int[] getFightersPosition (int team) {
		int i = 0 ;
		
		for ( Fighter f : fighters )
		{
            if(f!= null && f.team == team || team == -1) {
                fightersPosition[i++] = f.getPosition().getX() ;
                fightersPosition[i++] = f.getPosition().getY() ;
            }
		}
		return fightersPosition ;
	}
	
	public Fighter[] getFighters () {
		return fighters ;
	}


	@Override
	public int getFightersNumber(int team) {
        int i = 0 ;
        for ( Fighter f : fighters )
        {
            if(f != null && f.team == team) {
                i++ ;
            }
        }
        return i ;
	}

    @Override
    public int getFightersNumber() {
        return fighterNumber ;
    }

    @Override
    public int getArmiesNumber() {
        return nbArmies ;
    }
}
