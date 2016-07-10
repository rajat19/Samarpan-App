package com.infroid.samarpan;

public class ServerLink {
    public String SERVER_ADDRESS;
    public String MOBILE_ADDRESS;
    public String URL_LOGIN;
    public String URL_REGISTER;
    public String URL_PHOTO;
    public String URL_USER_HOME;
    public String URL_PROFILE;
    public String URL_WORK_EXPERIENCE;
    public String URL_ADD_EXPERIENCE;

    public ServerLink() {
    	SERVER_ADDRESS = "http://192.168.200.2/Samarpan/public/";
    	URL_PHOTO = SERVER_ADDRESS + "photo/";
        MOBILE_ADDRESS = SERVER_ADDRESS + "mobile/";
        URL_LOGIN = MOBILE_ADDRESS + "login";
        URL_REGISTER = MOBILE_ADDRESS + "register";
        URL_USER_HOME = MOBILE_ADDRESS + "profile";
        URL_PROFILE = MOBILE_ADDRESS + "profile/view";
        URL_WORK_EXPERIENCE = MOBILE_ADDRESS + "profile/work_experience";
        URL_ADD_EXPERIENCE = MOBILE_ADDRESS + "profile/add_experience";
    }
}
