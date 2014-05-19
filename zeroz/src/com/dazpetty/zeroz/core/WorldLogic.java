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
		
			actorMan.clearBodies();
			huntClosestEnemy();
			
			
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
			
			int checkVal = 0;
			if (!actorMan.zplayer.isGoRight) checkVal = 1;
				if (targetIsDrone[checkVal]) {
					if (actorMan.drone[closest_enemy[checkVal]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.drone[closest_enemy[checkVal]]);
						actorMan.hudtarget.setDrawTarget(actorMan.drone[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || actorMan.drone[closest_enemy[checkVal]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				} else {
					if (actorMan.zenemy[closest_enemy[checkVal]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.zenemy[closest_enemy[checkVal]]);
						actorMan.hudtarget.setDrawTarget(actorMan.zenemy[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || actorMan.zenemy[closest_enemy[checkVal]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}
				
			
		} else {
			int checkVal = 0;
			if (calcdist[0] > calcdist[1]) {
				checkVal = 1;
				
			}
				
				if (targetIsDrone[checkVal]) {
					if (actorMan.drone[closest_enemy[checkVal]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.drone[closest_enemy[checkVal]]);
						actorMan.hudtarget.setDrawTarget(actorMan.drone[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || actorMan.drone[closest_enemy[checkVal]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
					}
				}else{
					if (actorMan.zenemy[closest_enemy[checkVal]] != null) {
						actorMan.zplayer.giveQuickTarget(actorMan.zenemy[closest_enemy[checkVal]]);
						actorMan.hudtarget.setDrawTarget(actorMan.zenemy[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || actorMan.zenemy[closest_enemy[checkVal]] == null) {
						actorMan.zplayer.setTargetToNull();
						actorMan.hudtarget.dontDraw();
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
