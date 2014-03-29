package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Zenemy extends Zactor{

	
	public boolean isJumpy = false;
	public float shootDist = 4;
	float stateTime;
	
	
	public void spawn(int x, int y){
		
	}
	
	public void updateAI(Zplayer zplayer){
		type = "enemy";
		float relativetoplayerx = zplayer.worldpos.x - worldpos.x; 
		float relativetoplayery = zplayer.worldpos.y - worldpos.y;
		
		if (relativetoplayerx < 0){
			movingdirection = "left";
		}else{
			movingdirection = "right";
		}
		
		
		if (relativetoplayerx < -shootDist){
			goLeft();
		}else if (relativetoplayerx > shootDist+1){
			goRight();
		}
		
		if (relativetoplayery > 2){
			goJump();
		}
		
		if (isGrounded && isJumpy){
			goJump();
		}	
	}
}
