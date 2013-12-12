package com.me.tamer.gameobjects.tamer;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.services.SoundManager;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerStatic;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.tEvent;

public class GryphonScream extends DynamicObject {
	
	// Higher level entities
	private Tamer tamer = null;
	private SoundManager sound = null;
	private ControlContainer controls;

	// Finals
	private final float SCREAM_AREA_SIZE = 8.0f;
	private final float COOL_DOWN = 1.5f;
	private final float SCREAM_SPEED = 40f;
	private final int WAVE_AMOUNT = 30;
	private final float WAVE_FREQUENCY = 0.02f;

	// Booleans
	private boolean animationActive = false;
	private boolean onCoolDown = false;

	// Help
	private Vector2 wormPos = new Vector2();
	private Vector2 help = new Vector2();
	private Vector2 help2 = new Vector2(1, -1);
	private Vector2 newHeading = new Vector2();
	private Vector3 screamDirection = new Vector3(1, -1, 0);
	private ArrayList<Vector3> soundWaves = new ArrayList<Vector3>();

	public GryphonScream(Tamer tamer) {
		this.tamer = tamer;
		setZindex(-1);
		setGraphics(TamerStatic.SCREAM.getFileName());
		sound = SoundManager.instance();
		controls = ControlContainer.instance();
	}

	public void setGraphics(String graphics) {
		Renderer renderer = RenderPool.addRendererToPool("static", graphics);
		renderer.loadGraphics(graphics);
		renderer.setColor(1, 1f, 1f, 0.2f);
		setSize(getSize());
		setRenderType(graphics);
	}

	@Override
	public void update(float dt) {
		if (animationActive) {
			for (int i = 0; i < soundWaves.size(); i++) {
				soundWaves.get(i).add(
						screamDirection.tmp().mul(SCREAM_SPEED * dt));
				help.set(soundWaves.get(i).x, soundWaves.get(i).y);

				if (Helper.worldToScreen(help).y < Helper.worldToScreen(help2).y
						- SCREAM_AREA_SIZE / 4) {
					soundWaves.remove(i);
				}
				if (soundWaves.size() == 0) {
					animationActive = false;
					controls.getJoystick().enableMovement();
				}
			}
		}
	}

	public void draw(SpriteBatch batch) {
		Renderer renderer = RenderPool.getRenderer(getRenderType());
		for (int i = 0; i < soundWaves.size(); i++) {
			if (soundWaves.get(i).z == 0) {
				renderer.setSize(soundWaves.get(i).x, soundWaves.get(i).x / 2);
				renderer.setPosition(Helper.worldToScreen(tamer.getPosition().tmp()
						.add(soundWaves.get(i).x, soundWaves.get(i).y)));
				renderer.draw(batch);
			}
		}
		// draw direction wave
		if (soundWaves.size() > 0) {
			for (int i = 0; i < soundWaves.size(); i++) {
				renderer.setSize(soundWaves.get(i).x, soundWaves.get(i).x / 2);
				help.set(Helper.worldToScreen(tamer.getShadowPosition()));
				help.y += tamer.getShadow().getSize().y / 2;
				help.y = help.y - soundWaves.get(i).x / 4;
				renderer.setPosition(help);
				renderer.draw(batch);
			}
		}
		batch.setColor(Color.WHITE);
	}

	public void activate() {
		if (onCoolDown)
			return;

		for (int i = 0; i < soundWaves.size(); i++) {
			soundWaves.get(i).set(0, 0, 0);
		}

		animationActive = true;
		tamer.getShadowPosition().set(tamer.getShadow().getPosition());
		help2.set(tamer.getShadowPosition().tmp().sub(tamer.getPosition()));

		ArrayList<Creature> creatures = tamer.getEnvironment().getCreatures();
		for (int i = 0; i < creatures.size(); i++) {
			if (creatures.get(i).getClass() == Worm.class) {
				Worm worm = ((Worm) creatures.get(i));
				wormPos.set(worm.getHead().getPosition());
				if (wormPos.dst(tamer.getShadowPosition()) < SCREAM_AREA_SIZE) {
					newHeading.set(wormPos.x - tamer.getShadowPosition().x, wormPos.y
							- tamer.getShadowPosition().y);
					newHeading.nor();
					worm.setHeading(newHeading);
					worm.doScreamEffect();
					EventPool.addEvent(new tEvent(worm, "doScreamEffect", 0.5f,
							3));
				}
			}
		}
		onCoolDown = true;
		EventPool.addEvent(new tEvent(this, "enable", COOL_DOWN, 1));

		// update button
		controls.setScreamCooldown(true);

		// start animation
		EventPool.addEvent(new tEvent(this, "addWave", WAVE_FREQUENCY,
				WAVE_AMOUNT));

		controls.getJoystick().disableMovement();
		
		sound.setVolume(0.7f);
		sound.play(TamerSound.HAWK);
	}

	public void enable() {
		onCoolDown = false;
		controls.setScreamCooldown(false);
	}

	public void addWave() {
		Vector3 newWave = new Vector3();
		newWave.set(screamDirection.tmp());
		soundWaves.add(newWave);
	}
}
