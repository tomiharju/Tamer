package com.me.tamer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class SpearButton extends Actor {

	ControlContainer controlContainer = null;
	OrthographicCamera cam = null;
	OrthographicCamera uiCam = null;
	
	Vector2 targetPoint = null;
	Vector2 actualTargetPoint = null;
	Vector2 spearPosition = null;
	Vector2 spearHeading = null;
	
	Vector2 input = null;
	Vector2 localCenter = null;
	
	final Vector2 restingpoint = new Vector2(Gdx.graphics.getWidth() - 165,100);
	final float BUTTON_SIZE = 110;
	final float MIN_DISTANCE = 2;
	//final float MAX_DISTANCE = 7;
	final float SPEED = 5; // for increasing the throwing distance
	
	private float maxDistance;
	
	private final float GRAVITY = 5.0f;
	private final float INITIAL_SPEED = 2.0f;
	float tamerHeight;
	private Vector2 tamerPos = new Vector2();
	
	float throwDistance = 1; 
	boolean pressed = false;
	UiRenderer buttonRender = null;
	UiRenderer pointRender = null;
	UiRenderer pointRender2 = null;
	
	
	
	public SpearButton(ControlContainer controlContainer) {
		this.controlContainer = controlContainer;
	
		targetPoint		= new Vector2(0,0);
		actualTargetPoint = new Vector2(0,0);
		
		buttonRender = new UiRenderer();
		pointRender = new UiRenderer();
		pointRender2 = new UiRenderer();
		
		buttonRender.loadGraphics("icon_scream_v6");
		buttonRender.setSize(BUTTON_SIZE,BUTTON_SIZE);
		buttonRender.setPosition(restingpoint);
		
		pointRender.loadGraphics("joystick");
		pointRender.setSize(0.5f,0.5f);
		pointRender.setPosition(new Vector2(0,0));
		
		pointRender2.loadGraphics("joystick");
		pointRender2.setSize(1f,1f);
		pointRender2.setPosition(new Vector2(0,0));
		
		spearPosition = new Vector2();
		spearHeading = new Vector2();
		
		input = new Vector2();
		localCenter = new Vector2();
		localCenter.set(BUTTON_SIZE / 2, BUTTON_SIZE / 2);
	
		cam = controlContainer.getCam();
		uiCam = controlContainer.getUiCam();
		
		setVisible(false);
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		buttonRender.draw(batch);
		
		if (pressed){
			batch.setProjectionMatrix(cam.combined);
			pointRender.setPosition(IsoHelper.twoDToIso(targetPoint));
			pointRender.draw(batch);
			pointRender2.setPosition(IsoHelper.twoDToIso(actualTargetPoint));
			pointRender2.draw(batch);
			batch.setProjectionMatrix(uiCam.combined);
		}
	}

	public void act(float dt) {
		
		if(isVisible() && pressed){
			System.out.println("maxdist: " +getMaxDistance());
			if(throwDistance <= getMaxDistance() ){
				throwDistance += SPEED*dt;
				float distanceIndicator = Math.min(1,throwDistance / getMaxDistance());
				pointRender.setColor(distanceIndicator,0,0,distanceIndicator);
			}

			spearHeading.set(controlContainer.getEnvironment().getTamer().getHeading());
			spearHeading.nor().mul(throwDistance);
			spearPosition.set( controlContainer.getEnvironment().getTamer().getShadow().getPosition() );
			targetPoint.set(spearPosition.add(spearHeading));
			resolveActualTargetPoint();
		}	
	}
	
	public void createListener(){
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				input.set(x,y);

				if(input.dst(localCenter) < BUTTON_SIZE / 2 ){
					controlContainer.getJoystick().disableMovement();
					pressed = true;
					return true;
				}
                return false;
	        }
			 
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				input.set(x,y);
				
				if( throwDistance > MIN_DISTANCE && pressed ){
					Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
					if(spear != null)
						controlContainer.getEnvironment().getTamer().throwSpear(spear, targetPoint, throwDistance );
					else
						System.err.println("No spears remaining");
				}
				
				throwDistance = 1;
				pressed = false;
				controlContainer.getJoystick().enableMovement();	
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer){
				input.set(x,y);
				if(input.dst(localCenter) > BUTTON_SIZE / 2 ){
					pressed = false;
				}
			}
		});	
	}
	
	public void resolveActualTargetPoint(){
		//Resolve the actual point where tamer throws the spear
		tamerPos.set( controlContainer.getEnvironment().getTamer().getPosition() );
		float speedX = targetPoint.tmp().nor().x * INITIAL_SPEED;
		float speedY = targetPoint.tmp().nor().y * INITIAL_SPEED;
		float flyTime = (float)( Math.sqrt( tamerHeight / GRAVITY + Math.pow( speedY , 2 ) / 4 * GRAVITY ) - speedY / 2 * GRAVITY );
		
		
		spearPosition.set( controlContainer.getEnvironment().getTamer().getShadow().getPosition() );
		//System.out.println("spearPosition: "+spearPosition);
		
		System.out.println("targetPoint: " +targetPoint +", actual: " +actualTargetPoint);
		actualTargetPoint.set( tamerPos.x + speedX * flyTime, tamerPos.y + speedY * flyTime );
	}
	
	public float getMaxDistance(){
		float shadowDistance = controlContainer.getEnvironment().getTamer().getShadow().getDistance();
		tamerHeight = (float)Math.sqrt(Math.pow(shadowDistance, 2) + Math.pow(shadowDistance, 2));
		float droppingTime = (float)Math.sqrt(tamerHeight * GRAVITY);
		maxDistance = INITIAL_SPEED * droppingTime;
		
		return maxDistance;
	}
}
