package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
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
    final Drop game;
    final int LOW = 1;
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    Texture ball1;
    Texture ball2;
    Texture ball3;
    Sound hitSound;
    Sound gameOverSound;

    OrthographicCamera camera;
    Rectangle bucket;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle saveExitBounds;

    Array<Raindrop> raindrops;
    long lastDropTime;
    int dropsGathered;
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

    public GameScreen(final Drop gam) {
        this.game = gam;
        state = GAME_RUNNING;

        pauseBounds = new Rectangle(10,425,75,36);
        resumeBounds = new Rectangle(190, 175, 150, 36);
        saveExitBounds = new Rectangle(190, 100, 170, 36);

        // load the images for the balls
        ball1 = new Texture(Gdx.files.internal("ball.png"));
        ball2 = new Texture(Gdx.files.internal("ball2.png"));
        ball3 = new Texture(Gdx.files.internal("ball3.png"));

        // load the drop sound effect and the rain background "music"
        hitSound = Gdx.audio.newSound(Gdx.files.internal("blip.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));
//        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
//        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Raindrop>();

        points_dropped = 0;
        speed = 1;

        timer = 0;
        level = 1;

        x_loc = 0;
        y_loc = 0;
        is_touched = false;
        touchPoint = new Vector3();

        status = Gdx.app.getPreferences("Game Preferences").getString("SAVED_STATE", "NULL");
        if(status.compareTo("SAVED") == 0) {
            dropsGathered = Gdx.app.getPreferences("Game Preferences").getInteger("SAVED_SCORE", 0);
            Gdx.app.getPreferences("Game Preferences").putString("SAVED_STATE", "NOT_SAVED");
            Gdx.app.getPreferences("Game Preferences").putInteger("SAVED_SCORE", 0);
            Gdx.app.getPreferences("Game Preferences").flush();
        }

        else dropsGathered = 0;

        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        boolean isHit = false;
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;

        //Make Raindrop object
        Raindrop r = new Raindrop(raindrop, isHit);
        raindrops.add(r);
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

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Score: " + dropsGathered, 200, 480);
        game.font.draw(game.batch, "Pause", 25, 450);
        for (Raindrop raindrop : raindrops) {
            if (raindrop.getTimesHit() == 0)
                game.batch.draw(ball1, raindrop.getRectangle().x, raindrop.getRectangle().y);
            else if (raindrop.getTimesHit() == 1)
                game.batch.draw(ball2, raindrop.getRectangle().x, raindrop.getRectangle().y);
            else game.batch.draw(ball3, raindrop.getRectangle().x, raindrop.getRectangle().y);
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
            bucket.x = x_loc;
            bucket.y = y_loc;
            if (pauseBounds.contains(touchPos.x, touchPos.y)) {
                state = GAME_PAUSED;
            }
        } else {
            is_touched = false;
        }
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;
        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            timer++;
        }
        if ((timer >= 30 / level) || (level > 10)) {
            spawnRaindrop();
            timer = 0;
        }

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we play back
        // a sound effect as well.
        Iterator<Raindrop> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Raindrop raindrop = iter.next();
            raindrop.update();
            // Move raindrop down if not hit
            if (!raindrop.isHit)
                raindrop.getRectangle().y -= speed * Gdx.graphics.getDeltaTime() - raindrop.getVelocity();
            else
                raindrop.getRectangle().y += speed * Gdx.graphics.getDeltaTime() + raindrop.getVelocity();

            // if raindrop gets to bottom of screen
            if (raindrop.getRectangle().y + 64 < 0) {
                iter.remove();
                points_dropped++;

                if (points_dropped >= LOW) {
                    gameOverSound.play();
                    game.getPreferences().putHighScoreAvail(true);
                    game.getPreferences().putCurLevel(level);
                    game.getPreferences().putCurScore(dropsGathered);
                    Gdx.input.getTextInput(new TextInputListener() {
                        @Override
                        public void input(String text) {
                            game.getPreferences().putCurName(text);
                        }

                        @Override
                        public void canceled() {

                        }
                    }, "Score: " + dropsGathered, "Enter Name", "");
                    game.setScreen(new GameOverScreen(game));
                }

            }

            // if raindrop hits the top of the screen
            if ((raindrop.getRectangle().y - 64 > 480) && (raindrop.isHit)) {
                raindrop.setIsHit(false);
            }

            if (is_touched && raindrop.getRectangle().overlaps(bucket) && raindrop.getIsHittable()) {
                if (raindrop.getTimesHit() >= 2) iter.remove();
                dropsGathered++;
                if (dropsGathered % 10 == 0) level++;
                raindrop.setIsHit(true);
                hitSound.play();
            }

            speed = (1 * dropsGathered) + 100;
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
                state = GAME_RUNNING;
                return;
            }

            if (saveExitBounds.contains(touchPoint.x, touchPoint.y)) {
                Gdx.app.getPreferences("Game Preferences").putString("SAVED_STATE", "SAVED");
                Gdx.app.getPreferences("Game Preferences").flush();

                Gdx.app.getPreferences("Game Preferences").putInteger("SAVED_SCORE", dropsGathered);
                Gdx.app.getPreferences("Game Preferences").flush();

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
        game.getPreferences().putCurStateScore(dropsGathered);
        game.getPreferences().putCurStateLevel(level);
    }

    @Override
    public void resume() {
        dropsGathered = game.getPreferences().getCurStateScore();
        level = game.getPreferences().getCurStateLevel();
    }

    @Override
    public void dispose() {
        ball1.dispose();
        hitSound.dispose();
//        rainMusic.dispose();
    }

}