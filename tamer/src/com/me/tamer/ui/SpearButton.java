package com.me.tamer.ui;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	Vector2 distancePoint = null; //this is used now..
	Vector2 restingPoint = null;
	Vector2 deltaPoint = null;
	Vector2 tamerPos = null;
	Vector2 tamerHeading = null;
	Vector2 power = null;
	float buttonSize = 80;
	float maxPower = 10;
	boolean isPressed = false;
	UiRenderer buttonRender = null;
	UiRenderer pointRender = null;
	public SpearButton(InputController inputController) {
		this.inputcontroller = inputController;
		restingPoint	=new Vector2(250,100);
		deltaPoint = new Vector2(restingPoint);
		buttonRender = new UiRenderer();
		pointRender = new UiRenderer();
		buttonRender.loadGraphics("joystick");
		buttonRender.setSize(buttonSize,buttonSize);
		buttonRender.setPosition(restingPoint);
		
		pointRender.loadGraphics("joystick");
		pointRender.setSize(0.5f,0.5f);
		pointRender.setPosition(new Vector2(0,0));
		tamer = inputController.getLevel().getTamer();
		distancePoint = new Vector2(0,0);
		tamerPos = new Vector2(0,0);
		tamerHeading = new Vector2(0,0);
		power = new Vector2(0,0);
		cam = inputController.getCam();
		uiCam = inputController.getUiCam();
	}

	@Override
	public void draw(SpriteBatch batch) {
		buttonRender.setPosition(deltaPoint);
		buttonRender.draw(batch);

		batch.setProjectionMatrix(cam.combined);
		pointRender.setPosition(IsoHelper.twoDToIso(distancePoint));
		pointRender.draw(batch);
		batch.setProjectionMatrix(uiCam.combined);
		
	}

	@Override
	public void update(float dt) {
		if(!isPressed){
			deltaPoint.set(restingPoint);
			distancePoint.set(tamer.getPosition());
			tamerHeading.set(tamer.getHeading());
			distancePoint.add(tamerHeading.mul(2));
		}else{
			power.set(deltaPoint.tmp().sub(restingPoint));
			float magnitude = (power.len() / 100) * maxPower;
			tamerPos.set(tamer.getPosition());
			tamerHeading.set(tamer.getHeading());
		
			distancePoint.set(tamerPos);
			distancePoint.add(tamerHeading.mul(magnitude));
		}
			
		
	}

	@Override
	public void relocate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleInput(Vector2 input) {
		isPressed = true;
		float y = Math.min(restingPoint.y, Math.max(input.y,restingPoint.y - 100));
		deltaPoint.set(deltaPoint.x,y);
		return true;
		
	}

	@Override
	public void touchUp() {
		power.set(deltaPoint.tmp().sub(restingPoint));
		if(power.len() > 10){
			Spear spear = (Spear) RuntimeObjectFactory.getObjectFromPool("spear");
			if(spear != null)
				tamer.throwSpear(spear,distancePoint);
			else
				System.err.println("No more spears remaining");
		}
			
		isPressed = false;
		
	}

	@Override
	public void touchDown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTouched(Vector2 input) {
		if(input.dst(deltaPoint) < buttonSize / 2 )
			return true;
		return false;
	}

}
