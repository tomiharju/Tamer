package com.me.tamer;

import com.badlogic.gdx.Screen;

public class PlayScreen implements Screen{

	private TamerMain main;
	private Environment environment;
	
	public PlayScreen(TamerMain core){
		this.main = main;
		CreateEnvironment();
	}

	public void CreateEnvironment(){
		this.environment = new Environment();
		
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
	
	