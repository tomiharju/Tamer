package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

public class StaticObject implements GameObject{
	protected Vector2 position;
	
	protected Renderer renderer;
	private boolean isCarbage = false;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		renderer.draw(batch);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRender(String rendertype) {
		Renderer renderer = RendererFactory.createRenderer(rendertype);
		this.renderer = renderer;
	}
	

	@Override
	public void markAsCarbage() {
		isCarbage = true;
		
	}

	@Override
	public boolean isCarbage() {
		return isCarbage;
	}

	@Override
	public void setGraphicSize(String size) {
		String[] values = size.split(":");
		int w = Integer.parseInt(values[0]);
		int h = Integer.parseInt(values[1]);
		renderer.setSize(w, h);
		
	}
	

	@Override
	public void setGraphics(String graphics) {
		renderer.loadGraphics(graphics);
		
	}

	@Override
	public void setPosition(String pos) {
		// TODO ask grid object for real raw screen coordinate
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		Vector2 position = IsoHelper.twoDToIso(new Vector2(x,y));
		this.position = position;
		renderer.setPosition(position);
		
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}
	

}
