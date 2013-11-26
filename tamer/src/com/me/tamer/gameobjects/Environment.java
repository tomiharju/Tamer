package com.me.tamer.gameobjects;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.tamer.core.TamerGame;
import com.me.tamer.core.TamerStage;
import com.me.tamer.gameobjects.creatures.Creature;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.superclasses.StaticObject;
import com.me.tamer.gameobjects.tamer.Tamer;
import com.me.tamer.gameobjects.tiles.TileMap;
import com.me.tamer.gameobjects.tiles.obstacles.Obstacle;
import com.me.tamer.services.SoundManager;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.DrawOrderComparator;
import com.me.tamer.utils.EventPool;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.RuntimeObjectFactory;

public class Environment extends Actor {

	private TamerStage stage;
	private ControlContainer controls;
	private DrawOrderComparator comparator =  new DrawOrderComparator();

	// Settings
	private Vector2 mapBounds = null;
	private Vector2 cameraBounds = null;
	//TEst
	private TileMap tilemap;
	// Gameobject data
	private ArrayList<GameObject> gameobjects = new ArrayList<GameObject>();
	private ArrayList<GameObject> carbages = new ArrayList<GameObject>();
	private ArrayList<GameObject> newobjects = new ArrayList<GameObject>();
	private DynamicObject tamer = null;
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	private ArrayList<StaticObject> staticObjects = new ArrayList<StaticObject>();

	private SpriteCache environmentCache;
	private int cacheID;
	// Optimization variables
	Vector2 tamerShadowPosition = new Vector2();

	// Drawing-order
	private int loopCount = 0;
	private int sortRate = 6;

	// States during GAME_RUNNING
	public enum RunningState{
		BEGIN_ZOOM,NORMAL,TAMER_ENTER,END_FADE;
	}
	private RunningState state;

	// SoundManager
	SoundManager sound;
	
	// Help vectors used in "isVisible function"
	private Vector2 isoPoint = new Vector2();
	private Vector2 focusPoint = new Vector2();
	
	//fence
	private Vector2 fenceUpLeft = new Vector2();
	private Vector2 fenceBottomRight = new Vector2();

	public Environment() {
		RuntimeObjectFactory.createLinkToLevel(this);
		controls = ControlContainer.instance();
		sound = SoundManager.instance();
		stage = TamerStage.instance();
	}

	public void act(float dt) {
		addNewObjects();
		
		runCarbageCollection();
		resolveObstacles(dt);

		int numObjects = gameobjects.size();

		switch (state) {
		case BEGIN_ZOOM:
			break;
		case NORMAL:
			stepTimers(dt);
			for (int k = 0; k < numObjects; k++) {
				gameobjects.get(k).update(dt);
			}
			break;
		case TAMER_ENTER:
			if (tamer != null){
				tamer.update(dt);
				if (((Tamer) tamer).hasEnteredField()) {
					setState(RunningState.NORMAL);
					controls.enableInput();
					// sound.play(TamerSound.OPENING);
					
					//quick fix to the issue where this is not updated before moving joystick
					tamerShadowPosition = ((Tamer) tamer).getShadow().getCenterPosition();
				}
			}		
			break;
		case END_FADE:
			stage.getGame().changeLevelCompleteScreen();
			break;
		default:
			break;
		}
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.setProjectionMatrix(stage.getCamera().combined);
		int numObjects = gameobjects.size();
		sortDrawOrder(numObjects);
		environmentCache.setProjectionMatrix(stage.getCamera().combined);
		environmentCache.begin();
		environmentCache.draw(cacheID);
		environmentCache.end();
		stage.getSpriteBatch().begin();
		for (int k = 0; k < numObjects; k++) {
			Vector2 pos = gameobjects.get(k).getPosition();
			if (isVisible(pos))
				gameobjects.get(k).draw(batch);
		}
	}

	public void debugDraw(ShapeRenderer sr) {

		sr.setProjectionMatrix(stage.getCamera().combined);

		int size = gameobjects.size();
		for (int i = 0; i < size; i++)
			if (gameobjects.get(i).getDebug()) {
				gameobjects.get(i).debugDraw(sr);
			}
	}

	public void sortDrawOrder(int numObjects) {
		if (loopCount % sortRate == 0) {
			if (numObjects > 1) {
				Collections.sort(gameobjects, comparator);
				loopCount = 0;
			}
		}
		loopCount++;
	}

	public boolean isVisible(Vector2 point) {
		float zoom = getStage().getCamera().zoom;
		isoPoint.set(Helper.worldToScreen(point));
		
		//draw everything in the beginning
		if (getState() == RunningState.BEGIN_ZOOM || getState() == RunningState.TAMER_ENTER){
			return true;
		} else{
			focusPoint.set(Helper.worldToScreen(tamerShadowPosition));
		}
		
		return (isoPoint.x > focusPoint.x - Helper.TILESIZE.x * 7 * zoom
				&& isoPoint.x < focusPoint.x + Helper.TILESIZE.x * 7 * zoom
				&& isoPoint.y > focusPoint.y - Helper.TILESIZE.y * 21 * zoom && isoPoint.y < focusPoint.y
				+ Helper.TILESIZE.y * 21 * zoom);

	}

