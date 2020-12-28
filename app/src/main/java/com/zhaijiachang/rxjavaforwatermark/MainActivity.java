package com.zhaijiachang.rxjavaforwatermark;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.imageView);

        String TAG = "findViewById";

        Observable.just("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1078861629,3747050294&fm=26&gp=0.jpg")
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String mUrlPath) throws Exception {
                        URL mURL = new URL(mUrlPath);
                        HttpURLConnection mConnection = (HttpURLConnection) mURL.openConnection();
                        InputStream mInputStream = mConnection.getInputStream();
                        Bitmap mBitmap = BitmapFactory.decodeStream(mInputStream);
                        Log.e(TAG, "map1 : "+" --- "+Thread.currentThread().getName().toString());
                        return mBitmap;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(Bitmap mBitmap) throws Exception {
                        Log.e(TAG, "map2 : "+" --- "+Thread.currentThread().getName().toString());
                        return addConverText(mBitmap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap mBitmap) throws Exception {
                        mImageView.setImageBitmap(mBitmap);
                        Log.e(TAG, "accept : "+" --- "+Thread.currentThread().getName().toString());
                    }
                });


    }

    private Bitmap addConverText(Bitmap mBitmap) {
        int wid = mBitmap.getWidth();
        int hei = mBitmap.getHeight();
        // 画图片和文字
        Bitmap mBmp = Bitmap.createBitmap(wid,hei, Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBmp);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(26);
        mPaint.setAntiAlias(true);
        mCanvas.drawBitmap(mBitmap,0,0,mPaint);
        mCanvas.drawText("你不是真正的快乐",wid/10,hei/5,mPaint);
        return mBmp;
    }
}