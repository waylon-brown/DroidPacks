package com.appuccino.droidpacks.activities;

import android.accounts.AccountManager;
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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appuccino.droidpacks.R;
import com.appuccino.droidpacks.extra.CustomTextView;
import com.appuccino.droidpacks.extra.FontManager;
import com.appuccino.droidpacks.extra.MyLog;
import com.appuccino.droidpacks.fragments.LibraryFragment;
import com.appuccino.droidpacks.fragments.PacksFragment;
import com.appuccino.droidpacks.objects.Account;
import com.appuccino.droidpacks.objects.App;
import com.appuccino.droidpacks.objects.UserAppData;
import com.appuccino.droidpacks.objects.UserPackData;
import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import net.simonvt.menudrawer.MenuDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements ActionBar.TabListener, PacksFragment.OnFragmentInteractionListener, LibraryFragment.OnFragmentInteractionListener{

    public static boolean TEST_MODE = true;

    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    public static Context globalContext;

    //parse names
    public static final String PARSE_CLASS_USER = "User";
    public static final String PARSE_CLASS_USERAPPDATA = "UserAppData";
    public static final String PARSE_CLASS_PACKDATA = "UserPackData";
    public static final String PARSE_VARIABLE_EMAIL = "accountEmail";
    public static final String PARSE_VARIABLE_TYPE = "accountType";
    public static final String PARSE_VARIABLE_APPDATA = "appData";
    public static final String PARSE_VARIABLE_BOUGHTAPPIDS = "boughtApps";
    public static final String PARSE_VARIABLE_PACKID = "packID";
    public static final String PARSE_VARIABLE_PACKGOLD = "packGold";

    LibraryFragment libraryFrag;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    PagerSlidingTabStrip tabStrip;
    MenuDrawer menuDrawer;
    LinearLayout actionBarButton;
    CustomTextView tabUpdateText;
    CustomTextView accountName;
    CustomTextView accountTitle;
    ProgressBar accountProgressIndicator;
    public Account userAccount;

    PacksFragment.OnFragmentInteractionListener fragListener;

    public static List<App> storeAppList = new ArrayList<App>();
    public static UserAppData dummyUserAppData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalContext = this;
        FontManager.setup(this);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();

        Parse.initialize(this, "6QG3o0PcjzaVnqYBu4idca1NohQ6bUUpg7GRA8Ny", "9nQ3Kvu3U73lsFgYVtRJfCNUiqNcH00T8QEV8IEi");

        menuDrawer = MenuDrawer.attach(this);
        menuDrawer.setContentView(R.layout.activity_main);
        menuDrawer.setMenuView(R.layout.menu_drawer);
        menuDrawer.setMenuSize(dpToPx(260));

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        actionBarButton = (LinearLayout)findViewById(R.id.actionBar);
        tabUpdateText = (CustomTextView)findViewById(R.id.tabUpdateText);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabStrip.setViewPager(mViewPager);

        setupMenuDrawerViews();

        setupDummyData();
    }

    private void setupDummyData(){
        storeAppList.add(new App(1, "Scientific 7 Min Workout Pro", "com.scientific7", 14, 99, 15));
        storeAppList.add(new App(2, "Frequency Pro", "com.appuccino.frequency", 11, 15, 99));
        storeAppList.add(new App(3, "HoloConvert Pro", "com.appuccino.holoconvert", 14, 99, 4));
        storeAppList.add(new App(4, "TheCampusFeed", "com.appuccino.thecampusfeed", 14, 99, 1));

        int[] dummyAppIDs = {1, 2, 3, 4, 5};
        UserPackData[] dummyPackList = {new UserPackData(1, true),
                new UserPackData(2, false),
                new UserPackData(6, true)};
        dummyUserAppData = new UserAppData(dummyAppIDs, dummyPackList);
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
                MyLog.i("LOGGING IN: Email: " + email + " Type: " + type);

                accountProgressIndicator.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.GONE);
                accountTitle.setVisibility(View.GONE);

                retrieveParseUserData(email, type);
            }
        }
    }

    public void retrieveParseUserData(final String email, final String type){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo(PARSE_VARIABLE_EMAIL, email);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null && userList.size() >= 1) {    //user is in DB
                    ParseObject user = userList.get(0);
                    if (TEST_MODE)
                        Toast.makeText(MainActivity.this, "User found in DB", Toast.LENGTH_LONG).show();

                    UserAppData appData = UserAppData.appDataFromJSON(user.get("appData").toString());
                    userAccount = new Account(email, type, appData);
                } else if (e == null){                      //new user, add to DB
                    ParseObject user = new ParseObject(PARSE_CLASS_USER);
                    user.put(PARSE_VARIABLE_EMAIL, email);
                    user.put(PARSE_VARIABLE_TYPE, type);
                    user.put(PARSE_VARIABLE_APPDATA, "");
                    user.saveInBackground();
                    if (TEST_MODE)
                        Toast.makeText(MainActivity.this, "New user", Toast.LENGTH_LONG).show();

                    userAccount = new Account(email, type, null);
                } else {                                    //e isn't null, so an error occurred
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, "Couldn't connect to the network, please check your connection", Toast.LENGTH_LONG).show();
                }

                libraryFrag.updateList();
                accountProgressIndicator.setVisibility(View.GONE);
                accountName.setVisibility(View.VISIBLE);
                accountTitle.setVisibility(View.VISIBLE);
                setupMenuDrawerViews();
            }
        });
    }

    public static List<App> appListFromIDs(int[] ids){
        List<App> returnList = new ArrayList<App>();

        for(int i : ids){
            for(App a : storeAppList){
                if(a.id == i){
                    returnList.add(a);
                    break;
                }
            }
        }

        return returnList;
    }

    public void setTabUpdateTextVisibility(boolean visible){
        if(tabUpdateText != null){
            if(visible){
                tabUpdateText.setVisibility(View.VISIBLE);
            } else {
                tabUpdateText.setVisibility(View.GONE);
            }
        }

    }

    private void setupMenuDrawerViews(){
        if(menuDrawer != null){
            accountTitle = (CustomTextView)findViewById(R.id.accountTitle);
            accountName = (CustomTextView)findViewById(R.id.accountName);
            CustomTextView logInOutButton = (CustomTextView)findViewById(R.id.logInOutButton);
            CustomTextView howsItWorkButton = (CustomTextView)findViewById(R.id.howsItWorkButton);
            CustomTextView contactUsButton = (CustomTextView)findViewById(R.id.contactUsButton);
            accountProgressIndicator = (ProgressBar)findViewById(R.id.accountLoadingIndicator);

            if(actionBarButton != null){
                actionBarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuDrawer.toggleMenu();
                    }
                });
            }

            //not logged in
            if(userAccount == null || userAccount.getEmail().isEmpty()){
                accountTitle.setVisibility(View.GONE);
                accountName.setText("Not logged in");
                logInOutButton.setText("Log in");
                logInOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickUserAccount();
                    }
                });

                //set account name padding
                int topBottomPadding = 22;
                int leftPadding = 16;
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels1 = (int) (topBottomPadding*scale + 0.5f);
                int dpAsPixels2 = (int) (leftPadding*scale + 0.5f);
                accountName.setPadding(dpAsPixels2, dpAsPixels1, 0, dpAsPixels1);

            } else {    //logged in
                accountTitle.setVisibility(View.VISIBLE);
                accountName.setText(userAccount.getEmail());
                logInOutButton.setText("Log out");
                logInOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userAccount = null;
                        setupMenuDrawerViews();
                    }
                });

                //set account name padding
                int topPadding = 2;
                int bottomPadding = 14;
                int leftPadding = 16;
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels1 = (int) (topPadding*scale + 0.5f);
                int dpAsPixels2 = (int) (bottomPadding*scale + 0.5f);
                int dpAsPixels3 = (int) (leftPadding*scale + 0.5f);
                accountName.setPadding(dpAsPixels3, dpAsPixels1, 0, dpAsPixels2);
            }

            howsItWorkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            contactUsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
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

    private int dpToPx(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
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
                    return PacksFragment.newInstance();
                default:
                    libraryFrag = LibraryFragment.newInstance();
                    return libraryFrag;
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
