package de.druz.quadtris.random;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.druz.quadtris.Quadtris;

public class ColorMapBuilder {

	private class ColorHistory {
		public Color color;
		public ArrayList<Vector2> history = new ArrayList<Vector2>();
		public Vector2 preferedPoint;
		public boolean done = false;
		
		public ColorHistory(Color color) {
			super();
			this.color = color;
		}
		
	}
	
	public Color[][] build(int size) {
		Color[][] result = new Color[size][size];
		ColorHistory[] colorHistories = new ColorHistory[Quadtris.COLORS.length]; 
		for (int i = 0; i < Quadtris.COLORS.length; i++) {
			colorHistories[i] = startColorHistory(result, Quadtris.COLORS[i]);
		}
		
		int numberDone = 0;
		while (numberDone < colorHistories.length) {
			numberDone = 0;
			for (int i = 0; i < colorHistories.length; i++) {
				if (colorHistories[i].done) {
					numberDone++;
				} else {
					searchFreeCell(result, colorHistories[i]);
				}
			}
		}
		return result;
	}

	private void searchFreeCell(Color[][] map, ColorHistory colorHistory) {
		boolean keepGoing = true;
		do {
			if (colorHistory.history == null || colorHistory.history.size() < 1) {
				keepGoing = false;
				colorHistory.done = true;
			} else {
				Vector2 pos = colorHistory.history.get(colorHistory.history.size()-1);
				float deltaX = colorHistory.preferedPoint.x - pos.x;
				float deltaY = colorHistory.preferedPoint.y - pos.y;
				Vector2 move = new Vector2(deltaX, deltaY); 
				if (move.len() < 1) {
					// we are at preferedPoint
					int minus1Or1 = MathUtils.randomBoolean()? -1 : 1; 
					if (MathUtils.randomBoolean()) {
						move.x = minus1Or1;
						move.y = 0f;
					} else {
						move.x = 0f;
						move.y = minus1Or1;
					}
				} else {
					if (Math.abs(deltaX) > Math.abs(deltaY)) {
						move.y = 0f;
					} else {
						move.x = 0f;
					}
					move.nor();
				}

				int rotateDir = MathUtils.randomBoolean()? -1 : 1;
				int numberOfRotations = 0;
				boolean pointWasSet = false;
				do {
					pointWasSet = trySetPoint(map, colorHistory, pos, move);
					move.rotate90(rotateDir);
					numberOfRotations++;
				} while (!pointWasSet  && numberOfRotations < 4);

				if (pointWasSet) {
					// point was set
					keepGoing = false;
				} else {
					// no more free cells around current pos, delete it
					colorHistory.history.remove(colorHistory.history.size()-1);
				}
			}
		} while (keepGoing);
	}

	private boolean trySetPoint(Color[][] map, ColorHistory colorHistory,
			Vector2 pos, Vector2 move) {
		int x = Math.round(pos.x + move.x);
		int y = Math.round(pos.y + move.y);
		if (x >= 0 && x < map.length && 
				y >= 0 && y < map[0].length && map[x][y] == null) {
			map[x][y] = colorHistory.color;
			colorHistory.history.add(new Vector2(x, y));
			return true;
		}
		return false;
	}

	private ColorHistory startColorHistory(Color[][] map, Color color) {
		ColorHistory ch = new ColorHistory(color);
		ch.preferedPoint = new Vector2(MathUtils.random(0, map.length-1), MathUtils.random(0, map[0].length-1));
		setStartPoint(map, ch);
		return ch;
	}

	private void setStartPoint(Color[][] map, ColorHistory ch) {
		int minX = ch.color.equals(Quadtris.COLORS[2])? map.length-1 : 0;
		int minY = ch.color.equals(Quadtris.COLORS[1])? map[0].length-1 : 0;
		int maxX = ch.color.equals(Quadtris.COLORS[0])? 0 : map.length-1;
		int maxY = ch.color.equals(Quadtris.COLORS[3])? 0 : map[0].length-1;
		
		boolean foundFreeCell = false;
		do {
			int x = MathUtils.random(minX, maxX);
			int y = MathUtils.random(minY, maxY);
			if (map[x][y] == null) {
				map[x][y] = ch.color;
				ch.history.add(new Vector2(x, y));
				foundFreeCell = true;
			}
		} while (!foundFreeCell);
	}
}
