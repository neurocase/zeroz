package com.dazpetty.zeroz.managers;

import java.util.Arrays;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.core.GameScreen;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Crate;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.FlamerTurret;
import com.dazpetty.zeroz.entities.Item;
import com.dazpetty.zeroz.entities.WallTurret;
import com.dazpetty.zeroz.nodes.AnimatedTile;
import com.dazpetty.zeroz.nodes.Chain;
import com.dazpetty.zeroz.nodes.Crusher;
import com.dazpetty.zeroz.nodes.Door;
import com.dazpetty.zeroz.nodes.EntitySpawner;
import com.dazpetty.zeroz.nodes.Mover;
import com.dazpetty.zeroz.nodes.PowerCable;
import com.dazpetty.zeroz.nodes.Trigger;
import com.dazpetty.zeroz.nodes.WorldVolume;
import com.dazpetty.zeroz.nodes.ZeroTimer;

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
	public TiledMapTileLayer animatedLayer;

	public TiledMap map;
	//	map = new TmxMapLoader().load("data/levels/level" + levelstr + ".tmx");

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

	public int animTileCount = 0;
	public final int ANIMATED_TILES_LIMIT = 128;
	public final int ENEMY_SPAWNER_LIMIT = 20;
	public EntitySpawner playerSpawner;
	public EntitySpawner enemyspawner[] = new EntitySpawner[ENEMY_SPAWNER_LIMIT];

	public int worldvolumecount = 0;
	public final int WORLD_VOLUME_LIMIT = 20;
	public WorldVolume worldvolume[] = new WorldVolume[WORLD_VOLUME_LIMIT];

	public EventManager eventMan;
	public SceneManager scene;
	public CellManager cellMan;
	
	public AnimatedTile animatedTiles[]  = new AnimatedTile[ANIMATED_TILES_LIMIT];
	

	public int animatedTilesCount = 0;
	
	//public AnimatedTiledMapTile animatedTiles[] = new AnimatedTiledMapTile[ANIMATED_TILES_LIMIT];

	public LevelManager(int level, GameScreen gameScreen, World world) {
		this.entityMan = gameScreen.entityMan;
		this.scene = gameScreen.scene;
		this.world = world;
		
	
		
		String levelstr = Integer.toString(level);
		map = new TmxMapLoader().load("data/levels/level" + levelstr + ".tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		nodeLayer = (TiledMapTileLayer) map.getLayers().get("nodelayer");
		animatedLayer = (TiledMapTileLayer) map.getLayers().get("animlayer");

		cellMan = new CellManager(nodeLayer, collisionLayer, animatedLayer);
		//animTileMan = new AnimatedTilesManager(cellMan, scene, map);
		
		Arrays.fill(keys, Boolean.FALSE);
		

		eventMan = new EventManager(this);
		gameScreen.eventMan = this.eventMan;

	}

	/*
	 * ---------------------------- FUNCTIONS FOR BUILDING LEVEL
	 * ----------------------------
	 */

	/*
	 * --------------------- BUILDING LEVEL ---------------------
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
					if (value.equals("left")) {
						groundBodyDef.position.set(new Vector2(w, h + 1f));

						groundBody = world.createBody(groundBodyDef);
						groundShape.createChain(new Vector2[] {
								new Vector2(0, 0), new Vector2(2, -1) });

					} else {
						groundBodyDef.position.set(new Vector2(w + 1, h + 1f));

						groundBody = world.createBody(groundBodyDef);
						groundShape.createChain(new Vector2[] {
								new Vector2(0, 0), new Vector2(-2, -1) });
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
					 * PLATFORMS ARE NOT WORKING
					 */
					boolean THIS_CODE_IS_FIXED = true;
					if (THIS_CODE_IS_FIXED) {
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
						// fixtureDef.isSensor = true;
						fixtureDef.shape = groundBox;
						groundBody.createFixture(fixtureDef);
						groundBody.setUserData("platform");

						Fixture pfix = groundBody.createFixture(fixtureDef);
						// pfix.setSensor(true);
						pfix.setUserData("platform");

						for (int d = 0; d < c - 1; d++) {
							w++;
						}
						groundBox.dispose();
					}
				}
				if (cellMan.isCellConveyer(w, h)) {
					int c = 0;
					while (cellMan.isCellConveyer(w + c, h)) {
						c++;
					}
					
					String conveyerdirection = cellMan.getConveyerType(w, h);
					
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.position.set(new Vector2(w + c * 0.5f,
							h + 0.5f));
					Body groundBody = world.createBody(groundBodyDef);
					PolygonShape groundBox = new PolygonShape();
					groundBox.setAsBox(c * 0.5f, 0.5f);
					groundBody.createFixture(groundBox, 0.0f);
					groundBody.setUserData("conveyer" + conveyerdirection);
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 2;
					Fixture gfix = groundBody.createFixture(groundBox, 0.0f);
					gfix.setUserData("conveyer" + conveyerdirection);
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
					groundBox.dispose();
				}
			}
		}
		for (int h = 0; h < collisionLayer.getHeight(); h++) {
			for (int w = 0; w < collisionLayer.getWidth(); w++) {
				if (cellMan.isCellEnemySpawn(w, h)) {
					String type = (String) cellMan.getEnemyType(w, h);
					int rand = (int) (Math.random() * 10);
					if (rand == 0)
						rand = 1;
					int count = 0;
					count = cellMan.getCellCount(w, h);
					if (count == 0 || !cellMan.isCellCounter(w, h)) {
						count = rand;
					}
					// Cell cell = nodeLayer.getCell((int) (w), (int) (h));
					// Trigger trigger = new Trigger(cell, w, h, cellMan);
					enemyspawner[enemyspawners] = new EntitySpawner(w, h, type,
							count, cellMan.getCellTriggerValue(w, h),
							cellMan.getCellDelay(w, h), entityMan, cellMan.getEnemyPatrol(w, h));
					/*
					 * enemyspawner[enemyspawners] = new EntitySpawner( w, h,
					 * trigger, entityMan);
					 */
					DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
					System.out.println("Spawner Created of Type:" + type + "at"
							+ w + "," + h + " with " + count + " enemies");
					DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
					enemyspawners++;
				}
				
				if (cellMan.isCellDestroyable(w, h)) {

					if (scene.TOTAL_DESTROYABLES < scene.DESTROYABLE_LIMIT) {
						scene.destroyable[scene.TOTAL_DESTROYABLES] = new Destroyable(
								w, h, cellMan.getCellTriggerValue(w, h), world,
								eventMan);
						System.out.println("DESTROYABLE ADDED: "
								+ scene.TOTAL_DESTROYABLES);
						scene.TOTAL_DESTROYABLES++;

					}
				}
				if (cellMan.isCellDoor(w, h)) {
					if (scene.TOTAL_DOORS < scene.DOOR_LIMIT) {
						int value = cellMan.getCellTriggerValue(w, h);
						scene.door[scene.TOTAL_DOORS] = new Door(w, h, value,
								cellMan.getDoorType(w, h),
								cellMan.getInstanceType(w, h),
								cellMan.getCellDelay(w, h), world);
						scene.TOTAL_DOORS++;
						System.out.println("DOOR ADDED: " + scene.TOTAL_DOORS);
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
					/*
					 * Cell cell = nodeLayer.getCell((int) (w), (int) (h));
					 * Trigger trigger = new Trigger(cell, w, h, cellMan);
					 * trigger.enemySpawnType = "player"; playerStart = new
					 * EntitySpawner(w, h, trigger, entityMan);
					 */
					playerStart = new EntitySpawner(w, h, "player", 1, 0, 0,
							entityMan, 0);
				}
				if (cellMan.isCellWorldVolume(w, h)) {

					String type = cellMan.getInstanceType(w, h);
					DazDebug.print("++++WORLDVOLUME AT: X:" + w + " Y:" + h
							+ " TYPE:" + type);
					if (worldvolumecount < WORLD_VOLUME_LIMIT) {
						worldvolume[worldvolumecount] = new WorldVolume(w, h,
								type, cellMan.getCellTriggerValue(w, h), world,
								eventMan);
						worldvolumecount++;
					}
					DazDebug.print("++++WORLDVOLUME AT: X:" + w + " Y:" + h
							+ " TYPE:" + type);
				}
				if (cellMan.isCellTurret(w, h)) {

					String type = cellMan.getTurretType(w, h);
					int angle = cellMan.getAngle(w, h);
					DazDebug.print("++++TURRET AT: X:" + w + " Y:" + h
							+ " TYPE:" + type);
					if (scene.wallturretcount < scene.WALLTURRET_LIMIT) {
						scene.wallturret[scene.wallturretcount] = new WallTurret(
								w, h, type, angle, world, entityMan, scene);
						scene.wallturretcount++;
					}
				}
				if (cellMan.isCellFlamerTurret(w, h)) {

					String type = cellMan.getFlamerTurretType(w, h);
					//int angle = cellMan.getAngle(w, h);
					DazDebug.print("++++FLAMERTURRET AT: X:" + w + " Y:" + h
							+ " TYPE:" + type);
					if (scene.flamerturretcount < scene.FLAMER_TURRET_LIMIT) {
						scene.flamerturret[scene.flamerturretcount] = new FlamerTurret(
								w, h, type, world, entityMan);
						scene.flamerturretcount++;
					}
				}
				
				
				
				if (cellMan.isCellTimer(w, h)) {

					// String type = cellMan.(x, y)(w, h);
					// int angle = cellMan.getAngle(w,h);

					DazDebug.print("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
					DazDebug.print("WANT CREATE TIMER");

					if (scene.zerotimercount < scene.ZERO_TIMER_LIMIT) {

						DazDebug.print("NEW TIMER"
								+ cellMan.getInstanceType(w, h));
						DazDebug.print("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");

						scene.zerotimer[scene.zerotimercount] = new ZeroTimer(
								cellMan.getCellTriggerValue(w, h),
								cellMan.getTriggerCallValue(w, h),
								cellMan.getCellDelay(w, h),
								cellMan.getInstanceType(w, h), eventMan);
						scene.zerotimercount++;
					} else {

						DazDebug.print("CANT CREATE TIMER, TOO MANY");
						DazDebug.print("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
					}
				}
				if (cellMan.isCellMover(w, h)) {
					DazDebug.print("NEW MOVER");

					if (scene.movercountvalue < scene.MOVER_LIMIT) {

						scene.mover[scene.movercountvalue] = new Mover(w, h,
								cellMan.getMoverType(w, h),
								cellMan.getSpeedValue(w, h),
								cellMan.getCellTriggerValue(w, h),
								cellMan.getMoveXValue(w, h),
								cellMan.getMoveYValue(w, h), world);
						scene.movercountvalue++;
					} else {

						DazDebug.print("CANT CREATE MOVER, TOO MANY");
						DazDebug.print("MMMMMMMMMMMMMMMMMMMMMMMMMMM");
					}
				}
				if (cellMan.isCellPowerCable(w, h)) {
					scene.powercable[scene.powercablecount] = new PowerCable(w,
							h, cellMan.getPowerCablePiece(w, h),
							cellMan.getCellTriggerValue(w, h));
					scene.powercablecount++;
				}
				if (cellMan.isCellCrusher(w, h)) {
					//DazDebug.print("CRUSHER CREATED");
					//DazDebug.print("CCCCCCCCCCCCCCCCCCCCCCCCCCCC");
					
					scene.crusher[scene.crushercount] = new Crusher(w, h,
							cellMan.getCrusherType(w,h),
							cellMan.getSpeedValue(w, h), cellMan.getCellDelay(
									w, h), cellMan.getMoveXValue(w, h),
							cellMan.getMoveYValue(w, h), world);
					scene.crushercount++;
				}
				
				if (cellMan.isCellAnimated(w, h)){
				//	frameTile.clear();**
					int count = cellMan.getAnimatedTileFrames(w, h);
					int tileID = cellMan.getAnimatedCell(w, h).getTile().getId();
					//DazDebug.print("ANIMATED CELL WITH COUNT " + count + " AND TILE-ID " + tileID);
					animatedTiles[animTileCount] = new AnimatedTile(tileID);
					/*
					 * SEACH ARRAY FOR EXISTING TILE
					 */
					boolean animExists = false;
					
					for (int i = 0; i < animTileCount; i++){
						if (animatedTiles[i].tileID == tileID && !animExists){
							animExists = true;
							cellMan.getAnimatedCell(w, h).setTile(animatedTiles[i].getAnimatedTiledMapTile());
						}
					}
					if (!animExists){
						for (int i = 0; i < count; i++){
							//DazDebug.print("ANIMATED CELL TILE || ADDING | TILE ID " + tileID+i);
							animatedTiles[animTileCount].AddAnimationTileFrame((StaticTiledMapTile) map.getTileSets().getTile(tileID+i));
						}
						cellMan.getAnimatedCell(w, h).setTile(animatedTiles[animTileCount].getAnimatedTiledMapTile());
						animTileCount++;
					}
					
				}
				if (cellMan.isCellCrate(w, h)) {

				//	String type = cellMan.getFlamerTurretType(w, h);
					//int angle = cellMan.getAngle(w, h);
				//	DazDebug.print("++++FLAMERTURRET AT: X:" + w + " Y:" + h
					//		+ " TYPE:" + type);
					if (scene.cratecount < scene.CRATE_LIMIT) {
						scene.crate[scene.cratecount] = new Crate(w,h,world, cellMan.getCrateValue(w, h));
						scene.cratecount++;
					}
				}
				if (cellMan.isCellChain(w, h)) {

					//	String type = cellMan.getFlamerTurretType(w, h);
						//int angle = cellMan.getAngle(w, h);
					//	DazDebug.print("++++FLAMERTURRET AT: X:" + w + " Y:" + h
						//		+ " TYPE:" + type);
						if (scene.chaincount < scene.CHAIN_LIMIT) {
							scene.chain[scene.chaincount] = new Chain(w, h, 3, world);
							scene.chaincount++;
						}
					}
			}
		}
		
	}
	

	public void update(){
		//animTileMan.update();
	}
	
	EntitySpawner playerStart;

	public EntitySpawner getPlayerSpawner() {
		return playerStart;

	}

}
