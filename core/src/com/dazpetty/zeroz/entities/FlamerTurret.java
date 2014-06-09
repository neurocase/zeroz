package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dazpetty.zeroz.assets.ZeroAssetManager;
import com.dazpetty.zeroz.core.DazDebug;
import com.dazpetty.zeroz.managers.EntityManager;

public class FlamerTurret {

	public boolean isOpen = false;
	
	
	//public Vector2 screenpos = new Vector2(0,0);
//	public Vector2 velocity = new Vector2(0,0); 
	//public float height = 2;
	//public float width = 1.25f;
	//public TextureRegion textureRegion = null;
	
	public Vector2 worldpos = new Vector2(0,0);
	
	public float rotDir = 0;
	
	public Sprite sprite;
	
	
	public Sprite flamerturretsprite;
	public TextureRegion flamerturretTexRegion;
	public Texture flamerturretTexture;
	
	
	public TextureAtlas flamesTextureAtlas;
	public Sprite flamessprite;
	
	
	
	public Sprite turretbasesprite;
	public TextureRegion turretBaseTexRegion;
	public Texture turretBaseTexture;
	
 
	public Sprite turretbarrelsprite;
	public TextureRegion turretbarrelTexRegion;
	public Texture turretbarreltexture;
		
	
	
	private int currentFrame = 1;
	private String currentAtlasKey = new String("0001");
	
	
	public Body body;
	public FixtureDef fixtureDef;
	public Fixture turretfix;
	
	public Body sensorBody;
	public FixtureDef sensorfixtureDef;
	public Fixture turretsensorfix;
	
	public boolean isAlive;
	public int keyValue;
	public boolean openDoor = false;
	
	public String TurretType = "";
	
	public float maxangle = 0;
	public float minangle = 0;
	
	public TextureAtlas flamesTexAtlas;
	
	public PawnEntity zplayer;
	
	public EntityManager entityMan;
	
	private AtlasRegion flamesAtlasTexRegion;
	private String type = "";
	
//	public String currentAtlasKey = "";
	
	
	int framevalue = 0;
	//Sprite flamessprite;
	
	public FlamerTurret(float x, float y, String type, World world, EntityManager entityMan){
		this.type = type;
		this.entityMan = entityMan;
		
		worldpos.x = x;
		worldpos.y = y;
		
		currentFrame = 20;
		
	
		
		BodyDef turretbodyDef = new BodyDef();
		turretbodyDef.position.set(worldpos.x+0.5f, worldpos.y+0.5f);
		turretbodyDef.type = BodyType.StaticBody;
		body = world.createBody(turretbodyDef); 
		
		FixtureDef turretfixDef = new FixtureDef();
		PolygonShape pBox = new PolygonShape();
		pBox.setAsBox(0.5f, 0.5f);
		turretfixDef.shape = pBox;    
		turretfix = body.createFixture(turretfixDef);
		
		turretfix.setUserData(this);	
		
		turretfixDef.friction = 0;
		
		float sensoroffset = 3;
		
	
		
		if (type.equals("left")){
			sensoroffset = -sensoroffset +1;
		}
		
		
		BodyDef turretbodysensorDef = new BodyDef();
		turretbodysensorDef.position.set(worldpos.x+sensoroffset, worldpos.y+0.5f);
		turretbodysensorDef.type = BodyType.StaticBody;
		body = world.createBody(turretbodysensorDef); 
		
		FixtureDef turretsensorfixDef = new FixtureDef();
		PolygonShape sensBox = new PolygonShape();
		sensBox.setAsBox(2f, 1f);
		turretsensorfixDef.shape = sensBox;   
		turretsensorfixDef.isSensor = true;
		turretsensorfix = body.createFixture(turretsensorfixDef);
		turretsensorfix.setUserData(this);	
		turretsensorfix.setSensor(true);
		

		flamerturretTexture = ZeroAssetManager.flameturret;
		flamerturretTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion flamerturretTexRegion = new TextureRegion(flamerturretTexture, 0, 0, 128,
				64);
		flamerturretsprite = new Sprite(flamerturretTexRegion);
		flamerturretsprite.setPosition(x, y);
		flamerturretsprite.setSize(2, 1);
		
		
		
		flamesTexAtlas = new TextureAtlas(
				Gdx.files.internal("data/gfx/effects/flames.atlas"));
		
		currentAtlasKey = String.format("%04d", currentFrame);
		flamesAtlasTexRegion = flamesTexAtlas.findRegion(currentAtlasKey);

		flamessprite = new Sprite(flamesAtlasTexRegion);
		flamessprite.setPosition(x+1.35f, y+0.3f);
		flamessprite.setSize(4f,1.5f);
		
		
		
		//flamesTextureAtlas = ZeroAssetManager.flamesTexAtlas;

		//AtlasRegion atlasTexRegion = flamesTextureAtlas.findRegion("0001");
		//flamessprite = new Sprite(atlasTexRegion);

		if (type.equals("left")){
			flamerturretsprite.flip(true, false);
			flamerturretsprite.setPosition(x-1, y);
			flamessprite.flip(false, false);
			flamessprite.setPosition(x-4.35f, y+0.3f);
			
		}

		sprite = flamerturretsprite;
	}
	
	
	public void attemptShoot(float ang) {

		if (isAlive) {
			
		}
	}
	
	
	
	float aimangle = 0;
	public boolean finished = false;
	public long lasttimeshoot = System.currentTimeMillis();
	public boolean canHurtPlayer = false;
	
	
	public void update(){
		//DazDebug.print("UPDATING FLAMER TURRET");
		if (finished){
			if (System.currentTimeMillis() - lasttimeshoot > 750){
				finished = false;
			}
		}
		
		if (!finished){
			currentFrame++;
			if (currentFrame > 29){
				finished = true;
				currentFrame = 1;
				lasttimeshoot = System.currentTimeMillis();
				canHurtPlayer = false;
			}else if(currentFrame > 10){
				canHurtPlayer = true;
			}
			currentAtlasKey = String.format("%04d", currentFrame);
			flamesAtlasTexRegion = flamesTexAtlas.findRegion(currentAtlasKey);
			flamessprite.setRegion(flamesAtlasTexRegion);
			if (type.equals("left")){
				flamessprite.flip(true, false);
			}
		}
		
		
	}
	/*
	public float rotate(){
		//float rotang = 0;

		//Vector2 tmpVec = new Vector2(worldpos.x, worldpos.y);
		return 
	}*/
	
	
	
	public void draw(SpriteBatch batch){
		/*if (TurretType.equals("flamer")){
				flamerturretsprite.draw(batch);
		}*/
		if (TurretType.equals("wallturret")){
				turretbarrelsprite.draw(batch);
				turretbasesprite.draw(batch);
				turretbarrelsprite.setRotation(aimangle);
		}
	}


	public void shoot() {
	/*	if (finished){
			currentFrame = 1;
			finished = false;
		}*/
	}


	
}