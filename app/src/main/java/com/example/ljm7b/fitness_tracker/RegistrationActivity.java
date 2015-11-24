package com.example.ljm7b.fitness_tracker;

/**
 * Created by ljm7b on 10/10/2015.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RegistrationActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    private Spinner genderSpinner;
    private Spinner monthSpinner;
    private Spinner daySpinner;
    private Spinner yearSpinner;
    private int[] Thirty = {3, 5, 8, 10};
    private List<String> daysList;
    public static ArrayAdapter<String> monthsSpinnerAdapter;
    public static ArrayAdapter<String> daySpinnerAdapter;
    public static ArrayAdapter<String> yearSpinnerAdapter;
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
        String[] daysArray = getResources().getStringArray(R.array.days);
        daysList = new LinkedList<String>(Arrays.asList(daysArray));

        monthSpinner = (Spinner) findViewById(R.id.monthsSpinner);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);

        buildSpinner();

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
                String year = yearSpinner.getSelectedItem().toString();
                String day = daySpinner.getSelectedItem().toString();
                int month = monthSpinner.getSelectedItemPosition() + 1;
                String DOB = year + "-" + String.valueOf(month) + "-" + day;


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
                Log.d("Create Response", json.toString());
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();

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

                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
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


    void buildSpinner(){
        String[] monthsArray = getResources().getStringArray(R.array.months);
        monthsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, monthsArray )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView ) view).setGravity(Gravity.CENTER);
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                if (position % 2 == 0) { // we're on an even row
                    view.setBackgroundColor(Color.parseColor("#00FFFF"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF66FF"));
                }
                return view;
            }

        };
        monthSpinner.setAdapter(monthsSpinnerAdapter);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String[] daysArray = getResources().getStringArray(R.array.days);
                daysList = new LinkedList<String>(Arrays.asList(daysArray));
                if(position == 1){
                    daysList.remove(30);
                    daysList.remove(29);
                }
                else if(Arrays.binarySearch(Thirty,position) > 0){
                    daysList.remove(30);
                }
                buildDaysSpinner(daysList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        daySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, daysList )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView ) view).setGravity(Gravity.CENTER);
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                if (position % 2 == 0) { // we're on an even row
                    view.setBackgroundColor(Color.parseColor("#00FFFF"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF66FF"));
                }
                return view;
            }

        };
        daySpinner.setAdapter(daySpinnerAdapter);

        String[] yearsArray = getResources().getStringArray(R.array.years_array);
        yearSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, yearsArray )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView ) view).setGravity(Gravity.CENTER);
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                if (position % 2 == 0) { // we're on an even row
                    view.setBackgroundColor(Color.parseColor("#00FFFF"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF66FF"));
                }
                return view;
            }

        };
        yearSpinner.setAdapter(yearSpinnerAdapter);


    }

    void buildDaysSpinner(List<String> daysArray){
        //String[] daysArray = getResources().getStringArray(R.array.days);
        daySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, daysArray )
        {
            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setTextColor(Color.BLACK);

                return v;

            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView ) view).setGravity(Gravity.CENTER);
                ((TextView) view).setTextColor(Color.WHITE);
                ((TextView) view).setTextSize(20);
                if (position % 2 == 0) { // we're on an even row
                    view.setBackgroundColor(Color.parseColor("#00FFFF"));
                } else {
                    view.setBackgroundColor(Color.parseColor("#FF66FF"));
                }
                return view;
            }

        };
        daySpinner.setAdapter(daySpinnerAdapter);

    }
}