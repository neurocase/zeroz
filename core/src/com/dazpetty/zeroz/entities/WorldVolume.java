package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.LevelManager;

public class WorldVolume {
/*
 *  DISPLAY MESSAGES AND TRIGGER EVENTS
 */
	
	/*
	 *    World
	 * 
	 */
	
	
	public Body body;
	public FixtureDef worldVolFixDef;
	public Fixture worldVolFix;
	
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	
	public boolean isAlive = true;
	public boolean trigger = false;
	public String type;
	LevelManager levelMan;
	int triggerKey = 0;
	
	boolean onceOnly = false;
	
	public WorldVolume(float x, float y, String type, int triggerKey, World world, LevelManager levelMan){
		this.triggerKey = triggerKey;
		this.levelMan = levelMan;
		this.type = type;
		BodyDef worldVolDef = new BodyDef();
		worldVolDef.position.set(x, y);
		worldVolDef.type = BodyType.StaticBody;
		body = world.createBody(worldVolDef); 
		FixtureDef worldVolFixDef = new FixtureDef();
		PolygonShape pBox = new PolygonShape();
	    pBox.setAsBox(3, 3);
	    worldVolFixDef.shape = pBox;
	    worldVolFixDef.isSensor = true;
	    worldVolFix = body.createFixture(worldVolFixDef);
	    worldVolFix.setUserData(this);	
		body.createFixture(worldVolFixDef);		
		if (type.equals("once")){
			DazDebug.print("****");
			DazDebug.print("once only world volume created");
			DazDebug.print("****");
			onceOnly = true;
		}else{
			DazDebug.print("-----------------REPEATABLE world volume created");
		}
	}
	
	
	public boolean ready(){
		if (onceOnly = false){
			return true;
		}else{
			if (!allreadyUsed){
				allreadyUsed = true;
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean allreadyUsed = false;
	
	public void triggerVolumeOn(){
		if (!trigger && ready()){
			trigger = true;
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=");
			DazDebug.print("-=-=-=-=--=-=-=-=-TRIGGER ON:::" + type + "-=-=-=-=");
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=-=-=-=--=");
			levelMan.eventMan.CallTriggerValue(triggerKey);
		}
		
		
	
	}
	
	public void triggerVolumeOff(){
		if (trigger && ready()){
			trigger = false;
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=-=-=-=--=-");
			DazDebug.print("-=-=-=-=--=-=-=-=-TRIGGER OFF:::" + type + "-=-=-=-");
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=-=-=-=--=-");
		}
	}
}
