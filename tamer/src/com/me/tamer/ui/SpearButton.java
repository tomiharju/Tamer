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
	Vector2 tamerPos = null;
	Vector2 tamerHeading = null;
	
	Vector2 input = null;
	Vector2 localCenter = null;
	
	final Vector2 restingpoint = new Vector2(Gdx.graphics.getWidth() - 165,100);
	final float BUTTON_SIZE = 110;
	final float MIN_POWER = 2;
	final float MAX_POWER = 7;
	final float SPEED = 5;
	
	float power = 1; 
	boolean pressed = false;
	UiRenderer buttonRender = null;
	UiRenderer pointRender = null;
	
	
	
	public SpearButton(ControlContainer controlContainer) {
		this.controlContainer = controlContainer;
	
		targetPoint		= new Vector2(0,0);
		buttonRender = new UiRenderer();
		pointRender = new UiRenderer();
		buttonRender.loadGraphics("icon_scream_v6");
		buttonRender.setSize(BUTTON_SIZE,BUTTON_SIZE);
		buttonRender.setPosition(restingpoint);
		
		pointRender.loadGraphics("joystick");
		pointRender.setSize(0.5f,0.5f);
		pointRender.setPosition(new Vector2(0,0));
		
		tamerPos = new Vector2();
		tamerHeading = new Vector2();
		
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
			batch.setProjectionMatrix(uiCam.combined);
		}
	}

	public void act(float dt) {
		
		if(isVisible() && pressed){
			if(power <= MAX_POWER ){
				power += SPEED*dt;
				float powerIndicator = Math.min(1,power / MAX_POWER);
				pointRender.setColor(powerIndicator,0,0,powerIndicator);
			}

			tamerHeading.set(controlContainer.getEnvironment().getTamer().getHeading());
			tamerHeading.nor().mul(power);
			tamerPos.set(controlContainer.getEnvironment().getTamer().getPosition());
			targetPoint.set(tamerPos.add(tamerHeading));
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
				
				if( power > MIN_POWER && pressed ){
					Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
					if(spear != null)
						controlContainer.getEnvironment().getTamer().throwSpear(spear, targetPoint,power * 2);
					else
						System.err.println("No spears remaining");
				}
				
				power = 1;
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
}
