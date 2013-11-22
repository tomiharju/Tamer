package com.me.tamer.utils;

import aurelienribon.tweenengine.TweenAccessor;

import com.me.tamer.gameobjects.renderers.StaticRenderer;

public class StaticRendererAccessor implements TweenAccessor<StaticRenderer> {

	public static final int ALPHA = 0;

	@Override
	public int getValues(StaticRenderer target, int tweenType, float[] returnValues) {
		switch(tweenType) {
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(StaticRenderer target, int tweenType, float[] newValues) {
		switch(tweenType) {
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
