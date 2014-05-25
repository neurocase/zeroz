package com.dazpetty.zeroz.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dazpetty.zeroz.core.ZerozGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Zero-Z";
		config.width = 1280;
		config.height = 720;
		//config.width(1280);
		//config.height(720);
		new LwjglApplication(new ZerozGame(), config);
	}
}
