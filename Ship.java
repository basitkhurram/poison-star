package com.example.basit.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.SoundPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basit on 30-Dec-15.
 */
public class Ship extends Sprite{

    private boolean shooting = false;
    private boolean thrusting = false;
    private float ACCELERATION = 1;
    private float FRICTION = (float) 0.07;
    private float MAX_SPEED = 8;
    float MAX_ANG_VEL = 2.5F;
    private int ARSENAL_SIZE = 5;
    private int MISSILE_SPEED = 25;
    List<Sprite> missiles = new ArrayList<Sprite>();
    List<Sprite> missilesClone = new ArrayList<Sprite>();
    private Bitmap missileImage;
    private ImageInfo missileInfo;
    private int missileSound;

    public Ship(float[] pos, float[] vel, float ang, float ang_vel, Bitmap image,
                  ImageInfo imageInfo, SoundPool sound, float SCREEN_WIDTH, float SCREEN_HEIGHT,
                int missileSound, Bitmap missileImage, ImageInfo missileInfo) {
        super(pos, vel, ang, ang_vel, image, imageInfo, sound, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.missileImage = missileImage;
        this.missileInfo = missileInfo;
        this.missileSound = missileSound;
    }

    public boolean isShooting(){
        return shooting;
    }

    public void setShooters(boolean shooting){
        this.shooting = shooting;
    }

    public void shoot(){
        if (missiles.size() < ARSENAL_SIZE){
            missiles.add(spawnMissile());
            sound.play(missileSound, 0.7F, 0.7F, 10, 0, 0);
        }
    }


    public Sprite spawnMissile() {
        float posX = pos[0] + image.getWidth()/2 * (float) Math.cos(Math.toRadians(ang));
        float posY = pos[1] + image.getHeight()/2 * (float) Math.sin(Math.toRadians(ang));;
        float[] pos = new float[]{posX, posY};

        float velX = (float) MISSILE_SPEED * (float) Math.cos(Math.toRadians(ang));
        float velY = (float) MISSILE_SPEED * (float) Math.sin(Math.toRadians(ang));

        float[] vel = new float[]{velX, velY};
        Sprite missile = new Sprite(pos, vel, this.ang, 0, missileImage,
                missileInfo, null, SCREEN_WIDTH, SCREEN_HEIGHT);
        missile.animated = true;
        return missile;
    }


    public boolean isThrusting(){
        return thrusting;
    }

    public void setThruster(boolean thrusting){
        this.thrusting = thrusting;
    }

    public void thrust(){
        vel[0] += (float) ACCELERATION * Math.cos(Math.toRadians(ang)) * (1 - FRICTION);
        vel[1] += (float) ACCELERATION * Math.sin(Math.toRadians(ang)) * (1 - FRICTION);
    }

    @Override
    public void update(){
        vel[0] = vel[0] * (1 - FRICTION);
        vel[1] = vel[1] * (1 - FRICTION);
        pos = new float[] {(((float) (pos[0] +  vel[0]) % SCREEN_WIDTH) + SCREEN_WIDTH) % SCREEN_WIDTH,
                (((float) (pos[1] + vel[1]) % SCREEN_HEIGHT) + SCREEN_HEIGHT) % SCREEN_HEIGHT};

        ang = ((ang + ang_vel) % 360 + 360) % 360;

        dstTopLeft = new float[] {pos[0] - size[0]/2, pos[1] - size[1]/2};
        age += 1.5;
        if (animated){
            currentFrame = (currentFrame + 1) % frameCount;
        }

        if(isThrusting()){
            thrust();
            animated = true;
        }
        else{
            animated = false;
        }

        for(int idx = 0; idx < missiles.size(); idx++){
            if(missiles.get(idx).age < missileInfo.lifespan){
                missilesClone.add(missiles.get(idx));
            }
        }
        missiles = missilesClone;
        missilesClone = new ArrayList<Sprite>();
    }

/*
    @Override
    public void draw(Canvas canvas) {
        Rect dst = new Rect((int) dstTopLeft[0], (int) dstTopLeft[1],
                (int) dstTopLeft[0] + (int) size[0], (int) dstTopLeft[1] + (int) size[1]);

        if (animated) {
            image = reel.get(currentFrame);
        }
        canvas.save(); //save the position of the canvas
        canvas.rotate(ang, pos[0], pos[1]); //rotate the canvas' matrix
        canvas.drawBitmap(image, null, dst, null); //draw the ball on the "rotated" canvas
        canvas.restore(); //rotate the canvas' matrix back
    }
*/
}
