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
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Random;

class GameTimer extends AnimationTimer {
	private Stage stage;
	private long startSpawn;
	private long startSpawnPowerups;
	private GraphicsContext gc;
	private Spaceship spaceship;
	private Scene scene;
	private ArrayList<Ufo> ufos;
	private ArrayList<Powerups> powerups;
	private static boolean goLeft;
	private static boolean goRight;
	private static boolean goUp;
	private static boolean goDown;
	private static boolean shoot;
	private double backgroundx;
	private double time;
	private PowerupTimer timer;
	private PowerupTimer winTimer;
	private int win;	//1 if the player wins, 0 if lose
	
	private Image background = new Image( "images/bg3.png" );
	public final static int NUM_POWERUPS = 1;
	public final static int INITIAL_MONSTERS = 7;
	public final static int NUM_MONSTERS = 3;
	public final static int HEIGHT_PER_MONSTER = 60;
	public final static int MONSTER_INITIAL_XPOS = 790;
	public final static int BACKGROUND_SPEED = 1;
	public final static double POWERUPS_DELAY = 10.00;
	public final static double SPAWN_DELAY = 5.00;
	public final static double WINNING_TIME = 60.00;
    
    GameTimer(Scene scene, GraphicsContext gc, Stage stage) {
    	Random r = new Random();
    	this.gc = gc;
    	this.scene = scene;    	
    	this.stage = stage;
    	this.win = 0;
    	this.spaceship = new Spaceship("Apollo", r.nextInt((Game.WINDOW_HEIGHT-GameTimer.HEIGHT_PER_MONSTER)-(50))+50);
    	this.ufos = new ArrayList<Ufo>();
    	this.powerups = new ArrayList<Powerups>();
    	this.startSpawn = this.startSpawnPowerups = System.nanoTime();
    	this.time = GameTimer.WINNING_TIME;
    	this.prepareActionHandlers();
    	this.spawnUfoStart();
    	this.startGameTimer();
    }
    
    @Override
	public void handle(long currentNanoTime)
    {
		this.redrawBackgroundImage();
        this.autoSpawn(currentNanoTime);
                
        this.renderSprites();
        this.moveSprites();
        
        this.drawScore();
        this.drawStrength();
        this.drawTimer();
        this.drawStar();
        this.drawHeart();
        this.drawCoin();
        
        //changes the image of the spaceship
        if(this.spaceship.isImmortal()) {
        	this.spaceship.showImmortality();
        }
        
        //calls the method to set the game over scene once the spaceship died
        if(!this.spaceship.isAlive()) {
        	this.stop();
        	this.setGameOver(this.win);		// draw Game Over text
        }
    }
    
    //Sets the game to a win and the spaceship dies
    void setWin() {
    	this.win = 1;
    	this.spaceship.die();
    }
    
    private void redrawBackgroundImage() {
		// clear the canvas
        this.gc.clearRect(0, 0, Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT);

        // redraw background image (moving effect)
        this.backgroundx += GameTimer.BACKGROUND_SPEED;

        this.gc.drawImage( background, this.backgroundx-this.background.getWidth(), 0 );
        this.gc.drawImage( background, this.backgroundx, 0 );
        
        if(this.backgroundx>=Game.WINDOW_WIDTH) 
        	this.backgroundx = Game.WINDOW_WIDTH-this.background.getWidth();
    }
    
    //Method for spawning a power-up every 10secs and 3 UFOs every 5secs
    private void autoSpawn(long currentNanoTime) {
    	double spawnElapsedTime = (currentNanoTime - this.startSpawn) / 1000000000.0;
		double powerupsElapsedTime = (currentNanoTime - this.startSpawnPowerups) / 1000000000.0;
		
		// spawn power-ups
        if(powerupsElapsedTime > GameTimer.POWERUPS_DELAY) {
        	this.spawnPowerups();
        	this.startSpawnPowerups = System.nanoTime();
        }
       
        
        // spawn UFO
        if(spawnElapsedTime > GameTimer.SPAWN_DELAY) {
        	this.spawnUfo();
        	this.startSpawn = System.nanoTime();
        }
    }
    
