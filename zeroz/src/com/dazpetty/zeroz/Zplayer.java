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
	walkSheet = new Texture(Gdx.files.internal("data/gfx/enforcer/run.png"));     // #9
	///home/case/Dropbox/android/workspace/zeroz/zeroz-desktop/bin/
	   TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / 
			   FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);                                // #10
	   
	//   walkSheet.
	walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
	int index = 0;
	for (int i = 0; i < FRAME_ROWS; i++) {
	        for (int j = 0; j < FRAME_COLS; j++) {
	        	//if (i != FRAME_ROWS && j != FRAME_COLS){
	                walkFrames[index++] = tmp[i][j];
	        	//}
	        }
		}
	walkAnimation = new Animation(0.025f, walkFrames);              // #11
	//walkAnimation.
	//walkAnimation = new Animation              // #11
    spriteBatch = new SpriteBatch();                                // #12
    stateTime = 0f;   
    
  //  spriteBatch.setSize(1f,2f);//0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
  //  spriteBatch.setOrigin(playersprite.getWidth()/2, playersprite.getHeight()/2);
   // spriteBatch.setPosition(2f,1f);//-sprite.getWidth()/2, -sprite.getHeight()/2)
	}
	
	@Override
	public void initActor(TiledMapTileLayer cLayer, Vector2 actorstart){//, FileHandle newtexture){
		position = actorstart;
		collisionLayer = cLayer;
		initialized = true;
		//texture = new Texture(newtexture);//Gdx.files.internal("data/player.png"));
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		//textureRegion = new TextureRegion(texture, 0, 0, 64, 128);
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
