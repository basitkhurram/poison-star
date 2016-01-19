package com.example.basit.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Basit on 28-Dec-15.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener,
        SensorEventListener{

    MainThread mainThread;
    private Background background;
    final int FPS = 60;
    final float BG_WIDTH = 1654;
    final float BG_HEIGHT = 480;
    final float SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
    final float SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;
    private int sleepTime = FPS * (1/4);
    private int tillWake = sleepTime;
    private ImageInfo shipInfo;
    private Bitmap shipImage;
    Ship myShip;
    private ImageInfo missileInfo;
    private Bitmap missileImage;
    private List<Bitmap> missileImages = new ArrayList<Bitmap>();
    private ImageInfo asteroidInfo;
    private int ASTEROID_LIFESPAN = (int) (FPS/1.2);
    private Bitmap asteroidImage;
    private List<Sprite> asteroids = new ArrayList<Sprite>();
    private List<Sprite> asteroidsClone = new ArrayList<Sprite>();
    private List<Sprite> asteroidsExploding = new ArrayList<Sprite>();
    private List<Sprite> asteroidsExplodingClone = new ArrayList<Sprite>();
    private List<Bitmap> asteroidImages = new ArrayList<Bitmap>(16);
    Bitmap poison_star_0;
    Bitmap poison_star_1;
    Bitmap poison_star_2;
    Bitmap poison_star_3;
    Bitmap poison_star_4;
    Bitmap poison_star_5;
    Bitmap poison_star_6;
    Bitmap poison_star_7;
    Bitmap poison_star_8;
    Bitmap poison_star_9;
    Bitmap poison_star_10;
    Bitmap poison_star_11;
    Bitmap poison_star_12;
    Bitmap poison_star_13;
    Bitmap poison_star_14;
    Bitmap poison_star_15;

    private List<Bitmap> shipImages = new ArrayList<Bitmap>(2);
    Bitmap ship_image_1_1;
    Bitmap ship_image_1_2;

    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;

    private int MAX_ROCKS = 4;

    private SoundPool sounds;
    private int burstSound;
    private int blastfireSound;
    private int boostersSound;
    int stopBoosterSound = 0;

    float[] rotationMatrix = new float[16];
    float[] outRotationMatrix = new float[16];


    public GameView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        this.setOnTouchListener(this);

        mainThread = new MainThread(holder, this);
        setFocusable(true);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        burstSound = sounds.load(context, R.raw.burst, 1);
        blastfireSound = sounds.load(context, R.raw.blastfire, 1);
        boostersSound = sounds.load(context, R.raw.boosters, 1);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        init();
        mainThread.setRunning(true);
        mainThread.start();
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.starryskies_0);
        image = Bitmap.createScaledBitmap(image, (int) SCREEN_WIDTH * 4, (int) SCREEN_HEIGHT, false);
        background = new Background(image);
        background.setDisplacement(-5, 0);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retryStop = true;
        while (retryStop) {
            try {
                mainThread.setRunning(false);
                mainThread.join();
                retryStop = false;
            } catch (Exception e) {
            }
        }
        sensorManager.unregisterListener(this);
    }

    public void update() {
        if(tillWake < 0) {
                background.update();

                myShip.update();
                for (int idx = 0; idx < myShip.missiles.size(); idx++){
                    myShip.missiles.get(idx).update();
                }
        
                for (int idx = 0; idx < asteroids.size(); idx++) {
                    asteroids.get(idx).update();
                }
        
        
                if (asteroids.size() < MAX_ROCKS) {
                    asteroids.add(spawnAsteroid());
                }
        
        
                    asteroidsClone = new ArrayList<Sprite>();
                    for (int idx = 0; idx < asteroids.size(); idx++) {
                        if (collision(myShip, asteroids.get(idx))) {
                            asteroids.get(idx).animated = true;
                            asteroids.get(idx).lifespan = ASTEROID_LIFESPAN;
                            asteroidsExploding.add(asteroids.get(idx));
                            sounds.play(burstSound, 1, 1, 1, 0, 0);
                        }
                        else{
                            asteroidsClone.add(asteroids.get(idx));
                        }
                    }
                    asteroids = asteroidsClone;

                    group_collision(myShip.missiles, asteroids);
        
                    asteroidsExplodingClone = new ArrayList<Sprite>();
                    for (int idx = 0; idx < asteroidsExploding.size(); idx++) {
                        asteroidsExploding.get(idx).update();
                        if (asteroidsExploding.get(idx).age < ASTEROID_LIFESPAN){
                            asteroidsExplodingClone.add(asteroidsExploding.get(idx));
                        }
                    }
                    asteroidsExploding = asteroidsExplodingClone;
        
                    tillWake = sleepTime;
        }
        tillWake -= 1;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            background.draw(canvas);
            myShip.draw(canvas);

            for (int idx = 0; idx < asteroids.size(); idx++) {
                asteroids.get(idx).draw(canvas);
            }

            for (int idx = 0; idx < asteroidsExploding.size(); idx++) {
                asteroidsExploding.get(idx).draw(canvas);
            }

            for (int idx = 0; idx < myShip.missiles.size(); idx++){
                myShip.missiles.get(idx).draw(canvas);
            }
        }
    }

    public void init() {

        missileImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.missile_0_1));
        missileImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.missile_0_2));

        missileInfo = new ImageInfo(new float[]{0, 0}, new float[]{missileImages.get(0).getWidth(),
                missileImages.get(0).getHeight()},
                (float) (missileImages.get(0).getWidth() / 2), 20, missileImages);

        shipImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.ship_still));
        shipImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.ship_thrusters));

        for(int idx = 0; idx < 2; idx++){
            shipImages.set(idx, Bitmap.createScaledBitmap(shipImages.get(idx),
                    (int) SCREEN_WIDTH  / 8, (int) SCREEN_HEIGHT / 8, false));
        }

        shipInfo = new ImageInfo(new float[]{0, 0},
                new float[]{shipImages.get(0).getWidth(), shipImages.get(0).getHeight()},
                (float) (shipImages.get(0).getWidth() / 1.732), Float.POSITIVE_INFINITY, shipImages);

        myShip = new Ship(new float[]{SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2},
                new float[]{0, 0}, 0, 0, shipImages.get(0), shipInfo, sounds, SCREEN_WIDTH, SCREEN_HEIGHT,
                blastfireSound, missileImage, missileInfo);

        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_0));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_1));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_2));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_3));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_4));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_5));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_6));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_7));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_8));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_9));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_10));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_11));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_12));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_13));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_14));
        asteroidImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.poison_star_15));

        for(int idx = 0; idx < 16; idx++){
            asteroidImages.set(idx, Bitmap.createScaledBitmap(asteroidImages.get(idx), (int) SCREEN_WIDTH  / 24,
                    (int) SCREEN_HEIGHT / 12, false));
        }

        asteroidInfo = new ImageInfo(new float[]{0, 0},
                new float[]{asteroidImages.get(0).getWidth(), asteroidImages.get(0).getHeight()},
                (float) (asteroidImages.get(0).getWidth() / 2.3), Float.POSITIVE_INFINITY, asteroidImages);

        asteroids.add(spawnAsteroid());
        myShip.shoot();
    }

    public Sprite spawnAsteroid() {
        Random rand = new Random();

        int posX = rand.nextInt((int) SCREEN_WIDTH);
        int posY = rand.nextInt((int) SCREEN_HEIGHT);
        float[] pos = new float[]{posX, posY};
        while (distance(pos, myShip.pos) / 2 < shipInfo.radius + asteroidInfo.radius) {
            posX = rand.nextInt((int) SCREEN_WIDTH);
            posY = rand.nextInt((int) SCREEN_HEIGHT);
            pos = new float[]{posX, posY};
        }

        int velX = rand.nextInt(15) - 7;
        int velY = rand.nextInt(15) - 7;
        int ang = rand.nextInt(360);
        int ang_vel = rand.nextInt(9) - 4;

        float[] vel = new float[]{velX, velY};
        Sprite asteroid = new Sprite(pos, vel, ang, ang_vel, asteroidImages.get(0),
                asteroidInfo, null, SCREEN_WIDTH, SCREEN_HEIGHT);
        return asteroid;
    }


    private boolean collision(Sprite object, Sprite other_object) {
        return distance(object.pos, other_object.pos) < object.radius + other_object.radius;
    }

    private void group_collision(List<Sprite> missiles, List<Sprite> asteroids) {
        asteroidsClone = new ArrayList<Sprite>();
        for (int idx = 0; idx < asteroids.size(); idx++) {
            boolean addMe = true;
            innerloop:
            for (int idx2 = 0; idx2 < missiles.size(); idx2++) {
                if (collision(asteroids.get(idx), missiles.get(idx2))) {
                    // System.out.println("Collision detected");
                    addMe = false;
                    asteroids.get(idx).animated = true;
                    asteroids.get(idx).lifespan = ASTEROID_LIFESPAN;
                    asteroidsExploding.add(asteroids.get(idx));
                    sounds.play(burstSound, 1, 1, 10, 0, 0);
                    break innerloop;
                }
            }
            if (addMe) {
                asteroidsClone.add(asteroids.get(idx));
            }
        }
        this.asteroids = asteroidsClone;
    }

    private float distance(float[] pos_1, float[] pos_2) {
        float dx = pos_2[0] - pos_1[0];
        float dy = pos_2[1] - pos_1[1];
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        if (event.getPointerCount() == 1) {

            if (event.getX() > SCREEN_WIDTH / 2) {
                myShip.shoot();
            }

            else {
                if (action == event.ACTION_DOWN) {
                    if(!myShip.isThrusting()){
                        stopBoosterSound = sounds.play(boostersSound, 1, 1, 10, -1, 0);
                        myShip.setThruster(true);
                    }


                } else if (action == event.ACTION_UP) {
                    myShip.setThruster(false);
                    sounds.stop(stopBoosterSound);
                }
            }
        }

        else{

            myShip.shoot();


                if (action == event.ACTION_DOWN) {
                    if(!myShip.isThrusting()){
                        stopBoosterSound = sounds.play(boostersSound, 1, 1, 10, -1, 0);
                        myShip.setThruster(true);
                    }
                }
                else if (action == event.ACTION_UP) {
                    myShip.setThruster(false);
                    sounds.stop(stopBoosterSound);
                }
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] orientationValues = new float[16];
        float currentOrientation;
        float currentInclination;

        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_Y,
                SensorManager.AXIS_MINUS_X, outRotationMatrix);

        SensorManager.getOrientation(outRotationMatrix, orientationValues);

        rotationMatrix = outRotationMatrix;

        currentOrientation = (float) (Math.toDegrees(orientationValues[0])); //Azimuth; (Degrees);
        currentInclination = (float) (Math.toDegrees(orientationValues[1])); //         (Degrees);

        try {
            if (currentOrientation > 3 && currentOrientation < 45) {
                // System.out.println("RIGHT");
                if (myShip.ang_vel < myShip.MAX_ANG_VEL) {
                    myShip.ang_vel += (float) 1 / 25;
                }
            } else if (currentOrientation < -3 && currentOrientation > -45) {
                // System.out.println("LEFT");
                if (myShip.ang_vel > -myShip.MAX_ANG_VEL) {
                    myShip.ang_vel -= (float) 1 / 25;
                }
            } else {
                // System.out.println("STRAIGHT");
                myShip.ang_vel = 0;
            }
        }
        catch (NullPointerException e) {
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
