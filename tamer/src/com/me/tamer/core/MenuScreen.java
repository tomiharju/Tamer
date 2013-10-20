package com.me.tamer.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.tamer.services.MusicManager;
import com.me.tamer.services.MusicManager.TamerMusic;

public class MenuScreen extends AbstractScreen{
	TextButton continueButton, newGameButton, levelsButton, optionsButton, quitButton;
	Skin skin;
    SpriteBatch batch;
    Table table;
 
	public MenuScreen( TamerGame game ){
		super (game);
		create();
	}
	
    public void create(){
        batch = new SpriteBatch();
 
        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();
        
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
 
        skin.add("white", new Texture(pixmap));
 
        // Store the default libgdx font under the name "default".
        BitmapFont bfont=new BitmapFont();
        bfont.scale(1);
        skin.add("default",bfont);
 
        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
 
        textButtonStyle.font = skin.getFont("default");
 
        skin.add("default", textButtonStyle);
 
        continueButton = new TextButton("Continue Game",textButtonStyle);
        continueButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("TODO:Continue game");
            }
        });
        
        newGameButton=new TextButton("New Game",textButtonStyle);
        newGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log(TamerGame.LOG, "Setting new PlayScreen");
                game.setScreen( new PlayScreen(game));
            }
        });
        
        levelsButton = new TextButton("Select Level",textButtonStyle);
        levelsButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Gdx.app.log(TamerGame.LOG, "Level select button pressed //TODO");
            	//game.setScreen( new LevelsScreen(game));
            }
        });
        
        optionsButton = new TextButton("Options",textButtonStyle);
        optionsButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.app.log(TamerGame.LOG, "Options button pressed //TODO");
            }
        });
        
        quitButton = new TextButton("Quit Game",textButtonStyle);
        quitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
            	Gdx.app.log(TamerGame.LOG, "Quit game button pressed //TODO");
            }
        });
        
    }

    public void resize (int width, int height) {
        stage.setViewport(width, height, false);
    }
 
    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();
    }
    
	@Override
	public void show()
	{
	    super.show();
	    
	    game.getMusicManager().play( TamerMusic.MENU );
	    // retrieve the default table actor
	    Table table = super.getTable();
	    table.add( "TAMER" ).spaceBottom( 50 );
	    table.row();
	
	    table.add( continueButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( newGameButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( levelsButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( optionsButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();
	    table.add( quitButton ).size( 300, 60 ).uniform().spaceBottom( 10 );
	    table.row();	
	}
	
    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void pause() {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }
}
