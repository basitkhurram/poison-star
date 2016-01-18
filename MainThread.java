package com.example.basit.myapplication;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Basit on 28-Dec-15.
 */
public class MainThread extends Thread{
    private int FPS = 60;
    private Thread thread;
    private SurfaceHolder holder;
    private GameView gv;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder holder, GameView gv){
        super();
        this.holder = holder;
        this.gv = gv;
    }

    @Override
    public void run() {
        long startTime;
        long timeNow;
        long waitTime;
        int frames = 0;
        long targetDuration = 1000000000 / FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.holder.lockCanvas();
                synchronized (holder) {
                    this.gv.update();
                    this.gv.draw(canvas);
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                    }

                }
            }

            waitTime = ((startTime + targetDuration) - System.nanoTime()) / 1000000;
            // System.out.println(waitTime);
            if (waitTime > 0) {
                try {
                    this.sleep(waitTime);
                } catch (Exception e) {
                }
            }

        }
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}

