package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.me.tamer.core.TamerGame;
import com.me.tamer.services.MusicManager.TamerMusic;

public class PauseScreen extends AbstractMenu{
	//Pause menu spesific buttons
	TextButton continueButton, restartButton;
	
	public PauseScreen(final TamerGame game) {
		super(game);
		create();
	}
	
	@Override
	public void create(){
		super.create();
		
		continueButton = new TextButton("Continue",textButtonStyle);
		continueButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.setScreen(game.getPlayScreen());
            }
        });
		
		restartButton = new TextButton("Restart",textButtonStyle);
		restartButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                game.setScreen( game.createNewPlayScreen() );
            }
        });
		
		Table table = super.getTable();
	    table.add( "Pause" ).spaceBottom( 50 );
	    table.row();
	
	    table.add( continueButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( restartButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( optionsButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( mainMenuButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	}
	
	@Override
	public void show(){
		super.show();
	}
}
