package com.appuccino.droidpacks.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.FontManager;
import com.appuccino.droidpacks.fragments.LibraryFragment;
import com.appuccino.droidpacks.fragments.PacksFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import net.simonvt.menudrawer.MenuDrawer;

import java.util.Locale;


public class MainActivity extends Activity implements ActionBar.TabListener, PacksFragment.OnFragmentInteractionListener, LibraryFragment.OnFragmentInteractionListener{

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    PagerSlidingTabStrip tabStrip;
    MenuDrawer menuDrawer;
    LinearLayout actionBarButton;

    PacksFragment.OnFragmentInteractionListener fragListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontManager.setup(this);

        menuDrawer = MenuDrawer.attach(this);
        menuDrawer.setContentView(R.layout.activity_main);
        menuDrawer.setMenuView(R.layout.menu_drawer);

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        actionBarButton = (LinearLayout)findViewById(R.id.actionBar);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabStrip.setViewPager(mViewPager);

        setupMenuDrawerViews();
        //pickUserAccount();
    }

    private void setupMenuDrawerViews(){
        if(menuDrawer != null){
            if(actionBarButton != null){
                actionBarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuDrawer.toggleMenu();
                    }
                });
            }
        }
    }

    private void pickUserAccount() {
        //String[] accountTypes = new String[]{"com.google"};
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(menuDrawer != null){
            final int drawerState = menuDrawer.getDrawerState();
            if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
                menuDrawer.closeMenu();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context context;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return PacksFragment.newInstance(context);
                default:
                    return LibraryFragment.newInstance(context);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

}
