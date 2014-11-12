package com.appuccino.droidpacks.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.activities.MainActivity;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.extra.MyLog;
import com.appuccino.droidpacks.listadapters.ListAdapterLibrary;
import com.appuccino.droidpacks.objects.Account;
import com.appuccino.droidpacks.objects.App;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private CustomTextView emptyListTextView;
    private ListAdapterLibrary adapter;
    private List<App> appList;

    // TODO: Rename and change types of parameters
    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();
        return fragment;
    }

    public LibraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //name, package, minsdk, maxsdk, serverversionnumber
        Account userAccount = ((MainActivity)getActivity()).userAccount;
        if(userAccount != null){
            int[] userAppIDs = ((MainActivity)getActivity()).userAccount.getUserAppData().boughtAppIDs;
            appList = MainActivity.appListFromIDs(userAppIDs);
        } else {
            appList = new ArrayList<App>();
        }


        adapter = new ListAdapterLibrary(getActivity(), R.layout.list_row_library, appList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabfragment_library, container, false);

        // Set the adapter
        listView = (ListView) view.findViewById(R.id.libraryList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        emptyListTextView = (CustomTextView)view.findViewById(R.id.emptyLibraryText);
        //if list is empty or user not logged in, show empty text
        if(appList == null || appList.size() == 0 || ((MainActivity)getActivity()).userAccount == null){
            emptyListTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

            //if making list invisible because it is just empty, change the text
            if(((MainActivity)getActivity()).userAccount != null){
                emptyListTextView.setText("Your purchased apps will show up here");
            }
        } else {
            emptyListTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        //if doesnt have footer, add it
        if(listView.getFooterViewsCount() == 0)
        {
            //for card UI
            View headerFooter = new View(getActivity());
            headerFooter.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 20));
            listView.addHeaderView(headerFooter, null, false);
            listView.addFooterView(headerFooter, null, false);
        }

        detectAppStates();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        detectAppStates();
    }

    private void detectAppStates(){
        PackageManager pm = getActivity().getPackageManager();

        //checking for apps that are installed
        for(App app: appList){
            try {
                pm.getPackageInfo(app.appPackage, PackageManager.GET_ACTIVITIES);
                app.installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                app.installed = false;
            }


        }

        //checking installed app version codes
        for(App app: appList){
            PackageInfo pInfo = null;
            try {
                pInfo = getActivity().getPackageManager().getPackageInfo(app.appPackage, 0);
                String version = pInfo.versionName;
                int versionCode = pInfo.versionCode;
                app.setInstalledVersionCode(versionCode);
                if(app.needsUpdate){
                    ((MainActivity)getActivity()).setTabUpdateTextVisibility(true);
                }

                MyLog.i(app.appPackage + " versionName: " + version + " VersionCode: " + versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        updateList();
    }

    public void updateList(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }

        //if list is empty or user not logged in, show empty text
        if(appList == null || appList.size() == 0 || ((MainActivity)getActivity()).userAccount == null){
            emptyListTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

            //if making list invisible because it is just empty, change the text
            if(((MainActivity)getActivity()).userAccount != null){
                emptyListTextView.setText("Your purchased apps will show up here");
            }
        } else {
            emptyListTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
