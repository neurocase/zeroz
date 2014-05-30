package com.dazpetty.zeroz.entities;

import com.dazpetty.zeroz.managers.LevelManager;

public class Trigger {

	public int triggerValue = 0;
	
	
	// How many calls have been made to trigger
	public int callCounts = 0;
	
	// How many calls need to be made to trigger for it to activate
	public int triggerCallCount = 0;
	
	LevelManager levelMan;
	
	public Trigger(int triggerValue, int triggerCallCount, LevelManager levelMan){
		this.triggerValue = triggerValue;
		this.triggerCallCount = triggerCallCount;
		this.levelMan = levelMan;
	}

	public void Call(){
		callCounts++;
		if (callCounts == triggerCallCount){
			Trigger();
		}
	}
	
	public void Trigger(){
		
	}
	
}
