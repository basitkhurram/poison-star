package com.example.basit.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basit on 29-Dec-15.
 */
public class ImageInfo {
    final float[] topLeft;
    final float[] size;
    final float radius;
    final float lifespan;
    boolean animated;
    int imageFrameColumns;
    int imageFrameRows;
    List<Bitmap> reel = new ArrayList<Bitmap>();

    public ImageInfo(float[] topLeft, float[] size, float radius, float lifespan, List<Bitmap> reel){
        this.topLeft = topLeft;
        this.size = size;
        this.radius = radius;
        this.lifespan = lifespan;
        this.animated = false;
        this.imageFrameRows = 1;
        this.imageFrameColumns = 1;
        this.reel = reel;
    }


    public ImageInfo(float[] topLeft, float[] size, float radius, float lifespan, boolean animated){
        this.topLeft = topLeft;
        this.size = size;
        this.radius = radius;
        this.lifespan = lifespan;
        this.animated = false;
        this.imageFrameRows = 1;
        this.imageFrameColumns = 1;
    }

    public ImageInfo(float[] topLeft, float[] size, float radius, float lifespan, int imageFrameColumns){
        this.topLeft = topLeft;
        this.size = size;
        this.radius = radius;
        this.lifespan = lifespan;
        this.animated = false;
        this.imageFrameRows = 1;
        this.imageFrameColumns = imageFrameColumns;
    }

    public ImageInfo(float[] topLeft, float[] size, float radius, float lifespan,
                     int imageFrameRows, int imageFrameColumns){
        this.topLeft = topLeft;
        this.size = size;
        this.radius = radius;
        this.lifespan = lifespan;
        this.animated = false;
        this.imageFrameRows = imageFrameRows;
        this.imageFrameColumns = imageFrameColumns;
    }
}