package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerGame.ScreenType;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.TextureManager;

public class LoadingScreen extends AbstractScreen{
	AssetManager assetManager;
	
	public LoadingScreen(TamerGame game) {
		super(game);
	}
	
	public void initialize(AssetManager assetManager){
		this.assetManager = assetManager;
	}
	
	@Override 
	public void show(){
		super.show();
		loadAssets();
	}
	
	@Override
    public void render( float delta ){
		//Get asset manager progress
		float progress = assetManager.getProgress();
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Loading progress: " +progress);
		
		if ( assetManager.update() ){	
			Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Queue asset: " +assetManager.getQueuedAssets());
			Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Loaded asset: " +assetManager.getLoadedAssets());	
			game.setScreen(ScreenType.MENU);
		}
    }
	
	public void loadAssets(){
		//Instantiate soundmanager and pass assetManager for it
		SoundManager soundManager = SoundManager.instance();
		soundManager.setAssetManager( assetManager );
		soundManager.loadSounds();
		
		//Textures
		TextureManager textureManager = new TextureManager();
		textureManager.setAssetManager( assetManager );
		textureManager.loadTextures();
		
		//Atlas
		assetManager.load("data/graphics/sheetData", TextureAtlas.class);
	}

}
