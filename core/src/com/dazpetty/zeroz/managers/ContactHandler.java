package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.Projectile;

public class ContactHandler {

	private Array<PawnEntity> enemiesToDamage;
	public Array<PawnEntity> getEnemiesToDamage() { return enemiesToDamage; }
	
	private Array<Projectile> projToRemove;
	public Array<Projectile> getProjToRemove(){ return projToRemove; }
	
	private Array<Destroyable> destroyablesToDamage;
	public Array<Destroyable> getDestroyablesToDamage() { return destroyablesToDamage; }
	
	private Array<Drone> droneToDamage;
	public Array<Drone> getDronesToDamage(){ return droneToDamage; }
	
	private Array<CopterTurret> copterTurretToDamage;
	public Array<CopterTurret> getCopterTurretToDamage() { return copterTurretToDamage; }

	
	
	public ContactHandler(){
		enemiesToDamage = new Array<PawnEntity>(); 
		projToRemove = new Array<Projectile>();
		destroyablesToDamage = new Array<Destroyable>();
		droneToDamage = new Array<Drone>();
		copterTurretToDamage = new Array<CopterTurret>();
		
	}
	
	public void handleCollision(Object objA, Object objB){
		boolean removeProj = false;
		for (int i = 0; i < 2; i++){
			
			
			Object objBb = objB;
			/*
			 * TODO: MOve this out of collision listener into worldLogic so that the
			 * contactlistener doesn't crash
			 */
			if ((objA instanceof String || objA instanceof CopterTurret
					|| objA instanceof PawnEntity || objA instanceof Destroyable || objA instanceof Drone)
					&& objB instanceof Projectile) {
				
				
				if (objA instanceof String) {
					if (((String) objA).equals("solid")) {
						DazDebug.print("::: Ground TAKES DAMAGE");
						removeProj = true;
						
					}
				}
				
				if ((objB instanceof Projectile)) {
					if (((Projectile) objB).isAI()) {
						if (objA instanceof PawnEntity) {
							if (!((PawnEntity) objA).isAI) {
								DazDebug.print("::: PLAYER TAKES DAMAGE");
								removeProj = true;
							}
						}
					} else {
						// If the projectile is the players
						if (objA instanceof PawnEntity) {
							if (((PawnEntity) objA).isAI) {
								DazDebug.print("::: AI TAKES DAMAGE");
								enemiesToDamage.add((PawnEntity) objA);
								removeProj = true;
							}
						} else if (objA instanceof CopterTurret) {
							DazDebug.print("::: CopterTurret TAKES DAMAGE");
							removeProj = true;
							copterTurretToDamage.add((CopterTurret) objA);
						} else if (objA instanceof Destroyable) {
							DazDebug.print("::: Destroyable TAKES DAMAGE");
							removeProj = true;
							destroyablesToDamage.add((Destroyable) objA);
						} else if (objA instanceof Drone) {
							DazDebug.print("::: Drone TAKES DAMAGE");
							removeProj = true;
							droneToDamage.add((Drone) objA);
						}
					}
				}
				if (removeProj && (objB instanceof Projectile)){
					projToRemove.add((Projectile) objB);
				}
			}
			//swap
			
			
			removeProj = false;
			objB = objA;
			objA = objBb;
		}
	}
	
	public void clearCollisions(){
		enemiesToDamage.clear();
		projToRemove.clear();
		destroyablesToDamage.clear();
		droneToDamage.clear();	
		copterTurretToDamage.clear();
	}
	
	
}
