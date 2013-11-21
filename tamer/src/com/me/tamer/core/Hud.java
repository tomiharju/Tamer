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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.SnapshotArray;

public class Hud extends Group {
	public static final int SIZE = Gdx.graphics.getHeight() / 5;

	private static Hud instance;
	private TextButton menuButton;
	private TextButtonStyle textButtonStyle;
	private Skin skin;
	private TamerStage stage;

	private Label remainingLabel, survivedLabel, fpsLabel;
	public static final int LABEL_REMAINING = 0, LABEL_SURVIVED = 1,
			LABEL_FPS = 2;
	private int remaining = 0, survived = 0;
	private Image bgImage;

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
		float h = Gdx.graphics.getHeight() / 12;
		float w = Gdx.graphics.getWidth();
		float x = 0;
		float y = Gdx.graphics.getHeight() - h;
		setBounds(x, y, w, h);

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
		menuButton = new TextButton("Menu", textButtonStyle);
		menuButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				stage.getGame().setScreen(stage.getGame().getPauseScreen());
			}
		});


		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");

		remainingLabel = new Label("Remaining: ", labelStyle);
		survivedLabel = new Label("Survived: ", labelStyle);
		fpsLabel = new Label("FPS: ", labelStyle);

		FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
		skin = new Skin(skinFile);

		Table table = new Table(skin);
		table.setFillParent(true);

		table.setPosition(x, y);
		table.add(menuButton).uniform().spaceBottom(10);
		table.add(remainingLabel).uniform().spaceBottom(10);
		table.add(survivedLabel).uniform().spaceBottom(10);
		table.add(fpsLabel).uniform().spaceBottom(10);

		// Register actors
		this.addActor(bgImage);
		this.addActor(table);
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(stage.getUiCamera().combined);
		SnapshotArray<Actor> actors = getChildren();
		for (int i = 0; i < actors.size; i++) {
			actors.get(i).draw(batch, parentAlpha);
		}
	}

	public void resetHud() {
		remaining = 0;
		remainingLabel.setText("R: " + remaining);

		survived = 0;
		survivedLabel.setText("S: " + survived);
	}

	public void updateLabel(int type, int amount) {
		switch (type) {
		case LABEL_REMAINING: {
			remaining += amount;
			remainingLabel.setText("R: " + remaining);
			break;
		}
		case LABEL_SURVIVED: {
			survived += amount;
			survivedLabel.setText("S: " + survived);
			break;
		}
		case LABEL_FPS: {
			fpsLabel.setText("FPS: " + amount);
			break;
		}
		}
	}
}
