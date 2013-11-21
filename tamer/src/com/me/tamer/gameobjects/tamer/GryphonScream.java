package com.me.tamer.gameobjects.tamer;


import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.tEvent;

public class GryphonScream extends StaticObject {
	private final float SCREAM_AREA_SIZE = 10.0f;
	private final float COOL_DOWN = 2.0f;
	private final float SCREAM_SPEED			= 18f;
	private final float STARTING_POINT_ADJUSTMENT = 1;
	private final int WAVE_AMOUNT		= 4;
	private final float WAVE_FREQUENCY = 0.08f;
	private final float WAVE_INITIAL_SIZE = 1f;
	
	//circle scream area
	private final float SCREAM_CIRCLE_RADIUS = 6.0f;
	private Tamer tamer					= null;
	private boolean active			= false;
	private Vector2 wormPos				= new Vector2();
	private Vector2 tamerPos			= new Vector2();
	private Vector2 griffonHead			= new Vector2();
	private Vector2 headingHelp			= new Vector2();
	private Vector2 newHeading			= new Vector2();
	private Vector3 screamDirection		= new Vector3(1,-1,0);
	private ArrayList<Vector3> soundWaves = new ArrayList<Vector3>();
	
	private SoundManager sound			= null;
	
	private boolean onCoolDown = false;
	
	public GryphonScream(Tamer tamer){
		this.tamer = tamer;
		//Z-index for drawing order
		setZindex(-1);
		setGraphics("scream");
		
		sound = SoundManager.instance();
	}
	
	public void debugDraw(ShapeRenderer shapeRenderer){
		//
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static", graphics);
		render.loadGraphics(graphics);
		//render.setColor(1, 0.2f, 0.5f, 1);
		setSize(getSize());
		setRenderType(graphics);
	}
	
	@Override
	public void update(float dt) {
		if(active){
			for(int i = 0 ; i < soundWaves.size() ; i++){
				soundWaves.get(i).add(screamDirection.tmp().mul(SCREAM_SPEED*dt));
				//if(soundWaves.get(i).len() > SCREAM_AREA_SIZE + STARTING_POINT_ADJUSTMENT) {
				if(griffonHead.tmp().add(soundWaves.get(i).x, soundWaves.get(i).y).dst(tamerPos) < 0.3f){
					soundWaves.remove(i);
				}
				
			if (soundWaves.size() == 0) active = false;		
			}
			
			
			/*
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
			}*/
		}

	}
	
	public void addWave(){
		Vector3 newWave = new Vector3();
		newWave.set(screamDirection.tmp().mul(WAVE_INITIAL_SIZE));
		soundWaves.add(newWave);
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
		if(onCoolDown)
			return;

		for(int i = 0 ; i < soundWaves.size() ; i++){
			soundWaves.get(i).set(0,0,0);
		}

		//headingHelp.set(tamer.getHeading().tmp().set(tamer.getHeading().x,tamer.getHeading().y*0.5f));
		//headingHelp.set(tamer.getHeading());
		griffonHead.set(tamer.getCenterPosition());//.tmp().add(headingHelp));
		griffonHead.add(new Vector2(-STARTING_POINT_ADJUSTMENT, STARTING_POINT_ADJUSTMENT));
		active = true;
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
		onCoolDown = true;
		EventPool.addEvent(new tEvent(this,"enable",COOL_DOWN,1));
		
		//start animation
		EventPool.addEvent(new tEvent(this,"addWave", WAVE_FREQUENCY, WAVE_AMOUNT));
		
	}

	public void enable(){
		onCoolDown = false;
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
