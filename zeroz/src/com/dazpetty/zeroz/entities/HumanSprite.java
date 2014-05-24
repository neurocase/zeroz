package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class HumanSprite {

	public Sprite rightarmsprite;
	public Sprite armuzisprite;
	public Sprite armswordsprite;
	public Sprite aimladdersprite;
	public String type = null; 

	public float aimAngle = 0;
	public Sprite idlesprite;
	public Sprite runsprite;
	public Sprite backwalksprite;
	public Sprite crouchsprite;
	public Sprite crouchbacksprite;
	public Sprite upladdersprite;
	public Sprite deathsprite;
	public Sprite sprite;
	
	public TextureRegion armTexRegion;

	public Texture armSwordTexture;
	public Texture armUziTexture;
	public Texture aimLadderTexture;
	public Texture armShotgunTexture;

	public TextureAtlas runTextureAtlas;
	public TextureAtlas idleTextureAtlas;
	public TextureAtlas backWalkTextureAtlas;
	public TextureAtlas crouchTextureAtlas;
	public TextureAtlas crouchBackTextureAtlas;
	public TextureAtlas upLadderTextureAtlas;
	public TextureAtlas deathTextureAtlas;

	float stateTime;

	public HumanSprite(){
		aimLadderTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/ladderlean.png"));
		aimLadderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		armShotgunTexture = new Texture(
				Gdx.files.internal("data/gfx/weapon/armshotgun.png"));
		armShotgunTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion armShotgunTextureRegion = new TextureRegion(armShotgunTexture, 0,
				0, 128, 64);
		
		
		armUziTexture = new Texture(
				Gdx.files.internal("data/gfx/weapon/armuzi.png"));
		armUziTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion armUziTexRegion = new TextureRegion(armUziTexture, 0,
				0, 128, 64);
		
		armSwordTexture = new Texture(
				Gdx.files.internal("data/gfx/zman/armsword.png"));
		armSwordTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion armSwordTexRegion = new TextureRegion(armSwordTexture, 0,
				0, 64, 64);
	
		TextureRegion aimLadderTexRegion = new TextureRegion(aimLadderTexture,
				0, 0, 128, 128);
	
		
	
		aimladdersprite = new Sprite(aimLadderTexRegion);
		aimladdersprite.setPosition(-10, -10);// Gdx.graphics.getWidth()/2-width*32,Gdx.graphics.getHeight()/2);
		aimladdersprite.scale(1f);
	
		armuzisprite = new Sprite(armUziTexRegion);
		armuzisprite.setSize(2, 1);
		armuzisprite.setOrigin(((armuzisprite.getWidth() * 0.77f)),
				armuzisprite.getHeight() / 2);
		armuzisprite.setPosition(-10, -10);
		
		armswordsprite = new Sprite(armSwordTexRegion);
		armswordsprite.setSize(1, 1);
		armswordsprite.setOrigin(((armswordsprite.getWidth() * 0.77f)),
				armswordsprite.getHeight() / 2);
		armswordsprite.setPosition(-10, -10);
	
		//armsprite = new Sprite();
	
		runTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/run.atlas"));
		idleTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/idle.atlas"));
	
		backWalkTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/backwalk.atlas"));
		
		crouchTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/crouch.atlas"));
		crouchBackTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/crouchback.atlas"));
		
		upLadderTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/upladder.atlas"));
		
	
		deathTextureAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/zman/death1.atlas"));
	
		AtlasRegion runTexRegion = runTextureAtlas.findRegion("0000");
		AtlasRegion idleTexRegion = idleTextureAtlas.findRegion("0000");
		AtlasRegion crouchTexRegion = crouchTextureAtlas.findRegion("0000");
		AtlasRegion crouchBackTexRegion = crouchBackTextureAtlas.findRegion("0000");
		AtlasRegion backWalkTexRegion = backWalkTextureAtlas.findRegion("0000");
		AtlasRegion upLadderTexRegion = upLadderTextureAtlas.findRegion("0000");
		AtlasRegion deathTexRegion = deathTextureAtlas.findRegion("0000");
	
		runsprite = new Sprite(runTexRegion);
		//runsprite.setPosition(-10, -10);
		runsprite.scale(1f);
	
		idlesprite = new Sprite(idleTexRegion);
		//idlesprite.setPosition(-10, -10);
		idlesprite.scale(1f);
		
		crouchbacksprite = new Sprite(crouchBackTexRegion);
		//crouchbacksprite.setPosition(-10, -10);
		crouchbacksprite.scale(1f);
		
		crouchsprite = new Sprite(crouchTexRegion);
		//crouchsprite.setPosition(-10, -10);
		crouchsprite.scale(1f);
		
		backwalksprite = new Sprite(backWalkTexRegion);
		//backwalksprite.setPosition(-10, -10);
		backwalksprite.scale(1f);
	
		deathsprite = new Sprite(deathTexRegion);
		//deathsprite.setPosition(-32, -10);
		deathsprite.scale(1f);
		
		upladdersprite = new Sprite(upLadderTexRegion);
		//upladdersprite.setPosition(-32, -10);
		upladdersprite.scale(1f);
	}
	
	public void dispose(){
		runTextureAtlas.dispose();
		idleTextureAtlas.dispose();
		backWalkTextureAtlas.dispose();
		crouchTextureAtlas.dispose();
		crouchBackTextureAtlas.dispose();
		upLadderTextureAtlas.dispose();
		deathTextureAtlas.dispose();
		armUziTexture.dispose();
		aimLadderTexture.dispose();
		 
	}
}
