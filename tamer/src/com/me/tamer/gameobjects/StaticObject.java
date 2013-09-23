package com.me.tamer.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.physics.RigidBody;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.physics.RigidBodyCircle;
import com.me.tamer.utils.IsoHelper;
import com.me.tamer.utils.RendererFactory;

public class StaticObject implements GameObject{
	protected RigidBody body = null;
	protected Vector2 position;
	protected Vector2 size;
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
		float w = Float.parseFloat(values[0]);
		float h = Float.parseFloat(values[1]);
		renderer.setSize(w, h);
		this.size = new Vector2(w,h);
		
	}
	

	@Override
	public void setGraphics(String graphics) {
		renderer.loadGraphics(graphics);
		
	}

	@Override
	public void setPosition(String pos) {
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
	

}
