package com.dazpetty.zeroz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class GamePhysics {
	
	public void doPhysics(Actor zact) {

		float oldposx = zact.worldpos.x, oldposy = zact.worldpos.y;
		Vector2 newposition = new Vector2(zact.worldpos.x, zact.worldpos.y);

		if (zact.velocity.x > 0f && zact.isGrounded)
			zact.velocity.x -= zact.deceleration;
		if (zact.velocity.x < 0f && zact.isGrounded)
			zact.velocity.x += zact.deceleration;
		if (Math.abs(zact.velocity.x) < 0.5)
			zact.velocity.x = 0;

		newposition.x += (zact.velocity.x * Gdx.graphics.getDeltaTime());

		if (!zact.isFlying) {

			if (!zact.isGrounded) {
				boolean fall = true;
				if (zact.state == "ladderaim") {
					fall = false;
					zact.velocity.y = 0;
				}
				if (fall) {
					if (zact.state == "ladderslide") {
						zact.velocity.y -= (zact.gravMass / 2);
					} else {
						zact.velocity.y -= zact.gravMass;
					}
				}
			}
			newposition.y += (zact.velocity.y * Gdx.graphics.getDeltaTime());
		}
		boolean blocked = false;

		for (int i = 0; i < (int) zact.height; i++) {
			if (zact.isCellBlocked(newposition.x, zact.worldpos.y + 0.1f + i)) {

				zact.velocity.x = 0;
				blocked = true;
			}
		}
		if (!blocked && zact.state != "ladderaim") {
			zact.worldpos.x = newposition.x;
		}

		if (!zact.goThruPlatform
				&& zact.isCellPlatform(zact.worldpos.x, newposition.y)
				&& zact.velocity.y <= 0) {
			zact.velocity.y = 0;
			zact.isGrounded = true;
		}
		if (zact.isCellBlocked(zact.worldpos.x, newposition.y + 2)
				&& !zact.isGrounded && zact.velocity.y > 0) {
			zact.velocity.y = 0;
		}

		if (!zact.isCellBlocked(zact.worldpos.x, newposition.y)
				&& !zact.isGrounded) {// ||!zact.isCellBlocked(newposition.x+1,
										// newposition.y+0.1f+i)){
			zact.worldpos.y = newposition.y;
		} else {
			// zact.isGrounded = true;
			if (!zact.isCellBlocked(zact.worldpos.x, (int) (zact.worldpos.y))) {
				zact.worldpos.y = (int) (zact.worldpos.y);
				blocked = true;
				zact.velocity.y = 0;
				if (!zact.isCellBlocked(zact.worldpos.x,
						(int) (zact.worldpos.y + 1f))) {// ||
														// !zact.isCellBlocked(zact.position.x,
														// (int)(zact.position.y+2))){
					zact.isGrounded = true;
				} else {
					System.out.println("Donk!");
				}
			}
		}
		if (!Gdx.input.isKeyPressed(Input.Keys.UP)
				&& !zact.isCellBlocked(zact.worldpos.x, zact.worldpos.y - 0.1f)
				&& !zact.isCellPlatform(zact.worldpos.x, zact.worldpos.y - 0.1f)) {
			zact.isGrounded = false;
		}

	}
}
