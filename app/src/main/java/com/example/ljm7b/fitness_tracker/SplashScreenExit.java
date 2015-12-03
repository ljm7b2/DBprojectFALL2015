package com.example.ljm7b.fitness_tracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by ljm7b on 12/2/2015.
 */
public class SplashScreenExit extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreenexit);

        ImageView image = (ImageView)findViewById(R.id.imageView2);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        image.startAnimation(animation);


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1600);


                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreenExit.this,LoginActivity.class);
                    startActivity(intent);

                }
            }
        };
        timerThread.start();
    }

}

