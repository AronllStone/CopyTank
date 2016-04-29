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
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class GameScreenServer extends ApplicationAdapter implements Screen {

	public static int WIDTH;
	public static int HEIGHT;
	public static int GdxWidth;
	public static int GdxHeight;
	public static float kX;
	public static float kY;

	public static java.net.ServerSocket serverSocket;
	public static java.net.ServerSocket serverSocket2;
	public static Socket client;
	public static Socket client2;
	public static int ClientCount = 0;
	public static int ClientCount2 = 0;


	public static DataInputStream in;
	public static DataOutputStream out;
	public static DataInputStream in2;
	public static DataOutputStream out2;


	public static String P1 = "STOP";
	public static String fireP1 = "NULL";
	public static String P2 = "STOP";

	public static String fireP2 = "NULL";
	public static String P1buf = "STOP";
	public static String fireP1buf = "NULL";
	public static String P2buf = "STOP";
	public static String fireP2buf = "NULL";

	public static int timeServer = 0;
	public static int timeClient = 0;

	public static Queue<String> queueTimeClient = new LinkedList<String>();
	public static Queue<String> moveP2queue = new LinkedList<String>();
	public static Queue<String> fireP2queue = new LinkedList<String>();
	public static Queue<String> queueBuff = new LinkedList<String>();


	public static int lol;

	ShapeRenderer sr;


	public static OrthographicCamera camera;

	Texture spriteSheet;
	Texture arrowUp;
	Texture arrowDown;
	Texture arrowLeft;
	Texture arrowRight;

	SpriteBatch batch;

	public static Player player;
	public static Player player2;


	LevelManager lvlManager;
	BitmapFont font;

	int shootTimer;
	int shootTimer2;
	Main main;

	public GameScreenServer() {

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

		try {
			serverSocket = new java.net.ServerSocket(1525);
		} catch (IOException e) {
			System.out.println("Cant create socket");
			e.printStackTrace();
		}

		/*try {
			serverSocket2 = new java.net.ServerSocket(1526);
		} catch (IOException e) {
			System.out.println("Cant create socket");
			e.printStackTrace();
		}*/


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
		lvlManager = new LevelManager(spriteSheet, 2);
		player = new Player(spriteSheet, Level.PLAYER_START_POS, 8, 8, 8, 0, 2);
		player2 = new Player(spriteSheet, Level.PLAYER_START_POS2, 8, 8, 8, 0, 2);
		shootTimer = 0;
		shootTimer2 = 0;

		try {
			DatagramSocket datagramSocket = new DatagramSocket(1520);
			byte[] sendData = new byte[1024];
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			//datagramSocket.setSoTimeout(20);
			datagramSocket.receive(receivePacket);
			System.out.println("SERVER IN RECV");
			if (receivePacket != null) {
				System.out.println("			RECV YES");
				InetAddress IPAddress = receivePacket.getAddress();
				int port = receivePacket.getPort();
				sendData = "Ready".getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				datagramSocket.send(sendPacket);
			}
			System.out.println("SERVER OUT RECV");
			datagramSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			if (ClientCount == 0) {
				client = serverSocket.accept();
				ClientCount++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*try {
			if (ClientCount2 == 0) {
				client2 = serverSocket2.accept();
				client2.setSoTimeout(5);
				ClientCount2++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}*/

		Thread connectionServerUp = new ConnetctionServerUp();
		connectionServerUp.start();
		Thread connectionServerDown = new ConnetctionServerDown();
		connectionServerDown.start();

		/*Thread connectionServerUp2 = new ConnetctionServerUp2();
		connectionServerUp2.start();
		Thread connectionServerDown2 = new ConnetctionServerDown2();
		connectionServerDown2.start();*/


		/* Change Input Processor */
		Gdx.input.setInputProcessor(new InputProcessor());
	}

	public void render(float a) {

		//System.out.println("GameScreen is render");
		/* Clear the screen */

		//System.out.println("Waiting.........");
		timeServer++;
		/*if (queueTimeClient.size() != 0 & queueTimeClient.size() == moveP2queue.size()) {
			queueBuff = queueTimeClient;
			lol = Integer.valueOf(queueTimeClient.remove());
			if (lol <= timeServer) {
				P2 = moveP2queue.remove();
				fireP2 = fireP2queue.remove();
			}
			if (lol > timeServer) {
				queueTimeClient = queueBuff;
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}*/
		/*try {
			InputStream inputStream = client2.getInputStream();
			OutputStream outputStream = client2.getOutputStream();

			in2 = new DataInputStream(inputStream);
			out2 = new DataOutputStream(outputStream);

			if (timeClient < timeServer) {
				System.out.println("timeClient = " + timeClient);
				System.out.println("timeServer = " + timeServer);
				System.out.println(in2.readUTF());
			}
			if (timeClient == timeServer)
				out2.writeUTF("1");

//            P2 = in.readUTF();
//            fireP2 = in.readUTF();

//            String line = in.readUTF();                                 //TODO: Принятие данных от клиента
//            System.out.println("Client say = " + line);
//
//            out.writeUTF("");                                           //TODO: Отправка данных клиенту
//            System.out.println("Text message send");

		} catch (IOException e) {
			e.printStackTrace();
		}*/


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
			P1 = "LEFT";
		} else if (GameKeys.isDown(GameKeys.UP)) {
			player.setVelocity(Player.UP);
			P1 = "UP";
		} else if (GameKeys.isDown(GameKeys.RIGHT)) {
			player.setVelocity(Player.RIGHT);
			P1 = "RIGHT";
		} else if (GameKeys.isDown(GameKeys.DOWN)) {
			player.setVelocity(Player.DOWN);
			P1 = "DOWN";
		} else {
			player.setVelocity(Player.STOPPED);
			P1 = "STOP";
		}

		if (lvlManager.getCurrentLevel().resolveCollisions(player.getCollisionRect())) {
			player.setVelocity(Player.STOPPED);
		}

		if (P2.equals("LEFT")) {
			player2.setVelocity(Player.LEFT);
		} else if (P2.equals("UP")) {
			player2.setVelocity(Player.UP);
		} else if (P2.equals("RIGHT")) {
			player2.setVelocity(Player.RIGHT);
		} else if (P2.equals("DOWN")) {
			player2.setVelocity(Player.DOWN);
		} else if (P2.equals("STOP")) {
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
				fireP1 = "FIRE";
				shootTimer = 0;
			}
		} else
			fireP1 = "NULL";

		if (fireP2.equals("FIRE")) {
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




       /* *//*TODO ОТПРАВКА ДАННЫХ НА КЛИЕНТ*//*
		{


            try {
                out.writeUTF(P1);
                out.writeUTF(fireP1);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        *//*TODO ОТПРАВКА ДАННЫХ НА КЛИЕНТ*/

		P2buf = P2;
		fireP2buf = fireP2;
		P1buf = P1;
		fireP1buf = fireP1;

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


	public static class ConnetctionServerDown extends Thread {

		GameScreenServer gameScreenServer;

		public ConnetctionServerDown() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					InputStream inputStream = client.getInputStream();
					//OutputStream outputStream = client.getOutputStream();

					in = new DataInputStream(inputStream);
					//out = new DataOutputStream(outputStream);

					moveP2queue.add(in.readUTF());
					fireP2queue.add(in.readUTF());
					queueTimeClient.add(in.readUTF());

					//System.out.println("timeClient = " + timeClient);

					//out.writeUTF(P1);
					//out.writeUTF(fireP1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static class ConnetctionServerUp extends Thread {

		GameScreenServer gameScreenServer;

		public ConnetctionServerUp() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					//InputStream inputStream = client.getInputStream();
					OutputStream outputStream = client.getOutputStream();

					//in = new DataInputStream(inputStream);
					out = new DataOutputStream(outputStream);

					//P2 = in.readUTF();
					//fireP2 = in.readUTF();

					if (!P1.equals(P1buf) | !fireP1.equals(fireP1buf)) {
						//System.out.println("Server send comand");
						out.writeUTF(P1);
						out.writeUTF(fireP1);
						out.writeUTF(String.valueOf(timeServer));
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





	/*public static class ConnetctionServerDown2 extends Thread {

		GameScreenServer gameScreenServer;

		public ConnetctionServerDown2() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					InputStream inputStream = client2.getInputStream();
					//OutputStream outputStream = client.getOutputStream();

					in2 = new DataInputStream(inputStream);
					//out = new DataOutputStream(outputStream);

					P2 = in2.readUTF();
					fireP2 = in2.readUTF();

					//out.writeUTF(P1);
					//out.writeUTF(fireP1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	public static class ConnetctionServerUp2 extends Thread {

		GameScreenServer gameScreenServer;

		public ConnetctionServerUp2() {
		}

		@Override
		public void run() {

			try {
				while (true) {

					//InputStream inputStream = client.getInputStream();
					OutputStream outputStream = client2.getOutputStream();

					//in = new DataInputStream(inputStream);
					out2 = new DataOutputStream(outputStream);

					//P2 = in.readUTF();
					//fireP2 = in.readUTF();

					if (!P1.equals(P1buf) | !fireP1.equals(fireP1buf)) {
						//System.out.println("Server send comand");
						out2.writeUTF(P1);
						out2.writeUTF(fireP1);
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