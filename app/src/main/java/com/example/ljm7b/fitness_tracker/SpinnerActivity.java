package com.example.ljm7b.fitness_tracker;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by ljm7b on 10/14/2015.
 */

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Log.d(parent.getItemAtPosition(pos).toString(), "SPINNER INFO");
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}