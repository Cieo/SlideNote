package com.example.cieo233.notetest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/18.
 */

public class OcrActivity  extends Activity {
    ImageView Ocrimgae;
    int imagewidth,imageheight;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        Ocrimgae = (ImageView) findViewById(R.id.ocr_image);
        Intent intent=getIntent();
        if(intent !=null)
        {
            Uri uri = getIntent().getData();
            Ocrimgae.setImageURI(uri);
        }
        imagewidth = Ocrimgae.getWidth();
        imageheight = Ocrimgae.getHeight();
    }
}
