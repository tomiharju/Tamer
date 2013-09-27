package com.me.tamer.gameobjects.superclasses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Level;
import com.me.tamer.gameobjects.renders.RenderPool;
import com.me.tamer.gameobjects.renders.Renderer;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.physics.RigidBodyCircle;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

public class StaticObject implements GameObject{
	protected RigidBody body = null;
	protected Vector2 position;
	protected Vector2 size;
	protected String renderType = null;
	private boolean isCarbage = false;
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(renderType);
		renderer.setSize(size.x,size.y);
		renderer.setPosition(IsoHelper.twoDToIso(position));
		renderer.setOrientation(0);
		renderer.draw(batch);
		
	}

	@Override
	public void setRenderer(String renderinfo) {
		String[] info = renderinfo.split(":");
		RenderPool.addRendererToPool(info[0],info[1]);
		this.renderType = info[1];
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
	public void setSize(String size) {
		String[] values = size.split(":");
		float w = Float.parseFloat(values[0]);
		float h = Float.parseFloat(values[1]);
		this.size = new Vector2(w,h);
		
	}


	@Override
	public void setPosition(String pos) {
		String[] values = pos.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		this.position = new Vector2(x,y);
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}
	public void setRigidBody(String bodytype){
		if(bodytype.equalsIgnoreCase("box"))
			body = new RigidBodyBox(position,new Vector2(0,0),0,size.x,size.y); //Position, speed, mass, width,height ( speed and mass are 0 cause its static object )
		else if(bodytype.equalsIgnoreCase("circle"))
			body = new RigidBodyCircle(position,new Vector2(0,0),0,size.x);//Position, velocity, mass, radii
		else if(bodytype.equalsIgnoreCase("no-body"))
			body = null;
		
	}

	@Override
	public RigidBody getRigidBody() {
		return body;
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resolveForces(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void wakeUp(Level level) {
		// TODO Auto-generated method stub
		
	}

	

}
