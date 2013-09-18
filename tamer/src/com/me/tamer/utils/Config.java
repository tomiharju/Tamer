package com.me.tamer.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * @author tharju
 * Configuration object, used for configurating gameobjects
 * This class holds all the common data for each gameboject in game
 * This object can hold values which are not needed for some particular object
 * Unnessessary data is just left as blank.
 * Data is read in EnvironmentCreator, from level[i].xml file.
 */
public class Config {

	Vector2 position;
	Vector2 velocity;
	float mass;
	float width;
	float height;
	
	public Config(){
		
	}
	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	

	
	
}
