package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.physics.RigidBodyBox;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.tTimer;

public class Worm extends DynamicObject implements Creature{
	
	private final float BORDER_OFFSET = 0.0f;
	int ordinal = 1;
	private ArrayList<WormPart> parts;
	private final float SPEED = 2.5f;
	private WormPart head = null;
	private WormPart tail = null;
	
	//for effects
	private ControlContainer controls;
	private boolean colorChanged;
	private tTimer screamTimer;
	
	public Worm(){
		parts = new ArrayList<WormPart>();
		setBorderOffset(BORDER_OFFSET);
		
		controls = ControlContainer.instance();
	}
	
	public void setup(){
		//No action because this object is not ment to be on game right away
	}
	
	public void wakeUp(Environment environment){
		environment.getCreatures().add(this);
		addPart("head",0,super.getPosition(),super.getVelocity());
		for(int i = 0 ; i < 3 ; i++)
			addPart("joint",i+1,super.getPosition(),super.getVelocity());
		connectPieces();
		
		for(WormPart part : parts){
			environment.addObject(part);
			part.setZindex(0);
		}
		
		head = parts.get(0);
		tail = parts.get(parts.size()-1);
		
	}

	public void addPart(String type, int ordinal,Vector2 pos, Vector2 vel){
		WormPart part = null;
		if(type.equalsIgnoreCase("head")){
			part = new WormPart();
			part.createHead(pos,vel,this);
		}else if(type.equalsIgnoreCase("joint")){
			part = new WormPart();
			part.createBodyPart(ordinal,pos,vel,this);
		}else throw new IllegalArgumentException("Wrong partname");
		
		parts.add(part);
		
	}
	
	public void connectPieces(){
		for(int i = 0 ; i < parts.size() ; i++){
			if( (i + 1) < parts.size()){
				parts.get( i + 1 ).attachToParent(parts.get(i));
			}else if( (i + 1 ) == parts.size() )
				parts.get(i).setAsTail();		
		}
	}
	
	public void update(float dt){
		head.solveJoints(dt);
		head.updateChild(dt);
		head.getVelocity().set(head.getForce());
		solveEffects();
	}
	
	public void draw(SpriteBatch batch){
		//No action
	}
	
	public void solveEffects(){
		for(int i = 0 ; i < parts.size() ; i++){
			//if (parts.get(i).getPosition().dst(controls.getEnvironment().getTamer().getShadow().getPosition()) < controls.getSpearButton().getThrowDistance() / 2){
			if (parts.get(i).getPosition().dst(controls.getEnvironment().getTamer().getShadow().getPosition()) < 1){
				parts.get(i).setOnSpearRange(true);
			}else{
				parts.get(i).setOnSpearRange(false);
			}
		}
	}
	
	public void doScreamEffect(){
		if(!head.isBlinking())head.setBlinking(true);
		else head.setBlinking(false);	
	}

	public void setHead(WormPart part){
		head = part;
	}
	public void dispose(){
		parts = null;
		head = null;
	}
	
	public WormPart getTail(){
		return parts.get(parts.size()-1);
	}
	public float getSpeed(){
		return SPEED;
	}
	
	public WormPart getHead(){
		return head;
	}
	public Vector2 getPosition(){
		return head.getPosition();
	}
	public Vector2 getHeading(){
		return head.getHeading();
	}
	
	public ArrayList<WormPart> getParts(){
		return parts;
	}

	@Override
	public void spearHit(Spear spear) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unBind() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moveToPoint(Vector2 point) {
		head.moveToPoint(point);
		
	}

	@Override
	public Creature affectedCreature(Vector2 point,float radius) {
		for(int i = 0; i < parts.size() ; i++){
			if(parts.get(i).affectedCreature(point,radius) != null)
				return parts.get(i);
		}
		return null;
	}
	
	@Override
	public boolean isAffected(Vector2 point, float radius) {
		boolean partAffected = false;
		for(int i = 0 ; i < parts.size() ; i++){
			if(parts.get(i).getPosition().dst(point) < radius){
				partAffected = true;
			}
		}
		return partAffected;
	}

	@Override
	public void applyPull(Vector2 point, float magnitue) {
		//float distToHead = point.dst(head.getPosition());
		//float distToTail = point.dst(tail.getPosition());
		//if(distToHead < distToTail)
			head.applyPull(point,magnitue);
		//else
			//tail.applyPull(point,magnitue);
	}
	
	public void setHeading(Vector2 newHeading){
		head.setHeading(newHeading);
		head.setForce(getHeading().mul(SPEED));
	}
	
	public float getSPEED() {
		return SPEED;
	}

	@Override
	public RigidBodyBox getCollider() {
		return head.getCollider();
	}
	
}
