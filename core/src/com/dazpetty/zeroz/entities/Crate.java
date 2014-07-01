package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.assets.ZeroAssetManager;

public class Crate {

	public Vector2 worldPos = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0);
	
	Body body;
	BodyDef bodyDef;
	FixtureDef fixtureDef;
	
	public Texture texture;
	
	
	public Sprite sprite;
	
	public Crate(float x, float y, World world, String type){
		
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x,y);
		
		body = world.createBody(bodyDef);
		
		fixtureDef = new FixtureDef(); 
		
		fixtureDef.restitution = 0.1f;
		fixtureDef.density = 0.5f;
		if (type.equals("metal")){
			fixtureDef.density = 1.4f;
		}
	    PolygonShape pBox = new PolygonShape();
	    pBox.setAsBox(1f, 1f);
	    
	    body.setUserData("crate");
	    fixtureDef.shape = pBox;
	    

	    Fixture fixture = body.createFixture(fixtureDef);
	    fixture.setUserData("dynamic");
	    texture = ZeroAssetManager.cratetex;
	    if (type.equals("metal")){
	    	texture = ZeroAssetManager.metalcratetex;
		}
	    TextureRegion textureReg = new TextureRegion(texture, 0, 0, 128, 128);	    
	    sprite = new Sprite(textureReg);
	    sprite.setSize(2f, 2f);
	    sprite.setOriginCenter();
	    body.setFixedRotation(false);
	    worldPos = body.getPosition();
	//    body.setTransform(worldPos, 45);
	    pBox.dispose();
	}
	
	public void update(){
		worldPos = body.getPosition();
		sprite.setCenter(worldPos.x, worldPos.y);
		
		sprite.setRotation((float) Math.toDegrees(body.getAngle()));
	}
	
	
}
