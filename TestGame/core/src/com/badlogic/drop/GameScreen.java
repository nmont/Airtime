package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final Drop game;
    final int LOW = 1;

    Texture ball1;
    Texture ball2;
    Texture ball3;
    Sound hitSound;
    Sound gameOverSound;
//    Music rainMusic;

    OrthographicCamera camera;
    Rectangle bucket;
    Array<Raindrop> raindrops;
    long lastDropTime;
    int dropsGathered;
    int points_dropped;
    int speed;
    int timer;
    int level;

    float x_loc;
    float y_loc;
    boolean is_touched;

    public GameScreen(final Drop gam) {
        this.game = gam;

        // load the images for the droplet and the bucket, 64x64 pixels each

        //TODO
        // change images to platform and balloon
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
        if(level == 1){
            Gdx.gl.glClearColor(0, 0, 0.2f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else if(level == 2) {
            Gdx.gl.glClearColor(0, 0.1f, 0.2f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else if(level == 3) {
            Gdx.gl.glClearColor(0, 0.3f, 0.2f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else if(level == 4) {
            Gdx.gl.glClearColor(0.1f, 0.2f, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else if(level == 5) {
            Gdx.gl.glClearColor(0.3f, 0.2f, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else if(level == 6) {
            Gdx.gl.glClearColor(0.5f, 0.1f, 0.1f, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else if(level == 7) {
            Gdx.gl.glClearColor(0.9f, 0.1f, 0, 1);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        }
        else {
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
        for (Raindrop raindrop : raindrops) {
            if(raindrop.getTimesHit() == 0)game.batch.draw(ball1, raindrop.getRectangle().x, raindrop.getRectangle().y);
            else if(raindrop.getTimesHit() == 1)game.batch.draw(ball2, raindrop.getRectangle().x, raindrop.getRectangle().y);
            else game.batch.draw(ball3, raindrop.getRectangle().x, raindrop.getRectangle().y);
        }
        game.batch.end();

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
        }
        else {
            is_touched = false;
        }
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            timer ++;
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
            if(!raindrop.isHit) raindrop.getRectangle().y -= speed * Gdx.graphics.getDeltaTime() - raindrop.getVelocity();
            else raindrop.getRectangle().y += speed * Gdx.graphics.getDeltaTime() + raindrop.getVelocity();

            // if raindrop gets to bottom of screen
            if (raindrop.getRectangle().y + 64 < 0) {
                iter.remove();
                points_dropped++;

                if(points_dropped >= LOW) {
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

            if (is_touched && raindrop.getRectangle().overlaps(bucket) && raindrop.getIsHittable() ) {
                if(raindrop.getTimesHit() >= 2) iter.remove();
                dropsGathered++;
                if(dropsGathered % 10 == 0) level++;
                raindrop.setIsHit(true);
                hitSound.play();
            }

            speed = (1 * dropsGathered) + 100;

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