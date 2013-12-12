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

		//stage has to be created after state is set to GAME_RUNNING because of the threads
		tamerStage = TamerStage.instance();
		tamerStage.setup(game);
		
//		TamerStage.gameState = TamerStage.GAME_READY;
		//((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
	}
	
	@Override
	public void show() {
		super.show();
		game.getMusicManager().resume();
		
		Gdx.input.setInputProcessor( tamerStage );
		
		//if(((TamerStage)stage).getGameState() == TamerStage.GAME_PAUSED){
			//	((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
		//}	
	}
	
	public void dispose(){
		tamerStage.dispose();
	}

	@Override
	public void hide(){
		super.hide();
		//((TamerStage)stage).setGameState(TamerStage.GAME_PAUSED);
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
	
	