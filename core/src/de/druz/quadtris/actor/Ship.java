package de.druz.quadtris.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import de.druz.quadtris.Assets;
import de.druz.quadtris.Quadtris;
import de.druz.quadtris.utils.ArrayUtils;

public class Ship extends Actor {
	public static float SHIP_WIDTH = 300f;
	public static float SHIP_HEIGHT = 300f;

	// private static ShapeRenderer shapeRenderer;
	public Color color;
	public boolean[][] shape;
	public float alpha;

	public Ship(Color color, boolean[][] shape) {
		super();
		setWidth(SHIP_WIDTH);
		setHeight(SHIP_HEIGHT);
		this.color = color;
		this.shape = shape;
		this.setVisible(false);
	}

	public Ship(Color color, Color[][] map) {
		this(color, createBoolShape(color, map));
		for (int i = 1; i < 4; i++) {
			rotateLeft();
		}
		ArrayUtils.removeEmptyBottomRows(this.shape);
		
		if (MathUtils.randomBoolean()) {
			for (int i = 0; i < map.length; i++) {
				moveRight();
			}
		} else {
			for (int i = 0; i < map.length; i++) {
				moveLeft();
			}
		}
	}

	private static boolean[][] createBoolShape(Color color, Color[][] shape) {
		boolean[][] boolShape = new boolean[shape.length][shape[0].length];
		for (int x = 0; x < shape.length; x++) {
			for (int y = 0; y < shape[0].length; y++) {
				if (color.equals(shape[x][y])) {
					boolShape[x][y] = true;
				} else {
					boolShape[x][y] = false;
				}
			}
		}
		return boolShape;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
//		drawShape(batch);
		drawTexture(batch);
	}

	private void drawTexture(Batch batch) {
//		batch.begin();
//		batch.setColor(color);
		float blockHeight = getHeight() / shape[0].length;
		float blockWidth = getWidth() / shape.length;
		for (int x = 0; x < shape.length; x++) {
			for (int y = 0; y < shape[x].length; y++) {
				if (shape[x][y]) {
					batch.draw(Quadtris.colorBlocks.get(color), getX() + x * blockWidth,
							getY() + y * blockHeight, getWidth() / shape.length,
							getHeight() / shape[0].length);
					// Quadtris.shapeRenderer.rect(getWidth()/2-x*blockWidth-blockWidth/2,
					// getHeight()/2-y*blockHeight-blockHeight/2,
					// getWidth()/shape.length, getHeight()/shape[x].length);
					// float width = getWidth()/shape.length;
					// float height = getHeight()/shape[x].length;
					// Quadtris.shapeRenderer.rect(x*getWidth()/shape.length,
					// y*getHeight()/shape[x].length,
					// getOriginX(), getOriginY(), width, height, getScaleX(),
					// getScaleY(), getRotation());
				}
			}
		}
//		batch.end();
	}

	public void drawShape(Batch batch) {
		Quadtris.shapeRenderer.begin(ShapeType.Filled);
		Quadtris.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		Quadtris.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		// Quadtris.shapeRenderer.getTransformMatrix().translate(getWidth()/2,
		// getHeight()/2, 0);
		// Quadtris.shapeRenderer.getTransformMatrix().rotate(new Vector3(0, 0,
		// 1), getRotation());
		color.a = alpha;
		Quadtris.shapeRenderer.setColor(color);
		float blockHeight = getHeight() / shape[0].length;
		float blockWidth = getWidth() / shape.length;
		for (int x = 0; x < shape.length; x++) {
			for (int y = 0; y < shape[x].length; y++) {
				if (shape[x][y]) {
					Quadtris.shapeRenderer.rect(getX() + x * blockWidth,
							getY() + y * blockHeight, getWidth() / shape.length,
							getHeight() / shape[0].length);
					// Quadtris.shapeRenderer.rect(getWidth()/2-x*blockWidth-blockWidth/2,
					// getHeight()/2-y*blockHeight-blockHeight/2,
					// getWidth()/shape.length, getHeight()/shape[x].length);
					// float width = getWidth()/shape.length;
					// float height = getHeight()/shape[x].length;
					// Quadtris.shapeRenderer.rect(x*getWidth()/shape.length,
					// y*getHeight()/shape[x].length,
					// getOriginX(), getOriginY(), width, height, getScaleX(),
					// getScaleY(), getRotation());
				}
			}
		}
		Quadtris.shapeRenderer.end();
	}

	public void moveLeft() {
		// Gdx.app.log("event", "Trying to move ship left");
		if (canMoveLeft()) {
			for (int x = 0; x < shape.length - 1; x++) {
				for (int y = 0; y < shape.length; y++) {
					shape[x][y] = shape[x + 1][y];
				}
			}
			for (int y = 0; y < shape.length; y++) {
				shape[shape.length - 1][y] = false;
			}
			Assets.playSound(Assets.moveSound);
		} else {
			Assets.playSound(Assets.blockedSound);
		}
	}

	private boolean canMoveLeft() {
		for (int y = 0; y < shape[0].length; y++) {
			if (shape[0][y]) {
				return false;
			}
		}
		return true;
	}

	private boolean canMoveRight() {
		for (int y = 0; y < shape[shape.length - 1].length; y++) {
			if (shape[shape.length - 1][y]) {
				return false;
			}
		}
		return true;
	}

	public void moveRight() {
		// Gdx.app.log("event", "Trying to move ship right");
		if (canMoveRight()) {
			for (int x = shape.length - 1; x > 0; x--) {
				for (int y = 0; y < shape.length; y++) {
					shape[x][y] = shape[x - 1][y];
				}
			}
			for (int y = 0; y < shape.length; y++) {
				shape[0][y] = false;
			}	
			Assets.playSound(Assets.moveSound);
		} else {
			Assets.playSound(Assets.blockedSound);
		}
	}

	public void rotateLeft() {
		// rotateBy(90);
	    shape = ArrayUtils.rotateLeft(shape);
	    Assets.playSound(Assets.rotateSound);
	}

	public void rotateRight() {
		// rotateBy(-90);
	    shape = ArrayUtils.rotateRight(shape);
	    Assets.playSound(Assets.rotateSound);
    }

}
