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
//    Vector2 velocity;
    int velocity;
    long timeSinceHit;
    int timesHit;
    int lifeTime;
    boolean isHittable;

    public Raindrop(Rectangle rectangle, boolean isHit) {
        this.rectangle = rectangle;
        this.isHit = isHit;
        velocity = 0;
        timeSinceHit = 0;
        timesHit = 0;
        lifeTime = 0;
        isHittable = true;
    }

    public boolean getIsHit() {
        return isHit;
    }

    public boolean getIsHittable() {
        return isHittable;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
        if(isHit) velocity = 3;
        isHittable = false;
        timesHit++;
    }

    public int getTimesHit() {
        return timesHit;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getVelocity() {
//        if (velocity < 0) velocity = 0;
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    /**
     * updates the time since hit, sets isHit if past time limit of being hit
     */
    public void update() {
        lifeTime++;
        if(lifeTime % 20 == 0)velocity--;

        if(isHit) {
            timeSinceHit++;
//            velocity --;
        }
        if(timeSinceHit > MAX_TIME) {
            isHit = false;
            timeSinceHit = 0;
        }

        if(velocity < 0) isHittable = true;
    }

}
