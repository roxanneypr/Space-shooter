/*************************************************************************************************************************
 *
 * CMSC 22 Object-Oriented Programming
 * Space Shooter (Mini Project)
 * Problem Domain: Space shooter is a 2d shooting game where the goal is for the spaceship to stay alive for 1 minute.  
 * When a spaceship is created, it is given an initial strength level whose value is a random number between 100-150. 
 * The spaceship can move 10 pixels at a time. The ship can also shoot bullets when the space bar is pressed. The bullets 
 * shot by the ship move towards the right side of the screen in a straight line and disappear when the end of the screen 
 * is reached. The bullet’s damage is equal to the current ship strength. UFOs appear from the right side of the screen in
 * random positions and dies once hit by a bullet. When UFOs are spawned, its damage is randomized from 30 to 40 and its
 * speed is randomized from 1-5. At the start, there are 7 UFOs but 3 more are spawned every 5 seconds. When a UFO hits 
 * the spaceship, the spaceship’s strength is reduced by the UFO’s damage, and the UFO dies and disappears. When the 
 * spaceship’s strength reaches 0, the game is over and the ship loses.
 * Different power-ups can be collected by the spaceship:
 * Power-ups 		Effect
 * Star 		Makes the spaceship immortal
 * Heart	 	Doubles the strength of the spaceship
 * Coin			Adds 3 to the spaceship's speed
 * 
 * @author Roxanne Ysabel Resuello
 * @date 05-18-2022
 *************************************************************************************************************************/
package spacebattle;

import javafx.scene.image.Image;

class Bullet extends Sprite{
	private int damage;
	
	private final static double BULLET_SPEED = 10;
	private final static Image BULLET_IMAGE = new Image("images/bullet.png");


	Bullet(double x, double y, int strength){
		super(x,y,Bullet.BULLET_IMAGE);
		this.damage = strength;
	}
	
	//Damage getter
    int getDamage(){
    	return this.damage;
    }
    
    //Method for moving the bullets
	void move(){
		this.xPos += Bullet.BULLET_SPEED;
		if(this.xPos >= Game.WINDOW_WIDTH){				// if this bullet passes through the right of the scene, set visible to false
			this.vanish();
		}
	}
}
