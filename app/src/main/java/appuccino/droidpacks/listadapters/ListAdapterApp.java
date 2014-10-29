package appuccino.droidpacks.listadapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import appuccino.droidpacks.R;
import appuccino.droidpacks.objects.App;


public class ListAdapterApp extends ArrayAdapter<App> {

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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AppHolder holder = null;

        if(row == null)
        {


            holder = new AppHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            if(position == 0){
                holder.rowLayoutID = R.layout.list_column_appheaderitem;
            } else if(position == appList.size() - 1){
                holder.rowLayoutID = R.layout.list_column_appfooteritem;
            } else {
                holder.rowLayoutID = layoutResourceId;
            }
            row = inflater.inflate(holder.rowLayoutID, parent, false);

            holder.appIcon = (ImageView)row.findViewById(R.id.appIcon);

            row.setTag(holder);
        }
        else
        {
            holder = (AppHolder)row.getTag();
        }

        App app = appList.get(position);

        Picasso.with(context).load(R.drawable.example_icon).into(holder.appIcon);

        return row;
    }

    static class AppHolder
    {
        int rowLayoutID;
        ImageView appIcon;
    }
}
