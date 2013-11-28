package com.me.tamer.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.me.tamer.gameobjects.renderers.UiRenderer;
import com.me.tamer.services.TextureManager.TamerTexture;

public class SpearButton extends Actor {

	ControlContainer controlContainer = null;
	
	private Vector2 input = new Vector2();
	private Vector2 localCenter = new Vector2();
	
	final Vector2 restingpoint = new Vector2(Gdx.graphics.getWidth() - 165,100);
	final float BUTTON_SIZE = 180;

	boolean buttonPressed = false;
	boolean inputDisabled = false;
	UiRenderer renderer = new UiRenderer();
	UiRenderer renderer2 = new UiRenderer();
	UiRenderer renderer3 = new UiRenderer();
	UiRenderer currentRenderer;
	
	Label amountLabel;
	int spearsAvailable = 0;
	
	//Status variables
	private boolean onCooldown = false;
	private boolean spearOnRange = false;
	
	public SpearButton(ControlContainer controlContainer) {
		this.controlContainer = controlContainer;
		
		//on cooldown
		renderer.loadGraphics(TamerTexture.BUTTON_SPEAR);
		renderer.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer.setPosition(restingpoint);
		
		//ready to throw
		renderer2.loadGraphics(TamerTexture.BUTTON_SPEAR_GLOW);
		renderer2.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer2.setPosition(restingpoint);
		
		//pick up spear
		renderer3.loadGraphics(TamerTexture.BUTTON_SPEAR_GLOW_BLUE);
		renderer3.setSize(BUTTON_SIZE,BUTTON_SIZE);
		renderer3.setPosition(restingpoint);
		
		currentRenderer = renderer;
		
		//Amount label
		Skin skin = new Skin();
		Pixmap pixmap = new Pixmap(100, 100, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		
		BitmapFont bfont = new BitmapFont();
		bfont.scale(Gdx.graphics.getHeight() * 0.001f);
		skin.add("default", bfont);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		amountLabel = new Label("1", labelStyle);
		amountLabel.setPosition(restingpoint.x + BUTTON_SIZE / 10, restingpoint.y - BUTTON_SIZE / 4);
		
		//Positions
		localCenter.set(BUTTON_SIZE / 2, BUTTON_SIZE / 2);
		setPosition(restingpoint.x - BUTTON_SIZE/2, restingpoint.y - BUTTON_SIZE/2);
		setSize(BUTTON_SIZE, BUTTON_SIZE);
		
		createListener();
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		if (spearOnRange)currentRenderer = renderer3;
		else if (onCooldown)currentRenderer = renderer;
		else currentRenderer = renderer2;
		
		currentRenderer.draw(batch);
		
		//Draw number on top of the icon
		amountLabel.draw(batch, parentAlpha);
	}
	
	public void update(float dt){
		
	}

	public void createListener(){
		addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				input.set(x,y);
				if(input.dst(localCenter) < BUTTON_SIZE / 2 ){
					if(spearOnRange)controlContainer.getEnvironment().getTamer().pickUpSpear();
					else controlContainer.getEnvironment().getTamer().throwSpear();
					return true;
				}
                return false;
	        }
			 
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
			}
			
			public void touchDragged(InputEvent event, float x, float y, int pointer){
				input.set(x,y);
				if(input.dst(localCenter) > BUTTON_SIZE / 2 ){
					buttonPressed = false;
				}
			}
		});	
	}

	public void addSpearsAvailable(int s){
		spearsAvailable += s;
		amountLabel.setText(""+spearsAvailable);
	}
	
	public void setOnCooldown(boolean b){
		onCooldown = b;
	}
	
	public boolean isOnCooldown(){
		return onCooldown;
	}
	
	public boolean isSpearOnRange() {
		return spearOnRange;
	}

	public void setSpearOnRange(boolean spearOnRange) {
		this.spearOnRange = spearOnRange;
	}
}
