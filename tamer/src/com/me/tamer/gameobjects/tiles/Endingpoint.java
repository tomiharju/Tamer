package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.utils.Helper;

public class Endingpoint extends StaticObject implements Obstacle {
	Vector2 temp = new Vector2();
	private float scale = 0;
	private float bounds = 0;
	
	public void setup(Environment environment) {
		environment.addNewObject(this);
		environment.addObstacle(this);
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

	
	public void setHitBox(String scale) {
		float s = Float.parseFloat(scale);
		this.scale = s;
		bounds = this.scale;
	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		int size = creatures.size();
		for (int i = 0; i < size; i++) {
			if (creatures.get(i).getType() == Creature.TYPE_WORM) {
				Worm worm = (Worm)creatures.get(i);
				temp.set(worm.getHead().getPosition());
				
				Vector2 s = worm.getSize();
				Vector2 center = getPosition();
				
				if (temp.x + s.x / 2 > center.x - bounds
						&& temp.x - s.x / 2 < center.x
						&& temp.y + s.y  > center.y
						&& temp.y  < center.y + bounds) {
					worm.setInsideFence(true);
					worm.kill();
				} else worm.setInsideFence(false);
					
				
			}
		}
	}
}
