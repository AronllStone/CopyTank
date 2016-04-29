package managers;

import Screens.GameScreenClient;
import Screens.GameScreenServer;
import actors.Enemy;
import actors.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import Screens.GameScreen;

import java.util.ArrayList;

public class Level {

	public static final int TILE_SIZE = 16;
	public static final Vector2 PLAYER_START_POS = new Vector2(144,4);
	public static final Vector2 PLAYER_START_POS2 = new Vector2(120,4);
	public static final Rectangle BaseCollision = new Rectangle(176,0,32,32);

	public static int[] bLayers = {0};
	public static int[] fLayers = {1};

	private Texture spriteSheet;

	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;

	private ArrayList<Rectangle> collisionRects;
	private ArrayList<Rectangle> destructibleRects;
	private ArrayList<Rectangle> undestructibleRects;
	private ArrayList<Rectangle> baseRect;
	private ArrayList<Rectangle> spawnLocations;

	private TiledMapTileLayer destructibleLayer;
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer undestructibleLayer;
	private TiledMapTileLayer baseLayer;
	private int totalEnemies;
	private int enemiesLeft;

	private ArrayList<Enemy> enemies;
	private ArrayList<Player> players;

	private int spawnTimerCoolDown;
	private int spawnRate;
	private int ScreenGame;


	public Level(String mapName, Texture spriteSheet, int totalEnemies, int spawnRate, int ScreenGame){
		/*Create the Map*/
		map = new TmxMapLoader().load(mapName);
		renderer = new OrthogonalTiledMapRenderer(map, 1f);
		this.ScreenGame = ScreenGame;
		this.spriteSheet = spriteSheet;

		destructibleLayer = (TiledMapTileLayer) map.getLayers().get("Interactable");
		undestructibleLayer = (TiledMapTileLayer) map.getLayers().get("Meta");
		collisionLayer = (TiledMapTileLayer) map.getLayers().get("Meta");
		baseLayer = (TiledMapTileLayer) map.getLayers().get("Interactable");

		collisionRects = new ArrayList<Rectangle>();
		destructibleRects = new ArrayList<Rectangle>();
		undestructibleRects = new ArrayList<Rectangle>();
		baseRect = new ArrayList<Rectangle>();

		spawnLocations = new ArrayList<Rectangle>();



		this.totalEnemies = totalEnemies;
		enemies = new ArrayList<Enemy>();
		players = new ArrayList<Player>();

		this.spawnRate = spawnRate;

		init();

		constructCollisionRects();
	}

	public void init(){
		spawnTimerCoolDown = spawnRate + 1;
		enemiesLeft = totalEnemies;
		//TODO: set player position here
	}

	public int getEnemiesLeft(){
		return enemiesLeft;
	}

	public int getNumEnemiesOnScreen(){
		return enemies.size();
	}

	public ArrayList<Enemy> getEnemiesList(){
		return enemies;
	}

