package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class HUDTarget {

	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	
	Texture texture = null;
	Texture texturedest = null;
	
	public TextureRegion textureRegion = null;
	public Sprite sprite;
	public Sprite spritedest;
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public FixtureDef fixtureDef;
	public boolean isAlive =false;
	public long spawntime = System.currentTimeMillis();
	
	public HUDTarget(){
		texture = new Texture(("data/gfx/hud/target.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion texturereg = new TextureRegion(texture, 0, 0, 64, 64);
		sprite = new Sprite(texturereg);
		sprite.setSize(1f, 1f);
		sprite.setPosition(worldpos.x, worldpos.y);
	}
	
	public void setDrawTarget(HumanEntity actor){
		worldpos.x = actor.worldpos.x - (actor.width/2);
		worldpos.y = actor.worldpos.y + (actor.height/2);
		sprite.setPosition(worldpos.x, worldpos.y);
		spawntime = System.currentTimeMillis();
		isAlive = true;
	}
	
	public void setDrawTarget(Drone actor){
		worldpos.x = actor.worldpos.x + (actor.width/2);
		worldpos.y = actor.worldpos.y + (actor.height/6);
		sprite.setPosition(worldpos.x, worldpos.y);
		spawntime = System.currentTimeMillis();
		isAlive = true;
	}
	
	public void dontDraw(){
		isAlive = false;
	}
	
	public int flashcount = 0;
	
	public boolean canDraw(){
		flashcount++;
		if (flashcount > 20){
			flashcount = 0;
		}
		float timenow = System.currentTimeMillis();
		if (timenow - spawntime < 1000 && isAlive == true &&  (flashcount < 10 )){
			return true;
		}else{
			return false;
		}
	}
	
	
}
