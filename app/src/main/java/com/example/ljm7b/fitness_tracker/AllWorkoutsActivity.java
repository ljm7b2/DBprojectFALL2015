package com.example.ljm7b.fitness_tracker;

/**
 * Created by ljm7b on 10/10/2015.
 */

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class AllWorkoutsActivity extends ListActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser2 jParser2 = new JSONParser2();

    public static Vector<String> my_vec = new Vector<>();
    ArrayList<HashMap<String, String>> workoutsList;

    ArrayList<HashMap<String, String>> bodyList;

    // url to get all workouts list
    private static String url_all_workouts = "http://fall2015db.asuscomm.com/FitnessDB/getWorkoutLog.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_WORKOUTS = "all_workouts";
   // private static final String TAG_WID = "wid";
    private static final String TAG_NAME = "Workout";
    private static final String TAG_DURATION = "Duration";
    private static final String TAG_TYPE = "Type";
    private static final String TAG_DATE = "Date";
    public static int my_count = -1;
    //workouts JSONArray
    JSONArray workouts = null;

    public String monthsArr[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_workouts);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Hashmap for ListView
        workoutsList = new ArrayList<HashMap<String, String>>();
        bodyList = new ArrayList<HashMap<String, String>>();
        String userID = new SessionVariables().getUserID();
        // Loading workouts in Background Thread
        new LoadAllWorkouts().execute(userID);

        // Get listview
        ListView lv = getListView();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
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
            pDialog = new ProgressDialog(AllWorkoutsActivity.this);
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


            String userID = args[0];
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("User_ID", userID));

            // getting JSON string from URL

            JSONObject json = jParser2.makeHttpRequest(url_all_workouts, "GET", params);

            // Check your log cat for JSON response
            Log.d("All workouts: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // workouts found
                    // Getting Array of workouts
                    Log.d("The JSON STRING",json.toString());
                    workouts = json.getJSONArray(TAG_WORKOUTS);

                    workouts = sortJArray(workouts);

                    // looping through All workouts

                        String dateEarlier = new String();
                        for (int i = workouts.length()-1; i >= 0; i--) {
                            JSONObject c = workouts.getJSONObject(i);

                            if(i==workouts.length()-1){
                                dateEarlier = c.getString(TAG_DATE);
                            }

                            // Storing each json item in variable
                            //String id = c.getString(TAG_WID);
                            String name = c.getString(TAG_NAME);
                            String duration = c.getString(TAG_DURATION);
                            String type = c.getString(TAG_TYPE);
                            String dateCurrent = c.getString(TAG_DATE);

                            if (!dateCurrent.equals(dateEarlier) || i == workouts.length()-1) {
                                dateEarlier = dateCurrent;
                                my_vec.add(String.valueOf(i));

                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                //map.put(TAG_WID, id);
                                map.put(TAG_NAME, monthsArr[Integer.parseInt(dateEarlier.substring(5,7))-1].toUpperCase() + "  " + dateEarlier.substring(8,10) + ",  " + dateEarlier.substring(0,4));
                                map.put(TAG_DURATION, "");
                                map.put(TAG_TYPE, "");

                                // adding HashList to ArrayList


                                workoutsList.add(map);
                            }

                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                //map.put(TAG_WID, id);
                                map.put(TAG_NAME, "Activity: " + name);
                                map.put(TAG_DURATION, "Duration: " + duration + " hours");
                                map.put(TAG_TYPE, "Description: " + type);

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

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TAG_NAME, "Oops, nothing to see here!");
                map.put(TAG_DURATION, "Please log an activity.");
                workoutsList.add(map);
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AllWorkoutsActivity.this, workoutsList,
                            R.layout.list_item_brain, new String[] {
                            TAG_NAME, TAG_DURATION},
                            new int[] { R.id.name_brain, R.id.duration_brain}){
                    };
                    // updating listview


                    setListAdapter(adapter);

                }
            });

        }

    }

    public JSONArray sortJArray(JSONArray jsonArr){
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            try {
                jsonValues.add(jsonArr.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "Date";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                } catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }
}

