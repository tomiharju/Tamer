package com.me.tamer.utils;

import com.me.tamer.actions.Action;
import com.me.tamer.actions.Action.ActionType;
import com.me.tamer.actions.EndingPointAction;
import com.me.tamer.actions.QuickSandAction;
import com.me.tamer.actions.StartingPointAction;



public class ActionFactory {
	
	public static Action createAction(ActionType action){
		switch(action){
		case ENDINGPOINT:
			return new EndingPointAction();
		case QUICKSAND:
			return new QuickSandAction();
		case STARTINGPOINT:
			return new StartingPointAction();
		default:
			return null;
			
		
		}
	}

}
