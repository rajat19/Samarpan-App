package com.infroid.samarpan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UploadImage2Fragment extends Fragment implements View.OnClickListener {
	ServerLink link = new ServerLink();
    Session session;
    public final String UPLOAD_URL = link.URL_UPLOAD_PHOTO;
    String URL_PHOTO = link.URL_PHOTO;
    String URL_PROFILE = link.URL_PROFILE;
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    private Button buttonChoose;
    private Button buttonUpload, buttonView;
    private ImageView imageView;
    private Bitmap bitmap;
    ProgressDialog progressDialog;
    private Uri filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);
        buttonChoose = (Button) view.findViewById(R.id.buttonChoose);
        buttonUpload = (Button) view.findViewById(R.id.buttonUpload);
        buttonView = (Button) view.findViewById(R.id.buttonViewImage);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        session = new Session(getContext());
        new PhotoDetails().execute(session.getUserId());
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){

        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("currentuserid", session.getUserId()+"");

                String result = rh.sendPostRequest(UPLOAD_URL,data);
                Log.e("Resyl", result);
                return result;

            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            uploadImage();
        }
    }

    private class PhotoDetails extends AsyncTask<Integer, Void, String> {

        String photoname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Getting profile picture.....");
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
                        photoname = userObj.getString("photo");
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("JSON Data", "Sorry the image could not be loaded!");
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            if(flag == 1) {
                new DownloadImage().execute(photoname);
            }
            else
                Toast.makeText(getActivity(), "Didn't receive any data from server!", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Downloading profile picture.....");
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = URL_PHOTO + urls[0];
            Log.e("url = ", urlDisplay);
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}