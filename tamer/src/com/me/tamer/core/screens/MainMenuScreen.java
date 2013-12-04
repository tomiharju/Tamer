package com.me.tamer.core.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.tamer.core.TamerGame;
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
	    table.add( "the Tamer" ).spaceBottom( 50 );
	    table.row();
	
	    table.add( newGameButton ).size(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() / 12).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( levelsButton ).size(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() / 12).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( optionsButton ).size(Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight() / 12).uniform().spaceBottom( 10 );
	    table.row();		
	}
	
	@Override
	public void show(){
		super.show();
	}
}
