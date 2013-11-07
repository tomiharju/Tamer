package com.me.tamer.core;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tamer.services.LevelManager;
import com.me.tamer.services.MusicManager;
import com.me.tamer.services.PreferenceManager;


public class TamerGame extends Game{
	 // for logging
    public static final String LOG = TamerGame.class.getSimpleName();


	//FPS limiting
	int FPS = 30;
	long lastFrame = 0;
	long curFrame = System.currentTimeMillis();
    
	//Services
    private MusicManager musicManager;
    private PreferenceManager preferenceManager;
    private LevelManager levelManager;
    
    //Screens
    private PlayScreen playScreen;
    private MainMenuScreen mainMenuScreen;
    private PauseScreen pauseScreen;
    private LevelsScreen levelsScreen;
    
	//Main drawing batch
	private SpriteBatch batch 		= null;
	
	Hud hud;

	@Override
	public void create() {
		//Set log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() +" :: Creating game on " + Gdx.app.getType() );
		//Spritebatch is used for drawing sprites
		batch = new SpriteBatch();
		
		// create the preference manager
		preferenceManager = new PreferenceManager();
		
		// create the music manager
        musicManager = new MusicManager();
        musicManager.setVolume( preferenceManager.getVolume() );
        musicManager.setEnabled( preferenceManager.isMusicEnabled() );
        
        // create the level manager
        levelManager = new LevelManager();
        
        //create menu screens
        mainMenuScreen = new MainMenuScreen(this);
        pauseScreen = new PauseScreen(this);
        levelsScreen = new LevelsScreen(this);
        
        
        //start the game with main menu screen
		setScreen(mainMenuScreen);	
		
		//Hud
		hud = Hud.instance();
	}
	
	public PlayScreen createNewPlayScreen(){
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Reseting hud");
		hud.resetHud();
		
		//dispose old before making new one
		if (playScreen != null) playScreen.dispose();
		playScreen = new PlayScreen(this);
		
		
	
		return playScreen;
	}

	@Override
	public void render(){
		batch.begin();
		if (getScreen() != null) getScreen().render(Gdx.graphics.getDeltaTime());
		batch.end();
	}
	
	@Override
    public void pause()
    {
        super.pause();
        Gdx.app.log( TamerGame.LOG, "Pausing game" );
    }

    @Override
    public void resume(){
        super.resume();
        Gdx.app.log( TamerGame.LOG, "Resuming game" );
    }

    @Override
    public void dispose(){
        super.dispose();
        Gdx.app.log( TamerGame.LOG, "Disposing game" );
        musicManager.dispose();
        
        //sprites
        batch.dispose();
    }
    
	public MusicManager getMusicManager(){
        return musicManager;
    }
	
	public PlayScreen getPlayScreen() {
		return playScreen;
	}

	public MainMenuScreen getMainMenuScreen() {
		return mainMenuScreen;
	}

	public PauseScreen getPauseScreen() {
		return pauseScreen;
	}

	public LevelsScreen getLevelsScreen() {
		return levelsScreen;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}
}




