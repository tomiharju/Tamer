package com.me.tamer.gameobjects.tiles.obstacles;

import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.utils.Helper;

public class SandPart extends StaticObject{

	public void setup(Environment environment){
		environment.addNewObject(this);
		setZindex(1);
	}
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(Helper.TILESIZE);
		this.setRenderType(graphics);	
	}
}
