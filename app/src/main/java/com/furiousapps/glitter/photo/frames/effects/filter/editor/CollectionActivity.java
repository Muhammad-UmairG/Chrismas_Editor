package com.furiousapps.glitter.photo.frames.effects.filter.editor;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.R;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter.CollectionAdapter;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.Util;

import java.io.File;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    private @Nullable
    AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        TextView title = findViewById(R.id.toolbar_title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT_MAIN));
        getFromSdcard();
        RecyclerView rv = findViewById(R.id.rvcoll);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new CollectionAdapter(f, this));

        bannerAdView = new AdView(this, getResources().getString(R.string.bannerads),
                AdSize.BANNER_HEIGHT_50);
        RelativeLayout bannerAdContainer = (RelativeLayout) findViewById(R.id.bannerAdContainer);
        // Reposition the ad and add it to the view hierarchy.
        bannerAdContainer.addView(bannerAdView);

        if (Util.IS_DISPLAY_ADS) {

            bannerAdView.loadAd();

        }

    }


    public void getFromSdcard() {
        File file = new File(android.os.Environment.getExternalStorageDirectory(), "Pictures" + File.separator + Util.ALBUM);

        if (file.isDirectory()) {
            listFile = file.listFiles();


            for (int i = 0; i < listFile.length; i++) {

                f.add(listFile[i].getAbsolutePath());

            }
        }
    }

    @Override
    protected void onDestroy() {
        if (bannerAdView != null) {
            bannerAdView.destroy();
        }
        super.onDestroy();
    }
}
