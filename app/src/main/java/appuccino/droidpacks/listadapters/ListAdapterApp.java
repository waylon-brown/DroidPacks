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
    public View getView(int position, View convertView, ViewGroup parent) {

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
