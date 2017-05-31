package com.mediax.mediaxapp.constant;

/**
 * Created by Mayank on 27/04/2016.
 */
public interface AppConstants {


    static final String CHAT_USERNAME = "ankit123";
    static final String CHAT_PASSWORD = "12345678";


    /*alert type*/
    public static final int ALERT_TYPE_NO_NETWORK = 0x01;
    public static final int ALERT_TYPE_LOGOUT = 0x02;
    public static final int ALERT_TYPE_DELETE_USER = 0x03;
    public static final int ALERT_TYPE_ADD_NEWS_ERROR = 0x04;
    public static final int ALERT_TYPE_IMAGE_UPLOAD = 0x05;

    /* animation type*/
    public static final int ANIMATION_SLIDE_UP = 0x01;
    public static final int ANIMATION_SLIDE_LEFT = 0x02;


    static final int MY_READ_PHONE_STATE_REQUEST = 0x01;

    /* splash screen*/
    public static final int SPLASH_TIME = 3000;

    /* Request Tag*/
    public static final int REQUEST_TAG_NO_RESULT = 0x01;
    public static final int REQUEST_TAG_SIGN_IN_ACTIVITY = 0x02;
    public static final int REQUEST_TAG_SIGN_UP_ACTIVITY = 0x03;
    public static final int REQUEST_TAG_FORGOT_PASSWORD_ACTIVITY = 0x04;
    public static final int REQUEST_TAG_ADD_NEWS_ACTIVITY = 0x05;

    public static int REQUEST_TAG_PICK_IMAGE = 0x06;
    public static int REQUEST_TAG_Image_Capture = 0x07;

    /* App Tag*/
    public static final String APP_NAME = "MediaX";

    public static final String IS_LOGIN = "isLogin";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "userName";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_PROFILE_NAME = "user_name";
    public static final String IS_USER_VALID = "isUserValid";



    /* App URL*/


    public static final String RESPONCE_ERROR = "error";
    public static final String RESPONCE_MESSAGE = "message";
    public static final String BASE_URL = "http://mediainc.in/mediax_api/v1/index.php";

    public static final String BASE_URL_IMAGES = "http://mediainc.in/mediax_api/images/";

    public static final String SIGNUP_URL = BASE_URL + "/createUser";
    public static final String SIGNIN_URL = BASE_URL + "/login";
    public static final String GET_CATEGORIES_URL = BASE_URL + "/getAllCategories";

    public static final String CHANGE_PASSWORD_URL = BASE_URL + "/changePwd";
    public static final String GET_NEWS_URL = BASE_URL + "/getNews";

    public static final String ADD_NEWS_URL = BASE_URL + "/addNews";
    public static final String GET_JOBS_URL = BASE_URL + "/getJobs";
    public static final String ADD_JOBS_URL = BASE_URL + "/addJob";
    public static final String GET_USER_DETAILS = BASE_URL + "/getUserDetails";

    public static final String GET_PERSONS_URL = BASE_URL + "/getAllPersons?search_string=";
    public static final String GET_COMPANY_URL = BASE_URL + "/getAllCompany?search_string=";

    public static final String GET_EMPLOYEES_URL = BASE_URL + "/getAllCompanyEmployees?company_id=";

    public static final String UPDATE_USER_URL = BASE_URL + "/updateUser";
    public static final String UPLOAD_NEWS_IMAGE = BASE_URL + "/uploadNewsImage";
    public static final String UPLOAD_PROFILE_PIC = BASE_URL + "/updateImage";

    public static final String ADD_PERSON_URL = BASE_URL + "/addPerson";
    public static final String DELETE_PERSON_URL = BASE_URL + "/deletePerson";
    public static final String EDIT_PERSON_URL = BASE_URL + "/editPerson";
    public static final String IS_USER_VERIFIED_URL = BASE_URL + "/isUserVerified";

    public static final String CONTACT_US_URL = BASE_URL + "/contactAdmin";

    public static final String FORGOT_PASSWORD_URL = BASE_URL + "/forgotPassword";


    /* Dashboard*/
    public static final int APP_EXIT_TIME = 2000;

    public static final int MAINBOARD = 0;
    public static final int CHATS = 1;
    public static final int DATABASE = 2;
    public static final int NEWS = 3;
    public static final int JOBS = 4;
    public static final int REVIEW = 5;
    public static final int CONTACTUS = 6;
    public static final int SETTINGS = 7;
    public static final int LOGOUT = 8;


     /* Jobs Detail Activity*/

    public static final String JobsModel = "jobsModel";
    public static final String NewsModel = "newsModel";

    /* Quickblox*/
    public static final String QUICKBLOX_Token = "token";
    public static final String QUICKBLOX_LAST_AUTH_DATE_MILLIS = "authDateMillis";
    public static final String QUICKBLOX_USER_ID = "userId";


    /* Quickblox*/
    public static final String DASHBOARD_FRAGMENT_TYPE = "fragmentType";

    public static final String SUPER_USERNAME = "SuperUser";
    public static final String SUPER_PASSWORD = "SuperUser";
    public static final int SUPER_USER_ID = 15306015;

    /* User Adapter*/
    public static final String KEY_USER_DETAILS = "userDetails";

    /* Company Adapter*/
    public static final String KEY_CompanyDetails = "companyDetails";

    public static final String NO_PERSONS_FOUND = "No Persons found";
    public static final String NO_COMPANY_FOUND = "No Company found";

    public static final String NO_EMPLOYEES_FOUND = "No Employees found";
    public static final String NO_JOB_FOUND = "NO_JOB_FOUND";
    public static final String NO_NEWS_FOUND = "NO_NEWS_FOUND";

    public static final String KEY_NO_PERSONS_FOUND = "No Persons found";
    public static final String KEY_NO_COMPANY_FOUND = "No Company found";

    public static final String ACTION_TYPE_PESRON_ADD = "PERSON_ADD";
    public static final String ACTION_TYPE_PESRON_DELETE = "PERSON_DELETE";
    public static final String ACTION_TYPE_PESRON_EDIT = "PERSON_EDIT";


    public static final String NO_IMAGE = "noImage";


    //Zone Group
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String NORTH = "North";
    public static final String SOUTH = "South";

    public static final String EMAIL = "email";

    public static final String REGION = "region";
    public static final String ZONE = "zone";

}
