package com.appuccino.droidpacks.listadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.dialogs.AppInfoDialog;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.objects.App;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAdapterApp extends ArrayAdapter<App> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_FOOTER = 1;
    private static final int VIEW_TYPE_DEFAULT = 2;
    private static final int VIEW_TYPE_COUNT = 3;

    Context context;
    int layoutResourceId;
    List<App> appList;

    public ListAdapterApp(Context context, int layoutResourceId, List<App> appList) {
        super(context, layoutResourceId, appList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.appList = appList;
    }

    @Override
    public int getViewTypeCount() {
        // The total number of row types your adapter supports.
        // This should NEVER change at runtime.
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value from zero to (viewTypeCount - 1)
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (position == getCount() - 1) {
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_DEFAULT;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int layoutID = 0;

        switch(getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                layoutID = R.layout.list_column_appheaderitem;
                break;
            case VIEW_TYPE_FOOTER:
                layoutID = R.layout.list_column_appfooteritem;
                break;
            case VIEW_TYPE_DEFAULT:
                layoutID = layoutResourceId;

        }

        View row = convertView;
        AppHolder holder = null;

        if(row == null)
        {
            holder = new AppHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutID, parent, false);

            holder.card = (FrameLayout)row.findViewById(R.id.card);
            holder.appIcon = (ImageView)row.findViewById(R.id.appIcon);
            holder.redX = (ImageView)row.findViewById(R.id.redX);
            holder.appName = (CustomTextView)row.findViewById(R.id.appName);

            row.setTag(holder);
        }
        else
        {
            holder = (AppHolder)row.getTag();
        }

        App app = appList.get(position);

        holder.appName.setText(app.name);
        Picasso.with(context).load(R.drawable.example_icon).into(holder.appIcon);
        if(app.isCompatibleBoolean){
            holder.redX.setVisibility(View.GONE);
        } else {
            holder.redX.setVisibility(View.VISIBLE);
            Picasso.with(context).load(R.drawable.red_x).into(holder.redX);
        }

        //item click listener
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View commentDialogLayout = inflater.inflate(R.layout.dialog_app_info, null);
                new AppInfoDialog(context, commentDialogLayout, appList.get(position));
            }
        });

        return row;
    }

    static class AppHolder
    {
        FrameLayout card;
        ImageView appIcon;
        CustomTextView appName;
        ImageView redX;
    }
}
