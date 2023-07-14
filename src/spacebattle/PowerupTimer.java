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

class PowerupTimer extends Thread {
	private Spaceship spaceship;
	private Powerups powerup;
	private double time;
	private GameTimer timer;
	private int timerType;		//type of the timer
	
	private final static double UPGRADED_TIME = 5.00;
	private final static double SPEED_TIME = 8.00;
	public static final int UNCOLLECTED_TIMER = 0;
	public static final int IMMORTALITY_TIMER = 1;
	public static final int GAME_COUNTDOWN = 2;
	public static final int SPEED_TIMER = 3;
	
	//Constructor for the immortality and added speed timer
	PowerupTimer(Spaceship spaceship, int type){
		this.spaceship = spaceship;
		if(type == PowerupTimer.SPEED_TIMER) {
			this.time = PowerupTimer.SPEED_TIME;
		} else {
			this.time = PowerupTimer.UPGRADED_TIME;
		}
		this.timerType = type;
	}
	
	//Constructor for the power-up uncollected timer
	PowerupTimer(Powerups powerup, int type){
		this.powerup = powerup;
		this.time = PowerupTimer.UPGRADED_TIME;
		this.timerType = type;
	}
	
	//Constructor for the game count down timer
	PowerupTimer(double time, int type, GameTimer timer){
		this.time = time;
		this.timerType = type;
		this.timer = timer;
	}
	
	void refresh(){
		if(this.timerType == PowerupTimer.GAME_COUNTDOWN) {
			this.time = GameTimer.WINNING_TIME;
		} else if(this.timerType == PowerupTimer.SPEED_TIMER){
			this.time = PowerupTimer.SPEED_TIME;
		}else {
			this.time = PowerupTimer.UPGRADED_TIME;
		}
	}

	//count down
	private void countDown(){
		while(this.time!=0){
			try{
				Thread.sleep(1000);
				this.time--;
				if(this.timerType == PowerupTimer.GAME_COUNTDOWN) {
					this.timer.setTime(this.time);
				}
			}catch(InterruptedException e){
				System.out.println(e.getMessage());
			}
		}
		
		if (this.timerType == PowerupTimer.IMMORTALITY_TIMER) {
			this.spaceship.removeImmortality();			//removes the immortality of spaceship
		} else if(this.timerType == PowerupTimer.UNCOLLECTED_TIMER) {
			this.powerup.setUncollected();				//removes the uncollected power-up
			this.powerup.checkUncollected();
		} else if(this.timerType == PowerupTimer.SPEED_TIMER){		
			this.spaceship.removeAddedSpeed();			//removes the added speed
		}else {									
			this.timer.setWin();		//the spaceship wins once the win timer reaches 0
		}
	}

	@Override
	public void run(){
		this.countDown();
	}
}

