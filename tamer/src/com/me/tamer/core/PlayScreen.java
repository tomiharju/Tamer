package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.me.tamer.services.MusicManager.TamerMusic;

public class PlayScreen extends AbstractScreen{

	private Environment environment;
	
	public PlayScreen(TamerGame game){
		super(game);
	}
	
	@Override
	public void show() {
		//This method is called when the app is loaded
		game.getMusicManager().stop();
		environment = new Environment();

        // add the ship to the stage
        stage.addActor( environment );

        // add a fade-in effect to the whole stage
        stage.getRoot().getColor().a = 0f;
        stage.getRoot().addAction( Actions.fadeIn( 0.5f ) );
	}
}
	
	