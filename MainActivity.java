package com.example.basit.myapplication;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity{

    GameView gv;
    float touchX;
    float touchY;
    MediaPlayer backgroundMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gv = new GameView(this);
        setContentView(gv);
        // gv.setOnTouchListener(this);

        backgroundMusic = MediaPlayer.create(this, R.raw.background);
        backgroundMusic.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        setContentView(new GameView(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundMusic = MediaPlayer.create(this, R.raw.background);
        backgroundMusic.start();
        backgroundMusic.setLooping(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.stop();
        backgroundMusic.release();
    }


/*
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        return false;
    }
    */
}
