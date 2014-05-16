package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class CopterTurret {

	
	
	
	public boolean isAlive = true;
	public Vector2 worldpos = new Vector2(0, 0);
	
	public Texture baseTexture;
	public TextureRegion baseTextureReg;
	public Sprite baseSprite;
	
	public Texture barrelTexture;
	public TextureRegion barrelTextureReg;
	public Sprite barrelSprite;
	
	public BodyDef bodyDef = new BodyDef();
	public Body body;
	public FixtureDef fixtureDef;
	public CircleShape dynamicCircle;
	public float posMult = 0;
	
	
	
	
	public CopterTurret(float x, float y, World world, float positionMultiplyer, int id){
		posMult = positionMultiplyer;
		worldpos.x = x;
		worldpos.y = y;
		
		baseTexture = new Texture(("data/gfx/battlecopter/turretbase.png"));
		TextureRegion bossTextureReg = new TextureRegion(baseTexture, 0, 0, 128,
				128);
		
		baseSprite = new Sprite(bossTextureReg);
		baseSprite.setSize(1.5f, 1.5f);
		baseSprite.setOrigin(baseSprite.getWidth()/2, baseSprite.getHeight()/2);
		
		barrelTexture = new Texture(("data/gfx/battlecopter/turretbarrel.png"));
		TextureRegion barrelTextureReg = new TextureRegion(barrelTexture, 0, 0, 128,
				256);
		
		barrelSprite = new Sprite(barrelTextureReg);
		barrelSprite.setSize(2, 4);
		barrelSprite.setOrigin(barrelSprite.getWidth()/2,barrelSprite.getHeight()/2);
		
		bodyDef.position.set(worldpos.x+0.5f, worldpos.y+1f);
		bodyDef.gravityScale = 0;
		
		body = world.createBody(bodyDef); 
		body.setUserData(id);
		
		fixtureDef = new FixtureDef(); 
		fixtureDef.isSensor = true;
		
		dynamicCircle = new CircleShape(); 
		dynamicCircle.setRadius(0.7f);  
	    fixtureDef.shape = dynamicCircle;  
    	fixtureDef.filter.categoryBits = 2;
	    
	    Fixture fixture = body.createFixture(fixtureDef);
	    fixture.setUserData("copterturret");
		
	}
	
	boolean init = false;
	float distanceVar = 0;
	
	public void update(CopterBoss copterBoss){
		
		float y = copterBoss.worldpos.y;
		float x = copterBoss.worldpos.x;

		if (!init){
			distanceVar =    copterBoss.bossSprite.getX() - baseSprite.getOriginX();
			init = true;
		}
		
		float barrelY = (float) (distanceVar  / (Math.sin(copterBoss.rotangle)));
	//	System.out.println("dVar" + distanceVar + " y:" + y + "barrelY:" + barrelY + "copRot" + (copterBoss.rotangle));
		
		float xadj = posMult;
		float relativeY = 1.2f * xadj * (0.1f * copterBoss.rotangle/8);
		
		//relativeY = 0;
		//baseSprite.setPosition(x+3, (float) (y-(copterBoss.rotangle/8)));
		float xpos = x+(copterBoss.bossSprite.getWidth()/2)+xadj;
		float ypos =  (float) (y+(relativeY)+1.35);
		baseSprite.setPosition(xpos, ypos);
		//barrelSprite.setPosition(baseSprite.getX()-baseSprite.getWidth()/2, baseSprite.getY()-baseSprite.getHeight()/2);
		barrelSprite.setPosition((float) (baseSprite.getX()-0.3), baseSprite.getY()-1.3f);
		baseSprite.setRotation(copterBoss.bossSprite.getRotation());
		float angle = 0;
		angle += 0.2;
		barrelSprite.rotate(angle);
		//body.setTransform(x+(copterBoss.bossSprite.getWidth()/2)+xadj, (float) (y+(relativeY)+1.35),0);
		body.setTransform(xpos+0.7f, ypos+0.7f,0);
	}
}
