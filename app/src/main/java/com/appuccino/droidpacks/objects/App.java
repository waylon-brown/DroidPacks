package com.appuccino.droidpacks.objects;

import android.net.Uri;
import android.os.Build;

import com.appuccino.droidpacks.extra.MyLog;

public class App {

    public String name;
    public String appPackage;
    private Uri imageUri;
    public boolean installed = false;
    private int installedVersionCode = 0;
    public boolean needsUpdate = false;
    public boolean isIncompatible = false;
    public int minSDK = 0;
    public int maxSDK = Integer.MAX_VALUE;
    private int serverVersionNumber = 0;

    public App(String n, String p, int minSDK, int maxSDK, int serverVersionNumber){
        name = n;
        appPackage = p;
        this.minSDK = minSDK;
        this.maxSDK = maxSDK;
        this.serverVersionNumber = serverVersionNumber;

        //if minSDK is higher than user device version
        if(minSDK > Build.VERSION.SDK_INT){
            isIncompatible = true;
        } else if(maxSDK < Build.VERSION.SDK_INT){  //if max SDK is lower than user device version
            isIncompatible = true;
        }
    }

    public void setInstalledVersionCode(int vCode){
        installedVersionCode = vCode;
        MyLog.i("Installed version code for " + name + " is " + vCode);
        if(vCode < serverVersionNumber){
            needsUpdate = true;
        }
    }
}
