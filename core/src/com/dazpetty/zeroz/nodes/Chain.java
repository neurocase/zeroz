package com.dazpetty.zeroz.nodes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MotorJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

public class Chain {
	
	BodyDef bodyDef;
	Body body;
	Body bodyb;
	
	
	
	Joint joint;
	JointDef jointDef;
	Fixture fixture;
	
	Vector2 worldPos;
	
	public Chain(float x, float y, int links, World world){
		worldPos = new Vector2(x,y);
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x,y);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(1f, 0.2f);
		CircleShape pivot = new CircleShape();
		pivot.setRadius(0.2f);
		//bodyDef.
		
		body = world.createBody(bodyDef);
		

		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x,y-2);
		
		bodyb = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();

		Vector2 tmpVec2 = new Vector2 (0,0);
		fixtureDef.shape = box;
		
		fixture = bodyb.createFixture(fixtureDef);
		fixture.setUserData("dynamic");
		fixture.setFriction(0);
		body.setFixedRotation(false);
		
		//MotorJointDef distJointDef = new MotorJointDef();
		RevoluteJointDef distJointDef = new RevoluteJointDef();
		//DistanceJointDef
		//jointDef.type = JointType.RevoluteJoint
		//RopeJointDef ropeJointDef = new RopeJointDef();
	
		//ropeJointDef.
		//jointDef.type = JointType.DistanceJoint;
		distJointDef.bodyA = bodyb;
		distJointDef.bodyB = body;
		distJointDef.enableMotor = true;
		distJointDef.motorSpeed = 5;
		

		distJointDef.maxMotorTorque = 10;
		//distJointDef.
		//distJointDef.maxForce = 5;
		//distJointDef.initialize(body, bodyb);
		distJointDef.collideConnected = false;	
		
		
		distJointDef.enableLimit =true;
		
		//distJointDef.
		//distJointDef.length = 5;
	//	distJointDef.motorSpeed = 2;
	//	distJointDef.enableMotor = true;
	//	distJointDef.initialize(body, bodyb, worldPos);
		world.createJoint(distJointDef);
	//	body.setLinearVelocity(0, -2);
		body.setAngularVelocity(5);
		body.applyAngularImpulse(5, true);
	}

}
