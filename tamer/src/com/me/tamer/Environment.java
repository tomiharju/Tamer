package com.me.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.Level;
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
	
	
	public Environment(){
		//Spritebatch is used for drawing sprites
		batch 			= new SpriteBatch();
		setupCamera();
		createLevel(1);
	}
	public void setupCamera(){
		System.err.println("Viewport size "+ Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
		float ASPECT_RATIO = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
		cam	= new OrthographicCamera();		
		cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT * ASPECT_RATIO);
		cam.translate(-VIEWPORT_WIDTH/2,-VIEWPORT_HEIGHT/2,0);
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
		level.draw(batch);
		batch.end();
		
	}
	public void update(float dt){
		level.update(dt);
	}
	
	public void cleanUp(){
		batch.dispose();
	}
	

	}
