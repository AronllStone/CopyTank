package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import main.Main;

public class Menu implements Screen{

	private int Width, Height;
	Stage stage;
	Stage hello;
	Skin skin;
	Main main;
	OrthographicCamera camera;
	//private Table table, table2;

	public Menu(Main gameScreen){
		this.main = gameScreen;
		stage = new Stage();
		skin = new Skin();
		hello = new Stage();
/*
		Label nameLabel = new Label("Name:", skin);
		TextField nameText = new TextField("TText", skin);
		Label addressLabel = new Label("Address:", skin);
		TextField addressText = new TextField("loo",skin);

		Table table = new Table();
		table.add(nameLabel);              // Row 0, column 0.
		table.add(nameText).width(100);    // Row 0, column 1.
		table.row();                       // Move to next row.
		table.add(addressLabel);           // Row 1, column 0.
		table.add(addressText).width(100); // Row 1, column 1.
		   stage.addActor(table);*/
		Gdx.input.setInputProcessor(stage);

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		skin.add("default", new BitmapFont());

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.CLEAR);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

	    Height = Gdx.graphics.getHeight();
		Width = Gdx.graphics.getWidth();

		Table table = new Table();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Width, Height);
		System.out.println("GDX X = " + Gdx.graphics.getWidth() + " Y = " + Gdx.graphics.getHeight());
		table.setPosition(Width/2,Height/2);

		/*ImageButton imageButton = new ImageButton(skin.newDrawable("white", Color.CYAN));
//		imageButton.add("Helo");
		imageButton.setColor(Color.BLUE);
		imageButton.setPosition(5,5);

		stage.addActor(imageButton);*/
		stage.addActor(table);
		/*final TextButton StartText = new TextButton("Hello, This my first menu in this game\n", skin);
		StartText.setTouchable(Touchable.disabled);
		StartText.setPosition(Gdx.graphics.getWidth()*5/16, Gdx.graphics.getHeight()*15/16);*/


		final TextButton button = new TextButton("Server Game", skin);
		//button.setSize(100,100);
		table.add(button).height(100);
		table.getCell(button).width(100);
		table.row();
		final TextButton button2 = new TextButton("Client Game", skin);
		table.add(button2).height(100);
		table.getCell(button2).width(100);

		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new GameScreenServer());
			}
		});

		button2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				//Gdx.net.openURI("http://vk.com");
				main.setScreen(new GameScreenClient());
				//System.out.println("Button2 pressed");/*main.setScreen(new GameScreen());*/
			}
		});

		//table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

		//stage.act(Math.min(delta, 1 / 30f));
		//Table.drawDebug(stage);



	}

	@Override
	public void resize(int width, int height) {

		this.Width= Width;
		this.Height = Height;

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		//hello.dispose();
		main.dispose();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		camera.update();
		//hello.draw();
		stage.draw();
		main.batch.setProjectionMatrix(camera.combined);
		main.batch.begin();
		//main.font.draw(main.batch, "Hello my Dear Friend! Welcome to my Tanks Game", Gdx.graphics.getWidth()*5/16, Gdx.graphics.getHeight()*15/16);
		//main.font.draw(main.batch, "Tap anywhere to begin!", 100, 100);

		main.batch.end();

		/*if (Gdx.input.getX() == 100 & Gdx.input.getY() == 150 ){
			this.main.setScreen(new GameScreen());
			dispose();
		}*/

	}

}