package com.me.tamer.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.core.Level;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.gameobjects.tamer.GryphonScream;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.utils.RuntimeObjectFactory;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LassoButton extends Actor{
	
	private ControlContainer inputcontroller 	= null;
	private UiRenderer renderer 				= null;
	private Tamer tamer 						= null;
	private Environment environment 						= null;
	
	//Button variables
	Vector2 restingpoint 	= null;
	Vector2 delta			= null;
	float size				= 0;
	boolean isPressed		= false;
	//Temporary colors for button
	Color pressedCol		= null;
	Color notPressedCol		= null;


	public LassoButton(ControlContainer inputController) {
		this.inputcontroller = inputController;
		restingpoint	= new Vector2(Gdx.graphics.getWidth() - 110,200);
		delta			= new Vector2(0,0);
		size			= 110;
		environment		= inputcontroller.getEnvironment();
		tamer 			= environment.getTamer();
		renderer 		= new UiRenderer();
		renderer.loadGraphics("icon_scream_v6");
		renderer.setSize(size,size);
		renderer.setPosition(restingpoint);
		
		setPosition(restingpoint.x - size/2, restingpoint.y - size/2);
		setSize(size, size);
		
		addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	                Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
							+ " :: Gryphon touch started at (" + x + ", " + y + ")");
	                
	                if (tamer == null) tamer = environment.getTamer();
	                if (tamer != null){
	                	GryphonScream scream = (GryphonScream) RuntimeObjectFactory.getObjectFromPool("scream");
		        		if(scream != null)
		        			tamer.useScream(scream);
		        		else
		        			System.out.println("Scream is cooling down");
	                }
	                return true;
	        }
		});	
	}
	
	public void draw(SpriteBatch batch, float parentAlpha) {
		renderer.setSize(size, size);
		renderer.setPosition(restingpoint);
		renderer.draw(batch);	
	}
}