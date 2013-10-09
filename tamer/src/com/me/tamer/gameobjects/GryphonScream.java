package com.me.tamer.gameobjects;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.Interactable;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class GryphonScream extends DynamicObject {
	private final float SCREAM_AREA_WIDTH = 2.0f;
	private final float SCREAM_AREA_LENGTH = 4.0f;
	private Level level 				= null;
	private Tamer tamer					= null;
	private boolean isActive			= false;
	
	private Vector2 screamVert1 		= null;
	private Vector2 screamVert2 		= null;
	private Vector2 screamVert3 		= null;
	
	private Vector2 drawVert1			= null;
	private Vector2 drawVert2			= null;
	private Vector2 drawVert3			= null;
	
	private Vector2 wormPos				= null;
	private Vector2 tamerPos			= null;
	private Vector2 tamerHead			= null;
	
	//Debug
	private ShapeRenderer shapeRndr;
	
	public GryphonScream(){
		screamVert1 = new Vector2();
		screamVert2 = new Vector2();
		screamVert3 = new Vector2();
		
		drawVert1 = new Vector2();
		drawVert2 = new Vector2();
		drawVert3 = new Vector2();
		
		wormPos = new Vector2();
		tamerPos = new Vector2();
		tamerHead = new Vector2();
	}
	
	@Override
	public void draw(SpriteBatch batch){
		
	}
	
	@Override 
	public void debugDraw(ShapeRenderer shapeRndr){
		
		drawVert1.set(IsoHelper.twoDToIso(screamVert1));
		drawVert2.set(IsoHelper.twoDToIso(screamVert2));
		drawVert3.set(IsoHelper.twoDToIso(screamVert3));
		
		shapeRndr.setColor(1, 1, 1, 1);
		
		shapeRndr.begin(ShapeType.Line);
		shapeRndr.line(drawVert1.x, drawVert1.y, drawVert2.x, drawVert2.y );
		shapeRndr.end();
		
		shapeRndr.begin(ShapeType.Line);
		shapeRndr.line(drawVert1.x, drawVert1.y, drawVert3.x, drawVert3.y );
		shapeRndr.end();
		
		shapeRndr.begin(ShapeType.Line);
		shapeRndr.line(drawVert1.x, drawVert1.y, drawVert3.x, drawVert3.y );
		shapeRndr.end();
		
		shapeRndr.begin(ShapeType.Line);
		shapeRndr.line(drawVert2.x, drawVert2.y, drawVert3.x, drawVert3.y );
		shapeRndr.end();
			
	}
	
	@Override
	public void update(float dt) {
		if (isActive){
			ArrayList<Interactable> creatures = level.getCreatures();
			for (int i = 0; i < creatures.size(); i++){	
				if(creatures.get(i).getClass() == WormPart.class){
					WormPart wopa = ((WormPart)creatures.get(i));
					if(wopa.getPartName() == "Head"){
						
						wormPos.set(wopa.getPosition());		
						tamerPos.set(level.getTamer().getPosition());
						tamerHead.set(level.getTamer().getHeading());
						
						//Scream area is a triangle
						screamVert1.set(tamerPos);
						
						screamVert2.set(tamerPos);
						screamVert2.x += tamerHead.x * SCREAM_AREA_LENGTH - tamerHead.y * SCREAM_AREA_WIDTH;
						screamVert2.y += tamerHead.y * SCREAM_AREA_LENGTH + tamerHead.x * SCREAM_AREA_WIDTH;
						
						screamVert3.set(tamerPos);
						screamVert3.x += tamerHead.x * SCREAM_AREA_LENGTH + tamerHead.y * SCREAM_AREA_WIDTH;
						screamVert3.y += tamerHead.y * SCREAM_AREA_LENGTH - tamerHead.x * SCREAM_AREA_WIDTH; 
						
						//float cross1 = wormPos.crs(screamVert1.x - screamVert2.x, screamVert1.y - screamVert2.y);
						float cross2 = wormPos.crs(screamVert2.x - screamVert3.x, screamVert2.y - screamVert3.y);
						//float cross3 = wormPos.crs(screamVert3.x - screamVert1.x, screamVert3.y - screamVert1.y);
			
						System.out.println(cross2);
						//System.out.println(cross1 +", " +cross2 +", " +cross3);
						//System.out.println(wormPos.crs(screamVert2.tmp().sub(screamVert3.tmp())));
						
						//Check with cross-product if WormHead is inside scream area;
						//if( cross1 < 0 && cross2 < 0 && cross3 < 0){
							//System.out.println("sisäl ollaan.");
						//}
					}
				}		
			}
		}		
	}
	
	public void wakeUp(Level level){
		this.level = level;
		markAsActive();
	}
	
	public void activate(){
		System.out.println("skriieeek");
		isActive = true;
		markAsCarbage();
	}
	
	public void addToPool(){
		RuntimeObjectFactory.addToObjectPool("scream",this);
	}
	
	//Debug is on
	@Override
	public boolean getDebug(){
		return true;
	}
}