    //Spawns a powerup every 10 secs at the left side of the screen
    private void spawnPowerups() {
    	int type;
		Powerups newCollectible;
		Random r = new Random();

		for(int i=0;i<GameTimer.NUM_POWERUPS;i++){
			int x = r.nextInt(Game.WINDOW_WIDTH/2);
			int y = r.nextInt((Game.WINDOW_HEIGHT-50)-(50))+50;
			type = r.nextInt(3);
			switch(type){
				case 0: newCollectible = new Heart(x,y); break;
				case 1: newCollectible = new Star(x,y); break;
				default: newCollectible = new Coin(x,y); break;
			} 		
			this.powerups.add(newCollectible);
		}
		this.startPowerupTimer();
	}
    
    //Starts the countdown timer of the game
    private void startGameTimer() {
    	if(this.time!=0){
			this.winTimer = new PowerupTimer(this.time, PowerupTimer.GAME_COUNTDOWN, this);
	    	this.winTimer.start();
		} else this.winTimer.refresh();
    
    }
    
    //Time setter
    void setTime(double time) {
    	this.time = time;
    }
    
    //Starts the countdown timer of the power-ups
    //If uncollected for 5secs, the power-up disappears
    private void startPowerupTimer() {
    	for(int i = 0; i < this.powerups.size(); i++){
			Powerups c = this.powerups.get(i);
			if(!c.isUncollected()){
				this.timer = new PowerupTimer(c, 0);
		    	this.timer.start();
			} else this.timer.refresh();
    	}
    }

	private void renderSprites() {
    	// draw Spaceship
        this.spaceship.render(this.gc);
        
        // draw Sprites in ArrayLists
        for (Ufo ufo : this.ufos )
        	ufo.render( this.gc );
        
        for (Bullet b : this.spaceship.getBullets())
        	b.render( this.gc );
        
        for (Powerups c : this.powerups)
        	c.render( this.gc );
    }
    
	//Calls the move method of all the sprites
    private void moveSprites() {
        this.moveSpaceship();
        this.moveUFO();
        this.moveBullets();
        this.checkPowerups();
    }
	
