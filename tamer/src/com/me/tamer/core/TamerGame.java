package com.me.tamer.core;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.me.tamer.core.screens.AbstractScreen;
import com.me.tamer.core.screens.LevelCompleteScreen;
import com.me.tamer.core.screens.LevelsScreen;
import com.me.tamer.core.screens.LoadingScreen;
import com.me.tamer.core.screens.MainMenuScreen;
import com.me.tamer.core.screens.PauseScreen;
import com.me.tamer.core.screens.PlayScreen;
import com.me.tamer.services.LevelManager;
import com.me.tamer.services.MusicManager;
import com.me.tamer.services.PreferenceManager;
import com.me.tamer.utils.MusicAccessor;
import com.me.tamer.utils.ScreenAccessor;


/**
 * @author ville
 * 
 */
public class TamerGame extends Game {
	// for logging
	public static final String LOG = TamerGame.class.getSimpleName();

	public enum ScreenType {
		NEW_PLAY, RESUME_PLAY, MENU, PAUSE, LEVELS, COMPLETE, LOADING
	}

	// Services
	private MusicManager musicManager;
	private PreferenceManager preferenceManager;
	private LevelManager levelManager;
	private AssetManager assetManager;
	protected TweenManager tweenManager;

	// Screens
	private PlayScreen playScreen;
	private MainMenuScreen mainMenuScreen;
	private PauseScreen pauseScreen;
	private LevelsScreen levelsScreen;
	private LevelCompleteScreen levelCompleteScreen;
	private LoadingScreen loadingScreen;

	boolean fading = false;
	
	@Override
	public void create() {
		// Set log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: Creating game on " + Gdx.app.getType());

		// create the preference manager
		preferenceManager = new PreferenceManager();

		// create the music manager
		musicManager = new MusicManager();
		musicManager.setVolume(preferenceManager.getVolume());
		musicManager.setEnabled(preferenceManager.isMusicEnabled());

		// create the level manager
		levelManager = new LevelManager();

		// Create asset manager
		assetManager = new AssetManager();

		// create menu screens
		mainMenuScreen = new MainMenuScreen(this);
		pauseScreen = new PauseScreen(this);
		levelsScreen = new LevelsScreen(this);
		loadingScreen = new LoadingScreen(this,assetManager);
		levelCompleteScreen = new LevelCompleteScreen(this);

		// start the game with main menu screen
		setScreen(ScreenType.LOADING);
	}


	@Override
	public void render() {
		Gdx.gl.glClearColor(7f / 255, 10f / 255, 27f / 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		getScreen().render(Gdx.graphics.getDeltaTime());
		
		if (fading) {
			levelCompleteScreen.render(Gdx.graphics.getDeltaTime());
			tweenManager.update(Gdx.graphics.getDeltaTime());
			if (levelCompleteScreen.getFadingDone()) {
				levelCompleteScreen.showScreenContent();
				tweenManager = null;
				fading = false;

				//set levelComplete screen officially
				playScreen = null;
				setScreen(levelCompleteScreen);
			}
		}
	}

	public void setScreen(ScreenType type) {
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: Setting screen " + type);
		switch (type) {
		case NEW_PLAY:
			this.playScreen = new PlayScreen(this);
			super.setScreen( playScreen );
			break;
		case RESUME_PLAY:
			super.setScreen(playScreen);
			break;
		case LOADING:
			super.setScreen(loadingScreen);
			break;
		case MENU:
			super.setScreen(mainMenuScreen);
			break;
		case LEVELS:
			super.setScreen(levelsScreen);
			break;
		case COMPLETE:
//			levelCompleteScreen.resetFadingDone();
			
			tweenManager = new TweenManager();

			// fade screen tween
			Tween.registerAccessor(AbstractScreen.class, new ScreenAccessor());
			Tween.to(levelCompleteScreen, ScreenAccessor.ALPHA, 5.0f).target(1)
					.delay(0.0f).start(tweenManager);

			// fade music tween
			Tween.registerAccessor(MusicManager.class, new MusicAccessor());
			Tween.to(musicManager, MusicAccessor.VOLUME, 5.0f).target(0)
					.delay(0.0f).start(tweenManager);
			
			this.levelCompleteScreen.show();
			this.levelCompleteScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			fading=true;
			break;
		case PAUSE:
			super.setScreen(pauseScreen);
			break;
		default:
			break;
		}
	}

	@Override
	public void pause() {
		super.pause();
		Gdx.app.log(TamerGame.LOG, "Pausing game");
	}

	@Override
	public void resume() {
		super.resume();
		Gdx.app.log(TamerGame.LOG, "Resuming game");
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.app.log(TamerGame.LOG, "Disposing game");
		assetManager.dispose();
		
		mainMenuScreen.dispose();
		pauseScreen.dispose();
		levelsScreen.dispose();
		levelCompleteScreen.dispose();
		loadingScreen.dispose();
	}
	
	public LevelCompleteScreen getLevelCompleteScreen(){
		return levelCompleteScreen;
	}

	public PlayScreen getPlayScreen() {
		return playScreen;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
