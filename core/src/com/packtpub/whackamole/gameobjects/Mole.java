package com.packtpub.whackamole.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Mole {

    public Sprite moleSprite; //sprite to display the mole
    public Vector2 position = new Vector2();// The mole's position
    public float height, width; // the mole's dimensions
    public float scaleFactor; // scaling factor for the mole

    public State state = State.GOINGUP; // variable describing mole's current state
    public float currentHeight = 0.0f; // current height of the mole above ground
    public float speed = 2f; // speed of the mole as it goes up and down

    public float timeUnderGround = 0.0f; // time since the mole is underground
    public float maxTimeUnderGround = 0.8f; // max time allowed for the mole to stay underground

    public enum State {GOINGUP, GOINGDOWN, UNDERGROUND, STUNNED}; // define mole's states
    public float stunTime = 0.1f; // The amount of time the mole would be stunned
    public float stunCounter = 0.0f; // The amount of time the mole is currently stunned

    public void randomizeWaitTime() {
        maxTimeUnderGround = (float) Math.random() * 2f;
    }

    public boolean handleTouch(float touchX, float touchY) {
        if ((touchX >= position.x) && touchX <= (position.x + width) &&
                (touchY >= position.y) && touchY <= (position.y + currentHeight)) {

            state = State.STUNNED; // change the state to stunned

            /*state = State.UNDERGROUND; // change the state to underground
            currentHeight = 0.0f; // change the current height to 0
            moleSprite.setRegion(0, 0, (int) (width / scaleFactor), (int) (currentHeight / scaleFactor));
            moleSprite.setSize(moleSprite.getWidth(), currentHeight);
            // reset the underground timer
            timeUnderGround = 0.0f;
            randomizeWaitTime();*/
            return true;
        }
        return false;
    }


    public void update() {
        switch (state) {
            case STUNNED:
                if(stunCounter>=stunTime){
                    // send the mole underground
                    state= State.UNDERGROUND;
                    stunCounter=0.0f;
                    currentHeight=0.0f;
                    randomizeWaitTime();
                }
                else{
                    stunCounter+=Gdx.graphics.getDeltaTime();
                }
                break;
            case UNDERGROUND:
                if (timeUnderGround >= maxTimeUnderGround) {
                    state = State.GOINGUP;
                    timeUnderGround = 0.0f;
                } else {
                    timeUnderGround += Gdx.graphics.getDeltaTime();
                }
                break;
            // here increase the height till it reaches max, once it reaches, change the state
            case GOINGUP:
                currentHeight += speed;
                if (currentHeight >= height) {
                    currentHeight = height;
                    state = State.GOINGDOWN;
                }
                break;
            // here decrease the height till it reaches min(0), once it reaches, change the state
            case GOINGDOWN:
                currentHeight -= speed;
                if (currentHeight <= 0.0) {
                    currentHeight = 0.0f;
                    state = State.GOINGUP;
                }
                break;
        }
        // draw only some portion of the mole image, depending on height
        moleSprite.setRegion(0, 0, (int) (width / scaleFactor), (int) (currentHeight / scaleFactor));
        moleSprite.setSize(moleSprite.getWidth(), currentHeight);
    }

    public void render(SpriteBatch batch) {
        moleSprite.draw(batch);
    }
}