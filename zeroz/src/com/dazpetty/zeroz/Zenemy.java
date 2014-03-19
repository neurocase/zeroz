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

	private static final int        FRAME_COLS = 5;         // #1
	private static final int        FRAME_ROWS = 5;         // #2
	  
	Animation                        walkAnimation;          // #3
	Texture                         walkSheet;              // #4
	TextureRegion[]                 walkFrames;             // #5
	SpriteBatch                     spriteBatch;            // #6
	TextureRegion                   currentFrame;           // #7
	
	public boolean isJumpy = false;
	public float shootDist = 4;
	float stateTime;
	
	public void spawn(int x, int y){
	/*	position.x = x;
		position.y = x;*/
		
		
	}
	
	public void update(Zplayer zplayer){
		
		float relativetoplayerx = zplayer.worldpos.x - worldpos.x; 
		
		if (relativetoplayerx < -shootDist){
			//if (!isShooter){
			goLeft();
		//	}//else if
		}else if (relativetoplayerx > shootDist+1){
			goRight();
		}
		
		if (isGrounded && isJumpy){
			goJump();
		}
		
	}
	
	public void create(){
	height = 2;
	width = 1.25f;
	
	walkSheet = new Texture(Gdx.files.internal("data/gfx/enforcer/run.png"));     // #9
	   TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 
			   FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);                                // #10
	   
	walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
	int index = 0;
	for (int i = 0; i < FRAME_ROWS; i++) {
	        for (int j = 0; j < FRAME_COLS; j++) {
	        	walkFrames[index++] = tmp[i][j];
	        }
		}
	walkAnimation = new Animation(0.025f, walkFrames);              
    spriteBatch = new SpriteBatch();
    stateTime = 0f;
	}
	
	@Override
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart){
		worldpos = actorstart;
		collisionLayer = cLayer;
		initialized = true;
	}
	
	public void shoot(float angle){

	}
	
	public void draw(Camera camera) {
		
		
		
		
		stateTime += Gdx.graphics.getDeltaTime();                       // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);      
        
        if(!isGoRight && currentFrame.isFlipX()){
        	currentFrame.flip(true,false);
        }
        if(isGoRight && !currentFrame.isFlipX()){
        	currentFrame.flip(true,false);
        }
        
        
        Vector3 enemypos = new Vector3(worldpos.x, worldpos.y, 0);
        camera.project(enemypos);
        
        
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, enemypos.x, enemypos.y);   
  
        spriteBatch.end();	
	}
	
	public void giveTarget(Vector3 newTargetVec, int xt, int yt){
	//	hasTarget = true;
		
//		targetVec.x = xt;
//		targetVec.y = yt;

	//	aimVec.x = newTargetVec.x;
	//	aimVec.y = newTargetVec.y;
	}
	
	public void updateTarget(Camera camera){
	/*	aimTargVec.x = targetVec.x;
		aimTargVec.y = targetVec.y;
		camera.project(aimTargVec);	
		aimVec.x = aimTargVec.x - Gdx.graphics.getWidth()/2 - 16;
		aimVec.y = aimTargVec.y - Gdx.graphics.getHeight()/2 - height*32;
		
		System.out.println();
		System.out.println(Math.abs(position.x - targetVec.x));
		if (Math.abs(position.x - targetVec.x) >18){
			hasTarget = false;
		}
		if (Math.abs(position.y - targetVec.y) >18){
			hasTarget = false;*/
		}
	
	public void dispose() {
		walkSheet.dispose();
		spriteBatch.dispose();
	}
		 
	

}
