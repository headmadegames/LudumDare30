package de.druz.quadtris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

	public static boolean soundOn = false;
	
	public static Music music;
	
	public static Sound clickSound;
	public static Sound moveSound;
	public static Sound rotateSound;
	public static Sound switchSound;
	public static Sound connectSound;
	public static Sound collideSound;
	public static Sound fallingSound;
	public static Sound blockedSound;

	public static Texture logo;
	public static Texture block;
	public static Texture blocks;

	public static TextureRegion red;
	public static TextureRegion blue;
	public static TextureRegion green;
	public static TextureRegion gray;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void load() {
		Gdx.app.log("init", "loading Assets");
		block = loadTexture("block2.png");
		logo = loadTexture("logo.png");
		blocks = loadTexture("blocks.png");
		
		red = new TextureRegion(blocks, 0, 0, 32, 32);
		blue = new TextureRegion(blocks, 32, 0, 32, 32);
		green = new TextureRegion(blocks, 64, 0, 32, 32);
		gray = new TextureRegion(blocks, 96, 0, 32, 32);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music2.ogg"));
		music.setVolume(0.4f);
		music.setLooping(true);

		clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
		moveSound = Gdx.audio.newSound(Gdx.files.internal("move.wav"));
		rotateSound = Gdx.audio.newSound(Gdx.files.internal("rotate.wav"));
		switchSound = Gdx.audio.newSound(Gdx.files.internal("switch.wav"));
		connectSound = Gdx.audio.newSound(Gdx.files.internal("connect.wav"));
		collideSound = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
		fallingSound = Gdx.audio.newSound(Gdx.files.internal("falling.wav"));
		blockedSound = Gdx.audio.newSound(Gdx.files.internal("blocked.wav"));
//		Gdx.app.log("init", "block: " + block.toString());
	}

	public static void playSound(Sound sound) {
		if (soundOn) {
			sound.play();
		}
	}
}
