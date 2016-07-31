package com.infroid.samarpan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    TextView name, date_of_birth, retirement, expertise_in, gender, contact, contact_home,
            contact_other, contact_fax, contact_pager, address_permanent, address_current,
            address_alternate, email, email_other, skype, fb, google, linkedin, website, goals,
            interests, biography;
    ImageView photo;
    FloatingActionButton fab;
    Details completeDetail = new Details();
    ServerLink link = new ServerLink();
    FragmentSwitchListener mCallback;
    ProgressDialog progressDialog;
    Session session;
    int session_user_id;
    public String URL_PROFILE = link.URL_PROFILE;
    public String URL_PHOTO = link.URL_PHOTO;
    public ProfileFragment() {
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
        session_user_id = session.getUserId();
        Log.e("userid", session_user_id+"");
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        name = (TextView) view.findViewById(R.id.name);
        date_of_birth = (TextView) view.findViewById(R.id.date_of_birth);
        expertise_in = (TextView) view.findViewById(R.id.expertise_in);
        retirement = (TextView) view.findViewById(R.id.retirement);
        gender = (TextView) view.findViewById(R.id.gender);
        contact = (TextView) view.findViewById(R.id.contact);
        contact_home = (TextView) view.findViewById(R.id.contact_home);
        contact_other = (TextView) view.findViewById(R.id.contact_other);
        contact_fax = (TextView) view.findViewById(R.id.contact_fax);
        contact_pager = (TextView) view.findViewById(R.id.contact_pager);
        address_permanent = (TextView) view.findViewById(R.id.address_permanent);
        address_current = (TextView) view.findViewById(R.id.address_current);
        address_alternate = (TextView) view.findViewById(R.id.address_alternate);
        email = (TextView) view.findViewById(R.id.email);
        email_other = (TextView) view.findViewById(R.id.email_other);
        skype = (TextView) view.findViewById(R.id.skype);
        fb = (TextView) view.findViewById(R.id.fb);
        google = (TextView) view.findViewById(R.id.google);
        linkedin = (TextView) view.findViewById(R.id.linkedin);
        website = (TextView) view.findViewById(R.id.website);
        goals = (TextView) view.findViewById(R.id.goals);
        interests = (TextView) view.findViewById(R.id.interests);
        biography = (TextView) view.findViewById(R.id.biography);
        photo = (ImageView) view.findViewById(R.id.photo);
        fab = (FloatingActionButton) view.findViewById(R.id.floatEdit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.switchFragment(2);
            }
        });
        new Profile().execute(session_user_id);
        return view;
    }

    public void fillProfile(Details details) {
        name.setText(details.getName());
        String dob = details.getDate_of_birth()+" ("+details.getAge()+") years old";
        date_of_birth.setText(dob);
        expertise_in.setText(details.getExpertise_in());
        String retire = details.getRetirement()+" (At the age of "+details.getRetirement_age()+")";
        retirement.setText(retire);
        gender.setText(details.getGender());
        contact.setText(details.getContact());
        contact_home.setText(details.getContact_home());
        contact_other.setText(details.getContact_other());
        contact_fax.setText(details.getContact_fax());
        contact_pager.setText(details.getContact_pager());
        address_permanent.setText(details.getAddress_permanent());
        address_current.setText(details.getAddress_current());
        address_alternate.setText(details.getAddress_alternate());
        email.setText(details.getEmail());
        email_other.setText(details.getEmail_other());
        skype.setText(details.getSkype());
        fb.setText(details.getFb());
        google.setText(details.getGoogle());
        linkedin.setText(details.getLinkedin());
        website.setText(details.getWebsite());
        goals.setText(details.getGoals());
        interests.setText(details.getInterests());
        biography.setText(details.getBiography());
    }

    private class Profile extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching data.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        int flag = 0;
        @Override
        protected String doInBackground(Integer... arg) {
            int uid = arg[0];
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", Integer.toString(uid)));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_PROFILE);
            String json = jsonParser.makeServiceCall(URL_PROFILE, ServiceHandler.GET, params);
            Log.e("Response", "= "+json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray details = jsonObj.getJSONArray("details");
                        flag = 1;
                        JSONObject userObj = (JSONObject) details.get(0);
                        Log.e("Details: ", " >>"+userObj);
                        completeDetail.setName(userObj.getString("name"));
                        completeDetail.setDate_of_birth(userObj.getString("date_of_birth2"));
                        completeDetail.setAge(userObj.getString("age"));
                        completeDetail.setExpertise_in(userObj.getString("expertise_in"));
                        completeDetail.setRetirement(userObj.getString("retirement2"));
                        completeDetail.setRetirement_age(userObj.getString("retirement_age"));
                        completeDetail.setGender(userObj.getString("gender"));
                        completeDetail.setContact(userObj.getString("contact"));
                        completeDetail.setContact_home(userObj.getString("contact_home"));
                        completeDetail.setContact_other(userObj.getString("contact_other"));
                        completeDetail.setContact_fax(userObj.getString("contact_fax"));
                        completeDetail.setContact_pager(userObj.getString("contact_pager"));
                        completeDetail.setAddress_permanent(userObj.getString("add_permanent"));
                        completeDetail.setPincode_permanent(userObj.getString("pincode_permanent"));
                        completeDetail.setAddress_current(userObj.getString("add_current"));
                        completeDetail.setPincode_current(userObj.getString("pincode_current"));
                        completeDetail.setPincode_alternate(userObj.getString("pincode_alternate"));
                        completeDetail.setAddress_alternate(userObj.getString("add_alternate"));
                        completeDetail.setEmail(userObj.getString("email"));
                        completeDetail.setEmail_other(userObj.getString("email_other"));
                        completeDetail.setSkype(userObj.getString("skype"));
                        completeDetail.setFb(userObj.getString("fb"));
                        completeDetail.setGoogle(userObj.getString("google"));
                        completeDetail.setLinkedin(userObj.getString("linkedin"));
                        completeDetail.setWebsite(userObj.getString("website"));
                        completeDetail.setGoals(userObj.getString("goals"));
                        completeDetail.setInterests(userObj.getString("interests"));
                        completeDetail.setBiography(userObj.getString("biography"));
                        completeDetail.setPhoto(userObj.getString("photo"));
                        completeDetail.setCv(userObj.getString("cv"));
                    }
                } 
                catch (JSONException e) {
                    e.printStackTrace();
                }
            } 
            else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            if(flag == 1) {
                // new DownloadImage().execute(completeDetail.getPhoto());
                String imageServerPath = URL_PHOTO + completeDetail.getPhoto();
                Picasso.with(getContext()).load(imageServerPath).into(photo);
                fillProfile(completeDetail);
            }
            else
                Toast.makeText(getActivity(), "Didn't receive any data from server!", Toast.LENGTH_SHORT).show();
        }
    }
}