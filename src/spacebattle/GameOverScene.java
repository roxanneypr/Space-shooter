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

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameOverScene {
	private StackPane pane;
	private Scene scene;
	private GraphicsContext gc;
	private Canvas canvas;
	private int score;
	
	GameOverScene(int num, int score){
		this.pane = new StackPane();
		this.scene = new Scene(pane, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		this.canvas = new Canvas(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		this.score = score;
		this.setGameOver(num);
		
	}
	
	//Sets the gameover scene
	private void setGameOver(int num){
		Image bg = new Image("images/bg3.png", 800, 500, false, false);
        this.gc.drawImage(bg, 0, 0);
		Font theFont = Font.font("Verdana",FontWeight.BOLD,30);
		this.gc.setFont(theFont);											
		
		if (num==1) {		//sets a congratulations message if win
			this.gc.setFill(Color.YELLOW);										
			this.gc.fillText("Congratulations! You Win!", Game.WINDOW_WIDTH*0.22, Game.WINDOW_HEIGHT*0.3);
		} else {			//sets a game over message if win
			this.gc.setFill(Color.RED);										
			this.gc.fillText("Game Over! You Lose!", Game.WINDOW_WIDTH*0.25, Game.WINDOW_HEIGHT*0.3);
		}
		
		//sets the score
		this.gc.setFont(theFont);
		this.gc.setFill(Color.WHITE);
		this.gc.setStroke(Color.BLACK);
		this.gc.strokeText("SCORE: " + this.score, 320, 200);
		this.gc.fillText("SCORE: " + this.score, 320, 200);
		
		Button exitbtn = new Button("Exit Game");
		this.addEventHandler(exitbtn);
		
		
		pane.getChildren().add(this.canvas);
		pane.getChildren().add(exitbtn);
	}
	
	private void addEventHandler(Button btn) {
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				System.exit(0);						//exits the application
			}
		});
		
	}
	
	//Scene getter
	Scene getScene(){
		return this.scene;
	}
}