package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.PawnFoot;
import com.dazpetty.zeroz.entities.Projectile;

public class DazContactListener implements ContactListener {

	private int count;
	

	
	

	
	private boolean playerDead;
	public boolean damagePlayer = false;

	
	
	ContactHandler ch;
	
	public DazContactListener(ContactHandler ch){
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


		
		Object objA = fa.getBody().getUserData();
		Object objB = fb.getBody().getUserData();
		
	/*	if (objA instanceof PawnFoot || objB instanceof String){
			if (((String)objB).equals("ground")){
				((PawnFoot)objA).isOnGround = true;
			}
		}
		if (objB instanceof PawnFoot || objA instanceof String){
			if (((String)objA).equals("ground")){
				((PawnFoot)objB).isOnGround = true;
			}
		}*/
		
		ch.handleCollision(objA, objB);
	
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
	/*	Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa == null || fb == null)
			return;

		if (fa.getUserData() == null || fb.getUserData() == null)
			return;
		
		Object objA = fa.getBody().getUserData();
		Object objB = fb.getBody().getUserData();
		
		if (objA instanceof PawnFoot || objB instanceof String){
			if (((String)objB).equals("ground")){
				((PawnFoot)objA).isOnGround = false;
			}
		}
		if (objB instanceof PawnFoot || objA instanceof String){
			if (((String)objA).equals("ground")){
				((PawnFoot)objB).isOnGround = false;
			}
		}*/
		
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
	}

}
/*
 * public DazContactListener() { super(); itemsToRemove = new Array<Body>();
 * aiBodiesToRemove = new Array<Body>(); bodiesToRemove = new Array<Body>();
 * enemiesToDamage = new Array<Body>(); destroyablesToDamage = new
 * Array<Body>(); droneToDamage = new Array<Body>(); copterTurretToDamage = new
 * Array<Body>(); }
 * 
 * 
 * // set the user data of the body to the object
 * 
 * 
 * public void checkCollision(Fixture fa, Fixture fb, String object){ if
 * (fa.getUserData().equals(object)|| fb.getUserData().equals(object)){ String
 * sb = (String) fb.getUserData(); String sa = (String) fa.getUserData(); String
 * hold = new String (sa); int runloop = 0; System.out.println("Collision:"+ sa
 * + "("+ fa.toString() + ")" + " && " + sb + "("+ fb.toString() + ")"); while
 * (runloop < 2){ if ((sa == "item" && sb == "player")){ Body bod =
 * fa.getBody(); int stb = (Integer) bod.getUserData();
 * System.out.println("touch item:" + stb ); itemsToRemove.add(bod); }
 * 
 * if ((sa == "copterturret" || sa == "ai" || sa == "ground" || sa ==
 * "destroyable" || sa == "drone") && sb == "playerproj"){ if (fb.getBody() !=
 * null){ Body bod = fb.getBody(); int st = (Integer) bod.getUserData();
 * System.out.println(st); bodiesToRemove.add(fb.getBody()); if(sa == "ai"){
 * Body bodb = fa.getBody(); int stb = (Integer) bodb.getUserData();
 * System.out.println("Enemy " + stb + " damage."); if (fa.getBody() != null) {
 * enemiesToDamage.add(fa.getBody());} }else if(sa == "destroyable"){ Body bodb
 * = fa.getBody(); int stb = (Integer) bodb.getUserData();
 * System.out.println("Destroyable " + stb + " damage."); if (fa.getBody() !=
 * null) { destroyablesToDamage.add(fa.getBody());} }else if(sa == "drone"){
 * Body bodb = fa.getBody(); Integer stb = (Integer) bodb.getUserData();
 * System.out.println("---------Drone " + stb + " damage."); if (fa.getBody() !=
 * null) { droneToDamage.add(fa.getBody());} }else if(sa == "copterturret"){
 * Body bodb = fa.getBody(); int stb = (Integer) bodb.getUserData();
 * System.out.println("---------CopterTurret " + stb + " damage."); if
 * (fa.getBody() != null) { copterTurretToDamage.add(fa.getBody());} } } }
 * 
 * if ((sa == "player" || sa == "ground") && sb == "aiproj"){ if (fb.getBody()
 * != null){ Body bod = fb.getBody(); int st = (Integer) bod.getUserData();
 * System.out.println(st); aiBodiesToRemove.add(fb.getBody()); if(sa ==
 * "player"){ Body bodb = fa.getBody(); int stb = (Integer) bodb.getUserData();
 * System.out.println("Player " + stb + " damage.");
 * System.out.println("Damage Player"); damagePlayer = true; } } } runloop++; sa
 * = sb; sb = hold; } } }
 * 
 * public boolean DamagePlayer(){ if (damagePlayer){ damagePlayer = false;
 * return true; }else{ return false; } }
 */

/*
 * public void endContact(Contact contact) {
 * 
 * Fixture fa = contact.getFixtureA(); Fixture fb = contact.getFixtureB();
 * 
 * if(fa == null || fb == null) return;
 * 
 * if(fa.getUserData() != null && fa.getUserData().equals("")) { // count--; }
 * if(fb.getUserData() != null && fb.getUserData().equals("")) { // count--; }
 * 
 * }
 * 
 * public boolean playerCanJump() { return count > 0; } public Array<Body>
 * getDrones() { return droneToDamage; } public Array<Body> getDestroyables() {
 * return destroyablesToDamage; } public Array<Body> getAiBodies() { return
 * aiBodiesToRemove; } public Array<Body> getBodies() { return bodiesToRemove; }
 * public Array<Body> getItems() { return itemsToRemove; } public Array<Body>
 * getEnemies() { return enemiesToDamage; } public Array<Body> getCopterTurret()
 * { return copterTurretToDamage; } public boolean isPlayerDead() { return
 * playerDead; }
 * 
 * public void preSolve(Contact c, Manifold m) {} public void postSolve(Contact
 * c, ContactImpulse ci) {}
 * 
 * }
 */
