package de.druz.quadtris;

import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.druz.quadtris.actor.LogoActor;
import de.druz.quadtris.actor.ShipView;

public class Quadtris extends ApplicationAdapter {
	SpriteBatch batch;
	public static ShapeRenderer shapeRenderer;
	public static Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.GRAY};
	public static String[] COLOR_NAMES = {"This part will fly in from the LEFT", 
		"This part will fly in from the TOP", 
		"This part will fly in from the RIGHT", 
		"This part will fly in from the BOTTOM"};
	public static HashMap<Color, TextureRegion> colorBlocks = new HashMap<Color, TextureRegion>();
	public static int startSize = 3;
	public static int size = startSize;
	public static int score = 0;
	public static boolean roundDone = false;
	public static boolean showIntro = true;
	public static Label label;
	public static LogoActor logo;


	private Stage stage;
	private ExtendViewport viewport;
	private Skin skin;
	
	
	@Override
	public void create () {
		Assets.load();
		colorBlocks.put(COLORS[0], Assets.red);
		colorBlocks.put(COLORS[1], Assets.blue);
		colorBlocks.put(COLORS[2], Assets.green);
		colorBlocks.put(COLORS[3], Assets.gray);
		
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		stage = new Stage();
//		Camera cam = new OrthographicCamera(120, 120);
		viewport = new ExtendViewport(450, 450, stage.getCamera());
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage.setViewport(viewport);
		
		ShipView shipHolder = new ShipView();
		shipHolder.addListener(new InputListener() {


			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				Gdx.app.log("input", "Keydown " + keycode);
				ShipView shipView = (ShipView)event.getListenerActor();

				if (showIntro) {
					showIntro = false;
					logo.remove();
					return true;
				} else if (roundDone) {
					roundDone = false;
					shipView.generateShips(size);
					return true;
				} else {
					if (keycode == Keys.LEFT) {
						shipView.moveShipLeft();
						return true;
					} else if (keycode == Keys.RIGHT) {
						shipView.moveShipRight();
						return true;
					} else if (keycode == Keys.UP) {
						shipView.rotateShipRight();
						return true;
					} else if (keycode == Keys.DOWN) {
						shipView.rotateShipLeft();
						return true;
					} else if (keycode == Keys.SPACE) {
						shipView.nextShip();
						Color color = shipView.getActiveShip().color;
						label.setText(COLOR_NAMES[Arrays.asList(COLORS).indexOf(color)]);
						return true;
					} else if (keycode == Keys.X) {
						shipView.showAllShips();
						return true;
					} else if (keycode == Keys.Y) {
						shipView.showOneShip();
						return true;
					} else if (keycode == Keys.N) {
						shipView.generateShips(size);
						return true;
					} else if (keycode == Keys.ENTER) {
						shipView.showAllShips();
						shipView.connect();
						return true;
					}
				}
				return false;
			}
			
		});
		shipHolder.generateShips(size);
		label = new Label(COLOR_NAMES[0], skin);

		Table root = new Table(skin);
		root.setFillParent(true);
		root.setBackground(skin.getDrawable("default-pane"));
//		root.debug().defaults().space(6);
//		root.add(new TextButton("<", skin));
		root.add(new Label("Arrow keys to move and rotate, \n"
				+ "Space key to change worlds, \n"
				+ "Enter when ready", skin)).align(Align.center).colspan(3).row();
//		root.add(new TextButton(">", skin)).row();
		root.add(shipHolder).padTop(10).padBottom(10).colspan(3).row();
		root.add(label).colspan(3);
		stage.addActor(root);
		stage.setKeyboardFocus(shipHolder);


		logo = new LogoActor();
		Table logoRoot = new Table(skin);
		logoRoot.setFillParent(true);
		logoRoot.setBackground(skin.getDrawable("default-pane"));
		logoRoot.add(logo);
		stage.addActor(logoRoot);
		
//		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(stage);
		
		Assets.soundOn = true;
		Assets.music.play();
//		inputMultiplexer.addProcessor(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	@Override
	public void render () {
		stage.act();
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
		
//		batch.begin();
//		batch.end();

//		shapeRenderer.begin(ShapeType.Line);
//		Vector3 tempBounds = guiCam.project(new Vector3(startBounds.x, startBounds.y, 0f));
//		
//		shapeRenderer.box(tempBounds.x, tempBounds.y, 0f, startBounds.width*factor, startBounds.height*factor, 0f);
//		shapeRenderer.end();
	}
}
