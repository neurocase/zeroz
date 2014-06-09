package com.dazpetty.zeroz.managers;

import java.util.Iterator;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.dazpetty.zeroz.core.DazDebug;

public class AnimatedTilesManager {

	Array<StaticTiledMapTile> frameTiles = new Array<StaticTiledMapTile>(2);
	AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(1 / 30,
			frameTiles);
	//Iterator<TiledMapTile> tiles;
	
	AnimatedTile[] animatedTiles = new AnimatedTile[128];
	
	Map map;
	AnimatedTilesManager(CellManager cellMan, SceneManager scene, Map map) {
		//this.map = map;
	//	Iterator<TiledMapTile> tiles = map.getTileSet("tiles").iterator();
		
	}
	
	public int animtilescount = 0;
	
	public void addAnimatedCell(int w, int h, Cell cell, int tileFrames, CellManager cellMan){
		animatedTiles[animtilescount] = new AnimatedTile(w,h,cell, tileFrames, cellMan);
		animtilescount++;
	}
	
	
	
	public void update() {
		for (int i = 0; i < animtilescount; i++){
			DazDebug.print("updating tile " + i);
			animatedTiles[i].update();
		}
	}
}
