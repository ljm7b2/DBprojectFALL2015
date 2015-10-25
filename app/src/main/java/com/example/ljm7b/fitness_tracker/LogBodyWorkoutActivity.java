package com.example.ljm7b.fitness_tracker;

/**
 * Created by ljm7b on 10/10/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class LogBodyWorkoutActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private Spinner bodyWorkoutSpinner;

    private static String url_log_brain_workout = "http://fall2015db.asuscomm.com/FitnessDB/logBodyWorkout.php";

    JSONParser2 jsonParser2 = new JSONParser2();

    EditText timeSpent;

    private ArrayList<String> brainSpinnerWorkoutList;

    // Creating JSON Parser object
    JSONParser2 jParser2 = new JSONParser2();

    ArrayList<HashMap<String, String>> workoutsList;

    // url to get all workouts list
    private static String url_all_workouts = "http://fall2015db.asuscomm.com/FitnessDB/getAllBodyWorkouts.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_WORKOUTS = "body_workouts";
    private static final String TAG_EXERCISE_NAME = "Workout_Name";
    public static List<String> lables = new ArrayList<String>();
    public static ArrayAdapter<String> spinnerAdapter;

    //workouts JSONArray
    JSONArray workouts = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_brain_workout);

        // Hashmap for Gender spinner
        workoutsList = new ArrayList<HashMap<String, String>>();

        timeSpent = (EditText) findViewById(R.id.inputTime);

        // Loading workouts in Background Thread


        //Populate spinner from database
        lables = new ArrayList<String>();

        new LoadAllWorkouts().execute();


        Log.d("Workout list size", String.valueOf(workoutsList.size()));

        bodyWorkoutSpinner = (Spinner) findViewById(R.id.inputBrainWorkout);
        AddSpinnerListener();

        // Create button
        Button btnLogBrainWorkout = (Button) findViewById(R.id.btnLogBrainWorkout);
        btnLogBrainWorkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                String userID = new SessionVariables().getUserID();
                String bodyWorkoutName = bodyWorkoutSpinner.getSelectedItem().toString();
                String workoutTime = timeSpent.getText().toString();


                if (workoutTime.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Complete All Data Fields!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    return;
                }

                new LogBrainWorkout().execute(userID, bodyWorkoutName, workoutTime);
            }
        });
    }


    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void AddSpinnerListener(){
        bodyWorkoutSpinner.setOnItemSelectedListener(new SpinnerActivity());
    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllWorkouts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LogBodyWorkoutActivity.this);
            pDialog.setMessage("Loading workouts. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All workouts from url
         * */
        @Override
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();
            // getting JSON string from URL

            JSONObject json = jParser2.makeHttpRequest(url_all_workouts, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All workouts to view: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // workouts found
                    // Getting Array of workouts
                    workouts = json.getJSONArray(TAG_WORKOUTS);

                    // looping through All workouts
                    for (int i = 0; i < workouts.length(); i++) {
                        JSONObject c = workouts.getJSONObject(i);

                        // Storing each json item in variable
                        String exercise_name = c.getString(TAG_EXERCISE_NAME);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_EXERCISE_NAME, exercise_name);

                        // adding HashList to ArrayList
                        workoutsList.add(map);
                    }
                } else {
                    // no workouts found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewWorkoutActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all workouts
            pDialog.dismiss();

            for (int i = 0; i < workoutsList.size(); i++) {

                Log.d("workout list item", workoutsList.get(i).get(TAG_EXERCISE_NAME));
                lables.add(workoutsList.get(i).get(TAG_EXERCISE_NAME));
            }
            buildSpinner(lables);

        }

    }

    void buildSpinner(List<String> data){
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bodyWorkoutSpinner.setAdapter(spinnerAdapter);
    }



    /**
     * Background Async Task to Create new product
     * */
    class LogBrainWorkout extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LogBodyWorkoutActivity.this);
            pDialog.setMessage("Logging Workout..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating workout
         * */
        @Override
        protected String doInBackground(String... args) {

            String userID = args[0];
            String bodyWorkoutName = args[1];
            String workoutTime = args[2];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userID", userID));
            params.add(new BasicNameValuePair("bodyWorkoutName", bodyWorkoutName));
            params.add(new BasicNameValuePair("workoutTime", workoutTime));

            JSONObject json = new JSONObject();
            try {
                // getting JSON Object
                // Note that create product url accepts POST method
                json = jsonParser2.makeHttpRequest(url_log_brain_workout,
                        "POST", params);

                // check log cat from response
                Log.d("Create Response", json.toString());
            }
            catch(Exception e){
                Intent i = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(i);
                finish();
                //TODO: ADD MESSAGE TO THE USER ABOUT A FAILURE TO ACCESS DB
                return null;
            }

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create workout
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}

