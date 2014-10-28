package appuccino.droidpacks.listadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import appuccino.droidpacks.R;
import appuccino.droidpacks.objects.Pack;
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
        PackHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PackHolder();
            holder.packBackground = (LinearLayout)row.findViewById(R.id.packBackground);
            holder.horizontalAppList = (HListView)row.findViewById(R.id.horizontalAppList);

            List<String> testList = new ArrayList<String>();
            testList.add("First");
            testList.add("Second");
            testList.add("Third");
            testList.add("Third");
            testList.add("Third");
            testList.add("Third");
            testList.add("Third");
            testList.add("Third");
            testList.add("Third");
            holder.arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, testList);

            holder.horizontalAppList.setAdapter(holder.arrayAdapter);

            row.setTag(holder);
        }
        else
        {
            holder = (PackHolder)row.getTag();
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
        ArrayAdapter<String> arrayAdapter;
        HListView horizontalAppList;
    }
}
