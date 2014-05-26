package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
	
	public PawnFoot(PawnEntity parentPawn, World world){
		
		
		this.parentPawn = parentPawn;
		
		BodyDef footbodyDef = new BodyDef();
		footbodyDef.type = BodyType.KinematicBody;
		
		footbody = world.createBody(footbodyDef);
		footbody.setActive(true);
		footbody.setAwake(true);
		footfixtureDef = new FixtureDef();
	
		
		PolygonShape footBox = new PolygonShape();
		footBox.setAsBox(0.1f, 0.15f);
		footfixtureDef.shape = footBox;
		footfixture = footbody.createFixture(footfixtureDef);
		footbody.setUserData(this);
		footfixture.setUserData(this);
		footbody.createFixture(footfixtureDef);
		
		
		
	}
}
