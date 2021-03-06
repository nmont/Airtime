package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {
    final Airtime game;
    OrthographicCamera camera;
    Rectangle playBounds;
    Vector3 touchPoint;
    Rectangle highScoreBounds;

    public MainMenuScreen(final Airtime gam) {
        game = gam;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        touchPoint = new Vector3();
        playBounds = new Rectangle(90, 75, 150, 36);
        highScoreBounds = new Rectangle(90,25,185,36);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to Airtime! ", 100, 150);
        game.font.draw(game.batch, "Tap here to begin!", 100, 100);
        game.font.draw(game.batch, "Tap here for high scores", 100,50);

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(playBounds.x, playBounds.y, playBounds.width, playBounds.height);
        renderer.rect(highScoreBounds.x, highScoreBounds.y, highScoreBounds.width, highScoreBounds.height);

        game.batch.end();

        renderer.end();


        if (Gdx.input.isTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (playBounds.contains(touchPoint.x, touchPoint.y)) {
                //Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));
                dispose();
            }
            else if (highScoreBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new HighScoreScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
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
    }
}