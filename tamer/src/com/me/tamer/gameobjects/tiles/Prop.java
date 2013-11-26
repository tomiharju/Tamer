package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
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

public class Prop extends StaticObject implements Obstacle {
	private float scale = 0;
	private float bounds = 0;
	private Vector2 temp = new Vector2();
	private Vector2 collisionAxis = new Vector2();
	private Vector2 closestVertice = new Vector2();
	private Vector2 headingAdjust = new Vector2();
	private Vector2 newHeading = new Vector2();
	private Vector2 impulse = new Vector2();
	private ArrayList<Vector2> vertices;
	private ArrayList<Vector2> axes;

	public void setup(Environment env) {
		env.addObstacle(this);
		env.addNewObject(this);
		createVertices();
	}
	public void wakeup(Environment env){
		env.addNewObject(this);
		env.addObstacle(this);
		createVertices();
	}

	public void setPixelsX(String pixels) {
		float x = Float.parseFloat(pixels);
		setSize(x / Helper.TILE_WIDTH, getSize().y);
	}

	public void setPixelsY(String pixels) {
		float y = Float.parseFloat(pixels);
		setSize(getSize().x, y / Helper.TILE_WIDTH);
	}

	public void setHitBox(String scale) {
		float s = Float.parseFloat(scale);
		this.scale = s;
		bounds = this.scale;

	}

	

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		// Only collide with ground level obstacles
		if (!(getZIndex() == -1))
			return;
		int size = creatures.size();
		for (int i = 0; i < size; i++) {
			if(((DynamicObject) creatures.get(i)).isCollisionDisabled()){
				continue;
			}
				
			temp.set(((DynamicObject) creatures.get(i)).getPosition());
			// Creatures size
			Vector2 s = ((DynamicObject) creatures.get(i)).getSize();
			Vector2 center = getPosition();
			
			if (temp.x + s.x / 2 > center.x - bounds
					&& temp.x - s.x / 2 < center.x
					&& temp.y + s.y  > center.y
					&& temp.y  < center.y + bounds) {
				
				collisionAxis.set(getCollisionNormal(creatures.get(i)
						.getHeading()));
				headingAdjust.set(Helper.projection(creatures.get(i)
						.getHeading(), collisionAxis));
				newHeading.set(creatures.get(i).getHeading().tmp()
						.sub(headingAdjust));

				closestVertice.set(getClosestVertice(((DynamicObject) creatures
						.get(i)).getPosition()));
				float distance = closestVertice.dst(((Worm) creatures.get(i)).getHead().getPosition());
				//impulse.set(collisionAxis.mul(((Worm) creatures.get(i))
					//	.getSpeed() * 2 * Gdx.graphics.getDeltaTime()));
				impulse.set(collisionAxis.mul(distance));
				((Worm) creatures.get(i)).getHead().getPosition()
						.add(impulse);
				((DynamicObject)creatures.get(i)).setHeading(newHeading);

			}
		}
	}

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {

		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x, temp.y,-bounds,bounds);
		shapeRndr.end();
		shapeRndr.begin(ShapeType.Rectangle);
		shapeRndr.rect(temp.x-0.1f, temp.y-0.1f,.2f,.2f);
		shapeRndr.end();
		/*
		 * shapeRndr.setColor(1, 1, 1, 1);
		 * temp.set(Helper.worldToScreen(getPosition()));
		 * shapeRndr.begin(ShapeType.Rectangle); shapeRndr.rect(temp.x
		 * -0.1f,temp.y-0.1f, 0.2f ,0.2f); shapeRndr.end();
		 */

	}

	public boolean getDebug() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.me.tamer.gameobjects.superclasses.StaticObject#getCenterPosition() Do
	 * we need this?
	 */
	public Vector2 getCenterPosition() {
		return getPosition().tmp().set(getPosition().x - bounds,
				getPosition().y + bounds);

	}

	/**
	 * Create corner points and normals for the hitbox
	 */
	public void createVertices() {
		vertices = new ArrayList<Vector2>(4);
		Vector2 v1 = new Vector2((getPosition().x - bounds / 2),
				(getPosition().y));
		Vector2 v2 = new Vector2((getPosition().x - bounds / 2),
				(getPosition().y + bounds));
		Vector2 v3 = new Vector2((getPosition().x + bounds / 2),
				(getPosition().y + bounds));
		Vector2 v4 = new Vector2((getPosition().x + bounds / 2),
				(getPosition().y));
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);

		axes = new ArrayList<Vector2>();
		for (int i = 0; i < this.vertices.size(); i++) {
			Vector2 p1 = this.vertices.get(i);
			Vector2 p2 = this.vertices.get(i + 1 == this.vertices.size() ? 0
					: i + 1);
			Vector2 edge = p1.cpy().sub(p2);
			Vector2 normal = edge.rotate(-90);
			normal.nor();
			axes.add(normal);
		}
	}

	public Vector2 getCollisionNormal(Vector2 heading) {
		float smallestDot = 10000;
		Vector2 axis = null;
		for (int i = 0; i < axes.size(); i++) {
			Vector2 normal = axes.get(i);
			float dot = heading.dot(normal);
			if (dot < smallestDot) {
				smallestDot = dot;
				axis = normal;
			}
		}

		return axis.nor();
	}

	public Vector2 getClosestVertice(Vector2 point) {
		float mindist = 10000;
		Vector2 vertice = point;

		for (int i = 0; i < this.vertices.size(); i++) {
			float dist = this.vertices.get(i).dst(point);
			if (dist < mindist) {
				mindist = dist;
				vertice = this.vertices.get(i);
			}
		}
		return vertice;
	}

	public void dispose(Environment environment) {
		environment.getObstacles().remove(this);
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphics(TamerTexture tex) {
		// TODO Auto-generated method stub
		
	}

}
