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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser2 jsonParser2 = new JSONParser2();
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUserName;
    EditText inputPassword;
    EditText inputWeight;
    EditText inputHeight;
    //EditText inputGender;


    // url to create new product
    private static String url_create_workout = "http://fall2015db.asuscomm.com/FitnessDB/register.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

//        Spinner spinner = (Spinner) findViewById(R.id.inputGender);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.gender_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        // Edit Text
        inputFirstName = (EditText) findViewById(R.id.inputFirstName);
        inputLastName = (EditText) findViewById(R.id.inputLastName);
        inputUserName = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputWeight = (EditText) findViewById(R.id.inputWeight);
        inputHeight = (EditText) findViewById(R.id.inputHeight);

        // Create button
        Button btnRegisterUser = (Button) findViewById(R.id.btnRegister);

        // button click event
        btnRegisterUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                String nameFirst = inputFirstName.getText().toString();
                String nameLast = inputLastName.getText().toString();
                String username = inputUserName.getText().toString();
                String password = inputPassword.getText().toString();
                String weight = inputWeight.getText().toString();
                String height = inputHeight.getText().toString();

                if (nameFirst.equals("") || nameLast.equals("") || username.equals("") || password.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Complete All Data Fields!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    return;
                }

                new RegisterUser().execute(nameFirst, nameLast, username, password, weight, height);
            }
        });
    }


    /**
     * Background Async Task to Create new product
     * */
    class RegisterUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Register..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating workout
         * */
        @Override
        protected String doInBackground(String... args) {

            String nameFirst = args[0];
            String nameLast = args[1];
            String username = args[2];
            String password = args[3];
            String weight = args[4];
            String height = args[5];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nameFirst", nameFirst));
            params.add(new BasicNameValuePair("nameLast", nameLast));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("weight", weight));
            params.add(new BasicNameValuePair("height", height));
            //params.add(new BasicNameValuePair("gender", "M"));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser2.makeHttpRequest(url_create_workout,
                    "POST", params);

            // check log cat from response
            Log.d("Create Response", json.toString());

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