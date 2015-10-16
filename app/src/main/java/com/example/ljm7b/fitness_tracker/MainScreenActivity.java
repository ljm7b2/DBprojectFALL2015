package com.example.ljm7b.fitness_tracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainScreenActivity extends Activity{

    Button btnViewWorkouts;
    Button btnCreateWorkout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // Buttons
        btnViewWorkouts = (Button) findViewById(R.id.btnViewWorkouts);
        btnCreateWorkout = (Button) findViewById(R.id.btnCreateWorkout);

        // view products click event
        btnViewWorkouts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), AllWorkoutsActivity.class);
                startActivity(i);

            }
        });

        // view products click event
        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), NewWorkoutActivity.class);
                startActivity(i);

            }
        });

    }
}
