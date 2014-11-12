package com.appuccino.droidpacks.objects;

import com.appuccino.droidpacks.activities.MainActivity;

/**
 * Created by Waylon on 11/5/2014.
 */
public class Account {
    private String email;
    private String type;
    private UserAppData appData;

    public Account(String n, String t, UserAppData data){
        email = n;
        type = t;
        //appData = data;
        appData = MainActivity.dummyUserAppData;
    }

    public String getEmail(){
        return email;
    }

    public String getType(){
        return type;
    }

    public UserAppData getUserAppData(){
        return appData;
    }
}
