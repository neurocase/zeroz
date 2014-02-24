package com.dazpetty.zeroz;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Zplayer extends Zactor {

	
	private static final int        FRAME_COLS = 5;         // #1
	private static final int        FRAME_ROWS = 5;         // #2
	  
	Animation                       walkAnimation;          // #3
	Texture                         walkSheet;              // #4
	TextureRegion[]                 walkFrames;             // #5
	SpriteBatch                     spriteBatch;            // #6
	TextureRegion                   currentFrame;           // #7
	float stateTime;
	
	
	
	Set<Zbullet> bullets= new HashSet<Zbullet>();
	
	
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
    //walkFrames[walkFrames.length -1] = null;
	}
	
	@Override
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart){//, FileHandle newtexture){
		position = actorstart;
		collisionLayer = cLayer;
		initialized = true;
	}
	
	public void shoot(float angle){
		Zbullet bullet = new Zbullet();
		bullets.add(bullet);
		bullet.create(collisionLayer, position, angle);
	/*	System.out.print(aimVec.x);
		System.out.print(",");
		System.out.print(aimVec.y);
		System.out.print(":-player:");
		System.out.print(aimVec.x - position.x);
		System.out.print(",");
		System.out.print(aimVec.y - this.position.y);*/
	}
	
	public void draw(float x, float y){
		
		
		
		
		stateTime += Gdx.graphics.getDeltaTime();                       // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);      
        
        if(!isGoRight && currentFrame.isFlipX()){
        	currentFrame.flip(true,false);
        }
        if(isGoRight && !currentFrame.isFlipX()){
        	currentFrame.flip(true,false);
        }
        
        for (Zbullet zb : bullets){
        //		zb.update();
		}
        
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2);//-height*32);    
        for (Zbullet zb : bullets){
			spriteBatch.draw(zb.texture, zb.position.x, zb.position.y);
		}
        spriteBatch.end();	
	}
}
