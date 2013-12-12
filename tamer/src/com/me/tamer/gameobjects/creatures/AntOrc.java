package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.TamerGame;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.renderers.AnimatedRenderer;
import com.me.tamer.gameobjects.renderers.RenderPool;
import com.me.tamer.gameobjects.renderers.Renderer;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.services.SoundManager.TamerSound;
import com.me.tamer.services.TextureManager.TamerAnimations;
import com.me.tamer.utils.Helper;
import com.me.tamer.utils.tEvent;


public class AntOrc extends DynamicObject implements Creature{
	
	//Higher level entities
	private Environment environment;
	
	//finals
	private final float DECAY_SPEED = 0.4f; // move to higher level
	private final float WORM_SCAN_RADIUS = 10.0f; //ScanArea is a circle
	private final float WAYPOINT_SCAN_RADIUS = 0.5f;
	private final float ATTACK_DISTANCE = 0.3f;
	private final float SPEED_INCREASE = 2f; //A number on which the velocity is multiplied with
	private final float SPEED = 3.0f;
	private final float EATING_TIME = 3.0f;
	private final Vector2 EATING_OFFSET = new Vector2 (1.0f,-1.0f);
	private final float SIZE = 2.4f;
	
	//booleans
	private boolean attached = false;
	private boolean destinationReached = false;
	private boolean markedDead = false;
	private boolean returning = false;
	private boolean decaying = false;
	private boolean onSpearRange = false;
	
	//eating
	private int loopCount = 0;
	private float shakeRate = 5;
	private float shakeSpeed = SPEED / 3;
	private tEvent eatingTimer;
	private Worm targetWorm = null;
	private WormPart targetPart = null;
	private ArrayList<Creature> creatures;
	
	//Waypoints
	private ArrayList<Vector2> waypoints;
	private int nextWaypoint = 1;
	private float levelOfDecay = 1;
	
	//help
	private Vector2 help = new Vector2();
	
	public AntOrc(){
		waypoints = new ArrayList<Vector2>();
		waypoints.add(new Vector2(0,0));//place holder for the first value
	}
	
	public void update(float dt){
		solveIfSpearRange();
		if (!decaying){
			if(!returning && targetPart==null)scanWorms();
			if (attached){	
				if(loopCount % shakeRate == 0){
					//shake that booty
					setHeading(-1,1);
					shakeSpeed = shakeSpeed * -1;
				}loopCount++;
				
				setVelocity(getHeading().tmp().mul(shakeSpeed));
				getPosition().add(getVelocity().tmp().mul(dt));
				if(!eatingTimer.isFinished())eatingTimer.step(dt);
			}else if( checkTargetState() ) followTarget();
			else followPath();
			
			if (!attached){
				setVelocity(getHeading().tmp().mul(SPEED));
				getPosition().add(getVelocity().tmp().mul(dt));	
			}	
		}else{
			levelOfDecay -= DECAY_SPEED * Gdx.graphics.getDeltaTime();
			if(levelOfDecay < 0){
				kill();
			}
		}
	}
	
	public void draw(SpriteBatch batch){
		if (onSpearRange) batch.setColor(1, 0.6f, 0.6f, 1.0f);
		if (decaying) batch.setColor(1, 1, 1, levelOfDecay);
		
		AnimatedRenderer renderer = (AnimatedRenderer)RenderPool.getRenderer(getRenderType());
		renderer.setSize(getSize());
		renderer.setPosition(Helper.worldToScreen(getPosition()));
		renderer.setOrientation(solveOrientation());
		renderer.draw(batch);

		// reset to default color
		batch.setColor(Color.WHITE);
	}
	
	//---------------------------------
	//initialization
	//---------------------------------
	
	public void wakeUp(Environment environment){
		this.environment = environment;

		setGraphics(TamerAnimations.ANT);
		markAsActive();
		
		//Add the spawning position as a first waypoint
		help.set(getPosition());
		waypoints.set(0, help);
		
		environment.getCreatures().add(this);	
	}
	
	public void setGraphics(TamerAnimations graphics) {
		Renderer render = RenderPool.addRendererToPool("animated", graphics.getFileName());
		render.loadGraphics(graphics, 1, 8);
		setSize(new Vector2(SIZE,SIZE));
		setRenderType(graphics.getFileName());
	}
	
	public void setWaypoint(String s){
		String[] values = s.split(":");
		int x = Integer.parseInt(values[0]);
		int y = Integer.parseInt(values[1]);
		Vector2 waypoint = new Vector2(x,y);
		waypoints.add(waypoint);
	}
	
	//---------------------------------
	//Ant 
	//---------------------------------
	
