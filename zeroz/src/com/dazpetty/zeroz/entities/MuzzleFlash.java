package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.dazpetty.zeroz.managers.MyAssetManager;

public class MuzzleFlash {
	public Vector2 worldpos;
	public long timeshoot;
	public boolean isAlive = true;
	
	private Texture muzzTexture;
	private TextureRegion muzzTexRegion;
	
	public Sprite sprite;
	
	public MuzzleFlash(PawnEntity parententity, MyAssetManager assetMan){
		worldpos = parententity.worldpos;
		timeshoot = System.currentTimeMillis();
		muzzTexture = assetMan.muzzleFlashTexture;
		TextureRegion muzzTexRegion = new TextureRegion(muzzTexture, 0, 0, 32,
				32);
		
		sprite = new Sprite (muzzTexRegion);
		sprite.setSize(1,1);
		sprite.setPosition(worldpos.x, worldpos.y);
	}
	long timenow = 0;
	public void update(){
		long timenow = System.currentTimeMillis();
		if (timenow - timeshoot > 50){
			isAlive = false;
		}
	}
	
	
}
