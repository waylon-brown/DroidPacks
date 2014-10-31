package com.appuccino.droidpacks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageView;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.objects.App;
import com.squareup.picasso.Picasso;

public class AppInfoDialog extends AlertDialog.Builder{
    Context context;
    App app;

    public AppInfoDialog(final Context context, View layout, App app) {
        super(new ContextThemeWrapper(context, R.style.dialogLight));
        //super(context);
        this.context = context;
        this.app = app;
        setCancelable(true);
        setView(layout).setPositiveButton("Okay", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        createDialog(layout);
    }

    private void createDialog(View layout) {
        final AlertDialog dialog = create();
        dialog.show();

        ImageView appIcon = (ImageView)layout.findViewById(R.id.dialogAppIcon);
        ImageView appStoreIcon = (ImageView)layout.findViewById(R.id.appStoreImage);
        CustomTextView appName = (CustomTextView)layout.findViewById(R.id.dialogAppName);

        if(app.exampleIcon != null){
            Picasso.with(context).load(R.drawable.example_icon).into(appIcon);
        }
        if(app.exampleIcon != null){
            Picasso.with(context).load(R.drawable.example_app_image).into(appStoreIcon);
        }
        if(app.name != null && !app.name.isEmpty()){
            appName.setText(app.name);
        }
    }
}