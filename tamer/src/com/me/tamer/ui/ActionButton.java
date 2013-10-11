package com.me.tamer.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.core.Environment;
import com.me.tamer.gameobjects.GryphonScream;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.Spear;
import com.me.tamer.gameobjects.Tamer;
import com.me.tamer.gameobjects.Worm;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.utils.RuntimeObjectFactory;

public class ActionButton implements UiElement{
	
	private InputController inputcontroller 	= null;
	private UiRenderer renderer 				= null;
	private Tamer tamer 						= null;
	private GryphonScream scream				= null;
	private ArrayList<Worm> worms 				= null;
	private Level level 						= null;
	private Environment env 					= null;
	
	//Button variables
	Vector2 restingpoint 	= null;
	Vector2 delta			= null;
	float size				= 0;
	float pointersize		= 30f;
	boolean isPressed		= false;
	//Temporary colors for button
	Color pressedCol		= null;
	Color notPressedCol		= null;


	public ActionButton(InputController inputController) {
		this.inputcontroller = inputController;
		restingpoint	= new Vector2(430,230);
		delta			= new Vector2(0,0);
		size			= 75;
		level			= inputcontroller.getLevel();
		tamer 			= level.getTamer();
		env 			= inputcontroller.getEnvironment();
		renderer 		= new UiRenderer();
		renderer.loadGraphics("joystick");
		renderer.setSize(size,size);
		renderer.setPosition(restingpoint);
		//pressedCol = new Color(1.0f, 0.0f, 0.0f, 0.0f);
		//notPressedCol = new Color(0.0f, 0.0f, 1.0f, 1.0f);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		//if(isPressed)renderer.setColor(pressedCol);
		//else renderer.setColor(notPressedCol);
		
		renderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);
		
		renderer.setSize(size, size);
		renderer.setPosition(restingpoint);
		renderer.draw(batch);
		
	}
	
	@Override
	public void update(float dt) {
		
	}

	@Override
	public boolean isTouched(Vector2 input) {
		if(input.dst(restingpoint) < size && !isPressed){
			isPressed = true;
			return true;
		}	
		isPressed = false;
		return false;
	}
	
	@Override
	public void relocate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleInput(Vector2 input) {	
		return true;
	}
	
	@Override
	public void touchUp(Vector2 input) {
		if (isPressed){
			GryphonScream scream = (GryphonScream) RuntimeObjectFactory.getObjectFromPool("scream");
			
			if(scream != null)
				tamer.useScream(scream);
			else
				System.out.println("Scream is cooling down");
		}

		
		// TODO Auto-generated method stub
	}

	@Override
	public void touchDown() {
		// TODO Auto-generated method stub
		
	}

}
