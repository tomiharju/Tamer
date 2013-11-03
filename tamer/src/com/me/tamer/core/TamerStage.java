package com.me.tamer.core;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.IsoHelper;

public class TamerStage extends Stage{
	
	private final float VIRTUAL_WIDTH = 12 * (float)Math.sqrt(2);
	private final float VIRTUAL_HEIGHT = 40 * (float)(Math.sqrt(2) / 2);
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
	
	//Camera
	private Vector2 cameraPosition = new Vector2();
	private int cameraHolder = 0;
	public static final int TAMER_CAMERA = 0;
	public static final int SPEAR_CAMERA = 1;
	public static final int AIM_CAMERA = 2;
	
	//Debug
	ShapeRenderer debugRender = new ShapeRenderer();
	private static ArrayList<Vector2> debugLines = new ArrayList<Vector2>();
	private Vector2 start = new Vector2();
	private Vector2 end = new Vector2();
	
	
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
	}  
	
	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.scenes.scene2d.Stage#act(float)
	 */
	@Override
	public void act (float delta) {
		switch (TamerStage.gameState){
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

		super.getCamera().update();
		if (!super.getRoot().isVisible()) return;
		super.getSpriteBatch().setProjectionMatrix(super.getCamera().combined);
		super.getSpriteBatch().begin();
		
		super.getRoot().draw(super.getSpriteBatch(), 1);
		debugDraw();
		environment.debugDraw(debugRender);
		super.getSpriteBatch().end();
	}
	
	public void debugDraw(){
		
		debugRender.setProjectionMatrix(camera.combined);
		debugRender.setColor(1, 1, 1, 1);
		
		for (int i = 0; i<debugLines.size(); i+=2){
			start.set ( IsoHelper.twoDToTileIso(debugLines.get(i).tmp() ));
			end.set( IsoHelper.twoDToTileIso(debugLines.get(i+1).tmp() ));
			
			debugRender.begin(ShapeType.Line);
			debugRender.line(start.x , start.y, end.x, end.y );
			debugRender.end();
		}
	}
	
	public static void addDebugLine(Vector2 s, Vector2 e){
		
		debugLines.add( new Vector2().set(s));
		debugLines.add( new Vector2().set(e));
	}
	
	
	public void setupCamera(){	
		camera = new OrthographicCamera();
		camera.setToOrtho(false,VIRTUAL_WIDTH,VIRTUAL_HEIGHT);// * ASPECT_RATIO);
		camera.position.set(0f,-4,0f);
		
		uiCamera = new OrthographicCamera();
		uiCamera.setToOrtho(false);
		
	}
	
	//Think this through
	public void updateCamera(){
		controlCamera();
		camera.update();
		camera.position.set(cameraPosition.x,cameraPosition.y,0);
	}
	
	public void controlCamera(){
		switch (cameraHolder) {
		case TAMER_CAMERA:
			if(environment.getTamer()!=null)cameraPosition.set(IsoHelper.twoDToTileIso(environment.getTamer().getPosition()));	
			break;	
		case SPEAR_CAMERA:
			if (environment.getState() != Environment.SPEAR_TIME){
				Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: switched environment state to SPEAR_TIME");
				environment.setState(Environment.SPEAR_TIME);
				//disable joystick while in SPEAR_CAMERA MODE
				controlContainer.disableInput();
			}
			
			if (((Tamer)environment.getTamer()).getActiveSpear() != null){
				cameraPosition.set(IsoHelper.twoDToTileIso(((Tamer)environment.getTamer()).getActiveSpear().getPosition()));
			}else {
				//Back to default camera
				Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
						+ " :: switched to TAMER_CAMERA");
				cameraHolder = TAMER_CAMERA;
				//Back to default gameState
				environment.setState(Environment.NORMAL);
				//Re-enable joystick
				controlContainer.enableInput();
			}
			break;
		case AIM_CAMERA:
			cameraPosition.set(IsoHelper.twoDToTileIso(controlContainer.getSpearButton().getCameraPoint()));
			break;
		default:
			Gdx.app.error(TamerGame.LOG, this.getClass().getSimpleName() + " :: ran into cameraHolder default case");
			break;
		}
		
	}
	
	public void setCameraHolder(int holder){
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
		gameState = s;
	}
	
	public Environment getEnvironment(){
		return environment;
	}
}
