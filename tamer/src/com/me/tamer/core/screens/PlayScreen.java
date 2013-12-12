package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

		stage = TamerStage.instance();
		((TamerStage)stage).setup(game);
	}
	
	@Override
	public void show() {
		super.show();
		game.getMusicManager().resume();
		
		Gdx.input.setInputProcessor( stage );
//		if(((TamerStage)stage).getGameState() == TamerStage.GAME_PAUSED){
//				((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
//		}	
	}
	
	public void dispose(){
		stage.dispose();
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
		((TamerStage)stage).updateCamera(delta);
		stage.act( delta );
		stage.draw();
    }
	
	public TamerStage getStage(){
		return (TamerStage)stage;
	}
}
	
	