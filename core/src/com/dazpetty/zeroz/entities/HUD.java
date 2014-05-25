package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.dazpetty.zeroz.managers.InputHandler;
import com.dazpetty.zeroz.managers.MyAssetManager;

public class HUD {

	public Texture healthtex;
	public static Sprite healthsprite;
	
	public Texture dirbuttonstex;
	public Sprite dirbuttonssprite;
	
	public Texture jumpbuttontex;
	public Sprite jumpbuttonsprite;
	public Texture shootbuttontex;
	public Sprite shootbuttonsprite;
	
	public OrthographicCamera camera;
	public InputHandler inputHandler;
	
	public HUD(OrthographicCamera camera, InputHandler inputHandler){
		this.camera = camera;
		this.inputHandler = inputHandler;
		TextureRegion healthtexreg = new TextureRegion(MyAssetManager.healthtex, 0, 0, 64,
				 64);
		 
		healthsprite = new Sprite(healthtexreg);
		healthsprite.setSize(2f, 2f);
		healthsprite.setOrigin(0, healthsprite.getHeight());
		healthsprite.setPosition(30, 0);

		 
		 TextureRegion dirbuttontexreg = new TextureRegion(MyAssetManager.dirbuttonstex, 0, 0, 256,
				 64);
		 
		dirbuttonssprite = new Sprite(dirbuttontexreg);
		dirbuttonssprite.setSize(camera.viewportWidth/2, 2f);
		dirbuttonssprite.setOrigin(0, 0);
		dirbuttonssprite.setPosition(0, 0);

		
		 TextureRegion jumpbuttontexreg = new TextureRegion(MyAssetManager.jumpbuttontex, 0, 0, 64,
				 64);
		
		jumpbuttonsprite = new Sprite(jumpbuttontexreg);
		jumpbuttonsprite.setSize(2f,2f);
		jumpbuttonsprite.setOrigin(0, 0);
		
		 TextureRegion shootbuttontexreg = new TextureRegion(MyAssetManager.shootbuttontex, 0, 0, 64,
				 64);

		shootbuttonsprite = new Sprite(shootbuttontexreg);
		shootbuttonsprite.setSize(2f,2f);
		shootbuttonsprite.setOrigin(0, 0);
		shootbuttonsprite.setPosition(0f, 0f);
	}

	public void update(OrthographicCamera camera) {
		healthsprite.setPosition(camera.position.x - camera.viewportWidth/2.5f,
				camera.position.y + camera.viewportHeight/4);
		
		
	
		
		
		Vector3 tmpVec3 = new Vector3((inputHandler.getXInputPosition("jump")),
				0, 0);
		
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2;
		jumpbuttonsprite.setPosition(tmpVec3.x, tmpVec3.y);

		tmpVec3.x = (inputHandler.getXInputPosition("shoot"));
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2;
		shootbuttonsprite.setPosition(tmpVec3.x, tmpVec3.y);
		
		dirbuttonssprite.setPosition(camera.position.x - camera.viewportWidth/2,
				tmpVec3.y);
		
	}
}
