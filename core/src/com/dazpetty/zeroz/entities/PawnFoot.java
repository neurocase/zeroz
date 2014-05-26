package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PawnFoot {

	public boolean isOnGround = false;
	public PawnEntity parentPawn;
	
	public FixtureDef footfixtureDef;
	public BodyDef footbodyDef = new BodyDef();
	public Body footbody;	
	public World world;
	public Fixture footfixture;
	public Body body;
	
	public int numFootContacts = 0;
	public boolean isOnPlatform; 
	
	
	public PawnFoot(PawnEntity parentPawn, World world, Body body){
		this.body = body;
		this.parentPawn = parentPawn;
		
		BodyDef footbodyDef = new BodyDef();
		footbodyDef.type = BodyType.KinematicBody;
		
		footbody = world.createBody(footbodyDef);
		footbody.setActive(true);
		footbody.setAwake(true);
		footfixtureDef = new FixtureDef();
	//	footfixtureDef.filter.maskBits = 3;
		
		CircleShape footBox = new CircleShape();
		footBox.setRadius(0.2f);
		footBox.setPosition(new Vector2(0, -0.8f));
		footfixtureDef.shape = footBox;

		footfixture = body.createFixture(footfixtureDef);
		footfixture.setUserData(this);
		footfixture.setSensor(true);
		footfixtureDef.friction = 1;
		//footfixtureDef.isSensor = true;
		body.createFixture(footfixtureDef);
		
		
		
		
	}
	public void incFootContact(){
		numFootContacts++;
	}
	public void decFootContact(){
		numFootContacts--;
	}
}
