package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.PawnFoot;
import com.dazpetty.zeroz.entities.Projectile;
import com.dazpetty.zeroz.entities.WorldVolume;

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

	
	private Array<WorldVolume> worldVolumesToTrigger;
	public Array<WorldVolume> getWorldVolumesToTrigger() { return worldVolumesToTrigger; }
	
	
	public ContactHandler(){
		enemiesToDamage = new Array<PawnEntity>(); 
		projToRemove = new Array<Projectile>();
		destroyablesToDamage = new Array<Destroyable>();
		droneToDamage = new Array<Drone>();
		copterTurretToDamage = new Array<CopterTurret>();
		worldVolumesToTrigger = new Array<WorldVolume>();
		
	}
	
	public void handleCollision(Object objA, Object objB){
		boolean removeProj = false;
		for (int i = 0; i < 2; i++){
			Object objBb = objB;

			
			if ((objA instanceof PawnEntity) && (objB instanceof WorldVolume)){
				if (!((PawnEntity) objA).isAI) {
					worldVolumesToTrigger.add((WorldVolume) objB);
				}
			}
			if ((objA instanceof String || objA instanceof CopterTurret
					|| objA instanceof PawnEntity || objA instanceof Destroyable || objA instanceof Drone || objA instanceof Door)
					&& objB instanceof Projectile) {
				
				if (objA instanceof Door){
					DazDebug.print("ITS A FUCKEN DOOR MATE!");
					DazDebug.print("AND IT SHOULD BLOODY REGISTER!");
					removeProj = true;
				}
				
				if (objA instanceof String) {
					if (((String) objA).equals("solid")) {
					
						removeProj = true;
						
					}
				}
				
				if ((objB instanceof Projectile)) {
					if (((Projectile) objB).isAI()) {
						if (objA instanceof PawnEntity) {
							if (!((PawnEntity) objA).isAI) {
								//DazDebug.print("::: PLAYER TAKES DAMAGE");
								removeProj = true;
							}
						}
					} else {
						// If the projectile is the players
						if (objA instanceof PawnEntity) {
							if (((PawnEntity) objA).isAI) {
								//DazDebug.print("::: AI TAKES DAMAGE");
								enemiesToDamage.add((PawnEntity) objA);
								removeProj = true;
							}
						} else if (objA instanceof CopterTurret) {
							//DazDebug.print("::: CopterTurret TAKES DAMAGE");
							removeProj = true;
							copterTurretToDamage.add((CopterTurret) objA);
						} else if (objA instanceof Destroyable) {
							//DazDebug.print("::: Destroyable TAKES DAMAGE");
							removeProj = true;
							destroyablesToDamage.add((Destroyable) objA);
						} else if (objA instanceof Drone) {
							//DazDebug.print("::: Drone TAKES DAMAGE");
							removeProj = true;
							droneToDamage.add((Drone) objA);
						}
					}
				}
				if (removeProj && (objB instanceof Projectile)){
					projToRemove.add((Projectile) objB);
				}
			}else{
				if (objA instanceof PawnFoot && objB instanceof String){
					if (((String)objB).equals("solid")){
						((PawnFoot)objA).incFootContact();
					}else if (((String)objB).equals("platform")){
						((PawnFoot)objA).incFootContact();
						((PawnFoot)objA).isOnPlatform = true;
					}
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
		worldVolumesToTrigger.clear();
	}



	public void handleEndCollision(Object objA, Object objB) {
		for (int i = 0; i < 2; i++){
			Object objBb = objB;
			

		if ((objA instanceof PawnEntity) && (objB instanceof WorldVolume)){
			if (!((PawnEntity) objA).isAI) {
				((WorldVolume) objB).triggerVolumeOff();
			}
		}	
		if (objA instanceof PawnFoot && objB instanceof String){
			if (((String)objB).equals("solid")){
				((PawnFoot)objA).decFootContact();
			}else if (((String)objB).equals("platform")){
				((PawnFoot)objA).decFootContact();
				((PawnFoot)objA).isOnPlatform = true;
			}
		}
			objB = objA;
			objA = objBb;
		}
	}


	private Vector2 tmpVec = new Vector2(0,0);
	public void handlePresolve(Object objA, Object objB, Contact contact) {
	
		/*
		 *  contact.setEnabled(false);
		 *  	Will work if here
		 */
		
		
		for (int i = 0; i < 2; i++){
			
			Object objBb = objB;
		
			if (objA instanceof PawnFoot && objB instanceof String){
				tmpVec = ((PawnFoot)objA).parentPawn.mainbody.getLinearVelocity();
				if (tmpVec.y > 0){
					if (((String)objB).equals("platform")){
						contact.setEnabled(false);
					}
				}
				
			}
			if (objA instanceof PawnEntity && objB instanceof String){
				tmpVec = ((PawnEntity)objA).mainbody.getLinearVelocity();
				if (tmpVec.y > 0){
					if (((String)objB).equals("platform")){
						contact.setEnabled(false);
					}
				}
				
			}
			objB = objA;
			objA = objBb;
		}
		
		
	}
}
