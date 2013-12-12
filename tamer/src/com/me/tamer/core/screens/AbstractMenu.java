package com.me.tamer.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerGame.ScreenType;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;

public class AbstractMenu extends AbstractScreen {
	TextButton continueButton, newGameButton, levelsButton, optionsButton,
			mainMenuButton, exitButton, nextLevelButton, playAgainButton;
	
	Label endCapturedWorms;
	Skin skin;
	Table table;
	TextButtonStyle textButtonStyle;
	
	SoundManager sound;

	public AbstractMenu(TamerGame game) {
		super(game);
		sound = SoundManager.instance();

	}

	public void create() {
		// create stage
		stage = new Stage();

		// A skin can be loaded via JSON or defined programmatically, either is
		// fine. Using a skin is optional but strongly
		// recommended solely for the convenience of getting a texture, region,
		// etc as a drawable, tinted drawable, etc.
		skin = new Skin();

		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();

		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		BitmapFont bfont = new BitmapFont();
		bfont.scale(1);
		skin.add("default", bfont);

		// Configure a TextButtonStyle and name it "default". Skin resources are
		// stored by type, so this doesn't overwrite the font.
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		// textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		
		
		//Buttons
		newGameButton = new TextButton("New Game", textButtonStyle);
		newGameButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				sound.play(TamerSound.MENU_CLICK);
				game.setScreen( ScreenType.NEW_PLAY );
			}
		});
		
		nextLevelButton = new TextButton("Next Level", textButtonStyle);
		nextLevelButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				sound.play(TamerSound.MENU_CLICK);
				int levelNumber = game.getLevelManager().getCurrentLevel().getId();
				levelNumber++;
				game.getLevelManager().setCurrentLevel( levelNumber );
				game.setScreen( ScreenType.NEW_PLAY );
			}
		});
		
		playAgainButton = new TextButton("Play again", textButtonStyle);
		playAgainButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.getLevelCompleteScreen().hide();
				sound.play(TamerSound.MENU_CLICK);
				game.setScreen( ScreenType.NEW_PLAY );
			}
		});

		levelsButton = new TextButton("Select Level", textButtonStyle);
		levelsButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				sound.play(TamerSound.MENU_CLICK);
				game.setScreen(ScreenType.LEVELS);
				
			}
		});

		optionsButton = new TextButton("Options", textButtonStyle);
		optionsButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.debug(TamerGame.LOG, "Options button pressed");
				sound.play(TamerSound.MENU_CLICK);
			}
		});

		mainMenuButton = new TextButton("Main Menu", textButtonStyle);
		mainMenuButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				sound.play(TamerSound.MENU_CLICK);
				game.setScreen(ScreenType.MENU);
			}
		});
		
		exitButton = new TextButton("Exit Game", textButtonStyle);
		exitButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				sound.play(TamerSound.MENU_CLICK);
				Gdx.app.exit();
			}
		});
		
		//Labels
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		
		endCapturedWorms = new Label("", labelStyle);
		
	}

	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void show() {
		super.show();
		Gdx.input.setInputProcessor(stage);
	}
}
