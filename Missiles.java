package com.example.basit.myapplication;

import android.graphics.Bitmap;
import android.media.SoundPool;

/**
 * Created by Basit on 30-Dec-15.
 */
public class Missiles extends Sprite{
    public Missiles(float[] pos, float[] vel, float ang, float ang_vel, Bitmap image,
                    ImageInfo imageInfo, SoundPool sound, float SCREEN_WIDTH, float SCREEN_HEIGHT) {

        super(pos, vel, ang, ang_vel, image, imageInfo, sound, SCREEN_WIDTH, SCREEN_HEIGHT);
    }
}

