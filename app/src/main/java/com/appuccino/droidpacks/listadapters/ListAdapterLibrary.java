package com.appuccino.droidpacks.listadapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.objects.App;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAdapterLibrary extends ArrayAdapter<App> {

    Context context;
    int layoutResourceId;
    List<App> appList;

    public ListAdapterLibrary(Context context, int layoutResourceId, List<App> appList) {
        super(context, layoutResourceId, appList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.appList = appList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AppHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AppHolder();
            holder.appName = (CustomTextView)row.findViewById(R.id.appName);
            holder.actionButton = (CustomTextView)row.findViewById(R.id.appButton);
            holder.appIcon = (ImageView)row.findViewById(R.id.libraryIcon);

            row.setTag(holder);
        }
        else
        {
            holder = (AppHolder)row.getTag();
        }

        final App app = appList.get(position);

        holder.appName.setText(appList.get(position).name);
        Picasso.with(context).load(R.drawable.example_icon).into(holder.appIcon);

        if(app.installed){
            holder.actionButton.setText("Open");
            holder.actionButton.setBackgroundColor(context.getResources().getColor(R.color.flatblue));
            holder.actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(app.appPackage);
                    context.startActivity( LaunchIntent );
                }
            });
        } else if (!app.installed){
            holder.actionButton.setText("Install");
            holder.actionButton.setBackgroundColor(context.getResources().getColor(R.color.flatgreen));
        }

        return row;
    }

    static class AppHolder
    {
        CustomTextView appName;
        ImageView appIcon;
        CustomTextView actionButton;
    }
}
