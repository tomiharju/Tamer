package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;
import com.me.tamer.core.TamerGame;
import com.me.tamer.services.MusicManager;
import com.me.tamer.services.SoundManager.TamerSound;

public class LevelCompleteScreen extends AbstractMenu{
	
	private Image bgImage;
	private boolean fadingDone;
	MusicManager music;
	
	public LevelCompleteScreen(TamerGame game) {
		super(game);
		bgColor = new Color(1,1,1,0);
		create();
	}
	
	@Override
	public void create(){
		super.create();
		bgImage = new Image((new Texture(Gdx.files.internal("data/graphics/levelcomplete_bg.png"))));
		bgImage.setFillParent(true);
		stage.addActor(bgImage);
		music = game.getMusicManager();
		
		// retrieve the default table actor
		Table table = super.getTable();
	    table.add( endCapturedWorms ).uniform().spaceBottom(10);
	    table.row();
	    table.add( nextLevelButton ).size(Gdx.graphics.getWidth() /3, Gdx.graphics.getHeight() / 15).uniform().spaceBottom(10);
	    table.row();
	    table.add( playAgainButton ).size(Gdx.graphics.getWidth() /3, Gdx.graphics.getHeight() / 15).uniform().spaceBottom(10);
	    table.row();
	    table.add( exitButton ).size(Gdx.graphics.getWidth() /3, Gdx.graphics.getHeight() / 15).uniform().spaceBottom(10);
	    
	    stage.addActor(table);
	    table.setVisible(false);
	}
	
	public void showScreenContent(){
		music.stop();
		
		//Good and bad ending for a level
		if ( game.getPlayScreen().getStage().getLevel().getCaptured() > 0){
			endCapturedWorms.setText("You captured " + game.getPlayScreen().getStage().getLevel().getCaptured() +" beasts");
			sound.play(TamerSound.LEVEL_END_GOOD);
		} else {
			endCapturedWorms.setText("You failed");
			sound.play(TamerSound.LEVEL_END_BAD);
		}
		
		Table table = super.getTable();
		table.setVisible(true);
	}
	
	@Override
    public void render( float delta ){
        bgImage.setColor(bgColor.r,bgColor.g,bgColor.b,bgColor.a);
        if (bgColor.a==1.0f)fadingDone = true;
		stage.act( delta );
        stage.draw();
    }
	
	public boolean getFadingDone(){
		return fadingDone;
	}
	
	public void resetFadingDone(){
		bgColor = new Color(1,1,1,0);
		fadingDone = false;
		
		Table table = super.getTable();
		if (table!=null)table.setVisible(false);
	}
}
