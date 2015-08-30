package de.druz.quadtris.actor;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import de.druz.quadtris.Assets;
import de.druz.quadtris.Quadtris;
import de.druz.quadtris.random.ColorMapBuilder;
import de.druz.quadtris.utils.ArrayUtils;

public class ShipView extends WidgetGroup {

	private Ship activeShip;
	private Color[][] shape;
	private ColorMapBuilder colorMapBuilder = new ColorMapBuilder();
	public static boolean collided = false;

	@Override
	public void addActor(Actor actor) {
		super.addActor(actor);
		if (activeShip == null && actor instanceof Ship) {
			activeShip = (Ship)actor;
			activeShip.setVisible(true);
		}
	}

	@Override
	public float getPrefWidth() {
		return Ship.SHIP_WIDTH;
	}


	@Override
	public float getPrefHeight() {
		return Ship.SHIP_HEIGHT;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();

		Quadtris.shapeRenderer.begin(ShapeType.Filled);
		Quadtris.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		Quadtris.shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
		// Quadtris.shapeRenderer.getTransformMatrix().translate(getWidth()/2,
		// getHeight()/2, 0);
		// Quadtris.shapeRenderer.getTransformMatrix().rotate(new Vector3(0, 0,
		// 1), getRotation());
//		color.a = alpha;
		Quadtris.shapeRenderer.setColor(new Color(0.2f, 0.2f, 0.2f, 1f));
		Quadtris.shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
		Quadtris.shapeRenderer.end();
		
		batch.begin();
		super.draw(batch, parentAlpha);
	}


	public void setActiveShip(Ship activeShip) {
		this.activeShip.setVisible(false);
		this.activeShip = activeShip;
		activeShip.setVisible(true);
	}

	public Ship getActiveShip() {
		return activeShip;
	}

	public void moveShipLeft() {
		activeShip.moveLeft();
	}

	public void moveShipRight() {
		activeShip.moveRight();
	}

	public void rotateShipLeft() {
		activeShip.rotateLeft();
		ArrayUtils.removeEmptyBottomRows(activeShip.shape);
	}

	public void rotateShipRight() {
		activeShip.rotateRight();
		ArrayUtils.removeEmptyBottomRows(activeShip.shape);
	}

	@Override
	protected void drawChildren(Batch batch, float parentAlpha) {
//		activeShip.draw(batch, parentAlpha);
		super.drawChildren(batch, parentAlpha);
	}

	public void nextShip() {
		Actor prev = null;
		for (Actor actor : getChildren()) {
			if (actor == activeShip) {
				if (prev == null) {
					setActiveShip((Ship)getChildren().get(getChildren().size-1));
					return;
				} else {
					setActiveShip((Ship)prev);
					return;
				}
			}
			prev = actor;
		}
		Assets.playSound(Assets.switchSound);
	}

	public void showAllShips() {
		for (Actor actor : getChildren()) {
			actor.setVisible(true);
		}
	}

	public void showOneShip() {
		for (Actor actor : getChildren()) {
			actor.setVisible(false);
		}
		activeShip.setVisible(true);
	}

	public void generateShips(int size) {
		// remove old ships
		activeShip = null;
		if (getChildren() != null && getChildren().size > 0) {
			Actor[] tempActors = getChildren().toArray();
			for (int i = 0; i < tempActors.length; i++) {
				removeActor(tempActors[i]);
			}
		}
		
//		shape = generateColorMap(size);
//		shape = generateOnePath(size);
//		shape = generateShape(size);
		shape = colorMapBuilder.build(size);
		for (int i = 0; i < Quadtris.COLORS.length; i++) {
			addActor(new Ship(Quadtris.COLORS[i], shape));
		}
	}

	private Color[][] generateColorMap(int size) {
		Color[][] result = new Color[size][size];

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				result[x][y] = Quadtris.COLORS[MathUtils.random(0, 3)];					
			}	
		}

		boolean noMoreIsolated = true;
//		do {
			noMoreIsolated = solveIsolated(result);
