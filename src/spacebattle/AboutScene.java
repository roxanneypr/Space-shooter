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
import javafx.stage.Stage;

public class AboutScene {
	private StackPane pane;
	private Scene scene;
	private GraphicsContext gc;
	private Canvas canvas;
	private Stage stage;
	private Scene splashScene;
	
	AboutScene(Stage stage, Scene splashScene){
		this.pane = new StackPane();
		this.scene = new Scene(pane, Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT);
		this.canvas = new Canvas(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();
		this.stage = stage;
		this.splashScene = splashScene;
		this.initAbout();
	}
	
	//Method for initializing about scene
	private void initAbout(){
		Image bg = new Image("images/about.png", 800, 500, false, false);
        this.gc.drawImage(bg, 0, 0);
		
		Button b1 = new Button("Back");
		
		this.addEventHandler(b1);
		
	
		pane.getChildren().add(this.canvas);
		pane.getChildren().add(b1);
		b1.setTranslateY(185);
		
	}
	
	private void addEventHandler(Button btn) {
		btn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				stage.setScene(splashScene);		//sets the scene back to the splash scene
			}
		});
	}	
	
	//scenes getter
	Scene getScene(){
		return this.scene;
	}
}



