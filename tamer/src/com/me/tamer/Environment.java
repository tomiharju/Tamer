package com.me.tamer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Kesyttäjät
 *
 */


public class Environment implements InputProcessor{


	/**
	 * Physical world which holds all the gameobjects.
	 * Is used to pass input to objects.
	 */
	public Environment(){
		Gdx.input.setInputProcessor(this);
	}
	
	/**
	 * Create new level based on data read from level configuration file.
	 */
	public void CreateLevel(){
		
	}
	
	
}
