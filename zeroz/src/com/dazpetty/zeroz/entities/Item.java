package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Item {

	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	Texture texture = null;
	public float height = 2;
	public float width = 1.25f;
	public TextureRegion textureRegion = null;
	public float rotDir = 0;

	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public Sprite sprite;
	public FixtureDef fixtureDef;
	public boolean isAlive = true;
	public int addHealth =  20;
	public boolean isWeapon = false;
	public int itemWeaponNumber = 0;
	
	public void removeItem(){
		isAlive = false;
	}
	
	/*
	 *  HEALTH ITEM
	 *  0 = health
	 *   
	 */	
	
	
	public void dropWeapon(int weaponid){
		
		TextureRegion itemTexReg = new TextureRegion(texture,
				0, 0, 64, 64);
		
		switch (weaponid) {
			case 1:
				itemType = "uzi";
				itemWeaponNumber = 1;
				texture = new Texture(
						Gdx.files.internal("data/gfx/items/uzi.png"));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
				itemTexReg = new TextureRegion(texture,
						0, 0, 128, 32);
				isWeapon = true;
				break;
			case 2:
				itemType = "shotgun";
				itemWeaponNumber = 2;
				texture = new Texture(
						Gdx.files.internal("data/gfx/items/shotgun.png"));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
				itemTexReg = new TextureRegion(texture,
						0, 0, 128, 32);
				isWeapon = true;
				break;
			default:
		}

		sprite = new Sprite(itemTexReg);
		if(itemType.equals("shotgun") || itemType.equals("uzi")){
			sprite.setSize(1.5f, 0.5f);
			
		}else{
		sprite.setSize(1f, 1f);
		}
		sprite.setPosition(worldpos.x, worldpos.y);
		
	}
	
	public String itemType = "";
	public int id = 0;
	
	public Item(float x, float y, int id, String itemType ,World world){
		/*
		 *  HEALTH ITEM
		 */	
		this.id = id;
		this.itemType = itemType;
		
		worldpos = new Vector2(x,y);
		texture = new Texture(
				Gdx.files.internal("data/gfx/items/healthkit.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion itemTexReg = new TextureRegion(texture,
				0, 0, 64, 64);
		
		if(itemType.equals("shotgun")){
			itemWeaponNumber = 2;
			texture = new Texture(
					Gdx.files.internal("data/gfx/items/shotgun.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
			itemTexReg = new TextureRegion(texture,
					0, 0, 128, 32);
			isWeapon = true;
		}

		if(itemType.equals("uzi")){
			itemWeaponNumber = 1;
			texture = new Texture(
					Gdx.files.internal("data/gfx/items/uzi.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);	
			itemTexReg = new TextureRegion(texture,
					0, 0, 128, 32);
			isWeapon = true;
		}
		


		
		sprite = new Sprite(itemTexReg);
		if(itemType.equals("shotgun") || itemType.equals("uzi")){
			sprite.setSize(1.5f, 0.5f);
			
		}else{
		sprite.setSize(1f, 1f);
		}
		sprite.setPosition(x, y);
	
	}


	/*public void Pickup(HumanEntity zplayer) {
		if (isWeapon){
			int holdId = zplayer.weapon.weaponid;
			zplayer.weapon.giveWeapon(this);
			dropWeapon(holdId);
		}
	}*/
	
	
}
