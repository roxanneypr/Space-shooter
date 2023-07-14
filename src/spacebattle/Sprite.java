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
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite {
	protected Image img;
	protected double xPos, yPos, dx, dy;
	protected boolean visible;
	protected double width;
	protected double height;

    public Sprite(double xPos, double yPos, Image image){
		this.xPos = xPos;
		this.yPos = yPos;
		this.loadImage(image);
		this.visible = true;
	}

	private Rectangle2D getBounds(){
		return new Rectangle2D(this.xPos, this.yPos, this.width, this.height);
	}

	private void setSize(){
		this.width = this.img.getWidth();
        this.height = this.img.getHeight();
	}
	
	//checks if the two image intersects with each other
	protected boolean collidesWith(Sprite rect2)	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

		return rectangle1.intersects(rectangle2);
	}
	
	//loads the image
	protected void loadImage(Image image){
		try{
			this.img = image;
	        this.setSize();
		} catch(Exception e)	{
			e.printStackTrace();
		}
	}
	
	public void render(GraphicsContext gc){
        gc.drawImage( this.img, this.xPos, this.yPos );
    }
	
	//image getter
	public Image getImage(){
		return this.img;
	}
	
	//x position getter
	public double getXPos(){
		return this.xPos;
	}

	//y position getter
	public double getYPos(){
		return this.yPos;
	}

	public void setDX(int val){
		this.dx = val;
	}

	public void setDY(int val){
		this.dy = val;
	}

	//visibility getter
	public boolean isVisible(){
		return visible;
	}
	
	//visibility setter
	public void vanish(){
		this.visible = false;
	}
}
