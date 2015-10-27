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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private Spinner genderSpinner;
    private DatePicker dobPicker;
    private String dateDelimeter = "-";
    JSONParser2 jsonParser2 = new JSONParser2();
    EditText inputFirstName;
    EditText inputLastName;
    EditText inputUserName;
    EditText inputPassword;
    EditText inputWeight;
    EditText inputHeight;


    // url to create new product
    private static String url_register_user = "http://fall2015db.asuscomm.com/FitnessDB/register.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERID = "user_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        genderSpinner = (Spinner) findViewById(R.id.inputGender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        AddSpinnerListener();

        dobPicker = (DatePicker) findViewById(R.id.inputDOB);

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
                String sex = genderSpinner.getSelectedItem().toString();
                String DOB = String.valueOf(dobPicker.getYear()) + dateDelimeter + String.valueOf(checkDigit(dobPicker.getMonth())) + dateDelimeter + String.valueOf(checkDigit(dobPicker.getDayOfMonth()));


                if (nameFirst.equals("") || nameLast.equals("") || username.equals("") || password.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Complete All Data Fields!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    return;
                }

                new RegisterUser().execute(nameFirst, nameLast, username, password, weight, height, sex, DOB);
            }
        });
    }
    public String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }


    public void AddSpinnerListener(){
        genderSpinner.setOnItemSelectedListener(new SpinnerActivity());
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
            String gender = args[6];
            String DOB = args[7];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nameFirst", nameFirst));
            params.add(new BasicNameValuePair("nameLast", nameLast));
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("weight", weight));
            params.add(new BasicNameValuePair("height", height));
            params.add(new BasicNameValuePair("DOB", DOB));
            Log.d("date of birth", DOB);
            if (gender.equals("Male")) {
                params.add(new BasicNameValuePair("gender", "0"));
            } else {
                params.add(new BasicNameValuePair("gender", "1"));
            }
            JSONObject json = new JSONObject();
            try {
            // getting JSON Object
            // Note that create product url accepts POST method
                json = jsonParser2.makeHttpRequest(url_register_user,
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
                    new SessionVariables().setUserID(json.getString(TAG_USERID));
                    Intent i = new Intent(getApplicationContext(), SetGoalsActivity.class);
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