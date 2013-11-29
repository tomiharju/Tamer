package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.tamer.core.TamerGame;

public class LevelCompleteScreen extends AbstractMenu{
	
	private Image bgImage;
	private boolean fadingDone;
	
	public LevelCompleteScreen(TamerGame game) {
		super(game);
		bgColor = new Color(1,1,1,0);
	}
	
	@Override
	public void create(){
		super.create();
		bgImage = new Image((new Texture(Gdx.files.internal("data/graphics/levelcomplete_bg.png"))));
		bgImage.setFillParent(true);

		stage.addActor(bgImage);

			
	}
	
	public void createButtons(){
		 // retrieve the default table actor
		Table table = super.getTable();
	    table.add( "TAMER" ).spaceBottom( 50 );
	    table.row();
	
	    table.add( nextLevelButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();	
	    table.add( exitButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
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
}
