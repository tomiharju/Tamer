package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;

public class AbstractScreen implements Screen{

    protected final TamerGame game;
    protected Stage stage;
    protected TamerStage tamerStage;
    private BitmapFont font;

    private Skin skin;
    private Table table;
    protected Color bgColor = new Color(Color.BLACK);

    public AbstractScreen(final TamerGame game)
    {
        this.game = game;
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }

    // Lazily loaded collaborators
    public BitmapFont getFont()
    {
        if( font == null ) {
            font = new BitmapFont();
        }
        return font;
    }

    
    protected Skin getSkin()
    {
        if( skin == null ) {
            FileHandle skinFile = Gdx.files.internal( "skin/uiskin.json" );
            skin = new Skin( skinFile );
        }
        return skin;
    }

    protected Table getTable()
    {
        if( table == null ) {
            table = new Table( getSkin() );
            table.setFillParent( true );
    
            stage.addActor( table );
        }
        return table;
    }
    
	public void setColor(float r, float g, float b, float a){
		bgColor.set(r,g,b,a);
	}
	
	public Color getColor(){
		return bgColor;
	}

    //Screen implementation

    @Override
    public void show(){
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Showing screen: " + getName() );    
    }

    @Override
    public void resize(int width,int height ){
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Resizing screen: " + getName() + " to: " + width + " x " + height );
    }

    @Override
    public void render( float delta ){
		stage.act( delta );
		stage.draw();
    }

    @Override
    public void hide()
    {
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Hiding screen: " + getName() );
      
    }

    @Override
    public void pause(){
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Pausing screen: " + getName() );
    }

    @Override
    public void resume(){
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Resuming screen: " + getName() );
    }

    @Override
    public void dispose()
    {
        Gdx.app.log( TamerGame.LOG, this.getClass().getSimpleName() + " :: Disposing screen: " + getName() );

        // the following call disposes the screen's stage, but on my computer it
        // crashes the game so I commented it out; more info can be found at:
        // http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3624
        //stage.dispose();

        // as the collaborators are lazily loaded, they may be null
        if( font != null ) font.dispose();
        if( skin != null ) skin.dispose();
    }
}
