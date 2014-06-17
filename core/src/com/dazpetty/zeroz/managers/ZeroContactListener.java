package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.PawnFoot;
import com.dazpetty.zeroz.entities.Projectile;

public class ZeroContactListener implements ContactListener {

	private int count;
	private int numFootContacts;

	private boolean playerDead;
	public boolean damagePlayer = false;

	
	
	ContactHandler ch;
	
	public ZeroContactListener(ContactHandler ch){
		this.ch = ch;
		 
	}
	
	public void print(String str) {
		System.out.println(str);
	}

	@Override
	public void beginContact(Contact contact) {
	
		
		
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fa.getUserData() == null || fb.getUserData() == null)
			return;
		
		
		

		
		Object objA = fa.getUserData();
		Object objB = fb.getUserData();
		/*if ((objB instanceof PawnFoot) || (objA instanceof PawnFoot)) {
			if (objB instanceof String){
				if (((String)objB).equals("dynamic")){
					 contact.setFriction(10000f);
				 }
			}
			if (objA instanceof String){
				if (((String)objA).equals("dynamic")){
					 contact.setFriction(10000f);
				 }
			}
		}*/
		
		ch.handleCollision(objA, objB);
	
	
	}

	@Override
	public void endContact(Contact contact) {
	
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fa.getUserData() == null || fb.getUserData() == null)
			return;
		
		Object objA = fa.getUserData();
		Object objB = fb.getUserData();
		
		ch.handleEndCollision(objA, objB);

	}
	boolean setCollision = true;
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}

 
