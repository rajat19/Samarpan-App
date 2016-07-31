package com.infroid.samarpan;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    EditText txtForgotEmail;
    Button btnSend;
    ServerLink link = new ServerLink();
    String URL_FORGOT = link.URL_FORGOT;
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        txtForgotEmail = (EditText) view.findViewById(R.id.txtForgotEmail);
        btnSend = (Button) view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgot = txtForgotEmail.getText().toString();
                if(!forgot.equals("")) {
                    new SendResetLink().execute(forgot);
                }
            }
        });
        return view;
    }

    private class SendResetLink extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email", arg[0]));

            ServiceHandler jsonParser = new ServiceHandler();
            String json = jsonParser.makeServiceCall(URL_FORGOT, ServiceHandler.GET, params);
            if(json!=null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if(jsonObj!=null) {
                        String msg = jsonObj.getString("message");
                        String error = jsonObj.getString("errors");
                        if(error.length() == 0) {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
