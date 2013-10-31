package com.me.tamer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.UiRenderer;

public class Joystick extends Actor{
	private final float BUTTON_SIZE	= 250;
	private final float ZOOM_SPEED = 0.01f;
	private final float ZOOM_MIN_AMOUNT = 0.05f;
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
	
	float pointersize				= 180f;
	boolean pressed					= false;
	boolean movementDisabled 		= false;
	boolean inputDisabled			= false;
	
	
	public Joystick(ControlContainer inputController) {
		this.controlContainer = inputController;
		
		environment		= controlContainer.getEnvironment();
		
		renderer_outer.loadGraphics("joystick");
		renderer_outer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer_outer.setPosition(restingpoint);
		
		renderer_inner.loadGraphics("joystick_inner");
		renderer_inner.setSize(pointersize,pointersize);
		renderer_inner.setPosition(joystickPoint);
		
		//Actor variables
		setVisible(false);
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
		
		if(delta.len() > (BUTTON_SIZE - pointersize/2) / 2){
			delta = delta.nor().mul((BUTTON_SIZE - pointersize/2)/2);
			joystickPoint.set(restingpoint.tmp().add(delta));

		}
		
		//Zoom out camera when accelerating and in when braking
		if (controlContainer.getStage().getCamera().zoom - ZOOM_MIN_AMOUNT > 1 + 0.003f * delta.len())
			controlContainer.getStage().getCamera().zoom -= ZOOM_SPEED * 1.5f;
		else if (controlContainer.getStage().getCamera().zoom + ZOOM_MIN_AMOUNT < 1 + 0.003f * delta.len())
			controlContainer.getStage().getCamera().zoom += ZOOM_SPEED;
		
		if (isVisible() && pressed){
			if(movementDisabled)
				environment.getTamer().turn(delta.mul(dt));
			else{			
				environment.getTamer().manouver(delta.mul(dt));
			}	
		}		
	}
	
	public void createListener(){
		addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {	 
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2){
					if(!inputDisabled)joystickPoint.set(x,y);
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
	        	if(!inputDisabled)joystickPoint.set(x,y);
			 }
		});
	}
	
	public void disableMovement(){
		movementDisabled = true;	
	}
	
	public void enableMovement(){
		movementDisabled = false;
	}
	
	public void setInputDisabled(boolean b){
		inputDisabled = b;
	}
	
	public Vector2 getDelta(){
		return delta;
	}
}
