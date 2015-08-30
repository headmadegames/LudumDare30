package de.druz.quadtris.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.druz.quadtris.Quadtris;
import de.druz.quadtris.QuadtrisTest;
import de.druz.quadtris.QuadtrisTest2;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Quadtris(), config);
	}
}
