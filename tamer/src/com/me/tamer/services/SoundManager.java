package com.me.tamer.services;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader.SoundParameter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.me.tamer.core.TamerGame;

/**
 * A service that manages the sound effects.
 */
/**
 * @author ville
 *
 */
public class SoundManager  {
	//sound manager is a singleton
	private static SoundManager singleton;
	private AssetManager assetManager;
	
	public static SoundManager instance(){
		if (singleton==null)singleton = new SoundManager();
		return singleton;
	}
	
	/**
	 * Available sound files
	 */
	public enum TamerSound {
		//Menu
		MENU_CLICK("Menu_nappi01.ogg"),
		
		TAMER_SPEAR("Heitto02.ogg"),
		GRYPHON("hawk.wav"),
		ANT_EAT("Murkku_syonti01.ogg"),
		
		//Spear
		SPEAR_WORM("Keihas_mato03.ogg"),
		SPEAR_ANT("Keihas_murkku08.ogg"),
		SPEAR_SWAMP("Keihas_suo10.ogg"),
		SPEAR_GROUND("Keihas_nurmi02.ogg"),
		
		//old
		OPENING("01taunt00a.wav"),
		HIT("06kill03.wav"),
		HAWK("hawk.wav");
		

		private final String fileName;
		private final String path = "sound/";

		private TamerSound(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return path + fileName;
		}
	}

	/**
	 * The volume to be set on the sound.
	 */
	private float volume = 1.0f;

	/**
	 * Whether the sound is enabled.
	 */
	private boolean enabled = true;

	/**
	 * Creates the sound manager.
	 * constructor is private because this is a singleton
	 */
	private SoundManager() {
	}

	public void loadSounds(){
		for (TamerSound sound : TamerSound.values()){
			assetManager.load(sound.getFileName(), Sound.class);
			Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Started loading asset: " +sound.name());
		}
		
	}
	
	public void setAssetManager(AssetManager assetManager){
		this.assetManager = assetManager;
	}
	
	public void play(TamerSound sound) {
		// check if the sound is enabled
		if (!enabled)
			return;
		if ( assetManager.isLoaded(sound.getFileName(),Sound.class) ){
			Sound soundToPlay = assetManager.get( sound.getFileName(), Sound.class );
			Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Playing sound: " + sound.name());
			soundToPlay.play(volume);
		} else {
			Gdx.app.error(TamerGame.LOG, this.getClass().getSimpleName() + " :: Sound is not loaded: " + sound.name());
		}		
	}

	/**
	 * Sets the sound volume which must be inside the range [0,1].
	 */
	public void setVolume(float volume) {
		Gdx.app.log(TamerGame.LOG, "Adjusting sound volume to: " + volume);

		// check and set the new volume
		if (volume < 0 || volume > 1f) {
			throw new IllegalArgumentException(
					"The volume must be inside the range: [0,1]");
		}
		this.volume = volume;
	}

	/**
	 * Enables or disabled the sound.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Disposes the sound manager.
	 * TODO
	 */
	public void dispose() {
//		Gdx.app.log(TamerGame.LOG, "Disposing sound manager");
//		for (Sound sound : soundCache.retrieveAll()) {
//			sound.stop();
//			sound.dispose();
//		}
	}
}
