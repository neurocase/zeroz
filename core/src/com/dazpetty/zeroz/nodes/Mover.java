package com.dazpetty.zeroz.nodes;

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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.dazpetty.zeroz.core.DazDebug;

public class Mover {

	
	public Vector2 startpos = new Vector2(0,0);
	public Vector2 worldpos = new Vector2(0,0);
	public Vector2 moveto = new Vector2(0,0);
	public Vector2 velocity = new Vector2(0,0);
	
	
	public Body body;
	public FixtureDef fixtureDef;
	public Fixture doorfix;
	
	String movertype = "";
	int speed = 0;
	
	public Texture texture;
	
	public Sprite sprite;
	
	public Mover(float x, float y, String movertype, int speed, int triggervalue, float movex, float movey, World world){
		
		texture = new Texture(("data/gfx/movers/hoverplatform.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion texturereg = new TextureRegion(texture, 0, 0, 128,
				64);
		
		sprite = new Sprite(texturereg);
		
		moveto.x = movex+x;
		moveto.y = movey+y;
		
		
		startpos.x = x;
		startpos.y = y;
		
		if (movertype.equals("backward")){
			worldpos.x = moveto.x;
			worldpos.y = moveto.y;
		}else{
		worldpos.x = x;
		worldpos.y = y;
		}
		this.movertype = movertype;
		this.speed = speed;
		
		
		BodyDef doorbodyDef = new BodyDef();
		doorbodyDef.gravityScale = 0;
		doorbodyDef.position.set(worldpos.x, worldpos.y);
		doorbodyDef.type = BodyType.KinematicBody;
		body = world.createBody(doorbodyDef); 
		FixtureDef doorfixDef = new FixtureDef();
		doorfixDef.density = 100;
		PolygonShape pBox = new PolygonShape();
		sprite.setSize(4f, 2f);
		pBox.setAsBox(2f, 0.5f);
		doorfixDef.shape = pBox;    
		doorfix = body.createFixture(doorfixDef);
		doorfix.setUserData("solid");	
		body.applyLinearImpulse(1, 1, 1, 1, true);
		
		doorfixDef.friction = 0;
		
		//DazDebug.print("NEW MOVER AT: X:" +  worldpos.x + " Y:"+ worldpos.y);
	//	DazDebug.print("MOVETO X: " + moveto.x + " Y:" + moveto.y);
		//DazDebug.print("STARTPOS X: " + startpos.x + " Y:" + startpos.y);
		
		
	}
	
	boolean goForward = true;
	
	private Vector2 Movement = new Vector2(0,0);
	
	public void update(){
		
		Movement.x = 0;
		Movement.y = 0;
		
	

		
		worldpos = body.getPosition();
		
		sprite.setPosition(worldpos.x-2, worldpos.y-1.5f);
		
		
		if (goForward){
			if (worldpos.x <  moveto.x){
				//body.applyLinearImpulse(1, 0, 1, 1, true);
				//body.setLinearVelocity(1, body.getLinearVelocity().y);
				//body.set
				Movement.x = 1;
			//	DazDebug.print("MOVE MOVER X to end" );
			} 
			if (worldpos.y <  moveto.y){
			//	DazDebug.print("MOVE MOVER Y to end" );
				//body.setLinearVelocity(body.getLinearVelocity().x, 1);
				Movement.y = 1;
			}
			
			if ((worldpos.y >=  moveto.y) && (worldpos.x >=  moveto.x)){
				goForward = false;
			}
			
		}else{
			if (worldpos.x >  startpos.x){
				//body.setLinearVelocity(-1, body.getLinearVelocity().y);
				Movement.x = -1;
			//	DazDebug.print("MOVE MOVER X to start" );
			} 
			if (worldpos.y >  startpos.y){
				Movement.y = -1;
				
		//		DazDebug.print("MOVE MOVER Y to start" );
			}
			if ((worldpos.y <=  startpos.y) && (worldpos.x <=  startpos.x)){
				goForward = true;
			}
		}
		body.setLinearVelocity(Movement);
		
	}
	
	public void triggerOn(int trigval){
		
	}
	
}
