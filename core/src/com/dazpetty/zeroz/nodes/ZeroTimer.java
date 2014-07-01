package com.dazpetty.zeroz.nodes;

import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.EventManager;

public class ZeroTimer {
	/*
	 * 	TODO:
	 * 	Repeating : a timer whitch is repeating, triggering backwards and forwards, only needs one trigger
	 *  
	 *  Delayed (once) :A timer is activated, then delays, then triggers its second trigger, set the second trigger
	 *  to be the same as activate trigger to trigger a trap/ambush scenario where a door closes on the player, then 
	 *  opens after a short time  
	 * 
	 * 
	 *  ACTIVATE TRIGGER: TRIGGER WHICH ACTIVATES THE CLOCK 
	 *  SECOND TRIGGER:	  TRIGGER THE CLOCK ACTIVATES
	 * 			     
	 *  
	 */
	public int triggervalue = 0;
	public int triggercallvalue = 0;
	public int delay = 0;
	public String type = "";
	
	public boolean allreadyused = false;
	public boolean started = false;
	
	public EventManager eventMan;
	
	public ZeroTimer(int triggervalue, int triggercallvalue,int delay, String type, EventManager eventMan){
		DazDebug.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		DazDebug.print("CREATING TIMER, TRIG VALUE:" + triggervalue + " Call Value:" + triggercallvalue + " delay:" + delay + " type:" + type);
		DazDebug.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		
		this.triggervalue = triggervalue;
		this.triggercallvalue = triggercallvalue;
		
		//TIMING IS IN TENTHS OF A SECOND
		this.delay = delay * 100;
		this.type = type;
		this.eventMan = eventMan;
		
		if (type.equals("repeat")){
			System.out.println("REPEATING");
			started = true;
		}
	}
	
	public boolean checkTrigger(int triggertestvalue){
		return triggervalue == triggertestvalue;
	}
	
	public void callTimer(int triggertestvalue){
		if (checkTrigger(triggertestvalue)){
			start();
		}
	}
	
	public void callTrigger(){
	eventMan.CallTriggerValue(triggercallvalue);
	}
	public void start(){
		DazDebug.print("TTTTTTTTTTTTTTTTTTTT");
		DazDebug.print("TIMER STARTED!::DELAY:" + delay);
		DazDebug.print("TTTTTTTTTTTTTTTTTTTT");
		
		
		started = true;
		starttime = System.currentTimeMillis();
	}
	long starttime = System.currentTimeMillis();
	
	public boolean callOver = false;
	
	
	public void update(){
		
		//DazDebug.print("!!!UPDATE TIMER!!!");
		if (started && !callOver){
			if (System.currentTimeMillis() - starttime > delay){
				DazDebug.print("!!!DING!!!!!!!DING!!!!DING!!!!!!");
				DazDebug.print("TIMER CALLING TRIGGER" + triggercallvalue + "!!");
				DazDebug.print("!!!DING!!!!!!!DING!!!!DING!!!!!!");
				
				callTrigger();
				
				starttime = System.currentTimeMillis();
				
				if (type.equals("once")){
					callOver = true;
				}
				
			}else{

		
			}
		}else{

			
		}
	}
	
}
