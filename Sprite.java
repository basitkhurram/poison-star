package com.example.basit.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basit on 29-Dec-15.
 */
public class Sprite {

    float SCREEN_WIDTH;
    float SCREEN_HEIGHT;
    float[] pos;
    float[] vel;
    float ang;
    float ang_vel;
    Bitmap image;
    ImageInfo imageInfo;
    SoundPool sound;
    float age = 0;
    float lifespan;
    float radius;
    boolean animated;
    float[] size;
    float[] dstTopLeft;
    float[] srcTopLeft;
    int imageFrameRows;
    int imageFrameColumns;
    int currentFrameRow = 0;
    int currentFrameColumn = 0;
    int frameWidth;
    int frameHeight;
    List<Bitmap> reel = new ArrayList<Bitmap>();
    int currentFrame = 0;
    int frameCount;

    public Sprite(float[] pos, float[] vel, float ang, float ang_vel, Bitmap image,
                  ImageInfo imageInfo, SoundPool sound, float SCREEN_WIDTH, float SCREEN_HEIGHT) {
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.pos = pos;
        this.vel = vel;
        this.ang = ang;
        this.ang_vel = ang_vel;
        this.image = image;
        this.imageInfo = imageInfo;
        this.sound = sound;
        this.radius = imageInfo.radius;
        this.lifespan = imageInfo.lifespan;
        this.animated = imageInfo.animated;
        this.srcTopLeft = imageInfo.topLeft;
        this.size = imageInfo.size;
        this.imageFrameRows = imageInfo.imageFrameRows;
        this.imageFrameColumns = imageInfo.imageFrameColumns;
        this.frameWidth = (int) (size[0]/imageFrameColumns);
        this.frameHeight = (int) (size[0]/imageFrameRows);
        dstTopLeft = new float[] {pos[0] - size[0]/2, pos[1] - size[1]/2};
        reel = imageInfo.reel;
        frameCount = reel.size();
    }

    public Sprite clone(){
        return new Sprite(pos, vel, ang, ang_vel, image, imageInfo, sound, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public void setFrame(int newFrameRow, int newFrameColumn){
        this.currentFrameRow = newFrameRow;
        this.currentFrameColumn = newFrameColumn;
    }

    public void setFrame(int newFrame){
        this.currentFrame = newFrame;
    }

    public void draw(Canvas canvas){
        Matrix matrix = new Matrix();
        Rect dst = new Rect((int) dstTopLeft[0], (int) dstTopLeft[1],
                (int) dstTopLeft[0] + (int) size[0], (int) dstTopLeft[1] + (int) size[1]);

        if (animated){
            canvas.save();
            canvas.rotate(ang, pos[0], pos[1]);
            canvas.drawBitmap(reel.get(currentFrame), null, dst, null);
            canvas.restore();
        }

        else {

            Rect src = new Rect(0, 0,
                    (int) size[0], (int) size[1]);

            canvas.save(); //save the position of the canvas
            canvas.rotate(ang, pos[0], pos[1]); //rotate the canvas' matrix
            canvas.drawBitmap(image, src, dst, null); //draw the ball on the "rotated" canvas
            canvas.restore(); //rotate the canvas' matrix back
        }
    }

    public void update(){
        pos = new float[] {(((float) (pos[0] +  vel[0]) % SCREEN_WIDTH) + SCREEN_WIDTH) % SCREEN_WIDTH,
                (((float) (pos[1] + vel[1]) % SCREEN_HEIGHT) + SCREEN_HEIGHT) % SCREEN_HEIGHT};
        dstTopLeft = new float[] {pos[0] - size[0]/2, pos[1] - size[1]/2};
        ang += ang_vel;
        age += 1.5;
        if (animated){
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }

    public boolean collision(Sprite other_object){
        return distance(pos, other_object.pos) < radius + other_object.radius;
    }


    float distance(float[] pos_1, float[] pos_2){
        float dx = pos_2[0] - pos_1[0];
        float dy = pos_2[1] - pos_1[1];
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
