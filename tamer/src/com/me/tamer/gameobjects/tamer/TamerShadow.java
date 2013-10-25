package com.me.tamer.gameobjects.tamer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.utils.IsoHelper;

public class TamerShadow extends DynamicObject{
	private final float DISTANCE = 4.0f;
	private final float SIZE = 2.0f;
	private Tamer tamer;
	
	public TamerShadow(Tamer tamer){
		this.tamer = tamer;
		markAsActive();	
		size = new Vector2(SIZE, SIZE / 2);
		setGraphics("joystick");
		setPosition(new Vector2(0,0));
	}
	
	public void setGraphics(String graphics){
		Renderer renderer = RenderPool.addRendererToPool("animated",graphics);
		renderer.loadGraphics("joystick");
		renderType = graphics;
	}
	
	@Override
	public void update(float dt){
		position.set( tamer.getPosition().x + DISTANCE, tamer.getPosition().y - DISTANCE );
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.me.tamer.gameobjects.superclasses.DynamicObject#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(0.1f, 0.1f, 0.1f, 0.2f);
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		renderer.setPosition(IsoHelper.twoDToIso(position));
		renderer.setOrientation( solveOrientation() );
		renderer.setAngle(angle);
		renderer.draw(batch);
		
		//Reset to default
		batch.setColor(Color.WHITE); 
	}
	
	public float getDistance(){
		return DISTANCE;
	}
}
