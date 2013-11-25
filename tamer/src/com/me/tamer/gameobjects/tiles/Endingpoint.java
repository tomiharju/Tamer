package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.tamer.core.Hud;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.services.TextureManager.TamerTexture;
import com.me.tamer.utils.Helper;

public class Endingpoint extends StaticObject implements Obstacle {

	public void setup(Environment environment) {
		environment.addNewObject(this);
		environment.getObstacles().add(this);
		setZindex(1);

	}

	public void setPixelsX(String pixels) {
		float x = Float.parseFloat(pixels);
		setSize(x, getSize().y);
	}

	public void setPixelsY(String pixels) {
		float y = Float.parseFloat(pixels);
		setSize(getSize().x, y);
	}

	public void setGraphics(String graphics) {
		Renderer render = RenderPool.addRendererToPool("static", graphics);
		render.loadGraphics(graphics);
		setSize(Helper.TILESIZE);
		setRenderType(graphics);
	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		int size = creatures.size();
		for (int i = 0; i < size; i++) {
			if (creatures.get(i).getType() == Creature.TYPE_WORM) {
				Worm worm = (Worm)creatures.get(i);
				if (worm.isWithinRange(getPosition(), 1f) ) {
					worm.setInsideFence(true);
				} else worm.setInsideFence(false);
			}
		}
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setGraphics(TamerTexture tex) {
		// TODO Auto-generated method stub
		
	}
}
