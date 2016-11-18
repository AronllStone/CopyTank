package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import main.Main;

public class Menu implements Screen {

	private int Width, Height;
	public static BitmapFont font;
	Stage stage;
	Stage hello;
	Skin skin;
	Main main;
	OrthographicCamera camera;
	Texture bgTexture;
	Texture buttonTexture;
	Table table;

	//private Table table, table2;
	public static int ScaleWidth(int scale) {
		int x = Gdx.graphics.getWidth() * scale / 100;
		return x;
	}
	public static int ScaleHeight(int scale) {
		int x = Gdx.graphics.getHeight() * scale / 100;
		return x;
	}

	public static BitmapFont getFont(){
		return font;
	}

	public Menu(Main gameScreen) {
		this.main = gameScreen;
		stage = new Stage();
		skin = new Skin();
		hello = new Stage();


		bgTexture = new Texture(Gdx.files.internal("background.png"));
//		buttonTexture = new Texture(Gdx.files.internal("ship.png"));

		Gdx.input.setInputProcessor(stage);

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA4444);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
//		skin.add("Lol", buttonTexture);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("text.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = Gdx.graphics.getHeight() / 25;
		font = generator.generateFont(param);

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.CLEAR);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = font;
		skin.add("default", textButtonStyle);

		Height = Gdx.graphics.getHeight();
		Width = Gdx.graphics.getWidth();

		table = new Table();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Width, Height);
		table.setPosition(ScaleWidth(50), ScaleHeight(50));
		stage.addActor(table);

		final TextButton button1 = new TextButton("Single Player", skin);
		table.add(button1).height(100);
		table.getCell(button1).width(ScaleWidth(60));
		table.row();
		table.row();
		table.row();

		final TextButton button2 = new TextButton("Multiplayer", skin);
		table.add(button2).height(100);
		table.getCell(button2).width(ScaleWidth(60));
		table.row();
		table.row();
		table.row();

		final TextButton button3 = new TextButton("HELP", skin);
		table.add(button3).height(100);
		table.getCell(button3).width(ScaleWidth(60));
		table.row();


		button1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new GameScreen(main));
			}
		});

		button2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new Multiplayer(main));
			}
		});

		button3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
				main.setScreen(new Help(main));
			}
		});
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
		main.dispose();
	}

	@Override
	public void show() {

	}


}