package com.furiousapps.glitter.photo.frames.effects.filter.editor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap.BitmapLoader;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.Util;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class SaveActivity extends AppCompatActivity implements View.OnClickListener{

    private int mToolbarColor;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;
    private String pathSaved;
    Bitmap image;
    ResolveInfo info;
    private @Nullable
    AdView bannerAdView;
    private List<ResolveInfo> listResolve;
    private List<ResolveInfo> lri;
    private List<String> ls;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        setupViews(intent);
        bannerAdView = new AdView(this, getResources().getString(R.string.bannerads),
                AdSize.BANNER_HEIGHT_50);
        RelativeLayout bannerAdContainer = findViewById(R.id.bannerAdContainer);
        // Reposition the ad and add it to the view hierarchy.
        bannerAdContainer.addView(bannerAdView);

        if (Util.IS_DISPLAY_ADS) {

            bannerAdView.loadAd();

        }
        Bundle extras = intent.getExtras();
        String newString = null;
        if (extras == null) {
            pathSaved = new File(intent.getData().getPath()).getAbsolutePath();

        } else {
            newString = extras.getString("string");
        }
        if (newString != null) {
            pathSaved = newString;
        }

        findViewById(R.id.btn_fb).setOnClickListener(this);
        findViewById(R.id.btn_insta).setOnClickListener(this);
        findViewById(R.id.btn_whatsapp).setOnClickListener(this);
        findViewById(R.id.btn_twitter).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);
        ImageView ivsharephoto = findViewById(R.id.ivsharephoto);
        image = BitmapLoader.load(this, new int[]{720, 720}, pathSaved);
        ivsharephoto.setImageBitmap(image);

       /* ImageView ivsharemore = findViewById(R.id.ivsharemore);
        ivsharemore.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    Uri uri = FileProvider.getUriForFile(SaveActivity.this, BuildConfig.APPLICATION_ID + ".provider", new File(pathSaved));
                    intent.putExtra("android.intent.extra.STREAM", uri);

                    Intent chooserIntent = Intent.createChooser(intent, "Share");
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(chooserIntent);
                } catch (Exception e) {
                }
            }
        });*/
    }

    @SuppressLint({"PrivateResource"})
    private void setupViews(@NonNull Intent intent) {
        mToolbarColor = intent.getIntExtra("TOOL_BAR_COLOR", ContextCompat.getColor(this, R.color.ucrop_color_toolbar));
        mToolbarWidgetColor = intent.getIntExtra("WIDGET_COLOR_TOOLBAR", ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget));
        mToolbarTitle = intent.getStringExtra("TITLE_TEXT_TOOLBAR");
        mToolbarTitle = !TextUtils.isEmpty(mToolbarTitle) ? mToolbarTitle : getResources().getString(R.string.savephoto);
        setupAppBar();
    }

    private void setupAppBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(mToolbarColor);
        toolbar.setTitleTextColor(mToolbarWidgetColor);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(mToolbarWidgetColor);
        toolbarTitle.setText(mToolbarTitle);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT_MAIN));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    public void onSetWallpaperClick(View view) {


        final Dialog dialog = new Dialog(this);
// Include dialog.xml file
        dialog.setContentView(R.layout.dailog);
        dialog.show();

        Button lock_screen = (Button) dialog.findViewById(R.id.lock_screen);

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(SaveActivity.this);
// if decline button is clicked, close the custom dialog
        lock_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
              //  dialog.dismiss();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        wallpaperManager.setBitmap(image, null, true, WallpaperManager.FLAG_LOCK);
                        Toast.makeText(SaveActivity.this, "Wallpaper set Successfully...", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SaveActivity.this, "Device not support lock screen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button home_screen = dialog.findViewById(R.id.home_screen);
// if decline button is clicked, close the custom dialog
        home_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wallpaperManager.setBitmap(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(SaveActivity.this, "Wallpaper set Successfully...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        /*try {
            *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(image, null, true, WallpaperManager.FLAG_LOCK);
            }else{
                wallpaperManager.setBitmap(image);
            }*//*
            wallpaperManager.setBitmap(image);

            Toast.makeText(SaveActivity.this, "Wallpaper set Successfully...", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(SaveActivity.this, "Error setting wallpaper", Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    protected void onDestroy() {
        if (bannerAdView != null) {
            bannerAdView.destroy();
        }
        super.onDestroy();
    }

    private void shareImage(String packageName) {
        Intent shareIntent = new Intent("android.intent.action.SEND");
        File file = new File(pathSaved);
        shareIntent.setType("image/png");
        Uri photoURI = FileProvider.getUriForFile(this,  BuildConfig.APPLICATION_ID + ".provider" , file);
        shareIntent.putExtra("android.intent.extra.STREAM", photoURI);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        if (!"".equals(packageName)) {
            shareIntent.setPackage(packageName);
        }
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.txt_shareImag).toString()));
    }

    private boolean verifyPackage(String packageName) {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share /*2131361900*/:
                shareImage("");
                return;

            case R.id.btn_fb /*2131362014*/:
                String fbPackName = getResources().getString(R.string.fb_package_name);
                if (verifyPackage(fbPackName)) {
                    shareImage(fbPackName);
                    return;
                }
                Toast.makeText(this, getResources().getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
                return;
            case R.id.btn_insta /*2131362015*/:
                String instaPackName = getResources().getString(R.string.insta_package_name);
                if (verifyPackage(instaPackName)) {
                    shareImage(instaPackName);
                    return;
                }
                Toast.makeText(this, getResources().getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
                return;
            case R.id.btn_twitter /*2131362016*/:
                String twitPackName = getResources().getString(R.string.twitter_package_name);
                if (verifyPackage(twitPackName)) {
                    shareImage(twitPackName);
                    return;
                }
                Toast.makeText(this, getResources().getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
                return;
            case R.id.btn_whatsapp /*2131362017*/:
                String whatsappPackName = getResources().getString(R.string.whatsapp_package_name);
                if (verifyPackage(whatsappPackName)) {
                    shareImage(whatsappPackName);
                    return;
                }
                Toast.makeText(this, getResources().getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
                return;
            default:
                return;
        }
    }
}

