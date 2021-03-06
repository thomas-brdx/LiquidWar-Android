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

import android.util.Log;

import java.util.Deque;

import fr.umlv.tbcv.liquidwar.input.GameInput;
import fr.umlv.tbcv.liquidwar.logic.pathfinding.AStar;
import fr.umlv.tbcv.liquidwar.logic.pathfinding.JumpPointFinder;
import fr.umlv.tbcv.liquidwar.logic.pathfinding.PathFinder;

/**
 * Implementation of Fighter using Nodes for pathfinding (interacting with LiquidNodeMap).
 */
public class NodeFighter extends Fighter {
    private Deque<Coordinates> path ;       // Path of coordinates that the fighter has to follow to reach the cursor. Recalculated every time the cursor moves
    private PathFinder pathFinder ;         // Path algorithm used to reach the cursor
    private Coordinates nextPosition ;      // Next coordinate that the fighter has to reach
    private Coordinates approximateCursor ; // Last good cursor position. Only updated when the real cursor has moved far away from that last position
    private LiquidNodeMap nodeMap ;         // The grid
    protected boolean isLeader ;            // If node is leader
    private NodeFighter leader ;            // Leader fighter to follow if current fighter isn't itself a leader
    private Squad squad ;                   // Current squad in which the fighter belongs to
    private Squad newSquad ;                // Next squad the fighter will belong to, used when the fighter has to change team

    public NodeFighter (LiquidMap lwmap, int team) {
        super(team) ;
        if(!(lwmap instanceof LiquidNodeMap)) {
            throw new RuntimeException() ;
        }
        nodeMap = (LiquidNodeMap) lwmap ;
        pathFinder = new JumpPointFinder(lwmap) ;
        isLeader = true ;
    }

    public NodeFighter (LiquidMap lwmap, int team, Fighter leader) {
        this(lwmap,team) ;
        if(leader == null || !(leader instanceof NodeFighter)) {
            throw new RuntimeException() ; // Should never happen
        }
        isLeader = false ;
        this.leader = (NodeFighter)leader ;
    }

    public int move(LiquidMap lwmap, Fighter[] fighters) {

        // Get a path JumpPointFinder
        if(isLeader) {
            computePath();
            nodeMap.resetNodes();

            // If after computing, no available paths were found, we don't move
            if(path == null || path.isEmpty()) {
                return 0;
            }

            if(nextPosition == null || (Coordinates.getSquareDistance(position,nextPosition) <= 3 )) {
                nextPosition = path.pop() ;
            }
        }
        else {
            nextPosition = leader.nextPosition ;
        }



        Coordinates tempPosition = new Coordinates(position.getX(), position.getY()) ;
        Coordinates idealPosition = new Coordinates(position.getX(), position.getY()) ;
        Coordinates finalPosition = new Coordinates(position.getX(), position.getY()) ;

        for (int i = position.getX() - 1 ; i <= position.getX() + 1 ; i ++ ) {
            tempPosition.setX(i) ;
            for ( int j = position.getY() - 1 ; j <= position.getY() + 1  ; j++ ) {
                tempPosition.setY( j ) ;
                if ( (nodeMap.isEmpty(tempPosition)) &&
                        Coordinates.getSquareDistance( tempPosition, nextPosition ) <
                                Coordinates.getSquareDistance( finalPosition, nextPosition) ) {
                    finalPosition.copyCoordinates( tempPosition ); // Figure out the next best possible position
                }
                if ( Coordinates.getSquareDistance( tempPosition, nextPosition ) <
                        Coordinates.getSquareDistance( idealPosition, nextPosition) ) {
                    idealPosition.copyCoordinates( tempPosition ); // Figure out the next best position regardless of possibility
                }
            }
        }

//        Log.e("FIGHTER","CURRENT POSITION" + position) ;
//        Log.e("FIGHTER","WANTED POSITION " + nextPosition) ;
//        Log.e("FIGHTER","FINAL POSITION " + finalPosition );

        // If the fighter hasnt been able to move, there is an obstacle/a soldier
        if(finalPosition.equals(position)) {
            if(nodeMap.hasFighter(idealPosition)) {
                Fighter obstacle = nodeMap.getFighter(idealPosition) ;
                if (isFriend(obstacle)) {
                    heal(obstacle) ;
                }
                else {
                    attack(obstacle) ;
                }
            }
            return 0;
        }
        // Fighter can move freely, its position is updated
        else {
            nodeMap.putSoldier(finalPosition, this) ;
            position.copyCoordinates(finalPosition);
        }
        return 1 ;
    }

