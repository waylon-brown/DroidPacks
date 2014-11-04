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
import com.appuccino.droidpacks.dialogs.GetPackDialog;
import com.appuccino.droidpacks.extra.CustomTextView;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PackHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PackHolder();
            holder.packBackground = (LinearLayout)row.findViewById(R.id.packBackground);
            holder.horizontalAppList = (HListView)row.findViewById(R.id.horizontalAppList);
            holder.getPackButton = (CustomTextView)row.findViewById(R.id.downloadPackButton);

            List<App> appList = new ArrayList<App>();
            appList.add(new App("Scientific 7 Min Workout Pro", "com.scientific7", 14, 99, 4));
            appList.add(new App("Frequency Pro", "com.appuccino.frequency", 11, 15, 99));
            appList.add(new App("HoloConvert Pro", "com.appuccino.holoconvert", 14, 99, 4));
            appList.add(new App("TheCampusFeed", "com.appuccino.thecampusfeed", 14, 99, 1));

            holder.adapter = new ListAdapterApp(context, R.layout.list_column_app, appList);

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

        holder.getPackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_get_pack, null);
                new GetPackDialog(context, dialogLayout, packList.get(position));
            }
        });

        return row;
    }

    static class PackHolder
    {
        LinearLayout packBackground;
        ListAdapterApp adapter;
        HListView horizontalAppList;
        CustomTextView getPackButton;
    }
}
