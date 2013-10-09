package com.me.tamer.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.core.Environment;
import com.me.tamer.gameobjects.Level;

public class InputController implements InputProcessor{

	ArrayList<UiElement> buttons ;
	private Environment environment;
	OrthographicCamera uiCam = null;
	OrthographicCamera cam = null;
	private Vector2 input = new Vector2();
	private Level level;
	private HashMap<Integer,UiElement> selectedButtons = null;
	Vector2 testVector = new Vector2();
	public InputController(Environment env, Level lvl){
		this.environment = env;
		uiCam = environment.getUiCamera();
		cam = environment.getCamera();
		this.level = lvl;
		buttons = new ArrayList<UiElement>();
		buttons.add(new ActionButton(this));
		buttons.add(new Joystick(this));
		buttons.add(new SpearButton(this));
		selectedButtons = new HashMap<Integer,UiElement>();
		Gdx.input.setInputProcessor(this);
	}
	
	public void draw(SpriteBatch batch){
		for(UiElement u : buttons)
			u.draw(batch);
	}
	public void update(float dt){
		for(UiElement u : buttons)
			u.update(dt);
	}
	
	public ArrayList<UiElement> getButtons() {
		return buttons;
	}


	public Environment getEnvironment() {
		return environment;
	}


	public Level getLevel() {
		return level;
	}
	public OrthographicCamera getCam(){
		return cam;
	}
	public OrthographicCamera getUiCam(){
		return uiCam;
	}



	@Override
	public boolean keyDown(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("Pointer added "+pointer);
		input.set(screenX,uiCam.viewportHeight - screenY);
		for(UiElement e : buttons)
			if(e.isTouched(input)){
					e.handleInput(input);
					selectedButtons.put(pointer, e);
			}
	
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		input.set(screenX,uiCam.viewportHeight - screenY);
		UiElement element = selectedButtons.get(pointer);
		if(element != null){
			element.touchUp(input);
			selectedButtons.remove(pointer);
		}
		
	
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		input.set(screenX,uiCam.viewportHeight - screenY );
		for(UiElement button : selectedButtons.values())
			if(button.isTouched(input))
				button.handleInput(input);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
