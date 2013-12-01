package com.me.tamer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.UiRenderer;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.services.TextureManager.TamerStatic;


public class ScreamButton extends Actor{
	private final float BUTTON_SIZE						= 180;
	private ControlContainer controlContainer 			= null;
	private UiRenderer renderer 						= null;
	private UiRenderer renderer2 						= null;
	private UiRenderer currentRenderer					= null;
	private Tamer tamer 								= null;
	private Environment environment 					= null;
	
	//Button variables
//	Vector2 restingpoint 	= new Vector2(Gdx.graphics.getWidth() - 110,200);
	final Vector2 restingpoint = new Vector2(Gdx.graphics.getWidth() - 85,240);
	Vector2 delta			= new Vector2(0,0);
	private Vector2 input			= new Vector2(0,0);
	private Vector2 localCenter 	= new Vector2(BUTTON_SIZE / 2, BUTTON_SIZE / 2);
	boolean pressed		= false;
	private boolean onCooldown = false;
	

	public ScreamButton(ControlContainer controls) {
		this.controlContainer = controls;
		environment		= controlContainer.getEnvironment();
		tamer 			= environment.getTamer();
		renderer 		= new UiRenderer();
		
		renderer.loadGraphics(TamerStatic.BUTTON_SCREAM.getFileName());
		renderer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		
		//2nd renderer
		renderer2 		= new UiRenderer();
		renderer2.loadGraphics(TamerStatic.BUTTON_SCREAM_GLOW.getFileName());
		renderer2.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer2.setPosition(restingpoint);
		
		//set current renderer
		currentRenderer = renderer;
		
		//Actor variables
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		createListener();
			
	}
	
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (onCooldown) currentRenderer = renderer;
		else currentRenderer = renderer2;
				
		currentRenderer.setSize(BUTTON_SIZE, BUTTON_SIZE);
		currentRenderer.setPosition(restingpoint);
		currentRenderer.draw(batch);
	}
	
	public void setOnCooldown(boolean b){
		onCooldown = b;
	}
	
	public boolean isOnCooldown(){
		return onCooldown;
	}
	
	public void createListener(){
		addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2){ 
	                if (tamer == null) tamer = environment.getTamer();
	                if (tamer != null){
		        		tamer.useScream();
	                }
	                return true;
				}
				else return false;
	        }
		});
	}	
}

