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
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerStatic;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.tEvent;

public class GryphonScream extends DynamicObject {
	private final float SCREAM_AREA_SIZE = 8.0f;
	private final float COOL_DOWN = 1.5f;
	private final float SCREAM_SPEED			= 40f;
	private final float STARTING_POINT_ADJUSTMENT = 1;
	private final int WAVE_AMOUNT		= 30;
	private final float WAVE_FREQUENCY = 0.02f;//0.08f;
	
	//circle scream area
	private Tamer tamer					= null;
	private boolean animationActive			= false;
	private Vector2 wormPos				= new Vector2();
	private Vector2 tamerShadowPos			= new Vector2();
	private Vector2 griffonHead			= new Vector2();
	private Vector2 help			= new Vector2();
	private Vector2 help3			= new Vector2();
	private Vector2 help2			= new Vector2(1,-1);
	private Vector2 newHeading			= new Vector2();
	private Vector3 screamDirection		= new Vector3(1,-1,0);
	private ArrayList<Vector3> soundWaves = new ArrayList<Vector3>();
	
	private SoundManager sound			= null;
	private ControlContainer controls;
	
	private boolean onCoolDown = false;
	
	public GryphonScream(Tamer tamer){
		this.tamer = tamer;
		//Z-index for drawing order
		setZindex(-1);
		setGraphics(TamerStatic.SCREAM.getFileName());
		
		sound = SoundManager.instance();
		controls = ControlContainer.instance();
	}
	
	public void setGraphics(String graphics){
		//Renderer render = RenderPool.addRendererToPool("static", graphics);
		//render.loadGraphics(graphics);
		
		
		Renderer renderer = RenderPool.addRendererToPool("static",graphics);

		renderer.loadGraphics(graphics);
		renderer.setColor(1, 1f, 1f, 0.2f);
		setSize(getSize());
		setRenderType(graphics);
	}
	
	@Override
	public void update(float dt) {
		if(animationActive){
			for(int i = 0 ; i < soundWaves.size() ; i++){
				soundWaves.get(i).add(screamDirection.tmp().mul(SCREAM_SPEED*dt));
				
				help.set(soundWaves.get(i).x, soundWaves.get(i).y);
				
				if ( Helper.worldToScreen( help ).y < Helper.worldToScreen(help2).y - SCREAM_AREA_SIZE / 4){
					soundWaves.remove(i);
				}
				
			if (soundWaves.size() == 0) {
				animationActive = false;
				controls.getJoystick().enableMovement();
			}
			
			}
		}
	}
	
	public void addWave(){
		Vector3 newWave = new Vector3();
		newWave.set( screamDirection.tmp() );
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
		
		//draw direction wave
		if (soundWaves.size() > 0){
			for(int i = 0 ; i < soundWaves.size() ; i++){
				renderer.setSize(soundWaves.get(i).x,soundWaves.get(i).x  / 2);
				help.set( Helper.worldToScreen( tamerShadowPos ) );
				help.y += tamer.getShadow().getSize().y / 2;
				help.y = help.y - soundWaves.get(i).x / 4;
				renderer.setPosition(help);
				renderer.draw(batch);
			}
		}
		batch.setColor(Color.WHITE);
	}

	public void activate(){
		if(onCoolDown)
			return;
		
		for(int i = 0 ; i < soundWaves.size() ; i++){
			soundWaves.get(i).set(0,0,0);
		}

		//headingHelp.set(tamer.getHeading().tmp().set(tamer.getHeading().x,tamer.getHeading().y*0.5f));
		//headingHelp.set(tamer.getHeading());
		griffonHead.set(tamer.getPosition());//.getCenterPosition());//.tmp().add(headingHelp));
		griffonHead.add(new Vector2(-STARTING_POINT_ADJUSTMENT, STARTING_POINT_ADJUSTMENT));
		animationActive = true;
		sound.setVolume(0.7f);
		sound.play(TamerSound.HAWK);
		tamerShadowPos.set(tamer.getShadow().getPosition());
		
		help2.set(tamerShadowPos.tmp().sub(griffonHead));
	
		//Scream circle
		ArrayList<Creature> creatures = tamer.getEnvironment().getCreatures();
		
		for (int i = 0; i < creatures.size(); i++){	
			if(creatures.get(i).getClass() == Worm.class){
				
				Worm worm = ((Worm)creatures.get(i));
				wormPos.set(worm.getHead().getPosition());	
				
				if( wormPos.dst(tamerShadowPos) < SCREAM_AREA_SIZE){
					newHeading.set(wormPos.x - tamerShadowPos.x, wormPos.y - tamerShadowPos.y);
					newHeading.nor();
					worm.setHeading(newHeading);
					worm.doScreamEffect();
					EventPool.addEvent(new tEvent(worm,"doScreamEffect",0.5f,3));
				}
			}
		}	
		onCoolDown = true;
		EventPool.addEvent(new tEvent(this,"enable",COOL_DOWN,1));
			
		//update button
		controls.setScreamCooldown(true);
		
		//start animation
		EventPool.addEvent(new tEvent(this,"addWave", WAVE_FREQUENCY, WAVE_AMOUNT));
		
		controls.getJoystick().disableMovement();
	}

	public void enable(){
		onCoolDown = false;
		controls.setScreamCooldown(false);
	}
	
	
	@Override
	public boolean getDebug(){
		return false;
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}
}
