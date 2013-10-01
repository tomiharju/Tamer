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

	ArrayList<UIElement> buttons ;
	private Environment environment;
	OrthographicCamera uiCam = null;
	OrthographicCamera cam = null;
	private Level level;
	private UIElement selectedButton = null;
	public InputController(Environment env, Level lvl){
		this.environment = env;
		uiCam = environment.getUiCamera();
		cam = environment.getCamera();
		this.level = lvl;
		buttons = new ArrayList<UIElement>();
		buttons.add(new ActionButton(this));
		buttons.add(new Joystick(this));
		buttons.add(new SpearButton(this));
		
		Gdx.input.setInputProcessor(this);
	}
	
	public void draw(SpriteBatch batch){
		for(UIElement u : buttons)
			u.draw(batch);
	}
	public void update(float dt){
		for(UIElement u : buttons)
			u.update(dt);
	}
	
	public ArrayList<UIElement> getButtons() {
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
		for(UIElement e : buttons)
			if(e.isTouched(input)){
					e.handleInput(input);
					selectedButton = e;
			}
				
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(selectedButton != null)
			selectedButton.touchUp();
		selectedButton = null;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 input = new Vector2(screenX,uiCam.viewportHeight - screenY );
		
		if(selectedButton != null)
			selectedButton.handleInput(input);
		
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