	public void constructCollisionRects(){

		TiledMapTile currentTile;

		int cols = collisionLayer.getHeight();
		int rows = collisionLayer.getWidth();

		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				if(collisionLayer.getCell(i, j) != null){
					currentTile = collisionLayer.getCell(i,j).getTile();
					if(currentTile.getProperties().containsKey("Collidable")){
						if(currentTile.getProperties().containsKey("Destructible")){
							destructibleRects.add(new Rectangle(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE));
						}
						else
						if(currentTile.getProperties().containsKey("UnDestructible"))
						{
							undestructibleRects.add(new Rectangle(i*TILE_SIZE,j*TILE_SIZE,TILE_SIZE,TILE_SIZE));
						}
						else
						{
							collisionRects.add(new Rectangle(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE));
						}
					}
					if(currentTile.getProperties().containsKey("BaseP"))
					{
						//collisionRects.add(new Rectangle(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE));
						baseRect.add(new Rectangle(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE));
					}
					if(currentTile.getProperties().containsKey("Spawnable")){
						spawnLocations.add(new Rectangle(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE));
					}
				}
			}
		}
	}

	public boolean resolveCollisions(Rectangle rect){
		for(Rectangle collRect : collisionRects){
			if(rect.overlaps(collRect)){
				return true;
			}
		}
		for(Rectangle dRect: destructibleRects){
			if(rect.overlaps(dRect)){
				return true;
			}
		for(Rectangle bRect: baseRect)
		{
			if(rect.overlaps(bRect))
				return true;
		}
		for(Rectangle undRect: undestructibleRects)
			if (rect.overlaps(undRect)){
				return true;
			}
		}
		return false;
	}

	public boolean resloveDestructible(Rectangle rect){

		int destroyed = 0;
		for(int i = 0; i < destructibleRects.size(); i++){
			if(rect.overlaps(destructibleRects.get(i))){
				destructibleLayer.setCell((int)destructibleRects.get(i).x/TILE_SIZE,
						(int)destructibleRects.get(i).y/TILE_SIZE,null);
				destructibleRects.remove(i);
				destroyed++;
			}
		}
		if(destroyed > 0){
			destroyed = 0;
			return true;
		}
		return false;
	}

	public boolean resloveUnDestructible(Rectangle rect){
		int shot = 0;
		for(int i = 0; i < undestructibleRects.size(); i++){
			if(rect.overlaps(undestructibleRects.get(i))){
				undestructibleLayer.setCell((int)undestructibleRects.get(i).x/TILE_SIZE,
						(int)undestructibleRects.get(i).y/TILE_SIZE,null);
				     shot = 1;
			}
		}

		if(shot == 1)
		return true;
		return false;
	}

	public boolean resloveBase(Rectangle rect){

		int destroyed = 0;

		for(int i = 0 ; i < baseRect.size(); i++) {
			if (rect.overlaps(baseRect.get(i))){
				baseLayer.setCell((int)baseRect.get(0).x/TILE_SIZE,(int)baseRect.get(0).y/TILE_SIZE,null);
				baseLayer.setCell((int)baseRect.get(1).x/TILE_SIZE,(int)baseRect.get(1).y/TILE_SIZE,null);
				baseLayer.setCell((int)baseRect.get(2).x/TILE_SIZE,(int)baseRect.get(2).y/TILE_SIZE,null);
				baseLayer.setCell((int)baseRect.get(3).x/TILE_SIZE,(int)baseRect.get(3).y/TILE_SIZE,null);
			baseRect.clear();
			destroyed++;}
		}


		if(destroyed > 0){
			return true;
		}
		return false;
	}

	public void spawnEnemy(){
		if(spawnTimerCoolDown < spawnRate)
			return;

		spawnTimerCoolDown = 0;
		enemiesLeft--;

		double spawnLocationChance = Math.random();
		double spawnOpportunity = 1f/spawnLocations.size();

		for(int i = 1; i <= spawnLocations.size(); i++){
			if(spawnLocationChance < (i*spawnOpportunity)){
				enemies.add(new Enemy(spriteSheet,new Vector2(spawnLocations.get(i-1).x,
						spawnLocations.get(i-1).y), 8, 8, 8, 1, ScreenGame));
				break;
			}
		}
	}

	public boolean resloveEnemyCollisions(Rectangle r){
		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).getCollisionRect().overlaps(r)){
				System.out.println("enemy");
				enemies.get(i).setAlive(false);
				return true;
			}
		}

		return false;
	}

	public boolean reslovePlayerCollisions(Rectangle r){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getCollisionRect().overlaps(r)){
				System.out.println("qweqwe");
				players.get(i).setAlive(false);
				return true;
			}
		}

		return false;
	}

	public void update(float dt){
		spawnTimerCoolDown ++;

		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).isAlive()){
				if(resolveCollisions(enemies.get(i).getCollisionRect())){
			    	enemies.get(i).setVelocity(Enemy.STOPPED);
				}

				for(int j = 0; j < enemies.get(i).getBullets().size(); j++){
					if(resloveDestructible(enemies.get(i).getBullets().get(j).getCollisionRect())){
						enemies.get(i).getBullets().get(j).setAlive(false);
					}
					if(resloveBase(enemies.get(i).getBullets().get(j).getCollisionRect())){
						enemies.get(i).getBullets().get(j).setAlive(false);
					}
					if(reslovePlayerCollisions(enemies.get(i).getBullets().get(j).getCollisionRect())){
						enemies.get(i).getBullets().get(j).setAlive(false);
					}

				}

				enemies.get(i).update(dt);
			}
			else{
				enemies.remove(i);
			}
			/*for(int i = 0; i<players.size(); i++)
			{
				if(!players.get(i).isAlive())
			}*/
		}
	}


	public void drawBackground(){
		if(ScreenGame == 1)
			renderer.setView(GameScreen.camera);
		if(ScreenGame == 2)
			renderer.setView(GameScreenServer.camera);
		if(ScreenGame == 3)
			renderer.setView(GameScreenClient.camera);
		renderer.render(bLayers);
	}

	public void draw(SpriteBatch batch){
		for(Enemy e : enemies){
			e.draw(batch);
		}
	}

	public void drawShapes(ShapeRenderer sr){
		for(Enemy e: enemies){
			e.drawDebug(sr);
		}
	}

	public void drawForeground(){
		if(ScreenGame == 1)
			renderer.setView(GameScreen.camera);
		if(ScreenGame == 2)
			renderer.setView(GameScreenServer.camera);
		if(ScreenGame == 3)
			renderer.setView(GameScreenClient.camera);
		renderer.render(fLayers);
	}
}