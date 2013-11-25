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
		
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Switching state to GAME_RUNNING");
//		TamerStage.gameState = TamerStage.GAME_READY;
		((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
	}
	
	@Override
	public void show() {
		super.show();
		if(((TamerStage)stage).getGameState() == TamerStage.GAME_PAUSED){
				Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Switching state from GAME_PAUSED to GAME_RUNNING");
				((TamerStage)stage).setGameState(TamerStage.GAME_RUNNING);
		}	
		Gdx.input.setInputProcessor( stage );
	}
	
	public void dispose(){
		stage.dispose();
	}

	@Override
	public void hide(){
		super.hide();
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: switching gameState to GAME_PAUSED");
		((TamerStage)stage).setGameState(TamerStage.GAME_PAUSED);
	}
	
	@Override
    public void render( float delta ){
		Gdx.gl.glClearColor(bgColor.r,bgColor.g,bgColor.b,bgColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		((TamerStage)stage).updateCamera(delta);
		stage.act( delta );
		stage.draw();
    }
}
	
	