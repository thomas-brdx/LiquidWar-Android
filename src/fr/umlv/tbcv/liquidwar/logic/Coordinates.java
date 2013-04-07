package fr.umlv.tbcv.liquidwar.logic;

public class Coordinates {
	private int x,y,z ;
	
	public Coordinates () {
		x = 0 ;
		y = 0 ;
		y = 0 ;
	}
	
	public Coordinates (int x, int y) {
		this.x = x ;
		this.y = y ;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * Calculate the square distance between 2 positions
	 * @param a	The first position
	 * @param b The second position
	 * @return	The square distance between the 2 positions
	 */
	public static int getSquareDistance (Coordinates a, Coordinates b) {
		if ( a == null || b == null ) {
			return -1 ;
		}
		
		return ( (b.getX() - a.getX()) * (b.getX() - a.getX()) ) +
				( (b.getY() - a.getY()) * (b.getY() - a.getY()) ) ;

	}
}