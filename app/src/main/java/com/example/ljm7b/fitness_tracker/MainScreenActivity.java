package com.example.ljm7b.fitness_tracker;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainScreenActivity extends AppCompatActivity{

    private static String TAG = MainScreenActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();


    private CombinedChart mChart;
    private final int itemcount = 12;

    private ProgressDialog pDialog;

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };


    JSONArray workouts = null;
    JSONArray goals = null;
    JSONParser2 jParser2 = new JSONParser2();

    private static final String TAG_SUCCESS = "success";


    private static final String url_all_workouts = "http://fall2015db.asuscomm.com/FitnessDB/getWorkoutLog.php";
    private static final String TAG_WORKOUTS = "all_workouts";
    private static final String TAG_NAME = "Workout";
    private static final String TAG_DURATION = "Duration";
    private static final String TAG_TYPE = "Type";

    private static final String url_goals = "http://fall2015db.asuscomm.com/FitnessDB/getGoal.php";
    private static final String TAG_GOAL = "goals";
    private static final String TAG_BRAIN_GOAL = "Brain_Goal";
    private static final String TAG_BODY_GOAL = "Body_Goal";

    public JSONArray workouts2 = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_screen);


        new GetGoalsAndWorkouts().execute(new SessionVariables().getUserID());

        setContentView(R.layout.new_main_activity);

        mNavItems.add(new NavItem("Log Brain Activity", "Log Time Getting Smarter", R.drawable.ic_brain));
        mNavItems.add(new NavItem("Log Body Workout", "Log Time Pumping Iron", R.drawable.ic_body));
        mNavItems.add(new NavItem("View Activity Log", "Everything You Did Today", R.drawable.ic_binoculars));
        mNavItems.add(new NavItem("Video Workouts", "Get Inspired", R.drawable.ic_action_video));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigation Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                selectItemFromDrawer(position);

                Log.d("POSITION:", String.valueOf(position));
                mDrawerLayout.closeDrawers();
                switch(position){

                    case(0):{ //log brain activity

                        Intent i = new Intent(getApplicationContext(), LogBrainWorkoutActivity.class);
                        startActivity(i);
                        break;
                    }
                    case(1):{ //log body workout

                        Intent i = new Intent(getApplicationContext(), LogBodyWorkoutActivity.class);
                        startActivity(i);
                        break;

                    }
                    case(2):{ //view activity log
                        Intent i = new Intent(getApplicationContext(), AllWorkoutsActivity.class);
                        startActivity(i);
                        break;

                    }
                    case(3):{ //video workouts
                        Intent i = new Intent(getApplicationContext(), VideoMainActivity.class);
                        startActivity(i);
                        break;
                    }
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle your other action bar items...

        switch (item.getItemId()) {
            case R.id.actionToggleLineValues: {
                for (DataSet<?> set : mChart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (DataSet<?> set : mChart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon) {
            mTitle = title;
            mSubtitle = subtitle;
            mIcon = icon;
        }
    }

    private LineData generateLineData(JSONArray data) {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
    try {
        for (int index = 0; index < data.length(); index++) {

            JSONObject c = data.getJSONObject(index);
            try {
                String body_goal = c.getString("Body_Goal");
                for(int i = 0; i < 7; ++i)
                    entries.add(new Entry(Float.valueOf(body_goal), i));
            }catch(JSONException e) {
                e.printStackTrace();
            }


        }
    }catch(JSONException e) {
            e.printStackTrace();
        }

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleSize(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(JSONArray data) {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        try {
            for (int index = 0; index < data.length(); index++) {
                JSONObject c = data.getJSONObject(index);
                String duration = c.getString("Duration");
                String type = c.getString("Type");

                if(type.equals("body")){
                    float f = Float.valueOf(duration);

                    entries.add(new BarEntry(f, index));
                }


            }
        }catch(JSONException e) {
            e.printStackTrace();

        }

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.combined, menu);
        return true;
    }

    class GetGoalsAndWorkouts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainScreenActivity.this);
            pDialog.setMessage("Loading workouts. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All workouts from url
         */
        @Override
        protected String doInBackground(String... args) {
            // Building Parameters


            String userID = args[0];
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("User_ID", userID));


            // getting JSON string from URL

            JSONObject json = jParser2.makeHttpRequest(url_all_workouts, "GET", params);

            JSONObject json2 = jParser2.makeHttpRequest(url_goals, "GET", params);

            // Check your log cat for JSON response
            Log.d("All workouts: ", json.toString());
            Log.d("All workouts: ", json2.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                int success2 = json2.getInt(TAG_SUCCESS);

                if (success == 1 && success2 == 1) {
                    // workouts found
                    // Getting Array of workouts

                    workouts = json.getJSONArray(TAG_WORKOUTS);
                    goals = json2.getJSONArray(TAG_GOAL);

                } else {
                    Log.d("error with parsing", "0");
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all workouts
            try {
                workouts2 = concatArray(workouts, goals);
            }catch (JSONException e) {
                e.printStackTrace();

            }


            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //setContentView(R.layout.new_main_activity);

            mChart = (CombinedChart) findViewById(R.id.chart12);
            mChart.setDescription("");
            mChart.setBackgroundColor(Color.WHITE);
            mChart.setDrawGridBackground(false);
            mChart.setDrawBarShadow(false);

            // draw bars behind lines
            mChart.setDrawOrder(new CombinedChart.DrawOrder[] {
                    CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
            });

            YAxis rightAxis = mChart.getAxisRight();
            rightAxis.setDrawGridLines(false);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.setDrawGridLines(false);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

            CombinedData data = new CombinedData(mMonths);

            data.setData(generateLineData(goals));
            data.setData(generateBarData(workouts));
//        data.setData(generateBubbleData());
//         data.setData(generateScatterData());
//         data.setData(generateCandleData());

            mChart.setData(data);
            mChart.invalidate();
            pDialog.dismiss();

        }

    }

    private JSONArray concatArray(JSONArray arr1, JSONArray arr2)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }


}
