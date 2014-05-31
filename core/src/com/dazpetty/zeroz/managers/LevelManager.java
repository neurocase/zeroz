package com.dazpetty.zeroz.managers;

import java.util.Arrays;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.core.GameScreen;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.EntitySpawner;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.Trigger;
import com.dazpetty.zeroz.entities.WorldVolume;

/*	The LevelManager contains functions for searching the tiled map filetype for playerstart , enemyspawner, door and  item locations.
 *  the levels may also contain special tiles which indicate whether the level is a boss level or a scrolling level (a level where the
 *  player is continuosly running forward), it also helps set up the level for collision
 * 
 *  
 */

public class LevelManager {

	private static final Object[] Vector2 = null;
	public TiledMapTileLayer collisionLayer;
	public TiledMapTileLayer nodeLayer;

	public TiledMap map;

	public int KEYS_LIMIT = 10;
	public boolean[] keys = new boolean[KEYS_LIMIT];

	public boolean isLevelScrolling = false;
	public boolean isBossLevel = false;
	public Vector2 playerstart = new Vector2(0, 0);

	
	public int enemyspawners = 0;
	
	
	public Vector2 levelcompletepos = new Vector2(0, 0);

	public boolean isLevelComplete = false;

	public EntityManager entityMan;
	public World world;
	
	
	

	public final int ENEMY_SPAWNER_LIMIT = 20;
	public EntitySpawner playerSpawner;
	public EntitySpawner enemyspawner[] = new EntitySpawner[ENEMY_SPAWNER_LIMIT];
	
	public int worldvolumecount = 0;
	public final int WORLD_VOLUME_LIMIT = 20;
	public WorldVolume worldvolume[] = new WorldVolume[WORLD_VOLUME_LIMIT];
	
	public EventManager eventMan;
	public SceneManager scene;
	
	public CellManager cellMan;
	

	public LevelManager(int level, GameScreen gameScreen, World world) {
		this.entityMan = gameScreen.entityMan;
		this.scene = gameScreen.scene;
		this.world = world;
		
		String levelstr = Integer.toString(level);
		map = new TmxMapLoader().load("data/levels/level" + levelstr + ".tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		nodeLayer = (TiledMapTileLayer) map.getLayers().get("miscLayer");
		
		cellMan = new CellManager(nodeLayer,collisionLayer);
		
		Arrays.fill(keys, Boolean.FALSE);
		 
		eventMan = new EventManager(this);
		gameScreen.eventMan = this.eventMan;
		
	}
	

	/*	----------------------------
	 *  FUNCTIONS FOR BUILDING LEVEL
	 *  ----------------------------
	 */
	




	
	
	/*	---------------------
	 *  BUILDING LEVEL
	 *  ---------------------
	 */

	public void buildLevel(EntityManager entityMan) {
		this.entityMan = entityMan;
		for (int h = 0; h < collisionLayer.getHeight(); h++) {
			for (int w = 0; w < collisionLayer.getWidth(); w++) {

				if (cellMan.isCellBlocked(w, h, false)) {
					int c = 0;
					while (cellMan.isCellBlocked(w + c, h, false)) {
						c++;
					}
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.5f));
					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.5f);
					groundBody.createFixture(groundBox, 0.0f);
					groundBody.setUserData("ground");
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 2;
					Fixture gfix = groundBody.createFixture(groundBox, 0.0f);
					gfix.setUserData("solid");
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
					groundBox.dispose();
				}
				if (cellMan.isCellDiagonal(w, h)) {

					BodyDef groundBodyDef = new BodyDef();
					
					String value = cellMan.getDiagonalValue(w, h);
					Body groundBody;
					
					System.out.println("value========" + value);
					ChainShape groundShape = new ChainShape();
					if (value.equals("left")){
						groundBodyDef.position.set(new Vector2(w, h + 1f));
						
						groundBody = world.createBody(groundBodyDef);
						groundShape.createChain(new Vector2[]{new Vector2(0,0),new Vector2(2,-1)});
						
					}else{
						groundBodyDef.position.set(new Vector2(w+1, h + 1f));
						
						groundBody = world.createBody(groundBodyDef);
						groundShape.createChain(new Vector2[]{new Vector2(0,0),new Vector2(-2,-1)});
					}
					
					
		
					groundBody.createFixture(groundShape, 0.0f);
					groundBody.setUserData("ground");
					
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundShape;
					fixtureDef.filter.categoryBits = 2;
					Fixture gfix = groundBody.createFixture(groundShape, 0.0f);
					gfix.setUserData("solid");
					groundShape.dispose();
				}
				if (cellMan.isCellPlatform(w, h)) {
					/*
					 * 	PLATFORMS ARE NOT WORKING
					 */
					boolean THIS_CODE_IS_FIXED = true;
					if (THIS_CODE_IS_FIXED){
					int c = 0;
					while (cellMan.isCellPlatform(w + c, h)) {
						c++;
					}
				
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.75f));
					groundBodyDef.type = groundBodyDef.type = BodyType.StaticBody;

					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.2f);
					
					
					FixtureDef fixtureDef = new FixtureDef();
					
