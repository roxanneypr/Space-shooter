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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Game {
	private Stage stage;
	private Scene splashScene;	// the splash scene
	private Scene gameScene;		// the game scene
	private InstructionScene instructionScene;		//instruction scene
	private AboutScene aboutScene;		//about scene
	private Group root;
	private Canvas canvas;
	
	public final static int WINDOW_WIDTH = 800;
	public final static int WINDOW_HEIGHT = 500;
	
	public Game(){
		this.root = new Group();
		this.gameScene = new Scene( root );
		this.canvas = new Canvas( Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT );
        this.root.getChildren().add( this.canvas );
	}
	
	//Sets the stage
	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setTitle( "Space Shooter" );
        
		this.initSplash(stage);
		
		stage.setScene( this.splashScene );
        stage.setResizable(false);
		stage.show();
	}
	
	//Method for initializing the splash scene
	private void initSplash(Stage stage) {
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createCanvas(),this.createVBox());
        this.splashScene = new Scene(root);
	}
	
	private Canvas createCanvas() {
    	Canvas canvas = new Canvas(Game.WINDOW_WIDTH,Game.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        Image bg = new Image("images/welcome.png");
        gc.drawImage(bg, 0, 0);
        return canvas;
    }
    
    private VBox createVBox() {
    	VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(200,00,00,00));
        vbox.setSpacing(8);

        Button b1 = new Button("Start");
        Button b2 = new Button("Instruction");
        Button b3 = new Button("About");
        
        vbox.getChildren().add(b1);
        vbox.getChildren().add(b2);
        vbox.getChildren().add(b3);
        b1.setPrefSize(110, 25);
        b2.setPrefSize(110, 25);
        b3.setPrefSize(110, 25);
        
        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                setGame(stage);		// changes the scene into the game scene
            }
        });
        
        b2.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                setInstruction(stage);		// changes the scene into the instruction scene
            }
        });
        
        b3.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                setAbout(stage);		// changes the scene into the about scene
            }
        });
        
        return vbox;
    }
	
    //Method for setting game scene
	private void setGame(Stage stage) {
        stage.setScene( this.gameScene );	
        
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        
        GameTimer gameTimer = new GameTimer(gameScene, gc, this.stage);
        gameTimer.start();
        
	}	
	
	//Method for setting instruction scene
	private void setInstruction(Stage stage) {
		this.instructionScene = new InstructionScene(stage, splashScene);
		stage.setScene(this.instructionScene.getScene());
	}
	
	//Method for setting about scene
	private void setAbout(Stage stage) {
		this.aboutScene = new AboutScene(stage, splashScene);
		stage.setScene(this.aboutScene.getScene());
	}

}
