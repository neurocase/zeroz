package com.dazpetty.zeroz.client;

import com.dazpetty.zeroz.core.ZerozGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}
	@Override
	public ApplicationListener getApplicationListener () {
		return new ZerozGame();
	}
}