package com.dazpetty.zeroz.managers;

import java.util.Arrays;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.entities.CopterBoss;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;
import com.dazpetty.zeroz.entities.EnemySpawner;
import com.dazpetty.zeroz.entities.Item;

public class LevelManager {

	public TiledMapTileLayer collisionLayer;
	public TiledMapTileLayer miscLayer;
	public String levelcompleteKey = "levelcomplete";
	public String playerstartKey = "playerstart";
	
	public String bossKey = "boss";
	public String scrollingKey = "scrollingmap";
	
	public String itemKey = "item";
	public String blockedKey = "solid";
	public String platformKey = "platform";
	public String npcKey = "target";
	public String enemyKey = "enemyspawn";
	public String destroyableKey = "destroyable";
	public String doorKey = "door";
	public TiledMap map;
	public String ladderKey = "ladder";
	public int KEYS_LIMIT = 10;
	public boolean[] keys = new boolean[KEYS_LIMIT];
	
	
	public boolean isLevelScrolling = false;
	public boolean isBossLevel = false;
	public Vector2 playerstart = new Vector2(0, 0);
	
	public Vector2 levelcompletepos = new Vector2(0,0);
	
	public boolean isLevelComplete = false;
	
	public ActorManager actorMan;
	public World world;
	
	public LevelManager(int level, ActorManager actorMan, World world){
		this.actorMan = actorMan;
		this.world = world;
		
		String levelstr = Integer.toString(level);
		map = new TmxMapLoader().load("data/levels/level" + levelstr + ".tmx");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("collision");
		miscLayer = (TiledMapTileLayer) map.getLayers().get("miscLayer");
		Arrays.fill(keys, Boolean.FALSE);
		
	
	
	}
	

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
	public String getEnemyType(float x, float y){
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellEnemySpawn(x,y)){
			value = (String) cell.getTile().getProperties().get(enemyKey);
		}else{
			System.out.println("ERROR: CELL IS NOT ENEMY SPAWN");
		}
		return value;
	}
	
	public boolean isCellItem(float x, float y) {
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(itemKey);
		
	}
	public String getItemValue(float x, float y){
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellItem(x,y)){
			value = (String) cell.getTile().getProperties().get(itemKey);
		}
		return value;
	}
	
	public int getCellValue(float x, float y){
		Cell cell = miscLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellDoor(x,y)){
			value = (String) cell.getTile().getProperties().get(doorKey);
		}else if(isCellDestroyable(x,y)){
			value = (String) cell.getTile().getProperties().get(destroyableKey);
		}
		return Integer.parseInt(value);
	}
	
	public boolean isCellBlocked(float x, float y, boolean isActorLooking) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		if ((isCellDoor(x,y) || isCellDoor(x,y+1))&& isActorLooking){
			Cell celld = miscLayer.getCell((int) (x), (int) (y));
				if(celld != null && miscLayer.getCell((int) (x), (int) (y))!= null  && celld.getTile().getProperties().get(doorKey) != null){
					String value = (String)celld.getTile().getProperties().get(doorKey);
					System.out.print("AT DOOR: " + value);
					if (!keys[Integer.parseInt(value)]){
						System.out.print(" :" + " KEY NOT FOUND ");
						return true;
					}else{
						System.out.print(" :" + " KEY FOUND ");
					//	return false;
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
	
	
	public void buildLevel(ActorManager actorMan){
		this.actorMan = actorMan;
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
					FixtureDef fixtureDef = new FixtureDef();
					fixtureDef.shape = groundBox;
					fixtureDef.filter.categoryBits = 2;
					Fixture gfix = groundBody.createFixture(groundBox, 0.0f);
					gfix.setUserData("ground");
					for (int d = 0; d < c - 1; d++) {
						w++;
					}
				}
				if (isCellPlatform(w, h)) {
					int c = 0;
					while (isCellPlatform(w + c, h)) {
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
				if (isCellEnemySpawn(w, h)) {
					String type = (String) getEnemyType(w,h);
					int rand = (int) (Math.random() * 10);
					if (rand == 0) rand = 1;
					actorMan.enemyspawner[actorMan.enemyspawners] = new EnemySpawner(w, h, type,rand);
					System.out.println("Spawner Created of Type:" + type + "at" + w + "," + h + " with " + rand + " enemies");
					actorMan.enemyspawners++;
				}
				if (isCellDestroyable(w, h)) {
					int value = getCellValue(w, h);
					if (actorMan.TOTAL_DESTROYABLES < actorMan.DESTROYABLE_LIMIT) {
						actorMan.destroyable[value] = new Destroyable(w, h, value, world);
						System.out.println("DESTROYABLE ADDED: "
								+ actorMan.TOTAL_DESTROYABLES);
						actorMan.TOTAL_DESTROYABLES++;
	
					}
				}
				if (isCellDoor(w, h)) {
					if (actorMan.TOTAL_DOORS < actorMan.DOOR_LIMIT) {
						int value = getCellValue(w, h);
						actorMan.door[actorMan.TOTAL_DOORS] = new Door(w, h, value, world);
						actorMan.TOTAL_DOORS++;
						System.out.println("DOOR ADDED: " + actorMan.TOTAL_DOORS);
					}
				}
				if (isCellItem(w, h)) {
					if (actorMan.TOTAL_ITEMS < actorMan.ITEM_LIMIT) {
						String value = getItemValue(w, h);
						actorMan.item[actorMan.TOTAL_ITEMS] = new Item(w, h, actorMan.TOTAL_ITEMS, value, world);
						actorMan.TOTAL_ITEMS++;
						System.out.println(value + " ITEM ADDED: "
								+ actorMan.TOTAL_ITEMS + "at:" + w + "," + h);
					}
				}
				if (isCellLevelComplete(w, h)) {
					System.out.println("LEVEL COMPLETE AT: x:" + w + "y:" + h);
					levelcompletepos.x = w;
					levelcompletepos.y = h;
				}
				if (isCellPlayerStart(w, h)) {
					System.out.println("PlayerStart at: x" + w + "y:" + h);
					playerstart.x = w;
					playerstart.y = h;
				}
				if (!isLevelScrolling){
					if (isLevelScrolling(w, h)) {
						isLevelScrolling = true;
					}
				}
				if (!isBossLevel){
					if (isCellBoss(w, h)) {
						isBossLevel = true;
						actorMan.copterBoss = new CopterBoss(w, h, world);
						
					}
				}
		
		
		
			}
		}
	}
	
}
