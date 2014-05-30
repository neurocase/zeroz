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
	public TiledMapTileLayer miscLayer;
	public String levelcompleteKey = "levelcomplete";
	public String playerstartKey = "playerstart";

	public String bossKey = "boss";
	public String scrollingKey = "scrollingmap";

	public String idkey = "id";
	public String itemKey = "item";
	public String worldvolumekey = "worldvolume";
	public String triggervaluekey = "triggervalue";
	public String blockedKey = "solid";
	public String platformKey = "platform";
	public String npcKey = "target";
	public String enemyKey = "enemyspawn";
	public String enemyCountKey = "enemycount";
	public String destroyableKey = "destroyable";
	public String diagonalKey = "diagonal";
	public String doorKey = "door";
	public TiledMap map;
	public String ladderKey = "ladder";
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
	
	

	public LevelManager(int level, GameScreen gameScreen, World world) {
		this.entityMan = gameScreen.entityMan;
		this.scene = gameScreen.scene;
		this.world = world;
		
		String levelstr = Integer.toString(level);
		map = new TmxMapLoader().load("data/levels/level" + levelstr + ".tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		miscLayer = (TiledMapTileLayer) map.getLayers().get("miscLayer");
		Arrays.fill(keys, Boolean.FALSE);
		eventMan = new EventManager(this);
	}
	

	/*	----------------------------
	 *  FUNCTIONS FOR BUILDING LEVEL
	 *  ----------------------------
	 */
	

	public boolean isLevelScrolling(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(scrollingKey);
	}

	public boolean isCellBoss(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(bossKey);
	}
	
	public boolean isCellWorldVolume(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(worldvolumekey);
	}
	
	public String getWorldVolumeType(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellWorldVolume(x, y)) {
			value = (String) cell.getTile().getProperties().get(worldvolumekey);
		}
		return value;
	}
	
	public String getCellStringID(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellItem(x, y)) {
			value = (String) cell.getTile().getProperties().get(idkey);
		}
		return value;
	}
	
	public boolean isCellLadder(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(ladderKey);
	}
	public boolean isCellPlayerStart(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(playerstartKey);
	}
	public boolean isCellLevelComplete(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(levelcompleteKey);
	}

	public boolean isCellDestroyable(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(destroyableKey);
	}

	public boolean isCellDoor(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(doorKey);

	}

	public boolean isCellEnemySpawn(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(enemyKey);

	}
	
	public boolean isCellSpawnCounter(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(enemyCountKey);

	}

	public String getEnemyType(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellEnemySpawn(x, y)) {
			value = (String) cell.getTile().getProperties().get(enemyKey);
		} else {
			System.out.println("ERROR: CELL IS NOT ENEMY SPAWN");
		}
		return value;
	}

	public boolean isCellItem(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(itemKey);

	}

	public String getItemValue(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellItem(x, y)) {
			value = (String) cell.getTile().getProperties().get(itemKey);
		}
		return value;
	}
	
	public boolean isCellTriggerValue(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(triggervaluekey);
	}
	
	public int getCellTriggerValue(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellTriggerValue(x, y)) {
			value = (String) cell.getTile().getProperties().get(triggervaluekey);
		}
		return Integer.parseInt(value);
	}

	public int getCellValue(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellDoor(x, y)) {
			value = (String) cell.getTile().getProperties().get(doorKey);
		} else if (isCellDestroyable(x, y)) {
			value = (String) cell.getTile().getProperties().get(destroyableKey);
		}
		return Integer.parseInt(value);
	}

	public boolean isCellDiagonal(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(diagonalKey);
	}

	public String getDiagonalValue(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellDiagonal(x, y)) {
			value = (String) cell.getTile().getProperties().get(diagonalKey);
		}
		return value;
	}

	public boolean isCellBlocked(float x, float y, boolean isActorLooking) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		if ((isCellDoor(x, y) || isCellDoor(x, y + 1)) && isActorLooking) {
			Cell celld = miscLayer.getCell((int) (x), (int) (y));
			if (celld != null
					&& miscLayer.getCell((int) (x), (int) (y)) != null
					&& celld.getTile().getProperties().get(doorKey) != null) {
				String value = (String) celld.getTile().getProperties()
						.get(doorKey);
				System.out.print("AT DOOR: " + value);
				if (!keys[Integer.parseInt(value)]) {
					System.out.print(" :" + " KEY NOT FOUND ");
					return true;
				} else {
					System.out.print(" :" + " KEY FOUND ");
					// return false;
				}
			}
		}

		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(blockedKey);
	}

	public boolean isCellPlatform(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(platformKey);
	}
	
	
	
	/*	---------------------
	 *  BUILDING LEVEL
	 *  ---------------------
	 */

	public void buildLevel(EntityManager entityMan) {
		this.entityMan = entityMan;
		for (int h = 0; h < collisionLayer.getHeight(); h++) {
			for (int w = 0; w < collisionLayer.getWidth(); w++) {

				if (isCellBlocked(w, h, false)) {
					int c = 0;
					while (isCellBlocked(w + c, h, false)) {
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
				if (isCellDiagonal(w, h)) {

					BodyDef groundBodyDef = new BodyDef();
					
					String value = getDiagonalValue(w, h);
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
				if (isCellPlatform(w, h)) {
					/*
					 * 	PLATFORMS ARE NOT WORKING
					 */
					boolean THIS_CODE_IS_FIXED = true;
					if (THIS_CODE_IS_FIXED){
					int c = 0;
					while (isCellPlatform(w + c, h)) {
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
				if (isCellEnemySpawn(w, h)) {
					String type = (String) getEnemyType(w, h);
					int rand = (int) (Math.random() * 10);
					if (rand == 0)
						rand = 1;
					int count = 0;
					count = getEnemySpawnCount(w, h);
					if (count == 0 || !isCellSpawnCounter(w,h)){
						count = rand;
					}
					enemyspawner[enemyspawners] = new EntitySpawner(
							w, h, type,  count, getCellTriggerValue(w, h), entityMan);
					DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
					System.out.println("Spawner Created of Type:" + type + "at"
							+ w + "," + h + " with " + count + " enemies");
					DazDebug.print("-=-=-=-=+++++++++++++++++++++++++++++++++++=-=-=");
					enemyspawners++;
				}
				if (isCellDestroyable(w, h)) {
					int value = getCellValue(w, h);
					if (scene.TOTAL_DESTROYABLES < scene.DESTROYABLE_LIMIT) {
						scene.destroyable[value] = new Destroyable(w, h,
								value, world);
						System.out.println("DESTROYABLE ADDED: "
								+ scene.TOTAL_DESTROYABLES);
						scene.TOTAL_DESTROYABLES++;

					}
				}
				if (isCellDoor(w, h)) {
					if (scene.TOTAL_DOORS < scene.DOOR_LIMIT) {
						int value = getCellValue(w, h);
						scene.door[scene.TOTAL_DOORS] = new Door(w, h,
								value, world);
						scene.TOTAL_DOORS++;
						System.out.println("DOOR ADDED: "
								+ scene.TOTAL_DOORS);
					}
				}
				if (isCellItem(w, h)) {
					if (scene.TOTAL_ITEMS < scene.ITEM_LIMIT) {
						String value = getItemValue(w, h);
						scene.item[scene.TOTAL_ITEMS] = new Item(w, h,
								scene.TOTAL_ITEMS, value);
						scene.TOTAL_ITEMS++;
						System.out.println(value + " ITEM ADDED: "
								+ scene.TOTAL_ITEMS + "at:" + w + "," + h);
					}
				}
				if (isCellLevelComplete(w, h)) {
					System.out.println("LEVEL COMPLETE AT: x:" + w + "y:" + h);
					levelcompletepos.x = w;
					levelcompletepos.y = h;
				}
				if (isCellPlayerStart(w, h)) {
					System.out.println("PlayerStart at: x" + w + "y:" + h);
					playerStart = new EntitySpawner(w, h, "player", 1, 0, entityMan);
				}
				if (isCellWorldVolume(w, h)) {
					DazDebug.print("++++++++++++++++++++++++++++WORLDVOLUME AT");
					String type = getWorldVolumeType(w, h);
					if (worldvolumecount < WORLD_VOLUME_LIMIT ){
						worldvolume[worldvolumecount] = new WorldVolume(w,h,type,getCellTriggerValue(w,h), world,this);
						worldvolumecount++;
					}
					DazDebug.print("++++++++++++++++++++++++++++WORLDVOLUME AT: X:" + w + " Y:" + h + " TYPE:" + type);
				}
				if (!isLevelScrolling) {
					if (isLevelScrolling(w, h)) {
						isLevelScrolling = true;
					}
				}
				if (!isBossLevel) {
					if (isCellBoss(w, h)) {
						isBossLevel = true;
						scene.copterBoss = new CopterBoss(w, h, world);
					}
				}

			}
		}
	}

	private int getEnemySpawnCount(float x, float y) {
		if (isCellSpawnCounter(x, y)){
			
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "null";
		int intvalue = 0;
		if (isCellSpawnCounter(x, y)) {
			value = (String) cell.getTile().getProperties().get(enemyCountKey);
			intvalue = Integer.parseInt(value);
		}
		
		return intvalue;
		}
		return 0;
	}

	EntitySpawner playerStart;
	
	public EntitySpawner getPlayerSpawner() {
		return playerStart;
		
	}

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
	
	}

}