    //Catches the left and right key presses for the spaceship's movement
	private void prepareActionHandlers() {
    	this.scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if(code.equals("LEFT")) {
                	GameTimer.goLeft = true;
                }else if(code.equals("RIGHT")) {
                	GameTimer.goRight = true;
                }else if(code.equals("UP")) {
                	GameTimer.goUp = true;
                }else if(code.equals("DOWN")) {
                	GameTimer.goDown = true;
                }else if(code.equals("SPACE")) {
                	GameTimer.shoot = true;
                }
            }
        });

    	this.scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if(code.equals("LEFT")) {
                	GameTimer.goLeft = false;
                }else if(code.equals("RIGHT")) {
                	GameTimer.goRight = false;
                } else if(code.equals("UP")) {
                	GameTimer.goUp = false;
                } else if(code.equals("DOWN")) {
                	GameTimer.goDown = false;
                }else if(code.equals("SPACE")) {
                	GameTimer.shoot = false;
                }
            }
        });
    }
	
    //Gets called in handle() to move the spaceship
	private void moveSpaceship() {
		if (GameTimer.goLeft) {
            this.spaceship.setDX(-this.spaceship.getSpeed());
            this.spaceship.move();
		}else if (GameTimer.goRight){
        	this.spaceship.setDX(this.spaceship.getSpeed());
			this.spaceship.move();
		}else if (GameTimer.goUp){
        	this.spaceship.setDY(-this.spaceship.getSpeed());
        	this.spaceship.move();
		}else if (GameTimer.goDown){
        	this.spaceship.setDY(this.spaceship.getSpeed());
			this.spaceship.move();
		}else if (GameTimer.shoot)
        	this.spaceship.shoot();
        else {
        	this.spaceship.setDX(0);
			this.spaceship.setDY(0);
			this.spaceship.move();
        }
	}
	
	//Method for drawing the countdown timer in the status bar
	private void drawTimer(){
		int min = (int)this.time/60;
		int secs = (int)this.time%60;
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText("TIME:", 30, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.WHITE);
		if(secs<10) {
			this.gc.fillText(min+ ":0" + secs +"", 100, 30);
		} else {
			this.gc.fillText(min+ ":" + secs +"", 100, 30);
		}
	}
	
	//Method for drawing the spaceship's strength in the status bar
	private void drawStrength(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText("STRENGTH:", 595, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(spaceship.getStrength()+"",730, 30);
	}
	
	//Method for drawing the player's score in the status bar
	private void drawScore(){
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.YELLOW);
		this.gc.fillText("SCORE:", 460, 30);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(spaceship.getScore()+"", 550, 30);
	}
	
	//Method for drawing the star and the number of collected stars in the status bar
	private void drawStar(){
		this.gc.drawImage(Star.STAR_IMAGE, 280, 9);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(":"+spaceship.getStar()+"", 310, 30);
	}
	
	//Method for drawing the heart and the number of collected hearts in the status bar
	private void drawHeart(){
		this.gc.drawImage(Heart.HEART_IMAGE, 350, 10);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(":"+spaceship.getHeart()+"", 385, 30);
	}
	
	//Method for drawing the coin and the number of collected coins in the status bar
	private void drawCoin(){
		this.gc.drawImage(Coin.COIN_IMAGE, 213, 13);
		this.gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText(":"+spaceship.getCoin()+"", 240, 30);
		}
	
    //Checks if the power-ups collide with the spaceship in checkCollision()
	private void checkPowerups(){
		for(int i = 0; i < this.powerups.size(); i++){
			Powerups c = this.powerups.get(i);
			if(c.isVisible()){
				c.checkCollision(this.spaceship);
			}
			else powerups.remove(i);
		}
	}
	
    //Moves the bullets and if they are outside the screen, they are removed from the ArrayList
	private void moveBullets(){
		ArrayList<Bullet> bList = this.spaceship.getBullets();
		for(int i = 0; i < bList.size(); i++){
			Bullet b = bList.get(i);
			if(b.isVisible())
				b.move();
			else bList.remove(i);
		}
	}
	
     //Moves the UFO and checks if they collide with the spaceship's bullets in checkCollision()
     //If they are outside the screen, they get removed from the ArrayList
	private void moveUFO() {
		for(int i = 0; i < this.ufos.size(); i++){
			Ufo m = this.ufos.get(i);
			if(m.isVisible()){
				m.move();
				m.checkCollision(this.spaceship);
			}
			else this.ufos.remove(i);
		}
	}
	
	
	//Spawns 3 UFO at random y position
	private void spawnUfo(){
		
		int yPos, xPos = GameTimer.MONSTER_INITIAL_XPOS;
		Random r = new Random();
		
		for(int i=0;i<GameTimer.NUM_MONSTERS;i++){
			yPos = r.nextInt((Game.WINDOW_HEIGHT-GameTimer.HEIGHT_PER_MONSTER)-(60))+60;
			this.ufos.add(new Ufo(xPos, yPos));
		}
	}
	
	//Spawns 7 UFO at the start of the game
	private void spawnUfoStart(){
		int yPos, xPos = GameTimer.MONSTER_INITIAL_XPOS;
		Random r = new Random();
		
		for(int i=0;i<GameTimer.INITIAL_MONSTERS;i++){
			yPos = r.nextInt((Game.WINDOW_HEIGHT-GameTimer.HEIGHT_PER_MONSTER)-(HEIGHT_PER_MONSTER))+HEIGHT_PER_MONSTER;
			this.ufos.add(new Ufo(xPos, yPos));
		}
	}
	
	//Method for displaying the Game Over scene
	private void setGameOver(int type) {
		GameOverScene gScene = new GameOverScene(type, spaceship.getScore());
		this.stage.setScene(gScene.getScene());
	}
}
