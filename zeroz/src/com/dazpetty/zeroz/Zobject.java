package com.dazpetty.zeroz;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Zobject {
	
	
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 screenpos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0); 
	Texture texture;
	public float height = 2;
	public float width = 1.25f;
	public TextureRegion textureRegion;
	public float rotDir = 0;
	public boolean isFriendly = true;
	
	 
	
	
}
