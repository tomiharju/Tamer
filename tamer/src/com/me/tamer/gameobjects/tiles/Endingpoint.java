package com.me.tamer.gameobjects.tiles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.tamer.core.Hud;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.creatures.Worm;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.utils.Helper;

public class Endingpoint extends StaticObject implements Obstacle{
	private Hud hud;
	private ArrayList<Creature> survivedWorms = new ArrayList<Creature>();
	private boolean creatureOnList = false;
	
	public void setup(Environment level){
		level.addNewObject(this);
		level.getObstacles().add(this);
		setZindex(1);
		
		hud = Hud.instance();	
	}
	
	public void setPixelsX(String pixels){
		float x = Float.parseFloat(pixels);
		setSize(x,getSize().y);
	}
	public void setPixelsY(String pixels){
		float y = Float.parseFloat(pixels);
		setSize(getSize().x,y);
	}
	
	public void setGraphics(String graphics){
		Renderer render = RenderPool.addRendererToPool("static",graphics);
		render.loadGraphics(graphics);
		setSize(Helper.TILESIZE);
		setRenderType(graphics);
	}

	@Override
	public void resolve(ArrayList<Creature> creatures) {
		int size = creatures.size();
		for(int i = 0 ; i < size ; i ++){

			if(creatures.get(i).isAffected(getPosition(), 1f)){
				creatures.get(i).moveToPoint(getPosition());

				if(creatures.get(i).getClass() == Worm.class){
					creatureOnList = false;
					
					for (int j=0; j < survivedWorms.size(); j++){
						if (survivedWorms.get(j)==creatures.get(i))creatureOnList = true;
					}
					
					if(!creatureOnList){
						survivedWorms.add(creatures.get(i));
						
						Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
								+ " :: updating label survived");
						hud.updateLabel(Hud.LABEL_SURVIVED, 1);
						
						Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
								+ " :: updating label remaining");
						hud.updateLabel(Hud.LABEL_REMAINING, -1);
					}					
				}
			}	
		}	
	}

	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getDebug() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void wakeUp(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}
}
