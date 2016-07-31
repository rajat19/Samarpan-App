package com.infroid.samarpan;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class RegisterFragment extends Fragment implements View.OnClickListener {

    TextView textView, btnLog;
    EditText name, email, contact, password, password_confirmation;
    Button btnLogin, btnRegister;
    String sname, semail, scontact, spassword, spassword_confirmation;
    ServerLink link = new ServerLink();
    private String URL_REGISTER = link.URL_REGISTER;
    private Session session;
    FragmentSwitchListener mCallback;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        textView = (TextView) view.findViewById(R.id.txtSamarpan);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/samarn.ttf");
        textView.setTypeface(type);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name = (EditText) view.findViewById(R.id.name);
        email = (EditText) view.findViewById(R.id.email);
        contact = (EditText) view.findViewById(R.id.contact);
        password = (EditText) view.findViewById(R.id.password);
        password_confirmation = (EditText) view.findViewById(R.id.password_confirmation);
//        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLog = (TextView) view.findViewById(R.id.btnLogin);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);
        btnLog.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                sname = name.getText().toString();
                semail = email.getText().toString();
                scontact = contact.getText().toString();
                spassword = password.getText().toString();
                spassword_confirmation = password_confirmation.getText().toString();
                int flag = 0;
                if(sname.equals("")) {
                    Toast.makeText(getActivity(), "The name field is required.", Toast.LENGTH_SHORT).show();
                    flag++;
                }

                if(semail.equals("")) {
                    Toast.makeText(getActivity(), "The email field is required.", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                else if(!isEmail(semail)){
                    Toast.makeText(getActivity(), "Not a valid email address", Toast.LENGTH_SHORT).show();
                    flag++;
                }

                if(spassword.equals("")){
                    Toast.makeText(getActivity(), "The password field is required.", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                else if(spassword.length() < 6) {
                    Toast.makeText(getActivity(), "Password is too short. Must be of length greater than 6", Toast.LENGTH_SHORT).show();
                    flag++;
                }

                if(spassword.equals(spassword_confirmation)) {
//                    Toast.makeText(getActivity(),"Matc", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Confirmed Password do not match", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                if(flag == 0) {
                    /*Start async task to login the user*/
                    String arr[] = {sname, semail, scontact, spassword, spassword_confirmation};
                    new Register().execute(arr);
                }
                break;
            case R.id.btnLogin:
                mCallback.switchFragment(2);
                break;
        }
    }

    private static boolean isEmail(String s) {
        Matcher matcher = EMAIL_REGEX.matcher(s);
        return matcher.find();
    }

    private class Register extends AsyncTask<String, Void, Void> {

        int isLoggedIn = 0;
        String username = ""; int userid = 0;
        @Override
        protected Void doInBackground(String... arg) {
            String stName = arg[0];
            String stEmail = arg[1];
            String stContact = arg[2];
            String stPassword = arg[3];
            String stPasswordConfirmation = arg[4];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email", stEmail));
            params.add(new BasicNameValuePair("password", stPassword));
            params.add(new BasicNameValuePair("name", stName));
            params.add(new BasicNameValuePair("contact", stContact));
            params.add(new BasicNameValuePair("password_confirmation", stPasswordConfirmation));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_REGISTER);
            String json = jsonParser.makeServiceCall(URL_REGISTER, ServiceHandler.GET, params);
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
                                username = userObj.getString("name");
                                session.setUserId(userid);
                                session.setUserName(username);
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
                mCallback.switchFragment(2);
            }
        }
    }
}
