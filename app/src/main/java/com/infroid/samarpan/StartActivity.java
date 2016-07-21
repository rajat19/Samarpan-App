package com.infroid.samarpan;

import android.content.Intent;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

public class StartActivity extends AppCompatActivity {

    Session session;
    Intent in;
    ContentLoadingProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        session = new Session(getApplicationContext());
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progress);
        progressBar.show();
        int isLoggedIn = session.getLogInfo();
        if(Integer.toString(isLoggedIn).equals(null))
            isLoggedIn = 0;
        Log.e("log", isLoggedIn+"");
        switch (isLoggedIn) {
            case 0:
                Log.e("status: ", "LoggedOut");
                in = new Intent(getApplicationContext(), MainActivity.class);
                break;
            case 1:
                Log.e("status: ", "LoggedIn "+ session.getUserType()+"<-");
                if(session.getUserType().equals("0")) {
                    in = new Intent(getApplicationContext(), AdminActivity.class);
                }
                if(session.getUserType().equals("1")) {
                    in = new Intent(getApplicationContext(), ViewerActivity.class);
                }
                if(session.getUserType().equals("2")) {
                    in = new Intent(getApplicationContext(), UserActivity.class);
                }
                break;
        }
        startActivity(in);
        if(progressBar.isShown())
            progressBar.hide();
        finish();
    }
}
