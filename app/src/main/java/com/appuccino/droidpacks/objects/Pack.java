package com.appuccino.droidpacks.objects;

import java.util.List;

public class Pack {

    private List<App> appList;

    public Pack(List<App> list){
        appList = list;
    }

    public List<App> getAppList(){
        return appList;
    }
}
