package com.me.tamer.core;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
    
	//Main drawing batch
	private SpriteBatch batch 		= null;

	@Override
	public void create() {
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
        
		setScreen(new MainMenuScreen(this));	
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
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
    public void setScreen(Screen screen ){
        super.setScreen( screen );
        Gdx.app.log( TamerGame.LOG, "Setting screen: " + screen.getClass().getSimpleName() );
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
}




