package com.mm.appstatus;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.mm.appstatus.TabLayoutWhatsApp.GBWhatsAppPicture;
import com.mm.appstatus.TabLayoutWhatsApp.GBWhatsAppSaveStatuses;
import com.mm.appstatus.TabLayoutWhatsApp.GBWhatsAppVideos;

import java.util.ArrayList;
import java.util.List;

public class GBWhatsApp extends AppCompatActivity {

    private final String TAG = Drawer.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gbwhats_app);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GBWhatsApp.super.onBackPressed();
            }
        });
        toolbar.setTitle("GBWhatsApp Statuses");
        //setSupportActionBar(toolbar);


        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        adView = new AdView(this, getApplicationContext().getResources().getString(R.string.facebook_adds_id), AdSize.BANNER_HEIGHT_50);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        // Find the Ad Container
        LinearLayout adContainer = findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();

    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    private void setupViewPager(ViewPager viewPager) {
        GBWhatsApp.ViewPagerAdapter adapter = new GBWhatsApp.ViewPagerAdapter(GBWhatsApp.this, getSupportFragmentManager());
        adapter.addFragment(new GBWhatsAppPicture(), "Picture");
        adapter.addFragment(new GBWhatsAppVideos(), "Videos");
        adapter.addFragment(new GBWhatsAppSaveStatuses(), "Saved");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private Context mContext;

        public ViewPagerAdapter(Context context, FragmentManager manager) {
            super(manager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
