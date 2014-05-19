package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.entities.Actor;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.EnemySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.managers.ActorManager;
import com.dazpetty.zeroz.managers.DazContactListener;
import com.dazpetty.zeroz.managers.InputHandler;
import com.dazpetty.zeroz.managers.OrthoCamController;
import com.dazpetty.zeroz.managers.ParralaxCamera;
import com.dazpetty.zeroz.managers.ProjectileManager;
import com.dazpetty.zeroz.managers.LevelManager;




public class WorldLogic {

	
	

	//public ProjectileManager projMan = new ProjectileManager(PROJECTILE_LIMIT);
	//public ProjectileManager aiProjMan = new ProjectileManager(PROJECTILE_LIMIT);
	
	public InputHandler inputHandler = new InputHandler();
	
	public ActorManager actorMan;
	
	private TiledMapRenderer renderer;
	
	public DazContactListener cl;
	
	public OrthographicCamera camera;
	public World world;
	public LevelManager tm;
	
	public WorldLogic(OrthographicCamera camera, ActorManager actorMan, World world, LevelManager tm){
		
		
		this.actorMan = actorMan;
		this.camera = camera;
		this.world = world;
		this.tm = tm;
		/*
		 * SETUP CAMERA AND RENDERER
		 */
		// Tiled layer manager for cell (not box2d) based collision.
	
	
		
		/*
		 * SETUP WORLD AND COLLISIONS
		 */
		cl = new DazContactListener();
		world.setContactListener(cl);
		for (int h = 0; h < tm.collisionLayer.getHeight(); h++) {
			for (int w = 0; w < tm.collisionLayer.getWidth(); w++) {

				if (tm.isCellBlocked(w, h, false)) {

					int c = 0;
					while (tm.isCellBlocked(w + c, h, false)) {
						c++;
					}
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.5f));
					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.5f);
					groundBody.createFixture(groundBox, 0.0f);
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 2;
					Fixture gfix = groundBody.createFixture(groundBox, 0.0f);
					gfix.setUserData("ground");
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
				}
				if (tm.isCellPlatform(w, h)) {
					int c = 0;
					while (tm.isCellPlatform(w + c, h)) {
						c++;
					}
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.75f));
					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.2f);
					groundBody.createFixture(groundBox, 0.0f);
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 1;
					Fixture pfix = groundBody.createFixture(fixtureDef);
					pfix.setUserData("platform");
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
				}
				if (tm.isCellEnemySpawn(w, h)) {
					String type = (String) tm.getEnemyType(w,h);
					int rand = (int) (Math.random() * 10);
					if (rand == 0) rand = 1;
					actorMan.enemyspawner[actorMan.enemyspawners] = new EnemySpawner(w, h, type,rand);
					System.out.println("Spawner Created of Type:" + type + "at" + w + "," + h + " with " + rand + " enemies");
					actorMan.enemyspawners++;
				}
				if (tm.isCellDestroyable(w, h)) {
					int value = tm.getCellValue(w, h);
					if (actorMan.TOTAL_DESTROYABLES < actorMan.DESTROYABLE_LIMIT) {
						actorMan.destroyable[value] = new Destroyable(w, h, value, world);
						System.out.println("DESTROYABLE ADDED: "
								+ actorMan.TOTAL_DESTROYABLES);
						actorMan.TOTAL_DESTROYABLES++;

					}
				}
				if (tm.isCellDoor(w, h)) {
					if (actorMan.TOTAL_DOORS < actorMan.DOOR_LIMIT) {
						int value = tm.getCellValue(w, h);
						actorMan.door[actorMan.TOTAL_DOORS] = new Door(w, h, value, world);
						actorMan.TOTAL_DOORS++;
						System.out.println("DOOR ADDED: " + actorMan.TOTAL_DOORS);
					}
				}
				if (tm.isCellItem(w, h)) {
					if (actorMan.TOTAL_ITEMS < actorMan.ITEM_LIMIT) {
						String value = tm.getItemValue(w, h);
						actorMan.item[actorMan.TOTAL_ITEMS] = new Item(w, h, actorMan.TOTAL_ITEMS, value, world);
						actorMan.TOTAL_ITEMS++;
						System.out.println(value + " ITEM ADDED: "
								+ actorMan.TOTAL_ITEMS + "at:" + w + "," + h);
					}
				}
				if (tm.isCellLevelComplete(w, h)) {
					System.out.println("LEVEL COMPLETE AT: x:" + w + "y:" + h);
					tm.levelcompletepos.x = w;
					tm.levelcompletepos.y = h;
				}
				if (tm.isCellPlayerStart(w, h)) {
					System.out.println("PlayerStart at: x" + w + "y:" + h);
					tm.playerstart.x = w;
					tm.playerstart.y = h;
				}
				if (!tm.isLevelScrolling){
					if (tm.isLevelScrolling(w, h)) {
						tm.isLevelScrolling = true;
					}
				}
				if (!tm.isBossLevel){
					if (tm.isCellBoss(w, h)) {
						tm.isBossLevel = true;
						actorMan.copterBoss = new CopterBoss(w, h, world);
						
					}
				}
		
		
		
			}
		
		}
		// CREATE PLAYER
		actorMan.zplayer = new Actor(camera, world, false, tm,  tm.playerstart, -1,
				actorMan, "player");
		//actorMan.createActor(1,actorMan.es.enemyspawner);
		// INPUTHANDLER
		
		
		inputHandler.LoadInputHandler(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera, actorMan.zplayer);
	
		
	}
	
	
	
	
	
	
	
	public void update(){
		/*
		 * UPDATE PLAYER AND PROJECTILES
		 */

		actorMan.aiProjMan.updateProjectiles();
		actorMan.projMan.updateProjectiles();
	
		/*
		 * SPAWN ENEMIES AT SPAWNPOINT NEAR PLAYER
		 */
	//	if (pollCheck(11)){
			for (int i = 0; i < actorMan.enemyspawners; i++) {
				if (Math.abs((double) (actorMan.enemyspawner[i].worldpos.x - actorMan.zplayer.worldpos.x)) < 20) {
					if (actorMan.enemyspawner[i] != null ){//&& actorMan.enemyspawner[i].enemyType == "footsoldier") {
						actorMan.createActor(2, actorMan.enemyspawner[i]);
					} else {
						System.out.println("ERROR: There are no enemy spawners");
					}
				}
			}
			
			/*
			 * UPDATE ENEMY AI
			 */
			if (pollCheck(6)){
				for (int i = 0; i < actorMan.ENEMY_LIMIT; i++) {
					if (actorMan.zenemy[i] != null) {
						actorMan.zenemy[i].updateAI(actorMan.zplayer);
					}
				}
				for (int i = 0; i < actorMan.ENEMY_LIMIT; i++) {
					if (actorMan.zenemy[i] != null) {
						boolean enemyAim = true;
						if (enemyAim) {
							actorMan.zenemy[i].update(true, camera, true);
						} else {
							actorMan.zenemy[i].update(true, camera, true);
						}
						// If too far away from player, dispose of enemy
						if (Math.abs((double) (actorMan.zenemy[i].worldpos.x - actorMan.zplayer.worldpos.x)) > 30
								|| (double) (actorMan.zenemy[i].worldpos.y - actorMan.zplayer.worldpos.y) > 30) {
							actorMan.zenemy[i].isDisposed = true;
							actorMan.zenemy[i].isAlive = false;
						}
					}
				}
			}
		
		
		/*
		 * DEACTIVATE DOORS, ENEMIES AND DESTROYED OBJECTS
		 */
		if (pollCheck(10)){
			huntClosestEnemy();
			
			Array<Body> projbodies = cl.getBodies();
			for (int i = 0; i < projbodies.size; i++) {
				Body b = projbodies.get(i);
				System.out.println("Destroying Proj:" + b.getUserData());
				actorMan.projMan.KillProjectile((Integer) b.getUserData());
			}
			projbodies.clear();
			Array<Body> aiprojbodies = cl.getAiBodies();
			for (int i = 0; i < aiprojbodies.size; i++) {
				Body b = aiprojbodies.get(i);
				System.out.println("Destroying Ai Proj:" + b.getUserData());
				actorMan.aiProjMan
						.KillProjectile((Integer) b.getUserData());
				if (cl.DamagePlayer()) {
					actorMan.zplayer.takeDamage(5);
				}
			}
			aiprojbodies.clear();
	
			Array<Body> enemybodies = cl.getEnemies();
			for (int i = 0; i < enemybodies.size; i++) {
				Body b = enemybodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Enemy:" + b.getUserData());
				actorMan.zenemy[t].isAlive = false;
				actorMan.zenemy[t].body.setActive(false);
			}
			enemybodies.clear();	
			Array<Body> itembodies = cl.getItems();
			for (int i = 0; i < itembodies.size; i++) {
				Body b = itembodies.get(i);
				int t = (Integer) b.getUserData();
				System.out.println("Destroying Item :" + b.getUserData());
				if (actorMan.item[t].isAlive) {
					if (actorMan.item[t].itemType.equalsIgnoreCase("health")){
						actorMan.zplayer.health += actorMan.item[t].addHealth;
						actorMan.item[t].isAlive = false;
						actorMan.item[t].body.setActive(false);
						if (actorMan.zplayer.health > 150) {
							actorMan.zplayer.health = 150;
						}
					}
					if (actorMan.item[t].itemType.equals("shotgun")){// && zplayer.isCrouching){
						System.out.println("PICKING UP SHOTGUN");
						System.out.println("PICKING UP SHOTGUN");
						System.out.println("PICKING UP SHOTGUN");
						actorMan.item[t] = new Item(actorMan.item[t].worldpos.x, actorMan.item[t].worldpos.y, t, "uzi", world);
						actorMan.zplayer.weapon.setWeapon(1);
						//item[t].isAlive = false;
						//item[t].body.setActive(false);
					}else if (actorMan.item[t].itemType.equals("uzi")){// && zplayer.isCrouching){
						System.out.println("PICKING UP UZI");
						System.out.println("PICKING UP UZI");
						System.out.println("PICKING UP UZI");
						actorMan.item[t] = new Item(actorMan.item[t].worldpos.x, actorMan.item[t].worldpos.y, t, "shotgun", world);
						actorMan.zplayer.weapon.setWeapon(2);
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
				if (actorMan.drone[t].isAlive && actorMan.drone[t] != null) {
					actorMan.drone[t].isAlive = false;
					actorMan.drone[t].body.setActive(false);
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
				if (t < actorMan.copterBoss.TURRET_LIMIT){
					if (actorMan.copterBoss.copterTurret[t] != null){
						if (actorMan.copterBoss.copterTurret[t].isAlive && t <= turretbodies.size) {
							actorMan.copterBoss.copterTurret[t].isAlive = false;
							actorMan.copterBoss.copterTurret[t].body.setActive(false);
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
				if (actorMan.destroyable[t] != null) {
					actorMan.destroyable[t].isAlive = false;
					actorMan.destroyable[t].body.setActive(false);
					actorMan.destroyable[t].Destroy();
					killKeyValue = actorMan.destroyable[t].keyValue;
					actorMan.zplayer.tm.keys[killKeyValue] = true;
					for (int j = 0; j < actorMan.DOOR_LIMIT; j++) {
						if (actorMan.door[j] != null) {
							if (actorMan.zplayer.tm.keys[actorMan.door[j].keyValue]) {
								actorMan.door[j].openDoor();
							}
						}
					}
				} else {
					System.out.println("is NULL, destroyable.length ="
							+ actorMan.destroyable.length);
				}
			}
			destroybodies.clear();
		}
		if (actorMan.zplayer.worldpos.y < -15) {
			actorMan.zplayer.health = 0;
		}
		
		if (Gdx.input.isTouched()) {
			
		}
		
		
	
	}
	
	public void huntClosestEnemy(){
		/*
		 * TODO: CYCLE THROUGH ENEMY ACTORS AND GIVE PLAYER CLOSEST ONE FOR
		 * QUICK SHOOT
		 */
		/*
		 * 
		 *  NEED TO FIX HERE, CODE IS UGLY, WANT TO REFACTOR
		 */
		
		float targetPosX = 0;
		float targetPosY = 0;
		boolean drawTarget = false;

		
		//change these into arrays, 0 && 1 to flip
		//right = 0, left = 1
		
		
		int closest_enemy[] = {0,0};
		//int closest_enemy_left[] = 0;
		
		float calcdist[] = {999,999};
		//float calcdist_left[] = 999;
		boolean enemyTooFar[] = {false,false};
		//boolean enemyTooFar_left[] = false;
		boolean targetIsDrone[] = {false,false};
		//boolean targetIsDrone_left[] = false;

		int BIG_LIMIT = actorMan.ENEMY_LIMIT;
		if (actorMan.ENEMY_LIMIT < actorMan.DRONE_LIMIT) {
			BIG_LIMIT = actorMan.DRONE_LIMIT;
		}

		for (int i = 0; i < BIG_LIMIT; i++) {
			if (actorMan.zenemy[i] != null && actorMan.zenemy[i].isAlive
					&& actorMan.zenemy[i].worldpos.x > actorMan.zplayer.worldpos.x) {
				if (actorMan.zenemy[i].distanceFromPlayer < calcdist[0]) {
					calcdist[0] = actorMan.zenemy[i].distanceFromPlayer;
					closest_enemy[0] = i;
				}
			} else if (actorMan.zenemy[i] != null && actorMan.zenemy[i].isAlive
					&& actorMan.zenemy[i].worldpos.x < actorMan.zplayer.worldpos.x) {
				if (actorMan.zenemy[i].distanceFromPlayer < calcdist[1]) {
					calcdist[1] = actorMan.zenemy[i].distanceFromPlayer;
					closest_enemy[1] = i;
				}
			}
			if (actorMan.drone[i] != null && actorMan.drone[i].isAlive
					&& actorMan.drone[i].worldpos.x > actorMan.zplayer.worldpos.x) {
				if (actorMan.drone[i].distanceFromPlayer < calcdist[0]) {
					calcdist[0] = actorMan.drone[i].distanceFromPlayer;
					closest_enemy[0] = i;
					targetIsDrone[0] = true;
				}
			} else if (actorMan.drone[i] != null && actorMan.drone[i].isAlive
					&& actorMan.drone[i].worldpos.x < actorMan.zplayer.worldpos.x) {
				if (actorMan.drone[i].distanceFromPlayer < calcdist[1]) {
					calcdist[1] = actorMan.drone[i].distanceFromPlayer;
					closest_enemy[1] = i;
					targetIsDrone[1] = true;
				}
			}

		}
		if (calcdist[0] > 9) {
			enemyTooFar[0] = true;

		} else {
			enemyTooFar[0] = false;
		}
		if (calcdist[1] > 9) {
			enemyTooFar[1] = true;

		} else {
			enemyTooFar[1] = false;
		}

		
	//	boolean boolCheck = targetIsDrone[0];
		if (!actorMan.zplayer.isOnLadder && !tm.isLevelScrolling) {
			if (actorMan.zplayer.isGoRight) {
				if (targetIsDrone[0]) {
					if (actorMan.drone[closest_enemy[0]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.drone[closest_enemy[0]]);
						actorMan.hudtarget.setDrawTarget(actorMan.drone[closest_enemy[0]]);
					}
					if (enemyTooFar[0] || actorMan.drone[closest_enemy[0]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				} else {
					if (actorMan.zenemy[closest_enemy[0]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.zenemy[closest_enemy[0]]);
						actorMan.hudtarget.setDrawTarget(actorMan.zenemy[closest_enemy[0]]);
					}
					if (enemyTooFar[0] || actorMan.zenemy[closest_enemy[0]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}
				
			} else {
				if (targetIsDrone[1]) {
					if (actorMan.drone[closest_enemy[1]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.drone[closest_enemy[1]]);
						actorMan.hudtarget.setDrawTarget(actorMan.drone[closest_enemy[1]]);
						
					}
					if (enemyTooFar[1] || actorMan.drone[closest_enemy[1]] == null) {
						actorMan.zplayer.setTargetToNull();
					}
				} else {
					if (actorMan.zenemy[closest_enemy[1]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.zenemy[closest_enemy[1]]);
						actorMan.hudtarget.setDrawTarget(actorMan.zenemy[closest_enemy[1]]);
					}
					if (enemyTooFar[1] || actorMan.zenemy[closest_enemy[1]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}
			}
		} else {
			if (calcdist[0] < calcdist[1]) {
				if (targetIsDrone[0]) {
					if (actorMan.drone[closest_enemy[0]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.drone[closest_enemy[0]]);
						actorMan.hudtarget.setDrawTarget(actorMan.drone[closest_enemy[0]]);
					}
					if (enemyTooFar[0] || actorMan.drone[closest_enemy[0]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}else{
					if (actorMan.zenemy[closest_enemy[0]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.zenemy[closest_enemy[0]]);
						actorMan.hudtarget.setDrawTarget(actorMan.zenemy[closest_enemy[0]]);
					}
					if (enemyTooFar[0] || actorMan.zenemy[closest_enemy[0]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}
			} else {
				if (targetIsDrone[1]) {
					if (actorMan.drone[closest_enemy[1]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.drone[closest_enemy[1]]);
						actorMan.hudtarget.setDrawTarget(actorMan.drone[closest_enemy[1]]);
					}
					if (enemyTooFar[0] || actorMan.drone[closest_enemy[1]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}else{
					if (actorMan.zenemy[closest_enemy[1]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.zenemy[closest_enemy[1]]);
						actorMan.hudtarget.setDrawTarget(actorMan.zenemy[closest_enemy[1]]);
					}
					if (enemyTooFar[1] || actorMan.zenemy[closest_enemy[1]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}
			}
		}
	}
	
	public int loopcount = 0;
	public boolean pollCheck(int v){
		if (loopcount % v == 0){
			return true;
		}
		return false;
	}
	
	
	
}
