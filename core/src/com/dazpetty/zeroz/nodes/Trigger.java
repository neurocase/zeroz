package com.dazpetty.zeroz.nodes;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.dazpetty.zeroz.managers.CellManager;
import com.dazpetty.zeroz.managers.LevelManager;

public class Trigger {

	/*
	 * Trigger isn't working
	 * 
	 * 
	 * 	InstanceType:
	 * 			Repeating
	 * 			Once
	 * 	Delay: Seconds for delay
	 * 	Trigger Value: Value of this trigger
	 * 
	 * 	Call Value: Value this trigger calls, if this trigger wants to call
	 * 				other trigger objects, such as a timer, activated by trigger 1, opening
	 * 				a door, after delay, on trigger 2.
	 */
	
	String instanceType = "once";
	
	public int delay = 0;
	public int countvalue = 0;
	public int triggerValue = 0;
	public int callValue = 0;
	
	public String enemySpawnType = "";
	
	public Trigger(Cell cell, float x, float y, CellManager cellManager){
		
		countvalue = cellManager.getCellCount(x,y);
		triggerValue = cellManager.getCellTriggerValue(x, y);
		delay = cellManager.getCellDelay(x, y);
		callValue = cellManager.getCellCallValue(x,y);
		
		enemySpawnType = cellManager.getEnemyType(x, y);
		
		
		
	}
	
	// How many calls have been made to trigger
	public int callCounts = 0;
	
	// How many calls need to be made to trigger for it to activate
	public int triggerCallCount = 0;

	public void Call(){
		callCounts++;
		if (callCounts == triggerCallCount){
			Trigger();
		}
	}
	
	public void Trigger(){
		
	}
	
}
