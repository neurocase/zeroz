package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

abstract interface GameObject {
	
	
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	Texture texture = null;
	public float height = 2;
	public float width = 1.25f;
	public TextureRegion textureRegion = null;
	public float rotDir = 0;
	 
	
	
}
