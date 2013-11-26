package com.me.tamer.core;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.Environment.RunningState;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.Helper;

public class TamerStage extends Stage{
	public static TamerStage instance;
	
	
	float ASPECT_RATIO =(float)Gdx.graphics.getWidth() / ((float)Gdx.graphics.getHeight());
	private final float BEGIN_ZOOM_SPEED = 0.01F;
	private final float TAMER_OFFSET_ONSCREEN = 40 / 10;
	
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
	public static final int GAME_END_LEVEL = 4;
	private int gameState;
	
	//Camera
	private Vector2 cameraPosition = new Vector2();
	private int cameraHolder;
	public static final int TAMER_CAMERA = 0;
	public static final int SPEAR_CAMERA = 1;
	public static final int AIM_CAMERA = 2;
	public static final int BEGIN_CAMERA = 3;
	
	
	private final float ZOOM_SPEED = 0.01f;
	
	//Debug
	ShapeRenderer debugRender = new ShapeRenderer();
	
	//Helper
	private Vector2 help = new Vector2();
	
	//Shaders
	ShaderProgram shader;
	Texture tex0,tex1,mask;
	SpriteBatch vbatch;
	OrthographicCamera vcam;
	float vtime;
	
	//Camera begin movement
	private Vector2 currentPosition = new Vector2();
	private final float BEGIN_CAMERA_SPEED = 2.0f;
	
	
	//End fade tween
	TweenManager tweenManager;
	boolean fading = false;
	
	
	public static TamerStage instance(){
		if(instance==null) instance = new TamerStage();
		return instance;
	}
	
	public void setup(TamerGame game){
		this.game = game;
		//Cameras must be set up first
		setupCamera();
		
		level = game.getLevelManager().getCurrentLevel() ;
		level.setStage(this);
		createActors();	
	}
	
	private TamerStage(){
		//private constructor because this is singleton
		
	}
	
	public void createActors(){
		
		//Hud
        hud = Hud.instance();
        hud.initialize(this);
       
   
		//environment
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Adding level to PlayScreen and generating environment " +level.getId());
		level.createEnvironment();
		environment = level.getEnvironment();
		
        //input controller
        controlContainer = ControlContainer.instance();
        controlContainer.initialize(this);
        
        //Register actors in drawing order
        this.addActor( environment );
        this.addActor( hud );
        this.addActor( controlContainer);
                
        setCameraHolder(BEGIN_CAMERA);
        
	}  

	@Override
	public void act (float delta) {
		switch (gameState){
		case GAME_READY:
			break;
		case(GAME_RUNNING):
			getRoot().act(delta);
			break;
		case(GAME_PAUSED):
			break;
		case(GAME_END_LEVEL):
			break;
		default:
			break;
		}
	}
	
	@Override
	public void draw(){
		hud.updateLabel(Hud.LABEL_FPS, Gdx.graphics.getFramesPerSecond());
		super.getCamera().update();
		if (!super.getRoot().isVisible()) return;
		super.getRoot().draw(super.getSpriteBatch(), 1);
//		environment.debugDraw(debugRender);
//		Helper.debugDraw(debugRender, camera);
		super.getSpriteBatch().end();
	}
	
	public void setupCamera(){	
		//camera = new OrthographicCamera();
		camera = new OrthographicCamera(Helper.VIRTUAL_SIZE_X, Helper.VIRTUAL_SIZE_Y);
		//camera.setToOrtho(false,VIRTUAL_WIDTH,VIRTUAL_HEIGHT);
		//camera.position.set(0f,0,0f);
		
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(false);
		
	}
	
	//Think this through
	public void updateCamera(float dt){
		controlCamera(dt);
		
		camera.position.set(cameraPosition.x,cameraPosition.y,0);
		camera.update();
	}
	
	public void controlCamera(float dt){
		switch (cameraHolder) {
		case TAMER_CAMERA:
			if(environment.getTamer()!=null){
				help.set(Helper.worldToScreen(environment.getTamer().getPosition()));
				help.y -= TAMER_OFFSET_ONSCREEN;
				cameraPosition.set( help );	
			}
			break;	
		case BEGIN_CAMERA:
			if (environment.getState() != RunningState.BEGIN_ZOOM){
				camera.zoom = 2.0f;
				//begin this after asset manager has run so that you can use tamerpos etc
				//cameraPosition.set(environment.getTamer().getPosition());
				environment.setState(RunningState.BEGIN_ZOOM);
			}
			
//			currentPosition.set(currentPosition.x + BEGIN_CAMERA_SPEED * dt, currentPosition.y + BEGIN_CAMERA_SPEED * dt);
			cameraPosition.set(currentPosition);
			
			camera.zoom -= BEGIN_ZOOM_SPEED;
			
//			System.out.println(camera.zoom);
			if(camera.zoom <= 1.0f) {
				setCameraHolder(TAMER_CAMERA);
				environment.setState(RunningState.TAMER_ENTER);
			}
			break;
		default:
			Gdx.app.error(TamerGame.LOG, this.getClass().getSimpleName() + " :: ran into cameraHolder default case");
			break;
		}	
	}
	
	public void dispose(){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Disposing");
		controlContainer.dispose();
		environment.dispose();
		environment = null;
		this.getActors().clear();
		
	}
	
	public void setCameraHolder(int holder){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: setting camera holder to " +holder);
		this.cameraHolder = holder;
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
	
	public int getCameraHolder(){
		return cameraHolder;
	}
	
	public void setGameState(int s){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: switching to state " +s);
		gameState = s;
	}
	
	public Environment getEnvironment(){
		return environment;
	}

	public int getGameState() {
		return gameState;
	}

	public Level getLevel() {
		return level;
	}
}
