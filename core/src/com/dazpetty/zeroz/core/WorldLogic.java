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
import com.dazpetty.zeroz.entities.PawnEntity;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.EntitySpawner;
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.managers.EntityManager;
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
	
	public EntityManager entityMan;
	
	//private TiledMapRenderer renderer;
	

	
	public OrthographicCamera camera;
	public World world;
	public LevelManager levelMan;
	
	public WorldLogic(OrthographicCamera camera, EntityManager entityMan, World world, LevelManager levelMan){
		this.entityMan = entityMan;
		this.camera = camera;
		this.world = world;
		this.levelMan = levelMan;
	
		entityMan.zplayer = new PawnEntity(entityMan, levelMan.getPlayerSpawner());
		DazDebug.print("playerstart at x" + levelMan.playerstart.x + " y:" + levelMan.playerstart.y);
		playerSpawned = true;
		inputHandler.LoadInputHandler(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera, entityMan.zplayer);
	}
	
	
	
	
	
	public boolean playerSpawned = false;
	
	public void update(){
		
		if (!playerSpawned){
	
		}
		entityMan.zplayer.update(inputHandler.giveWorldPos, camera);
		
		/*
		 * UPDATE PLAYER AND PROJECTILES
		 */
		//entityMan.createExplosion(7, 7, 0, 0);
		
		/*for (int i = 0; i < entityMan.TOTAL_EXPLOSIONS; i++){
			if (entityMan.explosion[i] != null && entityMan.explosion[i].isAlive){
				entityMan.explosion[i].update();
			}
		}*/

		
		entityMan.aiProjMan.updateProjectiles();
		entityMan.projMan.updateProjectiles();
		Item pickUpItem = entityMan.itemAtLoc(entityMan.zplayer.worldpos);
		if (pickUpItem != null && pickUpItem.isAlive && entityMan.zplayer.isCrouching){
			entityMan.zplayer.Pickup(pickUpItem);
		}
		/*
		 * SPAWN ENEMIES AT SPAWNPOINT NEAR PLAYER
		 */
		if (pollCheck(11)){
			levelMan.checkSpawners();
		}
		
			
			/*
			 * UPDATE ENEMY AI
			 */
			if (pollCheck(6)){
				for (int i = 0; i < entityMan.ENEMY_LIMIT; i++) {
					if (entityMan.zenemy[i] != null) {
						entityMan.zenemy[i].updateAI(entityMan.zplayer);
					}
				}
				for (int i = 0; i < entityMan.ENEMY_LIMIT; i++) {
					if (entityMan.zenemy[i] != null) {
						boolean enemyAim = true;
						if (enemyAim) {
							entityMan.zenemy[i].update(true, camera);
						} else {
							entityMan.zenemy[i].update(true, camera);
						}
						// If too far away from player, dispose of enemy
						if (Math.abs((double) (entityMan.zenemy[i].worldpos.x - entityMan.zplayer.worldpos.x)) > 30
								|| (double) (entityMan.zenemy[i].worldpos.y - entityMan.zplayer.worldpos.y) > 30) {
							entityMan.zenemy[i].isDisposed = true;
							entityMan.zenemy[i].isAlive = false;
						}
					}
				}
			}
		
			entityMan.checkBodies();
			huntClosestEnemy();
			
			
		if (entityMan.zplayer.worldpos.y < -15) {
			entityMan.zplayer.health = 0;
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

		int BIG_LIMIT = entityMan.ENEMY_LIMIT;
		if (entityMan.ENEMY_LIMIT < entityMan.DRONE_LIMIT) {
			BIG_LIMIT = entityMan.DRONE_LIMIT;
		}

		for (int i = 0; i < BIG_LIMIT; i++) {
			if (entityMan.zenemy[i] != null && entityMan.zenemy[i].isAlive
					&& entityMan.zenemy[i].worldpos.x > entityMan.zplayer.worldpos.x) {
				if (entityMan.zenemy[i].distanceFromPlayer < calcdist[0]) {
					calcdist[0] = entityMan.zenemy[i].distanceFromPlayer;
					closest_enemy[0] = i;
				}
			} else if (entityMan.zenemy[i] != null && entityMan.zenemy[i].isAlive
					&& entityMan.zenemy[i].worldpos.x < entityMan.zplayer.worldpos.x) {
				if (entityMan.zenemy[i].distanceFromPlayer < calcdist[1]) {
					calcdist[1] = entityMan.zenemy[i].distanceFromPlayer;
					closest_enemy[1] = i;
				}
			}
			if (entityMan.drone[i] != null && entityMan.drone[i].isAlive
					&& entityMan.drone[i].worldpos.x > entityMan.zplayer.worldpos.x) {
				if (entityMan.drone[i].distanceFromPlayer < calcdist[0]) {
					calcdist[0] = entityMan.drone[i].distanceFromPlayer;
					closest_enemy[0] = i;
					targetIsDrone[0] = true;
				}
			} else if (entityMan.drone[i] != null && entityMan.drone[i].isAlive
					&& entityMan.drone[i].worldpos.x < entityMan.zplayer.worldpos.x) {
				if (entityMan.drone[i].distanceFromPlayer < calcdist[1]) {
					calcdist[1] = entityMan.drone[i].distanceFromPlayer;
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
		if (!entityMan.zplayer.isOnLadder && !levelMan.isLevelScrolling) {
			
			int checkVal = 0;
			if (!entityMan.zplayer.isGoRight) checkVal = 1;
				if (targetIsDrone[checkVal]) {
					if (entityMan.drone[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(entityMan.drone[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(entityMan.drone[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || entityMan.drone[closest_enemy[checkVal]] == null) {
						entityMan.zplayer.setTargetToNull();
						entityMan.hudtarget.dontDraw();
					}
				} else {
					if (entityMan.zenemy[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(entityMan.zenemy[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(entityMan.zenemy[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || entityMan.zenemy[closest_enemy[checkVal]] == null) {
						entityMan.zplayer.setTargetToNull();
						entityMan.hudtarget.dontDraw();
					}
				}
				
			
		} else {
			int checkVal = 0;
			if (calcdist[0] > calcdist[1]) {
				checkVal = 1;
				
			}
				
				if (targetIsDrone[checkVal]) {
					if (entityMan.drone[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(entityMan.drone[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(entityMan.drone[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || entityMan.drone[closest_enemy[checkVal]] == null) {
						entityMan.zplayer.setTargetToNull();
						entityMan.hudtarget.dontDraw();
					}
				}else{
					if (entityMan.zenemy[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(entityMan.zenemy[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(entityMan.zenemy[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || entityMan.zenemy[closest_enemy[checkVal]] == null) {
						entityMan.zplayer.setTargetToNull();
						entityMan.hudtarget.dontDraw();
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
