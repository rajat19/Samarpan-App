package com.infroid.samarpan;

public class ServerLink {
    public String SERVER_ADDRESS;
    public String MOBILE_ADDRESS;
    public String URL_LOGIN; //login email and password
    public String URL_REGISTER; // register initial details
    public String URL_PHOTO; // link to webfolder containing photos
    public String URL_USER_HOME; // link for user home
    public String URL_PROFILE; // link to retrieve user details
    public String URL_NEW; // link to create new registration
    public String URL_WORK_EXPERIENCE; // link to retrieve work experiences
    public String URL_ADD_EXPERIENCE; // link to add new experiences
    public String URL_EDIT; // link to edit details
    public String URL_CV; // link to webfolder containing cv
    public String URL_UPLOAD_PHOTO; // link to route to start photo upload

    public ServerLink() {
    	SERVER_ADDRESS = "http://192.168.200.2/Samarpan/public/";
    	URL_PHOTO = SERVER_ADDRESS + "photo/";
        URL_CV = SERVER_ADDRESS + "cv/";
        MOBILE_ADDRESS = SERVER_ADDRESS + "mobile/";
        URL_LOGIN = MOBILE_ADDRESS + "login";
        URL_REGISTER = MOBILE_ADDRESS + "register";
        URL_USER_HOME = MOBILE_ADDRESS + "profile";
        URL_PROFILE = MOBILE_ADDRESS + "profile/view";
        URL_NEW = MOBILE_ADDRESS + "profile/new";
        URL_EDIT = MOBILE_ADDRESS + "profile/update";
        URL_WORK_EXPERIENCE = MOBILE_ADDRESS + "profile/work_experience";
        URL_ADD_EXPERIENCE = MOBILE_ADDRESS + "profile/add_experience";
        URL_UPLOAD_PHOTO = MOBILE_ADDRESS + "profile/upload/photo";
    }
}
