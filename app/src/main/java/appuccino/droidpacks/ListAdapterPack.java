package appuccino.droidpacks;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.List;


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

            row.setTag(holder);
        }
        else
        {
            holder = (PackHolder)row.getTag();
        }

        Pack pack = packList.get(position);

        switch(position){
            case 0:
                holder.packBackground.setBackgroundColor(context.getResources().getColor(R.color.flatblue));
                break;
            case 1:
                holder.packBackground.setBackgroundColor(context.getResources().getColor(R.color.flatgreen));
                break;
            case 2:
                holder.packBackground.setBackgroundColor(context.getResources().getColor(R.color.flatpurple));
                break;
            case 3:
                holder.packBackground.setBackgroundColor(context.getResources().getColor(R.color.flatorange));
                break;
        }

        return row;
    }

    static class PackHolder
    {
        LinearLayout packBackground;
    }
}
