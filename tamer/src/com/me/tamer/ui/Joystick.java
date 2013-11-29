package com.me.tamer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.Environment.RunningState;
import com.me.tamer.gameobjects.renderers.UiRenderer;
import com.me.tamer.services.TextureManager.TamerStatic;


public class Joystick extends Actor{
	private final float BUTTON_SIZE	= 250;
	private final float ZOOM_IN_SPEED = 0.015f;
	private final float ZOOM_OUT_SPEED = 0.01f;
	private final float ZOOM_MIN_AMOUNT = 0.05f;
	private final float ZOOM_DEFAULT = 1.1f;
	private final float ZOOM_MAX_COEFFIENT = 0.007f;
	private ControlContainer controlContainer = null;
	private Environment environment = null;
	
	//Renderers
	private UiRenderer renderer_outer 		= new UiRenderer();
	private UiRenderer renderer_inner 		= new UiRenderer();
	
	//Joystick variables
	private Vector2 restingpoint 	= new Vector2(130,130);
	private Vector2 joystickPoint	= new Vector2(130,130);
	private Vector2 delta			= new Vector2(0,0);
	private Vector2 input			= new Vector2(0,0);
	private Vector2 localCenter 	= new Vector2(BUTTON_SIZE/2,BUTTON_SIZE/2);
	
	final float POINTER_SIZE				= 180f;
	boolean pressed					= false;
	boolean movementDisabled 		= false;
	boolean inputDisabled			= false;
	
	
	public Joystick(ControlContainer controls) {
		this.controlContainer = controls;
		
		environment		= controlContainer.getEnvironment();
		
		renderer_outer.loadGraphics(TamerStatic.JOYSTICK.getFileName());
		renderer_outer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer_outer.setPosition(restingpoint);
		
		renderer_inner.loadGraphics(TamerStatic.JOYSTICK_INNER.getFileName());
		renderer_inner.setSize(POINTER_SIZE,POINTER_SIZE);
		renderer_inner.setPosition(joystickPoint);
		
		//Actor variables
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
			renderer_outer.draw(batch);
			renderer_inner.setPosition(joystickPoint);
			renderer_inner.draw(batch);		
	}
	
	@Override
	public void act(float dt) {
		delta.set(joystickPoint.tmp().sub(restingpoint));
		
		if(delta.len() >= (BUTTON_SIZE - POINTER_SIZE / 2) / 2 ){
			delta = delta.nor().mul((BUTTON_SIZE - POINTER_SIZE / 2 ) / 2 );
			joystickPoint.set(restingpoint.tmp().add(delta));
		}
		
		//Zoom out camera when accelerating and in when braking		
		if (environment.getState() == RunningState.NORMAL){
			if (controlContainer.getStage().getCamera().zoom - ZOOM_MIN_AMOUNT > ZOOM_DEFAULT + ZOOM_MAX_COEFFIENT * delta.len())
				controlContainer.getStage().getCamera().zoom -= ZOOM_IN_SPEED;
			else if (controlContainer.getStage().getCamera().zoom + ZOOM_MIN_AMOUNT < ZOOM_DEFAULT + ZOOM_MAX_COEFFIENT * delta.len())
				controlContainer.getStage().getCamera().zoom += ZOOM_OUT_SPEED;
			
			if (pressed){
//				if(movementDisabled)
//					environment.getTamer().turn(delta);
//				else{			
					environment.getTamer().manouver(delta);
//				}	
			}
		}		
	}
	
	public void createListener(){
		addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {	 
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2){
					joystickPoint.set(x,y);
					pressed = true;
					return true;
				}
                return false;
	        }
	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        		pressed = false;
	        		joystickPoint.set(restingpoint);
	        }
	        
	        public void touchDragged(InputEvent event, float x, float y, int pointer){
	        	if(input.dst(localCenter) < BUTTON_SIZE / 2){
	        		joystickPoint.set(x,y);
	        	}
			 }
		});
	}
	
	public void disableMovement(){
		movementDisabled = true;	
	}
	
	public void enableMovement(){
		movementDisabled = false;
	}
	
	public Vector2 getDelta(){
		return delta;
	}
}
