package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import main.Main;

public class Help implements Screen {

	private int Width, Height;
	Stage stage;
	Skin skin;
	Main main;
	OrthographicCamera camera;
	Texture bgTexture;
	Texture help2Texture;
	Texture buttonTexture;
	Table table;
	int help = 0;
	//private Table table, table2;

	public Help(Main gameScreen) {
		this.main = gameScreen;
		stage = new Stage();
		skin = new Skin();


		bgTexture = new Texture(Gdx.files.internal("Help.jpg"));
		help2Texture = new Texture(Gdx.files.internal("Movements.png"));
		buttonTexture = new Texture(Gdx.files.internal("Next.png"));
		Gdx.input.setInputProcessor(stage);

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		skin.add("Lol", new Texture(pixmap));

		skin.add("default", new BitmapFont());

		TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("Lol");
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("Lol");
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		Height = Gdx.graphics.getHeight();
		Width = Gdx.graphics.getWidth();

		table = new Table();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Width, Height);
		table.setPosition(Menu.ScaleWidth(90), Menu.ScaleHeight(90));

		stage.addActor(table);
		final TextButton button1 = new TextButton("", skin);
		table.add(button1);
		table.getCell(button1).width(Menu.ScaleWidth(10));
		table.getCell(button1).height(Menu.ScaleHeight(10));
		table.row();

//		button1.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent changeEvent, Actor actor) {
//				System.out.println("Help = " + help);
//				if (help == 1)
//					help = 2;
//				else if (help == 2)
//					main.setScreen(new Menu(main));
//			}
//		});

		//table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

		//stage.act(Math.min(delta, 1 / 30f));
		//Table.drawDebug(stage);

		help = 1;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		camera.update();
		//hello.draw();
		main.batch.setProjectionMatrix(camera.combined);
		main.batch.begin();
		if (help == 1)
			main.batch.draw(bgTexture, 0, 0, Width, Height);
		if (help == 2)
			main.batch.draw(help2Texture, 0, 0, Width, Height);
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