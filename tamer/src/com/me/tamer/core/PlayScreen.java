package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.me.tamer.services.MusicManager.TamerMusic;

public class PlayScreen extends AbstractScreen{

	private Stage stage;
	
	public PlayScreen(final TamerGame game){
		super(game);
		create();
	}
	
	public void create(){
		//Stop music when the level starts
		game.getMusicManager().stop();
		game.getMusicManager().setVolume(0.8f);
		game.getMusicManager().play( TamerMusic.LEVEL ); 
		
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Switching state to GAME_RUNNING");
		TamerStage.gameState = TamerStage.GAME_RUNNING;
		
		//stage has to be created after state is set to GAME_RUNNING because of the threads
		stage = new TamerStage(game);	
	}
	
	@Override
	public void show() {
		super.show();
		if(TamerStage.gameState == TamerStage.GAME_PAUSED){
				Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName() + " :: Switching state from GAME_PAUSED to GAME_RUNNING");
				TamerStage.gameState = TamerStage.GAME_RUNNING;
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
		TamerStage.gameState = TamerStage.GAME_PAUSED;
	}
	
	@Override
    public void render( float delta ){
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		((TamerStage)stage).updateCamera();
		stage.act( delta );
		stage.draw();
    }
}
	
	