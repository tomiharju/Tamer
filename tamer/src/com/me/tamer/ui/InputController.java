package com.me.tamer.ui;

import java.util.ArrayList;

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
	private Level level;
	private ArrayList<UiElement> selectedButtons = null;
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
		selectedButtons = new ArrayList<UiElement>();
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
		Vector2 input = new Vector2(screenX,uiCam.viewportHeight - screenY);
		for(UiElement e : buttons)
			if(e.isTouched(input)){
					e.handleInput(input);
					selectedButtons.add(e);
			}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		System.out.println("Pointer " +pointer);
		if(selectedButtons.size() == 0)
			return true;
		if(pointer == 0){
			selectedButtons.get(0).touchUp();
			selectedButtons.remove(0);
		}else if(pointer == 1)
			if(selectedButtons.size() > 0){
				selectedButtons.get(1).touchUp();
				selectedButtons.remove(1);
			}
			else{
				selectedButtons.get(0).touchUp();
				selectedButtons.remove(0);
			}
		
		
	
		
		System.out.println(selectedButtons.size());
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 input = new Vector2(screenX,uiCam.viewportHeight - screenY );
		for(UiElement e : selectedButtons){
			if(e.isTouched(input))
				e.handleInput(input);
		}
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
