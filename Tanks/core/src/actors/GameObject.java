package actors;

import Screens.GameScreenClient;
import Screens.GameScreenServer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import Screens.GameScreen;
import managers.LevelManager;

import java.util.ArrayList;

abstract class GameObject {

	public static final int IDLE = 1;
	public static final int MOVING = 2;

	public static final int LEFT = 180;
	public static final int RIGHT = 0;
	public static final int DOWN = 270;
	public static final int UP = 90;
	public static final int STOPPED = -1;

	public static final int MOVE_SPEED = 1;

	protected TextureRegion[] frames;
	protected Animation movingAnimation;
	protected TextureRegion currentFrame;

	protected float stateTime;

	protected int currentState = IDLE;
	protected int currentFacing = RIGHT;

	protected Vector2 position;
	protected Vector2 velocity;
	protected Vector2 origin;
	protected Vector2 kof;
	protected float rotation;

	protected ArrayList<Bullet> bullets;
	LevelManager lvlManager;
	GameScreen gameScreen;
	public int screengam=0;

	protected GameObject(Texture spriteSheet, Vector2 position, int spriteSheetRows,
			int spriteSheetCols, int numFrames, int animationStartRow, int screengam){

		//System.out.println("kx = " + GameScreen.kX + " kY = " + GameScreen.kY);
		this.position = position;
		this.screengam = screengam;
		origin = new Vector2(position.x + 16, position.y + 16);
		rotation = 0f;
		bullets = new ArrayList<Bullet>();
		stateTime = 0f;
		spriteSheet = new Texture(Gdx.files.internal("TanksSpriteSheet.png"));
		lvlManager = new LevelManager(spriteSheet,screengam);

		/* Load Texture Regions into Animation */
		TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() /
				spriteSheetRows, spriteSheet.getHeight() / spriteSheetCols);
		frames = new TextureRegion[numFrames];
		for(int i = 0; i < numFrames; i++){
			frames[i] = tmp[animationStartRow][i];
		}
		movingAnimation = new Animation(0.075f, frames);
	}

	public ArrayList<Bullet> getBullets(){
		return bullets;
	}

	/*
	 * setVelocity() -- sets the movement speed and state of the player based on keyboard input(r) from the user.
	 */
	public void setVelocity(int r){
		switch(r){
			case LEFT:
				currentFacing = LEFT;
				rotation = 180f;
				currentState = MOVING;
				velocity = new Vector2(-MOVE_SPEED,0);
				break;
			case RIGHT:
				currentFacing = RIGHT;
				rotation = 0;
				currentState = MOVING;
				velocity = new Vector2(MOVE_SPEED,0);
				break;
			case UP:
				currentFacing = UP;
				rotation = 90f;
				currentState = MOVING;
				velocity = new Vector2(0, MOVE_SPEED);
				break;
			case DOWN:
				currentFacing = DOWN;
				rotation = 270f;
				currentState = MOVING;
				velocity = new Vector2(0, -MOVE_SPEED);
				break;
			case -1:
				currentState = IDLE;
				velocity = Vector2.Zero;
				break;
		}
	}


	/*
	 * shoot() -- determines position and velocity(direction/speed) of a newly generated bullet
	 * based on the position and facing of the player.
	 */
	public void shoot(int bulletType){
		Vector2 bulletVelocity;
		Vector2 bulletPosition;

		switch (currentFacing){
		case LEFT:
			bulletVelocity = new Vector2(-Bullet.BULLET_SPEED,0);
			bulletPosition = new Vector2(origin.x -16, origin.y);
			break;
		case RIGHT:
			bulletVelocity = new Vector2(Bullet.BULLET_SPEED,0);
			bulletPosition = new Vector2(origin.x +16, origin.y);
			break;
		case DOWN:
			bulletVelocity = new Vector2(0, -Bullet.BULLET_SPEED);
			bulletPosition = new Vector2(origin.x, origin.y - 16);
			break;
		case UP:
			bulletVelocity = new Vector2(0, Bullet.BULLET_SPEED);
			bulletPosition = new Vector2(origin.x, origin.y + 16);
			break;
		default:
			bulletVelocity = Vector2.Zero;
			bulletPosition = Vector2.Zero;
		}
		bullets.add(new Bullet(bulletPosition ,bulletVelocity, screengam));
		}

	/*
	 * getCollisionRect() -- returns the collision rectangles for the player.
	 */
	public Rectangle getCollisionRect(){
		if(currentState == IDLE){
			return new Rectangle(position.x + 6, position.y + 6, 32 -12, 32 -12);
		}
		else{
			switch(currentFacing){
			case RIGHT:
				return new Rectangle(position.x + 6 + 1, position.y + 6, 32 - 12, 32 -12);
			case LEFT:
				return new Rectangle(position.x + 6 - 1, position.y + 6, 32 -12, 32 -12);
			case UP:
				return new Rectangle(position.x +6, position.y +6 +1, 32-12, 32-12);
			case DOWN:
				return new Rectangle(position.x +6, position.y +6 -1, 32-12, 32-12);
			default:
				return null;
			}
		}
	}

	protected void update(float dt){
		origin = new Vector2(position.x + 16, position.y + 16);

		if(screengam == 1)
			if(getCollisionRect().x<0 || getCollisionRect().y < 0 ||
					getCollisionRect().x + getCollisionRect().width > GameScreen.WIDTH ||
					getCollisionRect().y + getCollisionRect().height > GameScreen.HEIGHT){
				setVelocity(STOPPED);
			}

		if(screengam == 2)
			if(getCollisionRect().x<0 || getCollisionRect().y < 0 ||
					getCollisionRect().x + getCollisionRect().width > GameScreenServer.WIDTH ||
					getCollisionRect().y + getCollisionRect().height > GameScreenServer.HEIGHT){
				setVelocity(STOPPED);
			}

		if(screengam == 3)
			if(getCollisionRect().x<0 || getCollisionRect().y < 0 ||
					getCollisionRect().x + getCollisionRect().width > GameScreenClient.WIDTH ||
					getCollisionRect().y + getCollisionRect().height > GameScreenClient.HEIGHT){
				setVelocity(STOPPED);
			}


		if(currentState == MOVING){
			position.add(velocity);
			stateTime += dt;
		}
		//TODO System.out.println("Position = " + position);

		// Update Bullet Array
		for(int i = 0; i < bullets.size(); i++){
			if(lvlManager.getCurrentLevel().resloveDestructible(bullets.get(i).getCollisionRect())){
				bullets.get(i).setAlive(false);
				continue;
			}

			if(lvlManager.getCurrentLevel().resloveUnDestructible(bullets.get(i).getCollisionRect())){
				bullets.get(i).setAlive(false);
				continue;
			}
			if(lvlManager.getCurrentLevel().resloveBase(bullets.get(i).getCollisionRect())){
				bullets.get(i).setAlive(false);
				continue;
			}

			if(lvlManager.getCurrentLevel().reslovePlayerCollisions(bullets.get(i).getCollisionRect())){
				bullets.get(i).setAlive(false);
				continue;
			}

			if(!bullets.get(i).getAlive())
				bullets.remove(i);
			else
			{
				bullets.get(i).update(dt);
			}

			/*if(!bullets.get(i).getAlive()){
				bullets.remove(i);
			}
			else{
				bullets.get(i).update(dt);
			}*/
		}

		currentFrame = movingAnimation.getKeyFrame(stateTime, true);
	}

	protected void draw(SpriteBatch batch){
		batch.draw(currentFrame, position.x, position.y, origin.x - position.x, origin.y - position.y,
				32, 32, 1, 1, rotation);
	}

	protected void drawDebug(ShapeRenderer sr){
		// Draw Bullets
		for(Bullet bullet: bullets){
			if(bullet.getAlive()){
				bullet.draw(sr);
			}
		}
	}
}