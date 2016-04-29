package Screens;

import actors.Bullet;
import actors.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import main.Main;
import managers.GameKeys;
import managers.InputProcessor;
import managers.Level;
import managers.LevelManager;

public class GameScreen extends ApplicationAdapter implements Screen{

	public static int WIDTH;
	public static int HEIGHT;
	public static int GdxWidth;
	public static int GdxHeight;
	public static float kX;
	public static float kY;



	public static OrthographicCamera camera;

	Texture spriteSheet;
	Texture arrowUp;
	Texture arrowDown;
	Texture arrowLeft;
	Texture arrowRight;


	SpriteBatch batch;
	ShapeRenderer sr;

	public static Player player;
	public static Player player2;
	LevelManager lvlManager;
	BitmapFont font;

	int shootTimer;
	int shootTimer2;
    Main main;

   public GameScreen(){

       System.out.println("GameScreen is created");
       GdxWidth = Gdx.graphics.getWidth();
       GdxHeight = Gdx.graphics.getHeight();


       WIDTH = 640;
       HEIGHT = 360;
       kX = (float) WIDTH/(float)GdxWidth;
       kY = (float) HEIGHT/(float) GdxHeight;
		/* Set up the camera */
       camera = new OrthographicCamera(WIDTH,HEIGHT);
       camera.setToOrtho(false, WIDTH, HEIGHT);
       camera.update();

		/* Set up variables */
       batch = new SpriteBatch();
       sr = new ShapeRenderer();
       arrowUp = new Texture(Gdx.files.internal("ArrowUp.png"));
       arrowDown = new Texture(Gdx.files.internal("ArrowDown.png"));
       arrowLeft = new Texture(Gdx.files.internal("ArrowLeft.png"));
       arrowRight = new Texture(Gdx.files.internal("ArrowRight.png"));
       spriteSheet = new Texture(Gdx.files.internal("TanksSpriteSheet.png"));
       lvlManager = new LevelManager(spriteSheet, 1);
       player = new Player(spriteSheet, Level.PLAYER_START_POS, 8, 8, 8, 0, 1);
       player2 = new Player(spriteSheet, Level.PLAYER_START_POS2, 8, 8, 8, 0, 1);
       shootTimer = 0;
       shootTimer2 = 0;


		/* Change Input Processor */
       Gdx.input.setInputProcessor(new InputProcessor());
   }

