package com.example.ljm7b.fitness_tracker;

/**
 * Created by ljm7b on 10/31/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;

/**
 * Created by vamsikrishna on 12-Feb-15.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash);

        ImageView image = (ImageView)findViewById(R.id.imageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        image.startAnimation(animation);

        WebView web = (WebView) findViewById(R.id.webView);
        web.setBackgroundColor(Color.TRANSPARENT); //for gif without background
        web.loadUrl("file:///android_asset/html/giphy.html");

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(4700);


                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreen.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}