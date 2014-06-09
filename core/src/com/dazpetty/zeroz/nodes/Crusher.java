package com.dazpetty.zeroz.nodes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.assets.ZeroAssetManager;
import com.dazpetty.zeroz.core.DazDebug;

public class Crusher {

	
	public Vector2 worldpos;
	public Vector2 Movement;
	public Vector2 startpos;
	public Vector2 moveto;
	
	public Texture texture;
	public TextureRegion texregion;
	public Sprite sprite;
	
	public Body body;
	public Fixture fixture;
	//public FixtureDef fixtureDef;
	
	public int speed = 1;
	
	
	public Shape pBox;
	
	public Crusher(float x, float y, String type, int speed, int delay, int movex, int movey, World world){
		
		x +=0.525;
		y +=3f;
		
		worldpos = new Vector2(x,y);
		startpos = new Vector2(x,y);
		moveto = new Vector2(x+movex,y+movey);
		Movement = new Vector2(0,0);
		
		texture = ZeroAssetManager.hazardcrusher;
		texregion = new TextureRegion(texture, 0, 0, 128, 256);
		sprite = new Sprite(texregion);
		sprite.setSize(3, 6);
		sprite.setPosition(x, y);
		
		BodyDef bodyDef = new BodyDef();
		
		bodyDef.position.set(worldpos);
		
		bodyDef.type = BodyType.KinematicBody;
		
		body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape box = new PolygonShape();
		box.setAsBox(1.5f,3);
		
		fixtureDef.shape = box;
		fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);
	}
	
	public boolean goForward = true;
	
	public void update(){
		
		Movement.x = 0;
		Movement.y = 0;
		
		worldpos = body.getPosition();
		
		sprite.setPosition(worldpos.x-1.5f, worldpos.y-3f);
		
		
		if (goForward){
			if (worldpos.x <  moveto.x){
				Movement.x = speed;
			} 
			if (worldpos.y <  moveto.y){
				Movement.y = speed;
			}
			if ((worldpos.y >=  moveto.y) && (worldpos.x >=  moveto.x)){
				goForward = false;
			}
			
		}else{
			if (worldpos.x >  startpos.x){
				Movement.x = -speed*5;
			} 
			if (worldpos.y >  startpos.y){
				Movement.y = -speed*5;
			}
			if ((worldpos.y <=  startpos.y) && (worldpos.x <=  startpos.x)){
				goForward = true;
			}
		}
		body.setLinearVelocity(Movement);
		
		
	}
	
	
	
	
	
}
