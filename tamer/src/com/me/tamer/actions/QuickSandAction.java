package com.me.tamer.actions;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.WormPart;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.Interactable;
import com.me.tamer.utils.tTimer;

public class QuickSandAction implements Action{

	private float power = 8.1f;
	private boolean isEntered = false;
	private boolean isActivated = false;
	private Vector2 activationTreshold = null;
	private Vector2 pullDirection = null;
	private Vector2 centerOfPull = null;
	private Vector2 size = null;
	private ArrayList<Interactable> affectedParts = null;
	public QuickSandAction(){
		pullDirection = new Vector2();
		centerOfPull = new Vector2();
		activationTreshold = new Vector2();
		size = new Vector2();
		affectedParts = new ArrayList<Interactable>();
	}
	@Override
	public void execute(Interactable obj) {
		System.out.println("Worm is in sandpit radius");

		if(((WormPart) obj).isTail()){
			System.out.println("Worm is in sandpit radius");
			if(!affectedParts.contains(obj))
				affectedParts.add(obj);
			
			if(!isEntered){
				System.out.println("SAnd entered");
				activationTreshold.set((((DynamicObject) obj).getPosition().tmp().sub(centerOfPull)));
				activationTreshold.nor();
				activationTreshold.mul(-1);
				isEntered = true;
			
			}
			if(!isActivated){
				pullDirection.set(centerOfPull.tmp().sub(((DynamicObject) obj).getPosition()));
				if(pullDirection.dot(activationTreshold) < 0){
					isActivated = true;
					new tTimer(this,"killPart",5).start();
				}
				
			}
		}
			if(isActivated){
				System.out.println("Sand activated");
				pullDirection.set(centerOfPull.tmp().sub(((DynamicObject) obj).getPosition()));
				float distance = pullDirection.len();
				pullDirection.nor();
				pullDirection.mul((float) Math.sqrt(distance));
				pullDirection.mul(power);
				((DynamicObject) obj).getVelocity().add(pullDirection.mul(Gdx.graphics.getDeltaTime()));
				
				
					
				
			}
		
	}
	@Override
	public void setCenterPoint(Vector2 point) {
		centerOfPull.set(point);
	}
	public void setSize(Vector2 size){
		this.size.set(size);
	}
	public void killPart(){
			if(affectedParts.size() == 0){
				isActivated = false;
				isEntered = false;
				System.out.println("Sandpit deactivating....");
			}else{
				new tTimer(this,"killPart",5).start();
				for(Interactable i : affectedParts)
					i.kill();
				
				affectedParts.clear();
	}
	}

}