//		} while (!noMoreIsolated);
		return result;
	}

	public boolean solveIsolated(Color[][] result) {
		boolean noMoreIsolated = true;
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result[0].length; y++) {
				if (isolated(result, x, y)) {
					noMoreIsolated = false;
					result[x][y] = bestFittingColor(result, x, y);
				}
			}	
		}
		return noMoreIsolated;
	}

	private Color bestFittingColor(Color[][] result, int x, int y) {
//		if (x < (result.length-1)/2) {
//			if (y < (result[0].length-1)/2) {
//				return Quadtris.COLORS[0];
//			} else if (x > (result[0].length-1)/2) {
//				return Quadtris.COLORS[1];
//			}
//		} else if (x > (result.length-1)/2) {
//			if (y < (result[0].length-1)/2) {
//				return Quadtris.COLORS[2];
//			} else if (x > (result[0].length-1)/2) {
//				return Quadtris.COLORS[3];
//			}
//		}
//		return Quadtris.COLORS[MathUtils.random(0, Quadtris.COLORS.length-1)];
		int deltaX = 0;
		int deltaY = 0;
		if (MathUtils.randomBoolean()) {
			if (x > 0 && x < result.length-2) {
				deltaX = MathUtils.randomBoolean()? 1 : -1;
			} else {
				if (x == result.length-1) {
					deltaX = -1;
				} else {
					deltaX = 1;
				}
			}
		} else {
			if (y > 0 && y < result[0].length-2) {
				deltaY = MathUtils.randomBoolean()? 1 : -1;
			} else {
				if (y == result[0].length-1) {
					deltaY = -1;
				} else {
					deltaY = 1;
				}
			}
		}
		return result[x+deltaX][y+deltaY];
	}

	private boolean isolated(Color[][] result, int x, int y) {
		Color color = result[x][y];
		if (x > 0 && result[x-1][y].equals(color)) {
			return false;
		} else if (x < result.length-2 && result[x+1][y].equals(color)) {
			return false;
		} else if (y > 0 && result[x][y-1].equals(color)) {
			return false;
		} else if (y < result[0].length-2 && result[x][y+1].equals(color)) {
			return false;
		}
		return true;
	}

	private Color[][] generateShape(int size) {
		Color[][] result = new Color[size][size];
		
		Color[] tempColors = new Color[Quadtris.COLORS.length];
		List<Color> tempColorsList = Arrays.asList(tempColors);
		for (int i = 0; i < Quadtris.COLORS.length; i++) {
			Color newColor = null;
			do {
				newColor = Quadtris.COLORS[MathUtils.random(0, Quadtris.COLORS.length-1)];
				if(tempColorsList.contains(newColor)) {
					newColor = null;
				} else {
					tempColors[i] = newColor;
				}
			} while (newColor == null);
		}
		tempColors = (Color[])tempColorsList.toArray();
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				if (x < size/2) {
					result[x][y] = tempColors[0];					
				} else {
					result[x][y] = tempColors[1];
				}
			}	
		}
		createPath(result, tempColors[2]);
		ArrayUtils.flip(result);
		createPath(result, tempColors[3]);
		
		return result;
	}

	private Color[][] generateOnePath(int size) {
		Color[][] result = new Color[size][size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				result[x][y] = Quadtris.COLORS[3];					
			}	
		}
		createPath(result, Quadtris.COLORS[0]);
		result = ArrayUtils.rotateLeft(result);
		createPath(result, Quadtris.COLORS[1]);
		result = ArrayUtils.rotateLeft(result);
		createPath(result, Quadtris.COLORS[2]);
		
		return result;
	}

	private void createPath(Color[][] result, Color color) {
		int startX = MathUtils.random((result.length-1)/5, (result.length-1)*4/5);
		int startY = MathUtils.random((result[0].length-1)/5, (result[0].length-1)*4/5);
		
		Vector2 v2 = new Vector2(0, -1);
		int x = startX;
		int y = startY;
		while (y >= 0) {
			if (x < 0 || x > result.length-1 || y < 0 || y > result[0].length-1) {
				x = startX;
				y = startY;
			} else {
				Gdx.app.log("debug", "coloring " + x + ", " + y);
				result[x][y] = color;
				x = x+Math.round(v2.x);
				y = y+Math.round(v2.y);
			}
			if (MathUtils.randomBoolean(0.3333f)) {
				// keep straight
			} else {
				v2.rotate90(MathUtils.randomBoolean()? -1:1);
			}
			if (Math.round(v2.x) != 0 && Math.round(v2.y) != 0) {
				Gdx.app.log("error", "x and y != 0 WTF " + v2.toString());
			}
		}
		Gdx.app.log("debug", "done with path at " + x + ", " + y);
	}

	public void connect() {
		boolean[][] combined = new boolean[activeShip.shape.length][activeShip.shape[0].length];
		for (Actor actor : getChildren()) {
			Ship ship = (Ship)actor;

			if(Quadtris.COLORS[2].equals(ship.color)) {ship.rotateRight();}
			if(Quadtris.COLORS[1].equals(ship.color)) {ship.rotateLeft();ship.rotateLeft();}
			if(Quadtris.COLORS[0].equals(ship.color)) {ship.rotateLeft();}
			
			collided = collided || combine(combined, ship.shape);
			
			Action appearAction = new Action() {
				float duration = 2000;
				float deltaSum = 0;
				@Override
				public boolean act(float delta) {
					deltaSum += delta;
					Ship ship = (Ship)actor;
					ship.alpha = deltaSum/duration;
					return deltaSum > duration;
				}
			};
			MoveByAction moveAction = new MoveByAction();
			
			if(Quadtris.COLORS[3].equals(ship.color)) {actor.moveBy(0, -400); moveAction.setAmount(0, 400); moveAction.setDuration(1f);}
			if(Quadtris.COLORS[2].equals(ship.color)) {actor.moveBy(400, 0);  moveAction.setAmount(-400, 0); moveAction.setDuration(2f);}
			if(Quadtris.COLORS[1].equals(ship.color)) {actor.moveBy(0, 400);  moveAction.setAmount(0, -400); moveAction.setDuration(3f);}
			if(Quadtris.COLORS[0].equals(ship.color)) {actor.moveBy(-400, 0); moveAction.setAmount(400, 0); moveAction.setDuration(4f);}
			
			actor.addAction(new SequenceAction(moveAction, new RemoveAction()));
		}
		if (collided) {
			Quadtris.label.setText("COLLISION!\nYour score: " + Quadtris.score);
			Quadtris.score = 0;
			Quadtris.size = Quadtris.startSize;
			Quadtris.roundDone = true;
			collided = false;

			Assets.playSound(Assets.collideSound);
		} else {
			Quadtris.label.setText("CONNECTED!");
			Quadtris.score++;
			Quadtris.size++;
			Quadtris.roundDone = true;

			Assets.playSound(Assets.connectSound);
		}
	}

	private boolean combine(boolean[][] combined, boolean[][] shape) {
		boolean collision = false;
		for (int x = 0; x < shape.length; x++) {
			for (int y = 0; y < shape[0].length; y++) {
				if (shape[x][y]) {
					if (combined[x][y]) {
						collision  = true;
					} else {
						combined[x][y] = true;
					}
				}
			}
		}
		return collision;
	}

}
