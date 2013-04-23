package fr.umlv.tbcv.liquidwar.logic;

import fr.umlv.tbcv.liquidwar.input.GameInput;

public class LiquidWorld  {
	public static final int gameWidth = 45 ;
	public static final int gameHeight = 80 ;
	public static int playerNumber = 2 ;
	private LiquidSimpleMap lwmap ;
	
	boolean gameOn = true ;
	private Player player ;
	private SimpleArmies armies ;
	
	
	public LiquidWorld() {
		lwmap = new LiquidSimpleMap( gameWidth , gameHeight) ;
		player = new Player() ;
		armies = new SimpleArmies(lwmap) ;
		
		new GameInput() ;
	}
	
	/**
	 * Realize a game turn (Every fighter moves one pixel at most)
	 */
	public void turn() {
		
		// Update Player position
		player.getPosition().setX( GameInput.getxPlayer() ) ;
		player.getPosition().setY( GameInput.getyPlayer() ) ;
		
		// Every fighter decides its next position and moves
		armies.move( lwmap ) ;
	}
	
	
	/*                   GETTERS / SETTERS                 */
	public static int getGamewidth() {
		return gameWidth;
	}
	public static int getGameheight() {
		return gameHeight;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public SimpleArmies getArmies() {
		return armies;
	}
	public void setArmies(SimpleArmies armies) {
		this.armies = armies;
	}
	
	
	/*														*/
	
}
