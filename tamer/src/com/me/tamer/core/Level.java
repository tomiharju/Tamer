package com.me.tamer.core;

import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.Environment.RunningState;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.utils.EnvironmentCreator;

public class Level {

	private TamerStage stage;
	private final int id;
	private String name;
	private boolean completed;
	private Level nextLevel;

	Environment environment = null;
	Hud hud = null;

	int worms = 0, normal = 0, dead = 0, fence = 0;

	public enum WormState {
		DEFAULT, DEAD, FENCE;
	}

	public Level(int id) {
		this.id = id;
		hud = Hud.instance();
		hud.resetHud();
	}

	public void setStage(TamerStage stage) {
		this.stage = stage;
	}

	/**
	 * @param current_env
	 *            Create new env based on data read from level configuration
	 *            file. Give this as parameter, so that EnvironmentFactory can
	 *            properly add objects to gameobjects
	 */
	public void createEnvironment() {
		environment = EnvironmentCreator.create(id);
		environment.setStage(stage);
	}

	public Environment getEnvironment() {
		return environment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		environment.setState(RunningState.END_FADE);
		this.completed = completed;
	}

	public Level getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(Level nextLevel) {
		this.nextLevel = nextLevel;
	}

	public int getId() {
		return id;
	}

	public void addWorm(Worm w) {
		worms++;
		updateHud();
	}

	public void setWormState(Worm w, WormState state) {
		switch (state) {
		case DEFAULT:
			worms++;
			fence--;
			break;
		case DEAD:
			worms--;
			dead++;
			break;
		case FENCE:
			worms--;
			fence++;
			break;
		default:
			break;
		}
		
		System.out.println("worms: " +worms +"dead:" +dead);
		
		updateHud();
		if (worms<=0) setCompleted(true);
	}

	public void updateHud() {
		hud.updateLabel(WormState.DEFAULT, worms);
		hud.updateLabel(WormState.DEAD, dead);
		hud.updateLabel(WormState.FENCE, fence);
	}
}
