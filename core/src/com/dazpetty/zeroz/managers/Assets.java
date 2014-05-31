package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	
	public static final AssetManager manager = new AssetManager();
	public static TextureParameter param = new TextureParameter();
	

	
	public Assets(){
		param.minFilter = TextureFilter.Linear;
		param.genMipMaps = true;
	}
	
	//public static final String projExplosionTexAtlas = "data/gfx/effects/explosion/explosion.atlas";
	public static final String dirbuttons = "data/gfx/hud/dirbuttons.png";
	public static final String shootbutton = "data/gfx/hud/shootbutton.png";
	public static final String jumpbutton = "data/gfx/hud/jumpbutton.png";

	public static void load(){
		//manager.load(projExplosionTexAtlas,TextureAtlas.class, param);
		manager.load(dirbuttons,Texture.class, param);
		manager.load(shootbutton,Texture.class, param);
		manager.load(jumpbutton,Texture.class, param);
	}
}
