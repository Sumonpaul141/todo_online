package com.belivit.todoonline.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class GlobalData {

    private static String version = "";
    private static String sourceWithVersion;
    //    private static String base_url = "http://10.0.2.2:5000";
    //    private static String base_url = "http://192.168.8.16:5000";
    private static String base_url = "https://todo-track-on.herokuapp.com";


    public static String getSourceWithVersion(Context context) {
        return sourceWithVersion = "APP_v"+ getVersion(context);
    }

    private static String getVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static String getLoginUrl() {
        String url = base_url + "/user_login";
        Log.d("paul", "getLoginUrl: " + url);
        return url;
    }
    public static String getRegistrationUrl() {
        String url = base_url + "/user_reg";
        Log.d("paul", "getRegistrationUrl: " + url);
        return url;
    }
    public static String getAddTasknUrl() {
        String url = base_url + "/add_task";
        Log.d("paul", "getAddTasknUrl: " + url);
        return url;
    }
    public static String getAllTasknUrl() {
        String url = base_url + "/all_task";
        Log.d("paul", "getAllTasknUrl: " + url);
        return url;
    }
    public static String getAddTodoUrl() {
        String url = base_url + "/add_todo";
        Log.d("paul", "getAddTodoUrl: " + url);
        return url;
    }

    public static String getAllTodoUrl() {
        String url = base_url + "/all_todo";
        Log.d("paul", "getAllTodoUrl: " + url);
        return url;
    }
    public static String getCheckTodoUrl() {
        String url = base_url + "/check_todo";
        Log.d("paul", "getCheckTodoUrl: " + url);
        return url;
    }
    public static String getDeleteTodoUrl() {
        String url = base_url + "/delete_todo";
        Log.d("paul", "getDeleteTodoUrl: " + url);
        return url;
    }
    public static String getDeleteTaskUrl() {
        String url = base_url + "/delete_task";
        Log.d("paul", "getDeleteTodoUrl: " + url);
        return url;
    }
    public static String getUpdateTaskUrl() {
        String url = base_url + "/update_task";
        Log.d("paul", "getUpdateTaskUrl: " + url);
        return url;
    }
    public static String getUpdateTodoUrl() {
        String url = base_url + "/update_todo";
        Log.d("paul", "getUpdateTodoUrl: " + url);
        return url;
    }

}