	public void render (float a) {
        //System.out.println("GameScreen is render");
        /* Clear the screen */
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    batch.setProjectionMatrix(camera.combined);

	    /*****************************************
	     *  			  UPDATING               *
	     *****************************************/
	    shootTimer ++;
	    shootTimer2 ++;

        if(GameKeys.isDown(GameKeys.LEFT)){
	    	player.setVelocity(Player.LEFT);
	    }
	    else if(GameKeys.isDown(GameKeys.UP)){
	    	player.setVelocity(Player.UP);
	    }
	    else if(GameKeys.isDown(GameKeys.RIGHT)){
	    	player.setVelocity(Player.RIGHT);
	    }
	    else if(GameKeys.isDown(GameKeys.DOWN)){
	    	player.setVelocity(Player.DOWN);
	    }
	    else{
	    	player.setVelocity(Player.STOPPED);
	    }

	    if(lvlManager.getCurrentLevel().resolveCollisions(player.getCollisionRect())){
	    	player.setVelocity(Player.STOPPED);
	    }

       /* if(GameKeys.isDown(GameKeys.A)){
            player2.setVelocity(Player.LEFT);
        }
        else if(GameKeys.isDown(GameKeys.W)){
            player2.setVelocity(Player.UP);
        }
        else if(GameKeys.isDown(GameKeys.D)){
            player2.setVelocity(Player.RIGHT);
        }
        else if(GameKeys.isDown(GameKeys.S)){
            player2.setVelocity(Player.DOWN);
        }
        else{
            player2.setVelocity(Player.STOPPED);
        }*/

        if(lvlManager.getCurrentLevel().resolveCollisions(player2.getCollisionRect())){
            player2.setVelocity(Player.STOPPED);
        }

	    for(int i = 0; i< lvlManager.getCurrentLevel().getEnemiesList().size(); i++){
	    	for(int j = 0; j < lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().size(); j++){
	    		if(lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).getCollisionRect().overlaps(player.getCollisionRect())){
	    			lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).setAlive(false);
	    			//TODO: Add logic to remove player
	    		}
	    	}
	    }

	    if(GameKeys.isDown(GameKeys.SPACE)){
	    	if(shootTimer > 30){
	    		player.shoot(Bullet.BULLET_PLAYER);
	    		shootTimer = 0;
	    	}
	    }

       /* if(GameKeys.isDown(GameKeys.F)){
            if(shootTimer2 > 30){
                player2.shoot(Bullet.BULLET_PLAYER);
                shootTimer2 = 0;
            }
        }*/

	    player.update(Gdx.graphics.getDeltaTime());
		player2.update(Gdx.graphics.getDeltaTime());
        //System.out.println("Player " + player.isAlive());
	    for(int i = 0; i < player.getBullets().size(); i++){
            if(lvlManager.getCurrentLevel().resloveDestructible(player.getBullets().get(i).getCollisionRect())){
                player.getBullets().get(i).setAlive(false);
                continue;
            }
            if(lvlManager.getCurrentLevel().resloveUnDestructible(player.getBullets().get(i).getCollisionRect())){
                player.getBullets().get(i).setAlive(false);
                continue;
            }
            if(lvlManager.getCurrentLevel().resloveBase(player.getBullets().get(i).getCollisionRect())){
                player.getBullets().get(i).setAlive(false);
                continue;
            }
            if(lvlManager.getCurrentLevel().resloveEnemyCollisions(player.getBullets().get(i).getCollisionRect())){
                player.getBullets().get(i).setAlive(false);
                continue;
            }
        }

        for(int i = 0; i < player2.getBullets().size(); i++){
            if(lvlManager.getCurrentLevel().resloveDestructible(player2.getBullets().get(i).getCollisionRect())){
                player2.getBullets().get(i).setAlive(false);
                continue;
            }
            if(lvlManager.getCurrentLevel().resloveUnDestructible(player2.getBullets().get(i).getCollisionRect())){
                player2.getBullets().get(i).setAlive(false);
                continue;
            }
            if(lvlManager.getCurrentLevel().resloveBase(player2.getBullets().get(i).getCollisionRect())){
                player2.getBullets().get(i).setAlive(false);
                continue;
            }
            if(lvlManager.getCurrentLevel().resloveEnemyCollisions(player2.getBullets().get(i).getCollisionRect())){
                player2.getBullets().get(i).setAlive(false);
                continue;
            }
        }

	    lvlManager.update(Gdx.graphics.getDeltaTime());

	    GameKeys.update();



	    /*****************************************
	     *  			   DRAWING               *
	     *****************************************/
	    lvlManager.drawLevelBack();

	    batch.begin();
	    player.draw(batch);
        player2.draw(batch);
//		batch.draw(arrowUp, ((float) Gdx.graphics.getWidth()*5/32)*kX, ((float) Gdx.graphics.getHeight() - (float)Gdx.graphics.getHeight()*3/4)* kY);
//		batch.draw(arrowDown, ((float) Gdx.graphics.getWidth()*5/32)*kX,( (float) Gdx.graphics.getHeight() - (float)Gdx.graphics.getHeight()*31/32)*kY);
//		batch.draw(arrowLeft, ((float) Gdx.graphics.getWidth()/16)*kX, ((float) Gdx.graphics.getHeight() - (float)Gdx.graphics.getHeight()*7/8)*kY);
//		batch.draw(arrowRight,((float) Gdx.graphics.getWidth()/48)*kX, ((float) Gdx.graphics.getHeight() - (float)Gdx.graphics.getHeight()*7/8)*kY);
	    lvlManager.draw(batch);
	    batch.end();

	    sr.begin(ShapeType.Filled);
	    player.drawDebug(sr);
        player2.drawDebug(sr);
	    lvlManager.drawShapes(sr);
	    sr.end();

	    lvlManager.drawLevelFor();
	}

    @Override
    public void show() {

    }

    public void resize (int width, int height) {

	}

	public void pause () {

	}

	public void resume () {

	}

    public void render(){
        System.out.println("Render of AppListnder");
    }

    @Override
    public void hide() {

    }

    public void dispose () {
        batch.dispose();
        spriteSheet.dispose();
        sr.dispose();

	}
}