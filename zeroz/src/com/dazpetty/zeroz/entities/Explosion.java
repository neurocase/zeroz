package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.Assets;
import com.dazpetty.zeroz.managers.MyAssetManager;

public class Explosion {
	




	private Vector2 worldpos;
	public Sprite sprite;
	public Sprite projexsprite;
	public Sprite fuseexsprite;
	public TextureRegion projExplosionTexRegion;
	public TextureRegion fuseExplosionTexRegion;
	
	int currentFrame = 1;
	
	public TextureAtlas fuseBoxExplosionTexAtlas;
	public TextureAtlas projExplosionTexAtlas;
	public int explosionid = 0;
	
	private float angle = 0;
	public boolean isAlive = true;
	private MyAssetManager assetMan;

	public Explosion(float x, float y, int id, float angle, MyAssetManager assetMan){
		this.assetMan = assetMan;
		isAlive = true;
		this.angle = angle;
		explosionid = id;
		currentFrame = 2;
		worldpos = new Vector2 (x,y);
		
		DazDebug.print("explosion at: x:" + x + " y:" + y + " id:" + id);
		projExplosionTexAtlas = assetMan.projExplosionTexAtlas;
		fuseBoxExplosionTexAtlas = assetMan.fuseBoxExplosionTexAtlas;
				
		
		AtlasRegion explosionTexRegion = projExplosionTexAtlas.findRegion("0001");
		AtlasRegion fuseExplosionTexRegion = projExplosionTexAtlas.findRegion("0001");


		projexsprite = new Sprite(explosionTexRegion);
		fuseexsprite = new Sprite(explosionTexRegion);
		switch (explosionid){
			case 0:
				sprite = projexsprite;
				break;
			case 1:
				sprite = fuseexsprite;
				break;
			default:
				sprite = projexsprite;
			
		}	
		
				
				
		
	
//		AtlasRegion explosionTexRegion = projExplosionTexAtlas.findRegion("0001");

		sprite = new Sprite(explosionTexRegion);
		sprite.setSize(2f, 2f);
		sprite.setPosition(x, y);
		sprite.setColor(1, 1, 1, 1);
		sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
	}
	private String currentAtlasKey = new String("0001");
	
	float alpha = 1;
	
	public void update(){
		currentFrame++;
		switch (explosionid){
			case 0:
				if (currentFrame > 20 ){
					if (alpha > 0){
						alpha -= 0.1;
						//alpha = 0.1f;
					}
				}
				if (currentFrame > 35 ){
					if (currentFrame % 2 == 0){
						currentFrame++;
						
					}
					
				}
				if (currentFrame > 47 ){
					sprite.rotate((float) (angle));
					alpha = 1;
					currentFrame = 1;	
					isAlive = false;
					
					
				}
				break;
			case 1:
				if (currentFrame > 99 ){
					currentFrame = 1;	
					alpha = 1;
					isAlive = false;

				}
				if (currentFrame > 65){
					alpha -= 0.05;
			
				}
				
				if (currentFrame % 2 == 0){
					currentFrame++;
				}
				break;
			default:
				if (currentFrame > 50 ){
					currentFrame = 1;	
					//alpha = 1;
					//sprite.setColor(1, 1, 1, 1);
				}
				break;
		}
		
		sprite.setColor(1, 1, 1, alpha);
		
	

		
		currentAtlasKey = String.format("%04d", currentFrame);
		sprite.setRegion(projExplosionTexAtlas.findRegion(currentAtlasKey));
	}

	
	
	public void dispose(){
		projExplosionTexAtlas.dispose();
	}
	
}
