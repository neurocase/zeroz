package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;

public class DazDebug {

	int t = 0;

	public long milliTimeTick = System.nanoTime();
	
	String lastTimeString = "";
	
	public void tick(){
	/*	t++;
	//long milliTimeNow = System.currentTimeMillis();
		if (t > 30){	
			long tickTime = System.currentTimeMillis();
				print("-  30 TICKs: " + (tickTime/30) + "      -");
			milliTimeTick = System.currentTimeMillis();
			t = 0;
		}*/
	}
	static long startTime = System.currentTimeMillis();
	public int stringCalledXTimes = 0;
	public static void print(String str){
	/*	if (str == lastTimeString){
			stringCalledXTimes++;
		}else{
			if (stringCalledXTimes > 1){
				System.out.println("REPEATED  [ " + stringCalledXTimes + " ]  TIMES");
			}
			
			*/
			System.out.println(str + "at:" + ((System.currentTimeMillis() - startTime)) + " milli secs");
		/*	stringCalledXTimes = 0;
		}
		
		lastTimeString = str;*/
		
	}
	
	public void update(){
		
	}
}
