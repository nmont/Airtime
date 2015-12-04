package com.badlogic.drop;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by nmont on 11/19/2015.
 */
public class Raindrop {

    final int MAX_TIME = 200;

    Rectangle rectangle;
    boolean isHit;
    Vector2 velocity;
    long timeSinceHit;
    int timesHit;

    public Raindrop(Rectangle rectangle, boolean isHit) {
        this.rectangle = rectangle;
        this.isHit = isHit;
        velocity = new Vector2();
        timeSinceHit = 0;
        timesHit = 0;
    }

    public boolean getIsHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
        timesHit++;
    }

    public int getTimesHit() {
        return timesHit;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    /**
     * updates the time since hit, sets isHit if past time limit of being hit
     */
    public void update() {
        timeSinceHit++;
        if(timeSinceHit > MAX_TIME) {
            isHit = false;
            timeSinceHit = 0;
        }
    }

}
