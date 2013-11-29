package com.me.tamer.gameobjects.tamer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.services.TextureManager.TamerStatic;
import com.me.tamer.utils.Helper;

public class TamerShadow extends DynamicObject{
	private final float SIZE = 2.0f;
	
	public TamerShadow(Tamer tamer){
		setPosition(new Vector2(0f,0f));
		setGraphics(TamerStatic.TAMER_SHADOW.getFileName());
		setZindex(-1);
		
	}
	
	@Override
	public void setGraphics(String graphics){
		Renderer renderer = RenderPool.addRendererToPool("static",graphics);
		renderer.loadGraphics(graphics);
		setSize(SIZE, SIZE / 2);
		setRenderType(graphics);
	}
	
	@Override
	public void update(float dt){
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.me.tamer.gameobjects.superclasses.DynamicObject#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(0.1f, 0.1f, 0.1f, 0.7f);
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize().x,getSize().y);
		renderer.setPosition(Helper.worldToScreen(getPosition()));
		renderer.draw(batch);
		
		//Reset to default
		batch.setColor(Color.WHITE); 
	}
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		Vector2 temp = new Vector2();
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getCenterPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x-0.25f, temp.y-0.25f, 0.5f, 0.5f);
		shapeRndr.end();
		/*
		 * shapeRndr.setColor(1, 1, 1, 1);
		 * temp.set(Helper.worldToScreen(getPosition()));
		 * shapeRndr.begin(ShapeType.Rectangle); shapeRndr.rect(temp.x
		 * -0.1f,temp.y-0.1f, 0.2f ,0.2f); shapeRndr.end();
		 */

	}

	public boolean getDebug() {
		return true;
	}

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}



	
	
}
