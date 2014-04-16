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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class EnemyMan extends Actor{

	public EnemyMan(Camera scam, World world, 
			TiledMapTileLayer cLayer, Vector2 actorstart, int id, HumanSprite humanSprite) {
		super(scam, world, true, cLayer, actorstart, id, humanSprite);
		// TODO Auto-generated constructor stub
	}

	
	/*public EnemyMan(Camera scam, world, true, Collisionlayer clayer,  Vector2 aistart) {
		//super(scam, body, world, true);
		// TODO Auto-generated constructor stub
	}*/

	public boolean isJumpy = false;
	public float shootDist = 4;
	float stateTime;
	float relativetoplayerx = 0; 
	float relativetoplayery = 0;
	public Vector2 relativepos = new Vector2(relativetoplayerx,relativetoplayery);
	//public float aimAtPlayer = 0;
	public void spawn(int x, int y){
		
	}
	
	public void updateAI(Actor zplayer){
		
		isAI = true;
		type = "enemy";
		float relativetoplayerx = zplayer.worldpos.x - worldpos.x ; 
		float relativetoplayery = zplayer.worldpos.y - worldpos.y;
		relativepos.x = relativetoplayerx;
		relativepos.y = relativetoplayery;
		targetWorldVec.x = zplayer.worldpos.x;
		targetWorldVec.y = zplayer.worldpos.y;
		aimAtPlayer = relativepos.angle();
		
		aimAngle = aimAtPlayer;
		
		/*if (relativetoplayerx > 0){
			movingdirection = "right";
		}else{
			movingdirection = "left";
		}*/
		
		
		if (relativetoplayerx < -shootDist){
			goLeft();
		}else if (relativetoplayerx > shootDist){
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
