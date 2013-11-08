package com.me.tamer.ui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.tamer.core.Hud;
import com.me.tamer.utils.Helper;

public class TouchWrap extends Actor{
	private ControlContainer controls;
	private boolean aimMode = false;
	private float throwDistance = 0.0f;

	public TouchWrap() {
		this.controls = ControlContainer.instance();	
		setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - Hud.SIZE); 
		createInputListener();
	}
	
	public void createInputListener(){
		this.addListener(new InputListener(){
			Vector3 input = new Vector3();
			Vector2 waypoint1 = new Vector2(),waypoint2 = new Vector2(),waypoint3 = new Vector2(),targetPoint = new Vector2();
			private ArrayList<Vector2> waypoints = new ArrayList<Vector2>();
			private Vector2 help = new Vector2();
			
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				
				if (aimMode){
					input.set(x,Gdx.graphics.getHeight() - y,0);
					controls.getStage().getCamera().unproject(input);
					targetPoint.set(input.x, input.y);
					
					help.set(controls.getEnvironment().getTamer().getShadow().getPosition());
					targetPoint = Helper.screenToWorld(targetPoint);
					
					if(help.dst(targetPoint) < throwDistance / 2){	
						//this is where spear ends up
						//help.set( ((Tamer)tamer).getShadow().getPosition() );
						//waypoint1.set(help.add(targetPoint));
						waypoint1.set(targetPoint);
						
						//help.set( ((Tamer)tamer).getPosition() );
						//waypoint2.set(help.add(targetPoint));
						//Set camera to follow way point 2
						//cameraPoint.set(waypoint2);
						
						//help.set( ((Tamer)tamer).getPosition().tmp().add(-3,3) );
						//waypoint3.set(help.add(targetPoint.tmp().mul(0.8f)));
						
						waypoints.clear();
						waypoints.add(waypoint1);
						//waypoints.add(waypoint2);
						//waypoints.add(waypoint3);

						//Stop aiming after spear has been thrown
						controls.getSpearButton().stopAim();
						//controls.getEnvironment().getTamer().tryThrowSpear(waypoints );
					}
					
						
					return true;
				}else
					return false;
			}
		});
	}
	
	public void setAimMode(boolean b){
		aimMode = b;
	}
	
	public void setThrowDistance(float d){
		throwDistance = d;
	}
}
