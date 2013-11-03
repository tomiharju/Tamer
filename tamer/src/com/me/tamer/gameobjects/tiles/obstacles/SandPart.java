package com.me.tamer.gameobjects.tiles.obstacles;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.IsoHelper;

public class SandPart extends StaticObject{
	
	private Vector2 temp = new Vector2();

	public void setup(Environment environment){
		environment.addNewObject(this);
		setZindex(1);
	}
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(1,0.5f);
		this.setRenderType(graphics);
		
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		
		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(IsoHelper.twoDToTileIso(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x-0.1f,temp.y-0.1f, 0.2f,0.2f);
		shapeRndr.end();
		
	}
	
	public boolean getDebug(){
		return true;
	}

}
