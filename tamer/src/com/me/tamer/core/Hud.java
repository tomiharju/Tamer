package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;

public class Hud extends Group{
	private static Hud instance;
	private TextButton menuButton;
	private TextButtonStyle textButtonStyle;
	private Skin skin;
	private TamerStage stage;
	
	private Label remainingLabel, survivedLabel,fpsLabel;
	public static final int LABEL_REMAINING = 0,LABEL_SURVIVED = 1, LABEL_FPS = 2;
	private int remaining = 0, survived = 0;
	
	private Hud(){
		create();
	}
	
	public static Hud instance(){
		if(instance==null) instance = new Hud();
		return instance;
	}
	
	public void initialize(TamerStage stage){
		this.stage = stage;
	}
	
	public void create(){
		//setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight() / 5);
		//setPosition(0, 600);
		
		//Skin and font
		skin = new Skin();
        Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
 
        skin.add("white", new Texture(pixmap));
 
        BitmapFont bfont=new BitmapFont();
        bfont.scale(Gdx.graphics.getHeight() * 0.001f);
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
		
		//menuButton.setPosition(20, Gdx.graphics.getHeight() - 35);
		//this.addActor(menuButton);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		
		remainingLabel = new Label("Remaining: ",labelStyle);
		//remainingLabel.setPosition(120, Gdx.graphics.getHeight() - 35);
		//this.addActor(remainingLabel);
		
		survivedLabel = new Label("Survived: ",labelStyle);
		//survivedLabel.setPosition(280, Gdx.graphics.getHeight() - 35);
		//this.addActor(survivedLabel);
		fpsLabel = new Label("FPS: ",labelStyle);
		
		FileHandle skinFile = Gdx.files.internal( "skin/uiskin.json" );
        skin = new Skin( skinFile );
        
        Table table = new Table( skin );
        table.setFillParent( true );
        
        this.addActor(table);
        
        table.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 20.0f );
		
		table.add( menuButton ).size( Gdx.graphics.getWidth() / 4, 60 ).uniform().spaceBottom( 10 );
		table.add( remainingLabel ).size( Gdx.graphics.getWidth() / 4, 60 ).uniform().spaceBottom( 10 );
		table.add( survivedLabel ).size( Gdx.graphics.getWidth() / 4, 60 ).uniform().spaceBottom( 10 );
		table.add( fpsLabel ).size( Gdx.graphics.getWidth() / 4, 60 ).uniform().spaceBottom( 10 );
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){		
		batch.setProjectionMatrix(stage.getUiCamera().combined); 
		SnapshotArray<Actor> actors = getChildren();
		for (int i = 0; i < actors.size; i++){
			actors.get(i).draw(batch, parentAlpha);
		}
	}	
	
	public void resetHud(){
		remaining = 0;
		remainingLabel.setText("Remaining: " +remaining);
		
		survived = 0;
		survivedLabel.setText("Survived: " +survived);
	}
	
	public void updateLabel(int type, int amount){
		switch(type){
		case LABEL_REMAINING:{
			remaining += amount;
			remainingLabel.setText("Remaining: " +remaining);
			break;
		}
		case LABEL_SURVIVED:{
			survived += amount;
			survivedLabel.setText("Survived: " +survived);
			break;
		}
		case LABEL_FPS:{
			fpsLabel.setText("FPS: " +amount);
			break;
		}
		}
		
	}
}
