package com.appuccino.droidpacks.listadapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.extra.MyLog;
import com.appuccino.droidpacks.network.DownloadAppTask;
import com.appuccino.droidpacks.objects.App;
import com.squareup.picasso.Picasso;

import java.io.File;
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

        setActionButton(app, holder.actionButton);
        return row;
    }

    private void setActionButton(final App app, CustomTextView actionButton){
        if(app.isIncompatible){
            MyLog.i("App " + app.name + " is incompatible");

            actionButton.setText("Incompatible");
            actionButton.setBackgroundColor(context.getResources().getColor(R.color.flatpurple));
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //user's device is below min SDK
                    if(app.minSDK > Build.VERSION.SDK_INT)
                        Toast.makeText(context, "This app's minimum SDK version is higher than your device's", Toast.LENGTH_LONG).show();
                    else    //user's device is above max SDK
                        Toast.makeText(context, "This app's maximum SDK version is lower than your device's", Toast.LENGTH_LONG).show();
                }
            });
        } else if(app.needsUpdate){
            MyLog.i("App " + app.name + " needs an update");

            actionButton.setText("Update");
            actionButton.setBackgroundColor(context.getResources().getColor(R.color.flatorange));
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLog.i("Downloading...");
                    downloadApp(app);
                }
            });
        } else if(app.installed){
            MyLog.i("App " + app.name + " is installed");

            actionButton.setText("Open");
            actionButton.setBackgroundColor(context.getResources().getColor(R.color.flatblue));
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(app.appPackage);
                    context.startActivity( LaunchIntent );
                }
            });
        } else {
            MyLog.i("App " + app.name + " needs to be downloaded");

            actionButton.setText("Install");
            actionButton.setBackgroundColor(context.getResources().getColor(R.color.flatgreen));
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLog.i("Downloading...");
                    downloadApp(app);
                }
            });
        }
    }

    private void downloadApp(App app){
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.show();

        final DownloadAppTask downloadTask = new DownloadAppTask(context, this, app, progressDialog);
        downloadTask.execute("http://droidpacks.com/Scientific7MinuteWorkout%20Pro.apk");

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

    public void installApp(App app){
        File file = new File(DownloadAppTask.FILE_PATH);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    static class AppHolder
    {
        CustomTextView appName;
        ImageView appIcon;
        CustomTextView actionButton;
    }
}
