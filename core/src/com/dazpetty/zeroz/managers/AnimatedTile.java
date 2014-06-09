package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.dazpetty.zeroz.core.DazDebug;

public class AnimatedTile {

	public int initialID = 0;
	public int finalID = 0;
	public int currentFrameID = 0;
	public Cell cell; 
	public CellManager cellMan;
	public int initialX = 0;
	public int initialY = 0;
	
	public AnimatedTile(int x, int y, Cell cell, int tileFrames, CellManager cellMan){
		initialX = x;
		initialY = y;
		this.cellMan = cellMan;
		this.cell = cell;
		initialID = cell.getTile().getId();
		finalID = initialID + tileFrames;
	}

	public void update(){
		
		
		if (currentFrameID < finalID){
			currentFrameID++;
		}else{
			currentFrameID = initialID;
		}
		
		DazDebug.print("UPDATING ANIMATED TILE ID:" +currentFrameID );
	//	cell.getTile().setId(currentFrameID);
	//	cell.setTile(cellMan.getAnimatedCell(initialX+currentFrameID, initialY).getTile());
	//	cell.setTile(currentFrameID);
		
		
	}

}
