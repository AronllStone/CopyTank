package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import main.Main;

public class Multiplayer implements Screen {

	private int Width, Height;
	Stage stage;
	Stage hello;
	Skin skin;
	Main main;
	OrthographicCamera camera;
	Texture bgTexture;
	//private Table table, table2;

	public Multiplayer(Main gameScreen) {
		this.main = gameScreen;
		stage = new Stage();
		skin = new Skin();
		hello = new Stage();

		bgTexture = new Texture(Gdx.files.internal("background.png"));
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

		skin.add("default", Menu.getFont());

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
		table.setPosition(Menu.ScaleWidth(50), Menu.ScaleHeight(50));
		stage.addActor(table);
		final TextButton button2 = new TextButton("Server Game", skin);
		table.add(button2).height(100);
		table.getCell(button2).width(Menu.ScaleWidth(60));
		table.row();
		table.row();
		table.row();

		final TextButton button3 = new TextButton("Client Game", skin);
		table.add(button3).height(100);
		table.getCell(button3).width(Menu.ScaleWidth(60));
		table.row();
		table.row();
		table.row();

		final TextButton button4 = new TextButton("Back", skin);
		table.add(button4).height(100);
		table.getCell(button4).width(Menu.ScaleWidth(60));

		button2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new Connecting(main, 2));
			}
		});

		button3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new Connecting(main, 3));
			}
		});

		button4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new Menu(main));
			}
		});

		//table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

		//stage.act(Math.min(delta, 1 / 30f));
		//Table.drawDebug(stage);


	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		camera.update();
		//hello.draw();

		main.batch.setProjectionMatrix(camera.combined);
		main.batch.begin();
		main.batch.draw(bgTexture, 0, 0, Width, Height);
		//main.font.draw(main.batch, "Hello my Dear Friend! Welcome to my Tanks Game", Gdx.graphics.getWidth()*5/16, Gdx.graphics.getHeight()*15/16);
		//main.font.draw(main.batch, "Tap anywhere to begin!", 100, 100);

		main.batch.end();
		stage.draw();

	}

	@Override
	public void resize(int width, int height) {

		this.Width = Width;
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
		//main.batch.dispose();
	}

	@Override
	public void show() {

	}



}