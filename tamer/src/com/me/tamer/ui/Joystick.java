package com.me.tamer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import com.me.tamer.core.Level;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.StaticRenderer;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.VectorHelper;

public class Joystick extends Actor /*implements UiElement*/{

	private ControlContainer inputcontroller = null;
	private UiRenderer renderer = null;
	private Tamer tamer = null;
	private Vector2 tamerPosition = null;
	private Environment environment = null;
	private Level level = null;
	private Vector2 movementAxis = null;
	
	private Matrix3 translate = new Matrix3().rotate(45);
	//Joystick variables
	Vector2 restingpoint 	= new Vector2(130,130);
	protected Vector2 delta			= null;
	private Vector2 deltaHelp		= null;
	private Vector2 input			= null;
	private Vector2 localCenter 	= null;
	protected Vector2 point			= null;
	private final float BUTTON_SIZE				= 250;
	float pointersize		= 30f;
	boolean pressed		= false;
	boolean movementDisabled = false;
	
	
	public Joystick(ControlContainer inputController) {
		this.inputcontroller = inputController;
		delta			= new Vector2(0,0);
		deltaHelp		= new Vector2(0,0);
		input			= new Vector2(0,0);
		localCenter 	= new Vector2(BUTTON_SIZE/2,BUTTON_SIZE/2);
		movementAxis	= new Vector2(0,0);
		point			= new Vector2(restingpoint.x,restingpoint.y);
		tamerPosition	= new Vector2();
		renderer 		= new UiRenderer();
		tamer 			= inputcontroller.getEnvironment().getTamer();
		environment		= inputcontroller.getEnvironment();
		renderer.loadGraphics("icon_scream_v6");
		renderer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		
		//set Actor variables
		setVisible(false);
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		renderer.setSize(BUTTON_SIZE, BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		renderer.draw(batch);
		renderer.setSize(pointersize,pointersize);
		renderer.setPosition(point);
		renderer.draw(batch);		
	}
	
	@Override
	public void act(float dt) {
		if (isVisible() && pressed){
			deltaHelp.set(delta);
			if(deltaHelp.len() > BUTTON_SIZE / 2){
				deltaHelp = delta.nor().mul(BUTTON_SIZE/2);
				point.set(restingpoint.tmp().add(delta));
			}
			if(movementDisabled)
				environment.getTamer().turn(deltaHelp);
			else{
				deltaHelp.mul(translate);
				checkBounds(deltaHelp.mul(dt));
			}	
			environment.moveCamera(environment.getTamer().getPosition());
		}	
	}
	
	
	public void checkBounds(Vector2 movement){
		Vector2 mapBounds = environment.getMapBounds();
		tamerPosition.set(environment.getTamer().getPosition());
	
		movement.set(IsoHelper.twoDToIso(movement));
		tamerPosition.set(IsoHelper.twoDToIso(tamerPosition));
		tamerPosition.add(movement);
		if(tamerPosition.x > mapBounds.x || tamerPosition.x < -mapBounds.x){
			Vector2 remove = VectorHelper.projection((mapBounds.tmp().set(1,0)),movement);
			movementAxis.set(1,0);
			movement.sub(VectorHelper.projection(movement,movementAxis));
		}
		if(tamerPosition.y > mapBounds.y || tamerPosition.y < -mapBounds.y){
			Vector2 remove = VectorHelper.projection((mapBounds.tmp().set(1,0)),movement);
			movementAxis.set(0,1);
			movement.sub(VectorHelper.projection(movement,movementAxis));
		}
		
		environment.getTamer().manouver(movement);	
	}
	
	public void createListener(){
		addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {	 
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2 ){
					point.set(x,y);
					pressed = true;
					return true;
				}
                return false;
	        }
	 
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        		pressed = false;
	                point.set(restingpoint);
	        }
	        
	        public void touchDragged(InputEvent event, float x, float y, int pointer){
				 point.set(x,y);
				 delta.set(point.tmp().sub(restingpoint));	
			 }
		});
	}
	
	public void disableMovement(){
		movementDisabled = true;
	}
	
	public void enableMovement(){
		movementDisabled = false;
	}
}
