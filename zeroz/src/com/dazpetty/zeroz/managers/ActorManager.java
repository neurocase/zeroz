package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.entities.Actor;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.Drone;
import com.dazpetty.zeroz.entities.EnemySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.HumanSprite;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.Weapon;

public class ActorManager {

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
	public Actor[] zenemy = new Actor[ENEMY_LIMIT];
	public Destroyable[] destroyable = new Destroyable[DESTROYABLE_LIMIT];
	public Door[] door = new Door[DOOR_LIMIT];
	public Item[] item = new Item[ITEM_LIMIT];
	public Drone[] drone = new Drone[DRONE_LIMIT];
	
	public HumanSprite humanSprite = new HumanSprite();
	
	public Actor zplayer;
	
	public OrthographicCamera camera;
	public World world;
	public HUDTarget hudtarget;
	public LevelManager tm;
	
	public DazContactListener cl;
	
	public ActorManager(OrthographicCamera camera, World world, LevelManager tm){
		this.camera = camera;
		this.world = world;
		this.tm = tm; 
		hudtarget = new HUDTarget();
		
		cl = new DazContactListener();
		world.setContactListener(cl);
	}
	
	
	
	public void createActor(int s, EnemySpawner es) {
		Vector2 startpos = new Vector2(0, 0);
		if (es != null){
		startpos = new Vector2(es.worldpos.x, es.worldpos.y);
		}
		long timenow = System.currentTimeMillis();
		
		if (s == 1) {
			zplayer = new Actor(camera, world, false, tm, tm.playerstart, -1,
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
						zenemy[enemycount] = new Actor(camera, world, true,
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
							zenemy[enemycount] = new Actor(camera, world, true,
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
				
				//long a = timenow - es.lasttimespawn;
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
	
	public void clearBodies(){
		
		/*
		 * DEACTIVATE DOORS, ENEMIES AND DESTROYED OBJECTS
		 */
	//	if (pollCheck(10)){
			
			
			Array<Body> projbodies = cl.getBodies();
			for (int i = 0; i < projbodies.size; i++) {
				Body b = projbodies.get(i);
				System.out.println("Destroying Proj:" + b.getUserData());
				projMan.KillProjectile((Integer) b.getUserData());
			}
			projbodies.clear();
			Array<Body> aiprojbodies = cl.getAiBodies();
			for (int i = 0; i < aiprojbodies.size; i++) {
				Body b = aiprojbodies.get(i);
				System.out.println("Destroying Ai Proj:" + b.getUserData());
				aiProjMan
						.KillProjectile((Integer) b.getUserData());
				if (cl.DamagePlayer()) {
					zplayer.takeDamage(5);
				}
			}
			aiprojbodies.clear();
	
			Array<Body> enemybodies = cl.getEnemies();
			for (int i = 0; i < enemybodies.size; i++) {
				Body b = enemybodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Enemy:" + b.getUserData());
				zenemy[t].isAlive = false;
				zenemy[t].body.setActive(false);
			}
			enemybodies.clear();	
			Array<Body> itembodies = cl.getItems();
			for (int i = 0; i < itembodies.size; i++) {
				Body b = itembodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Item :" + b.getUserData());
				if (item[t].isAlive) {
					if (item[t].itemType.equalsIgnoreCase("health")){
						zplayer.health += item[t].addHealth;
						item[t].isAlive = false;
						item[t].body.setActive(false);
						if (zplayer.health > 150) {
							zplayer.health = 150;
						}
					}
					if (item[t].itemType.equals("shotgun")){// && zplayer.isCrouching){
						System.out.println("PICKING UP SHOTGUN");
						System.out.println("PICKING UP SHOTGUN");
						System.out.println("PICKING UP SHOTGUN");
						item[t] = new Item(item[t].worldpos.x, item[t].worldpos.y, t, "uzi", world);
						zplayer.weapon.setWeapon(1);
						//item[t].isAlive = false;
						//item[t].body.setActive(false);
					}else if (item[t].itemType.equals("uzi")){// && zplayer.isCrouching){
						System.out.println("PICKING UP UZI");
						System.out.println("PICKING UP UZI");
						System.out.println("PICKING UP UZI");
						item[t] = new Item(item[t].worldpos.x, item[t].worldpos.y, t, "shotgun", world);
						zplayer.weapon.setWeapon(2);
						//item[t].isAlive = false;
						//item[t].body.setActive(false);
					}else{
						//wtfc();
					}
				}else{
					
				}
			}
			itembodies.clear();
	
			Array<Body> dronebodies = cl.getDrones();
			for (int i = 0; i < dronebodies.size; i++) {
				Body b = dronebodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Drone :" + b.getUserData());
				if (drone[t].isAlive && drone[t] != null) {
					drone[t].isAlive = false;
					drone[t].body.setActive(false);
				}
			}
			dronebodies.clear();
			
			Array<Body> turretbodies = cl.getCopterTurret();
			for (int i = 0; i < turretbodies.size; i++) {
				Body b = turretbodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Turret :" + b.getUserData());
				//Seems to be a bug here, or in the contact listener, where a body of
				// value over array limit will return
				if (t < copterBoss.TURRET_LIMIT){
					if (copterBoss.copterTurret[t] != null){
						if (copterBoss.copterTurret[t].isAlive && t <= turretbodies.size) {
							copterBoss.copterTurret[t].isAlive = false;
							copterBoss.copterTurret[t].body.setActive(false);
						}
					}
				}
			}
			turretbodies.clear();
	
			int killKeyValue = 0;
			Array<Body> destroybodies = cl.getDestroyables();
			for (int i = 0; i < destroybodies.size; i++) {
				Body b = destroybodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Destroyable:" + b.getUserData()
						+ "at Address:" + t);
				if (destroyable[t] != null) {
					destroyable[t].isAlive = false;
					destroyable[t].body.setActive(false);
					destroyable[t].Destroy();
					killKeyValue = destroyable[t].keyValue;
					zplayer.tm.keys[killKeyValue] = true;
					for (int j = 0; j < DOOR_LIMIT; j++) {
						if (door[j] != null) {
							if (zplayer.tm.keys[door[j].keyValue]) {
								door[j].openDoor();
							}
						}
					}
				} else {
					System.out.println("is NULL, destroyable.length ="
							+ destroyable.length);
				}
			}
			destroybodies.clear();
		}
		
	

	
	
	
	
	
	
	
	



	public void dispose(){
		for (int i = 0; i < ENEMY_LIMIT; i++) {
			zenemy[i].dispose();
		}
		humanSprite.dispose();
		zplayer.dispose();
	}
	
}
