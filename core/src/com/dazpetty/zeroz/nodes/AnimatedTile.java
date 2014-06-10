package com.dazpetty.zeroz.nodes;

import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

public class AnimatedTile {

	public Array<StaticTiledMapTile> frameTile;
	public int tileID = 0;
	
	public AnimatedTile(int tileID) {
		this.tileID = tileID;
		frameTile = new Array<StaticTiledMapTile>();
	}
	
	public void AddAnimationTileFrame(StaticTiledMapTile staticTile){
		frameTile.add(staticTile);
	}
	
	public AnimatedTiledMapTile getAnimatedTiledMapTile(){
		AnimatedTiledMapTile animTile = new AnimatedTiledMapTile(1/4f, frameTile);
		return animTile;
	}
	
	
}
