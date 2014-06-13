package com.dazpetty.zeroz.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dazpetty.zeroz.core.GameScreen;
import com.dazpetty.zeroz.entities.PawnEntity;

public class InputHandler {
/*
 * viewwidth
 * viewheight
 * camera
 * playerTarget
 * aimlessVec
 * playerShoot
 * zplayer
 * attemptShoot();
 * giveWorldPos = false;
 * playerShoot = true;
 */
	public float ang = 0;
	float viewwidth;
	float viewheight;
	Camera camera;
	PawnEntity zplayer;
	Vector2 aimlessVec = new Vector2(0, 0);
	public GameScreen game;
	
	public boolean playerShoot = false;
	public boolean giveWorldPos = false;
	
	private boolean InputHandlerLoaded = false;
	
	/*
	 *  TOUCH VARIABLES
	 */
	float tleft = 0.1f;
	float tright = 0.3f;
	float tdown = 0.5f;
	float tjump = 0.7f;
	float tshoot = 0.9f;
	
	//float crouchbegin = 0.5f;
	//float crouchend = 0.6f;
	

	float touchAreaHeight = 128;
	
	int goDirection = 0;
	
	public void LoadInputHandler(float viewwidthin, float viewheightin, Camera camera, PawnEntity zplayer){
		viewwidth = viewwidthin;
		viewheight = viewheightin;
		this.camera = camera;
		this.zplayer = zplayer;
		InputHandlerLoaded = true;
	}
	
	float buttonarea = 0.075f;
	public boolean checkTouch(float section, float value){
		if (Math.abs(section - value) < buttonarea){
			return true;
		}
		return false;
			
	}
	
	
	public void checkKeyboard() {
		zplayer.isShooting = false;
		zplayer.isCrouching = false;
		if (Gdx.input.isKeyPressed(Keys.U)) {
			zplayer.attemptShoot(ang);
		}
		if (Gdx.input.isKeyPressed(Keys.C)) {
			if (zplayer.isOnLadder)
				zplayer.velocity.y = 0;
			playerShoot = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			zplayer.pressRight = true;
		
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			zplayer.pressLeft = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)
				&& !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.pressUp = true;
		}
		float an = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			zplayer.pressDown = true;
		}
	}
	
	
	
	public void checkTouch() {
		for (int p = 0; p < 5; p++) {
			boolean wantJump = false;
			boolean wantCrouch = false;
			if (Gdx.input.isTouched(p)) {
				Vector3 touchPos = new Vector3(Gdx.input.getX(p),
						Gdx.input.getY(p), 0);
				float section = touchPos.x / viewwidth;
				boolean inTarget = false;
				if (touchPos.y < viewheight - touchAreaHeight) {
					inTarget = true;
				}
				if (inTarget) {
					camera.unproject(touchPos);
					int i = (int) touchPos.x;
					int j = (int) touchPos.y;
				}
				if (!inTarget) {

				//	if (!(section > 0.2 && section < 0.3)) {
						/*if (section < tleft) {
							zplayer.pressLeft = true;
						} 
						if (section < tright) {
							zplayer.pressRight = true;*/
					//	}
				//	}
					if (checkTouch(section, tleft)){
						zplayer.pressLeft = true;
					}else if (checkTouch(section, tright)){
						zplayer.pressRight = true;
					}
					if (checkTouch(section, tdown)){
						zplayer.pressDown = true;
					}
					if (checkTouch(section, tjump)){
						zplayer.pressUp = true;
					}
					if (checkTouch(section, tshoot)){
						zplayer.pressShoot = true;
						zplayer.isShooting = false;
					}
					
						
				/*	if (section >= crouchbegin && section <= crouchend) {
						
					}*/
				/*	if (section >= tjump && section <= tshoot) {
						zplayer.pressUp = true;
					}
					if (section > tshoot && section < 1) {
					//	zplayer.actorTarget = aimlessVec;
						zplayer.pressShoot = true;
						zplayer.isShooting = false;
					}*/
				}
				if (inTarget) {
					Vector2 newAimVec = new Vector2();

					newAimVec.x = touchPos.x - zplayer.worldpos.x;
					newAimVec.y = touchPos.y - zplayer.worldpos.y;

					zplayer.aimingAt.x = newAimVec.x;
					zplayer.aimingAt.y = newAimVec.y;
					zplayer.isShooting = true;
		
					newAimVec.y -= 1;
					float ang = newAimVec.angle();

					zplayer.attemptShoot(ang);

					giveWorldPos = false;
					playerShoot = true;
				}
			}
		/*	if (!wantCrouch && wantJump) {
				zplayer.goJump();
				zplayer.goThruPlatform = true;
			}
			if (wantCrouch && wantJump) {
				zplayer.goThruPlatform = true;
				zplayer.goJumpDown();
			}
			if (wantCrouch && !wantJump) {
				zplayer.isCrouching = true;
			}*/
		}
	}
	
	public float getYInputPosition(){
		return viewheight - touchAreaHeight;
	}
	
	
	public float getXInputPosition(String str){
		float x = 0;
		if(str == "left"){
			x =  tleft * viewwidth;
		}else if(str == "down"){
			x = tdown * viewwidth;
		}else if(str == "right"){
			x = tright * viewwidth;
		}else if(str == "jump"){
			x = tjump * viewwidth;
		}else if(str == "shoot"){
			x = tshoot * viewwidth;
		}
		return x;
	}
	
	
}
