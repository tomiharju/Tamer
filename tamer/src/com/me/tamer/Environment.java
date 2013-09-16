package com.me.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tamer.gameobjects.GameObject;
import com.me.tamer.gameobjects.Renderer.RenderType;
import com.me.tamer.utils.EnvironmentCreator;
import com.me.tamer.utils.GameObjectFactory;

/**
 * @author Kesyttäjät
 * Physical world which holds all the gameobjects.
 * Is used to pass input to objects.
 */


public class Environment implements InputProcessor{

	private OrthographicCamera cam 	= null;
	private SpriteBatch batch 		= null;
	
	//Define viewport size
	private final float VIEWPORT_WIDTH = 10;
	private final float VIEWPORT_HEIGHT = 10;
	
	//Gameobject data
	private ArrayList<GameObject> gameobjects = null;
	private ArrayList<GameObject> carbages	= null;
	
	
	
	public Environment(){
		//Inputprocessor handles all the user interaction
		Gdx.input.setInputProcessor(this);
		//Spritebatch is used for drawing sprites
		batch = new SpriteBatch();
		gameobjects = new ArrayList<GameObject>();
		carbages = new ArrayList<GameObject>();
		setupCamera();
		createLevel(1);
	}
	public void setupCamera(){
		cam	= new OrthographicCamera(VIEWPORT_WIDTH,VIEWPORT_HEIGHT);
		cam.setToOrtho(true);
		//cam.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);
	}
	
	/**
	 * @param current_level 
	 * Create new level based on data read from level configuration file.
	 */
	public void createLevel(int current_level){
		new EnvironmentCreator().create(current_level);
		//gameobjects.add(GameObjectFactory.createGameObject("com.me.tamer.gameobjects.tiles.GrassTile", RenderType.STATIC));
	}
	
	
	public void draw(){
		//update camera ( needed if its matrix is changed, its translated for example )
		cam.update();
		//Start uploading sprites
		batch.begin();
		//Set projection matrix to batch
		batch.setProjectionMatrix(cam.combined);
		for(GameObject o : gameobjects)
			o.draw(batch);
		batch.end();
		
	}
	public void update(float dt){
		runCarbageCollection();
		for(GameObject o : gameobjects)
			o.update(dt);
	}
	public void runCarbageCollection(){
		for(GameObject o : gameobjects)
			if(o.isCarbage())
				carbages.add(o);
		if(carbages.size() > 0){
			gameobjects.removeAll(carbages);
			carbages.clear();
		}
	}
	
	
	
	public void cleanUp(){
		batch.dispose();
	}
	

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
