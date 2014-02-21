package com.dazpetty.zeroz;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "zeroz";
		cfg.useGL20 = false;
		cfg.width = 756;
		cfg.height = 480;
		
		new LwjglApplication(new ZerozGame(), cfg);
	}
}
