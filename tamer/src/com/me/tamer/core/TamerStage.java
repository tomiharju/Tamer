package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.ui.ControlContainer;

public class TamerStage extends Stage{
	
	private final float VIRTUAL_WIDTH = 12;
	private final float VIRTUAL_HEIGHT = 20;
	float ASPECT_RATIO = (float)Gdx.graphics.getWidth() / ((float)Gdx.graphics.getHeight());
	
	private OrthographicCamera camera, uiCamera;
	
	private TamerGame game;
	private Level level;
	
	//Actors
	private Hud hud;
	private Environment environment;
	private ControlContainer controlContainer;
	
	//GameStates
	public static final int GAME_READY = 0; 
	public static final int GAME_RUNNING = 1; 
	public static final int GAME_PAUSED = 2; 
	public static final int GAME_OVER = 3;
	public static int gameState;
	
	
	public TamerStage(TamerGame game){
		this.game = game;
		//Cameras must be set up first
		setupCamera();
		
		level = game.getLevelManager().getCurrentLevel() ;
		level.setStage(this);
		Gdx.input.setInputProcessor(this);
		createActors();
	}
	
	public void createActors(){
		
		//Hud
        hud = new Hud(this);
   
		//environment
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Adding level to PlayScreen and generating environment " +level.getId());
		level.createEnvironment();
		environment = level.getEnvironment();
		
        //input controller
        controlContainer = new ControlContainer( environment, this);
        
        //Register actors in drawing order
        this.addActor( environment );
        this.addActor( hud );
        this.addActor( controlContainer);
	}    
	
	public void setupCamera(){	
		camera = new OrthographicCamera();
		camera.setToOrtho(false,VIRTUAL_WIDTH,VIRTUAL_HEIGHT);// * ASPECT_RATIO);
		camera.position.set(0f,-4,0f);
		
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(false);
		
	}
	
	//Think this through
	public void updateCam(){
		camera.update();
	}
	
	public OrthographicCamera getCamera(){
		return camera;
	}
	
	public OrthographicCamera getUiCamera(){
		return uiCamera;
	}
	
	public TamerGame getGame(){
		return game;
	}
}
