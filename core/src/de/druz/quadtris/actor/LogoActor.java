package de.druz.quadtris.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import de.druz.quadtris.Assets;
import de.druz.quadtris.Quadtris;

public class LogoActor extends WidgetGroup {

	@Override
	public float getPrefWidth() {
		return 450;
	}

	@Override
	public float getPrefHeight() {
		return 450;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Assets.logo, getX() + 40, getY() + 40);

		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.draw(batch, parentAlpha);
	}
}
