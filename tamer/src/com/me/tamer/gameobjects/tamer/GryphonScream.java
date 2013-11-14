package com.me.tamer.gameobjects.tamer;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.tEvent;

public class GryphonScream extends StaticObject {
	private final float SCREAM_AREA_WIDTH = 8.0f;
	private final float SCREAM_AREA_LENGTH = 8.0f;
	
	//circle scream area
	private final float SCREAM_CIRCLE_RADIUS = 6.0f;
	private Tamer tamer					= null;
	private boolean isActive			= false;
	private Vector2 wormPos				= new Vector2();
	private Vector2 tamerPos			= new Vector2();
	private Vector2 griffonHead			= new Vector2();
	private Vector2 headingHelp			= new Vector2();
	private Vector2 newHeading			= new Vector2();
	private Vector3 screamDirection		= new Vector3(1,-1,0);
	private ArrayList<Vector3> soundWaves = new ArrayList<Vector3>();
	private float screamSpeed			= 2f;
	private SoundManager sound			= null;
	
	private boolean isOnCooldown = false;
	
	public GryphonScream(Tamer tamer){
		this.tamer = tamer;
		//Z-index for drawing order
		setZindex(-1);
		setGraphics("scream");
		
		
		for(int i = 0 ;i < 8 ; i++)
			soundWaves.add(new Vector3(0,0,0));
		sound = SoundManager.instance();
	}
	
	public void debugDraw(ShapeRenderer shapeRenderer){
		//
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static", graphics);
		render.loadGraphics(graphics);
		setSize(getSize());
		setRenderType(graphics);
	}
	
	@Override
	public void update(float dt) {
		if(isActive){
			for(int i = 0 ; i < soundWaves.size() ; i++){
				if(soundWaves.get(i).len() < 10)
					soundWaves.get(i).add(screamDirection.tmp().mul(screamSpeed*i*dt));
				if(soundWaves.get(i).len() > 10){
					if(i == soundWaves.size() - 1  ){
						isActive = false;
						for(int k = 0 ; k < soundWaves.size() ; k++)
							soundWaves.get(k).set(0,0,1);
					}
					else
						soundWaves.get(i).set(0,0,1);
					
				}
			}
		}

	}
	public void draw(SpriteBatch batch){
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		for(int i = 0 ; i < soundWaves.size() ; i++){
			if(soundWaves.get(i).z == 0){
				renderer.setSize(soundWaves.get(i).x,soundWaves.get(i).x  / 2);
				renderer.setPosition(Helper.worldToScreen(griffonHead.tmp().add(soundWaves.get(i).x,soundWaves.get(i).y)));
				renderer.draw(batch);
			}
			
		}
	}

	
	public void activate(){
		if(isOnCooldown)
			return;

		for(int i = 0 ; i < soundWaves.size() ; i++){
			soundWaves.get(i).set(0,0,0);
		}

		headingHelp.set(tamer.getHeading().tmp().set(tamer.getHeading().x,tamer.getHeading().y*0.5f));
		griffonHead.set(tamer.getCenterPosition().tmp().add(headingHelp.mul(2)));
		isActive = true;
		sound.setVolume(0.7f);
		sound.play(TamerSound.HAWK);
		tamerPos.set(tamer.getShadow().getPosition());
	
		//Scream circle
		ArrayList<Creature> creatures = tamer.getEnvironment().getCreatures();
		
		for (int i = 0; i < creatures.size(); i++){	
			if(creatures.get(i).getClass() == Worm.class){
				
				Worm worm = ((Worm)creatures.get(i));
				wormPos.set(worm.getHead().getPosition());	
				
				if( wormPos.dst(tamerPos) < SCREAM_CIRCLE_RADIUS){
					newHeading.set(wormPos.x - tamerPos.x, wormPos.y - tamerPos.y);
					newHeading.nor();
					worm.setHeading(newHeading);
					worm.doScreamEffect();
					EventPool.addEvent(new tEvent(worm,"doScreamEffect",0.5f,3));
				}
			}
		}	
		isOnCooldown = true;
		EventPool.addEvent(new tEvent(this,"enable",3.14f,1));
	}

	public void enable(){
		isOnCooldown = false;
	}
	
	
	@Override
	public boolean getDebug(){
		return false;
	}





	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}
	

}
