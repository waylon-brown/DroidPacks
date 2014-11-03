package com.appuccino.droidpacks.objects;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by Waylon on 10/28/2014.
 */
public class App {

    public String name;
    public String appPackage;
    private Uri imageUri;
    public Drawable exampleIcon;
    public boolean installed = false;

    public App(String n, String p){
        name = n;
        appPackage = p;
    }
}
