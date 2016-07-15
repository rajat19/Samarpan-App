package com.infroid.samarpan;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match

    TextView textView;
    EditText email, password;
    Button btnLogin, btnRegister, btnForgot;
    String emailString, passwordString;
    FragmentSwitchListener mCallback;
    ServerLink link = new ServerLink();
    public String URL_LOGIN = link.URL_LOGIN;
    private Session session;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback = (FragmentSwitchListener) activity;
        }
        catch (ClassCastException e) {
            Log.d("Error is", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        session = new Session(getContext());
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        textView = (TextView) view.findViewById(R.id.txtSamarpan);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/samarn.ttf");
        textView.setTypeface(type);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnForgot = (Button) view.findViewById(R.id.btnForgot);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                emailString = email.getText().toString();
                passwordString = password.getText().toString();
                int flag = 0;
                if(emailString.equals("")) {
                    Toast.makeText(getActivity(), "The email field is required.", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                else if(!isEmail(emailString)){
                    Toast.makeText(getActivity(), "Not a valid email address", Toast.LENGTH_SHORT).show();
                    flag++;
                }

                else if(passwordString.equals("")){
                    Toast.makeText(getActivity(), "The password field is required.", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                else if(passwordString.length() < 6) {
                    Toast.makeText(getActivity(), "Password is too short. Must be of length greater than 6", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                if(flag == 0) {
                    /*Start async task to login the user*/
                    String arr[] = {emailString, passwordString};
                    new Login().execute(arr);
                }
                    break;
            case R.id.btnForgot:
                mCallback.switchFragment(3);
                break;
            case R.id.btnRegister:
                mCallback.switchFragment(1);
                break;
        }
    }

    private static boolean isEmail(String s) {
        Matcher matcher = EMAIL_REGEX.matcher(s);
        return matcher.find();
    }

    private class Login extends AsyncTask<String, Void, Void> {

        int isLoggedIn = 0;
        int userid = 0;
        String username = "";
        String usertype = "";
        @Override
        protected Void doInBackground(String... arg) {
            String stEmail = arg[0];
            String stPassword = arg[1];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email", stEmail));
            params.add(new BasicNameValuePair("password", stPassword));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_LOGIN);
            String json = jsonParser.makeServiceCall(URL_LOGIN, ServiceHandler.GET, params);
            Log.e("Response", "= "+json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray errors = jsonObj.getJSONArray("errors");
                        JSONArray details = jsonObj.getJSONArray("details");

                        if(errors.length() > 0) {
                            isLoggedIn = 0;
                            Toast.makeText(getActivity(), errors.get(0).toString(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            isLoggedIn = 1;
                            for(int i = 0; i < details.length(); i++) {
                                JSONObject userObj = (JSONObject) details.get(i);
                                Log.e("Details: ", " >>"+userObj);
                                userid = userObj.getInt("id");
                                usertype = userObj.getString("type");
                                username = userObj.getString("name");
                                session.setUserId(userid);
                                session.setUserName(username);
                                session.setUserType(usertype);
                                session.setLogInfo(1);
                            }
                        }
                    }
                } 
                catch (JSONException e) {
                    e.printStackTrace();
                }

            } 
            else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(isLoggedIn == 1) {
                Log.e("logg", isLoggedIn+" "+username);
                Toast.makeText(getActivity(), "Welcome "+username, Toast.LENGTH_SHORT);
                if(session.getUserType().equals("1")) {
                    Intent i = new Intent(getContext(), ViewerActivity.class);
                    startActivity(i);
                }
                if(session.getUserType().equals("2")) {
                    Intent i = new Intent(getContext(), UserActivity.class);
                    startActivity(i);
                }
            }
        }
    }
}
