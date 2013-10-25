package com.me.tamer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.gameobjects.tamer.GryphonScream;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.utils.RuntimeObjectFactory;

public class ScreamButton extends Actor{
	
	private ControlContainer controlContainer 	= null;
	private UiRenderer renderer 				= null;
	private Tamer tamer 						= null;
	private Environment environment 						= null;
	
	//Button variables
	Vector2 restingpoint 	= null;
	Vector2 delta			= null;
	private Vector2 input			= null;
	private Vector2 localCenter 	= null;
	private final float BUTTON_SIZE				= 110;
	boolean isPressed		= false;
	

	public ScreamButton(ControlContainer inputController) {
		this.controlContainer = inputController;
		restingpoint	= new Vector2(Gdx.graphics.getWidth() - 110,200);
		delta			= new Vector2(0,0);
		input			= new Vector2(0,0);
		localCenter 	= new Vector2(BUTTON_SIZE / 2, BUTTON_SIZE / 2);
		environment		= controlContainer.getEnvironment();
		tamer 			= environment.getTamer();
		renderer 		= new UiRenderer();
		renderer.loadGraphics("button_scream");
		renderer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		addListener(new InputListener(){
			 public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2 ){ 
					Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
							+ " :: Gryphon touch started at (" + x + ", " + y + ")");
	                
	                if (tamer == null) tamer = environment.getTamer();
	                if (tamer != null){
	                	GryphonScream scream = (GryphonScream) RuntimeObjectFactory.getObjectFromPool("scream");
		        		if(scream != null)
		        			tamer.useScream(scream);
		        		else
		        			Gdx.app.log(TamerGame.LOG, this.getClass()
									.getSimpleName() + " :: Tried to use scream before it is returned to pool");
	                }
	                return true;
				}
				else return false;
	        }
		});	
	}
	
	public void draw(SpriteBatch batch, float parentAlpha) {
		renderer.setSize(BUTTON_SIZE, BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		renderer.draw(batch);

	}
}
