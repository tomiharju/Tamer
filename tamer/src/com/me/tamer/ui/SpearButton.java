package com.me.tamer.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.renderers.UiRenderer;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

public class SpearButton extends Actor {

	ControlContainer controlContainer = null;
	OrthographicCamera cam = null;
	OrthographicCamera uiCam = null;

	private Vector2 help = new Vector2();
	
	private Vector2 input = new Vector2();
	private Vector2 localCenter = new Vector2();
	
	final Vector2 restingpoint = new Vector2(Gdx.graphics.getWidth() - 165,100);
	final float BUTTON_SIZE = 180;
	final float MIN_DISTANCE = 2;
	final float SPEED = 12; // for increasing the throwing distance
	final float MAX_DISTANCE = 15;

	private Vector2 cameraPoint = new Vector2(); //the point that AIM_CAMERA mode follows

	float throwDistance = 0; 
	boolean buttonPressed = false;
	boolean inputDisabled = false;
	UiRenderer renderer = new UiRenderer();
	UiRenderer renderer2 = new UiRenderer();
	UiRenderer renderer3 = new UiRenderer();
	UiRenderer currentRenderer;
	
	//Status variables
	private boolean onCooldown = false;


	private boolean spearOnRange = false;
	
	private Joystick joystick;
	
	public SpearButton(ControlContainer controlContainer) {
		this.controlContainer = controlContainer;
		
		//on cooldown
		renderer.loadGraphics(TamerTexture.BUTTON_SPEAR);
		renderer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		
		//ready to throw
		renderer2.loadGraphics(TamerTexture.BUTTON_SPEAR_GLOW);
		renderer2.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer2.setPosition(restingpoint);
		
		//pick up spear
		renderer3.loadGraphics("joystick");
		renderer3.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer3.setPosition(restingpoint);
		
		currentRenderer = renderer;
		
		localCenter.set(BUTTON_SIZE / 2, BUTTON_SIZE / 2);
	
		cam = controlContainer.getCam();
		uiCam = controlContainer.getUiCam();
		
		joystick = controlContainer.getJoystick();
		
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		if (spearOnRange)currentRenderer = renderer3;
		else if (onCooldown)currentRenderer = renderer;
		else currentRenderer = renderer2;
			
		currentRenderer.draw(batch);
		
	}

	public void createListener(){
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2 ){
					if(spearOnRange)controlContainer.getEnvironment().getTamer().pickUpSpear();
					else controlContainer.getEnvironment().getTamer().throwSpear();
					return true;
				}
                return false;
	        }
			 
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				//stopAim();		
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer){
				input.set(x,y);
				if(input.dst(localCenter) > BUTTON_SIZE / 2 ){
					buttonPressed = false;
				}
			}
		});	
	}

	
	public float getThrowDistance(){
		return throwDistance;
	}
	
	public Vector2 getCameraPoint(){
		return cameraPoint;
	}
	
	public void setOnCooldown(boolean b){
		onCooldown = b;
	}
	
	public boolean isOnCooldown(){
		return onCooldown;
	}
	
	public boolean isSpearOnRange() {
		return spearOnRange;
	}

	public void setSpearOnRange(boolean spearOnRange) {
		this.spearOnRange = spearOnRange;
	}
}
