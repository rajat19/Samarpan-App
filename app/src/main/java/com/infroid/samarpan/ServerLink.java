package com.infroid.samarpan;

public class ServerLink {
    public String SERVER_ADDRESS;
    public String MOBILE_ADDRESS;
    public String URL_LOGIN; //login email and password
    public String URL_REGISTER; // register initial details
    public String URL_PHOTO; // link to webfolder containing photos
    public String URL_ADMIN_VIEWERS;
    public String URL_ADMIN_CITIZENS;
    public String URL_ADMIN_EDIT;
    public String URL_DEPARTMENTS;
    public String URL_USER_HOME; // link for user home
    public String URL_PROFILE; // link to retrieve user details
    public String URL_NEW; // link to create new registration
    public String URL_WORK_EXPERIENCE; // link to retrieve work experiences
    public String URL_ADD_EXPERIENCE; // link to add new experiences
    public String URL_EDIT; // link to edit details
    public String URL_CV; // link to webfolder containing cv
    public String URL_UPLOAD_PHOTO; // link to route to start photo upload
    public String SEARCH_COMPANY; //for profile viewer
    public String SEARCH_PRIVATE_CATEGORY;
    public String SEARCH_MINISTRY;
    public String SEARCH_DEPARTMENT;
    public String SEARCH_POSITION;
    public String SEARCH_COMPANY_NAME; // for senior citizen

    public ServerLink() {
    	SERVER_ADDRESS = "http://192.168.200.2/Samarpan/public/";
    	URL_PHOTO = SERVER_ADDRESS + "photo/";
        URL_CV = SERVER_ADDRESS + "cv/";
        MOBILE_ADDRESS = SERVER_ADDRESS + "mobile/";
        URL_LOGIN = MOBILE_ADDRESS + "login";
        URL_REGISTER = MOBILE_ADDRESS + "register";
        URL_ADMIN_VIEWERS = MOBILE_ADDRESS + "admin/search_viewers";
        URL_ADMIN_CITIZENS = MOBILE_ADDRESS + "admin/search_citizens";
        URL_ADMIN_EDIT = MOBILE_ADDRESS + "admin/update";
        URL_DEPARTMENTS = MOBILE_ADDRESS + "departments";
        URL_USER_HOME = MOBILE_ADDRESS + "profile";
        URL_PROFILE = MOBILE_ADDRESS + "profile/view";
        URL_NEW = MOBILE_ADDRESS + "profile/new";
        URL_EDIT = MOBILE_ADDRESS + "profile/update";
        URL_WORK_EXPERIENCE = MOBILE_ADDRESS + "profile/work_experience";
        URL_ADD_EXPERIENCE = MOBILE_ADDRESS + "profile/add_experience";
        URL_UPLOAD_PHOTO = MOBILE_ADDRESS + "profile/upload/photo";
        SEARCH_COMPANY = MOBILE_ADDRESS + "search/companies";
        SEARCH_PRIVATE_CATEGORY = MOBILE_ADDRESS + "search/category/private";
        SEARCH_MINISTRY = MOBILE_ADDRESS + "search/ministry";
        SEARCH_DEPARTMENT = MOBILE_ADDRESS + "search/department";
        SEARCH_POSITION = MOBILE_ADDRESS + "search/position";
        SEARCH_COMPANY_NAME = MOBILE_ADDRESS + "search/senior_citizen/company";
    }
}