					fixtureDef.filter.categoryBits = 4;
				//	fixtureDef.isSensor = true;
					fixtureDef.shape = groundBox;
					groundBody.createFixture(fixtureDef);
					groundBody.setUserData("platform");
					
					Fixture pfix = groundBody.createFixture(fixtureDef);
					//pfix.setSensor(true);
					pfix.setUserData("platform");
					
					
					
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
					groundBox.dispose();
					}
				}
				if (cellMan.isCellEnemySpawn(w, h)) {
					String type = (String) cellMan.getEnemyType(w, h);
					int rand = (int) (Math.random() * 10);
					if (rand == 0)
						rand = 1;
					int count = 0;
					count = cellMan.getCellCount(w, h);
					if (count == 0 || !cellMan.isCellCounter(w,h)){
						count = rand;
					}
				//	Cell cell = nodeLayer.getCell((int) (w), (int) (h));
				//	Trigger trigger = new Trigger(cell, w, h, cellMan);
					enemyspawner[enemyspawners] = new EntitySpawner(
							w, h, type,  count, cellMan.getCellTriggerValue(w, h), cellMan.getCellDelay(w,h), entityMan);
				/*	enemyspawner[enemyspawners] = new EntitySpawner(
							w, h, trigger, entityMan);*/
					DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
					System.out.println("Spawner Created of Type:" + type + "at"
							+ w + "," + h + " with " + count + " enemies");
					DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
					enemyspawners++;
				}
				if (cellMan.isCellDestroyable(w, h)) {
					
					if (scene.TOTAL_DESTROYABLES < scene.DESTROYABLE_LIMIT) {
						scene.destroyable[scene.TOTAL_DESTROYABLES] = new Destroyable(w, h,
								cellMan.getCellTriggerValue(w, h), world, eventMan);
						System.out.println("DESTROYABLE ADDED: "
								+ scene.TOTAL_DESTROYABLES);
						scene.TOTAL_DESTROYABLES++;

					}
				}
				if (cellMan.isCellDoor(w, h)) {
					if (scene.TOTAL_DOORS < scene.DOOR_LIMIT) {
						int value = cellMan.getCellTriggerValue(w, h);
						scene.door[scene.TOTAL_DOORS] = new Door(w, h,
								value, cellMan.getDoorType(w,h), cellMan.getInstanceType(w,h),  cellMan.getCellDelay(w,h), world);
						scene.TOTAL_DOORS++;
						System.out.println("DOOR ADDED: "
								+ scene.TOTAL_DOORS);
					}
				}
				if (cellMan.isCellItem(w, h)) {
					if (scene.TOTAL_ITEMS < scene.ITEM_LIMIT) {
						String value = cellMan.getItemValue(w, h);
						scene.item[scene.TOTAL_ITEMS] = new Item(w, h,
								scene.TOTAL_ITEMS, value);
						scene.TOTAL_ITEMS++;
						System.out.println(value + " ITEM ADDED: "
								+ scene.TOTAL_ITEMS + "at:" + w + "," + h);
					}
				}
				if (cellMan.isCellLevelComplete(w, h)) {
					System.out.println("LEVEL COMPLETE AT: x:" + w + "y:" + h);
					levelcompletepos.x = w;
					levelcompletepos.y = h;
				}
				if (cellMan.isCellPlayerStart(w, h)) {
					System.out.println("PlayerStart at: x" + w + "y:" + h);
					/*Cell cell = nodeLayer.getCell((int) (w), (int) (h));
					Trigger trigger = new Trigger(cell, w, h, cellMan);
					trigger.enemySpawnType = "player";
					playerStart = new EntitySpawner(w, h, trigger, entityMan);*/
					playerStart = new EntitySpawner(w, h, "player", 1, 0, 0, entityMan);
				}
				if (cellMan.isCellWorldVolume(w, h)) {
				
					String type = cellMan.getInstanceType(w, h);
					DazDebug.print("++++WORLDVOLUME AT: X:" + w + " Y:" + h + " TYPE:" + type);
					if (worldvolumecount < WORLD_VOLUME_LIMIT ){
						worldvolume[worldvolumecount] = new WorldVolume(w,h,type,cellMan.getCellTriggerValue(w,h), world,this);
						worldvolumecount++;
					}
					DazDebug.print("++++WORLDVOLUME AT: X:" + w + " Y:" + h + " TYPE:" + type);
				}
		/*		if (!isLevelScrolling) {
					if (isLevelScrolling(w, h)) {
						isLevelScrolling = true;
					}
				}
				if (!isBossLevel) {
					if (isCellBoss(w, h)) {
						isBossLevel = true;
						scene.copterBoss = new CopterBoss(w, h, world);
					}
				}*/

			}
		}
	}

	EntitySpawner playerStart;

	
	public EntitySpawner getPlayerSpawner() {
		return playerStart;
		
	}
/*
	public void checkSpawners() {

		for (int i = 0; i < enemyspawners; i++) {
			if (Math.abs((double) (enemyspawner[i].worldpos.x - entityMan.zplayer.worldpos.x)) < 16) {
				if (enemyspawner[i] != null ){
					enemyspawner[i].createActor();
				} else {
					System.out.println("ERROR: There are no enemy spawners");
				}
			}
		}
	
	}*/

}
