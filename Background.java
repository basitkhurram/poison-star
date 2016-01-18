package com.example.basit.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Basit on 28-Dec-15.
 */
public class Background {
    private Bitmap background;
    private int x, y, dx, dy;
    private float SCREEN_WIDTH, SCREEN_HEIGHT;

    public Background(Bitmap background){
        this.background = background;
        this.SCREEN_WIDTH = background.getWidth()/4;
        this.SCREEN_HEIGHT = background.getHeight()/4;
        x = y = dx = dy = 0;
    }

    public void update(){
        if(x < -4*SCREEN_WIDTH){
            x = 0;
        }
        if(y < -SCREEN_HEIGHT){
            y = 0;
        }
        x += dx;
        y += dy;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(background, x, y, null);
        canvas.drawBitmap(background, x + 4*SCREEN_WIDTH, y, null);

        // canvas.drawBitmap(background, x + SCREEN_WIDTH, y, null);
    }

    public void setDisplacement(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }
}


