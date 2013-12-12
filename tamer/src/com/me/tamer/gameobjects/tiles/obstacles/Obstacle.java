package com.me.tamer.gameobjects.tiles.obstacles;

import java.util.ArrayList;

import com.me.tamer.gameobjects.creatures.Creature;

/**
 * @author Tamer
 * Interface which offers a common resolve function for all the gameobjects that need to act
 * when a worm / ant steps on it.
 *
 */
public interface Obstacle {
	public void resolve(ArrayList<Creature> creatures);

}
