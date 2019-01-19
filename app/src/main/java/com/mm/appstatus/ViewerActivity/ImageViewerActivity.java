package com.mm.appstatus.ViewerActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mm.appstatus.Drawer;
import com.mm.appstatus.Observer;
import com.mm.appstatus.R;
import com.mm.appstatus.Subject;
import com.mm.appstatus.Utills.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class ImageViewerActivity extends AppCompatActivity implements Subject {

    private final String TAG = "ImageViewer";
    public LinearLayout btn_download, btn_share, btn_re_post;
    public ImageButton img_btn_download, img_btn_share, img_re_post;
    public int count = Config.count;
    public List<Observer> observers;
    String image_path = "", path = "", atype = "", package_name = "";
    String type = "";
    ImageView imageView;
    String listenerValue = "";
    Drawer drawer = new Drawer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imageView = findViewById(R.id.imageView);

        observers = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            image_path = intent.getStringExtra("image");
            type = intent.getStringExtra("type");
            atype = intent.getStringExtra("atype");

            if (image_path != null) {
                Glide.with(this).load(image_path)
                        .into(imageView);
            }
        }

        if (type.equals("0")) {
            path = Config.WhatsAppSaveStatus;
            package_name = "com.whatsapp";

        } else if (type.equals("1")) {
            path = Config.GBWhatsAppSaveStatus;
            package_name = "com.gbwhatsapp";

        } else if (type.equals("2")) {
            path = Config.WhatsAppBusinessSaveStatus;
            package_name = "com.whatsapp.w4b";

        }


        btn_download = findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /* type == 0 (WhatsApp Normal)
                    type == 1 (WhatsApp GB)
                    type == 2 (WhatsApp Business)*/
                copyFileOrDirectory(image_path, path);

                sharePerAds();
            }
        });
        img_btn_download = findViewById(R.id.img_btn_download);
        img_btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /* type == 0 (WhatsApp Normal)
                    type == 1 (WhatsApp GB)
                    type == 2 (WhatsApp Business)*/
                copyFileOrDirectory(image_path, path);

                sharePerAds();
            }
        });


        btn_share = findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_path.endsWith(".jpg")) {
                    shareVia("image/jpg", image_path);
                } else if (image_path.endsWith(".mp4")) {
                    shareVia("video/mp4", image_path);
                }

                sharePerAds();

            }
        });
        img_btn_share = findViewById(R.id.img_btn_share);
        img_btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_path.endsWith(".jpg")) {
                    shareVia("image/jpg", image_path);
                } else if (image_path.endsWith(".mp4")) {
                    shareVia("video/mp4", image_path);
                }

                sharePerAds();

            }
        });


        btn_re_post = findViewById(R.id.btn_re_post);
        btn_re_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_path.endsWith(".jpg")) {

                    shareViaWhatsApp("image/jpg", image_path, package_name);
                } else if (image_path.endsWith(".mp4")) {

                    shareViaWhatsApp("video/mp4", image_path, package_name);
                }
                sharePerAds();


            }
        });
        img_re_post = findViewById(R.id.img_re_post);
        img_re_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_path.endsWith(".jpg")) {

                    shareViaWhatsApp("image/jpg", image_path, package_name);
                } else if (image_path.endsWith(".mp4")) {

                    shareViaWhatsApp("video/mp4", image_path, package_name);
                }

                sharePerAds();
            }
        });

        if (atype.equals("0")) {
            btn_download.setVisibility(View.GONE);
        } else if (atype.equals("1")) {
            btn_download.setVisibility(View.VISIBLE);
        }

        sharePerAds();


    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public void shareVia(String type, String path) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType(type);
        File fileToShare = new File(path);
        Uri uri = Uri.fromFile(fileToShare);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
/*
        sharingIntent.setPackage("com.whatsapp");//package name of the app
*/
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    public void shareViaWhatsApp(String type, String path, String package_name) {

/*
        com.whatsapp.w4b
        com.gbwhatsapp
        com.whatsapp
*/
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(package_name, PackageManager.GET_META_DATA);
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType(type);
            File fileToShare = new File(path);
            Uri uri = Uri.fromFile(fileToShare);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.setPackage(package_name);//package name of the app
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void allSharedPreference(int i) {
        SharedPreferences preferences = getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ALL", String.valueOf(i));
        editor.commit();
    }

    public void sharePerAds() {
        int i;
        if (Config.getALLState(ImageViewerActivity.this).length() > 0) {
            i = Integer.parseInt(Config.getALLState(ImageViewerActivity.this));
            if (i > count) {

                allSharedPreference(0);
            } else {
                i++;
                allSharedPreference(i);
            }
        } else {
            i = 1;
            allSharedPreference(i);
        }
        listenerValue = String.valueOf(i);
        register(drawer);
        notifyObservers();
        unregister(drawer);
    }


    @Override
    public void register(final Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unregister(final Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (final Observer observer : observers) {
            Log.v("KKKKKKKKK", "" + listenerValue);
            observer.update(listenerValue, ImageViewerActivity.this);
        }
    }
}
