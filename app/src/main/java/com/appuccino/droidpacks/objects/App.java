package com.appuccino.droidpacks.objects;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by Waylon on 10/28/2014.
 */
public class App {

    public String name;
    private Uri imageUri;
    public Drawable exampleIcon;

    public App(String n, Drawable i){
        name = n;
        exampleIcon = i;
    }
}
