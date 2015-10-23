//package com.example.ljm7b.fitness_tracker;
//
///**
// * Created by ljm7b on 10/10/2015.
// */
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.net.URLConnection;
//import org.apache.http.NameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.app.ListActivity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//
//public class AllWorkoutsActivity extends ListActivity {
//
//    // Progress Dialog
//    private ProgressDialog pDialog;
//
//    // Creating JSON Parser object
//    JSONParser2 jParser2 = new JSONParser2();
//
//    ArrayList<HashMap<String, String>> workoutsList;
//
//    // url to get all workouts list
//    private static String url_all_workouts = "http://fall2015db.asuscomm.com/fitness/GET_ALLWORKOUT.php";
//
//    // JSON Node names
//    private static final String TAG_SUCCESS = "success";
//    private static final String TAG_WORKOUTS = "workouts";
//    private static final String TAG_WID = "wid";
//    private static final String TAG_NAME = "name";
//    private static final String TAG_CALORIES = "calories";
//    private static final String TAG_DESCRIPTION = "description";
//
//    //workouts JSONArray
//    JSONArray workouts = null;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.all_workouts);
//
//        // Hashmap for ListView
//        workoutsList = new ArrayList<HashMap<String, String>>();
//
//        // Loading workouts in Background Thread
//        new LoadAllWorkouts().execute();
//
//        // Get listview
//        ListView lv = getListView();
//
//    }
//
//    // Response from Edit Product Activity
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // if result code 100
//        if (resultCode == 100) {
//            // if result code 100 is received
//            // means user edited/deleted product
//            // reload this screen again
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
//        }
//
//    }
//
//    /**
//     * Background Async Task to Load all product by making HTTP Request
//     * */
//    class LoadAllWorkouts extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         * */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(AllWorkoutsActivity.this);
//            pDialog.setMessage("Loading workouts. Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        /**
//         * getting All workouts from url
//         * */
//        @Override
//        protected String doInBackground(String... args) {
//            // Building Parameters
//            List<NameValuePair> params = new ArrayList<>();
//            // getting JSON string from URL
//
//            JSONObject json = jParser2.makeHttpRequest(url_all_workouts, "GET", params);
//
//            // Check your log cat for JSON reponse
//            Log.d("All workouts: ", json.toString());
//
//            try {
//                // Checking for SUCCESS TAG
//                int success = json.getInt(TAG_SUCCESS);
//
//                if (success == 1) {
//                    // workouts found
//                    // Getting Array of workouts
//                    workouts = json.getJSONArray(TAG_WORKOUTS);
//
//                    // looping through All workouts
//                    for (int i = 0; i < workouts.length(); i++) {
//                        JSONObject c = workouts.getJSONObject(i);
//
//                        // Storing each json item in variable
//                        String id = c.getString(TAG_WID);
//                        String name = c.getString(TAG_NAME);
//                        String calories = c.getString(TAG_CALORIES);
//                        String description = c.getString(TAG_DESCRIPTION);
//
//                        // creating new HashMap
//                        HashMap<String, String> map = new HashMap<String, String>();
//
//                        // adding each child node to HashMap key => value
//                        map.put(TAG_WID, id);
//                        map.put(TAG_NAME, "Type: " + name);
//                        map.put(TAG_CALORIES, "Calories Burned: " + calories);
//                        map.put(TAG_DESCRIPTION, "Description: " + description);
//
//                        // adding HashList to ArrayList
//                        workoutsList.add(map);
//                    }
//                } else {
//                    // no workouts found
//                    // Launch Add New product Activity
//                    Intent i = new Intent(getApplicationContext(),
//                            NewWorkoutActivity.class);
//                    // Closing all previous activities
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog
//         * **/
//        @Override
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after getting all workouts
//            pDialog.dismiss();
//            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    /**
//                     * Updating parsed JSON data into ListView
//                     * */
//                    ListAdapter adapter = new SimpleAdapter(
//                            AllWorkoutsActivity.this, workoutsList,
//                            R.layout.list_item, new String[] { TAG_WID,
//                            TAG_NAME, TAG_CALORIES,TAG_DESCRIPTION},
//                            new int[] { R.id.wid, R.id.name, R.id.calories, R.id.description });
//                    // updating listview
//                    setListAdapter(adapter);
//                }
//            });
//
//        }
//
//    }
//}
//
