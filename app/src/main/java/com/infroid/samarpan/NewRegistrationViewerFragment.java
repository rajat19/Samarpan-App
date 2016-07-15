package com.infroid.samarpan;

import android.app.Activity;
import android.app.DatePickerDialog;
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

public class NewRegistrationViewerFragment extends Fragment implements View.OnClickListener{

    Button btnDOB, btnSubmit;
    TextView date_of_birth;
    int year, month, day, profileCreated = 0;
    DatePickerFragment picker = new DatePickerFragment();
    Bundle args;
    EditText firstname, description, expertise_in, members, contact_work, contact_fax,
            contact_pager, contact_other, address_permanent, city_permanent, state_permanent, country_permanent, pincode_permanent, address_alternate,
            city_alternate, state_alternate, country_alternate, pincode_alternate, email_work, email_other,
            fb, google, linkedin, skype, website;
    ServerLink link = new ServerLink();
    Session session;
    FragmentSwitchListener mCallback;
    Details detailsNew = new Details();
    public String URL_NEW = link.URL_NEW;
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_REGEX = Pattern.compile("^[0-9]{0,10}$", Pattern.CASE_INSENSITIVE);
    public NewRegistrationViewerFragment() {
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
        setCurrentDateOnView();
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
                    new NewRegistration().execute();
                }
                break;
        }
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
        detailsNew.setFirstname(firstname.getText().toString());
        detailsNew.setDescription(description.getText().toString());
        detailsNew.setExpertise_in(expertise_in.getText().toString());
        detailsNew.setMembers(members.getText().toString());
        detailsNew.setEmail_work(email_work.getText().toString());
        detailsNew.setContact_work(contact_work.getText().toString());
        detailsNew.setContact_fax(contact_fax.getText().toString());
        detailsNew.setContact_pager(contact_pager.getText().toString());
        detailsNew.setContact_other(contact_other.getText().toString());
        detailsNew.setAddress_permanent(address_permanent.getText().toString());
        detailsNew.setCity_permanent(city_permanent.getText().toString());
        detailsNew.setState_permanent(state_permanent.getText().toString());
        detailsNew.setCountry_permanent(country_permanent.getText().toString());
        detailsNew.setPincode_permanent(pincode_permanent.getText().toString());
        detailsNew.setAddress_alternate(address_alternate.getText().toString());
        detailsNew.setCity_alternate(city_alternate.getText().toString());
        detailsNew.setState_alternate(state_alternate.getText().toString());
        detailsNew.setCountry_alternate(country_alternate.getText().toString());
        detailsNew.setPincode_alternate(pincode_alternate.getText().toString());
        detailsNew.setDate_of_birth(date_of_birth.getText().toString());
    }

    public int validateFilledDetails() {
        int flag = 0;
        /*First name should not be empty*/
        if (detailsNew.getFirstname().isEmpty()) {
            Toast.makeText(getActivity(), "The firstname field is required.", Toast.LENGTH_SHORT).show();
            flag++;
        }

        /*Contacts should only be numbers*/
        if(!detailsNew.getContact_work().isEmpty() && !isPhone(detailsNew.getContact_work())){
            Toast.makeText(getActivity(), "Not a valid office contact no.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(!detailsNew.getContact_other().isEmpty() && !isPhone(detailsNew.getContact_other())){
            Toast.makeText(getActivity(), "Not a valid other contact no.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(!detailsNew.getEmail_work().isEmpty() &&!isEmail(detailsNew.getEmail_work())) {
            Toast.makeText(getActivity(), "Not a valid office email address.", Toast.LENGTH_SHORT).show();
            flag++;
        }
        if(!detailsNew.getEmail_other().isEmpty() &&!isEmail(detailsNew.getEmail_other())) {
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

    private class NewRegistration extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void ...arg) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("currentuserid", session.getUserId().toString()));
            params.add(new BasicNameValuePair("firstname", detailsNew.getFirstname()));
            params.add(new BasicNameValuePair("date_of_birth", detailsNew.getDate_of_birth()));
            params.add(new BasicNameValuePair("contact_work", detailsNew.getContact_work()));
            params.add(new BasicNameValuePair("contact_other", detailsNew.getContact_other()));
            params.add(new BasicNameValuePair("contact_fax", detailsNew.getContact_fax()));
            params.add(new BasicNameValuePair("contact_pager", detailsNew.getContact_pager()));
            params.add(new BasicNameValuePair("members", detailsNew.getMembers()));
            params.add(new BasicNameValuePair("email_work", detailsNew.getEmail_work()));
            params.add(new BasicNameValuePair("email_other", detailsNew.getEmail_other()));
            params.add(new BasicNameValuePair("description", detailsNew.getDescription()));
            params.add(new BasicNameValuePair("expertise_in", detailsNew.getExpertise_in()));
            params.add(new BasicNameValuePair("address_permanent", detailsNew.getAddress_permanent()));
            params.add(new BasicNameValuePair("city_permanent", detailsNew.getCity_permanent()));
            params.add(new BasicNameValuePair("state_permanent", detailsNew.getState_permanent()));
            params.add(new BasicNameValuePair("country_permanent", detailsNew.getCountry_permanent()));
            params.add(new BasicNameValuePair("pincode_permanent", detailsNew.getPincode_permanent()));
            params.add(new BasicNameValuePair("address_alternate", detailsNew.getAddress_alternate()));
            params.add(new BasicNameValuePair("city_alternate", detailsNew.getCity_alternate()));
            params.add(new BasicNameValuePair("state_alternate", detailsNew.getState_alternate()));
            params.add(new BasicNameValuePair("country_alternate", detailsNew.getCountry_alternate()));
            params.add(new BasicNameValuePair("pincode_alternate", detailsNew.getPincode_alternate()));
            params.add(new BasicNameValuePair("fb", detailsNew.getFb()));
            params.add(new BasicNameValuePair("google", detailsNew.getGoogle()));
            params.add(new BasicNameValuePair("linkedin", detailsNew.getLinkedin()));
            params.add(new BasicNameValuePair("skype", detailsNew.getSkype()));
            params.add(new BasicNameValuePair("website", detailsNew.getWebsite()));

            ServiceHandler jsonParser = new ServiceHandler();
            Log.e("Address = ", URL_NEW);
            String json = jsonParser.makeServiceCall(URL_NEW, ServiceHandler.GET, params);
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
