package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;


public class GameScreen implements Screen {
    final Airtime game;
    final int LOW = 1;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    private Music Music;


    Texture ball1;
    Texture ball2;
    Texture ball3;
    Sound hitSound;
    Sound gameOverSound;

    OrthographicCamera camera;
    Rectangle cursor;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle saveExitBounds;

    Array<Ball> balls;
    long lastDropTime;
    int BallsGathers;
    int points_dropped;
    int speed;
    int timer;
    int level;
    int state;

    String status;

    float x_loc;
    float y_loc;
    boolean is_touched;
    Vector3 touchPoint;

    public GameScreen(final Airtime gam) {
        this.game = gam;
        state = GAME_RUNNING;

        pauseBounds = new Rectangle(10,425,75,36);
        resumeBounds = new Rectangle(190, 175, 150, 36);
        saveExitBounds = new Rectangle(190, 100, 170, 36);

        // load the images for the balls
        ball1 = new Texture(Gdx.files.internal("ball.png"));
        ball2 = new Texture(Gdx.files.internal("ball2.png"));
        ball3 = new Texture(Gdx.files.internal("ball3.png"));
        Music = Gdx.audio.newMusic(Gdx.files.internal("lucky.mp3"));

        Music.setLooping(true);
        Music.play();

        // load the drop sound effect and the rain background "music"
        hitSound = Gdx.audio.newSound(Gdx.files.internal("blip.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));
//        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
//        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the cursor
        cursor = new Rectangle();
        cursor.x = 800 / 2 - 64 / 2; // center the cursor horizontally
        cursor.y = 20; // bottom left corner of the cursor is 20 pixels above
        // the bottom screen edge
        cursor.width = 64;
        cursor.height = 64;

        // create the balls array and spawn the first balls
        balls = new Array<Ball>();

        points_dropped = 0;
        speed = 1;

        timer = 0;
//        level = 1;

        x_loc = 0;
        y_loc = 0;
        is_touched = false;
        touchPoint = new Vector3();

        status = game.getPreferences().getSavedState();
        //Gdx.app.getPreferences("Game Preferences").getString("SAVED_STATE", "NULL");
        if(status.compareTo("SAVED") == 0) {
            BallsGathers = game.getPreferences().getCurStateScore();
            level = game.getPreferences().getCurStateLevel();
//            BallsGathers = Gdx.app.getPreferences("Game Preferences").getInteger("SAVED_SCORE", 0);
            game.getPreferences().putSavedState("NOT_SAVED");
//            Gdx.app.getPreferences("Game Preferences").putString("SAVED_STATE", "NOT_SAVED");
            game.getPreferences().putCurStateScore(0);
//            Gdx.app.getPreferences("Game Preferences").putInteger("SAVED_SCORE", 0);
//            Gdx.app.getPreferences("Game Preferences").flush();
        }

        else
        {
            BallsGathers = 0;
            level = 1;
        }

        spawnBall();
    }

    private void spawnBall() {
        Rectangle ball_rectangle = new Rectangle();
        boolean isHit = false;
        ball_rectangle.x = MathUtils.random(0, 800 - 64);
        ball_rectangle.y = 480;
        ball_rectangle.width = 64;
        ball_rectangle.height = 64;

        //Make Ball object
        Ball r = new Ball(ball_rectangle, isHit);
        balls.add(r);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.

        switch (state) {
            case GAME_RUNNING:
                Playing(delta);
                break;
            case GAME_PAUSED:
                Paused(delta);
                break;
        }
    }

    private void Playing(float delta) {
        if (level == 1) {
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else if (level == 2) {
            Gdx.gl.glClearColor(0, 0.1f, 0.2f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else if (level == 3) {
            Gdx.gl.glClearColor(0, 0.3f, 0.2f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else if (level == 4) {
            Gdx.gl.glClearColor(0.1f, 0.2f, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else if (level == 5) {
            Gdx.gl.glClearColor(0.3f, 0.2f, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else if (level == 6) {
            Gdx.gl.glClearColor(0.5f, 0.1f, 0.1f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else if (level == 7) {
            Gdx.gl.glClearColor(0.9f, 0.1f, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        } else {
            Gdx.gl.glClearColor(1.0f, 0, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the cursor and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Score: " + BallsGathers, 200, 480);
        game.font.draw(game.batch, "Pause", 25, 450);
        for (Ball ball : balls) {
            if (ball.getTimesHit() == 0)
                game.batch.draw(ball1, ball.getRectangle().x, ball.getRectangle().y);
            else if (ball.getTimesHit() == 1)
                game.batch.draw(ball2, ball.getRectangle().x, ball.getRectangle().y);
            else game.batch.draw(ball3, ball.getRectangle().x, ball.getRectangle().y);
        }

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(pauseBounds.x, pauseBounds.y, pauseBounds.width, pauseBounds.height);

        game.batch.end();
        renderer.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            is_touched = true;
            x_loc = touchPos.x - 64 / 2;
            y_loc = touchPos.y - 64 / 2;
            cursor.x = x_loc;
            cursor.y = y_loc;
            if (pauseBounds.contains(touchPos.x, touchPos.y)) {
                Music.pause();
                state = GAME_PAUSED;
            }
        } else {
            is_touched = false;
        }
        if (cursor.x < 0)
            cursor.x = 0;
        if (cursor.x > 800 - 64)
            cursor.x = 800 - 64;
        // check if we need to create a new ball
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            timer++;
        }
        if ((timer >= 30 / level) || (level > 10)) {
            spawnBall();
            timer = 0;
        }

        // move the balls, remove any that are beneath the bottom edge of
        // the screen or that hit the cursor. In the later case we play back
        // a sound effect as well.
        Iterator<Ball> iter = balls.iterator();
        while (iter.hasNext()) {
            Ball ball = iter.next();
            ball.update();
            // Move ball down if not hit
            if (!ball.isHit)
                ball.getRectangle().y -= speed * Gdx.graphics.getDeltaTime() - ball.getVelocity();
            else
                ball.getRectangle().y += speed * Gdx.graphics.getDeltaTime() + ball.getVelocity();

            // if ball gets to bottom of screen
            if (ball.getRectangle().y + 64 < 0) {
                iter.remove();
                points_dropped++;

                if (points_dropped >= LOW) {
                    Music.dispose();
                    gameOverSound.play();
                    game.getPreferences().putHighScoreAvail(true);
                    game.getPreferences().putCurLevel(level);
                    game.getPreferences().putCurScore(BallsGathers);
                    Gdx.input.getTextInput(new TextInputListener() {
                        @Override
                        public void input(String text) {
                            game.getPreferences().putCurName(text);
                        }

                        @Override
                        public void canceled() {

                        }
                    }, "Score: " + BallsGathers, "Enter Name", "");
                    game.setScreen(new GameOverScreen(game));
                }

            }

            // if ball hits the top of the screen
            if ((ball.getRectangle().y - 64 > 480) && (ball.isHit)) {
                ball.setIsHit(false);
            }

            if (is_touched && ball.getRectangle().overlaps(cursor) && ball.getIsHittable()) {
                if (ball.getTimesHit() >= 2) iter.remove();
                BallsGathers++;
                if (BallsGathers % 10 == 0) level++;
                ball.setIsHit(true);
                hitSound.play();
            }

            speed = (1 * BallsGathers) + 100;
        }
    }

    private void Paused(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Resume", resumeBounds.getX(), resumeBounds.getY() + resumeBounds.height / 2);
        game.font.draw(game.batch, "Save and Exit", saveExitBounds.getX(), saveExitBounds.getY() + saveExitBounds.height / 2);


        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(resumeBounds.x, resumeBounds.y, resumeBounds.width, resumeBounds.height);
        renderer.rect(saveExitBounds.x, saveExitBounds.y, saveExitBounds.width, saveExitBounds.height);

        game.batch.end();

        renderer.end();

        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (resumeBounds.contains(touchPoint.x, touchPoint.y)) {
                Music.play();
                state = GAME_RUNNING;
                return;
            }

            if (saveExitBounds.contains(touchPoint.x, touchPoint.y)) {
//                Gdx.app.getPreferences("Game Preferences").putString("SAVED_STATE", "SAVED");
                game.getPreferences().putSavedState("SAVED");
//                Gdx.app.getPreferences("Game Preferences").flush();

//                Gdx.app.getPreferences("Game Preferences").putInteger("SAVED_SCORE", BallsGathers);
                game.getPreferences().putCurStateScore(BallsGathers);
//                Gdx.app.getPreferences("Game Preferences").flush();
                game.getPreferences().putCurStateLevel(level);
                Music.stop();

                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
//        rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        game.getPreferences().putCurStateScore(BallsGathers);
        game.getPreferences().putCurStateLevel(level);
    }

    @Override
    public void resume() {
        BallsGathers = game.getPreferences().getCurStateScore();
        level = game.getPreferences().getCurStateLevel();
    }

    @Override
    public void dispose() {
        ball1.dispose();
        hitSound.dispose();
    }

}