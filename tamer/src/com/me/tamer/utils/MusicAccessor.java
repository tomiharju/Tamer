package com.me.tamer.utils;

import aurelienribon.tweenengine.TweenAccessor;
import com.me.tamer.services.MusicManager;

public class MusicAccessor implements TweenAccessor<MusicManager> {

	public static final int VOLUME = 0;

	@Override
	public int getValues(MusicManager target, int tweenType,
			float[] returnValues) {
		switch (tweenType) {
		case VOLUME:
			returnValues[0] = target.getVolume();
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(MusicManager target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case VOLUME:
			target.setVolume(newValues[0]);
			break;
		default:
			assert false;
		}
	}
}
