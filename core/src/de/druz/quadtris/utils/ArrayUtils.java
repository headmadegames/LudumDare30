package de.druz.quadtris.utils;

import com.badlogic.gdx.graphics.Color;

public class ArrayUtils {

	public static boolean[][] rotateLeft(boolean[][] array) {
		final int M = array.length;
	    final int N = array[0].length;
	    boolean[][] newArray = new boolean[N][M];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            newArray[c][M-1-r] = array[r][c];
	        }
	    }
		return newArray;
	}

	public static boolean[][] rotateRight(boolean[][] array) {
	    final int M = array.length;
	    final int N = array[0].length;
	    boolean[][] newArray = new boolean[N][M];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            newArray[N-1-c][r] = array[r][c];
	        }
	    }
		return newArray;
	}

	public static Color[][] flip(Color[][] array) {
	    final int M = array.length;
	    final int N = array[0].length;
	    Color[][] newArray = new Color[M][N];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            newArray[M-1-r][N-1-c] = array[r][c];
	        }
	    }
		return newArray;
	}

	public static Color[][] rotateLeft(Color[][] array) {
		final int M = array.length;
	    final int N = array[0].length;
	    Color[][] newArray = new Color[N][M];
	    for (int r = 0; r < M; r++) {
	        for (int c = 0; c < N; c++) {
	            newArray[c][M-1-r] = array[r][c];
	        }
	    }
		return newArray;
	}

	public static void removeEmptyBottomRows(boolean[][] shape) {
	    int firstRowWithData = -1;
	    for (int y = 0; y < shape[0].length; y++) {
	    	for (int x = 0; x < shape.length; x++) {
				if (shape[x][y]) {
					firstRowWithData = y;
					break;
				}
			}
	    	if (firstRowWithData >= 0) {
	    		break;
			}
		}
	    if (firstRowWithData > 0) {
	    	for (int x = 0; x < shape.length; x++) {
		    	for (int y = 0; y+firstRowWithData < shape[0].length; y++) {
		    		shape[x][y] = shape[x][firstRowWithData+y]; 
		    	}
	    	}
	    	for (int x = 0; x < shape.length; x++) {
		    	for (int y = shape[0].length-firstRowWithData; y < shape[0].length; y++) {
					shape[x][y] = false;
				}
	    	}
		}
	}

}
