package com.dazpetty.zeroz.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.dazpetty.zeroz.assets.ZeroAssetManager;
import com.dazpetty.zeroz.managers.InputHandler;

public class HUD {

	public Texture healthtex;
	public static Sprite healthsprite;
	
	public Texture dirbuttonstex;
	public Sprite dirbuttonssprite;
	
	public Texture downbuttonstex;
	public Sprite downbuttonsprite;
	
	public Texture leftbuttonstex;
	public Sprite leftbuttonsprite;
	
	public Texture rightbuttonstex;
	public Sprite rightbuttonsprite;
	
	public Texture jumpbuttontex;
	public Sprite jumpbuttonsprite;
	public Texture shootbuttontex;
	public Sprite shootbuttonsprite;
	
	public OrthographicCamera camera;
	public InputHandler inputHandler;
	
	public HUD(OrthographicCamera camera, InputHandler inputHandler){
		this.camera = camera;
		this.inputHandler = inputHandler;
		TextureRegion healthtexreg = new TextureRegion(ZeroAssetManager.healthtex, 0, 0, 64,
				 64);
		 
		healthsprite = new Sprite(healthtexreg);
		healthsprite.setSize(2f, 2f);
		healthsprite.setOrigin(0, healthsprite.getHeight());
		healthsprite.setPosition(30, 0);

		 
		 TextureRegion dirbuttontexreg = new TextureRegion(ZeroAssetManager.dirbuttonstex, 0, 0, 256,
				 64);
		 
		dirbuttonssprite = new Sprite(dirbuttontexreg);
		dirbuttonssprite.setSize(camera.viewportWidth/2, 2f);
		dirbuttonssprite.setOrigin(0, 0);
		dirbuttonssprite.setPosition(0, 0);

		 TextureRegion leftbuttontexreg = new TextureRegion(ZeroAssetManager.leftbuttontex, 0, 0, 64,
				 64);
		
		leftbuttonsprite = new Sprite(leftbuttontexreg);
		leftbuttonsprite.setSize(2f,2f);
		leftbuttonsprite.setOrigin(leftbuttonsprite.getWidth()/2, 0);
		
		 TextureRegion rightbuttontexreg = new TextureRegion(ZeroAssetManager.rightbuttontex, 0, 0, 64,
				 64);
		
		rightbuttonsprite = new Sprite(rightbuttontexreg);
		rightbuttonsprite.setSize(2f,2f);
		rightbuttonsprite.setOrigin(rightbuttonsprite.getWidth()/2, 0);
		
		 TextureRegion downbuttontexreg = new TextureRegion(ZeroAssetManager.downbuttontex, 0, 0, 64,
				 64);
		
		downbuttonsprite = new Sprite(downbuttontexreg);
		downbuttonsprite.setSize(2f,2f);
		downbuttonsprite.setOrigin(downbuttonsprite.getWidth()/2, 0);
		
		
		 TextureRegion jumpbuttontexreg = new TextureRegion(ZeroAssetManager.jumpbuttontex, 0, 0, 64,
				 64);
		
		jumpbuttonsprite = new Sprite(jumpbuttontexreg);
		jumpbuttonsprite.setSize(2f,2f);
		jumpbuttonsprite.setOrigin(jumpbuttonsprite.getWidth()/2, 0);
		
		
		 TextureRegion shootbuttontexreg = new TextureRegion(ZeroAssetManager.shootbuttontex, 0, 0, 64,
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
		tmpVec3.x = (inputHandler.getXInputPosition("jump"));
		tmpVec3.y = camera.position.y - camera.viewportHeight/2.5f;
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2.5f;
		jumpbuttonsprite.setCenter(tmpVec3.x, tmpVec3.y);

		tmpVec3.x = (inputHandler.getXInputPosition("shoot"));
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2.5f;
		shootbuttonsprite.setCenter(tmpVec3.x, tmpVec3.y);
		
		tmpVec3.x = (inputHandler.getXInputPosition("left"));
		//tmpVec3.x = 10f;
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2.5f;
		leftbuttonsprite.setOrigin(0, leftbuttonsprite.getWidth());
		leftbuttonsprite.setCenter(tmpVec3.x, tmpVec3.y);
		
		tmpVec3.x = (inputHandler.getXInputPosition("right"));
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2.5f;
		rightbuttonsprite.setCenter(tmpVec3.x, tmpVec3.y);
		
		tmpVec3.x = (inputHandler.getXInputPosition("down"));
		camera.unproject(tmpVec3);
		tmpVec3.y = camera.position.y - camera.viewportHeight/2.5f;
		downbuttonsprite.setCenter(tmpVec3.x, tmpVec3.y);
		
		dirbuttonssprite.setPosition(camera.position.x - camera.viewportWidth/2,
				tmpVec3.y);
		
	}
}
