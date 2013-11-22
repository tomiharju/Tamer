package com.me.tamer.utils;

import aurelienribon.tweenengine.TweenAccessor;

import com.me.tamer.core.screens.AbstractScreen;

public class ScreenAccessor implements TweenAccessor<AbstractScreen> {

	public static final int RED = 0;
	public static final int ALPHA = 1;

	@Override
	public int getValues(AbstractScreen target, int tweenType,
			float[] returnValues) {
		switch(tweenType) {
		case RED:
			returnValues[0] = target.getColor().r;
			return 1;
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(AbstractScreen target, int tweenType,
			float[] newValues) {
		switch(tweenType) {
		case RED:
			target.setColor(newValues[0], target.getColor().g, target.getColor().b, target.getColor().a);
			break;
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			break;
		default:
			assert false;
		}
		
	}

}
