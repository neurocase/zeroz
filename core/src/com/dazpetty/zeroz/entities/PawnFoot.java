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
	public boolean isOnPlatform = false;
	public boolean isOnGround = false;
	public PawnEntity parentPawn;
	public BodyDef footbodyDef = new BodyDef();
	public Body footbody;	
	public World world;
	public FixtureDef footfixtureDef;
	public Fixture footfixture;
	public Body body;
	public int numFootContacts = 0;
	
	public void fallMode(){
		for(Fixture fixture : body.getFixtureList()){
			fixture.setSensor(true);
				if (fixture.getUserData() instanceof PawnEntity){
					fixture.setSensor(false);
				}
			}
	}
	
	public void platformMode(){
		for(Fixture fixture : body.getFixtureList()){
				fixture.setSensor(false);			
			}
	}
	
	
	public PawnFoot(PawnEntity parentPawn, World world, Body body){
		this.body = body;
		this.parentPawn = parentPawn;
		
		BodyDef footbodyDef = new BodyDef();
		footbodyDef.type = BodyType.KinematicBody;
		
		footfixtureDef = new FixtureDef();
		footfixtureDef.filter.maskBits = 7;
		
		CircleShape footBox = new CircleShape();
		footBox.setRadius(0.27f);
		footBox.setPosition(new Vector2(0, -0.8f));
		footfixtureDef.shape = footBox;

		footfixture = body.createFixture(footfixtureDef);
		footfixture.setUserData(this);
		footfixtureDef.friction = 1;
		
		body.createFixture(footfixtureDef);

	}
	public void incFootContact(){
		numFootContacts++;
	}
	public void decFootContact(){
		numFootContacts--;
	}
}
