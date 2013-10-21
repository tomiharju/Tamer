package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.me.tamer.services.MusicManager.TamerMusic;
import com.me.tamer.ui.InputController;
import com.me.tamer.utils.EnvironmentCreator;
import com.me.tamer.utils.IsoHelper;

public class PlayScreen extends AbstractScreen{
	//Define viewport size
	private final float VIRTUAL_WIDTH = 12;
	private final float VIRTUAL_HEIGHT = 20;
	float ASPECT_RATIO = (float)Gdx.graphics.getWidth() / ((float)Gdx.graphics.getHeight());
	
	//debug
	private ShapeRenderer shapeRenderer;
	
	//Cameras
	private OrthographicCamera uiCam = null;
	private OrthographicCamera cam 	= null;
	
	private InputController inputController;

	private Level level;
	
	private Hud hud;
	
	public PlayScreen(final TamerGame game){
		super(game);
	}
	
	@Override
	public void show() {
		//This method is called when the app is loaded
		//Stop music when the level starts
		game.getMusicManager().stop();
		
		//Cameras must be set up first
		setupCamera();
		
		level =  game.getLevelManager().getCurrentLevel() ;
		level.setPlayScreen(this);
		
		//--Actors--//
		//environment
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Adding level to PlayScreen and generating environment " +level.getId());
		level.createEnvironment();
        stage.addActor( level.getEnvironment() );
        
        //input controller
        inputController = new InputController(level, level.getEnvironment(), this);
        stage.addActor( inputController);
        //Links inputController to Ui
        level.getEnvironment().setInputController(inputController);
        
        //Hud
        hud = new Hud();
        hud.addListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		            System.out.println("down");
		            return true;
		    }
		    
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		            System.out.println("up");
		    }
        });
        stage.addActor(hud);
        
        // add a fade-in effect to the whole stage || DOES NOT WORK =(((
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction( Actions.fadeIn( 0.5f ) );
	}
	
	@Override
    public void render( float delta ){
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.update();
		stage.act( delta );
        stage.draw();
    }
	
	public void setupCamera(){	
		cam = new OrthographicCamera();
		cam.setToOrtho(false,VIRTUAL_WIDTH,VIRTUAL_HEIGHT);// * ASPECT_RATIO);
		
		cam.position.set(0f,0f,0f);
		uiCam = new OrthographicCamera();
		uiCam.setToOrtho(false);
	}
	
	public InputController getInputController(){
		return inputController;
	}
	
	public OrthographicCamera getCamera(){
		return cam;
	}
	
	public OrthographicCamera getUiCamera(){
		return uiCam;
	}
}
	
	