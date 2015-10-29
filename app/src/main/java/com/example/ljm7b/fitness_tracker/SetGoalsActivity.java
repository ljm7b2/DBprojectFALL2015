package com.example.ljm7b.fitness_tracker;

/**
 * Created by ljm7b on 10/10/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SetGoalsActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    JSONParser2 jsonParser2 = new JSONParser2();
    EditText inputBrainGoal;
    EditText inputBodyGoal;

    // url to create new product
    private static String url_set_user_goals = "http://fall2015db.asuscomm.com/FitnessDB/setUserGoals.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_goals);

        // Edit Text
        inputBrainGoal = (EditText) findViewById(R.id.inputBrainGoal);
        inputBodyGoal = (EditText) findViewById(R.id.inputBodyGoal);

        if (new SessionVariables().isDateExpired()){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.goal_alert_message)
                    .setTitle(R.string.goal_alert_title);

            builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
            new SessionVariables().setDateExpired(false);
        }

        // Create button
        Button btnSetUserGoals = (Button) findViewById(R.id.btnSetGoal);

        // button click event
        btnSetUserGoals.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                String brainGaol = inputBrainGoal.getText().toString();
                String bodyGoal = inputBodyGoal.getText().toString();

                if (brainGaol.equals("") || bodyGoal.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Complete All Data Fields!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    return;
                }

                new SetUserGoals().execute(brainGaol, bodyGoal);
            }
        });
    }
    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }

    /**
     * Background Async Task to Create new product
     * */
    class SetUserGoals extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SetGoalsActivity.this);
            pDialog.setMessage("Setting Goals..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Setting Goals
         * */
        @Override
        protected String doInBackground(String... args) {

            String brainGoal = args[0];
            String bodyGoal = args[1];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userID", new SessionVariables().getUserID()));
            params.add(new BasicNameValuePair("brainGoal", brainGoal));
            params.add(new BasicNameValuePair("bodyGoal", bodyGoal));

            JSONObject json = new JSONObject();
            try {
            // getting JSON Object
            // Note that create product url accepts POST method
                json = jsonParser2.makeHttpRequest(url_set_user_goals,
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