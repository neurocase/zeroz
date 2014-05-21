package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.entities.CopterTurret;
import com.dazpetty.zeroz.entities.HumanEntity;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.EnemySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.HumanSprite;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.Projectile;
import com.dazpetty.zeroz.entities.Weapon;

public class EntityManager {

	public final int ENEMY_LIMIT = 10;
	public final int ENEMY_SPAWN_LIMIT = 20;
	public final int DESTROYABLE_LIMIT = 10;
	public final int DOOR_LIMIT = 10;
	public final int DRONE_LIMIT = ENEMY_LIMIT;
	public final int ITEM_LIMIT = 20;
	public final int PROJECTILE_LIMIT = 20;
	
	public int TOTAL_DESTROYABLES = 0;
	public int TOTAL_DOORS = 0;
	public int TOTAL_ITEMS = 0;
	
	public int enemycount = 0;
	public int dronecount = 0;
	public int enemyspawners = 0;
	
	public ProjectileManager projMan = new ProjectileManager(PROJECTILE_LIMIT);
	public ProjectileManager aiProjMan = new ProjectileManager(PROJECTILE_LIMIT);
	
	public CopterBoss copterBoss;// = new CopterBoss();
	
	public EnemySpawner enemyspawner[] = new EnemySpawner[ENEMY_SPAWN_LIMIT];
	public HumanEntity[] zenemy = new HumanEntity[ENEMY_LIMIT];
	public Destroyable[] destroyable = new Destroyable[DESTROYABLE_LIMIT];
	public Door[] door = new Door[DOOR_LIMIT];
	public Item[] item = new Item[ITEM_LIMIT];
	public Drone[] drone = new Drone[DRONE_LIMIT];
	
	public HumanSprite humanSprite = new HumanSprite();
	
	public HumanEntity zplayer;
	
	public OrthographicCamera camera;
	public World world;
	public HUDTarget hudtarget;
	public LevelManager tm;
	
	public DazContactListener cl;
	public ContactHandler ch;
	
	public EntityManager(OrthographicCamera camera, World world, LevelManager tm){
		this.camera = camera;
		this.world = world;
		this.tm = tm; 
		hudtarget = new HUDTarget();
		
		ch = new ContactHandler();
		cl = new DazContactListener(ch);
		world.setContactListener(cl);
	}
	
	public void print(String str){
		System.out.print(str);
	}
	public int item_poll = 0;
	public Item itemAtLoc(Vector2 vec){
		item_poll++;
		if (item_poll >= item.length) item_poll = 0;
			if (item[item_poll] != null){
				if (Math.abs(item[item_poll].worldpos.x - vec.x) < 2){
					if (Math.abs(item[item_poll].worldpos.y - vec.y) < 2){			
						return item[item_poll];
				}
		 	}
		 }
		return null;
	}
	
