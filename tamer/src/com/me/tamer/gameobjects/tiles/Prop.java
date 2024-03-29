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
import com.me.tamer.utils.Helper;

/**
 * @author Tamer
 * Props are all the gameobjects which are static and which can be collided with.
 * Prop uses Separating axis theorem to resolve collisions. 
 *
 */
public class Prop extends StaticObject implements Obstacle {
	private float scale = 0;
	private Vector2 temp = new Vector2();
	private Vector2 collisionAxis = new Vector2();
	private Vector2 newHeading = new Vector2();
	private Vector2 impulse = new Vector2();
	private Vector2 pos = new Vector2();
	private ArrayList<Vector2> vertices;
	private ArrayList<Vector2> axes;
	
	
	private Vector2 projectResult1 = new Vector2();
	private Vector2 projectResult2 = new Vector2();
	private Vector2 projection1 = new Vector2();
	private Vector2 projection2 = new Vector2();
	private Vector2 closest = new Vector2();
	private Vector2 axis = new Vector2();
	private float overlap = 0;
	public void setup(Environment env) {
		env.addObstacle(this);
		env.addNewObject(this);
		createVertices();
	}

	@Override
	public void wakeUp(Environment env) {
		env.addObstacle(this);
		createVertices();
	}

	public void setPixelsX(String pixels) {
		float x = Float.parseFloat(pixels);
		setSize(x / Helper.TILE_WIDTH_PIXEL, getSize().y);
	}

	public void setPixelsY(String pixels) {
		float y = Float.parseFloat(pixels);
		setSize(getSize().x, y / Helper.TILE_WIDTH_PIXEL);
	}

	public void setHitBox(String scale) {
		float s = Float.parseFloat(scale);
		this.scale = s;
		setBounds(this.scale);

	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		// Only collide with ground level obstacles
		if (!(getZIndex() == -1))
			return;
		int size = creatures.size();
		for (int i = 0; i < size; i++) {
			if (((DynamicObject) creatures.get(i)).isCollisionDisabled()) {
				continue;
			}

			temp.set(((Worm) creatures.get(i)).getHead().getCenterPosition());
			// Creatures size
			Vector2 s = ((DynamicObject) creatures.get(i)).getSize();
			// Prop position help vector
			pos.set(getPosition());
			if(temp.x + s.x / 2 > pos.x - getBounds() 
					&& temp.x - s.x / 2 < pos.x  
					&& temp.y + s.y / 2 > pos.y 
					&& temp.y - s.y / 2 < pos.y + getBounds() ){
				
				collisionAxis.set(getCollisionNormal(temp));
				impulse.set(collisionAxis.tmp().mul(-overlap));
				newHeading.set(creatures.get(i).getHeading().tmp().add(collisionAxis));
				newHeading.add(collisionAxis.tmp().mul(0.1f));
				((DynamicObject) creatures.get(i)).setHeading(newHeading);
				((Worm) creatures.get(i)).getHead().getPosition().add(impulse);
			}
		}
	}

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {

		shapeRndr.setColor(1, 1, 1, 1);
		temp.set(Helper.worldToScreen(getPosition()));
		for(int i = 0 ; i < vertices.size() ; i ++){
		shapeRndr.begin(ShapeType.Rectangle);
		temp.set(Helper.worldToScreen(vertices.get(i)));
		shapeRndr.rect(temp.x - 0.1f, temp.y - 0.1f, 0.2f, 0.2f);
		shapeRndr.end();
		}
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

	public Vector2 getCenterPosition() {
		return getPosition().tmp().set(getPosition().x - getBounds(),
				getPosition().y + getBounds());

	}

	/**
	 * Create corner points and normals for the hitbox
	 */
	public void createVertices() {
		vertices = new ArrayList<Vector2>(4);
		Vector2 v1 = new Vector2((getPosition().x),
				(getPosition().y));
		Vector2 v2 = new Vector2((getPosition().x - getBounds()),
				(getPosition().y));
		Vector2 v3 = new Vector2((getPosition().x - getBounds()),
				(getPosition().y + getBounds()));
		Vector2 v4 = new Vector2((getPosition().x),
				(getPosition().y) + getBounds());
		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);

		axes = new ArrayList<Vector2>();
		for (int i = 0; i < this.vertices.size(); i++) {
			Vector2 p1 = this.vertices.get(i);
			Vector2 p2 = this.vertices.get(i + 1 == this.vertices.size() ? 0
					: i + 1);
			Vector2 edge = p2.cpy().sub(p1);
			Vector2 normal = edge.rotate(90);
			normal.nor();
			axes.add(normal);
		}
	}

	public Vector2 getCollisionNormal(Vector2 creaturePos) {

		overlap = -10000f;
		closest.set(getClosestVertice(creaturePos));
		axis.set(closest.tmp().sub(creaturePos));
		axis.nor();
		
		projection1.set(project(axis));
		projection2.set(projectPoint(axis,creaturePos));
		
		float o = getOverlap(projection1, projection2);
		if( o > overlap){
			overlap = o;
			collisionAxis.set(axis);
		}
		for( int i = 0 ; i < axes.size() ; i ++){
			axis.set(axes.get(i));
			projection1.set(project(axis));
			projection2.set(projectPoint(axis,creaturePos));
			
			o = getOverlap(projection1, projection2);
			if( o > overlap){
				overlap = o ;
				collisionAxis.set(axis);
			
			}
		}
		return collisionAxis;

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

	public Vector2 project(Vector2 axis) {
		float min = axis.dot(this.vertices.get(0));
		float max = min;

		for (int i = 0; i < this.vertices.size(); i++) {
			float p = axis.dot(this.vertices.get(i));
			if (p < min)
				min = p;
			else if (p > max)
				max = p;
		}
		return projectResult1.set(min, max);
	}

	public Vector2 projectPoint(Vector2 axis,Vector2 creaturePos) {

		float center = axis.dot(creaturePos);
		float min = center - 0.5f;
		float max = center - 0.5f;
		return projectResult2.set(min, max);
		
	}

	public float getOverlap(Vector2 a, Vector2 b) {
		float max0 = a.y;
		float min1 = b.x;
		return min1 - max0;
	}

	public void dispose(Environment environment) {
		environment.getObstacles().remove(this);
	}

}
