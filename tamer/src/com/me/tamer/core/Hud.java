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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;
import com.me.tamer.core.Level.WormState;
import com.me.tamer.core.TamerGame.ScreenType;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.tEvent;

public class Hud extends Group {
	public static final int SIZE = Gdx.graphics.getHeight() / 5;

	private static Hud instance;
	private TextButton menuButton;
	private TextButtonStyle textButtonStyle;
	private Skin skin;
	private TamerStage stage;
	private Image bgImage;
	private Image lostImage;

	private Label beastsLabel, capturedLabel, lostLabel, fpsLabel;
	
	public static final int LABEL_REMAINING = 0, LABEL_SURVIVED = 1,
			LABEL_DEAD = 4, LABEL_FPS = 2;
	
	//Help text hud
	private Label noEscapeLabel, helpLabel;
	private Image fenceArrow;
	
	private Hud() {
		create();
	}

	public static Hud instance() {
		if (instance == null)
			instance = new Hud();
		return instance;
	}

	public void initialize(TamerStage stage) {
		this.stage = stage;
	}

	public void create() {
		float h = Gdx.graphics.getHeight() / 10;
		float w = Gdx.graphics.getWidth();
		float x = 0;
		float y = Gdx.graphics.getHeight() - h;
		
		//For some reason position can not be set or actors don't work as supposed to
		setSize(w,h);
		
		bgImage = new Image(new Texture(
				Gdx.files.internal("data/graphics/levelcomplete_bg.png")));
		bgImage.setFillParent(true);
		bgImage.setPosition(x, y);

		// Skin and font
		skin = new Skin();
		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		BitmapFont bfont = new BitmapFont();
		bfont.scale(Gdx.graphics.getHeight() * 0.001f);
		skin.add("default", bfont);

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = skin.getFont("default");

		skin.add("default", textButtonStyle);

		// Actors
		menuButton = new TextButton("I I", textButtonStyle);
		menuButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				stage.getGame().setScreen(ScreenType.PAUSE);
			}
		});

		LabelStyle labelStyle_white = new LabelStyle();
		labelStyle_white.font = skin.getFont("default");
		
		//Red color label
		LabelStyle labelStyle_red = new LabelStyle();
		labelStyle_red.font = skin.getFont("default");
		labelStyle_red.fontColor = new Color(Color.RED);

		beastsLabel = new Label("", labelStyle_white);
		lostLabel = new Label("", labelStyle_white);
		capturedLabel = new Label("", labelStyle_white);
		fpsLabel = new Label("FPS: ", labelStyle_white);
		

		FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
		skin = new Skin(skinFile);
		
		
		//Hud icons
		lostImage = new Image(new Texture(
				Gdx.files.internal("data/graphics/x.png")));
		
		SplitPaneStyle sty = new SplitPaneStyle();
		Table table = new Table(skin);
		table.setFillParent(true);

		table.add(menuButton).uniform();
		table.add(beastsLabel).uniform();
		table.add(capturedLabel).uniform();
		table.add(lostLabel).uniform();
		table.add(fpsLabel).uniform();
		
		table.setBounds(x, y,w, h);
		
		//BEGIN ADVICE
		noEscapeLabel = new Label("DO NOT LET THE BEASTS ESCAPE!", labelStyle_red);
		noEscapeLabel.setPosition( ( Gdx.graphics.getWidth() - noEscapeLabel.getWidth() ) / 2, Gdx.graphics.getHeight() / 2);
		noEscapeLabel.setVisible(false);
		
		helpLabel = new Label("", labelStyle_white);
		helpLabel.setVisible(false);
//		helpLabel.setPosition( ( Gdx.graphics.getWidth() - noEscapeLabel.getWidth() ) / 2, Gdx.graphics.getHeight() / 2);
		
		fenceArrow = new Image(new Texture(
				Gdx.files.internal("data/graphics/arrow.png")));
		fenceArrow.setVisible(false);

		// Register actors	
		this.addActor(bgImage);
		this.addActor(table);
		this.addActor(helpLabel);
		this.addActor(noEscapeLabel);
		this.addActor(fenceArrow);
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(stage.getUiCamera().combined);
		SnapshotArray<Actor> actors = getChildren();
		for (int i = 0; i < actors.size; i++) {
			if (actors.get(i).isVisible()) actors.get(i).draw(batch, parentAlpha);
		}
	}
	
	 public void updateLabel(int type, int amount) {
		 fpsLabel.setText("FPS: " +amount);
	 }
	
	public void updateLabel(WormState state, int amount){
		switch(state){
		case DEFAULT: {
			beastsLabel.setText("Beasts: " + amount);
			break;
		}
		case FENCE: {
			capturedLabel.setText("Captured: " + amount);
			break;
		}	
		case DEAD: {
			lostLabel.setText("Lost: " + amount);
			break;
		}
		}
	}
	//Help -label implementation
	public void showHelp(boolean show){
		helpLabel.setVisible(show);
		if(show){
			helpLabel.setText( stage.getLevel().getHelpText() );
			helpLabel.setPosition( ( Gdx.graphics.getWidth() - helpLabel.getPrefWidth() ) / 2, Gdx.graphics.getHeight() / 2);
		}
		
	}
	
	//No escape -label implementation
	public void startNoEscape(){
		noEscapeLabel.setVisible(true);
		EventPool.addEvent(new tEvent(this, "blinkNoEscape", 1f, 3));
	}
	
	public void blinkNoEscape(){
		if(!noEscapeLabel.isVisible()) noEscapeLabel.setVisible(true);
		else noEscapeLabel.setVisible(false);
	}
	
	public void showNoEscape(boolean show){
		noEscapeLabel.setVisible(show);
		if(show){
			noEscapeLabel.setPosition( ( Gdx.graphics.getWidth() - noEscapeLabel.getPrefWidth() ) / 2, Gdx.graphics.getHeight() / 2);
		}
	}
}
