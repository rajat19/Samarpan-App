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
    
    public class EditDetailsFragment extends Fragment implements View.OnClickListener{
    
        Button btnDOB, btnRetire, btnCV, btnPhoto, btnSubmit;
        TextView date_of_birth, retirement;
        int year, month, day, profileCreated = 0;
        DatePickerFragment picker = new DatePickerFragment();
        Bundle args;
        ProgressDialog progressDialog;
        EditText firstname, middlename, lastname, biography, expertise_in, goals, interests,
                contact_home, contact_fax, contact_pager, contact_other, address_permanent,
                city_permanent, state_permanent, country_permanent, pincode_permanent, address_current,
                city_current, state_current, country_current, pincode_current, address_alternate,
                city_alternate, state_alternate, country_alternate, pincode_alternate, email_other, fb,
                google, linkedin, skype, website;
        AppCompatRadioButton maleRadio, femaleRadio, otherRadio;
        ServerLink link = new ServerLink();
        Session session;
        FragmentSwitchListener mCallback;
        Details completeDetail = new Details();
        public String URL_EDIT = link.URL_EDIT;
        public String URL_PROFILE = link.URL_PROFILE;
        private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        private static final Pattern PHONE_REGEX = Pattern.compile("^[0-9]{0,10}$", Pattern.CASE_INSENSITIVE);
        public EditDetailsFragment() {
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
            btnRetire = (Button) view.findViewById(R.id.btnRetire);
            // btnPhoto = (Button) view.findViewById(R.id.btnPhoto);
            // btnCV = (Button) view.findViewById(R.id.btnCV);
            btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
            date_of_birth = (TextView) view.findViewById(R.id.date_of_birth);
            retirement = (TextView) view.findViewById(R.id.retirement);
            firstname = (EditText) view.findViewById(R.id.fillFirstName);
            middlename = (EditText) view.findViewById(R.id.fillMiddleName);
            lastname = (EditText) view.findViewById(R.id.fillLastName);
            biography = (EditText) view.findViewById(R.id.biography);
            expertise_in = (EditText) view.findViewById(R.id.expertise_in);
            goals = (EditText) view.findViewById(R.id.goals);
            interests = (EditText) view.findViewById(R.id.interests);
            contact_home = (EditText) view.findViewById(R.id.contact_home);
            contact_fax = (EditText) view.findViewById(R.id.contact_fax);
            contact_pager = (EditText) view.findViewById(R.id.contact_pager);
            contact_other = (EditText) view.findViewById(R.id.contact_other);
            email_other = (EditText) view.findViewById(R.id.email_other);
            address_permanent = (EditText) view.findViewById(R.id.address_permanent);
            city_permanent = (EditText) view.findViewById(R.id.city_permanent);
            state_permanent = (EditText) view.findViewById(R.id.state_permanent);
            country_permanent = (EditText) view.findViewById(R.id.country_permanent);
            pincode_permanent = (EditText) view.findViewById(R.id.pincode_permanent);
            address_current = (EditText) view.findViewById(R.id.address_current);
            city_current = (EditText) view.findViewById(R.id.city_current);
            state_current = (EditText) view.findViewById(R.id.state_current);
            country_current = (EditText) view.findViewById(R.id.country_current);
            pincode_current = (EditText) view.findViewById(R.id.pincode_current);
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
            maleRadio = (AppCompatRadioButton) view.findViewById(R.id.genderMale);
            femaleRadio = (AppCompatRadioButton) view.findViewById(R.id.genderFemale);
            otherRadio = (AppCompatRadioButton) view.findViewById(R.id.genderOther);
            setCurrentDateOnView();
            new Profile().execute(session.getUserId());
            btnDOB.setOnClickListener(this);
            btnRetire.setOnClickListener(this);
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
                case R.id.btnRetire:
                    args = new Bundle();
                    args.putInt("year", year);
                    args.putInt("month", month);
                    args.putInt("day", day);
                    picker.setArguments(args);
                    picker.setCallback(onDateR);
                    picker.show(getFragmentManager(), "datePicker");
                    break;
                case R.id.btnSubmit:
                    getFilledDetails();
                    int flag = validateFilledDetails();
                    if(flag == 0) {
                        /*No errors*/
                        new Edit().execute();
                    }
                    break;
            }
        }
    
        public void fillProfile(Details details) {
            firstname.setText(details.getFirstname());
            middlename.setText(details.getMiddlename());
            lastname.setText(details.getLastname());
            date_of_birth.setText(details.getDate_of_birth());
            expertise_in.setText(details.getExpertise_in());
            retirement.setText(details.getRetirement());
            String gender = details.getGender();
            if(gender.equalsIgnoreCase("Male"))
                maleRadio.setChecked(true);
            else if(gender.equalsIgnoreCase("Female"))
                femaleRadio.setChecked(true);
            else if(gender.equalsIgnoreCase("Other"))
                otherRadio.setChecked(true);
            contact_home.setText(details.getContact_home());
            contact_other.setText(details.getContact_other());
            contact_fax.setText(details.getContact_fax());
            contact_pager.setText(details.getContact_pager());
            address_permanent.setText(details.getAddress_permanent());
            address_current.setText(details.getAddress_current());
            address_alternate.setText(details.getAddress_alternate());
            city_permanent.setText(details.getCity_permanent());
            city_current.setText(details.getCity_current());
            city_alternate.setText(details.getCity_alternate());
            state_permanent.setText(details.getState_permanent());
            state_current.setText(details.getState_current());
            state_alternate.setText(details.getState_alternate());
            country_permanent.setText(details.getCountry_permanent());
            country_current.setText(details.getCountry_current());
            country_alternate.setText(details.getCountry_alternate());
            pincode_permanent.setText(details.getPincode_permanent());
            pincode_current.setText(details.getPincode_current());
            pincode_alternate.setText(details.getPincode_alternate());
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
    
        DatePickerDialog.OnDateSetListener onDateR = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int newyear, int monthOfYear, int dayOfMonth) {
                year = newyear;
                month = monthOfYear;
                day = dayOfMonth;
                retirement.setText(new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year).append(" "));
            }
        };
    
        public void getFilledDetails() {
            completeDetail.setFirstname(firstname.getText().toString());
            completeDetail.setMiddlename(middlename.getText().toString());
            completeDetail.setLastname(lastname.getText().toString());
            completeDetail.setBiography(biography.getText().toString());
            completeDetail.setExpertise_in(expertise_in.getText().toString());
            completeDetail.setGoals(goals.getText().toString());
            completeDetail.setInterests(interests.getText().toString());
            completeDetail.setContact_home(contact_home.getText().toString());
            completeDetail.setContact_fax(contact_fax.getText().toString());
            completeDetail.setContact_pager(contact_pager.getText().toString());
            completeDetail.setContact_other(contact_other.getText().toString());
            completeDetail.setAddress_permanent(address_permanent.getText().toString());
            completeDetail.setCity_permanent(city_permanent.getText().toString());
            completeDetail.setState_permanent(state_permanent.getText().toString());
            completeDetail.setCountry_permanent(country_permanent.getText().toString());
            completeDetail.setPincode_permanent(pincode_permanent.getText().toString());
            completeDetail.setAddress_current(address_current.getText().toString());
            completeDetail.setCity_current(city_current.getText().toString());
            completeDetail.setState_current(state_current.getText().toString());
            completeDetail.setCountry_current(country_current.getText().toString());
            completeDetail.setPincode_current(pincode_current.getText().toString());
            completeDetail.setAddress_alternate(address_alternate.getText().toString());
            completeDetail.setCity_alternate(city_alternate.getText().toString());
            completeDetail.setState_alternate(state_alternate.getText().toString());
            completeDetail.setCountry_alternate(country_alternate.getText().toString());
            completeDetail.setPincode_alternate(pincode_alternate.getText().toString());
            completeDetail.setDate_of_birth(date_of_birth.getText().toString());
            completeDetail.setRetirement(retirement.getText().toString());
            if(maleRadio.isChecked()) {
                completeDetail.setGender("Male");
            }
            else if(femaleRadio.isChecked()) {
                completeDetail.setGender("Female");
            }
            else if(otherRadio.isChecked()) {
                completeDetail.setGender("Other");
            }
        }
    
        public int validateFilledDetails() {
            int flag = 0;
            /*First name should not be empty*/
            if (completeDetail.getFirstname().isEmpty()) {
                Toast.makeText(getActivity(), "The firstname field is required.", Toast.LENGTH_SHORT).show();
                flag++;
            }
            /*Contacts should only be numbers*/
            if(!completeDetail.getContact_home().isEmpty() && !isPhone(completeDetail.getContact_home())){
                Toast.makeText(getActivity(), "Not a valid home contact no.", Toast.LENGTH_SHORT).show();
                flag++;
            }
            if(!completeDetail.getContact_other().isEmpty() && !isPhone(completeDetail.getContact_other())){
                Toast.makeText(getActivity(), "Not a valid other contact no.", Toast.LENGTH_SHORT).show();
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
                            completeDetail.setMiddlename(userObj.getString("middlename"));
                            completeDetail.setLastname(userObj.getString("lastname"));
                            completeDetail.setDate_of_birth(userObj.getString("date_of_birth2"));
                            completeDetail.setExpertise_in(userObj.getString("expertise_in"));
                            completeDetail.setRetirement(userObj.getString("retirement2"));
                            completeDetail.setGender(userObj.getString("gender"));
                            completeDetail.setContact_home(userObj.getString("contact_home"));
                            completeDetail.setContact_other(userObj.getString("contact_other"));
                            completeDetail.setContact_fax(userObj.getString("contact_fax"));
                            completeDetail.setContact_pager(userObj.getString("contact_pager"));
                            completeDetail.setAddress_permanent(userObj.getString("address_permanent"));
                            completeDetail.setCity_permanent(userObj.getString("city_permanent"));
                            completeDetail.setState_permanent(userObj.getString("state_permanent"));
                            completeDetail.setCountry_permanent(userObj.getString("country_permanent"));
                            completeDetail.setPincode_permanent(userObj.getString("pincode_permanent"));
                            completeDetail.setAddress_current(userObj.getString("address_current"));
                            completeDetail.setCity_current(userObj.getString("city_current"));
                            completeDetail.setState_current(userObj.getString("state_current"));
                            completeDetail.setCountry_current(userObj.getString("country_current"));
                            completeDetail.setPincode_current(userObj.getString("pincode_current"));
                            completeDetail.setAddress_alternate(userObj.getString("address_alternate"));
                            completeDetail.setCity_alternate(userObj.getString("city_alternate"));
                            completeDetail.setState_alternate(userObj.getString("state_alternate"));
                            completeDetail.setCountry_alternate(userObj.getString("country_alternate"));
                            completeDetail.setPincode_alternate(userObj.getString("pincode_alternate"));
                            completeDetail.setEmail_other(userObj.getString("email_other"));
                            completeDetail.setSkype(userObj.getString("skype"));
                            completeDetail.setFb(userObj.getString("fb"));
                            completeDetail.setGoogle(userObj.getString("google"));
                            completeDetail.setLinkedin(userObj.getString("linkedin"));
                            completeDetail.setWebsite(userObj.getString("website"));
                            completeDetail.setGoals(userObj.getString("goals"));
                            completeDetail.setInterests(userObj.getString("interests"));
                            completeDetail.setBiography(userObj.getString("biography"));
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
    
        private class Edit extends AsyncTask<Void, Void, Void> {
    
            @Override
            protected Void doInBackground(Void ...arg) {
                List<NameValuePair> params = new ArrayList<>();
                Log.e("req", params+"");
                params.add(new BasicNameValuePair("id", session.getUserId().toString()));
                params.add(new BasicNameValuePair("firstname", completeDetail.getFirstname()));
                params.add(new BasicNameValuePair("middlename", completeDetail.getMiddlename()));
                params.add(new BasicNameValuePair("lastname", completeDetail.getLastname()));
                params.add(new BasicNameValuePair("gender", completeDetail.getGender()));
                params.add(new BasicNameValuePair("date_of_birth", completeDetail.getDate_of_birth()));
                params.add(new BasicNameValuePair("retirement", completeDetail.getRetirement()));
                params.add(new BasicNameValuePair("contact_home", completeDetail.getContact_home()));
                params.add(new BasicNameValuePair("contact_other", completeDetail.getContact_other()));
                params.add(new BasicNameValuePair("contact_fax", completeDetail.getContact_fax()));
                params.add(new BasicNameValuePair("contact_pager", completeDetail.getContact_pager()));
                params.add(new BasicNameValuePair("email_other", completeDetail.getEmail_other()));
                params.add(new BasicNameValuePair("biography", completeDetail.getBiography()));
                params.add(new BasicNameValuePair("goals", completeDetail.getGoals()));
                params.add(new BasicNameValuePair("interests", completeDetail.getInterests()));
                params.add(new BasicNameValuePair("expertise_in", completeDetail.getExpertise_in()));
                params.add(new BasicNameValuePair("address_permanent", completeDetail.getAddress_permanent()));
                params.add(new BasicNameValuePair("city_permanent", completeDetail.getCity_permanent()));
                params.add(new BasicNameValuePair("state_permanent", completeDetail.getState_permanent()));
                params.add(new BasicNameValuePair("country_permanent", completeDetail.getCountry_permanent()));
                params.add(new BasicNameValuePair("pincode_permanent", completeDetail.getPincode_permanent()));
                params.add(new BasicNameValuePair("address_current", completeDetail.getAddress_current()));
                params.add(new BasicNameValuePair("city_current", completeDetail.getCity_current()));
                params.add(new BasicNameValuePair("state_current", completeDetail.getState_current()));
                params.add(new BasicNameValuePair("country_current", completeDetail.getCountry_current()));
                params.add(new BasicNameValuePair("pincode_current", completeDetail.getPincode_current()));
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
    //                Log.e("logg", isLoggedIn + " " + username);
    //                Toast.makeText(getActivity(), "Welcome " + username, Toast.LENGTH_SHORT);
    //                Intent i = new Intent(getContext(), UserActivity.class);
    //                startActivity(i);
                    mCallback.switchFragment(1);
                }
            }
        }
    }
