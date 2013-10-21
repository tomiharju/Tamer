package com.me.tamer.core;

import com.me.tamer.gameobjects.Environment;
import com.me.tamer.utils.EnvironmentCreator;

public class Level{
	
	private PlayScreen playScreen;
	private final int id;
	private String name;
    private boolean completed;
    private Level nextLevel;

	Environment environment = null;

	public Level(int id){
		this.id = id;
	}
	
	public void setPlayScreen(PlayScreen playScreen){
		this.playScreen = playScreen;
	}
	
	public void resize(float width, float height){
		//This should be done on screen level?
		/*
		 // calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }
 
        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    	*/
    	}
	
	/**
	 * @param current_env 
	 * Create new env based on data read from level configuration file.
	 * Give this as parameter, so that EnvironmentFactory can properly add objects to gameobjects
	 */
	public void createEnvironment(){
		environment = EnvironmentCreator.create(id);
		environment.setPlayScreen(playScreen);
	}
	
	public Environment getEnvironment(){
		return environment;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Level getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(Level nextLevel) {
		this.nextLevel = nextLevel;
	}
	
	public int getId() {
		return id;
	}
}
