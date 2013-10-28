package com.me.tamer.gameobjects.tiles.obstacles;

import java.util.ArrayList;

import com.me.tamer.gameobjects.creatures.Creature;

public interface Obstacle {

	public void resolve(ArrayList<Creature> creatures);

}
