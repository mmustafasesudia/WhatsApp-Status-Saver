package com.mm.appstatus;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.facebook.ads.AudienceNetworkAds;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;


public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 1000;
    private static int SPLASH_TIME = 4000;
    Context context;
    boolean isCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        AudienceNetworkAds.initialize(this);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!isStoragePermissionGranted()) {
            Toast.makeText(this, "Please allow Permission to continue..", Toast.LENGTH_SHORT).show();
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else if (isStoragePermissionGranted()) {

            startActivity();
        }

    }

    public void startActivity() {
        handlerSplash();
    }

    public void handlerSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, Drawer.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PPP", "Permission is granted");
                //startActivity();
                return true;
            } else {

                Log.v("PPP", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PPP", "Permission is granted");
            //startActivity();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        //openFragment();
                        startActivity();
                    }

                } else {
                    /*ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            1);*/
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        ForceUpdateAsync forceUpdateAsync = new ForceUpdateAsync(SplashScreen.this);
        //new ForceUpdateAsync(currentVersion, Drawer.this).execute();
        try {
            String latestVersion = forceUpdateAsync.execute().get();
            if (latestVersion != null) {
                Log.v("Splash", "|| " + latestVersion + " || " + currentVersion);
                if (currentVersion.equalsIgnoreCase(latestVersion)) {
                    //Toast.makeText(Drawer.this, "Update Available", Toast.LENGTH_SHORT).show();
                    startDialog();
                    //isUpdate = true;
                } else {
                    //Toast.makeText(Drawer.this, "Update Not Available", Toast.LENGTH_SHORT).show();
                    //isUpdate = false;
                    handlerSplash();
                }
            } else {
                //isUpdate = false;
                handlerSplash();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void startDialog() {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                SplashScreen.this);
        myAlertDialog.setTitle("Update Available");
        myAlertDialog.setMessage("A new version of WhatsApp Statuses Saver is available. Please update to version");

        myAlertDialog.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        handlerSplash();
                        rateUsOnPlayStore();
                    }
                });

        myAlertDialog.setNegativeButton("Not Now",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        handlerSplash();

                    }
                });
        myAlertDialog.show();
    }


    public void rateUsOnPlayStore() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }
}
