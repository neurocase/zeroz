package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;

public class DazDebug {

	int t = 0;

	public long milliTimeTick = System.nanoTime();
	
	
	
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
	public static void print(String str){
		System.out.println(str + "at:" + ((System.currentTimeMillis() - startTime)) + " milli secs");
	}
}
