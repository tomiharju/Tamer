package com.me.tamer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.core.Environment;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.Tamer;
import com.me.tamer.gameobjects.renders.StaticRenderer;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.VectorHelper;

public class Joystick implements UiElement{

	private InputController inputcontroller = null;
	private UiRenderer renderer = null;
	private Tamer tamer = null;
	private Vector2 tamerPosition = null;
	private Level level = null;
	private Environment env = null;
	private Vector2 movementAxis = null;
	
	private Matrix3 translate = new Matrix3().rotate(45);
	//Joystick variables
	Vector2 restingpoint 	= new Vector2(150,100);
	Vector2 delta			= null;
	Vector2 pointer			= null;
	float size				= 250;
	float pointersize		= 30f;
	boolean isPressed		= false;
	
	
	public Joystick(InputController inputController) {
		this.inputcontroller = inputController;
		delta			= new Vector2(0,0);
		movementAxis	= new Vector2(0,0);
		pointer			= new Vector2(restingpoint.x,restingpoint.y);
		tamerPosition	= new Vector2();
		renderer 		= new UiRenderer();
		tamer 			= inputcontroller.getLevel().getTamer();
		level			= inputcontroller.getLevel();
		env 			= inputcontroller.getEnvironment();
		renderer.loadGraphics("joystick");
		renderer.setSize(size,size);
		renderer.setPosition(restingpoint);
	}

	@Override
	public void draw(SpriteBatch batch) {
		renderer.setSize(size, size);
		renderer.setPosition(restingpoint);
		renderer.draw(batch);
		renderer.setSize(pointersize,pointersize);
		renderer.setPosition(pointer);
		renderer.draw(batch);
		
	}

	@Override
	public void update(float dt) {
		if(!isPressed){
			pointer.set(restingpoint);
		}
		else{
			delta.set(pointer.tmp().sub(restingpoint));
			if(delta.len() > size / 2){
				delta.nor().mul(size/2);
				pointer.set(restingpoint.tmp().add(delta));
			}
			if(delta.len() < size / 4)
				tamer.turn(delta);
			else{
				delta.mul(translate);
				checkBounds(delta.mul(dt));
			}
			
		}
		
		env.moveCamera(tamer.getPosition());
	
	}
	
	public void checkBounds(Vector2 movement){
		Vector2 mapBounds = level.getMapBounds();
		tamerPosition.set(tamer.getPosition());
	
		movement.set(IsoHelper.twoDToIso(movement));
		tamerPosition.set(IsoHelper.twoDToIso(tamerPosition));
		tamerPosition.add(movement);
		if(tamerPosition.x > mapBounds.x || tamerPosition.x < -mapBounds.x){
			Vector2 remove = VectorHelper.projection((mapBounds.tmp().set(1,0)),movement);
			System.out.println(remove.toString());
			movementAxis.set(1,0);
			movement.sub(VectorHelper.projection(movement,movementAxis));
			
			
		}
		if(tamerPosition.y > mapBounds.y || tamerPosition.y < -mapBounds.y){
			Vector2 remove = VectorHelper.projection((mapBounds.tmp().set(1,0)),movement);
			System.out.println(remove.toString());
			movementAxis.set(0,1);
			movement.sub(VectorHelper.projection(movement,movementAxis));
			
		

			
		}
		
			tamer.manouver(movement);
	
		
		
	}

	@Override
	public void relocate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleInput(Vector2 input) {
		isPressed = true;
		pointer.set(input.x,input.y);
		
		return true;
		
	}

	@Override
	public void touchUp(Vector2 input) {
		isPressed = false;
		
	}

	@Override
	public void touchDown() {
		isPressed = true;
		
	}

	@Override
	public boolean isTouched(Vector2 input) {
		if(input.dst(restingpoint) < size / 2 + 4)
			return true;
		return false;
	}

}
