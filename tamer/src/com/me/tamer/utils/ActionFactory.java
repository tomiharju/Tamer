package com.me.tamer.utils;

import com.me.tamer.actions.Action;
import com.me.tamer.actions.Action.ActionType;
import com.me.tamer.actions.EndingPointAction;
import com.me.tamer.actions.QuickSandAction;
import com.me.tamer.actions.StartingPointAction;



public class ActionFactory {
	
	public static Action createAction(String action) throws IllegalArgumentException{
		if(action.equalsIgnoreCase("endingpoint"))
			return new EndingPointAction();
		else if(action.equalsIgnoreCase("quicksand"))
			return new QuickSandAction();
		else if(action.equalsIgnoreCase("startingpoint"))
			return new StartingPointAction();
		else
			throw new IllegalArgumentException("Uknown action");
	}

}
