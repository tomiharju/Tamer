package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.services.MusicManager.TamerMusic;

public class PlayScreen extends AbstractScreen{

	private Stage stage;
	
	public PlayScreen(final TamerGame game){
		super(game);
		create();
	}
	
	public void create(){
		game.getMusicManager().stop();
		game.getMusicManager().setVolume(1.0f);
		game.getMusicManager().play( TamerMusic.LEVEL ); 

		//stage has to be created after state is set to GAME_RUNNING because of the threads
		stage = TamerStage.instance();
		((TamerStage)stage).setup(game);
		
//		TamerStage.gameState = TamerStage.GAME_READY;
		//((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
	}
	
	@Override
	public void show() {
		super.show();
		
		Gdx.input.setInputProcessor( stage );
		
		if(((TamerStage)stage).getGameState() == TamerStage.GAME_PAUSED){
				((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
		}	
		
	}
	
	public void dispose(){
		stage.dispose();
	}

	@Override
	public void hide(){
		super.hide();
		((TamerStage)stage).setGameState(TamerStage.GAME_PAUSED);
	}
	
	@Override
    public void render( float delta ){
		((TamerStage)stage).updateCamera(delta);
		stage.act( delta );
		stage.draw();
    }
}
	
	