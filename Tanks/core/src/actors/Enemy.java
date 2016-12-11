package actors;

import Screens.GameScreen;
import Screens.GameScreenClient;
import Screens.GameScreenServer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import managers.Level;


public class Enemy extends GameObject {

	private int random;
	private boolean alive;
	public int screengas;
	public int shootcount;
	public int id;
	public boolean draw;
	public boolean seePlayer = false;


	public Enemy(Texture spriteSheet, Vector2 position, int spriteSheetRows,
				 int spriteSheetCols, int numFrames, int animationStartRow, int screengam, int id) {
		super(spriteSheet, position, spriteSheetRows, spriteSheetCols, numFrames, animationStartRow, screengam);

		this.screengas = screengam;
		random = 0;
		velocity = Vector2.Zero;
		alive = true;
		shootcount = 0;
		this.id = id;
		draw = true;


		randomMovement();
	}

	public boolean isAlive() {
		return alive;
	}

	public int getId() {
		return id;
	}

	public void setId(int i) {
		id = i;
	}

	public void setAlive(boolean b) {
		alive = b;
	}

	public void randomMovement() {

		random = (screengas == 1) ? (int) (GameScreen.random.nextDouble() * 100) : ((screengas == 2) ? (int) (GameScreenServer.random.nextDouble() * 100) : ((screengas == 3) ? (int) (GameScreenClient.random.nextDouble() * 100) : 0));

		switch (currentFacing) {
			case LEFT:
				if (random < 10) { //Turn up
					setVelocity(RIGHT);
				} else if (random < 50) { //Turn Down
					setVelocity(DOWN);
				} else if (random < 75) { // Turn RIGHT
					setVelocity(UP);
				} else { //Idle
					setVelocity(STOPPED);
				}
				break;
			case RIGHT:
				if (random < 10) { //Turn Down
					setVelocity(LEFT);
				} else if (random < 30) { //Turn Left
					setVelocity(DOWN);
				} else if (random < 75) { // Turn Up
					setVelocity(UP);
				} else { //Idle
					setVelocity(STOPPED);
				}
				break;
			case UP:
				if (random < 10) { //Turn Right
					setVelocity(DOWN);
				} else if (random < 50) { //Turn Down
					setVelocity(LEFT);
				} else if (random < 75) { // Turn Left
					setVelocity(RIGHT);
				} else { //Idle
					setVelocity(STOPPED);
				}
			case DOWN:
				if (random < 10) { //Turn Right
					setVelocity(UP);
				} else if (random < 50) { //Turn Left
					setVelocity(LEFT);
				} else if (random < 75) { // Turn Up
					setVelocity(RIGHT);
				} else { //Idle
					setVelocity(STOPPED);
				}
				break;
			case IDLE:
				if (random < 25) { //Turn Right
					setVelocity(RIGHT);
				} else if (random < 50) { //Turn Down
					setVelocity(DOWN);
				} else if (random < 75) { // Turn UP
					setVelocity(UP);
				} else if (random < 100) { // Turn Left
					setVelocity(LEFT);
				}
				break;
		}
	}

	@Override
	public void update(float dt) {

		if (draw) {
			Level level = GameScreen.lvlManager.getCurrentLevel();
			double mathRand = 0;
			shootcount++;

			if (screengas == 1)
				mathRand = GameScreen.random.nextDouble();
			else if (screengas == 2)
				mathRand = GameScreenServer.random.nextDouble();
			else if (screengas == 3)
				mathRand = GameScreenClient.random.nextDouble();


			if ((super.previousX - getPosition().x == 0) && (super.previousY - getPosition().y == 0)) {
				randomMovement();
			}

			float EnemyOnLinePlayerX = GameScreen.player.getPosition().x - getPosition().x;
			float EnemyOnLinePlayerY = GameScreen.player.getPosition().y - getPosition().y;
/*
		float EnemyAverageX = getPosition().x + 4;
		float EnemyAverageY = getPosition().y + 4;	//TODO переменные для охоты на базу


		float Base0X = 0;
		float Base1Y = 0;
		float Base2X = 0;
		if (GameScreen.lvlManager.getCurrentLevel().baseIsAlive()) {
			Base0X = GameScreen.lvlManager.getCurrentLevel().get0Base().x;
			Base1Y = GameScreen.lvlManager.getCurrentLevel().get1Base().y;
			Base2X = GameScreen.lvlManager.getCurrentLevel().get2Base().x;
		}
*/

		/*if (mathRand < .01) {
			randomMovement();
		}*/

//		if ((mathRand < .01) && (GameScreen.player.getPosition().x - getPosition().x == 0 || GameScreen.player.getPosition().y - getPosition().y == 0)) {
//		if ((EnemyOnLinePlayerX == 0 || EnemyOnLinePlayerY == 0) || (Base0X < EnemyAverageX & Base2X > EnemyAverageX) || (Base1Y > EnemyAverageY)) {	//TODO проверка на прямую наводку на базу, а так же на игрока
			if ((EnemyOnLinePlayerX == 0 || EnemyOnLinePlayerY == 0)) {
				turnEnemyOnPlayer(EnemyOnLinePlayerX, EnemyOnLinePlayerY);

			/*if (Base0X < EnemyAverageX & Base2X > EnemyAverageX) {	//TODO боты начинают охоту на базу
				setVelocity(DOWN);
			}
			if (Base1Y > EnemyAverageY) {
				if (EnemyAverageX > Base2X)
					setVelocity(LEFT);
				else if (EnemyAverageX < Base0X)
					setVelocity(RIGHT);
			}
*/
				if (shootcount > 30) {
					shoot(Bullet.BULLET_ENEMY);
					shootcount = 0;
				}
			}

			if (mathRand < .01) {
				if (shootcount > 30) {
					shoot(Bullet.BULLET_ENEMY);
					shootcount = 0;
				}
			}
			if (level.resolveCollisions(getCollisionRect()) || level.resolveEnemyOnPlayerCollisions(getCollisionRect()) || level.resolveEnemyOnEnemyCollisions(getCollisionRect(), id)) {
				setVelocity(STOPPED);
			}
		}
		super.update(dt);
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	public void turnEnemyOnPlayer(float EnemyOnLinePlayerX, float EnemyOnLinePlayerY) {
		if (EnemyOnLinePlayerX > 0)
			setVelocity(RIGHT);
		if (EnemyOnLinePlayerX < 0)
			setVelocity(LEFT);
		if (EnemyOnLinePlayerY > 0)
			setVelocity(UP);
		if (EnemyOnLinePlayerY < 0)
			setVelocity(DOWN);
	}

	@Override
	public void drawDebug(ShapeRenderer sr) {
		sr.setColor(Color.RED);
		super.drawDebug(sr);
	}
}
