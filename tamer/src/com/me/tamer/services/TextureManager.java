package com.me.tamer.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.me.tamer.core.TamerGame;

public class TextureManager {
	AssetManager assetManager;
	
	public enum TamerTexture{
		//Animated gameobjects
		ANT("animations/antorc"),
		SPEAR("animations/spear"),
		TAMER("animations/tamer"),
		WORMPART("animations/vwormpart2"),
		WORMHEAD("animations/wormhead"),
		
		//controls
		BUTTON_SPEAR("button_spear"),
		BUTTON_SPEAR_GLOW("button_spear_glow"),
		BUTTON_SCREAM("button_scream"),
		BUTTON_SCREAM_GLOW("button_scream_glow"),
		JOYSTICK("joystick"),
		JOYSTICK_INNER("joystick_inner"),
		
		//
		TAMER_SHADOW("tamershadow"),
		SCREAM("scream"),
		TARGET_TILE("targetTile"),
		SWAMP_TEST("animations/swamp1");
		
		private final String fileName;
		private final String animationsPath = "data/graphics/";
		private final String extension = ".png";
		
		private TamerTexture(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return animationsPath + fileName + extension;
		}
	}
	
	public void loadTextures(){
		for( TamerTexture t : TamerTexture.values() ){
			assetManager.load(t.getFileName(), Texture.class);
			Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Started loading asset: " +t.name());
		}
	}
	
	public void setAssetManager(AssetManager m){
		this.assetManager = m;
	}
}
