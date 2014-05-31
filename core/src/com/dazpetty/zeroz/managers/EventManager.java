package com.dazpetty.zeroz.managers;

import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.entities.Destroyable;
import com.dazpetty.zeroz.entities.Door;

/*
 * 	The Event Manager handles messages between triggers; Buttons, Destroyable Objects, World Volumes etc.
 *  and the objects which they interact with, spawners, doors, movers. 
 *  
 *  some objects, such as clocks and kill counters, may need to send and recieve messges.
 * 		
 */
public class EventManager {

	public LevelManager levelMan;
	public SceneManager scene;
	
	
	//public final int DESTROYABLE_LIMIT = 10;
//	public final int DOOR_LIMIT = 10;
	
//	public int TOTAL_DESTROYABLES = 0;
//	public int TOTAL_DOORS = 0;
	
	
	//public Destroyable[] destroyable = new Destroyable[DESTROYABLE_LIMIT];
//	public Door[] door = new Door[DOOR_LIMIT];
	
	
	public EventManager(LevelManager levelMan){
		this.levelMan = levelMan;
		this.scene = levelMan.scene;
	}

	public void CallTriggerValue(int value){
		DazDebug.print("revieved trigger: " + value);
		if (value == 0){
			return;
		}
		
		//Call doors, movers and spawners, with this trigger
		for (int i = 0; i < scene.ACTUATOR_LIMIT; i++){
			if (levelMan.enemyspawner[i] != null){
				levelMan.enemyspawner[i].triggerOn(value);
			}

			if (scene.door[i] != null && scene.door[i].checkKey(value)) {
				
				String doorstate = "";
				if (scene.door[i].openDoor){
					doorstate = "open";
				}else{
					doorstate = "closed";
				}
				
				DazDebug.print("triggering " + doorstate + " door " + i + " with value: " + value);
				scene.door[i].trigger(value);
			}
			
		}


		
		
		
	}
	
	public void PollActuators(){
		for (int i = 0; i < scene.ACTUATOR_LIMIT; i++){
			if (levelMan.enemyspawner[i] != null){
				levelMan.enemyspawner[i].update();
			}

			if (scene.door[i] != null) {
				scene.door[i].update();
			}
			
		}
	}
	
}
