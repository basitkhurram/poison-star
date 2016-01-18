package com.example.basit.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;

import java.util.Random;

/**
 * Created by Basit on 30-Dec-15.
 */
public class Asteroids extends Sprite{

    public Asteroids(float[] pos, float[] vel, float ang, float ang_vel, Bitmap image,
                    ImageInfo imageInfo, SoundPool sound, float SCREEN_WIDTH, float SCREEN_HEIGHT) {

        super(pos, vel, ang, ang_vel, image, imageInfo, sound, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public Sprite spawnAsteroid(Sprite myShip) {
        Random rand = new Random();

        int posX = rand.nextInt((int) SCREEN_WIDTH);
        int posY = rand.nextInt((int) SCREEN_HEIGHT);
        float[] pos = new float[]{posX, posY};
        while (distance(pos, myShip.pos) / 3 < myShip.radius + imageInfo.radius) {
            posX = rand.nextInt((int) SCREEN_WIDTH);
            posY = rand.nextInt((int) SCREEN_HEIGHT);
            pos = new float[]{posX, posY};
        }

        int velX = rand.nextInt(15) - 7;
        int velY = rand.nextInt(15) - 7;
        int ang = rand.nextInt(360);
        int ang_vel = rand.nextInt(9) - 4;

        float[] vel = new float[]{velX, velY};
        Sprite asteroid = new Sprite(pos, vel, ang, ang_vel, image,
                imageInfo, null, SCREEN_WIDTH, SCREEN_HEIGHT);
        return asteroid;
    }



}
