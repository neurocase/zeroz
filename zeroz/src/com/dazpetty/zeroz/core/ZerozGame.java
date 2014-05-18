package com.dazpetty.zeroz.core;

import java.util.LinkedHashSet;
import java.util.Set;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ZerozGame extends Game {
	
	  public SpriteBatch batch;
	  public BitmapFont font;
	  

	  
	  public Sprite leftarrowsprite;
	  public Sprite rightarrowsprite;
	  public Sprite buttonsprite;
	  
	  public Texture dirbuttonstex;
	  public Texture rightarrowtex;
	  public Texture buttontex;
	  
	  public int level = 0;
	  public int TOTAL_LEVELS = 10;
	  
	  public void nextLevel(){
		  level++;
	  }
	  
	  public void setLevel(int setLevel){
		  level = setLevel;
	  }
	  
	@Override
	public void create() {
		
		dirbuttonstex = new Texture(
				Gdx.files.internal("data/gfx/hud/dirbuttons.png"));
		dirbuttonstex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		rightarrowtex = new Texture(
				Gdx.files.internal("data/gfx/buttons/right.png"));
		rightarrowtex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		buttontex = new Texture(
				Gdx.files.internal("data/gfx/buttons/circle.png"));
		buttontex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

	//	TextureRegion leftarrowtexreg = new TextureRegion(leftarrowtex, 0, 0,
		//		32, 32);
		
		TextureRegion rightarrowtexreg = new TextureRegion(rightarrowtex, 0, 0,
				32, 32);
		TextureRegion buttontexreg = new TextureRegion(buttontex, 0, 0, 32, 32);
		
		
	/*	leftarrowsprite = new Sprite(leftarrowtexreg);
		leftarrowsprite.setSize(2f, 1f);
		leftarrowsprite.setOrigin(0, 0);
		leftarrowsprite.setPosition(0f, 0f);*/

		//rightarrowsprite = new Sprite(rightarrowtexreg);
		//rightarrowsprite.setSize(2f, 1f);
		//rightarrowsprite.setOrigin(0, 0);
		//rightarrowsprite.setPosition(0f, 0f);

		buttonsprite = new Sprite(buttontexreg);
		buttonsprite.setSize(2f, 1f);
		buttonsprite.setOrigin(0, 0);
		buttonsprite.setPosition(0f, 0f);
		
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new MainMenu(this));
        
        

        
	}

	@Override
	public void dispose() {
		 batch.dispose();
	     font.dispose();
	}

	@Override
	public void render() {
		 super.render(); //important!
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}