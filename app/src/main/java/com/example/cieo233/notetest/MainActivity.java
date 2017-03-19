package com.example.cieo233.notetest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;

/**
 * Created by cky on 2017/2/11.
 */
public class MainActivity extends Activity implements Camera.PreviewCallback{
    private CameraPreview mPreview;
    private DrawView mDrawview;
    private int LayoutWidth,LayoutHeight,PreviewWidth,PreviewHeight;
    int [] result = {0,0,0,0,0,0,0,0};
    PptTask mPptTask;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Btn_TakePicture = (Button) findViewById(R.id.Btn_TakePicture);
        Btn_TakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreview.takePicture(result);
            }
        });
        final LinearLayout Preview = (LinearLayout) findViewById(R.id.Preview);
        ViewTreeObserver vto = Preview.getViewTreeObserver(); //获取屏幕预览宽度与高度
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                LayoutWidth = Preview.getMeasuredWidth();
                LayoutHeight = Preview.getMeasuredHeight();
              //  Log.i("debug1",LayoutWidth + " " + LayoutHeight);
                return true;
            }
        });
    }
    private void InitPreview() { //初始化预览
        mPreview = (CameraPreview) findViewById(R.id.CameraPreview);
        mDrawview = (DrawView) findViewById(R.id.DrawView);
        mDrawview.setZOrderOnTop(true);
        mPreview.mCamera.setPreviewCallback(this);

    }
    protected  void onResume(){
        super.onResume();
        if(mPreview == null)
            InitPreview();
    }
    protected  void onPause(){
        super.onPause();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if(null != mPptTask){
            switch(mPptTask.getStatus()){
                case RUNNING:
                    return;
                case PENDING:
                    mPptTask.cancel(false);
                    break;
            }
        }

        mPptTask = new PptTask(data);
        mPptTask.execute((Void)null);
    }

    private class PptTask extends AsyncTask<Void, Void, Void> {

        private byte[] mData;
        //构造函数
        PptTask(byte[] data){
            this.mData = data;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Camera.Size size = mPreview.mCamera.getParameters().getPreviewSize(); //获取预览大小
            PreviewWidth = size.width;  //宽度
            PreviewHeight = size.height;
            Log.i("debug1",PreviewWidth + " " + PreviewHeight);
           // Log.i("debug1","识别");
            final YuvImage image = new YuvImage(mData, ImageFormat.NV21, PreviewWidth, PreviewHeight, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream(mData.length);
            if(!image.compressToJpeg(new Rect(0, 0, PreviewWidth, PreviewHeight), 100, os)){
                return null;
            }
            byte[] tmp = os.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0,tmp.length);
            PPtdetection(bmp);
            return null;
        }
        void  trans(int[] point,int[] data){
            for(int i = 0;i < 4;i ++){
                point[i * 2] = data[i * 2];
                point[i * 2 + 1] = PreviewHeight - data[i * 2 +1];
            }
        }
        public int[] PPtdetection(Bitmap bitmap){

            int w = bitmap.getWidth(), h = bitmap.getHeight();
            int[] pix = new int[w * h];
          //  Log.i("kaychen",w + "," + h);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
            int []solvepoint = {0,0,0,0,0,0,0,0};
            OpenCVHelper.Checkedge(pix,result,w,h);
            trans(solvepoint,result);

            for(int i = 0;i < 4;++i){
                Log.i("kaychen",result[i* 2] + "," + result[i * 2+1]  + " " + solvepoint[i* 2] + "," + solvepoint[i * 2+1]);
            }
            for(int i =0 ;i < 8;++i){ //画框
                if(result[i] != 0){
                    mDrawview.drawLine(solvepoint);
                }
            }
            return result;
        }
    }
}