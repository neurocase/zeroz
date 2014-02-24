package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Zphysics {

	/*public void doPhysics(Zactor zact){
		
		float oldposx = zact.position.x, oldposy = zact.position.y;
		Vector2 newposition = new Vector2( zact.position.x,zact.position.y);
		if (zact.velocity.x > 0f)zact.velocity.x -= zact.deceleration;
		if (zact.velocity.x < 0f)zact.velocity.x += zact.deceleration;
		if (Math.abs(zact.velocity.x) < 0.2) zact.velocity.x = 0;
		
		newposition.x += (zact.velocity.x * Gdx.graphics.getDeltaTime());
		
		int i = 0;
		while (i < zact.height){
			
			if (!zact.isFlying){
				if (!zact.isGrounded){
					zact.velocity.y -= zact.gravMass;
				}
				newposition.y += (zact.velocity.y * Gdx.graphics.getDeltaTime());
			}
			if (!zact.isCellBlocked(newposition.x, newposition.y+0.1f+i)||!zact.isCellBlocked(newposition.x+1, newposition.y+0.1f+i)){
				zact.position.x = newposition.x;
				zact.position.y = newposition.y;
			}else{
				System.out.print("blocked new:");
				System.out.print( newposition.x);
				System.out.print(",");
				System.out.print( newposition.y);
				System.out.print("blocked old:");
				System.out.print(zact.position.x);
				System.out.print(",");
				System.out.print(zact.position.y);
				System.out.println();
			}
			i++;
		}
		if (zact.isCellBlocked(zact.position.x, zact.position.y)){
			zact.isGrounded = true;
			zact.velocity.y = 0;
		}else{
			zact.isGrounded = false;
		}
	}*/

	public void doPhysics(Zplayer zact) {
		
		float oldposx = zact.position.x, oldposy = zact.position.y;
		Vector2 newposition = new Vector2( zact.position.x,zact.position.y);
		
		if (zact.velocity.x > 0f)zact.velocity.x -= zact.deceleration;
		if (zact.velocity.x < 0f)zact.velocity.x += zact.deceleration;
		if (Math.abs(zact.velocity.x) < 0.2) zact.velocity.x = 0;
		
		newposition.x += (zact.velocity.x * Gdx.graphics.getDeltaTime());
		
			if (!zact.isFlying){
				if (!zact.isGrounded){
					zact.velocity.y -= zact.gravMass;
				}
				newposition.y += (zact.velocity.y * Gdx.graphics.getDeltaTime());
			}
			boolean blocked = false;
			
			for (int i = 0; i < (int)zact.height; i++){
			if (zact.isCellBlocked(newposition.x, zact.position.y+0.1f+i)){
				
				zact.velocity.x = 0;
				blocked = true;
				}
			}
			if (!blocked){
				zact.position.x = newposition.x;	
			}
			
			if (!Gdx.input.isKeyPressed(Input.Keys.UP) && zact.isCellPlatform(zact.position.x, newposition.y) && zact.velocity.y <= 0){
				zact.velocity.y = 0;  
				zact.isGrounded = true;
			}
			
			if (!zact.isCellBlocked(zact.position.x, newposition.y) && !zact.isGrounded){//||!zact.isCellBlocked(newposition.x+1, newposition.y+0.1f+i)){
				zact.position.y = newposition.y;
			}else{
				zact.isGrounded = true;
				if(!zact.isCellBlocked(zact.position.x, (int)(zact.position.y)))
				zact.position.y =  (int)(zact.position.y);
				blocked = true;
				zact.velocity.y = 0;
			}
			if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !zact.isCellBlocked(zact.position.x, zact.position.y-0.1f) && !zact.isCellPlatform(zact.position.x, zact.position.y-0.1f)){
				zact.isGrounded = false;
			}
			
			if (blocked){
			/*	System.out.print("blocked new:");
				System.out.print( newposition.x);
				System.out.print(",");
				System.out.print( newposition.y);
				System.out.print("blocked old:");
				System.out.print(zact.position.x);
				System.out.print(",");
				System.out.print(zact.position.y);
				if (zact.isGrounded){
					System.out.print(" isGrounded ");
					System.out.print(" Height:");
					System.out.print(zact.height);*/
				}
				System.out.println();
//		}
	}
}
