package com.me.tamer.core;


import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.tamer.services.MusicManager.TamerMusic;

public class MainMenuScreen extends AbstractMenu{

	public MainMenuScreen(final TamerGame game) {
		super(game);
		create();
	}
	
	@Override
	public void create(){
		super.create();
		 // retrieve the default table actor
		Table table = super.getTable();
	    table.add( "TAMER" ).spaceBottom( 50 );
	    table.row();
	
	    table.add( newGameButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( levelsButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( optionsButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();	
	}
	
	@Override
	public void show(){
		super.show();
		game.getMusicManager().setVolume(0.3f);
		game.getMusicManager().play( TamerMusic.MENU );      
	}
}
