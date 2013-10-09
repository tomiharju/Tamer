package com.me.tamer.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Spear;
import com.me.tamer.gameobjects.Tamer;
import com.me.tamer.gameobjects.renders.UiRenderer;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class SpearButton implements UiElement {

	InputController inputcontroller = null;
	OrthographicCamera cam = null;
	OrthographicCamera uiCam = null;
	Tamer tamer = null;
	Vector2 restingPoint = null;
	Vector2 targetPoint = null;
	Vector2 tamerPos = null;
	Vector2 tamerHeading = null;
	
	final float BUTTON_SIZE = 100;
	final float MIN_POWER = 2;
	final float MAX_POWER = 10;
	final float SPEED = 5;
	
	float power = 1; 
	boolean isPressed = false;
	UiRenderer buttonRender = null;
	UiRenderer pointRender = null;
	
	
	
	public SpearButton(InputController inputController) {
		this.inputcontroller = inputController;
		restingPoint	= new Vector2(250,100);
		targetPoint		= new Vector2(0,0);
		buttonRender = new UiRenderer();
		pointRender = new UiRenderer();
		buttonRender.loadGraphics("joystick");
		buttonRender.setSize(BUTTON_SIZE,BUTTON_SIZE);
		buttonRender.setPosition(restingPoint);
		
		pointRender.loadGraphics("joystick");
		pointRender.setSize(0.5f,0.5f);
		pointRender.setPosition(new Vector2(0,0));
		tamer = inputController.getLevel().getTamer();
		
		tamerPos = new Vector2(tamer.getPosition());
		tamerHeading = new Vector2(tamer.getHeading());
	
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
			if(power <= MAX_POWER )
				power += SPEED*dt;
		
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
				tamer.throwSpear(spear, targetPoint);
			else
				System.err.println("No spears remaining");
		}
			power = 1;
			isPressed = false;
			
		
	}

	@Override
	public void touchDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTouched(Vector2 input) {
		if(input.dst(restingPoint) < BUTTON_SIZE / 2 ){
			isPressed = true;
			return true;
		}
			
		return false;
	}

}
