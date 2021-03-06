package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.nodes.Trigger;

/*
 * 			CellManager grabs cells from layers, 
 * 
 */




public class CellManager {
	
	public String levelcompleteKey = "levelcomplete";
	public String playerstartKey = "playerstart";
	public String bossKey = "boss";
	public String scrollingKey = "scrollingmap";
	public String delaykey = "delay";
	public String idkey = "id";
	public String itemKey = "item";
	public String callvalueKey = "callvalue";
	public String powercableKey = "powercable";
	public String worldvolumeKey = "worldvolume";
	public String triggervalueKey = "triggervalue";
	public String blockedKey = "solid";
	public String platformKey = "platform";
	public String npcKey = "target";
	public String enemyKey = "enemyspawn";
	public String instanceKey = "instancetype";
	public String countKey = "count";
	public String destroyableKey = "destroyable";
	public String diagonalKey = "diagonal";
	public String doorKey = "door";
	public String ladderKey = "ladder";
	public String angleKey = "angle";
	public String turretKey = "turret";
	public String flamerturretKey = "flamerturret";
	public String crusherKey = "crusher";
	public String crateKey = "crate";
	public String deathKey = "death";
	public String animatedKey = "animated";
	public String conveyerKey = "conveyer";
	public String patrolKey = "enemypatrol";
	public String chainKey = "chain";
	
	
	
	public String calltriggerKey = "triggercall";
	public String timerKey = "timer";
	
	public String moverKey = "mover";
	public String movexKey = "movex";
	public String moveyKey = "movey";
	public String speedKey = "speed";
	

	public TiledMapTileLayer collisionLayer;
	public TiledMapTileLayer nodeLayer;
	
	public TiledMapTileLayer animatedLayer;
	
