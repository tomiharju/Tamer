package com.me.tamer.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
		
		joystick = new Joystick(this);
		this.addActor( joystick);
		
		screamButton = new ScreamButton(this);
		this.addActor(screamButton);
		
		spearButton = new SpearButton(this);
		this.addActor(spearButton);
		
	}
	
	@Override
	public void act(float dt){
		//add here other control elements as well... or maybe just create listeners on this
		if (environment.getTamer() != null) {
			joystick.setVisible(true);
			spearButton.setVisible(true);
		}
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
	
	public SpearButton getSpearButton(){
		return spearButton;
	}
	
	public TamerStage getStage(){
		return stage;
		
	}
	
	public void setStage(TamerStage stage){
		this.stage = stage;
	}
	
	public void setInputDisabled(boolean b){
		joystick.setInputDisabled(b);
		spearButton.setInputDisabled(b);
		screamButton.setInputDisabled(b);
	}
}
