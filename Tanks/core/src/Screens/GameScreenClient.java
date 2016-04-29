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

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
	public static String P1buf = "STOP";
	public static String fireP1buf = "NULL";
	public static String P2buf = "STOP";
	public static String fireP2buf = "NULL";

	public static DataInputStream in;
	public static DataOutputStream out;
	public static DataInputStream in2;
	public static DataOutputStream out2;


	public static int timeServer = 0;
	public static int timeClient = 0;

	public static Queue<String> queueTimeServer = new LinkedList<String>();
	public static Queue<String> moveP1queue = new LinkedList<String>();
	public static Queue<String> fireP1queue = new LinkedList<String>();
	public static Queue<String> queueBuf = new LinkedList<String>();

	public static int lol;


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

	public GameScreenClient() {

		//this.setScreen(new Menu(this));
		//Gdx.graphics.setWindowedMode(800,480);

		System.out.println("GameScreen is created");
		//System.out.println(Gdx.graphics.getDensity());
		//double he = Gdx.graphics.getHeight()*0.7;
		//double wi = Gdx.graphics.getWidth()* 0.7;
		//WIDTH = Gdx.graphics.getWidth();
		//HEIGHT = Gdx.graphics.getHeight();
		//WIDTH = (int)wi;
		//HEIGHT = (int)he;
		GdxWidth = Gdx.graphics.getWidth();
		GdxHeight = Gdx.graphics.getHeight();


		int ip1 = 0;
		int ip2 = 0;
		try {
			DatagramSocket datagramSocket = new DatagramSocket(1500);
			byte[] sendData = new byte[1];
			byte[] receiveData = new byte[1024];
			/*for (int i = 0; i < 254; i++)
				for (int j = 0; j < 254; j++) {
					ip1 = i;
					ip2 = j;
					String Ip = "192.168." + ip1 + "." + ip2;
					InetAddress ipAd = InetAddress.getByName(Ip);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAd, 1520);
					datagramSocket.send(sendPacket);
					//System.out.println(Ip);
				}*/
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("localhost"), 1520);
			datagramSocket.send(sendPacket);
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				//datagramSocket.setSoTimeout(20);
				datagramSocket.receive(receivePacket);
				System.out.println("In recv");
				if (receivePacket != null) {
					String recv = Arrays.toString(receiveData);
					System.out.println("Recv yes");
					IpADDR = receivePacket.getAddress();
//					IpADDR = InetAddress.getByName("localhost");
					break;
				}
				System.out.println("OUT RECV");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			client = new java.net.Socket(IpADDR, 1525);
			System.out.println("Server is connected");
			OutputStream sout = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(sout);
			out.writeUTF(P2);
			out.writeUTF(fireP2);
			out.writeUTF(String.valueOf(timeClient));


		} catch (IOException e) {
			dispose();
			e.printStackTrace();
		}