	public void createActor(int s, EnemySpawner es) {
		Vector2 startpos = new Vector2(0, 0);
		if (es != null){
		startpos = new Vector2(es.worldpos.x, es.worldpos.y);
		}
		long timenow = System.currentTimeMillis();
		
		if (s == 1) {
			zplayer = new HumanEntity(camera, world, false, tm, tm.playerstart, -1,
					this, "player");
		}
		if (s == 2) {

			
			if (es.enemyType.equals("footsoldier")){
				//wtfc();
			//	System.out.println();
				Weapon uzi = new Weapon(1);
				
				//long a = timenow - es.lasttimespawn;
				if (es.attemptSpawn(timenow)
						&& (zenemy[enemycount] == null
								|| zenemy[enemycount].isAlive == false || zenemy[enemycount].isDisposed)) {
					if (zenemy[enemycount] != null
							&& zenemy[enemycount].body != null) {
						System.out.println("destroying enemy body" + enemycount);
						zenemy[enemycount].reUseActor(startpos, uzi);
						System.out.println("Spawning Renewing Enemy:" + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
					} else {
						System.out.println("Spawning New Enemy:" + es.enemyType + "," + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
						/*
						 * I HAVE NO IDEA WHY I NEED TO PASS zplayer.tm INSTEAD OF
						 * tm, BUT IT WONT WORK OTHERWISE.
						 */
						zenemy[enemycount] = new HumanEntity(camera, world, true,
								tm, startpos, enemycount, 
								 this, es.enemyType);
					}
					es.lasttimespawn = System.currentTimeMillis();
					enemycount++;
					if (enemycount == ENEMY_LIMIT) {
						enemycount = 0;
					}
				}
			}else if (es.enemyType.equals("paratrooper")){
				Weapon uzi = new Weapon(1);
					//long a = timenow - es.lasttimespawn;
					if (es.attemptSpawn(timenow)
							&& (zenemy[enemycount] == null
									|| zenemy[enemycount].isAlive == false || zenemy[enemycount].isDisposed)) {
						if (zenemy[enemycount] != null
								&& zenemy[enemycount].body != null) {
							System.out.println("destroying enemy body" + enemycount);
							zenemy[enemycount].reUseActor(startpos, uzi);
							System.out.println("Spawning Renewing Enemy:" + enemycount
									+ " X:" + startpos.x + "Y" + startpos.y);
						} else {
							System.out.println("Spawning New Enemy:" + es.enemyType + "," + enemycount
									+ " X:" + startpos.x + "Y" + startpos.y);
							/*
							 * I HAVE NO IDEA WHY I NEED TO PASS zplayer.tm INSTEAD OF
							 * tm, BUT IT WONT WORK OTHERWISE.
							 */
							zenemy[enemycount] = new HumanEntity(camera, world, true,
									tm, startpos, enemycount, 
									 this, es.enemyType);
						}
						}
						es.lasttimespawn = System.currentTimeMillis();
						enemycount++;
						if (enemycount == ENEMY_LIMIT) {
							enemycount = 0;
						}
					}
				
			}else if (es.enemyType.equals("drone")){
				
				if (es.attemptSpawn(timenow)
						&& (drone[dronecount] == null
								|| drone[dronecount].isAlive == false )){//|| drone[dronecount].isDisposed)) {
					if (drone[dronecount] != null
							&& drone[dronecount].body != null) {
						System.out.println("destroying enemy body" + enemycount);
						drone[dronecount].reUseDrone(startpos);
						System.out.println("Spawning Renewing Enemy:" + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
					} else {
						System.out.println("Spawning New Enemy:" + es.enemyType + "," + enemycount
								+ " X:" + startpos.x + "Y" + startpos.y);
						/*
						 * I HAVE NO IDEA WHY I NEED TO PASS zplayer.tm INSTEAD OF
						 * tm, BUT IT WONT WORK OTHERWISE.
						 */
						
						drone[dronecount] = new Drone(startpos.x, startpos.y, world, dronecount, camera);
					}
					es.lasttimespawn = System.currentTimeMillis();
					dronecount++;
					if (dronecount == DRONE_LIMIT) {
						dronecount = 0;
					}
				}
			
		}
		
	}
	
	public void checkBodies(){

		/*
		 * DEACTIVATE DOORS, ENEMIES AND DESTROYED OBJECTS
		 */
		float damage = zplayer.weapon.damage;
		Array<HumanEntity> enemiesToDamage = ch.getEnemiesToDamage();
		if (enemiesToDamage != null){
			for (int i = 0; i < enemiesToDamage.size; i++){
				HumanEntity he = (HumanEntity) enemiesToDamage.get(i);
				prin.t("AI: " + he.id + " takes " + zplayer.weapon.damage + " damage.");
				he.takeDamage(damage);
			}
		}
		
		Array<Projectile> projToRemove = ch.getProjToRemove();
		if (projToRemove != null){
			for (int i = 0; i < projToRemove.size; i++){
				Projectile proj = (Projectile) projToRemove.get(i);
				prin.t("Kill Projectile:" + proj.id);
				proj.killProj();
			}
		}
		
		
		Array<Destroyable> destroyablesToDamage = ch.getDestroyablesToDamage();
		if (destroyablesToDamage != null){
			for (int i = 0; i < destroyablesToDamage.size; i++){
				Destroyable dest = (Destroyable) destroyablesToDamage.get(i);
				prin.t("Damaging Destroyable "+ dest.id);
				dest.damageDestroyable(damage);
				int killKeyValue = dest.id;
				zplayer.tm.keys[killKeyValue] = true;
				for (int j = 0; j < DOOR_LIMIT; j++) {
					if (door[j] != null) {
						if (zplayer.tm.keys[door[j].keyValue]) {
							door[j].openDoor();
						}
					}
				}
			}
		}
		
		Array<Drone> droneToDamage = ch.getDronesToDamage();
		if (droneToDamage != null){
			for (int i = 0; i< droneToDamage.size; i++){
				Drone drone = (Drone) droneToDamage.get(i);
				drone.takeDamage(damage);
			}
		}
		
		Array<CopterTurret> copterTurretToDamage = ch.getCopterTurretToDamage();
		if (copterTurretToDamage != null){
			for (int i = 0; i < copterTurretToDamage.size; i++){
				CopterTurret tur = (CopterTurret) copterTurretToDamage.get(i);
				tur.takeDamage(damage);
			}
		}
		
		ch.clearCollisions();

		}
		
	
	public void dispose(){
		for (int i = 0; i < ENEMY_LIMIT; i++) {
			zenemy[i].dispose();
		}
		humanSprite.dispose();
		zplayer.dispose();
	}

	public void Pickup(Item pickUpItem) {
		prin.t("player picking up " + pickUpItem.itemType);
		if (pickUpItem.isWeapon){
			int holdId = zplayer.weapon.weaponid;
			zplayer.weapon.weaponid = pickUpItem.itemWeaponNumber;
			pickUpItem.itemWeaponNumber = holdId;
			pickUpItem.dropWeapon(holdId);
		}else{
			if (pickUpItem.itemType.equals("health")){
				prin.t("Give player health" + pickUpItem.addHealth);
				zplayer.health += pickUpItem.addHealth;
				pickUpItem.removeItem();
			}
		}
		
	}
	
}
