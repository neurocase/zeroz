package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

/*
 * Zactor: Zactor is the Actor class, all game entities which move are actors,
 * actor is the base class for enemies, ai and the player.
 * 
 */

public class Zactor extends Zobject{

	public boolean isJump = true;
	public boolean isAlive = true;
	public boolean isGrounded = true;
	
	public float jumpSpeed = 17;
	public float moveSpeed = 2;
	public int health = 100;
	public float rotdir = 0;
	public float gravMass = 0.45f;
	public float deceleration = 0.25f;
	public String blockedKey = "collision";
	public boolean initialized = false;
	public Vector2 position = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0);
	protected TiledMapTileLayer collisionLayer;
	protected boolean isFlying = false;
	protected boolean isGoRight = true;
	
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart){
		position = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRegion = new TextureRegion(texture, 0, 0, 64, 128);
	}
	
	protected boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));		
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	public void goLeft(){
			velocity.x -=  moveSpeed;
    		if (velocity.x < -10) velocity.x = -10;  
    		isGoRight = false;
	}

	public void goRight() {
      			velocity.x +=  moveSpeed;
          		if (velocity.x > 10) velocity.x = 10;  
          		isGoRight = true;

	}

	public void goJump() {
		if (isGrounded){
			velocity.y += jumpSpeed;
      	  isGrounded = false;
		}
	}
	
	public void dispose() {
	//	texture.dispose();
	}

}






