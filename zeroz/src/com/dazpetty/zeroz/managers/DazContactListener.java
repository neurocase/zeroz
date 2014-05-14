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
	private Array<Body> droneToDamage;
	private boolean playerDead;
	public boolean damagePlayer = false;

	
	public DazContactListener() {
		super();
		itemsToRemove = new Array<Body>();
		aiBodiesToRemove = new Array<Body>();
		bodiesToRemove = new Array<Body>();
		enemiesToDamage = new Array<Body>();
		destroyablesToDamage = new Array<Body>();
		droneToDamage = new Array<Body>();
	}
	
	
	public void checkCollision(Fixture fa, Fixture fb, String object){
		if (fa.getUserData().equals(object)|| fb.getUserData().equals(object)){
			String sb = (String) fb.getUserData();
			String sa = (String) fa.getUserData();
			String hold = sa;
			int runloop = 0;
			System.out.println("Collision:"+ sa + " && " + sb);
			while (runloop < 2){
				if ((sa == "item" && sb == "player")){
							Body bod = fa.getBody();
							int stb = (Integer) bod.getUserData();
							System.out.println("touch item:" + stb );
							itemsToRemove.add(bod);
				}
				
				if ((sa == "ai" || sa == "ground" || sa == "destroyable" || sa == "drone") && sb == "playerproj"){
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
						}else if(sa == "drone"){
							Body bodb = fa.getBody();
							Integer stb = (Integer) bodb.getUserData();
							System.out.println("---------Drone " + stb + " damage.");
							if (fa.getBody() != null) { droneToDamage.add(fa.getBody());}
						}
					}
				}
			
				if ((sa == "player" || sa == "ground") && sb == "aiproj"){
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
						}
					}
				}
				runloop++;
				sa = sb;
				sb = hold;
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
		
		checkCollision(fa, fb,"destroyable");
		checkCollision(fa, fb,"item");
		checkCollision(fa, fb,"player");
		checkCollision(fa, fb,"playerproj");
		checkCollision(fa, fb,"aiproj");
		checkCollision(fa, fb,"drone");
	
		
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
	public Array<Body> getDrones() { return droneToDamage; }
	public Array<Body> getDestroyables() { return destroyablesToDamage; }
	public Array<Body> getAiBodies() { return aiBodiesToRemove; }
	public Array<Body> getBodies() { return bodiesToRemove; }
	public Array<Body> getItems() { return itemsToRemove; }
	public Array<Body> getEnemies() { return enemiesToDamage; }
	public boolean isPlayerDead() { return playerDead; }
	
	public void preSolve(Contact c, Manifold m) {}
	public void postSolve(Contact c, ContactImpulse ci) {}
	
}
