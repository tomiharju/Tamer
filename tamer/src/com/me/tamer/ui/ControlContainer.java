package com.me.tamer.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.SnapshotArray;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.Environment;

public class ControlContainer extends Group{
	private static ControlContainer instance = null;
	private TamerStage stage;
	private OrthographicCamera cam, uiCam;
	private Environment environment;
	Vector2 testVector = new Vector2();
	
	//Actors
	Joystick joystick;
	SpearButton spearButton;
	ScreamButton screamButton;
	TouchWrap touchWrap;
	
	private ControlContainer() {
		//constructor should not be accessible
	}
	
	public static ControlContainer instance(){
		if (instance == null) instance = new ControlContainer();
			return instance;
	}
	
	public void initialize(TamerStage stage){
		//has to be initialized when first created
		this.stage = stage;
		this.environment = stage.getEnvironment();
		cam = stage.getCamera();
		uiCam = stage.getUiCamera();
		create();
	}
	
	public void create(){
		//touch wrap is added first so it under other controls
		touchWrap = new TouchWrap();
		this.addActor(touchWrap);
		
		joystick = new Joystick(this);
		this.addActor( joystick);
		
		screamButton = new ScreamButton(this);
		this.addActor(screamButton);
		
		spearButton = new SpearButton(this);
		this.addActor(spearButton);
		SnapshotArray<Actor> actors = getChildren();
//		System.out.println("Controlcontainer has "+actors.size +" children");
		//Hide all buttons in startup
		disableInput();
	}
	
	@Override
	public void act(float dt){
		SnapshotArray<Actor> actors = getChildren();
		for (int i = 0; i < actors.size; i++){
			actors.get(i).act(dt);
		}
		
	}
	@Override
	public void draw (SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(uiCam.combined);
		SnapshotArray<Actor> actors = getChildren();
		for (int i = 0; i < actors.size; i++){
			actors.get(i).draw(batch, parentAlpha);
		}
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	public OrthographicCamera getCam(){
		return cam;
	}
	public OrthographicCamera getUiCam(){
		return uiCam;
	}
	
	public Joystick getJoystick(){
		return joystick;
	}
	
	public TouchWrap getTouchWrap(){
		return touchWrap;
	}
	
	public SpearButton getSpearButton(){
		return spearButton;
	}
	
	public TamerStage getStage(){
		return stage;
		
	}
	
	public void setStage(TamerStage stage){
		this.stage = stage;
	}
	
	public void enableInput(){
		setVisible(true);
	}
	public void disableInput(){
		setVisible(false);
	}
	public void dispose(){
		instance = null;
	}
}
