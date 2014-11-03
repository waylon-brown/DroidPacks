package com.appuccino.droidpacks.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.FontManager;
import com.appuccino.droidpacks.objects.Pack;

public class GetPackDialog extends AlertDialog.Builder{
    Context context;
    Pack pack;

    public GetPackDialog(final Context context, View layout, Pack pack) {
        super(new ContextThemeWrapper(context, R.style.dialogLight));
        //super(context);
        this.context = context;
        this.pack = pack;
        setCancelable(true);
        setView(layout).setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setTitle("Pay what you want!");

        createDialog(layout);
    }

    private void createDialog(View layout) {
        final AlertDialog dialog = create();

        TextView title =  new TextView(context);
        title.setText("Pay what you want!");
        title.setGravity(Gravity.CENTER);
        title.setTextSize(27);
        title.setTypeface(FontManager.light);
        title.setTextColor(context.getResources().getColor(R.color.lightblue));
        float scale = context.getResources().getDisplayMetrics().density;
        float sizeInDp = 16.0f;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        title.setPadding(0, dpAsPixels, 0, dpAsPixels);
        dialog.setCustomTitle(title);

        dialog.show();

        Button yesButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        EditText priceEditText = (EditText)layout.findViewById(R.id.priceEditText);

        priceEditText.setTypeface(FontManager.light);
        priceEditText.setSelection(priceEditText.getText().length());
        yesButton.setTypeface(FontManager.light);

    }
}