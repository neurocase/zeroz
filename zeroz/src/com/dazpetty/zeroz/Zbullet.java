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
	//public float rotDir = 0;
	double speed = 2;
	
	public void create(TiledMapTileLayer cLayer, Vector2 posVec, float angle){
		collisionLayer = cLayer;
		initialized = true;
		position = posVec;
		//Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2
		
		velocity.x = (float) (speed * Math.cos(angle));
		velocity.y = (float) (speed * Math.sin(angle));
		
		
		System.out.print(aimVec.angle());
		texture = new Texture(Gdx.files.internal("data/dpad.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 128, 128);
		
		sprite = new Sprite(textureRegion);
		sprite.setSize(1f,1f);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(position.x,position.y);
	}
	
	public void update(){
			position.x +=  velocity.x;
			position.y +=  velocity.y;
	};
	
	

}
