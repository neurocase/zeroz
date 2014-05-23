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
	
	
	
	public MyAssetManager(){
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
