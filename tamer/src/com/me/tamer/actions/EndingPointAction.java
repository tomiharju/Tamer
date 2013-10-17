package com.me.tamer.actions;

import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.Creature;

public class EndingPointAction implements Action{

	@Override
	public void execute(Creature obj) {
			obj.moveToFinish();
		//TODO: ScoreManager.addWormRescueScore(sizeOfRescuedWorm);
	}

	@Override
	public void setCenterPoint(Vector2 point) {
		// TODO Auto-generated method stub
		
	}

}
