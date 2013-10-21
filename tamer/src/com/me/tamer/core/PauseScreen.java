package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.me.tamer.services.MusicManager.TamerMusic;

public class PauseScreen extends AbstractMenu{
	//Pause menu spesific buttons
	TextButton continueButton;
	
	public PauseScreen(final TamerGame game) {
		super(game);
		create();
	}
	
	@Override
	public void create(){
		super.create();
		
		continueButton = new TextButton("Continue Game",textButtonStyle);
		continueButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("TODO:Continue game");
            }
        });
		 
	}
	
	@Override
	public void show(){
		super.show();
		game.getMusicManager().play( TamerMusic.MENU );
	    // retrieve the default table actor
		Table table = super.getTable();
	    table.add( "Pause" ).spaceBottom( 50 );
	    table.row();
	
	    table.add( continueButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( optionsButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( toMainMenuButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();   
	}
}
