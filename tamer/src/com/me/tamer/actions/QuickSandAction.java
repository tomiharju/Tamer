package com.me.tamer.actions;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.WormPart;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.Interactable;

public class QuickSandAction implements Action{

	private float power = 0.5f;
	private boolean isEntered = false;
	private boolean isActivated = false;
	private Vector2 activationTreshold = null;
	private Vector2 pullDirection = null;
	private Vector2 centerOfPull = null;
	private Vector2 size = null;
	
	public QuickSandAction(){
		pullDirection = new Vector2();
		centerOfPull = new Vector2();
		activationTreshold = new Vector2();
		size = new Vector2();
	}
	@Override
	public void execute(Interactable obj) {
		if(((WormPart) obj).isTail()){
			if(!isEntered){
				activationTreshold.set((((DynamicObject) obj).getPosition().tmp().sub(centerOfPull)));
				activationTreshold.nor();
				activationTreshold.mul(-1);
				isEntered = true;
			}
			if(!isActivated){
				pullDirection.set(centerOfPull.tmp().sub(((DynamicObject) obj).getPosition()));
				System.out.println(pullDirection.dot(activationTreshold));
				if(pullDirection.dot(activationTreshold) < 0)
					isActivated = true;
				
			}
			if(isActivated){
				pullDirection.set(centerOfPull.tmp().sub(((DynamicObject) obj).getPosition()));
				pullDirection.nor().mul(power);
				((DynamicObject) obj).getVelocity().add(pullDirection);
			}
		}
	}
	@Override
	public void setCenterPoint(Vector2 point) {
		centerOfPull.set(point);
	}
	public void setSize(Vector2 size){
		this.size.set(size);
	}

}
