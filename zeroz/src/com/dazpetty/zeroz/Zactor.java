package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
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

public class Zactor {

	public boolean isJump = true;
	public boolean isAlive = true;
	public boolean isGrounded = true;
	public int actorHeight = 2;
	public float jumpSpeed = 10;
	public float moveSpeed = 2;
	public int health = 100;
	public float rotdir = 0;
	public float gravMass = 0.25f;
	public float deceleration = 0.25f;
	public String blockedKey = "solid";
	public boolean initialized = false;
	public Vector2 actorpos = new Vector2(0,0);
	public Vector2 actorvel = new Vector2(0,0);
	private TiledMapTileLayer collisionLayer;
	private boolean isFlying = false;
	Texture actorTex;
	public TextureRegion actorTexReg;
	
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart, FileHandle actorTex){
		actorpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		this.actorTex = new Texture(actorTex);//Gdx.files.internal("data/player.png"));
		this.actorTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		actorTexReg = new TextureRegion(this.actorTex, 0, 0, 64, 128);
	}
	
	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x), (int) (y));		
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}
	
	public void goLeft(){
		boolean canMove = true;
		//int i = 0;
		int istart = 0;
		if (actorvel.y == 0.0f) istart = 1;
		for (int i=istart; i < (int)actorHeight; i++){
		//	System.out.print(istart);
			if (isCellBlocked(actorpos.x, actorpos.y+i)){
	  		  actorvel.x = 0;
	  		  canMove = false;
			}
		}
		
		if(canMove){
			actorvel.x -=  moveSpeed;
    		if (actorvel.x < -10) actorvel.x = -10;  
		}
	}

	public void goRight() {
		boolean canMove = true;
		for (int i=0; i < (int)actorHeight; i++){
        	if (isCellBlocked(actorpos.x+1, actorpos.y+1)){
        		actorvel.x = 0;
        		canMove = false;
        	}	
		}
        	
		if(canMove){
      			actorvel.x +=  moveSpeed;
          		if (actorvel.x > 10) actorvel.x = 10;  
      		}
	}

	public void goJump() {
		if (isGrounded){
			actorvel.y += jumpSpeed;
      	  isGrounded = false;
		}
	}
	
	public void doPhysics(){
		float oldposx = actorpos.x, oldposy = actorpos.y;
		Vector2 newactorpos = new Vector2( actorpos.x,actorpos.y);
		
		
		if (actorvel.x > 0f)actorvel.x -= deceleration;
		if (actorvel.x < 0f)actorvel.x += deceleration;
		if (Math.abs(actorvel.x) < 0.2) actorvel.x = 0;
		
		newactorpos.x += (actorvel.x * Gdx.graphics.getDeltaTime());
		
		int i = 0;
		while (i < actorHeight){
			
			if (!isFlying){
				if (!isGrounded){
					actorvel.y -= gravMass;
				}
				newactorpos.y += (actorvel.y * Gdx.graphics.getDeltaTime());
			}
			
			if (!isCellBlocked(newactorpos.x, newactorpos.y+0.1f+i)||!isCellBlocked(newactorpos.x+1, newactorpos.y+0.1f+i)){
				actorpos.x = newactorpos.x;
				actorpos.y = newactorpos.y;
			}else{
				System.out.print("blocked new:");
				System.out.print( newactorpos.x);
				System.out.print(",");
				System.out.print( newactorpos.y);
				System.out.print("blocked old:");
				System.out.print(actorpos.x);
				System.out.print(",");
				System.out.print(actorpos.y);
				System.out.println();
			}
			i++;
		}
		
		if (isCellBlocked(actorpos.x, actorpos.y)){
			isGrounded = true;
			actorvel.y = 0;
		}else{
			isGrounded = false;
		}
		
		//if(isCellBlocked(actorpos.x, actorpos.y+1)){
		//	actorpos.x = (int)oldposx;
		//	actorpos.y = (int)oldposy;
		//}
	}
	
	public void dispose() {
		actorTex.dispose();
	}

}






