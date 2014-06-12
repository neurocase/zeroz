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
import com.dazpetty.zeroz.entities.HUDTarget;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.managers.EntityManager;
import com.dazpetty.zeroz.managers.EventManager;
import com.dazpetty.zeroz.managers.ZeroContactListener;
import com.dazpetty.zeroz.managers.InputHandler;
import com.dazpetty.zeroz.managers.OrthoCamController;
import com.dazpetty.zeroz.managers.ParralaxCamera;
import com.dazpetty.zeroz.managers.ProjectileManager;
import com.dazpetty.zeroz.managers.LevelManager;
import com.dazpetty.zeroz.managers.SceneManager;
import com.dazpetty.zeroz.nodes.Door;
import com.dazpetty.zeroz.nodes.EntitySpawner;




public class WorldLogic {

	
	

	//public ProjectileManager projMan = new ProjectileManager(PROJECTILE_LIMIT);
	//public ProjectileManager aiProjMan = new ProjectileManager(PROJECTILE_LIMIT);
	
	public InputHandler inputHandler = new InputHandler();
	
	public EntityManager entityMan;
	
	
	
	//private TiledMapRenderer renderer;
	

	
	public OrthographicCamera camera;
	public World world;
	public LevelManager levelMan;
	public SceneManager scene;
	public EventManager eventMan;
	public DazDebug dazDebug;
	
	public WorldLogic(GameScreen gameScreen){
		this.entityMan = gameScreen.entityMan;
		this.camera = gameScreen.camera;
		this.world = gameScreen.world;
		this.levelMan = gameScreen.levelMan;
		this.scene = gameScreen.scene;
		this.eventMan = gameScreen.eventMan;
	
		entityMan.zplayer = new PawnEntity(entityMan, levelMan.getPlayerSpawner(),0);
		dazDebug.print("playerstart at x" + levelMan.playerstart.x + " y:" + levelMan.playerstart.y);
		playerSpawned = true;
		inputHandler.LoadInputHandler(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera, entityMan.zplayer);
	}
	
	
	
	
	
	public boolean playerSpawned = false;
	
