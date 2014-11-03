package com.appuccino.droidpacks.listadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.objects.App;
import com.appuccino.droidpacks.objects.Pack;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.HListView;


public class ListAdapterPack extends ArrayAdapter<Pack> {

    Context context;
    int layoutResourceId;
    List<Pack> packList;

    public ListAdapterPack(Context context, int layoutResourceId, List<Pack> packList) {
        super(context, layoutResourceId, packList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.packList = packList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PackHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PackHolder();
            holder.packBackground = (LinearLayout)row.findViewById(R.id.packBackground);
            holder.horizontalAppList = (HListView)row.findViewById(R.id.horizontalAppList);

//            List<String> testList = new ArrayList<String>();
//            testList.add("First");
//            testList.add("Second");
//            testList.add("Third");
//            testList.add("Fourth");
//            testList.add("Fifth");
//            testList.add("Third");
//            testList.add("Third");
//            testList.add("Third");
//            testList.add("Third");
//            holder.arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, testList);

            List<App> tempAppList = new ArrayList<App>();
            tempAppList.add(new App("TheCampusFeed"));
            tempAppList.add(new App("Scientific 7 Min Workout Pro"));
            tempAppList.add(new App("Frequency Pro"));
            tempAppList.add(new App("SmartSilence Pro"));

            holder.adapter = new ListAdapterApp(context, R.layout.list_column_app, tempAppList);

            holder.horizontalAppList.setAdapter(holder.adapter);

            row.setTag(holder);
        }
        else
        {
            holder = (PackHolder)row.getTag();
        }

        //if doesnt havefooter, add it
        if(holder.horizontalAppList.getFooterViewsCount() == 0)
        {
            //for card UI
            View sideHeaderFooter = new View(context);
            sideHeaderFooter.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 8));
            holder.horizontalAppList.addFooterView(sideHeaderFooter, null, false);
        }

        Pack pack = packList.get(position);

        switch(position){
            case 0:
                holder.packBackground.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bluegradient));
                break;
            case 1:
                holder.packBackground.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.greengradient));
                break;
            case 2:
                holder.packBackground.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.purplegradient));
                break;
            case 3:
                holder.packBackground.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.redgradient));
                break;
        }

        return row;
    }

    static class PackHolder
    {
        LinearLayout packBackground;
        ListAdapterApp adapter;
        HListView horizontalAppList;
    }
}
