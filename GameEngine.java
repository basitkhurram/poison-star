package com.example.basit.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.SoundPool;

/**
 * Created by Basit on 29-Dec-15.
 */
public class GameEngine {

    private float SCREEN_WIDTH;
    private float SCREEN_HEIGHT;
    private ImageInfo shipInfo;
    private Bitmap shipImage;

    public GameEngine(float SCREEN_WIDTH, float SCREEN_HEIGHT){
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    }

    public void init() {
        shipInfo = new ImageInfo(new float[] {0,0}, new float[] {1004/2, 345}, 251,
                Float.POSITIVE_INFINITY, false);

//        Sprite myShip = Sprite(new float[] {SCREEN_WIDTH/2, SCREEN_HEIGHT/2},
//        new float[] {0, 0}, 0, 0, Bitmap image,
//                ImageInfo imageInfo, null, SCREEN_WIDTH, SCREEN_HEIGHT);

    }

}