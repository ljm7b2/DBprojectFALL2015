package com.example.ljm7b.fitness_tracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity{

    Button btnLogBrainWorkout;
    Button btnLogBodyWorkout;
    Button btnCreateWorkout;
    Button btnVideoWorkout;
    Button btnGetWorkoutLog;
    Button btnShowGraph;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Buttons
        btnLogBrainWorkout = (Button) findViewById(R.id.btnLogBrainWorkout);
        btnLogBodyWorkout = (Button) findViewById(R.id.btnLogBodyWorkout);
        btnCreateWorkout = (Button) findViewById(R.id.btnCreateWorkout);
        btnVideoWorkout = (Button) findViewById(R.id.btnVideoWorkouts);
        btnGetWorkoutLog = (Button) findViewById(R.id.btnGetWorkoutLog);
        btnShowGraph = (Button) findViewById(R.id.btnShowGraph);

        //Globals





        // view products click event
        btnLogBrainWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), LogBrainWorkoutActivity.class);
                startActivity(i);

            }
        });


        // view products click event
        btnLogBodyWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), LogBodyWorkoutActivity.class);
                startActivity(i);

            }
        });
//
//        // view products click event
//        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                // Launching create new product activity
//                Intent i = new Intent(getApplicationContext(), NewWorkoutActivity.class);
//                startActivity(i);
//
//            }
//        });


        // view products click event
        btnVideoWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), VideoMainActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnGetWorkoutLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), AllWorkoutsActivity.class);
                startActivity(i);

            }
        });
        // view products click event
        btnShowGraph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), CombinedChartActivity.class);
                startActivity(i);

            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
