package com.example.ljm7b.fitness_tracker;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by ljm7b on 10/14/2015.
 */

public class SessionVariables {

    private static String user_id;

    private static boolean dateExpired;

    public void setUserID(String userID){
        user_id = userID;
    }
    public String getUserID(){
        return user_id;
    }

    public void setDateExpired(boolean value){
        dateExpired = value;
    }

    public boolean isDateExpired(){
        return dateExpired;
    }
}