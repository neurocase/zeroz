package com.dazpetty.zeroz.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAssetManager {
	

	public TextureAtlas projExplosionTexAtlas;

	public TextureAtlas fuseBoxExplosionTexAtlas;
	
	public Texture muzzleFlashTexture;
	public Texture projtex;

	public Texture projtexreg;
	
	/*
	 *  INPUT // HUD TEXTURES
	 */
	public static Texture dirbuttonstex;
	public static Texture shootbuttontex;
	public static Texture jumpbuttontex;
	
	
	public MyAssetManager(){
		
		dirbuttonstex = new Texture(
				Gdx.files.internal("data/gfx/hud/dirbuttons.png"));
		dirbuttonstex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		shootbuttontex = new Texture(
				Gdx.files.internal("data/gfx/hud/shootbutton.png"));
		shootbuttontex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		jumpbuttontex = new Texture(
				Gdx.files.internal("data/gfx/hud/jumpbutton.png"));
		jumpbuttontex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		
		muzzleFlashTexture = new Texture(
				Gdx.files.internal("data/gfx/effects/shotblast.png"));
		muzzleFlashTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		projExplosionTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/effects/explosion/explosion.atlas"));
		fuseBoxExplosionTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/effects/explosion/fuseexplosion.atlas"));
		projtex = new Texture(Gdx.files.internal("data/gfx/effects/bullet2.png"));
		projtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		
		
	}
}
