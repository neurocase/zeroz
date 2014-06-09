package com.dazpetty.zeroz.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ZeroAssetManager {
	

	public static TextureAtlas projExplosionTexAtlas;

	public static TextureAtlas fuseBoxExplosionTexAtlas;
	
	public static TextureAtlas flamesTexAtlas;
	
	public static TextureAtlas powerCableTexAtlas;
	
	public static Texture muzzleFlashTexture;
	public static Texture projtex;

	public static Texture projtexreg;
	
	public static Texture flameturret;
	public static Texture wallturret;
	public static Texture wallturretbarrel;
	
	public static Texture hazardcrusher;
	
	public static Sound pistolSound;
	public static Sound shotgunSound;
	
	
	
	/*
	 *  INPUT // HUD TEXTURES
	 */
	public static Texture healthtex;
	public static Texture dirbuttonstex;
	public static Texture shootbuttontex;
	public static Texture jumpbuttontex;
	
	
	public ZeroAssetManager(){
		
		pistolSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/weapon/pistol.mp3"));
		shotgunSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/weapon/shotgun.mp3"));
		shotgunSound.setPitch(0, 0.5f);
		/*
		 * 		TEXTURES
		 */
		
		hazardcrusher = new Texture(
				Gdx.files.internal("data/gfx/crusher/crusherhazard.png"));
		hazardcrusher.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		flameturret = new Texture(
				Gdx.files.internal("data/gfx/wallturret/flameturret.png"));
		flameturret.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		wallturret = new Texture(
				Gdx.files.internal("data/gfx/wallturret/turretbase.png"));
		wallturret.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		wallturretbarrel = new Texture(
				Gdx.files.internal("data/gfx/wallturret/turretbarrel.png"));
		wallturretbarrel.setFilter(TextureFilter.Linear, TextureFilter.Linear); 
		 
		healthtex = new Texture(
				Gdx.files.internal("data/gfx/hud/health.png"));
		healthtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
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
		
		
		
		/*
		 * 			TEXTURE ATLAS
		 * 
		 */
		
		
		flamesTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/effects/flames.atlas"));
		
		powerCableTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/power/powercable.atlas"));
		
		
		projExplosionTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/effects/explosion/explosion.atlas"));
		fuseBoxExplosionTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/effects/explosion/fuseexplosion.atlas"));
		projtex = new Texture(Gdx.files.internal("data/gfx/effects/bullet2.png"));
		projtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		
		
		
		
		
	}
}
