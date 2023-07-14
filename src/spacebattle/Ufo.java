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
import java.util.Random;

import javafx.scene.image.Image;

class Ufo extends Sprite {
	private int damage;
	private double speed;
	private boolean moveRight;
	private boolean gainedScore;	//boolean value if the UFO already added a score to the spaceship

	private final static Image UFO_IMAGE = new Image("images/ufo.png");
	private final static int GAIN = 1;

	Ufo(int x, int y){
		super(x, y, UFO_IMAGE);
		Random r = new Random();
		this.damage = r.nextInt(41-30)+30;
		this.speed = r.nextInt(6-1)+1;
		this.moveRight = false;
		gainedScore = false;
	}
	
	//Moves the UFOs
	void move() {
		if(this.moveRight) {
			if(this.xPos+this.dx <= Game.WINDOW_WIDTH-this.width) {
				this.xPos += this.speed;
			} else {
				this.moveRight = false;
			}
		} else if(!this.moveRight) {
			if (this.xPos+this.dx >= 0) {
				this.xPos -= this.speed;
			} else {
				this.moveRight = true;
			}
		}
	}
	
	//Sets the UFO to die and adds 1 to the spaceship's score
	private void getHit(int damage, Spaceship spaceship){
		this.die();
		if(!this.gainedScore) {
			this.gainedScore = true;
			spaceship.gainScore(Ufo.GAIN);
		}
	}
	
	//removes the UFO from the screen
	private void die(){
		this.vanish();
	}

	/*
     * Traverses through all the bullets of spaceship
     * If hit, the UFO dies and the bullet vanishes
     * Checks if this UFO collides with the spaceship itself -> UFO dies
     * */
	void checkCollision(Spaceship spaceship){
		for	(int i = 0; i < spaceship.getBullets().size(); i++)	{
			if (this.collidesWith(spaceship.getBullets().get(i))){
				this.getHit(spaceship.getBullets().get(i).getDamage(), spaceship);
				spaceship.getBullets().get(i).vanish();
			}
		}
		if(this.collidesWith(spaceship)){
			spaceship.getDamage(this.damage);
			this.die();
		}
	}
}