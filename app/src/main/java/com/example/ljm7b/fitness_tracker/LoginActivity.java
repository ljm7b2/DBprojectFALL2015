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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;
    SessionVariables Global= new SessionVariables();
    JSONParser2 jsonParser2 = new JSONParser2();
    EditText inputUserName;
    EditText inputPassword;


    // url to create new product
    private static String url_login_user = "http://fall2015db.asuscomm.com/FitnessDB/login.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USERID = "user_id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Edit Text
        inputUserName = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        // Create button
        Button btnLoginUser = (Button) findViewById(R.id.btnLogin);

        // button click event
        btnLoginUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

                String username = inputUserName.getText().toString();
                String password = inputPassword.getText().toString();

                if (username.equals("") || password.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please Complete All Data Fields!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
                    toast.show();
                    return;
                }

                new RegisterUser().execute(username, password);
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
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating workout
         * */
        @Override
        protected String doInBackground(String... args) {

            String username = args[0];
            String password = args[1];

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = new JSONObject();
            // getting JSON Object
            // Note that create product url accepts POST method
            try {
                json = jsonParser2.makeHttpRequest(url_login_user,
                        "POST", params);

                // check log cat from response
                Log.d("Create Response", json.toString());
            }
            catch(Exception e) {
                Intent i = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(i);
                finish();
                return null;
            }

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Global.setUserID(json.getString(TAG_USERID));
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