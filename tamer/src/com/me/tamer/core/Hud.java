package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;

public class Hud extends Group{
	private OrthographicCamera uiCam;
	private TextButton menuButton;
	private TextButtonStyle textButtonStyle;
	private Skin skin;
	private TamerStage stage;
	
	public Hud(TamerStage stage){
		this.stage = stage;
		create();
	}
	
	public void create(){
		
		//Skin and font
		skin = new Skin();
        Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
 
        skin.add("white", new Texture(pixmap));
 
        BitmapFont bfont=new BitmapFont();
        bfont.scale(1);
        skin.add("default",bfont);
        
		textButtonStyle = new TextButtonStyle();
		
		textButtonStyle.font = skin.getFont("default");
		 
        skin.add("default", textButtonStyle);
		
        //Actors
		menuButton = new TextButton("Menu",textButtonStyle);
		menuButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                stage.getGame().setScreen( stage.getGame().getPauseScreen() );
            }
        });
		menuButton.setPosition(50, Gdx.graphics.getHeight() - 50);
		this.addActor(menuButton);
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){		
		batch.setProjectionMatrix(stage.getUiCamera().combined); 
		SnapshotArray<Actor> actors = getChildren();
		for (int i = 0; i < actors.size; i++){
			actors.get(i).draw(batch, parentAlpha);
		}
	}
	
}
