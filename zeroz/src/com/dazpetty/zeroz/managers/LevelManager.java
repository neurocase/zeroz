package com.dazpetty.zeroz.managers;

import java.util.Arrays;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

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
	
	public LevelManager(int level){
		
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
}