package managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class LevelManager {

	private int currentLevel = 0;
	private ArrayList<Level> levels;

	private Level testLevel;
	private Level level2;
	public int ScreenGame = 0;

	public LevelManager(Texture spriteSheet, int ScreenG) {
		levels = new ArrayList<Level>();
		//ScreenGame = ScreenG;

		testLevel = new Level("Levels/TestLevel.tmx", spriteSheet, 20, 110, ScreenG);
		levels.add(testLevel);

		level2 = new Level("Levels/Level2.tmx", spriteSheet, 20, 110, ScreenG);
		levels.add(level2);

	}

	public void setScreenGame(int screenGame) {
		this.ScreenGame = screenGame;
	}

	public int getScreenGame() {
		return ScreenGame;
	}

	public int getAllLevels() {
		return levels.size();
	}

	public int getCurrentLevelNumber() {
		return currentLevel;
	}

	public Level getCurrentLevel() {
		return levels.get(currentLevel);
	}

	public void nextLevel() {
		currentLevel++;
	}

	public void update(float dt) {

		if (levels.get(currentLevel).getNumEnemiesOnScreen() < 1 && levels.get(currentLevel).getEnemiesLeft() > 0) { //TODO количиство enemy на экране
			levels.get(currentLevel).spawnEnemy();
		}

		levels.get(currentLevel).update(dt);
	}

	public void drawLevelFor() {
		levels.get(currentLevel).drawForeground();
	}

	public void draw(SpriteBatch batch) {
		levels.get(currentLevel).draw(batch);
	}

	public void drawShapes(ShapeRenderer sr) {
		levels.get(currentLevel).drawShapes(sr);
	}

	public void drawLevelBack() {
		levels.get(currentLevel).drawBackground();
	}

}
