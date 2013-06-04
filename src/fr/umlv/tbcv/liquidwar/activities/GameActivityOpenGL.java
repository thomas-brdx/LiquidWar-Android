package fr.umlv.tbcv.liquidwar.activities;

import fr.umlv.tbcv.liquidwar.display.LiquidWarRenderer;
import fr.umlv.tbcv.liquidwar.input.GameInput;
import fr.umlv.tbcv.liquidwar.logic.Coordinates;
import fr.umlv.tbcv.liquidwar.logic.LiquidWorld;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class GameActivityOpenGL extends Activity {
	
	private GLSurfaceView myGLView ;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		myGLView = new MyGLSurfaceView(this);
		
		
		setContentView(myGLView);
	}
	
	
	
	public class MyGLSurfaceView extends GLSurfaceView {
		
	    private final LiquidWarRenderer myRenderer ;


		public MyGLSurfaceView(Context context) {
			super(context);
			
			setEGLContextClientVersion(2);
			
			// Set the Renderer for drawing on the GLSurfaceView
			myRenderer = new LiquidWarRenderer() ;
	        setRenderer(myRenderer);	
//	        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		}
		
		@Override
	    public boolean onTouchEvent(MotionEvent e) {
//	        Log.e("OriginalCursor", "("+ x + "," + y + ")") ;

            // Every touch on the screen can move up to one player cursor
            for(int touches = 0 ; touches < e.getPointerCount() ; touches++ ) {
                int x = (int) e.getX( e.getPointerId(touches)) ;
                int y = (int) e.getY( e.getPointerId(touches)) ;
                Coordinates touchCoordinate = new Coordinates( x* LiquidWorld.getGamewidth()  / this.getWidth() , LiquidWorld.getGameheight() - ( (y* LiquidWorld.getGameheight() / this.getHeight()) ) ) ;

                for(int i = 0 ; i < GameInput.getNbPlayers() ; i++) {
                    Coordinates playerCoordinate = GameInput.getPlayerCoordinate(i) ;

//                    Log.e("DIST", "Distance entre toucher et player "+i+" = "+ Coordinates.getSquareDistance(touchCoordinate, playerCoordinate )) ;
                    if( Coordinates.getSquareDistance(touchCoordinate, playerCoordinate ) < 25 ) {
                        GameInput.setPlayersCoordinates(i,touchCoordinate);
                        // Have to break here so that the touch coordinate only modifies one player cursor
                        break ;
                    }
                }
            }

//            int x = (int) e.getX( e.getPointerId(0) );
//            int y = (int) e.getY( e.getPointerId(0) );
//            Coordinates nextCoordinates = new Coordinates( x* LiquidWorld.getGamewidth()  / this.getWidth() , LiquidWorld.getGameheight() - ( (y* LiquidWorld.getGameheight() / this.getHeight()) ) );
//
//            Log.e("DIST", "Distance entre toucher et player 0 : " + Coordinates.getSquareDistance(GameInput.getPlayerCoordinate(0), nextCoordinates)) ;
//            GameInput.setPlayersCoordinates(0,nextCoordinates);
//	        Log.e("CustomCursor", "PLAYER @ "+ GameInput.getPlayerCoordinate(0) ) ;
	        

	        return true;
	    }
	}
	
	
	
}
