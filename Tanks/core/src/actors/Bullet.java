package actors;

import Screens.GameScreen;
import Screens.GameScreenClient;
import Screens.GameScreenServer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet{

	private Vector2 position;
	private Vector2 velocity;
	private boolean alive;

	public static final int BULLET_PLAYER = 0;
	public static final int BULLET_ENEMY = 1;
	public static final int RADIUS = 4;

	public static final int BULLET_SPEED = 5;
	public int screengam=0;

	public Bullet(Vector2 position, Vector2 velocity, int screengam) {
		this.position = position;
		this.velocity = velocity;
		this.alive = true;
		this.screengam = screengam;
	}

	public Rectangle getCollisionRect(){
		return new Rectangle(position.x - RADIUS, position.y - RADIUS, 2*RADIUS, 2*RADIUS);
	}

	public void update(float dt){
		position.x += velocity.x;
		position.y += velocity.y;
		if(screengam ==1)
		{
			if(position.x < 0 - RADIUS){
				alive = false;
			}
			else if(position.x > GameScreen.WIDTH){
				alive = false;
			}
			else if(position.y > GameScreen.HEIGHT){
				alive = false;
			}
			else if(position.y < 0 - RADIUS){
				alive = false;
			}
		}

		if(screengam ==2)
		{
			if(position.x < 0 - RADIUS){
				alive = false;
			}
			else if(position.x > GameScreenServer.WIDTH){
				alive = false;
			}
			else if(position.y > GameScreenServer.HEIGHT){
				alive = false;
			}
			else if(position.y < 0 - RADIUS){
				alive = false;
			}
		}

		if(screengam ==3)
		{
			if(position.x < 0 - RADIUS){
				alive = false;
			}
			else if(position.x > GameScreenClient.WIDTH){
				alive = false;
			}
			else if(position.y > GameScreenClient.HEIGHT){
				alive = false;
			}
			else if(position.y < 0 - RADIUS){
				alive = false;
			}
		}


	}

	public void draw(ShapeRenderer sr){
		if(screengam ==1)
		sr.circle(position.x/ GameScreen.kX, position.y/ GameScreen.kY, RADIUS);
		if(screengam ==2)
			sr.circle(position.x/ GameScreenServer.kX, position.y/ GameScreenServer.kY, RADIUS);
		if(screengam == 3)
			sr.circle(position.x/ GameScreenClient.kX, position.y/ GameScreenClient.kY, RADIUS);
	}

	public boolean getAlive(){
		return alive;
	}

	public void setAlive(boolean b){
		alive = b;
	}
}
