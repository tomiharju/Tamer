package com.me.tamer.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.core.Environment;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.Tamer;
import com.me.tamer.gameobjects.renders.StaticRenderer;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.utils.IsoHelper;

public class Joystick implements UIElement{

	private InputController inputcontroller = null;
	private UiRenderer renderer = null;
	private Tamer tamer = null ;
	private Level level = null;
	private Environment env = null;
	//Joystick variables
	Vector2 restingpoint 	= null;
	Vector2 delta			= null;
	Vector2 pointer			= null;
	float size				= 0;
	float pointersize		= 30f;
	boolean isPressed		= false;
	
	
	public Joystick(InputController inputController) {
		this.inputcontroller = inputController;
		restingpoint	= new Vector2(100,100);
		delta			= new Vector2(0,0);
		pointer			= new Vector2(restingpoint.x,restingpoint.y);
		size			= 100;
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
			Vector2 delta = restingpoint.cpy().sub(pointer);
			pointer.add(delta);
			delta.set(0,0);
			tamer.manouver(delta);
		}
		else{
			delta.set(pointer.cpy().sub(restingpoint));
			if(delta.len() > size / 2){
				delta.nor().mul(size/2);
				pointer.set(restingpoint.cpy().add(delta));
			}
			delta.div(10);
			env.moveCamera(delta.cpy().mul(dt));
			checkBounds(delta.cpy().mul(dt));
			
		}
	
	}
	
	public void checkBounds(Vector2 movement){
		Vector2 camBounds = level.getCamBounds();
		Vector2 position = new Vector2();
		position.set(tamer.getPosition());
		position.add(movement);
		
		
		
		//Check wether position + delta is still inside camera bounds
		if(position.x > camBounds.x || position.x < -camBounds.x){
			movement.set(0,movement.y);
		}
		if(position.y > camBounds.y || position.y < -camBounds.y){
			movement.set(movement.x,0);
		
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
	public void touchUp() {
		isPressed = false;
		
	}

	@Override
	public void touchDown() {
		isPressed = true;
		
	}

	@Override
	public boolean isTouched(Vector2 input) {
		if(input.dst(restingpoint) < size + 10)
			return true;
		return false;
	}

}
