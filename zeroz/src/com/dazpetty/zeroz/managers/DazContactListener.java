package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class DazContactListener implements ContactListener {
	
	private int count;
	private Array<Body> aiBodiesToRemove;
	private Array<Body> bodiesToRemove;
	private Array<Body> itemsToRemove;
	private Array<Body> enemiesToDamage;
	private Array<Body> destroyablesToDamage;
	private boolean playerDead;
	public boolean damagePlayer = false;

	
	public DazContactListener() {
		super();
		itemsToRemove = new Array<Body>();
		aiBodiesToRemove = new Array<Body>();
		bodiesToRemove = new Array<Body>();
		enemiesToDamage = new Array<Body>();
		destroyablesToDamage = new Array<Body>();
	}
	
	
	public void printCollisions(Fixture fa, Fixture fb){
		String sb = (String) fb.getUserData();
		String sa = (String) fa.getUserData();
		System.out.println("fa is: "+ sa + " " + " fb is: " + sb);
		if ((sa == "item" && sb == "player")){
					Body bod = fa.getBody();
					int stb = (Integer) bod.getUserData();
					System.out.println("touch item:" + stb );
					itemsToRemove.add(fa.getBody());
		}
		if ((sb == "item" && sa == "player")){
					Body bod = fb.getBody();
					int stb = (Integer) bod.getUserData();
					System.out.println("touch item:" + stb );
					itemsToRemove.add(fb.getBody());
		}
		
		
		if ((sa == "ai" || sa == "ground" || sa == "destroyable") && sb == "playerproj"){
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
				}else if(sa == "destroyable"){
					Body bodb = fa.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Destroyable " + stb + " damage.");
					if (fa.getBody() != null) { destroyablesToDamage.add(fa.getBody());}
				}
			}
		}
		if ((sb == "ai"|| sb == "ground" || sb == "destroyable") && sa == "playerproj"){
			if (fa.getBody() != null){
				Body bod = fa.getBody();
				String st = (String) bod.getUserData();
				System.out.println(st);
				aiBodiesToRemove.add(fa.getBody());
				if(sb == "ai"){
					Body bodb = fb.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Enemy " + stb + " damage.");
					if (fb.getBody() != null) {enemiesToDamage.add(fb.getBody());}
				}else if(sb == "destroyable"){
					Body bodb = fb.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Destroyable " + stb + " damage.");
					if (fb.getBody() != null) { destroyablesToDamage.add(fb.getBody());}
				}
			}
		}
		
		if ((sa == "player" || sa == "ground" || sa == "destroyable") && sb == "aiproj"){
			if (fb.getBody() != null){
				Body bod = fb.getBody();
				String st = (String) bod.getUserData();
				System.out.println(st);
				aiBodiesToRemove.add(fb.getBody());
				if(sa == "player"){
					Body bodb = fa.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Player " + stb + " damage.");
					System.out.println("Damage Player");
					damagePlayer = true;
				}else if(sa == "destroyable"){
					Body bodb = fa.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Destroyable " + stb + " damage.");
					if (fa.getBody() != null) { destroyablesToDamage.add(fa.getBody());}
				}
			}
		}
		if ((sb == "player"|| sb == "ground" || sb == "destroyable") && sa == "aiproj"){
			if (fa.getBody() != null){
				Body bod = fa.getBody();
				String st = (String) bod.getUserData();
				System.out.println(st);
				bodiesToRemove.add(fa.getBody());
				if(sb == "player"){
					Body bodb = fb.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Player " + stb + " damage.");
					System.out.println("Damage Player");
					damagePlayer = true;
				}else if(sb == "destroyable"){
					Body bodb = fb.getBody();
					String stb = (String) bodb.getUserData();
					System.out.println("Destroyable " + stb + " damage.");
					if (fb.getBody() != null) { destroyablesToDamage.add(fb.getBody());}
				}
			}
		}
		
		
	}
	public boolean DamagePlayer(){
		if (damagePlayer){
			damagePlayer = false;
			return true;
		}else{
			return false;
		}
	}
	
	public void beginContact(Contact contact) {
		
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		
		if(fa == null || fb == null) return;
		
		if (fa.getUserData() == null || fb.getUserData() == null) return;
				
	/*	if(fa.getUserData() != null && fa.getUserData().equals("ai")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("ai")) {
			printCollisions(fa,fb);
		}*/
		
		if(fa.getUserData() != null && fa.getUserData().equals("destroyable")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("destroyable")) {
			printCollisions(fa,fb);
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("item")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("item")) {
			printCollisions(fa,fb);
		}
		
	/*	if(fa.getUserData() != null && fa.getUserData().equals("player")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("player")) {
			printCollisions(fa,fb);
		}*/
		
		
		if(fa.getUserData() != null && fa.getUserData().equals("playerproj")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("playerproj")) {
			printCollisions(fa,fb);
		}
		if(fa.getUserData() != null && fa.getUserData().equals("aiproj")) {
			printCollisions(fa,fb);
		}
		if(fb.getUserData() != null && fb.getUserData().equals("aiproj")) {
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
	public Array<Body> getDestroyables() { return destroyablesToDamage; }
	public Array<Body> getAiBodies() { return aiBodiesToRemove; }
	public Array<Body> getBodies() { return bodiesToRemove; }
	public Array<Body> getItems() { return itemsToRemove; }
	public Array<Body> getEnemies() { return enemiesToDamage; }
	public boolean isPlayerDead() { return playerDead; }
	
	public void preSolve(Contact c, Manifold m) {}
	public void postSolve(Contact c, ContactImpulse ci) {}
	
}
