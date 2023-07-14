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

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;

class Spaceship extends Sprite {
	private String name;
	private boolean alive;
	private boolean isImmortal;
	private int score;
	private int strength;
	private int star;		//number of stars collected
	private int heart;		//number of hearts collected
	private int speed;		
	private int coin;		//number of coins collected
	private ArrayList<Bullet> bullets;
	private PowerupTimer timer;

	private final static Image SPACESHIP_IMAGE = new Image("images/spaceship.png");	
	private final static Image IMMORTALITY_IMAGE = new Image("images/immortal2.png");	//Spaceship immortality image
	private final static double INITIAL_X = 10;
	public final static int SPACESHIP_SPEED = 10;
	public final static int ADDED_SPEED = 3;

	Spaceship(String name, double y){
       	super(Spaceship.INITIAL_X, y, Spaceship.SPACESHIP_IMAGE);
       	Random r = new Random();
       	this.star = 0;
    	this.heart = 0;
		this.coin = 0;
		this.name = name;
		this.alive = true;
		this.isImmortal = false;
		this.speed = SPACESHIP_SPEED;
		this.bullets = new ArrayList<Bullet>();
		this.strength = r.nextInt(151-100)+100;
	}
	
	//Changes the image of spaceship when immortal
	void showImmortality() {
		this.loadImage(IMMORTALITY_IMAGE);
	}
	
	//Name getter
	String getName(){
		return this.name;
	}
	
	//Score getter
	int getScore(){
		return this.score;
	}
	
	//Getter of the number of stars collected
	int getStar(){
		return this.star;
	}
	
	//Collected hearts getter
	int getHeart(){
		return this.heart;
	}
	
	//Collected coins gettter
	int getCoin(){
		return this.coin;
	}
	
	//Spaceship's strength getter
	int getStrength(){
		return this.strength;
	}
	
	//Adds 1 to the number of stars collected
	void setStar() {
		this.star +=1;
	}
	
	//Adds 1 to the number of hearts collected
	void setHeart() {
		this.heart +=1;
	}
	
	//Adds 1 to the number of coins collected
	void setCoin() {
		this.coin +=1;
	}
	
	//Bullet getter
	ArrayList<Bullet> getBullets(){
		return this.bullets;
	}
	
	//Die setter
    void die(){
    	this.alive = false;
    }
    
    //Deducts a damage to the spaceship's strength
    void getDamage(int damage) {
    	if(!this.isImmortal) {
    		this.strength -= damage;
    	}
    	if(this.strength<=0) {
    		this.die();
    	}
    }
    
    //Immortality state getter
    boolean isImmortal() {
    	return this.isImmortal;
    }
	
    //Removes the immortality of the spaceship
    void removeImmortality(){
    	this.isImmortal = false;
    	this.loadImage(SPACESHIP_IMAGE);
    }   
    
    //Sets the spaceship to immortal
    void provideImmortality(){
    	if(!this.isImmortal){
	    	this.isImmortal = true;
	    	this.timer = new PowerupTimer(this, PowerupTimer.IMMORTALITY_TIMER);
	    	this.timer.start();
	    }else this.timer.refresh();
    }
	
    //Method for shooting
    public void shoot(){
		this.bullets.add(new Bullet(this.xPos+(this.width-5), this.yPos+(this.height/2.4), this.strength));
    }
    
    //Adds 1 to the score when a UFO was shot
    void gainScore(int increase){
    	this.score+=increase;
    	System.out.println("Score: "+score);
    }
    
    //Alive state getter
    boolean isAlive(){
    	return this.alive;
    } 

    //Method for moving
    void move() {
    	if((this.xPos+this.dx >= 0 && this.xPos+this.dx <= Game.WINDOW_WIDTH-this.width) && 
    			(this.yPos+this.dy >= 0 && this.yPos+this.dy <= Game.WINDOW_HEIGHT-this.height)) {
			this.xPos += this.dx;
    		this.yPos += this.dy;
    	}
	}
    
    //Doubles the strength once a heart is collected
	public void doubleStrength() {
		this.strength += this.strength;
	}
	
	//Removes the added speed of the coin power-up
	void removeAddedSpeed() {
		this.speed -= Spaceship.ADDED_SPEED;
	}
	
	//Adds 3 to the speed once a coin is collected
	void addSpeed() {
		if(this.speed == Spaceship.SPACESHIP_SPEED){
			this.speed += Spaceship.ADDED_SPEED;
	    	this.timer = new PowerupTimer(this, PowerupTimer.SPEED_TIMER);
	    	this.timer.start();
	    }else this.timer.refresh();
		
	}
	
	//Speed getter
	int getSpeed() {
		return this.speed;
	}
	
}