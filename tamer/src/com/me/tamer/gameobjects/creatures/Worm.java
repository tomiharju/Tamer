package com.me.tamer.gameobjects.creatures;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me.tamer.core.Hud;
import com.me.tamer.gameobjects.Environment;
import com.me.tamer.gameobjects.superclasses.DynamicObject;
import com.me.tamer.gameobjects.superclasses.GameObject;
import com.me.tamer.gameobjects.tamer.Spear;
import com.me.tamer.ui.ControlContainer;
import com.me.tamer.utils.DrawOrderComparator;

public class Worm extends DynamicObject implements Creature{
	
	private final int NUMBER_PARTS = 8;
	
	private ArrayList<WormPart> parts;
	private final float SPEED = 5.0f;
	private WormPart head = null;
	private WormPart tail = null;
	
	private boolean collisionDisabled = false;
	private boolean beingEaten = false;
	private boolean bound = false;
	
	//for effects
	private ControlContainer controls;
	private boolean colorChanged;
	private DrawOrderComparator comparator;
	
	//for draw order
	ArrayList<GameObject> gameobjects = new ArrayList<GameObject>();
	
	//Hud
	Hud hud;
	
	public Worm(){
		parts = new ArrayList<WormPart>();
		comparator = new DrawOrderComparator();
		
		hud = Hud.instance();
		
	}
	
	public void wakeUp(Environment environment){
		environment.getCreatures().add(this);
		addPart("head",0,super.getPosition(),super.getVelocity());
		for(int i = 0 ; i < NUMBER_PARTS ; i++)
			addPart("joint",i+1,super.getPosition(),super.getVelocity());
		connectPieces();
		
		for(int i = parts.size() - 1 ; i >= 0 ; i-- ){
			parts.get(i).setZindex(-1);
		}
		
		head = parts.get(0);
		tail = parts.get(parts.size()-1);	
			
		//for drawing order
		//is there better way to do this?
		for(int i = 0 ; i < parts.size() ; i++){
			gameobjects.add(((GameObject)parts.get(i)));
		}
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
	public void removePart(WormPart part){
		parts.remove(part);
		if(parts.size() > 0)
			tail = parts.get(parts.size()-1);
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
		head.update(dt);
		head.getVelocity().set(head.getForce());
		
		//kill worm when head has decayed
		if (head.getLevelOfDecay() < 0.1){
			markAsCarbage();
		}
	}
	
	public void draw(SpriteBatch batch){	
		Collections.sort(gameobjects, comparator);
		for(int i = 0 ; i < gameobjects.size() ; i++){
			gameobjects.get(i).draw(batch);
		}
	}
	
	public void doScreamEffect(){
		if(!head.isBlinking())head.setBlinking(true);
		else head.setBlinking(false);	
	}

	
	public void dispose(){
		parts = null;
		head = null;
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
		head.applyPull(point,magnitue);
	}
	@Override
	public boolean isCollisionDisabled() {
		return collisionDisabled;//!(head.getInvMass() > 0);
	}
	
	public void setCollisionDisabled(boolean b){
		collisionDisabled = b;
	}
	

	@Override
	public void setup(Environment level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(Environment level) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void debugDraw(ShapeRenderer shapeRndr) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void unBind() {
		for(int i = 0; i < parts.size(); i ++){
			parts.get(i).unBind();
		}
		bound = false;
	}

	@Override
	public void lassoHit(String lasso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void kill() {
		//kill the whole fucker
		for(int i = 0; i < parts.size(); i++){
			parts.get(i).kill();
			parts.get(i).decay();
		}
	}
	
	@Override
	public void spearHit(Spear spear) {	
	}
	
	public void bind(){
		for(int i = 0; i < parts.size(); i ++){
			parts.get(i).bind();
		}
		bound = true;
	}

	public void setTail(WormPart part){
		tail = part;
	}
	public void setHeading(Vector2 newHeading){
		head.setHeading(newHeading);
		head.setForce(getHeading().mul(SPEED));
	}

	public void setHead(WormPart part){
		head = part;
	}
	
	@Override
	public void markAsCarbage(){
		super.markAsCarbage();
		hud.updateLabel(Hud.LABEL_REMAINING, -1);
	}
	
	@Override
	public void setGraphics(String graphics) {
		// TODO Auto-generated method stub
	}

	public ArrayList<WormPart> getParts(){
		return parts;
	}

	public float getSPEED() {
		return SPEED;
	}
	@Override
	public int getType() {
		return Creature.TYPE_WORM;
	}
	public WormPart getTail(){
		return parts.get(parts.size()-1);
	}
	public float getSpeed(){
		return SPEED;
	}
	public Vector2 getSize(){
		return head.getSize();
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

	@Override
	public void decay() {
	}

	@Override
	public boolean isDecaying() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setAttacked(boolean b){
		for(int i = 0; i < parts.size(); i ++)
			parts.get(i).setAttacked(b);
	}
	
	public void setBeingEaten(boolean b){
		beingEaten = b;
	}
	
	public boolean isBeingEaten(){
		return beingEaten;
	}
	
	public boolean isBound(){
		return bound;
	}
}