	public void stepTimers(float dt) {
		EventPool.step(dt);
	}

	/**
	 * @param dt
	 *            "Resolves" each gamemeobject which impelent Obstalce interface
	 *            Resolve is currently used to detect and respond to collisions,
	 *            and quicksand uses it for worm pulling.
	 * 
	 */
	public void resolveObstacles(float dt) {
		int size = obstacles.size();
		for (int i = 0; i < size; i++) {
			obstacles.get(i).resolve(creatures);
		}

	}

	public void runCarbageCollection() {
		int size = gameobjects.size();
		for (int i = 0; i < size; i++)
			if (gameobjects.get(i).isCarbage()) {
				gameobjects.get(i).dispose(this);
				carbages.add(gameobjects.get(i));
			}

		if (carbages.size() > 0) {
			gameobjects.removeAll(carbages);
			carbages.clear();
			Gdx.app.debug(TamerGame.LOG,
					this.getClass().getSimpleName()
							+ " :: Gameobjects after carbage collection "
							+ gameobjects.size());
		}
	}

	public void addNewObjects() {
		if (newobjects.size() > 0) {
			for (GameObject go : newobjects) {
				go.wakeUp(this);
				gameobjects.add(go);
			}
			newobjects.clear();
		}
	}

	public void addNewObject(GameObject obj) {
		newobjects.add(obj);
	}

	/**
	 * Looks through all added gameobjects, and adds each obstacle to separate
	 * obstacle list We use this obstacle list to apply effects on worms ( for
	 * example we check if worms is inside quicksand tile, and then apply force
	 * to it)
	 */
	public void setupGame() {
		
		generateSpriteCache();
		
//		setState(RunningState.BEGIN_ZOOM);
		//Camera mode will change Running_state when it's done
		stage.setCameraHolder(TamerStage.BEGIN_CAMERA);
		
	}
	/**
	 * @param tilemap
	 * This method is called from TileMap at setup()
	 * It creates a link between TileMap and environment.
	 * This link is used to call tilemap.drawTileMap which draws the cached terrain
	 */
	public void setTileMapObject(TileMap tilemap){
		this.tilemap = tilemap;
	}
	
	public void generateSpriteCache(){
		environmentCache = new SpriteCache(staticObjects.size() + tilemap.getNumTiles(),false);
		environmentCache.beginCache();
		tilemap.generate(environmentCache);
		Collections.sort(staticObjects, comparator);
		TamerStage stage = TamerStage.instance();

		AssetManager assetManager = stage.getGame().getAssetManager();
	
		Vector2 help = new Vector2();
		
		for (int i = 0; i < staticObjects.size(); i++) {
			TextureRegion texture = assetManager.get("data/graphics/sheetData",
					TextureAtlas.class).findRegion(staticObjects.get(i).getRenderType());
			help.set(Helper.worldToScreen(staticObjects.get(i).getPosition()));
			environmentCache.add(texture, help.x,
					help.y, staticObjects.get(i).getSize().x,staticObjects.get(i).getSize().y);
			
		}
		
		cacheID = environmentCache.endCache();
		environmentCache.setProjectionMatrix(stage.getCamera().combined);
		System.out.println("CAche created with "+staticObjects.size() + " objects");
	}

	/**
	 * @param obj
	 *            Levelcreator calls this function to add new objects
	 */
	public void addObject(GameObject obj) {
		gameobjects.add(obj);
	}

	public void addObstacle(Obstacle obstacle) {
		this.obstacles.add(obstacle);
	}
	public void addStaticObject(StaticObject obj){
		staticObjects.add(obj);
	}

	public void setMapBounds(String value) {
		String[] values = value.split(":");
		// Bounds come as total width and total height from editor, so to make
		// it "lenght from origin" we divide by 2
		mapBounds = new Vector2(Float.parseFloat(values[0]) / 2,
				Float.parseFloat(values[1]) / 2);
		mapBounds.set(mapBounds.x * Helper.TILESIZE.x, mapBounds.y
				* Helper.TILESIZE.x);
	}

	public void setMapSize(String size) {

	}

	public void setTamer(Tamer tamer) {
		this.tamer = tamer;
		tamerShadowPosition = tamer.getShadow().getCenterPosition();
	}
	
	public void setStage(TamerStage stage) {
		this.stage = stage;
	}

	public void dispose() {
		for (GameObject go : gameobjects) {
			go.dispose(this);
		}
		gameobjects.clear();
		carbages.clear();
		newobjects.clear();
		tamer = null;
		obstacles.clear();
	}

	public Vector2 getMapBounds() {
		return mapBounds;
	}

	public Vector2 getCamBounds() {
		return cameraBounds;
	}

	public ArrayList<Creature> getCreatures() {
		return creatures;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public Vector2 getTamerShadowPosition() {
		return tamerShadowPosition;
	}

	public Tamer getTamer() {
		return (Tamer) tamer;
	}

	public TamerStage getStage() {
		return stage;
	}

	public void setState(RunningState s) {
		Gdx.app.log(TamerGame.LOG, this.getClass().getSimpleName()
				+ " :: setting RunningState to " + s);
		state = s;
	}

	public RunningState getState() {
		return state;
	}

}