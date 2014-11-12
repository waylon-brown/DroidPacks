package com.appuccino.droidpacks.fragments;

import android.app.Activity;
import android.app.Fragment;
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
import com.appuccino.droidpacks.listadapters.ListAdapterPack;
import com.appuccino.droidpacks.objects.App;
import com.appuccino.droidpacks.objects.Pack;

import java.util.ArrayList;
import java.util.List;

public class PacksFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private ListAdapterPack adapter;
    List<Pack> packList;

    // TODO: Rename and change types of parameters
    public static PacksFragment newInstance() {
        PacksFragment fragment = new PacksFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PacksFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<App> appList = ((MainActivity)getActivity()).storeAppList;

        packList = new ArrayList<Pack>();
        packList.add(new Pack(appList));
        packList.add(new Pack(appList));
        packList.add(new Pack(appList));
        packList.add(new Pack(appList));

        adapter = new ListAdapterPack(getActivity(), R.layout.list_row_pack, packList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabfragment_packs, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.list);
        mListView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        detectIncompatibleApps();

        return view;
    }

    private void detectIncompatibleApps(){
        PackageManager pm = getActivity().getPackageManager();

        for(Pack pack: packList){
            //checking installed app version codes
            for(App app: pack.getAppList()){
                if(App.appIsCompatible(app.minSDK, app.maxSDK)){
                    app.isCompatibleBoolean = true;
                } else {
                    app.isCompatibleBoolean = false;
                }
            }

            updateList();
        }
    }

    public void updateList(){
        if(adapter != null){
            adapter.notifyDataSetChanged();
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
