package voidhug.test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by voidhug on 15/4/20.
 */
public class Task extends ActionBarActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mItems;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment list_0 = new List_0();
        fragmentManager.beginTransaction().replace(R.id.content_frame, list_0).commit();

        mItems = getResources().getStringArray(R.array.items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ListView) findViewById(R.id.left_drawer);

        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mItems));
        mListView.setOnItemClickListener(new DrawerItemClickListener());
        mTitle = mDrawerTitle = getTitle();



        //enable ActionBar app icon to behave as action to toggle nav drawer

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);



    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, DateSetting.class);
            this.startActivityForResult(intent,1);
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }


    }


    private void selectItem(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        switch(position) {
            case 0:
                Fragment list_0 = new List_0();
                fragmentManager.beginTransaction().replace(R.id.content_frame, list_0).commit();
                break;
            case 1:
                Fragment list_1 = new List_1();
                fragmentManager.beginTransaction().replace(R.id.content_frame, list_1).commit();
                break;
            case 2:
                Fragment list_2 = new List_3();
                fragmentManager.beginTransaction().replace(R.id.content_frame, list_2).commit();
                break;
            case 3:
                Fragment list_3 = new List_2();
                fragmentManager.beginTransaction().replace(R.id.content_frame, list_3).commit();
                break;
            case 4:
                Fragment list_4 = new List_4();
                fragmentManager.beginTransaction().replace(R.id.content_frame, list_4).commit();
                break;
            case 5:
                Fragment list_5 = new List_5();
                fragmentManager.beginTransaction().replace(R.id.content_frame, list_5).commit();
                break;
            default:
                break;
        }
    }




}



