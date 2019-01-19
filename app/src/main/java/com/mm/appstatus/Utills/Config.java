package com.mm.appstatus.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {
    public static final String WhatsAppDirectoryPath = "/storage/emulated/0/WhatsApp/Media/.Statuses/";
    public static final String WhatsAppSaveStatus = "/storage/emulated/0/WhatsAppStatusesDir/Media/WhatsApp/";

    public static final String GBWhatsAppDirectoryPath = "/storage/emulated/0/GBWhatsApp/Media/.Statuses/";
    public static final String GBWhatsAppSaveStatus = "/storage/emulated/0/WhatsAppStatusesDir/Media/GBWhatsApp/";

    public static final String WhatsAppBusinessDirectoryPath = "/storage/emulated/0/WhatsApp Business/Media/.Statuses/";
    public static final String WhatsAppBusinessSaveStatus = "/storage/emulated/0/WhatsAppStatusesDir/Media/WhatsAppBusiness/";


    public static final int count = 6;


    public static String getPictureShareState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("SharePicture", "").length() > 0) {
            return prefs.getString("SharePicture", "");
        } else
            return "";
    }

    public static String getPictureDownloadState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("DownloadPicture", "").length() > 0) {
            return prefs.getString("DownloadPicture", "");
        } else
            return "";
    }

    public static String getVideoDownloadState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("DownloadVideo", "").length() > 0) {
            return prefs.getString("DownloadVideo", "");
        } else
            return "";
    }

    public static String getVideoShareState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("ShareVideo", "").length() > 0) {
            return prefs.getString("ShareVideo", "");
        } else
            return "";
    }

    public static String getSaveDeleteState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("DeleteSave", "").length() > 0) {
            return prefs.getString("DeleteSave", "");
        } else
            return "";
    }

    public static String getSaveShareState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("ShareSave", "").length() > 0) {
            return prefs.getString("ShareSave", "");
        } else
            return "";
    }

    public static String getALLState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("ALL", "").length() > 0) {
            return prefs.getString("ALL", "");
        } else
            return "";
    }
}
