package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Zbullet extends Zobject{
	
	public boolean initialized = false;
	protected TiledMapTileLayer collisionLayer;
	public Texture texture;
	private Sprite sprite;
	public Vector2 position = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	public Vector2 aimVec = new Vector2(0,0);
	public float rotDir = 0;
	
	public void create(TiledMapTileLayer cLayer, Vector2 posVec, Vector2 aimVecIn){
		collisionLayer = cLayer;
		initialized = true;
		
		//Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2
		
		System.out.print(aimVec.angle());
		texture = new Texture(Gdx.files.internal("data/dpad.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 128, 128);
		
		sprite = new Sprite(textureRegion);
		sprite.setSize(1f,1f);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(0f,0f);
	}
	
	public void update(){
			
	};
	
	

}
