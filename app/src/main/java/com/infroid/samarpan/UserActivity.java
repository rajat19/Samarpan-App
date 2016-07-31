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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity implements FragmentSwitchListener{

    Toolbar toolbar;
    TextView textView, navname, navmail;
    ImageView navpic;
    DrawerLayout drawerLayout;
    Session session;
    ServerLink link = new ServerLink();
    String URL_PHOTO = link.URL_PHOTO;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    View headerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        session = new Session(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new UserHomeFragment());
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
        headerLayout = navigationView.getHeaderView(0);
        navname = (TextView) headerLayout.findViewById(R.id.navname);
        navmail = (TextView) headerLayout.findViewById(R.id.navmail);
        navpic = (ImageView) headerLayout.findViewById(R.id.navpic);
        String imageServerPath = URL_PHOTO + session.getUserImage();
        Picasso.with(getApplicationContext()).load(imageServerPath).into(navpic);
        String uname = session.getUserName();
        String fname = uname.substring(0, uname.indexOf(" "));
        navname.setText(fname);
        navmail.setText(session.getUserEmail());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new UserHomeFragment());
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            case 4:
                fragmentTransaction.replace(R.id.main_container, new AddExperienceFragment());
                break;
            /*Add Experience*/
            default:
            /*Redirect to Home*/
                fragmentTransaction.replace(R.id.main_container, new HomeFragment());
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    @Override
    public void switchFragment(int position, String user_id) {

    }
}
