package com.infroid.samarpan;


import android.app.ProgressDialog;
import android.content.Intent;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    EditText email, name, message;
    Button send;
    ProgressDialog progressDialog;
    ServerLink link = new ServerLink();
    String URL_CONTACT = link.URL_CONTACT;
    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        email = (EditText) view.findViewById(R.id.email);
        name = (EditText) view.findViewById(R.id.name);
        message = (EditText) view.findViewById(R.id.message);
        send = (Button) view.findViewById(R.id.btnSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semail = email.getText().toString();
                String sname = name.getText().toString();
                String smessage = message.getText().toString();

            }
        });
        return view;
    }

    private class SendMessage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Sending Message...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... arg) {
            String stEmail = arg[0];
            String stName = arg[1];
            String stMessage = arg[2];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("email", stEmail));
            params.add(new BasicNameValuePair("name", stName));
            params.add(new BasicNameValuePair("message", stMessage));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_CONTACT);
            String json = jsonParser.makeServiceCall(URL_CONTACT, ServiceHandler.GET, params);
            Log.e("Response", "= "+json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray errors = jsonObj.getJSONArray("errors");
                        JSONArray details = jsonObj.getJSONArray("details");

                        if(errors.length() > 0) {
                            Toast.makeText(getActivity(), errors.get(0).toString(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), details.get(0).toString(), Toast.LENGTH_SHORT).show();
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
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
