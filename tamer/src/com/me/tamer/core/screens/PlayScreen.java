package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.services.MusicManager.TamerMusic;

public class PlayScreen extends AbstractScreen{

	
	public PlayScreen(final TamerGame game){
		super(game);
		create();
	}
	
	public void create(){
		game.getMusicManager().stop();
		game.getMusicManager().setVolume(1.0f);
		game.getMusicManager().play( TamerMusic.LEVEL ); 

		tamerStage = TamerStage.instance();
		tamerStage.setup(game);
	

	}
	
	@Override
	public void show() {
		super.show();
		game.getMusicManager().resume();
		Gdx.input.setInputProcessor( tamerStage );
	}
	
	public void dispose(){
		tamerStage.dispose();
	}

	@Override
	public void hide(){
		super.hide();
	}
	
	@Override
    public void pause(){
		((TamerStage)stage).setGameState(TamerStage.GAME_PAUSED);
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Pausing screen: " + getName() );
    }

    @Override
    public void resume(){
    	((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Resuming screen: " + getName() );
    }
	
	
	@Override
    public void render( float delta ){
		tamerStage.updateCamera(delta);
		tamerStage.act( delta );
		tamerStage.draw();
    }
	
	public TamerStage getStage(){
		return tamerStage;
	}
}
	
	