/*
		try {
			client2 = new java.net.Socket(IpADDR, 1526);
			System.out.println("Server is connected");
			client2.setSoTimeout(5);
			//OutputStream sout2 = client2.getOutputStream();
			//DataOutputStream out2 = new DataOutputStream(sout2);
			//out2.writeUTF(P2);
			//out2.writeUTF(fireP2);


		} catch (IOException e) {
			dispose();
			e.printStackTrace();
		}
*/


		WIDTH = 640;
		HEIGHT = 360;
		kX = (float) WIDTH / (float) GdxWidth;
		kY = (float) HEIGHT / (float) GdxHeight;
		//Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);         //РАЗМЕР ЭКРАНА ДЛЯ DESKTOP
		// System.out.println("gdxWidth = " + GdxWidth);
		//System.out.println("gdxHeight = " + GdxHeight);
		//System.out.println("Width = " + WIDTH);
		//System.out.println("Heigth  = " + HEIGHT);
		/* Set up the camera */
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		camera.setToOrtho(false, WIDTH, HEIGHT);
//        camera.zoom = 0.5f;
//        HEIGHT = HEIGHT/2;
//        WIDTH = WIDTH/2;

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
		player2 = new Player(spriteSheet, Level.PLAYER_START_POS, 8, 8, 8, 0, 3);
		player = new Player(spriteSheet, Level.PLAYER_START_POS2, 8, 8, 8, 0, 3);
		shootTimer = 0;
		shootTimer2 = 0;

		Thread connectionClientUp = new ConnetctionClientUp();
		connectionClientUp.start();
		Thread connectionClientDown = new ConnetctionClientDown();
		connectionClientDown.start();



		/*Thread connectionClientUp2 = new ConnetctionClientUp2();
		connectionClientUp2.start();
		Thread connectionClientDown2 = new ConnetctionClientDown2();
		connectionClientDown2.start();
		*/


		/* Change Input Processor */
		Gdx.input.setInputProcessor(new InputProcessor());
	}

	public void render(float a) {


		/*try {
			InputStream sin = client2.getInputStream();
			OutputStream sout = client2.getOutputStream();

			in2 = new DataInputStream(sin);
			out2 = new DataOutputStream(sout);
			if (timeClient > timeServer) {
				System.out.println("timeClient = " + timeClient);
				System.out.println("timeServer = " + timeServer);

				System.out.println(in2.readUTF());
			}

			if (timeServer == timeClient)
				out2.writeUTF("1");
		} catch (IOException e) {
		}*/
		timeClient++;
		/*if (queueTimeServer.size() != 0 & queueTimeServer.size() == moveP1queue.size()) {
			queueBuf = queueTimeServer;

			lol = Integer.valueOf(queueTimeServer.remove());
			if (lol <= timeClient) {
				P1 = moveP1queue.remove();
				fireP1 = fireP1queue.remove();
			}
			if (lol > timeClient) {
				queueTimeServer = queueBuf;
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}*/


		//System.out.println("GameScreen is render");
		/* Clear the screen */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Gdx.app.log("GameScreen FPS", (1/a) + "");

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
			P2 = "UP";
		} else if (GameKeys.isDown(GameKeys.RIGHT)) {
			player.setVelocity(Player.RIGHT);
			P2 = "RIGHT";
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
		} else if (P1.equals("UP")) {
			player2.setVelocity(Player.UP);
		} else if (P1.equals("RIGHT")) {
			player2.setVelocity(Player.RIGHT);
		} else if (P1.equals("DOWN")) {
			player2.setVelocity(Player.DOWN);
		} else {
			player2.setVelocity(Player.STOPPED);
		}

		if (lvlManager.getCurrentLevel().resolveCollisions(player2.getCollisionRect())) {
			player2.setVelocity(Player.STOPPED);
		}

		for (int i = 0; i < lvlManager.getCurrentLevel().getEnemiesList().size(); i++) {
			for (int j = 0; j < lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().size(); j++) {
				if (lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).getCollisionRect().overlaps(player.getCollisionRect())) {
					lvlManager.getCurrentLevel().getEnemiesList().get(i).getBullets().get(j).setAlive(false);
					//TODO: Add logic to remove player
				}
			}
		}

		if (GameKeys.isDown(GameKeys.SPACE)) {
			if (shootTimer > 30) {
				player.shoot(Bullet.BULLET_PLAYER);
				fireP2 = "FIRE";
				shootTimer = 0;
			}
		} else
			fireP2 = "NULL";

		if (fireP1.equals("FIRE")) {
			if (shootTimer2 > 30) {
				player2.shoot(Bullet.BULLET_PLAYER);
				shootTimer2 = 0;
			}
		}

		player.update(Gdx.graphics.getDeltaTime());
		player2.update(Gdx.graphics.getDeltaTime());
		//System.out.println("Player " + player.isAlive());
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

       /* try {
			out.writeUTF(P2);
            out.writeUTF(fireP2);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


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


	public static class ConnetctionClientDown extends Thread {

		GameScreenClient gameScreenClient;

		public ConnetctionClientDown() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					InputStream inputStream = client.getInputStream();
					//OutputStream outputStream = client.getOutputStream();

					in = new DataInputStream(inputStream);
					//out = new DataOutputStream(outputStream);

					
					/*moveP1queue.add(in.readUTF());
					fireP1queue.add(in.readUTF());
					queueTimeServer.add(in.readUTF());*/
					//System.out.println("TimeServer = " + timeServer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static class ConnetctionClientUp extends Thread {

		GameScreenClient gameScreenClient;

		public ConnetctionClientUp() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					//InputStream inputStream = client.getInputStream();
					OutputStream outputStream = client.getOutputStream();

					//in = new DataInputStream(inputStream);
					out = new DataOutputStream(outputStream);

					//P1 = in.readUTF();
					//fireP1 = in.readUTF();

					if (!P2.equals(P2buf) | !fireP2.equals(fireP2buf)) {
						//System.out.println("client send comand");
						out.writeUTF(P2);
						out.writeUTF(fireP2);
						out.writeUTF(String.valueOf(timeClient));
						P2buf = P2;
						fireP2buf = fireP2;
						P1buf = P1;
						fireP1buf = fireP1;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}


/*

	public static class ConnetctionClientDown2 extends Thread {

		GameScreenClient gameScreenClient;

		public ConnetctionClientDown2() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					InputStream inputStream = client2.getInputStream();
					//OutputStream outputStream = client.getOutputStream();

					in2 = new DataInputStream(inputStream);
					//out = new DataOutputStream(outputStream);

					P1 = in2.readUTF();
					fireP1 = in2.readUTF();

					//out.writeUTF(P2);
					//out.writeUTF(fireP2);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static class ConnetctionClientUp2 extends Thread {

		GameScreenClient gameScreenClient;

		public ConnetctionClientUp2() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					//InputStream inputStream = client.getInputStream();
					OutputStream outputStream = client2.getOutputStream();

					//in = new DataInputStream(inputStream);
					out2 = new DataOutputStream(outputStream);

					//P1 = in.readUTF();
					//fireP1 = in.readUTF();

					if (!P2.equals(P2buf) | !fireP2.equals(fireP2buf)) {
						//System.out.println("client send comand");
						out2.writeUTF(P2);
						out2.writeUTF(fireP2);
						P2buf = P2;
						fireP2buf = fireP2;
						P1buf = P1;
						fireP1buf = fireP1;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
*/


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