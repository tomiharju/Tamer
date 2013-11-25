package com.me.tamer.core.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.tamer.core.Level;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerGame.ScreenType;
import com.me.tamer.services.SoundManager.TamerSound;


public class LevelsScreen extends AbstractMenu{
	List<TextButton> levelButtons;
	protected List<Level> levels;
	protected int levelId = 0;
	
	public LevelsScreen(final TamerGame game){
		super(game);
		create();
	}
	
	@Override
	public void create(){
		super.create();
		levelButtons = new ArrayList<TextButton>();
		levels = game.getLevelManager().getLevels();
		
		levelId = 0;
		
		for ( int i = 0; i < levels.size(); i++){
			TextButton newButton = new TextButton( levels.get(i).getName() ,textButtonStyle);		
			newButton.addListener(new ChangeListener() {
				final int button = levelId;
	            public void changed (ChangeEvent event, Actor actor) { 
	            	sound.play(TamerSound.MENU_CLICK);
	            	game.getLevelManager().setCurrentLevel( levels.get(button).getId() );
	            	game.setScreen( ScreenType.NEW_PLAY );
	            	
	            }
	        });
			
			levelButtons.add(newButton);
			levelId++;
		}
		
		Table table = super.getTable();
	    table.add( "Levels" ).spaceBottom( 50 );
	    table.row();
	    
	    for (int i = 0; i < levelButtons.size(); i++){
	    	table.add(levelButtons.get(i)).size( 300, 60 ).uniform().spaceBottom( 10 );
	    	table.row();
	    }
	    table.add(mainMenuButton).size( 300, 60 ).uniform().spaceBottom( 10 );
	}
	
	@Override
	public void show() {
		super.show();	    
	}
}
