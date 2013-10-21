package com.me.tamer.ui;

import java.util.ArrayList;
import java.util.HashMap;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Level;
import com.me.tamer.core.PlayScreen;
import com.me.tamer.gameobjects.Environment;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class InputController extends Actor implements InputProcessor{

	private PlayScreen playScreen;
	
	private OrthographicCamera cam, uiCam;
	
	ArrayList<UiElement> buttons ;
	private Level level;
	private Vector2 input = new Vector2();
	private Environment environment;
	private HashMap<Integer,UiElement> selectedButtons = null;
	Vector2 testVector = new Vector2();
	
	public InputController(Level lvl, Environment envi, PlayScreen playScreen){
		this.playScreen = playScreen;
		this.level = lvl;
		this.environment = envi;
		buttons = new ArrayList<UiElement>();
		Gdx.input.setInputProcessor(null);
		
		cam = playScreen.getCamera();
		uiCam = playScreen.getUiCamera();
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){
		batch.setProjectionMatrix(uiCam.combined); 
		
		int size = buttons.size();
		for(int i = 0 ; i < size ; i ++)
			buttons.get(i).draw(batch);
	}
	public void act(float dt){
		int size = buttons.size();
		for(int i = 0 ; i < size ; i ++)
			buttons.get(i).update(dt);
	}
	
	public void enableInput(){
		buttons.clear();
	
		buttons.add(new ScreamButton(this));
		buttons.add(new Joystick(this));
		buttons.add(new SpearButton(this));
		buttons.add(new LassoButton(this));
		
		selectedButtons = new HashMap<Integer,UiElement>();
		Gdx.input.setInputProcessor(this);
	}
	public void disableInput(){
		Gdx.input.setInputProcessor(null);
	}
	public ArrayList<UiElement> getButtons() {
		return buttons;
	}

	public Level getLevel() {
		return level;
	}

	public Environment getEnvironment() {
		return environment;
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
