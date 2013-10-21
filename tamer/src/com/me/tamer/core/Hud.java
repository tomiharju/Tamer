package com.me.tamer.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Hud extends Actor{
	private PlayScreen playScreen;
	
	public Hud(){
		
	}
	
	public void setPlayScreen(PlayScreen playScreen){
		this.playScreen = playScreen;
	}
	
	public void act(){
		
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){
		
		//batch.setProjectionMatrix(uiCam.combined); 
		
	}
}
