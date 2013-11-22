package com.me.tamer.utils;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tamer.gameobjects.renderers.Renderer;

public class RendererAccessor implements TweenAccessor<Renderer> {

	public static final int ALPHA = 0;

	@Override
	public int getValues(Renderer target, int tweenType, float[] returnValues) {
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
	public void setValues(Renderer target, int tweenType, float[] newValues) {
		switch(tweenType) {
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
