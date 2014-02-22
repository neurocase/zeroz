package com.dazpetty.zeroz;

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
	
	
	
	public void create(){
	height = 2;
	width = 1.25f;
	
	/*
	 *   WHAT IF, I Create a Sprite sheet for all animations, each on a line
	 *   and can use TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 
	 *	 FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);                                // #10
	 *
	 *   
	 * 
	 */
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
	
	public void draw(float x, float y){
		stateTime += Gdx.graphics.getDeltaTime();                       // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);      
        
        if(!isGoRight && currentFrame.isFlipX()){
        	currentFrame.flip(true,false);// #16
        }
        if(isGoRight && !currentFrame.isFlipX()){
        	currentFrame.flip(true,false);// #16
        }   
        spriteBatch.begin();
        spriteBatch.draw(currentFrame, Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2);//-height*32);                         // #17
        spriteBatch.end();	
	}
}
