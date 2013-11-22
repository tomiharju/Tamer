package com.me.tamer.gameobjects.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Kesyttäjät
 * This class is the superclass for statically drawn objects
 * This class only has a static Sprite object with attached texture on it.
 *
 */
public class StaticRenderer implements Renderer {
	
	private Sprite sprite;
	public StaticRenderer(){
		
	}
	@Override
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	@Override
	public void loadGraphics(String graphicsName) {
		String gfx = graphicsName.split("\\.")[0];
		sprite = new Sprite(RenderPool.atlas.findRegion(gfx));
		
		sprite.setColor(Color.WHITE);
	}
	@Override
	public void setSize(float w, float h) {
		sprite.setSize(w, h);
		
	}
	@Override
	public void setSize(Vector2 size) {
		sprite.setSize(size.x,size.y);
		
	}
	@Override
	public void setPosition(Vector2 pos) {
		sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y );
	}
	
	public void setBounds(float x, float y, float width, float height){
		sprite.setBounds(x, y, width, height);
		
	}
	
	@Override
	public void setOrientation(int orientation) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void loadGraphics(String animName, int FRAME_COLS, int FRAME_ROWS) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setAngle(float angle) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void loadEffect(String animName, int FRAME_COLS, int FRAME_ROWS,
			boolean looping, float speed) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setColor(float r, float g, float b, float a) {
		if(sprite!=null)sprite.setColor(r, g, b, a);	
	}
	
	@Override
	public Color getColor() {	
		if(sprite!=null)return sprite.getColor();
		else return Color.WHITE;
	}
	

}
