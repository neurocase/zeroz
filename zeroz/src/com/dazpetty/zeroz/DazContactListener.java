package com.dazpetty.zeroz;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class DazContactListener implements ContactListener {
	
	private int count;
	private Array<Body> bodiesToRemove;
	private Array<Body> enemiesToDamage;
	private boolean playerDead;

	
	public DazContactListener() {
		super();
		bodiesToRemove = new Array<Body>();
		enemiesToDamage = new Array<Body>();
	}
	
	
	public void printCollisions(Fixture fa, Fixture fb){
		String sb = (String) fb.getUserData();
		String sa = (String) fa.getUserData();
		System.out.println("fa is: "+ sa + " " + " fb is: " + sb);
		
		if ((sa == "ai" || sa == "ground") && sb == "playerproj"){
			if (fb.getBody() != null){
				Body bod = fb.getBody();
				String st = (String) bod.getUserData();
				System.out.println(st);
				bodiesToRemove.add(fb.getBody());
				if(sa == "ai"){
					Body bodb = fa.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Enemy " + stb + " damage.");
					if (fa.getBody() != null) { enemiesToDamage.add(fa.getBody());}
				}
			}
		}
		if ((sb == "ai"|| sb == "ground") && sa == "playerproj"){
			if (fa.getBody() != null){
				Body bod = fa.getBody();
				String st = (String) bod.getUserData();
				System.out.println(st);
				bodiesToRemove.add(fa.getBody());
				if(sb == "ai"){
					Body bodb = fb.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Enemy " + stb + " damage.");
					if (fb.getBody() != null) {enemiesToDamage.add(fb.getBody());}
				}
			}
		}
		
		
	}
	
	public void beginContact(Contact contact) {
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		
		if(fa == null || fb == null) return;
		
		if (fa.getUserData() == null || fb.getUserData() == null) return;
				
		if(fa.getUserData() != null && fa.getUserData().equals("ai")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ai")) {
			printCollisions(fa,fb);
		}
		
		
		
		if(fa.getUserData() != null && fa.getUserData().equals("player")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("player")) {
			printCollisions(fa,fb);
		}
		
		
		if(fa.getUserData() != null && fa.getUserData().equals("playerproj")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("playerproj")) {
			printCollisions(fa,fb);
		}

		
	}
	
	public void endContact(Contact contact) {
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa == null || fb == null) return;
		
		if(fa.getUserData() != null && fa.getUserData().equals("")) {
		//	count--;
		}
		if(fb.getUserData() != null && fb.getUserData().equals("")) {
		//	count--;
		}
		
	}
	
	public boolean playerCanJump() { return count > 0; }
	public Array<Body> getBodies() { return bodiesToRemove; }
	public Array<Body> getEnemies() { return enemiesToDamage; }
	public boolean isPlayerDead() { return playerDead; }
	
	public void preSolve(Contact c, Manifold m) {}
	public void postSolve(Contact c, ContactImpulse ci) {}
	
}
