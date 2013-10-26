package com.me.tamer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.core.Level;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.VectorHelper;

public class Joystick extends Actor /*implements UiElement*/{

	private ControlContainer inputcontroller = null;
	private UiRenderer renderer_outer = null;
	private UiRenderer renderer_inner = null;

	private Tamer tamer 			= null;
	private Vector2 tamerPosition 	= null;
	private Environment environment = null;
	private Level level 			= null;
	private Vector2 movementAxis 	= null;
	
	private Matrix3 translate 		= new Matrix3().rotate(45);
	//Joystick variables
	Vector2 restingpoint 			= new Vector2(130,130);
	protected Vector2 delta			= null;
	private Vector2 input			= null;
	private Vector2 moveTemp		= new Vector2();
	private Vector2 localCenter 	= null;
	protected Vector2 point			= null;
	private final float BUTTON_SIZE	= 250;
	float pointersize				= 180f;
	boolean pressed					= false;
	boolean movementDisabled 		= false;
	
	
	public Joystick(ControlContainer inputController) {
		this.inputcontroller = inputController;
		delta			= new Vector2(0,0);
		input			= new Vector2(0,0);
		localCenter 	= new Vector2(BUTTON_SIZE/2,BUTTON_SIZE/2);
		movementAxis	= new Vector2(0,0);
		point			= new Vector2(restingpoint.x,restingpoint.y);
		tamerPosition	= new Vector2();
		renderer_outer 		= new UiRenderer();
		renderer_inner 		= new UiRenderer();
		tamer 			= inputcontroller.getEnvironment().getTamer();
		environment		= inputcontroller.getEnvironment();
		renderer_outer.loadGraphics("joystick");
		renderer_outer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer_outer.setPosition(restingpoint);
		
		renderer_inner.loadGraphics("joystick_inner");
		renderer_inner.setSize(pointersize,pointersize);
		renderer_inner.setPosition(restingpoint);
		
		//set Actor variables
		setVisible(false);
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
	
		renderer_outer.draw(batch);
		renderer_inner.draw(batch);		
	}
	
	@Override
	public void act(float dt) {
		if (isVisible() && pressed){
			delta.set(point.tmp().sub(restingpoint));
			if(delta.len() > (BUTTON_SIZE - pointersize/2) / 2){
				delta = delta.nor().mul((BUTTON_SIZE - pointersize/2)/2);
				point.set(restingpoint.tmp().add(delta));
			}
			if(movementDisabled)
				environment.getTamer().turn(delta);
			else{
				delta.mul(translate);
				checkBounds(delta.mul(dt));

			}	
			environment.moveCamera(environment.getTamer().getPosition());
		}	
	}
	

	
	public void checkBounds(Vector2 movement){
		Vector2 mapBounds = environment.getMapBounds();
		tamerPosition.set(environment.getTamer().getShadow().getPosition());
		movement.set(IsoHelper.twoDToTileIso(movement));
		tamerPosition.set(IsoHelper.twoDToTileIso(tamerPosition));
		tamerPosition.add(movement);
		if(tamerPosition.x > mapBounds.x / 2 || tamerPosition.x < -mapBounds.x / 2){
			movementAxis.set(1,0);
			movement.sub(VectorHelper.projection(movement,movementAxis));
		}
		if(tamerPosition.y > mapBounds.y / 2 || tamerPosition.y < -mapBounds.y / 2){
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