	public CellManager(TiledMapTileLayer nodeLayer, TiledMapTileLayer collisionLayer,TiledMapTileLayer animatedLayer){
		this.nodeLayer = nodeLayer;
		this.collisionLayer = collisionLayer;
		this.animatedLayer = animatedLayer;
	}
	
	
	public boolean isCellInstanceType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(instanceKey);
	}
	
	public boolean isLevelScrolling(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(scrollingKey);
	}

	public boolean isCellBoss(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(bossKey);
	}
	
	public boolean isCellWorldVolume(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(worldvolumeKey);
	}
	public boolean isCellLadder(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(ladderKey);
	}
	public boolean isCellPlayerStart(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(playerstartKey);
	}
	public boolean isCellLevelComplete(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(levelcompleteKey);
	}

	public boolean isCellDestroyable(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(destroyableKey);
	}

	public boolean isCellDoor(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(doorKey);

	}

	public boolean isCellEnemySpawn(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(enemyKey);

	}
	
	public boolean isCellCounter(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(countKey);

	}
	public boolean isCellCrate(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(crateKey);

	}
	
	public boolean isCellAnimated(float x, float y) {
		Cell cell = animatedLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(animatedKey);

	}

	public boolean isCellCallValue(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(callvalueKey);

	}
	

	public boolean isCellItem(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(itemKey);

	}
	
	
	public boolean isCellTriggerValue(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(triggervalueKey);
	}
	
	public boolean isCellDelay(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(delaykey);
	}
	
	public boolean isCellPowerCable(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(powercableKey);
	}
	
	public boolean isCellDiagonal(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(diagonalKey);
	}
	
	

	public boolean isCellBlocked(float x, float y, boolean isActorLooking) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	public boolean isCellDeath(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(deathKey);
	}
	
	public boolean isCellConveyer(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(conveyerKey);
	}

	public boolean isCellPlatform(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(platformKey);
	}
	
	public boolean isCellTurret(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(turretKey);
	}
	public boolean isCellFlamerTurret(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(flamerturretKey);
	}
	
	public boolean isCellAngle(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(angleKey);
	}
	
	public boolean isCellTimer(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(timerKey);
	}
	
	public boolean isCellCallTriggerValue(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(calltriggerKey);
	}
	
	public boolean isCellMover(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(moverKey);
	}
	
	public boolean isCellCrusher(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(crusherKey);
	}

	
	public boolean isCellMoveX(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(movexKey);
	}
	public boolean isCellMoveY(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(moveyKey);
	}
	public boolean isCellSpeed(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(speedKey);
	}
	public boolean isEnemyPatrol(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(patrolKey);
	}
	
	public boolean isCellChain(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(chainKey);
	}
	

	/*
	 * 					GET PROPERTY FUNCTIONS
	 */
	/*
	public int isEnemyPatrol(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey(patrolKey);
	}*/
	public String getCrateValue(float x, float y) {
		String val = "wood";
	
		if (isCellCrate(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			val = (String) cell.getTile().getProperties().get(crateKey);

			return val;
		}
		return val;
	}
	
	public int getEnemyPatrol(float x, float y) {
		if (isEnemyPatrol(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(patrolKey);

			return Integer.parseInt(val);
		}
		DazDebug.print("TRIED TO GET patrolKey, BUT FOUND NO PROPERTY CALLED patrolKey");
		return 0;
	}
	
	
	
	public Cell getAnimatedCell(float x, float y) {
		return	animatedLayer.getCell((int) (x), (int) (y));
	}
	public int getAnimatedTileFrames(float x, float y) {
		if (isCellAnimated(x, y)){
			Cell cell = animatedLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(animatedKey);

			return Integer.parseInt(val);
		}
		DazDebug.print("TRIED TO GET animatedKey, BUT FOUND NO PROPERTY CALLED animatedKey");
		return 0;
	}
	
	public String getCrusherType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellCrusher(x, y)) {
			value = (String) cell.getTile().getProperties().get(crusherKey);
		} 
		return value;
	}
	
	public int getSpeedValue(float x, float y) {
		if (isCellSpeed(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(speedKey);

			return Integer.parseInt(val);
		}
		DazDebug.print("TRIED TO GET speedKey, BUT FOUND NO PROPERTY CALLED speedKey");
		return 0;
	}
	
	
	public int getMoveXValue(float x, float y) {
		if (isCellMoveX(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(movexKey);

			return Integer.parseInt(val);
		}
		DazDebug.print("TRIED TO GET movexKey, BUT FOUND NO PROPERTY CALLED movexKey");
		return 0;
	}
	public int getMoveYValue(float x, float y) {
		if (isCellMoveY(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(moveyKey);

			return Integer.parseInt(val);
		}
		DazDebug.print("TRIED TO GET moveyKey, BUT FOUND NO PROPERTY CALLED moveyKey");
		return 0;
	}
	public int getPowerCablePiece(float x, float y) {
		int val = 0;
		int check = 0;
		if (isCellPowerCable(x-1, y)){
			val += 1;
			check++;
		}
		if (isCellPowerCable(x, y+1)){
			val += 2;
			check++;
		}
		if (isCellPowerCable(x+1, y)){
			val += 4;
			check++;
		}
		if (isCellPowerCable(x, y-1)){
			val += 8;
			check++;
		}
		if (check != 2){
			val = 0;
		}
		
		return val;
	}
	
	public String getMoverType(float x, float y) {
		if (isCellMover(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(moverKey);

			return val;
		}
		DazDebug.print("TRIED TO GET MOVERVALUE, BUT FOUND NO PROPERTY CALLED moverKey");
		return "";
	}

	public int getTriggerCallValue(float x, float y) {
		if (isCellCallTriggerValue(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String val = (String) cell.getTile().getProperties().get(calltriggerKey);

			return Integer.parseInt(val);
		}
		DazDebug.print("TRIED TO GET calltriggerKey VALUE, BUT FOUND NO PROPERTY CALLED calltriggerKey");
		return 0;
	}
	
	public int getAngle(float x, float y) {
		if (isCellAngle(x,y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String ang = (String) cell.getTile().getProperties().get(angleKey);
			return Integer.parseInt(ang);
		}
		return 0;
	}
	
	
	public String getTurretType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellTurret(x, y)) {
			value = (String) cell.getTile().getProperties().get(turretKey);
		}
		return value;
	}
	
	public String getFlamerTurretType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellFlamerTurret(x, y)) {
			value = (String) cell.getTile().getProperties().get(flamerturretKey);
		}
		return value;
	}
	
	public String getItemValue(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellItem(x, y)) {
			value = (String) cell.getTile().getProperties().get(itemKey);
		}
		return value;
	}
	/*public String getCellItemID(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellItem(x, y)) {
			value = (String) cell.getTile().getProperties().get(idkey);
		}
		return value;
	}*/

	public String getDiagonalValue(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellDiagonal(x, y)) {
			value = (String) cell.getTile().getProperties().get(diagonalKey);
		}
		return value;
	}
	
	public String getEnemyType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "null";
		if (isCellEnemySpawn(x, y)) {
			value = (String) cell.getTile().getProperties().get(enemyKey);
		} 
		return value;
	}
	
	public String getConveyerType(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));
		String value = "right";
		if (isCellConveyer(x, y)) {
			value = (String) cell.getTile().getProperties().get(conveyerKey);
		} 
		return value;
	}

	public int getCellTriggerValue(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellTriggerValue(x, y)) {
			value = (String) cell.getTile().getProperties().get(triggervalueKey);
			if (value.equals("")){
				value = "0";
			}
		}
		return Integer.parseInt(value);
	}

	
	public int getCellDelay(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "0";
		if (isCellDelay(x, y)) {
			value = (String) cell.getTile().getProperties().get(delaykey);
		}
		return Integer.parseInt(value);
	}

	public String getDoorType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "0";	
		if (isCellDoor(x, y)){
			value = (String) cell.getTile().getProperties().get(doorKey);
			return value;
		}
		return value;
	}
	
	public int getCellCount(float x, float y) {
		if (isCellCounter(x, y)){
			
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
		String value = "null";
		int intvalue = 0;
		if (isCellCounter(x, y)) {
			value = (String) cell.getTile().getProperties().get(countKey);
			intvalue = Integer.parseInt(value);
		}
		
		return intvalue;
		}
		return 0;
	}
	
	
	public String getInstanceType(float x, float y) {
		Cell cell = nodeLayer.getCell((int) (x), (int) (y));
	//	String value = "null";
		//if (isCellInstanceType(x, y)) {
		String value = (String) cell.getTile().getProperties().get(instanceKey);
		//}
		
		
		return value;
	}
	
	public int getCellCallValue(float x, float y) {
		if (isCellCallValue(x, y)){
			Cell cell = nodeLayer.getCell((int) (x), (int) (y));
			String value = "null";
			int intvalue = 0;
			if (isCellCounter(x, y)) {
				value = (String) cell.getTile().getProperties().get(callvalueKey);
				intvalue = Integer.parseInt(value);
			}
			
			return intvalue;
			}
			return 0;
	}
	
	public Trigger[] trigary = new Trigger[128];
	public int trigcount = 0;
	
	public Trigger giveMeTrigger(float x, float y){
		//
		
		//trigary[trigcount] = new Trigger(Cell nodeLayer.getCell((int) (x), (int) (y), x,y,this);
		trigcount++;
		return trigary[trigcount-1];
		
	}






	
	
}
