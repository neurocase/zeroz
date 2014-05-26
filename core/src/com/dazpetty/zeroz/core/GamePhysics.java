package com.dazpetty.zeroz.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.dazpetty.zeroz.entities.PawnEntity;

/*
 * The game physics class checks the tiles of the map, it provides collision detection for the player and other entities,
 * it does not use box2d and was implimented prior to box2d being integrated into the project, it may be replaced by
 * box 2d sometime later.
 *
 * This class modifies the velocity of PawnEntity(s) with the help of the LevelManager
 *
 *
 */

public class GamePhysics {

	public void doPhysics(PawnEntity pawn) {

		if (pawn.levelMan != null) {
			float oldposx = pawn.worldpos.x, oldposy = pawn.worldpos.y;
			Vector2 newposition = new Vector2(pawn.worldpos.x, pawn.worldpos.y);

			if (pawn.velocity.x > 0f && pawn.isGrounded)
				pawn.velocity.x -= pawn.deceleration;
			if (pawn.velocity.x < 0f && pawn.isGrounded)
				pawn.velocity.x += pawn.deceleration;
			if (Math.abs(pawn.velocity.x) < 0.5)
				pawn.velocity.x = 0;

			newposition.x += (pawn.velocity.x * Gdx.graphics.getDeltaTime());

			if (!pawn.isFlying) {

				if (!pawn.isGrounded) {
					boolean fall = true;
					if (pawn.state == "ladderaim") {
						fall = false;
						pawn.velocity.y = 0;
					}
					if (fall) {
						if (pawn.isOnLadder) {
							if (pawn.isCrouching) {
								pawn.velocity.y = -2;
							} else if (pawn.isJump && pawn.pressJump) {
								pawn.velocity.y = 2;
								// System.out.print("ON A LADDER AND JUMPING");
							} else if (!pawn.pressJump) {
								if (pawn.velocity.y > 0) {
									pawn.velocity.y = 0;
								}
							}
						} else if (!pawn.isOnLadder) {
							pawn.velocity.y -= pawn.gravMass;
						}
					}
				}
				newposition.y += (pawn.velocity.y * Gdx.graphics.getDeltaTime());
			}
			boolean blocked = false;

			for (int i = 0; i < (int) pawn.height; i++) {
				if (pawn.levelMan.isCellBlocked(newposition.x, pawn.worldpos.y
						+ 0.1f + i, true)) {

					pawn.velocity.x = 0;
					blocked = true;
				}
			}
			if (!blocked && pawn.state != "ladderaim") {
				pawn.worldpos.x = newposition.x;
			}

			if (!pawn.goThruPlatform
					&& pawn.levelMan.isCellPlatform(pawn.worldpos.x,
							newposition.y) && pawn.velocity.y <= 0
					&& !pawn.isOnLadder) {
				pawn.velocity.y = 0;
				pawn.isGrounded = true;
			}
			if (pawn.levelMan.isCellBlocked(pawn.worldpos.x, newposition.y + 2,
					true) && !pawn.isGrounded && pawn.velocity.y > 0) {
				pawn.velocity.y = 0;
			}

			if (pawn.levelMan.isCellBlocked(pawn.worldpos.x, newposition.y + 2,
					true) && !pawn.isGrounded && pawn.velocity.y > 0) {
				pawn.velocity.y = 0;
			}

			// isGrounded prevents jumping, it also prevents calculating gravity
			// when negative

			if (!pawn.levelMan.isCellBlocked(pawn.worldpos.x, newposition.y,
					true) && !pawn.isGrounded) {
				pawn.worldpos.y = newposition.y;
			} else {
				if (!pawn.levelMan.isCellBlocked(pawn.worldpos.x,
						(int) (pawn.worldpos.y), blocked)) {
					pawn.worldpos.y = (int) (pawn.worldpos.y);
					blocked = true;
					pawn.velocity.y = 0;
					if (!pawn.levelMan.isCellBlocked(pawn.worldpos.x,
							(int) (pawn.worldpos.y + 1f), true)) {
						pawn.isGrounded = true;
					} else {
						System.out.println("Donk!");
					}
				}
			}
			
			
		/*	
			if (!pawn.pawnFoot.isOnGround) {
				pawn.worldpos.y = newposition.y;
			} else {
				if (!pawn.pawnFoot.isOnGround) {
					pawn.worldpos.y = (int) (pawn.worldpos.y);
					blocked = true;
					pawn.velocity.y = 0;
					if (!pawn.levelMan.isCellBlocked(pawn.worldpos.x,
							(int) (pawn.worldpos.y + 1f), true)) {
						pawn.isGrounded = true;
					} else {
						System.out.println("Donk!");
					}
				}
			}*/
			
			
			
			
			
			
			
			if (!Gdx.input.isKeyPressed(Input.Keys.UP)
					&& !pawn.levelMan.isCellBlocked(pawn.worldpos.x,
							pawn.worldpos.y - 0.1f, true)
					&& !pawn.levelMan.isCellPlatform(pawn.worldpos.x,
							pawn.worldpos.y - 0.1f)) {
				pawn.isGrounded = false;
			}

		}

	}
}