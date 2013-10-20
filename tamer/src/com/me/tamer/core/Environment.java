package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.ui.InputController;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.LevelCreator;

/**
 * @author Kesyttajat
 * Controller object
 * Is (ir)responsible for creating levels and handling camera movement
 *  
 */


public class Environment extends Actor{

	//Check which ones should be moved on a higher level
	
	//Cameras
	private OrthographicCamera uiCam = null;
	private OrthographicCamera cam 	= null;
	
	//Define viewport size
	private final float VIRTUAL_WIDTH = 12;
	private final float VIRTUAL_HEIGHT = 20;
	float ASPECT_RATIO = (float)Gdx.graphics.getWidth() / ((float)Gdx.graphics.getHeight());
	//Refrence to active level
	Level level = null;
	InputController inputController;

	//debug
	private ShapeRenderer shapeRenderer;

	public Environment(){
		setupCamera();
		createLevel(1);
		inputController = new InputController(this,level);
		level.linkToUi(inputController);
		shapeRenderer = new ShapeRenderer();
	}
	
	public void setupCamera(){	
		cam = new OrthographicCamera();
		cam.setToOrtho(false,VIRTUAL_WIDTH,VIRTUAL_HEIGHT);// * ASPECT_RATIO);
		
		cam.position.set(0f,0f,0f);
		uiCam = new OrthographicCamera();
		uiCam.setToOrtho(false);
	}
	
	public void resize(float width, float height){
		//This should be done on screen level?
		/*
		 // calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }
 
        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    	*/
    	}
	
	public OrthographicCamera getCamera(){
		return cam;
	}
	
	public OrthographicCamera getUiCamera(){
		return uiCam;
	}
	
	public void moveCamera(Vector2 tamerPos){
	
		//Vector2 camBounds = level.getCamBounds();
		//float y = Math.min(camBounds.y , Math.max(tamerPos.y,-camBounds.y ));
		//float x = Math.min(camBounds.x , Math.max(tamerPos.x,-camBounds.x ));
		
		Vector2 newPos = IsoHelper.twoDToIso(tamerPos);
		cam.position.set(newPos.x,newPos.y,0);
		
	}
	
	/**
	 * @param current_level 
	 * Create new level based on data read from level configuration file.
	 * Give this as parameter, so that EnvironmentFactory can properly add objects to gameobjects
	 */
	public void createLevel(int current_level){
		level = LevelCreator.create(current_level);	
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){
		//update camera ( needed if its matrix is changed, its translated for example )
		cam.update();
		//set Viewport

		batch.setProjectionMatrix(cam.combined); 
		level.draw(batch);
		
		//Set projection matrix to batch
		batch.setProjectionMatrix(uiCam.combined); 
		inputController.draw(batch);
			
		shapeRenderer.setProjectionMatrix(cam.combined);
		level.debugDraw(shapeRenderer);	
	}
	
	
	public void act(float dt){
		inputController.update(dt);
		level.update(dt);
	}
	
	//Cleaning up. How should we call this?
	public void dispose(SpriteBatch batch){
		batch.dispose();
	}
	
	public InputController getInputController(){
		return inputController;
	}
}
