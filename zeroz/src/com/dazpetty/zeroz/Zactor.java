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
	
	public float jumpSpeed = 10;
	public float moveSpeed = 2;
	public int health = 100;
	public float rotdir = 0;
	public float gravMass = 0.25f;
	public float deceleration = 0.25f;
	public String blockedKey = "solid";
	public boolean initialized = false;
	public Vector2 position = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0);
	private TiledMapTileLayer collisionLayer;
	private boolean isFlying = false;
	



	
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart, FileHandle newtexture){
		position = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		texture = new Texture(newtexture);//Gdx.files.internal("data/player.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textureRegion = new TextureRegion(texture, 0, 0, 64, 128);
	}
	
	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));		
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	public void goLeft(){
		boolean canMove = true;
		//int i = 0;
		int istart = 0;
		if (velocity.y == 0.0f) istart = 1;
		for (int i=istart; i < (int)height; i++){
		//	System.out.print(istart);
			if (isCellBlocked(position.x, position.y+i)){
	  		  velocity.x = 0;
	  		  canMove = false;
			}
		}
		
		if(canMove){
			velocity.x -=  moveSpeed;
    		if (velocity.x < -10) velocity.x = -10;  
		}
	}

	public void goRight() {
		boolean canMove = true;
		for (int i=0; i < (int)height; i++){
        	if (isCellBlocked(position.x+1, position.y+1)){
        		velocity.x = 0;
        		canMove = false;
        	}	
		}
        	
		if(canMove){
      			velocity.x +=  moveSpeed;
          		if (velocity.x > 10) velocity.x = 10;  
      		}
	}

	public void goJump() {
		if (isGrounded){
			velocity.y += jumpSpeed;
      	  isGrounded = false;
		}
	}
	
	public void doPhysics(){
		float oldposx = position.x, oldposy = position.y;
		Vector2 newposition = new Vector2( position.x,position.y);
		
		
		if (velocity.x > 0f)velocity.x -= deceleration;
		if (velocity.x < 0f)velocity.x += deceleration;
		if (Math.abs(velocity.x) < 0.2) velocity.x = 0;
		
		newposition.x += (velocity.x * Gdx.graphics.getDeltaTime());
		
		int i = 0;
		while (i < height){
			
			if (!isFlying){
				if (!isGrounded){
					velocity.y -= gravMass;
				}
				newposition.y += (velocity.y * Gdx.graphics.getDeltaTime());
			}
			
			if (!isCellBlocked(newposition.x, newposition.y+0.1f+i)||!isCellBlocked(newposition.x+1, newposition.y+0.1f+i)){
				position.x = newposition.x;
				position.y = newposition.y;
			}else{
				System.out.print("blocked new:");
				System.out.print( newposition.x);
				System.out.print(",");
				System.out.print( newposition.y);
				System.out.print("blocked old:");
				System.out.print(position.x);
				System.out.print(",");
				System.out.print(position.y);
				System.out.println();
			}
			i++;
		}
		
		if (isCellBlocked(position.x, position.y)){
			isGrounded = true;
			velocity.y = 0;
		}else{
			isGrounded = false;
		}
		
		//if(isCellBlocked(position.x, position.y+1)){
		//	position.x = (int)oldposx;
		//	position.y = (int)oldposy;
		//}
	}
	
	public void dispose() {
		texture.dispose();
	}

}






