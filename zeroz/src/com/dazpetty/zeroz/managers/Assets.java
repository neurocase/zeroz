package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Assets {
	
	
	public static final AssetManager manager = new AssetManager();
	
	public static TextureParameter param = new TextureParameter();
	
	
	public Assets(){
		param.minFilter = TextureFilter.Linear;
		param.genMipMaps = true;
		//param.
	}
	
	public static final String dirbuttons = "data/gfx/hud/dirbuttons.png";
	
	public static void load(){
		manager.load(dirbuttons,Texture.class, param);
	
	}
	
	
}
