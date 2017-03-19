package com.example.cieo233.notetest;

/**
 * Created by Administrator on 2017/3/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*定义一个画矩形框的类*/
public class DrawView extends SurfaceView implements SurfaceHolder.Callback{

    protected SurfaceHolder sh;
    private int mWidth;
    private int mHeight;
    public DrawView(Context context,AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        sh = getHolder();
        sh.addCallback(this);
        sh.setFormat(PixelFormat.TRANSPARENT);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int w, int h) {
        // TODO Auto-generated method stub
        mWidth = w;
        mHeight = h;
    }

    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        // TODO Auto-generated method stub

    }
    public void drawLine(int []point)
    {
        if (sh.isCreating()){
            return;
        }
        Canvas canvas = sh.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.TRANSPARENT);
        Paint p = new Paint();
        p.setAntiAlias(true);

        p.setStrokeWidth((float) 20);              //设置线宽
        p.setColor(Color.RED);
        p.setStyle(Style.FILL_AND_STROKE);
//        canvas.drawCircle(point[1],(float)point[0] / 1920 * 1692,30,p);
//        p.setColor(Color.GREEN);
//        canvas.drawCircle(point[3],(float)point[2] / 1920 * 1692,30,p);
//        p.setColor(Color.BLUE);
//        canvas.drawCircle(point[5],(float)point[4] / 1920 * 1692,30,p);
//        p.setColor(Color.YELLOW);
//        canvas.drawCircle(point[7],(float)point[6] / 1920 * 1692,30,p);
        p.setColor(Color.WHITE);
        p.setStrokeJoin(Paint.Join.MITER);
        p.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(point[3],(float)point[2] / 1920 * 1692, point[1],(float)point[0] / 1920 * 1692, p);
        canvas.drawLine(point[3],(float)point[2] / 1920 * 1692, point[7],(float)point[6] / 1920 * 1692, p);
        canvas.drawLine(point[5],(float)point[4] / 1920 * 1692, point[1],(float)point[0] / 1920 * 1692, p);
        canvas.drawLine(point[5],(float)point[4] / 1920 * 1692,point[7],(float)point[6] / 1920 * 1692,p);
        sh.unlockCanvasAndPost(canvas);
    }

}