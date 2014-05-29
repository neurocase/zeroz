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
	public Body body;
	public FixtureDef worldVolFixDef;
	public Fixture worldVolFix;
	
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	
	public boolean isAlive = true;
	public boolean trigger = false;
	public String type;
	LevelManager levelMan;
	
	public WorldVolume(float x, float y, String type, World world, LevelManager levelMan){
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
	}
	
	public void triggerVolumeOn(){
		if (!trigger){
			trigger = true;
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=");
			DazDebug.print("-=-=-=-=--=-=-=-=-TRIGGER ON:::" + type + "-=-=-=-=");
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=-=-=-=--=");
		}
		
			for (int i = 0; i < levelMan.ENEMY_SPAWNER_LIMIT; i++){
				
			//	if (levelMan.enemyspawner[i].type.equals("triggered")){
				if (levelMan.enemyspawner[i] != null){
					
					if (levelMan.enemyspawner[i].type.equals("triggered")){
					levelMan.enemyspawner[i].triggerOn();
				}
				//}
				}
			//}
		}
	}
	
	public void triggerVolumeOff(){
		if (trigger){
			trigger = false;
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=-=-=-=--=-");
			DazDebug.print("-=-=-=-=--=-=-=-=-TRIGGER OFF:::" + type + "-=-=-=-");
			DazDebug.print("-=-=-=-=--=-=-=-=--=-=-=-=--=-=-=-=-=-=-=-=--=-");
		}
	}
}
