package com.me.tamer.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.me.tamer.core.TamerGame;

public class TextureManager {
	AssetManager assetManager;
	
	public enum TamerStatic{

		
		BUTTON_SPEAR("button_spear"),
		BUTTON_SPEAR_GLOW("button_spear_glow"),
		BUTTON_SPEAR_GLOW_BLUE("button_spear_glow_blue"),
		BUTTON_SCREAM("button_scream"),
		BUTTON_SCREAM_GLOW("button_scream_glow"),
		JOYSTICK("joystick"),
		JOYSTICK_INNER("joystick_inner"),
		//Tamer skil graphics
		TAMER_SHADOW("tamershadow"),
		SCREAM("scream"),
		TARGET_TILE("targetTile"),
		SPEAR_CRACK("spearCrack");

		private final String fileName;

		private TamerStatic(String fileName) {
			this.fileName = fileName;
		}
		public String getFileName() {
			return fileName;
		}
	}
	
	public enum TamerAnimations{
		//Animated gameobjects
		ANT("animations/antorc"),
		SPEAR("animations/spear"),
		TAMER("animations/tamer"),
		WORMPART("animations/vwormpart2"),
		WORMHEAD("animations/wormhead");
		
	
		
		
		private final String fileName;
		private final String animationsPath = "data/graphics/";
		private final String extension = ".png";
		
		private TamerAnimations(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return animationsPath + fileName + extension;
		}
	}
	
	public void loadTextures(){
		for( TamerAnimations t : TamerAnimations.values() ){
			assetManager.load(t.getFileName(), Texture.class);
			Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Started loading asset: " +t.name());
		}
	}
	
	public void setAssetManager(AssetManager m){
		this.assetManager = m;
	}
}
