package com.appuccino.droidpacks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.extra.FontManager;
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
        CustomTextView playStoreButton = (CustomTextView)layout.findViewById(R.id.goToPlayStoreButton);
        CustomTextView incompatibleText = (CustomTextView)layout.findViewById(R.id.incompatibleText);
        Button yesButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        //if(app.exampleIcon != null){
        Picasso.with(context).load(R.drawable.example_icon).into(appIcon);
        //}
        //if(app.exampleIcon != null){
            Picasso.with(context).load(R.drawable.example_app_image).into(appStoreIcon);
        //}
        if(app.name != null && !app.name.isEmpty()){
            appName.setText(app.name);
        }
        if(app.isCompatibleBoolean){
            incompatibleText.setVisibility(View.GONE);
        } else {
            incompatibleText.setVisibility(View.VISIBLE);
            incompatibleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //user's device is below min SDK
                    if(app.minSDK > Build.VERSION.SDK_INT)
                        Toast.makeText(context, "This app's minimum SDK version is higher than your device's", Toast.LENGTH_LONG).show();
                    else    //user's device is above max SDK
                        Toast.makeText(context, "This app's maximum SDK version is lower than your device's", Toast.LENGTH_LONG).show();
                }
            });
        }
        yesButton.setTypeface(FontManager.light);

        playStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + app.appPackage));
                context.startActivity(browserIntent);
            }
        });
    }
}