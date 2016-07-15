package com.infroid.samarpan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity implements FragmentSwitchListener{

    Toolbar toolbar;
    TextView textView;
    DrawerLayout drawerLayout;
    Session session;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        session = new Session(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();

        textView = new TextView(getApplicationContext());
        textView.setText("Samarpan");
        textView.setTextSize(30);
        textView.setTextColor(Color.WHITE);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/samarn.ttf");
        textView.setTypeface(type);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new HomeFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(textView);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.create_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new NewRegistrationFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(textView);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.profile_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new ProfileFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(textView);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.edit_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new EditDetailsFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(textView);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.work_experience_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new WorkExperiencesFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(textView);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.photo_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new UploadPhotoFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(textView);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout_id:
                        session.setLogInfo(0);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void switchFragment(int id) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch(id) {
            case 1:
            /*Profile*/
                fragmentTransaction.replace(R.id.main_container, new ProfileFragment());
                break;
            case 2:
            /*Edit Details*/
                fragmentTransaction.replace(R.id.main_container, new EditDetailsFragment());
                break;
            case 3:
            /*Work Experience*/
                fragmentTransaction.replace(R.id.main_container, new WorkExperiencesFragment());
                break;
            default:
            /*Redirect to Home*/
                fragmentTransaction.replace(R.id.main_container, new HomeFragment());
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }
}