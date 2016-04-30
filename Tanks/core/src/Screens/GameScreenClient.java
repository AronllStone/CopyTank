package Screens;

import actors.Bullet;
import actors.Player;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import main.Main;
import managers.GameKeys;
import managers.InputProcessor;
import managers.Level;
import managers.LevelManager;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class GameScreenClient extends ApplicationAdapter implements Screen {

	public static int WIDTH;
	public static int HEIGHT;
	public static int GdxWidth;
	public static int GdxHeight;
	public static float kX;
	public static float kY;

	public static java.net.Socket client;
	public static java.net.Socket client2;
	public static InetAddress IpADDR = null;


	public static String P1 = "STOP";
	public static String fireP1 = "NULL";
	public static String P2 = "STOP";
	public static String fireP2 = "NULL";

	public static DataInputStream in;
	public static DataOutputStream out;

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
	public static LevelManager lvlManager;

	public static int P1lives = 3;
	public static int P2lives = 3;
	public static int P1timeLives;
	public static int P2timeLives;


	int shootTimer;
	int shootTimer2;
	Main main;

	public GameScreenClient() {

		GdxWidth = Gdx.graphics.getWidth();
		GdxHeight = Gdx.graphics.getHeight();

		UDPsender();
		try {
			client = new java.net.Socket(IpADDR, 1525);
			OutputStream sout = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(sout);
			out.writeUTF(P2 + fireP2);
		} catch (IOException e) {
			dispose();
			e.printStackTrace();
		}

		WIDTH = 640;
		HEIGHT = 360;
		kX = (float) WIDTH / (float) GdxWidth;
		kY = (float) HEIGHT / (float) GdxHeight;
		/* Set up the camera */
		camera = new OrthographicCamera(WIDTH, HEIGHT);
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
		lvlManager = new LevelManager(spriteSheet, 3);
		player2 = new Player(spriteSheet, Level.PLAYER_START_POS, 8, 8, 8, 3, 3);
		player = new Player(spriteSheet, Level.PLAYER_START_POS2, 8, 8, 8, 0, 3);
		shootTimer = 0;
		shootTimer2 = 0;

		/* Change Input Processor */
		Gdx.input.setInputProcessor(new InputProcessor());
	}

	public void UDPsender() {
		try {

			int ip1 = 0;
			int ip2 = 0;
			DatagramSocket datagramSocket = new DatagramSocket(1500);
			byte[] sendData = new byte[1];
			sendData = String.valueOf((int)Math.random() * 100).getBytes();
			byte[] receiveData = new byte[1024];
			for (int i = 0; i < 254; i++)
				for (int j = 0; j < 254; j++) {
					ip1 = i;
					ip2 = j;
					String Ip = "192.168." + ip1 + "." + ip2;
					InetAddress ipAd = InetAddress.getByName(Ip);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAd, 1520);
					datagramSocket.send(sendPacket);
					//System.out.println(Ip);
				}
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 1520);
			datagramSocket.send(sendPacket);
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				datagramSocket.receive(receivePacket);
				System.out.println("In recv");
				if (receivePacket != null) {
					String recv = Arrays.toString(receiveData);
					IpADDR = receivePacket.getAddress();
//					IpADDR = InetAddress.getByName("localhost");
					break;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void render(float a) {

		try {
			InputStream inputStream = client.getInputStream();
			in = new DataInputStream(inputStream);
			String lin = in.readUTF();
			P1 = lin.substring(0, 4);
			fireP1 = lin.substring(4);
		} catch (IOException e) {
			e.printStackTrace();
		}

		P1timeLives++;
		if (!player.isAlive() & P1lives > 1 & P1timeLives > 100) {
			P1lives--;
			player = new Player(spriteSheet, new Vector2(120, 4), 8, 8, 8, 0, 3);
			P1timeLives = 0;
		}


		P2timeLives++;
		if (!player2.isAlive() & P2lives > 1 & P2timeLives > 100) {
			P2lives--;
			player2 = new Player(spriteSheet, new Vector2(144, 4), 8, 8, 8, 3, 3);
			P2timeLives = 0;
		}

		/* Clear the screen */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		/*****************************************
		 *  			  UPDATING               *
		 *****************************************/
		shootTimer++;
		shootTimer2++;
			if (GameKeys.isDown(GameKeys.LEFT)) {
				player.setVelocity(Player.LEFT);
				P2 = "LEFT";
			} else if (GameKeys.isDown(GameKeys.UP)) {
				player.setVelocity(Player.UP);
				P2 = "UPPP";
			} else if (GameKeys.isDown(GameKeys.RIGHT)) {
				player.setVelocity(Player.RIGHT);
				P2 = "RIGH";
			} else if (GameKeys.isDown(GameKeys.DOWN)) {
				player.setVelocity(Player.DOWN);
				P2 = "DOWN";
			} else {
				player.setVelocity(Player.STOPPED);
				P2 = "STOP";
			}

			if (lvlManager.getCurrentLevel().resolveCollisions(player.getCollisionRect())) {
				player.setVelocity(Player.STOPPED);
			}
			if (P1.equals("LEFT")) {
				player2.setVelocity(Player.LEFT);
			} else if (P1.equals("UPPP")) {
				player2.setVelocity(Player.UP);
			} else if (P1.equals("RIGH")) {
				player2.setVelocity(Player.RIGHT);
			} else if (P1.equals("DOWN")) {
				player2.setVelocity(Player.DOWN);
			} else {
				player2.setVelocity(Player.STOPPED);
			}

			if (lvlManager.getCurrentLevel().resolveCollisions(player2.getCollisionRect())) {
				player2.setVelocity(Player.STOPPED);
			}

		if (player.isAlive() && player2.isAlive())
			for (int i = 0; i < lvlManager.getCurrentLevel().getEnemiesList().size(); i++) {
				for (int j = 0; j < lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().size(); j++) {

					if (player.isAlive() & lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).getCollisionRect().overlaps(player.getCollisionRect())) {
						lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).setAlive(false);
						//System.out.println("Player down");
						player.setAlive(false);
						P1timeLives = 0;
						//TODO: Add logic to remove player
					}

					if (player2.isAlive() & lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).getCollisionRect().overlaps(player2.getCollisionRect())) {
						lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).setAlive(false);
						//System.out.println("Player down");
						player2.setAlive(false);
						P2timeLives = 0;
						//TODO: Add logic to remove player
					}

				}
			}


		if (player.isAlive())
			if (GameKeys.isDown(GameKeys.SPACE)) {
				if (shootTimer > 30) {
					player.shoot(Bullet.BULLET_PLAYER);
					fireP2 = "FIRE";
					shootTimer = 0;
				}
			} else
				fireP2 = "NULL";
		if (player2.isAlive())
			if (fireP1.equals("FIRE")) {
				if (shootTimer2 > 30) {
					player2.shoot(Bullet.BULLET_PLAYER);
					shootTimer2 = 0;
				}
			}
		if (player.isAlive())
			player.update(Gdx.graphics.getDeltaTime());
		if (player2.isAlive())
			player2.update(Gdx.graphics.getDeltaTime());
		//System.out.println("Player " + player.isAlive());
		if (player.isAlive())
			for (int i = 0; i < player.getBullets().size(); i++) {
				if (lvlManager.getCurrentLevel().resloveDestructible(player.getBullets().get(i).getCollisionRect())) {
					player.getBullets().get(i).setAlive(false);
					continue;
				}
				if (lvlManager.getCurrentLevel().resloveUnDestructible(player.getBullets().get(i).getCollisionRect())) {
					player.getBullets().get(i).setAlive(false);
					continue;
				}
				if (lvlManager.getCurrentLevel().resloveBase(player.getBullets().get(i).getCollisionRect())) {
					player.getBullets().get(i).setAlive(false);
					continue;
				}
				if (lvlManager.getCurrentLevel().resloveEnemyCollisions(player.getBullets().get(i).getCollisionRect())) {
					player.getBullets().get(i).setAlive(false);
					continue;
				}
			}
		if (player2.isAlive())
			for (int i = 0; i < player2.getBullets().size(); i++) {
				if (lvlManager.getCurrentLevel().resloveDestructible(player2.getBullets().get(i).getCollisionRect())) {
					player2.getBullets().get(i).setAlive(false);
					continue;
				}
				if (lvlManager.getCurrentLevel().resloveUnDestructible(player2.getBullets().get(i).getCollisionRect())) {
					player2.getBullets().get(i).setAlive(false);
					continue;
				}
				if (lvlManager.getCurrentLevel().resloveBase(player2.getBullets().get(i).getCollisionRect())) {
					player2.getBullets().get(i).setAlive(false);
					continue;
				}
				if (lvlManager.getCurrentLevel().resloveEnemyCollisions(player2.getBullets().get(i).getCollisionRect())) {
					player2.getBullets().get(i).setAlive(false);
					continue;
				}
			}

		lvlManager.update(Gdx.graphics.getDeltaTime());

		GameKeys.update();

		//TODO: СДЕЛАТЬ ОТПРАВКУ ДАННЫХ С КЛИЕНТА
		try {


			OutputStream outputStream = client.getOutputStream();

			out = new DataOutputStream(outputStream);

			String line = P2 + fireP2;
			System.out.println("LOL");
			out.writeUTF(line);

		} catch (IOException e) {
			e.printStackTrace();
		}


		/*****************************************
		 *  			   DRAWING               *
		 *****************************************/
		lvlManager.drawLevelBack();

		batch.begin();
		if (player.isAlive())
			player.draw(batch);
		if (player2.isAlive())
			player2.draw(batch);
		lvlManager.draw(batch);
		batch.end();

		sr.begin(ShapeType.Filled);
		if (player.isAlive())
			player.drawDebug(sr);
		if (player2.isAlive())
			player2.drawDebug(sr);
		lvlManager.drawShapes(sr);
		sr.end();

		lvlManager.drawLevelFor();
	}


	@Override
	public void show() {

	}

	public void resize(int width, int height) {

	}

	public void pause() {

	}

	public void resume() {

	}

	public void render() {
		System.out.println("Render of AppListnder");
	}

	@Override
	public void hide() {

	}

	public void dispose() {
		batch.dispose();
		spriteSheet.dispose();
		sr.dispose();
		try {
			client.close();
			client2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}