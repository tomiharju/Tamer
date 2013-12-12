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
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.Helper;

public class TamerStage extends Stage{
	public static TamerStage instance;
	
	float ASPECT_RATIO =(float)Gdx.graphics.getWidth() / ((float)Gdx.graphics.getHeight());
	private final float BEGIN_ZOOM_SPEED = 0.003F;
	private final float BEGIN_CAMERA_SPEED = 8f;
	private final float TAMER_OFFSET_ONSCREEN = 40 / 10;
	private final float BEGIN_ZOOM_AMOUNT = 1.4f;
	private final float ZOOM_DEFAULT = 1.1f;
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
	private Vector2 cameraStartPosition = new Vector2();
	private Vector2 cameraTargetPosition = new Vector2();
	private Vector2 cameraHeading = new Vector2();
	private boolean cameraReturning = false;
	private boolean cameraDoneMoving = false;
	
	private int cameraHolder;
	public static final int TAMER_CAMERA = 0;
	public static final int SPEAR_CAMERA = 1;
	public static final int AIM_CAMERA = 2;
	public static final int BEGIN_CAMERA = 3;
	
	//Debug
	ShapeRenderer debugRender = new ShapeRenderer();
	
	//Helper
	private Vector2 help = new Vector2();
	
	//Camera begin movement
	private Vector2 currentPosition = new Vector2();
	
	//End fade tween
	TweenManager tweenManager;
	boolean fading = false;
	
	//Sound
	SoundManager sound = null;
	
	public static TamerStage instance(){
		if(instance==null) instance = new TamerStage();
		return instance;
	}
	
	public void setup(TamerGame game){
		this.game = game;
		//Cameras must be set up first
		setupCamera();
		createActors();	
	}
	
	private TamerStage(){
		//private constructor because this is singleton
		sound = SoundManager.instance();
	}
	
	public void createActors(){
		
		level = game.getLevelManager().getCurrentLevel();
		level.setStage(this);
		
		//environment
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Adding level to PlayScreen and generating environment " +level.getId());
		level.createEnvironment();
		environment = level.getEnvironment();
		
		//Hud
		//should be created after level because needs info from it
        hud = Hud.instance();
        hud.initialize(this);
		
        //input controller
        controlContainer = ControlContainer.instance();
        controlContainer.initialize(this);
        
        //Register actors in drawing order
        this.addActor( environment );
        this.addActor( hud );
        this.addActor( controlContainer);
        
        setGameState(GAME_RUNNING);
                
        reset();
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
	//	environment.debugDraw(debugRender);
	//	Helper.debugDraw(debugRender, camera);
		super.getSpriteBatch().end();
	}
	
	public void setupCamera(){	
		camera = new OrthographicCamera(Helper.VIRTUAL_SIZE_X, Helper.VIRTUAL_SIZE_Y);
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(false);
	}
	
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
			if (!cameraDoneMoving){
				cameraPosition.x += cameraHeading.x * dt * BEGIN_CAMERA_SPEED;
				cameraPosition.y += cameraHeading.y * dt * BEGIN_CAMERA_SPEED;
		
				if (!cameraReturning && cameraPosition.dst( cameraTargetPosition ) < 3.0f){
					cameraHeading.mul(-1);
					cameraReturning = true;
				} 
				
				if ( cameraReturning && cameraPosition.dst( cameraStartPosition ) < 3.0f){
					cameraDoneMoving = true;
					environment.setState(RunningState.TAMER_ENTER);
					hud.showHelp(false);
					hud.showNoEscape(true);
				} 
				
			}
			else
			{
				help.set(Helper.worldToScreen(environment.getTamer().getPosition()));
				help.y -= TAMER_OFFSET_ONSCREEN;
				cameraPosition.set( help );	
				
				camera.zoom -= BEGIN_ZOOM_SPEED;
				if(camera.zoom < ZOOM_DEFAULT) {
					camera.zoom = ZOOM_DEFAULT;
					setCameraHolder(TAMER_CAMERA);
//					environment.setState(RunningState.TAMER_ENTER);
//					sound.play(TamerSound.OPENING);
					environment.setState(RunningState.NORMAL);
					controlContainer.enableInput();
					hud.showNoEscape(false);
				}
			}
			break;
		default:
			Gdx.app.error(TamerGame.LOG, this.getClass().getSimpleName() + " :: ran into cameraHolder default case");
			break;
		}	
	}
	
	public void reset(){
		cameraReturning = false;
		cameraDoneMoving = false;
		fading = false;
		
		camera.zoom = BEGIN_ZOOM_AMOUNT;
		
//		cameraStartPosition.set( Helper.worldToScreen( environment.getTamer().getPosition() ));
		cameraStartPosition.set( Helper.worldToScreen( new Vector2(-20,0) ));
		cameraPosition.set(cameraStartPosition);
		
		//CHANGE THIS 
		cameraTargetPosition.set( Helper.worldToScreen( new Vector2(20,0)) );
		//------------------//
		
		cameraHeading.set( cameraTargetPosition.tmp().sub(cameraPosition));
		cameraHeading.nor();
		
		environment.setState(RunningState.BEGIN_ZOOM);
		
		hud.showHelp(true);
		hud.showNoEscape(false);
	}
	
	//Propably lacking stuff
	public void dispose(){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Disposing");
		controlContainer.dispose();
		environment.dispose();

		super.getSpriteBatch().dispose();
		
		environment = null;
		level = null;
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
	
	public void setGameState(int s){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: switching to Game State " +s);
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
	
	public SpriteBatch getSpriteBatch(){
		return super.getSpriteBatch();
	}
}