    /**
     * move equivalent to the one in SimpleFighter for testing purposes
     * @param lwmap The grid
     * @param fighters The array of fighters (useless in here)
     */
    public void move2(LiquidMap lwmap, Fighter[] fighters) {
        if(!(lwmap instanceof LiquidNodeMap)) {
            throw new RuntimeException() ;
        }
        LiquidNodeMap nodeMap = (LiquidNodeMap) lwmap ;
        Coordinates cursor = GameInput.getPlayerCoordinate(team) ;
        Coordinates tempPosition = new Coordinates(position.getX(), position.getY()) ;
        Coordinates idealPosition = new Coordinates(position.getX(), position.getY()) ;
        Coordinates finalPosition = new Coordinates(position.getX(), position.getY()) ;

        for (int i = position.getX() - 1 ; i <= position.getX() + 1 ; i ++ ) {
            tempPosition.setX(i) ;
            for ( int j = position.getY() - 1 ; j <= position.getY() + 1  ; j++ ) {
                tempPosition.setY( j ) ;
                if ( (nodeMap.isEmpty(tempPosition)) &&
                        Coordinates.getSquareDistance( tempPosition, cursor ) <
                                Coordinates.getSquareDistance( finalPosition, cursor) ) {
                    finalPosition.copyCoordinates( tempPosition ); // Figure out the next best possible position
                }
                if ( Coordinates.getSquareDistance( tempPosition, cursor ) <
                        Coordinates.getSquareDistance( idealPosition, cursor) ) {
                    idealPosition.copyCoordinates( tempPosition ); // Figure out the next best position regardless of possibility
                }
            }
        }

//        Log.e("FIGHTER","CURRENT POSITION" + position) ;
//        Log.e("FIGHTER","WANTED POSITION " + nextPosition) ;

        // If the fighter hasnt been able to move, there is an obstacle/a soldier
        if(finalPosition.equals(position)) {
            if(nodeMap.hasFighter(idealPosition)) {
                Fighter obstacle = nodeMap.getFighter(idealPosition) ;
                if (isFriend(obstacle)) {
                    heal(obstacle) ;
                }
                else {
                    attack(obstacle) ;
                }
            }
            return ;
        }
        // Fighter can move freely, its position is updated
        else {
            nodeMap.putSoldier(finalPosition, this) ;
            position.copyCoordinates(finalPosition);
        }
    }

    /**
     * Attacks another fighter
     * @param ennemy Fighter to attack
     * @return The attacked fighter if he has switched team ;
     *         null otherwise
     */
    public Fighter attack (Fighter ennemy) {
        NodeFighter nodeEnnemy = (NodeFighter) ennemy ; // TODO Check for this, but should never happen unless programmer messes around
        nodeEnnemy.health -= damageAmount ;
        if(nodeEnnemy.health <= 0) {
            nodeEnnemy.health = (short)-team ; // Mark the fighter as "dead and switching to another team"
            nodeEnnemy.newSquad = this.squad ;
//            nodeEnnemy.health = FULL_HEALTH ;
//            nodeEnnemy.team = team ;

            // If the ennemy switches team, he also has to switch squad
//            if(nodeEnnemy.isLeader) {
//                nodeEnnemy.squad.changeLeader();
//            }
//            nodeEnnemy.squad.removeFighter(nodeEnnemy);
//            squad.addFighter(nodeEnnemy);
            return ennemy ;
        }
        return null ;
    }

    /**
     * Calculate a path between the soldier current position and the player's cursor
     *
     * @return 1 if a path has been found ;
     *         -1 if no path exists ;
     *         0 if the path doesn't need to be recalculated (follow previous path)
     */
    private int computePath () {
        Coordinates cursor = GameInput.getPlayerCoordinate(team) ;

        // Update approximate cursor
        if(approximateCursor == null) {
            approximateCursor = new Coordinates(cursor) ;
        }
        else {
            if( !approximateCursor.equals(cursor) || nodeMap.obstacleInPath(approximateCursor,cursor)) {
                approximateCursor.copyCoordinates(cursor);
            }
            //TODO Fix this. Supposed to avoid useless path recalculations but has weird side effets with JPS
//            else {
//                // No need to recalculate paths if the cursor hasnt moved since last time
//                return 0 ;
//            }
        }

        // Make sure we're not already at the destination
        if(position.equals(cursor)) {
            return 0 ;
        }
        path = pathFinder.finder(position,cursor) ;
        if(path != null && !path.isEmpty()) {
            path.pop(); // First node is useless (it's where we are right now)
            return 0 ;
        }
        return -1 ;
    }

    /** GETTERS / SETTERS **/
    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public Squad getNewSquad() {
        return newSquad;
    }

    public NodeFighter getLeader() {
        return leader;
    }

    public void setLeader(NodeFighter leader) {
        this.leader = leader;
    }

    public void turnIntoLeader() {
        isLeader = true ;
    }

    public void turnIntoGrunt() {
        isLeader = false ;
    }

}