	public void update(){
		
		if (!playerSpawned){
	
		}
		entityMan.zplayer.update(inputHandler.giveWorldPos, camera);
		eventMan.PollActuators();
		/*
		 * UPDATE PLAYER AND PROJECTILES
		 */
		//entityMan.createExplosion(7, 7, 0, 0);
		
		/*for (int i = 0; i < entityMan.TOTAL_EXPLOSIONS; i++){
			if (entityMan.explosion[i] != null && entityMan.explosion[i].isAlive){
				entityMan.explosion[i].update();
			}
		}*/
		
		scene.update();
		levelMan.update();
		
		for (int i = 0; i < scene.MOVER_LIMIT; i++){
			if (scene.mover[i] != null){
				//DazDebug.print("UPDATE MOVER [" + i + "]" );
				scene.mover[i].update();
			}
		}
		for (int i = 0; i < scene.CRUSHER_LIMIT; i++){
			if (scene.crusher[i] != null){
				//DazDebug.print("UPDATE MOVER [" + i + "]" );
				scene.crusher[i].update();
			}
		}
		
		
		boolean noTimers = true;
		for (int i = 0; i < scene.ZERO_TIMER_LIMIT; i++){
			if (scene.zerotimer[i] != null){
				//DazDebug.print("ZERO TIMER [" + i + "]" );
				scene.zerotimer[i].update();
				noTimers = false;
			}
		}
		if (noTimers){
		//	DazDebug.print("THERE ARE NO TIMERS");
	
		}

		for (int i = 0; i < scene.WALLTURRET_LIMIT; i++){
			if (scene.wallturret[i] != null){
				scene.wallturret[i].update(entityMan.zplayer);
			}
		}
		scene.aiProjMan.updateProjectiles();
		scene.projMan.updateProjectiles();
		Item pickUpItem = entityMan.itemAtLoc(entityMan.zplayer.worldpos);
		if (pickUpItem != null && pickUpItem.isAlive && entityMan.zplayer.isCrouching){
			entityMan.zplayer.Pickup(pickUpItem);
		}
		/*
		 * SPAWN ENEMIES AT SPAWNPOINT NEAR PLAYER
		 */
		if (pollCheck(11)){
		//	levelMan.checkSpawners();
		}
		
			
			/*
			 * UPDATE ENEMY AI
			 */
			if (pollCheck(6)){
				for (int i = 0; i < scene.ENEMY_LIMIT; i++) {
					if (scene.zenemy[i] != null) {
						scene.zenemy[i].updateAI(entityMan.zplayer);
					}
				}
				for (int i = 0; i < scene.ENEMY_LIMIT; i++) {
					if (scene.zenemy[i] != null) {
						boolean enemyAim = true;
						if (enemyAim) {
							scene.zenemy[i].update(true, camera);
						} else {
							scene.zenemy[i].update(true, camera);
						}
						// If too far away from player, dispose of enemy
						if (Math.abs((double) (scene.zenemy[i].worldpos.x - entityMan.zplayer.worldpos.x)) > 30
								|| (double) (scene.zenemy[i].worldpos.y - entityMan.zplayer.worldpos.y) > 30) {
							scene.zenemy[i].isDisposed = true;
							scene.zenemy[i].isAlive = false;
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

		int BIG_LIMIT = scene.ENEMY_LIMIT;
		if (scene.ENEMY_LIMIT < scene.DRONE_LIMIT) {
			BIG_LIMIT = scene.DRONE_LIMIT;
		}

		for (int i = 0; i < BIG_LIMIT; i++) {
			if (scene.zenemy[i] != null && scene.zenemy[i].isAlive
					&& scene.zenemy[i].worldpos.x > entityMan.zplayer.worldpos.x) {
				if (scene.zenemy[i].distanceFromPlayer < calcdist[0]) {
					calcdist[0] = scene.zenemy[i].distanceFromPlayer;
					closest_enemy[0] = i;
				}
			} else if (scene.zenemy[i] != null && scene.zenemy[i].isAlive
					&& scene.zenemy[i].worldpos.x < entityMan.zplayer.worldpos.x) {
				if (scene.zenemy[i].distanceFromPlayer < calcdist[1]) {
					calcdist[1] = scene.zenemy[i].distanceFromPlayer;
					closest_enemy[1] = i;
				}
			}
			if (scene.drone[i] != null && scene.drone[i].isAlive
					&& scene.drone[i].worldpos.x > entityMan.zplayer.worldpos.x) {
				if (scene.drone[i].distanceFromPlayer < calcdist[0]) {
					calcdist[0] = scene.drone[i].distanceFromPlayer;
					closest_enemy[0] = i;
					targetIsDrone[0] = true;
				}
			} else if (scene.drone[i] != null && scene.drone[i].isAlive
					&& scene.drone[i].worldpos.x < entityMan.zplayer.worldpos.x) {
				if (scene.drone[i].distanceFromPlayer < calcdist[1]) {
					calcdist[1] = scene.drone[i].distanceFromPlayer;
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
					if (scene.drone[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(scene.drone[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(scene.drone[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || scene.drone[closest_enemy[checkVal]] == null) {
						entityMan.zplayer.setTargetToNull();
						entityMan.hudtarget.dontDraw();
					}
				} else {
					if (scene.zenemy[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(scene.zenemy[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(scene.zenemy[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || scene.zenemy[closest_enemy[checkVal]] == null) {
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
					if (scene.drone[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(scene.drone[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(scene.drone[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || scene.drone[closest_enemy[checkVal]] == null) {
						entityMan.zplayer.setTargetToNull();
						entityMan.hudtarget.dontDraw();
					}
				}else{
					if (scene.zenemy[closest_enemy[checkVal]] != null) {
						entityMan.zplayer.giveQuickTarget(scene.zenemy[closest_enemy[checkVal]]);
						entityMan.hudtarget.setDrawTarget(scene.zenemy[closest_enemy[checkVal]]);
					}
					if (enemyTooFar[checkVal] || scene.zenemy[closest_enemy[checkVal]] == null) {
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
