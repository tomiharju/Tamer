package com.me.tamer.gameobjects.tamer;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.creatures.WormPart;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.me.tamer.utils.tTimer;

public class GryphonScream extends DynamicObject {
	private final float SCREAM_AREA_WIDTH = 4.0f;
	private final float SCREAM_AREA_LENGTH = 4.0f;
	private Environment environment 				= null;
	private boolean isActive			= false;
	private Vector2 position			= new Vector2();
	private Vector2 size				= new Vector2(10,10);
	
	private Vector2 screamVert1 		= null;
	private Vector2 screamVert2 		= null;
	private Vector2 screamVert3 		= null;
	
	private Vector2 drawVert1			= null;
	private Vector2 drawVert2			= null;
	private Vector2 drawVert3			= null;
	
	private Vector2 wormPos1			= null;
	private Vector2 wormPos2			= null;
	private Vector2 wormPos3			= null;
	
	private Vector2 wormPos				= null;
	private Vector2 tamerPos			= null;
	private Vector2 tamerHead			= null;
	private Vector2 newHeading			= null;
	
	//ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	public GryphonScream(Environment environment){
		screamVert1 = new Vector2();
		screamVert2 = new Vector2();
		screamVert3 = new Vector2();
		
		drawVert1 = new Vector2();
		drawVert2 = new Vector2();
		drawVert3 = new Vector2();
		
		wormPos1 = new Vector2();
		wormPos2 = new Vector2();
		wormPos3 = new Vector2();
		
		wormPos = new Vector2();
		tamerPos = new Vector2();
		tamerHead = new Vector2();
		newHeading = new Vector2();
		
		//Z-index for drawing order
		setZindex(-1);
		setGraphics();
		
	}
	
	public void debugDraw(ShapeRenderer shapeRenderer){
		shapeRenderer.setProjectionMatrix(environment.getStage().getCamera().combined);
		shapeRenderer.setColor(1, 1, 1, 1);
		
		drawVert1.set(IsoHelper.twoDToTileIso(screamVert1));
		drawVert2.set(IsoHelper.twoDToTileIso(screamVert2));
		drawVert3.set(IsoHelper.twoDToTileIso(screamVert3));
		
	
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(drawVert1.x, drawVert1.y, drawVert2.x, drawVert2.y );
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(drawVert1.x, drawVert1.y, drawVert3.x, drawVert3.y );
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.line(drawVert2.x, drawVert2.y, drawVert3.x, drawVert3.y );
		shapeRenderer.end();
	}
	
	
	public void setGraphics(){
		Renderer render = RenderPool.addRendererToPool("animated","scream");
		render.loadGraphics("scream_ph",4,1);
		setSize(new Vector2(3,3));
		renderType = "scream";
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Scream graphics are set");
		
	}
	
	@Override
	public void update(float dt) {
		
		if(isActive){
			
			tamerPos.set(environment.getTamer().getShadow().getPosition());
			tamerHead.set(environment.getTamer().getHeading());
			screamVert1.set(tamerPos);
			screamVert2.set(tamerPos);
			screamVert2.x += tamerHead.x * SCREAM_AREA_LENGTH - tamerHead.y * SCREAM_AREA_WIDTH;
			screamVert2.y += tamerHead.y * SCREAM_AREA_LENGTH + tamerHead.x * SCREAM_AREA_WIDTH;
			
			screamVert3.set(tamerPos);
			screamVert3.x += tamerHead.x * SCREAM_AREA_LENGTH + tamerHead.y * SCREAM_AREA_WIDTH;
			screamVert3.y += tamerHead.y * SCREAM_AREA_LENGTH - tamerHead.x * SCREAM_AREA_WIDTH;
			ArrayList<Creature> creatures = environment.getCreatures();
			
			for (int i = 0; i < creatures.size(); i++){	
				if(creatures.get(i).getClass() == Worm.class){
					
					Worm worm = ((Worm)creatures.get(i));
		
					wormPos.set(worm.getHead().getPosition());		
					wormPos1.set(wormPos.x - screamVert1.x, wormPos.y - screamVert1.y);
					wormPos2.set(wormPos.x - screamVert2.x, wormPos.y - screamVert2.y);
					wormPos3.set(wormPos.x - screamVert3.x, wormPos.y - screamVert3.y);
					
					float cross1 = wormPos1.crs(screamVert2.x - screamVert1.x, screamVert2.y - screamVert1.y);
					float cross2 = wormPos2.crs(screamVert3.x - screamVert2.x, screamVert3.y - screamVert2.y);
					float cross3 = wormPos3.crs(screamVert1.x - screamVert3.x, screamVert1.y - screamVert3.y);
					
					//Check with cross-product if WormHead is inside scream-area;
					if( cross1 > 0 && cross2 > 0 && cross3 > 0){
						newHeading.set(wormPos.x - tamerPos.x, wormPos.y - tamerPos.y);
						newHeading.nor();
						worm.getHead().setHeading(newHeading);
					}
				}	
			}	
		}	
	}

	public void wakeUp(Environment level){
		this.environment = level;
		markAsActive();
	}
	
	public void activate(){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Scream activated");
		isActive = true;
		tTimer timer = new tTimer(this,"deactivateScream",1);
		timer.start();
	}

	public void deactivateScream(){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Scream timer completed. Returning to object pool.");
		isActive = false;
		markAsCarbage();
		RuntimeObjectFactory.addToObjectPool("scream",this);
	}
	
	//Debug is on
	@Override
	public boolean getDebug(){
		return true;
	}
	
	public Vector2 getPosition(){
		return position;
	}

	public Vector2 getSize(){
		return size;
	}
}
