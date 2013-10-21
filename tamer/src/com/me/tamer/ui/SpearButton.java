package com.me.tamer.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class SpearButton implements UiElement {

	InputController inputcontroller = null;
	OrthographicCamera cam = null;
	OrthographicCamera uiCam = null;
	Tamer tamer = null;
	
	Vector2 targetPoint = null;
	Vector2 tamerPos = null;
	Vector2 tamerHeading = null;
	
	final Vector2 restingPoint = new Vector2(300,100);
	final float BUTTON_SIZE = 110;
	final float MIN_POWER = 2;
	final float MAX_POWER = 10;
	final float SPEED = 5;
	
	float power = 1; 
	boolean isPressed = false;
	UiRenderer buttonRender = null;
	UiRenderer pointRender = null;
	
	
	
	public SpearButton(InputController inputController) {
		this.inputcontroller = inputController;
	
		targetPoint		= new Vector2(0,0);
		buttonRender = new UiRenderer();
		pointRender = new UiRenderer();
		buttonRender.loadGraphics("icon_scream_v6");
		buttonRender.setSize(BUTTON_SIZE,BUTTON_SIZE);
		buttonRender.setPosition(restingPoint);
		
		pointRender.loadGraphics("joystick");
		pointRender.setSize(0.5f,0.5f);
		pointRender.setPosition(new Vector2(0,0));
		tamer = inputController.getLevel().getTamer();
		
		tamerPos = new Vector2();
		tamerHeading = new Vector2();
	
		cam = inputController.getCam();
		uiCam = inputController.getUiCam();
	}

	@Override
	public void draw(SpriteBatch batch) {
		buttonRender.draw(batch);

		batch.setProjectionMatrix(cam.combined);
		pointRender.setPosition(IsoHelper.twoDToIso(targetPoint));
		pointRender.draw(batch);
		batch.setProjectionMatrix(uiCam.combined);
		
	}

	@Override
	public void update(float dt) {
		if(isPressed)
			if(power <= MAX_POWER ){
				power += SPEED*dt;
				float powerIndicator = Math.min(1,power / MAX_POWER);
				pointRender.setColor(powerIndicator,0,0,powerIndicator);
			}
		
		tamerHeading.set(tamer.getHeading());
		tamerHeading.nor().mul(power);
		tamerPos.set(tamer.getPosition());
		targetPoint.set(tamerPos.add(tamerHeading));
	}

	@Override
	public void relocate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleInput(Vector2 input) {
		return true;
		
	}

	@Override
	public void touchUp(Vector2 input) {
		if( power > MIN_POWER && restingPoint.dst(input) < BUTTON_SIZE / 2 ){
			Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
			if(spear != null)
				tamer.throwSpear(spear, targetPoint,power * 2);
			else
				System.err.println("No spears remaining");
		}
			power = 1;
			isPressed = false;
			pointRender.resetColor();
			((Joystick) inputcontroller.getButtons().get(1)).enableMovement();
		
	}

	@Override
	public void touchDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTouched(Vector2 input) {
		if(input.dst(restingPoint) < BUTTON_SIZE / 2 ){
			isPressed = true;
			((Joystick) inputcontroller.getButtons().get(1)).disableMovement();
			return true;
		}
			
		return false;
	}

}
