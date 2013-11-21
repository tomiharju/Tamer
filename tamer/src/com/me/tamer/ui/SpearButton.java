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
import com.me.tamer.utils.Helper;

public class SpearButton extends Actor {

	ControlContainer controlContainer = null;
	OrthographicCamera cam = null;
	OrthographicCamera uiCam = null;

	private Vector2 help = new Vector2();
	private Vector2 targetpointHeading = new Vector2();
	
	private Vector2 input = new Vector2();
	private Vector2 localCenter = new Vector2();
	
	final Vector2 restingpoint = new Vector2(Gdx.graphics.getWidth() - 165,100);
	final float BUTTON_SIZE = 110;
	final float MIN_DISTANCE = 2;
	final float SPEED = 12; // for increasing the throwing distance
	final float MAX_DISTANCE = 15;

	private ArrayList<Vector2> waypoints = new ArrayList<Vector2>();
	private Vector2 waypoint1 = new Vector2();
	private Vector2 waypoint2 = new Vector2();
	private Vector2 waypoint3 = new Vector2();
	private Vector2 cameraPoint = new Vector2(); //the point that AIM_CAMERA mode follows

	float throwDistance = 0; 
	boolean buttonPressed = false;
	boolean inputDisabled = false;
	UiRenderer buttonRender = null;
	UiRenderer pointRender = null;
	UiRenderer pointRender2 = null;
	
	private Joystick joystick;
	
	public SpearButton(ControlContainer controlContainer) {
		this.controlContainer = controlContainer;
		
		buttonRender = new UiRenderer();
		pointRender = new UiRenderer();
		pointRender2 = new UiRenderer();
		
		buttonRender.loadGraphics("button_spear");
		buttonRender.setSize(BUTTON_SIZE,BUTTON_SIZE);
		buttonRender.setPosition(restingpoint);	
		
		pointRender.loadGraphics("joystick");
		pointRender.setSize(1.0f,1.0f);
		pointRender.setPosition(new Vector2(0,0));
		
		pointRender2.loadGraphics("joystick");
		pointRender2.setSize(1f,1f);
		pointRender2.setPosition(new Vector2(0,0));
		
		localCenter.set(BUTTON_SIZE / 2, BUTTON_SIZE / 2);
	
		cam = controlContainer.getCam();
		uiCam = controlContainer.getUiCam();
		
		joystick = controlContainer.getJoystick();
		
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		buttonRender.draw(batch);
		
		if (buttonPressed){
			batch.setProjectionMatrix(cam.combined);
			
			//pointRender.setPosition(IsoHelper.twoDToTileIso(waypoint1));
			//pointRender.setPosition(Helper.worldToScreen(controlContainer.getEnvironment().getTamer().getShadow().getPosition()));
			//pointRender.setSize(throwDistance, throwDistance / 2);
			//pointRender.draw(batch);

			//pointRender2.setPosition(IsoHelper.twoDToTileIso(waypoint2));
			//pointRender2.draw(batch);
			
			batch.setProjectionMatrix(uiCam.combined);
		}
	}

	public void act(float dt) {
		
		/*
		if(isVisible() && buttonPressed){
			if(throwDistance <= MAX_DISTANCE ){
				throwDistance += SPEED*dt;
				controlContainer.getTouchWrap().setThrowDistance(throwDistance);
			}
			*/
			/*
			if(throwDistance <= MAX_DISTANCE ){
				help.set( controlContainer.getEnvironment().getTamer().getShadow().getPosition() );
				if (controlContainer.getEnvironment().checkInsideBounds(help.add(targetPoint),BORDER_OFFSET)){
					throwDistance += SPEED*dt;
					//throwDistance = joystick.getDelta().len() * 5.0f;
					//System.out.println("restinpoint: " +restingpoint +", input: " +input);
				}
				
				float distanceIndicator = Math.min(1,throwDistance / MAX_DISTANCE);
				//pointRender.setColor(distanceIndicator,0,0,distanceIndicator);
			}
			
			targetpointHeading.set(controlContainer.getEnvironment().getTamer().getHeading());
			targetpointHeading.nor();
			targetPoint.set(targetpointHeading.tmp().mul(throwDistance));
			
			//this is where spear ends up
			help.set( controlContainer.getEnvironment().getTamer().getShadow().getPosition() );
			waypoint1.set(help.add(targetPoint));
			
			help.set( controlContainer.getEnvironment().getTamer().getPosition() );
			waypoint2.set(help.add(targetPoint));
			//Set camera to follow way point 2
			cameraPoint.set(waypoint2);
			
			help.set( controlContainer.getEnvironment().getTamer().getPosition().tmp().add(-3,3) );
			waypoint3.set(help.add(targetPoint.tmp().mul(0.8f)));
		}	*/
		
	}
	
	public void createListener(){
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				input.set(x,y);
				
				if(input.dst(localCenter) < BUTTON_SIZE / 2 ){
					
					//set to AIM Camera
					/*
					Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
							+ " :: switched to AIM_CAMERA");
					controlContainer.getStage().setCameraHolder(TamerStage.AIM_CAMERA);
					
					controlContainer.getJoystick().disableMovement();
					buttonPressed = true;		
					controlContainer.getTouchWrap().setAimMode(true);
					*/
					
					//controlContainer.getEnvironment().getTamer().tryThrowSpear(waypoints );
					controlContainer.getEnvironment().getTamer().throwSpear();
					
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
	
	public void stopAim(){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: switched to TAMER_CAMERA");
		controlContainer.getStage().setCameraHolder(TamerStage.TAMER_CAMERA);
		
		//throwdistance needs to be zero (effects won't trigger)
		throwDistance = 0;
		
		buttonPressed = false;
		controlContainer.getTouchWrap().setAimMode(false);
		controlContainer.getJoystick().enableMovement();
	}
	
	public float getThrowDistance(){
		return throwDistance;
	}
	
	public Vector2 getCameraPoint(){
		return cameraPoint;
	}
	
	
}
