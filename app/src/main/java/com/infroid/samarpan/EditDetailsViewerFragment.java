package com.infroid.samarpan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditDetailsViewerFragment extends Fragment implements View.OnClickListener{

    Button btnDOB, btnSubmit;
    TextView date_of_birth;
    int year, month, day, profileCreated = 0;
    DatePickerFragment picker = new DatePickerFragment();
    Bundle args;
    ProgressDialog progressDialog;
    EditText firstname, description, expertise_in, members, contact_work, contact_fax,
            contact_pager, contact_other, address_permanent, city_permanent, state_permanent, country_permanent, pincode_permanent, address_alternate,
            city_alternate, state_alternate, country_alternate, pincode_alternate, email_work, email_other,
            fb, google, linkedin, skype, website;
    ServerLink link = new ServerLink();
    Session session;
    FragmentSwitchListener mCallback;
    Details completeDetail = new Details();
    public String URL_EDIT = link.URL_EDIT;
    public String URL_PROFILE = link.URL_PROFILE;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_REGEX = Pattern.compile("^[0-9]{0,10}$", Pattern.CASE_INSENSITIVE);
    public EditDetailsViewerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_registration, container, false);
        session = new Session(getContext());
        btnDOB = (Button) view.findViewById(R.id.btnDOB);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        date_of_birth = (TextView) view.findViewById(R.id.date_of_birth);
        firstname = (EditText) view.findViewById(R.id.fillFirstName);
        description = (EditText) view.findViewById(R.id.description);
        expertise_in = (EditText) view.findViewById(R.id.expertise_in);
        members = (EditText) view.findViewById(R.id.members);
        contact_work = (EditText) view.findViewById(R.id.contact_work);
        contact_fax = (EditText) view.findViewById(R.id.contact_fax);
        contact_pager = (EditText) view.findViewById(R.id.contact_pager);
        contact_other = (EditText) view.findViewById(R.id.contact_other);
        email_work = (EditText) view.findViewById(R.id.email_work);
        email_other = (EditText) view.findViewById(R.id.email_other);
        address_permanent = (EditText) view.findViewById(R.id.address_permanent);
        city_permanent = (EditText) view.findViewById(R.id.city_permanent);
        state_permanent = (EditText) view.findViewById(R.id.state_permanent);
        country_permanent = (EditText) view.findViewById(R.id.country_permanent);
        pincode_permanent = (EditText) view.findViewById(R.id.pincode_permanent);
        address_alternate = (EditText) view.findViewById(R.id.address_alternate);
        city_alternate = (EditText) view.findViewById(R.id.city_alternate);
        state_alternate = (EditText) view.findViewById(R.id.state_alternate);
        country_alternate = (EditText) view.findViewById(R.id.country_alternate);
        pincode_alternate = (EditText) view.findViewById(R.id.pincode_alternate);
        fb = (EditText) view.findViewById(R.id.fb);
        google = (EditText) view.findViewById(R.id.google);
        linkedin = (EditText) view.findViewById(R.id.linkedin);
        skype = (EditText) view.findViewById(R.id.skype);
        website = (EditText) view.findViewById(R.id.website);
        new Profile().execute(session.getUserId());
        btnDOB.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDOB:
                args = new Bundle();
                args.putInt("year", year);
                args.putInt("month", month);
                args.putInt("day", day);
                picker.setArguments(args);
                picker.setCallback(onDateB);
                picker.show(getFragmentManager(), "datePicker");
                break;
            case R.id.btnSubmit:
                getFilledDetails();
                int flag = validateFilledDetails();
                if(flag == 0) {
                    /*No errors*/
                    new Update().execute();
                }
                break;
        }
    }

    public void fillProfile(Details details) {
        firstname.setText(details.getFirstname());
        date_of_birth.setText(details.getDate_of_birth());
        expertise_in.setText(details.getExpertise_in());
        members.setText(details.getMembers());
        contact_work.setText(details.getContact_work());
        contact_other.setText(details.getContact_other());
        contact_fax.setText(details.getContact_fax());
        contact_pager.setText(details.getContact_pager());
        address_permanent.setText(details.getAddress_permanent());
        address_alternate.setText(details.getAddress_alternate());
        city_permanent.setText(details.getCity_permanent());
        city_alternate.setText(details.getCity_alternate());
        state_permanent.setText(details.getState_permanent());
        state_alternate.setText(details.getState_alternate());
        country_permanent.setText(details.getCountry_permanent());
        country_alternate.setText(details.getCountry_alternate());
        pincode_permanent.setText(details.getPincode_permanent());
        pincode_alternate.setText(details.getPincode_alternate());
        email_work.setText(details.getEmail_work());
        email_other.setText(details.getEmail_other());
        skype.setText(details.getSkype());
        fb.setText(details.getFb());
        google.setText(details.getGoogle());
        linkedin.setText(details.getLinkedin());
        website.setText(details.getWebsite());
        description.setText(details.getDescription());
    }

    public void setCurrentDateOnView() {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    DatePickerDialog.OnDateSetListener onDateB = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int newyear, int monthOfYear, int dayOfMonth) {
            year = newyear;
            month = monthOfYear;
            day = dayOfMonth;
            date_of_birth.setText(new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year).append(" "));
        }
    };

    public void getFilledDetails() {
        completeDetail.setFirstname(firstname.getText().toString());
        completeDetail.setDescription(description.getText().toString());
        completeDetail.setExpertise_in(expertise_in.getText().toString());
        completeDetail.setMembers(members.getText().toString());
        completeDetail.setEmail_work(email_work.getText().toString());
        completeDetail.setContact_work(contact_work.getText().toString());
        completeDetail.setContact_fax(contact_fax.getText().toString());
        completeDetail.setContact_pager(contact_pager.getText().toString());
        completeDetail.setContact_other(contact_other.getText().toString());
        completeDetail.setAddress_permanent(address_permanent.getText().toString());
        completeDetail.setCity_permanent(city_permanent.getText().toString());
        completeDetail.setState_permanent(state_permanent.getText().toString());
        completeDetail.setCountry_permanent(country_permanent.getText().toString());
        completeDetail.setPincode_permanent(pincode_permanent.getText().toString());
        completeDetail.setAddress_alternate(address_alternate.getText().toString());
        completeDetail.setCity_alternate(city_alternate.getText().toString());
        completeDetail.setState_alternate(state_alternate.getText().toString());
        completeDetail.setCountry_alternate(country_alternate.getText().toString());
        completeDetail.setPincode_alternate(pincode_alternate.getText().toString());
        completeDetail.setDate_of_birth(date_of_birth.getText().toString());
    }

    public int validateFilledDetails() {
        int flag = 0;
        /*First name should not be empty*/
        if (completeDetail.getFirstname().isEmpty()) {
            Toast.makeText(getActivity(), "The firstname field is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        /*Contacts should only be numbers*/
        if(!completeDetail.getContact_work().isEmpty() && !isPhone(completeDetail.getContact_work())){
            Toast.makeText(getActivity(), "Not a valid office contact no.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(!completeDetail.getContact_other().isEmpty() && !isPhone(completeDetail.getContact_other())){
            Toast.makeText(getActivity(), "Not a valid other contact no.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(!completeDetail.getEmail_work().isEmpty() &&!isEmail(completeDetail.getEmail_work())) {
            Toast.makeText(getActivity(), "Not a valid office email address.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(!completeDetail.getEmail_other().isEmpty() &&!isEmail(completeDetail.getEmail_other())) {
            Toast.makeText(getActivity(), "Not a valid other email address.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        return flag;
    }

    private static boolean isEmail(String s) {
        Matcher matcher = EMAIL_REGEX.matcher(s);
        return matcher.find();
    }

    private static boolean isPhone(String s) {
        Matcher matcher = PHONE_REGEX.matcher(s);
        return matcher.find();
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
                        completeDetail.setFirstname(userObj.getString("firstname"));
                        completeDetail.setDate_of_birth(userObj.getString("date_of_birth2"));
                        completeDetail.setExpertise_in(userObj.getString("expertise_in"));
                        completeDetail.setMembers(userObj.getString("members"));
                        completeDetail.setContact_work(userObj.getString("contact_work"));
                        completeDetail.setContact_other(userObj.getString("contact_other"));
                        completeDetail.setContact_fax(userObj.getString("contact_fax"));
                        completeDetail.setContact_pager(userObj.getString("contact_pager"));
                        completeDetail.setAddress_permanent(userObj.getString("address_permanent"));
                        completeDetail.setCity_permanent(userObj.getString("city_permanent"));
                        completeDetail.setState_permanent(userObj.getString("state_permanent"));
                        completeDetail.setCountry_permanent(userObj.getString("country_permanent"));
                        completeDetail.setPincode_permanent(userObj.getString("pincode_permanent"));
                        completeDetail.setAddress_alternate(userObj.getString("address_alternate"));
                        completeDetail.setCity_alternate(userObj.getString("city_alternate"));
                        completeDetail.setState_alternate(userObj.getString("state_alternate"));
                        completeDetail.setCountry_alternate(userObj.getString("country_alternate"));
                        completeDetail.setPincode_alternate(userObj.getString("pincode_alternate"));
                        completeDetail.setEmail_work(userObj.getString("email_work"));
                        completeDetail.setEmail_other(userObj.getString("email_other"));
                        completeDetail.setSkype(userObj.getString("skype"));
                        completeDetail.setFb(userObj.getString("fb"));
                        completeDetail.setGoogle(userObj.getString("google"));
                        completeDetail.setLinkedin(userObj.getString("linkedin"));
                        completeDetail.setWebsite(userObj.getString("website"));
                        completeDetail.setDescription(userObj.getString("description"));
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
            if(flag == 1) {
                setCurrentDateOnView();
                fillProfile(completeDetail);
            }
            else
                Toast.makeText(getActivity(), "Didn't receive any data from server!", Toast.LENGTH_SHORT).show();

            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    private class Update extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void ...arg) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", session.getUserId().toString()));
            params.add(new BasicNameValuePair("firstname", completeDetail.getFirstname()));
            params.add(new BasicNameValuePair("date_of_birth", completeDetail.getDate_of_birth()));
            params.add(new BasicNameValuePair("contact_work", completeDetail.getContact_work()));
            params.add(new BasicNameValuePair("contact_other", completeDetail.getContact_other()));
            params.add(new BasicNameValuePair("contact_fax", completeDetail.getContact_fax()));
            params.add(new BasicNameValuePair("contact_pager", completeDetail.getContact_pager()));
            params.add(new BasicNameValuePair("members", completeDetail.getMembers()));
            params.add(new BasicNameValuePair("email_work", completeDetail.getEmail_work()));
            params.add(new BasicNameValuePair("email_other", completeDetail.getEmail_other()));
            params.add(new BasicNameValuePair("description", completeDetail.getDescription()));
            params.add(new BasicNameValuePair("expertise_in", completeDetail.getExpertise_in()));
            params.add(new BasicNameValuePair("address_permanent", completeDetail.getAddress_permanent()));
            params.add(new BasicNameValuePair("city_permanent", completeDetail.getCity_permanent()));
            params.add(new BasicNameValuePair("state_permanent", completeDetail.getState_permanent()));
            params.add(new BasicNameValuePair("country_permanent", completeDetail.getCountry_permanent()));
            params.add(new BasicNameValuePair("pincode_permanent", completeDetail.getPincode_permanent()));
            params.add(new BasicNameValuePair("address_alternate", completeDetail.getAddress_alternate()));
            params.add(new BasicNameValuePair("city_alternate", completeDetail.getCity_alternate()));
            params.add(new BasicNameValuePair("state_alternate", completeDetail.getState_alternate()));
            params.add(new BasicNameValuePair("country_alternate", completeDetail.getCountry_alternate()));
            params.add(new BasicNameValuePair("pincode_alternate", completeDetail.getPincode_alternate()));
            params.add(new BasicNameValuePair("fb", completeDetail.getFb()));
            params.add(new BasicNameValuePair("google", completeDetail.getGoogle()));
            params.add(new BasicNameValuePair("linkedin", completeDetail.getLinkedin()));
            params.add(new BasicNameValuePair("skype", completeDetail.getSkype()));
            params.add(new BasicNameValuePair("website", completeDetail.getWebsite()));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_EDIT);
            String json = jsonParser.makeServiceCall(URL_EDIT, ServiceHandler.GET, params);
            Log.e("Response", "= " + json);
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray errors = jsonObj.getJSONArray("errors");
                        JSONArray details = jsonObj.getJSONArray("details");

                        if (errors.length() > 0) {
                            profileCreated = 0;
                            Toast.makeText(getActivity(), errors.get(0).toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            profileCreated = 1;
                            for (int i = 0; i < details.length(); i++) {
                                JSONObject userObj = (JSONObject) details.get(i);
                                Log.e("Details: ", " >>" + userObj);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (profileCreated == 1) {
                mCallback.switchFragment(1);
            }
        }
    }
}