	public void followTarget(){
		//Check if target is close enough to be attacked and update heading to target
		if ( getPosition().dst( targetPart.getPosition() ) < ATTACK_DISTANCE ){
			//attach and start timer for detaching
			targetWorm.bind();
			targetWorm.setAttacked(true);
			targetWorm.setBeingEaten(true);
			attached = true;
			eatingTimer = new tEvent(this, "detach", EATING_TIME, 1);
			
			//set eating position
			setPosition( targetPart.getPosition().tmp().add(EATING_OFFSET));
			
			//eating ant has higher z-index
			setZindex(-1);
		}
		setHeading( targetPart.getPosition().tmp().sub( getPosition()).nor() );
	}
	
	public void followPath(){
		if (!markedDead){
			setHeading( waypoints.get(nextWaypoint).tmp().sub(getPosition()).nor() );
			if (nextWaypoint == waypoints.size() - 1) destinationReached = true;
			
			//first check if waypoint is reached then set heading to next waypoint
			if ( getPosition().dst( waypoints.get(nextWaypoint)) < WAYPOINT_SCAN_RADIUS ){	
				if (!destinationReached){
					nextWaypoint++;
				}else nextWaypoint--;	
			}	
			if (nextWaypoint < 0 ){
				markedDead = true;
				kill();
			}	
		}
	}
	
	public void scanWorms(){
		//lock to wormpart that is scanned first
		creatures = environment.getCreatures();
		for (int i = 0; i < creatures.size(); i++){			
			if ( creatures.get(i).getType() == Creature.TYPE_WORM ){
				Worm worm = (Worm) creatures.get(i);
				ArrayList<WormPart> wormParts = worm.getParts();
				for (int j = 0; j < wormParts.size(); j++){
					WormPart part = wormParts.get(j);
					
					//Check that worm is inside scan radius && not being eaten && not deacaying && not being attacked && not drowning
					if ( part.getPosition().dst( getPosition() ) < WORM_SCAN_RADIUS && !worm.isBeingEaten() && !part.isDecaying() && !part.isAttacked() && !worm.isSubmerged()){				
						//flag that worm is being attacked so it won't be attacked by other ants
						targetWorm = worm;
						targetWorm.setAttacked(true);
						
						//always lock to center of the worm 
						lockToTarget(wormParts.get( wormParts.size() / 2));
					}
				}		
			}
		}
	}
	
	public void lockToTarget(WormPart wp){
		//Increase speed and set target
		targetPart = wp;
		getVelocity().mul(SPEED_INCREASE) ;
	}

	public void detach(){
		Gdx.app.debug(TamerGame.LOG, this.getClass().getSimpleName() + " :: Ant detached");
		attached = false;
		
		targetWorm.decay();
		targetPart=null;
		
		//returning means that ant won't take new targets
		returning = true;
		
		//return to home
		destinationReached = true;
		nextWaypoint = 0;
		
		//reset z-index after eating
		setZindex(0);
	}

	public boolean checkTargetState(){
		if (targetPart == null) return false;
		//remove target if it is drowning
		 if (targetWorm.isSubmerged()){
			targetWorm = null;
			targetPart = null;
			return false;
		}
		return true;
	}
	
	public void solveIfSpearRange(){
		onSpearRange = false;
		if (getPosition().dst( environment.getTamer().getShadow().getPosition()) < SIZE && !decaying && !markedDead){
			onSpearRange = true;
			environment.getTamer().setCreatureOnSpearRange( getCenterPosition() );
		}
	}

	@Override
	public int getType() {
		return Creature.TYPE_ANT;
	}

	public Worm getTargetWorm(){
		return targetWorm;
	}
	
	@Override
	public boolean isCollisionDisabled() {
		return true;
	}
	
	//-----------------------------------------
	//Creature implementation
	//-----------------------------------------
	
	@Override
	public void spearHit(Spear spear) {
		if(targetPart != null){
			targetWorm.unBind();
			targetWorm.setBeingEaten(false);
		}
		
		if(targetWorm!=null){
			targetWorm.setBeingEaten(false);
			targetWorm.setAttacked(false);
		}
		
		decay();
		playSound(TamerSound.SPEAR_ANT);
	}
	
	@Override
	public void kill() {
		markAsCarbage();	
	}
	
	@Override
	public void decay() {
		decaying = true;
	}
	
	@Override
	public void unBind() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyPull(Vector2 point,float magnitude) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Creature affectedCreature(Vector2 point,float radius) {
		if( this.getPosition().dst(point) - SIZE /2 < radius)
			return this;
		else
			return null;
	}
	
	@Override
	public float getSpeed() {
		return SPEED;
	}

	@Override
	public boolean isDecaying() {
		return decaying;
	}
}
