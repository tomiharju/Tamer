package com.me.tamer.core;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.ui.InputController;
import com.me.tamer.ui.UIElement;
import com.me.tamer.utils.LevelCreator;

/**
 * @author Kesyttäjät
 * Controller object
 * Is (ir)responsible for creating levels and handling camera movement
 *  
 */


public class Environment {

	private OrthographicCamera cam 	= null;
	private SpriteBatch batch 		= null;
	
	//Ui camera
	private OrthographicCamera uiCam = null;
	
	//Define viewport size
	private final float VIEWPORT_WIDTH = 12;
	private final float VIEWPORT_HEIGHT = 20;

	//Refrence to active level
	Level level = null;
	InputController inputcontroller;
	ArrayList<UIElement> uiElements ;
	
	
	
	public Environment(){
		//Spritebatch is used for drawing sprites
		batch 			= new SpriteBatch();
		setupCamera();
		createLevel(1);
		inputcontroller = new InputController(this,level);
	}
	
	public void setupCamera(){
		System.err.println("Viewport size "+ Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
		float ASPECT_RATIO = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		cam	= new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT * 2);
		
		uiCam = new OrthographicCamera();
		uiCam.setToOrtho(false);
	
	}
	
	public OrthographicCamera getCamera(){
		
		return cam;
	}
	public OrthographicCamera getUiCamera(){
		return uiCam;
	}
	
	public void moveCamera(){
		Vector3 camPos = new Vector3();
		Vector2 tamerPos = IsoHelper.twoDToIso(level.getTamer().getPosition());
		Vector2 camBounds = IsoHelper.getTileCoordinates(level.getCamBounds(),1);
		Vector2 camOffset = IsoHelper.getTileCoordinates(level.getCamBoundsOffset(), 1);
		camBounds.x += camOffset.x;
		camBounds.y += camOffset.y;
		
		
		
		//X
		if (tamerPos.x  > camBounds.x - VIEWPORT_WIDTH / 2){
			camPos.x = camBounds.x - VIEWPORT_WIDTH / 2;
		}else if(tamerPos.x < -camBounds.x + VIEWPORT_WIDTH / 2){
			camPos.x = -camBounds.x + VIEWPORT_WIDTH / 2;
		}else{
			camPos.x = tamerPos.x;
		}
		
		
		//Y
		if(tamerPos.y  > camBounds.y - VIEWPORT_HEIGHT / 2){
			camPos.y = camBounds.y - VIEWPORT_HEIGHT / 2;
		}else if(tamerPos.y < -camBounds.y + VIEWPORT_HEIGHT / 2){
			camPos.y = -camBounds.y + VIEWPORT_HEIGHT / 2;
		}else{	
			camPos.y = tamerPos.y;	
		}
		 
		
		//Z
		camPos.z = 0;
		
		cam.position.set(camPos);
	}
	
	/**
	 * @param current_level 
	 * Create new level based on data read from level configuration file.
	 * Give this as parameter, so that EnvironmentFactory can properly add objects to gameobjects
	 */
	public void createLevel(int current_level){
		level = LevelCreator.create(current_level);
	}
	
	
	
	public void draw(){
		//update camera ( needed if its matrix is changed, its translated for example )
		cam.update();
		//Start uploading sprites
		batch.begin();
		
		
		batch.setProjectionMatrix(cam.combined); 
		level.draw(batch);

		//Set projection matrix to batch
		batch.setProjectionMatrix(uiCam.combined); 
		inputcontroller.draw(batch);
		
		batch.end();
		
	}
	public void update(float dt){
		inputcontroller.update(dt);
		level.update(dt);
		
	}
	
	public void cleanUp(){
		batch.dispose();
	}
	

	}
