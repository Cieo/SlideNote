package com.example.cieo233.notetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Cieo233 on 3/19/2017.
 */

public class TessTwo {
    private static final String TAG = "TessTwo";
    private static TessTwo instance;
    private static String TESSDATA;
    private TessBaseAPI tessBaseAPI;

    private TessTwo() {
    }

    public void doOCR(String imagePath, String language) {
        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(TESSDATA, language);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap = convertGreyImg(bitmap);
        bitmap = scaleImage(bitmap, 5);
        tessBaseAPI.setImage(bitmap);
        Log.e(TAG, "doOCR: "+tessBaseAPI.getUTF8Text());
    }

    public static void prepareTessData(Context context, String toPath) {
        TESSDATA = toPath;
        String fileList[];
        try {
            File file = new File(toPath + "tessdata/");
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.e(TAG, "prepareTessData: create dir fail");
                    Log.e(TAG, "prepareTessData: "+file.getAbsolutePath() );
                }
            }
            fileList = context.getAssets().list("tessdata");
            for (String path : fileList) {
                File newFile = new File(toPath + "tessdata/" + path);
                if (!newFile.exists()) {
                    InputStream inputStream = context.getAssets().open("tessdata/" + path);
                    OutputStream outputStream = new FileOutputStream(newFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                    inputStream.close();
                    outputStream.close();

                }
                Log.e(TAG, "prepareTessData: " + path);
                Log.e(TAG, "prepareTessData: " + newFile.exists());
                Log.e(TAG, "prepareTessData: " + newFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static synchronized TessTwo getInstance() {
        if (instance == null) {
            if (instance != null) {
                return instance;
            }
            instance = new TessTwo();
        }
        return instance;
    }

    public Bitmap convertGreyImg(Bitmap image) {
        //得到图像的宽度和长度
        int width = image.getWidth();
        int height = image.getHeight();
        //创建线性拉升灰度图像
        Bitmap linegray = null;
        linegray = image.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环对图像的像素进行处理
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到每点的像素值
                int col = image.getPixel(i, j);
                int alpha = col & 0xFF000000;
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 增加了图像的亮度
                red = (int) (1.1 * red + 30);
                green = (int) (1.1 * green + 30);
                blue = (int) (1.1 * blue + 30);
                //对图像像素越界进行处理
                if (red >= 255)
                {
                    red = 255;
                }

                if (green >= 255) {
                    green = 255;
                }

                if (blue >= 255) {
                    blue = 255;
                }
                // 新的ARGB
                int newColor = alpha | (red << 16) | (green << 8) | blue;
                //设置新图像的RGB值
                linegray.setPixel(i, j, newColor);
            }
        }
        return linegray;
    }

    private static Bitmap scaleImage(Bitmap bitmap, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }
}
