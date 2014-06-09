package com.dazpetty.zeroz.nodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.dazpetty.zeroz.assets.ZeroAssetManager;

public class PowerCable {

	/*
	 *  Power cables are used to indicate a relationship between locked doors and their triggers/destroyables
	 */
	public Sprite sprite;
	public Sprite destroyedsprite;
	public static TextureAtlas powerCableTexAtlas;
	private String currentAtlasKey = new String("0001");
	public boolean render = true;
	int framevalue = 1;
	private AtlasRegion atlasTexRegion;
	
	private int triggerkeyVal = 0;
	
	public PowerCable(float x, float y, int powercablevalue, int triggerkeyVal){
		this.triggerkeyVal = triggerkeyVal; 
		powerCableTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/power/powercable.atlas"));
		switch (powercablevalue){
			case 12:
				framevalue = 1;
				break;
			case 5:
				framevalue = 2;
				break;
			case 9:
				framevalue = 3;
				break;
			case 10:
				framevalue = 4;
				break;
			case 6:
				framevalue = 5;
				break;
			case 3:
				framevalue = 6;
				break;
			case 0:
				render = false;
				break;
		}
		

		currentAtlasKey = String.format("%04d", framevalue);
		atlasTexRegion = powerCableTexAtlas.findRegion(currentAtlasKey);

		sprite = new Sprite(atlasTexRegion);
		sprite.setPosition(x, y);
		sprite.setSize(1,1);
		
		framevalue += 6;
		

		currentAtlasKey = String.format("%04d", framevalue);
		atlasTexRegion = powerCableTexAtlas.findRegion(currentAtlasKey);

		destroyedsprite = new Sprite(atlasTexRegion);
		destroyedsprite.setPosition(x, y);
		destroyedsprite.setSize(1,1);
	}
	
	public void Trigger(int triggerkey){
		
		if (triggerkey == triggerkeyVal){
			sprite = destroyedsprite;
			
			
		}
	}
	
	
}
