package com.me.tamer.core;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.ui.InputController;
import com.me.tamer.ui.UIElement;
import com.me.tamer.utils.LevelCreator;

/**
 * @author Kesyttäjät
 * Controller object
 * Is responsible for creating levels and handling camera movement
 *  
 */


public class Environment {

	private OrthographicCamera cam 	= null;
	private SpriteBatch batch 		= null;
	
	//Define viewport size
	private final float VIEWPORT_WIDTH = 12;
	private final float VIEWPORT_HEIGHT = 24;

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
		cam	= new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT * ASPECT_RATIO);		
	
	}
	public OrthographicCamera getCamera(){
		return cam;
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
		//Set projection matrix to batch
		batch.setProjectionMatrix(cam.combined); 
		inputcontroller.draw(batch);
		level.draw(batch);
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
