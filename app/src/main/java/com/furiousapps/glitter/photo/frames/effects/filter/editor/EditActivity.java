package com.furiousapps.glitter.photo.frames.effects.filter.editor;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter.EffectAdapter;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter.FontAdapter;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.adapter.GalleryAdapter;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap.BitmapLoader;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap.BitmapProcessing;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.DisplayUtil;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.StickerView1;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.Util;

import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter;
import me.grantland.widget.AutofitTextView;

public class EditActivity extends BaseActivity implements ColorPickerDialogListener {
    private static final String TAG = "EditActivity";
    private AutofitTextView afltext;
    private Bitmap bmmain;
    private Bitmap bmmask;
    private int center;
    private boolean checkTouch;
    private boolean isStickerText =false;
    private String colorsample;
    private int contrast = 0;
    private int dis;
    private EditText edtext;
    private EffectAdapter effectAdapter;
    private int exposure = 6;
    private String filter = "thumb_meffect_00001";
    private boolean firstTouch = false;
    private String fontsample;
    private GalleryAdapter galleryAdapter;
    private GPUImageView gpuview;
    private int highlight = 0;
    private ImageView icframe, icframe2, iceffect, icabc, icadjust, icGift, ictext, icSanta, icHat, icTree, icLight;
    private boolean isNext;
    private ImageView ivalign;



    private RelativeLayout bannerAdContainer;

    private @Nullable
    AdView bannerAdView;
    private InterstitialAd interstitialAd;
    private ImageView ivchangecontrast;
    private ImageView ivchangeexposure;
    private ImageView ivchangehighlight;
    private ImageView ivchangeshadow;
    private ImageView ivchangesharpen;
    private ImageView ivchangetemperature;
    private ImageView ivchangevignette;
    private ImageView ivcircle;
    private ImageView ivframe;
    private ImageView ivphoto;
    private int kindEdit = 0;
    private String[] listItem;
    private String[] listfont;
    private LinearLayout llchange;
    private LinearLayout llcontrol;
    // private InterstitialAd interstitialAd;
    private boolean mShowLoader = true;
    private int mToolBarHeight;
    private int mToolbarColor;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;
    private MenuItem menuItemCrop;
    private Drawable menuItemCropIconDone;
    private Drawable menuItemCropIconSave;
    private int precontrast = 0;
    private int preexposure = 6;
    private int prehighlight = 0;
    private int preshadow = 0;
    private int presharpen = 0;
    private int pretemperature = 6;
    private int prevignette = 0;

    private RelativeLayout rlphoto;
    private RelativeLayout rlslider;
    private RelativeLayout rltext;
    private RecyclerView rvselect;
    private RecyclerView rvtext;
    private String savePath;
    private SeekBar sbslider;
    private int shadow = 0;
    private int sharpen = 0;
    private int temperature = 6;
    private String textsample;
    private long timetouch;
    private TextView toolbarTitle;
    private TextView tvslider;
    private int type = 0;
    private int vignette = 0;
    private int widthScreen;
    private int wthumb;
    Drawable stateButtonDrawable;

    @Override
    public void onColorSelected(int dialogId, int color) {
        if (isStickerText) {
            int i = 0;
            while (i < rlphoto.getChildCount()) {
                try {
                    if ((rlphoto.getChildAt(i) instanceof StickerView1) && rlphoto.getChildAt(i).isFocusable() && ((StickerView1) rlphoto.getChildAt(i)).isDrawedit()) {
                        ((StickerView1) rlphoto.getChildAt(i)).changeColor(color);
                        return;
                    }
                } catch (Exception e) {
                }
                i++;
            }
        }else {
            loadSampleText(String.format("#%06X", (0xFFFFFF & color)),"","");
        }

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }


    class saveAndGo extends AsyncTask<Void, Void, String> {
        saveAndGo() {
        }

        protected void onPreExecute() {
            mShowLoader = true;
        }

        protected String doInBackground(Void... dataArr) {
            return "";
        }

        protected void onPostExecute(String result) {
            savePath = savePhoto();
            mShowLoader = false;
            if (savePath.equals("")) {
                Toast.makeText(EditActivity.this, "Couldn't save photo, error", Toast.LENGTH_SHORT).show();
            } else    if (interstitialAd != null && interstitialAd.isAdLoaded()){
                interstitialAd.show();
            } else {
                requestNewInterstitial();
                Intent intent = new Intent().setClass(EditActivity.this, SaveActivity.class);
                intent.setData(Uri.parse(savePath));
                startActivity(intent);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bannerAdContainer= findViewById(R.id.bannerAdContainer);
        gpuview = findViewById(R.id.gpuview);
        rlphoto = findViewById(R.id.rlphoto);
        ivphoto = findViewById(R.id.ivphoto);
        rvselect = findViewById(R.id.rvselect);
        ivframe = findViewById(R.id.ivframe);
        rltext = findViewById(R.id.rltext);
        rvtext = findViewById(R.id.rvtext);
        afltext = findViewById(R.id.afltext);
        edtext = findViewById(R.id.edtext);
        ivalign = findViewById(R.id.ivalign);
        ivcircle = findViewById(R.id.ivcircle);
        icframe = findViewById(R.id.icframe);
        icSanta = findViewById(R.id.ic_santa);
        icHat = findViewById(R.id.ic_hat);
        icTree = findViewById(R.id.ic_tree);
        icLight = findViewById(R.id.ic_light);
        icframe2 = findViewById(R.id.icframe2);
        icadjust = findViewById(R.id.icadjust);
        ictext = findViewById(R.id.ictext);
        iceffect = findViewById(R.id.iceffect);
        icGift = findViewById(R.id.ic_gift);
        icabc = findViewById(R.id.icabc);
        ivchangeexposure = findViewById(R.id.ivchangeexposure);
        ivchangecontrast = findViewById(R.id.ivchangecontrast);
        ivchangesharpen = findViewById(R.id.ivchangesharpen);
        ivchangetemperature = findViewById(R.id.ivchangetemperature);
        ivchangehighlight = findViewById(R.id.ivchangehighlight);
        ivchangeshadow = findViewById(R.id.ivchangeshadow);
        ivchangevignette = findViewById(R.id.ivchangevignette);
        llcontrol = findViewById(R.id.llcontrol);
        llchange = findViewById(R.id.llchange);
        rlslider = findViewById(R.id.rlslider);
        sbslider = findViewById(R.id.sbslider);
        tvslider = findViewById(R.id.tvslider);


        Intent intent = getIntent();
        setupViews(intent);
        this.isNext = false;
        this.widthScreen = DisplayUtil.getDisplayWidthPixels(this);
        this.bmmain = BitmapLoader.load(this, new int[]{1440, 1440}, new File(intent.getData().getPath()).getAbsolutePath());
        if (this.bmmain == null) {
            try {
                Toast.makeText(this, "Couldn't handle this image, It has large size!", Toast.LENGTH_SHORT).show();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (this.bmmain.getWidth() > this.widthScreen) {
            this.bmmain = BitmapProcessing.resizeBitmap(this.bmmain, this.widthScreen, (this.widthScreen * this.bmmain.getHeight()) / this.bmmain.getWidth());
        }
        interstitialAd = new InterstitialAd(this, getResources().getString(R.string.interstitialads));


        this.menuItemCropIconDone = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_don);
        this.menuItemCropIconDone.mutate();
        this.menuItemCropIconDone.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
        this.menuItemCropIconSave = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_save);
        this.menuItemCropIconSave.mutate();
        this.menuItemCropIconSave.setColorFilter(this.mToolbarWidgetColor, Mode.SRC_ATOP);
        LayoutParams layoutParams = new LayoutParams(this.widthScreen, this.widthScreen);
        layoutParams.addRule(13);
        this.rlphoto.setLayoutParams(layoutParams);
        this.gpuview.setImage(this.bmmain);
        this.bmmask = BitmapLoader.loadFromResource(this, new int[]{300, 300}, R.drawable.mask);
        this.bmmask = BitmapProcessing.resizeBitmap(this.bmmask, (this.widthScreen * 2) / 5, (this.widthScreen * 2) / 5);

            this.ivframe.setImageBitmap(BitmapLoader.loadFromAsset(this, new int[]{1440, 1440}, "frames/frame_0001.png"));

        this.rvselect.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        bannerAdView = new AdView(this, getResources().getString(R.string.bannerads),
                AdSize.BANNER_HEIGHT_50);

        // Reposition the ad and add it to the view hierarchy.
        bannerAdContainer.addView(bannerAdView);

        if (Util.IS_DISPLAY_ADS) {
            //  adView.loadAd(new Builder().build());
            //    interstitialAd.loadAd();
            bannerAdView.loadAd();
        }
        this.icframe.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.frame));
                loadFrame();
            }
        });


        this.icframe2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.frame));
                loadFrame2();
            }
        });


        this.icadjust.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                kindEdit = 3;
                type = 1;
                setTextSlider(exposure);
                setChange();
                llchange.setVisibility(View.VISIBLE);
                rlslider.setVisibility(View.VISIBLE);
                menuItemCrop.setIcon(menuItemCropIconDone);
                clickable(false);
            }
        });

        this.iceffect.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 0;
                toolbarTitle.setText(getResources().getString(R.string.effects));
                loadEffect();
            }
        });

        this.icGift.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.gifts));
                loadGift();
            }
        });

        this.icSanta.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.santa));
                loadSanta();
            }
        });

        this.icHat.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.hat));
                loadHat();
            }
        });

        this.icTree.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.tree));
                loadTrees();
            }
        });

        this.icLight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.light));
                loadLights();
            }
        });

        this.icabc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toolbarTitle.setText(getResources().getString(R.string.captions));
                loadABC();
            }
        });

        this.ictext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                kindEdit = 1;
                toolbarTitle.setText(getResources().getString(R.string.addtext));
                rltext.setVisibility(View.VISIBLE);
                rvtext.setVisibility(View.GONE);
                edtext.setVisibility(View.VISIBLE);
                edtext.requestFocus();
                afltext.setText("");
                edtext.setText("");
                ivalign.setTag(1);
                ivalign.setImageResource(R.drawable.iccentertextalignment);
                ivcircle.setTag(0);
                ivcircle.setImageResource(R.drawable.iccircle);
                afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
                clickable(false);
                loadSampleText("#ffffff", Util.FONT_MAIN, "");
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtext, 1);
                menuItemCrop.setIcon(menuItemCropIconDone);
            }
        });

        findViewById(R.id.ivchangetext).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rvtext.setVisibility(View.GONE);
                edtext.setVisibility(View.VISIBLE);
                edtext.requestFocus();
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edtext, 1);
            }
        });
        this.edtext.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
        this.edtext.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                afltext.setText(charSequence.toString());
            }

            public void afterTextChanged(Editable editable) {
            }
        });
        findViewById(R.id.ivchangefont).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                rvtext.setVisibility(View.VISIBLE);
                closeKeyboard();
                edtext.setVisibility(View.GONE);

                rvtext.setLayoutManager(new LinearLayoutManager(EditActivity.this, RecyclerView.HORIZONTAL, false));
                loadFont();
            }
        });
        findViewById(R.id.ivchangecolor).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                closeKeyboard();

                isStickerText =false;
                ColorPickerDialog.newBuilder().show(EditActivity.this);

            }
        });


        this.ivalign.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setAlignText();
            }
        });
        this.ivcircle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setCircleText();
            }
        });



        loadPointforSlider();
        this.ivchangeexposure.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 1;
                setTextSlider(exposure);
                setChange();
            }
        });
        this.ivchangecontrast.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 2;
                setTextSlider(contrast);
                setChange();
            }
        });
        this.ivchangesharpen.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 3;
                setTextSlider(sharpen);
                setChange();
            }
        });
        this.ivchangehighlight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 4;
                setTextSlider(highlight);
                setChange();
            }
        });
        this.ivchangeshadow.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 5;
                setTextSlider(shadow);
                setChange();
            }
        });
        this.ivchangetemperature.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 6;
                setTextSlider(temperature);
                setChange();
            }
        });
        this.ivchangevignette.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                type = 7;
                setTextSlider(vignette);
                setChange();
            }
        });


        init();
        setListner();
        loadFrame();
        loadPopup();
    }

    private void init() {
    }

    private void setListner() {
    }


    private void setAlignText() {
        if (ivalign.getTag().equals(1)) {
            afltext.setGravity(3);
            ivalign.setImageResource(R.drawable.icalignleft);
            ivalign.setTag(2);
        } else if (ivalign.getTag().equals(2)) {
            afltext.setGravity(5);
            ivalign.setImageResource(R.drawable.icalignright);
            ivalign.setTag(3);
        } else if (ivalign.getTag().equals(3)) {
            afltext.setGravity(17);
            ivalign.setImageResource(R.drawable.iccentertextalignment);
            ivalign.setTag(1);
        }
    }

    private void setCircleText() {
        if (ivcircle.getTag().equals(0)) {
            ivcircle.setImageResource(R.drawable.iccirclepressed);
            ivcircle.setTag(1);
            afltext.setShadowLayer(1.6f, StickerView1.MAX_SCALE_SIZE, StickerView1.MAX_SCALE_SIZE, -1);
        } else if (ivcircle.getTag().equals(1)) {
            ivcircle.setTag(0);
            ivcircle.setImageResource(R.drawable.iccircle);
            afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
        }
    }
    private void loadPopup() {
        this.interstitialAd =new InterstitialAd(this, getResources().getString(R.string.interstitialads));

        this.interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "onAdLoaded: popup");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "onAdClicked: ");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "onLoggingImpression: ");
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.d(TAG, "onInterstitialDisplayed: ");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.d(TAG, "onInterstitialDismissed: ");
                if (isNext) {
                    requestNewInterstitial();
                    Intent intent = new Intent().setClass(EditActivity.this, SaveActivity.class);
                    intent.setData(Uri.parse(savePath));
                    startActivity(intent);
                    return;
                }
                finish();
            }
        });
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        if (!Util.IS_DISPLAY_ADS|| interstitialAd.isAdLoaded() ) {
            return;
        }
        if (!interstitialAd.isAdLoaded()){
            try {
                interstitialAd.loadAd();
            } catch (Throwable e) {
                // Do nothing, just skip and wait for ad loading
            }
        }

    }

    private void setClick() {
        galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerViewItemClickListener() {
            public void onItemClick(View view, String resId) {
                if (rltext.getVisibility() != View.GONE  || llchange.getVisibility() != View.GONE) {
                    return;
                }
                if (resId.contains("frame_")) {
                    addFrame(resId);
                }else if (resId.contains("frame2_")) {
                    addFrame(resId);
                }else if (resId.contains("tree_")) {
                    addStickerItem(resId);
                }else if (resId.contains("santa_")) {
                    addStickerItem(resId);
                }else if (resId.contains("light_")) {
                    addStickerItem(resId);
                }else if (resId.contains("cap_")) {
                    addStickerItem(resId);
                } else if (resId.contains("gift_")) {
                    addStickerItem(resId);
                } else if (resId.contains("textture_")) {
                    addABC(resId);
                } else if (resId.contains("snap_")) {
                    addSnap(resId);
                }
            }
        });
    }

    private void effectClick() {
        effectAdapter.setOnItemClickListener(new EffectAdapter.OnRecyclerViewItemClickListener() {
            public void onItemClick(View view, String resId) {
                if (rltext.getVisibility() == View.GONE  && llchange.getVisibility() == View.GONE && resId.contains("thumb_meffect_")) {
                    filter = resId;
                    gpuEffect();
                }
            }
        });
    }

    @SuppressLint({"PrivateResource"})
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);
        MenuItem menuItemLoader = menu.findItem(R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(mToolbarWidgetColor, Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Log.i("Glitter Photo Effect", e.getMessage());
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }
        menuItemCrop = menu.findItem(R.id.menu_crop);
        menuItemCrop.setIcon(menuItemCropIconSave);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (kindEdit == 0) {
            if (item.getItemId() == R.id.menu_crop) {
                isNext = true;
                clickSave();
            } else if (item.getItemId() == R.id.menu_loader) {
                isNext = false;
                if (interstitialAd != null && interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                } else {
                    finish();
                }
            }
        } else if (kindEdit == 1) {
            if (item.getItemId() == R.id.menu_crop) {
                textsample = afltext.getText().toString();
                if (!textsample.equals("")) {
                    afltext.setDrawingCacheEnabled(true);
                    afltext.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    Bitmap bitmap = Bitmap.createBitmap(afltext.getDrawingCache());
                    afltext.setDrawingCacheEnabled(false);
                    boolean checkEdit = false;
                    int i = 0;
                    while (i < rlphoto.getChildCount()) {
                        try {
                            if ((rlphoto.getChildAt(i) instanceof StickerView1) && ((StickerView1) rlphoto.getChildAt(i)).isEdit()) {
                                checkEdit = true;
                                ((StickerView1) rlphoto.getChildAt(i)).setEdit(false);
                                ((StickerView1) rlphoto.getChildAt(i)).setText(textsample);
                                ((StickerView1) rlphoto.getChildAt(i)).setColor(colorsample);
                                ((StickerView1) rlphoto.getChildAt(i)).setFont(fontsample);
                                ((StickerView1) rlphoto.getChildAt(i)).setAlign((Integer) ivalign.getTag());
                                ((StickerView1) rlphoto.getChildAt(i)).setCircle((Integer) ivcircle.getTag());
                                ((StickerView1) rlphoto.getChildAt(i)).setWaterMark(bitmap, false);
                                break;
                            }
                        } catch (Exception e) {
                            Log.i("Glitter Photo Effect", e.getMessage());
                        }
                        i++;
                    }
                    if (!checkEdit) {
                        addText(bitmap);
                    }
                }
            } else if (item.getItemId() == R.id.menu_loader) {
                resetEditSticker();
            }
            rltext.setVisibility(View.GONE);
            kindEdit = 0;
            toolbarTitle.setText(getResources().getString(R.string.editphoto));
            clickable(true);
            closeKeyboard();
        } else if (kindEdit == 3) {
            if (item.getItemId() == R.id.menu_crop) {
                preexposure = exposure;
                precontrast = contrast;
                prehighlight = highlight;
                preshadow = shadow;
                presharpen = shadow;
                pretemperature = temperature;
                prevignette = vignette;
            } else {
                exposure = preexposure;
                contrast = precontrast;
                highlight = prehighlight;
                shadow = preshadow;
                sharpen = preshadow;
                temperature = pretemperature;
                vignette = prevignette;
                gpuEffect();
            }
            llcontrol.setVisibility(View.VISIBLE);
            rvselect.setVisibility(View.VISIBLE);
            llchange.setVisibility(View.GONE);
            rlslider.setVisibility(View.GONE);
            kindEdit = 0;
            type = 0;
            toolbarTitle.setText(getResources().getString(R.string.editphoto));
            clickable(true);
        }
        menuItemCrop.setIcon(menuItemCropIconSave);
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (this.kindEdit == 0) {
                this.isNext = false;

                if (interstitialAd != null && this.interstitialAd.isAdLoaded()) {
                    this.interstitialAd.show();
                    return true;
                }
                finish();
            } else if (this.kindEdit == 1) {
                resetEditSticker();
                this.kindEdit = 0;
                this.rltext.setVisibility(View.GONE);
                this.toolbarTitle.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                this.menuItemCrop.setIcon(this.menuItemCropIconSave);
                return false;
            } else if (this.kindEdit == 3) {
                this.exposure = this.preexposure;
                this.contrast = this.precontrast;
                this.highlight = this.prehighlight;
                this.shadow = this.preshadow;
                this.sharpen = this.preshadow;
                this.temperature = this.pretemperature;
                this.vignette = this.prevignette;
                gpuEffect();
                this.kindEdit = 0;
                this.type = 0;
                this.llcontrol.setVisibility(View.VISIBLE);
                this.rvselect.setVisibility(View.VISIBLE);
                this.llchange.setVisibility(View.GONE);
                this.rlslider.setVisibility(View.GONE);
                this.toolbarTitle.setText(getResources().getString(R.string.editphoto));
                clickable(true);
                this.menuItemCrop.setIcon(this.menuItemCropIconSave);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint({"PrivateResource"})
    private void setupViews(@NonNull Intent intent) {
        mToolBarHeight = (int) TypedValue.applyDimension(1, 50.0f, getResources().getDisplayMetrics());
        mToolbarColor = intent.getIntExtra("Toolbar_Color", ContextCompat.getColor(this, R.color.ucrop_color_toolbar));
        mToolbarWidgetColor = intent.getIntExtra("WIDGET_COLOR_TOOLBAR", ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget));
        mToolbarTitle = intent.getStringExtra("TITLE_TEXT_TOOLBAR");
        mToolbarTitle = !TextUtils.isEmpty(mToolbarTitle) ? mToolbarTitle : getResources().getString(R.string.editphoto);
        setupAppBar();
    }

    private void setupAppBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbar.setBackgroundColor(mToolbarColor);
        toolbar.setTitleTextColor(mToolbarWidgetColor);
        toolbarTitle.setTextColor(mToolbarWidgetColor);
        toolbarTitle.setText(mToolbarTitle);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT_MAIN));
        stateButtonDrawable = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_cross).mutate();
        stateButtonDrawable.setColorFilter(mToolbarWidgetColor, Mode.SRC_ATOP);

        toolbar.setNavigationIcon(stateButtonDrawable);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mShowLoader = false;
    }

    private void clickSave() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestPermission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_write_storage_rationale), BaseActivity.REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {

            new saveAndGo().execute();
        }
    }


    private String savePhoto() {
        ivphoto.setImageBitmap(gpuview.getGPUImage().getBitmapWithFilterApplied());
        rlphoto.setDrawingCacheEnabled(true);
        Bitmap bitmap=viewToBitmap(rlphoto);
        rlphoto.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        String timestamp=new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());


        StringBuilder builder=new StringBuilder();
        builder.append(timestamp).append(".jpg");
        String imageName=builder.toString();
        File folder=new File(pathtoSave());

        if(!folder.exists()){
            folder.mkdirs();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        StringBuilder stringBuilder=new StringBuilder();
        String filepath=  stringBuilder.append(folder.getAbsolutePath()).append(File.separator).append(imageName).toString();
        try {
            File mfile = new File(filepath);
            mfile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(mfile);
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            Log.d("EditActivity", "savePhoto: "+e.getMessage());

            return "";
        }
        addImageGallery(filepath);
        rlphoto.setDrawingCacheEnabled(false);

        return filepath;

    }

    private void addImageGallery(String url) {
        ContentValues values = new ContentValues();
        values.put("_data", url);
        values.put("mime_type", "image/jpeg");
        getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
    }
    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private String pathtoSave() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return getFilesDir() + File.separator + Util.ALBUM;
        }
        try {
            return Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + Util.ALBUM;
        } catch (Exception e) {
            return Environment.getExternalStorageDirectory() + File.separator + Util.ALBUM;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BaseActivity.REQUEST_STORAGE_WRITE_ACCESS_PERMISSION /*102*/:
                if (grantResults[0] == 0) {
                    clickSave();
                    return;
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }

    private void addStickerItem(String resId) {
        StickerView1 stickerView = new StickerView1(this, false);
        LayoutParams params = new LayoutParams(-1, -1);
        params.addRule(8, R.id.image);
        params.addRule(6, R.id.image);
        rlphoto.addView(stickerView, params);
        Bitmap bitmap = BitmapLoader.loadFromAsset(this, new int[]{512, 512}, resId.replace("thumb_", ""));
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            int width = widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(bitmap, width, (bitmap.getHeight() * width) / bitmap.getWidth());
        } else {
            int height = widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(bitmap, (bitmap.getWidth() * height) / bitmap.getHeight(), height);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(resId);
    }

    private void addFrame(String resId) {
        ivframe.setImageBitmap(BitmapLoader.loadFromAsset(this, new int[]{1440, 1440}, resId.replace("thumb_", "")));
    }


    private void addABC(String resId) {
        StickerView1 stickerView = new StickerView1(this, true);
        LayoutParams params = new LayoutParams(-1, -1);
        params.addRule(8, R.id.image);
        params.addRule(6, R.id.image);
        rlphoto.addView(stickerView, params);
        Bitmap bitmap = BitmapLoader.loadFromAsset(this, new int[]{720, 720}, resId.replace("thumb_", ""));
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            int width = widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(bitmap, width, (bitmap.getHeight() * width) / bitmap.getWidth());
        } else {
            int height = widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(bitmap, (bitmap.getWidth() * height) / bitmap.getHeight(), height);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(resId);
    }

    private void addSnap(String resId) {
        StickerView1 stickerView = new StickerView1(this, false);
        LayoutParams params = new LayoutParams(-1, -1);
        params.addRule(8, R.id.image);
        params.addRule(6, R.id.image);
        this.rlphoto.addView(stickerView, params);
        Bitmap bitmap = BitmapLoader.loadFromAsset(this, new int[]{512, 512}, resId);
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            int width = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(bitmap, width, (bitmap.getHeight() * width) / bitmap.getWidth());
        } else {
            int height = this.widthScreen / 2;
            bitmap = BitmapProcessing.resizeBitmap(bitmap, (bitmap.getWidth() * height) / bitmap.getHeight(), height);
        }
        stickerView.setWaterMark(bitmap, true);
        stickerView.setTag(resId);
    }

    private void addText(Bitmap bm) {
        StickerView1 stickerView = new StickerView1(this, true);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(8, R.id.image);
        params.addRule(6, R.id.image);
        this.rlphoto.addView(stickerView, params);
        stickerView.setWaterMark(bm, true);
        stickerView.setTag("text");
        stickerView.setColor(this.colorsample);
        stickerView.setFont(this.fontsample);
        stickerView.setText(this.textsample);
        stickerView.setAlign((Integer) this.ivalign.getTag());
        stickerView.setCircle((Integer) this.ivcircle.getTag());
    }

    private void resetStickersFocus() {
        for (int i = 0; i < this.rlphoto.getChildCount(); i++) {
            try {
                if (this.rlphoto.getChildAt(i) instanceof StickerView1) {
                    this.rlphoto.getChildAt(i).setFocusable(false);
                }
            } catch (Exception e) {
                Log.i("Glitter Photo Effect", e.getMessage());
            }
        }
    }

    private void resetEditSticker() {
        for (int i = 0; i < this.rlphoto.getChildCount(); i++) {
            try {
                if (this.rlphoto.getChildAt(i) instanceof StickerView1) {
                    ((StickerView1) this.rlphoto.getChildAt(i)).setEdit(false);
                }
            } catch (Exception e) {
                Log.i("Glitter Photo Effect", e.getMessage());
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean z = false;
        if (this.rltext.getVisibility() == View.GONE  && this.llchange.getVisibility() == View.GONE) {
            if (ev.getAction() == 0) {
                this.checkTouch = z;
                int x = ((int) ev.getX()) - Util.getRelativeLeft(this.rlphoto);
                int y = ((int) ev.getY()) - Util.getRelativeTop(this.rlphoto);
                for (int i = this.rlphoto.getChildCount() - 1; i >= 0; i--) {
                    try {
                        if (this.rlphoto.getChildAt(i) instanceof StickerView1) {
                            StickerView1 stickerView = (StickerView1) this.rlphoto.getChildAt(i);
                            if (!stickerView.isInDelete((float) x, (float) y) || !stickerView.isFocusable()) {
                                if (stickerView.isInController((float) x, (float) y) && stickerView.isFocusable()) {
                                    this.checkTouch = true;
                                    break;
                                } else if (!stickerView.isInEdit((float) x, (float) y) || !stickerView.isFocusable()) {
                                    if (stickerView.isInFlip((float) x, (float) y) && stickerView.isFocusable()) {
                                        this.checkTouch = true;
                                        break;
                                    } else if (stickerView.getContentRect().contains((float) x, (float) y)) {
                                        this.checkTouch = true;
                                        if (!stickerView.isFocusable()) {
                                            resetStickersFocus();
                                            stickerView.setFocusable(true);
                                            stickerView.bringToFront();
                                        }
                                        if (!this.firstTouch || System.currentTimeMillis() - this.timetouch > 300) {
                                            this.firstTouch = true;
                                            this.timetouch = System.currentTimeMillis();
                                        } else {
                                            this.firstTouch = false;
                                            if (!stickerView.getText().equals("")) {
                                                this.toolbarTitle.setText(getResources().getString(R.string.edittext));
                                                this.kindEdit = 1;
                                                stickerView.setEdit(true);
                                                this.menuItemCrop.setIcon(this.menuItemCropIconDone);
                                                this.rltext.setVisibility(View.VISIBLE);
                                                this.rvtext.setVisibility(View.GONE);
                                                this.edtext.setVisibility(View.VISIBLE);
                                                this.ivalign.setTag(stickerView.getAlign());
                                                this.edtext.requestFocus();
                                                if (this.ivalign.getTag().equals(1)) {
                                                    this.afltext.setGravity(17);
                                                    this.ivalign.setImageResource(R.drawable.iccentertextalignment);
                                                } else if (this.ivalign.getTag().equals(2)) {
                                                    this.afltext.setGravity(3);
                                                    this.ivalign.setImageResource(R.drawable.icalignleft);
                                                } else if (this.ivalign.getTag().equals(3)) {
                                                    this.afltext.setGravity(5);
                                                    this.ivalign.setImageResource(R.drawable.icalignright);
                                                }
                                                this.ivcircle.setTag(stickerView.getCircle());
                                                if (this.ivcircle.getTag().equals(0)) {
                                                    this.ivcircle.setImageResource(R.drawable.iccircle);
                                                    this.afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
                                                } else if (this.ivcircle.getTag().equals(1)) {
                                                    this.ivcircle.setImageResource(R.drawable.iccirclepressed);
                                                    this.afltext.setShadowLayer(1.6f, StickerView1.MAX_SCALE_SIZE, StickerView1.MAX_SCALE_SIZE, -1);
                                                }
                                                loadSampleText(stickerView.getColor(), stickerView.getFont(), stickerView.getText());
                                                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(this.edtext, 1);
                                            }
                                        }
                                    }
                                } else {
                                    this.checkTouch = true;
                                    if (stickerView.getText().equals("")) {
                                        isStickerText =true;
                                        ColorPickerDialog.newBuilder().show(EditActivity.this);



                                    } else {
                                        this.toolbarTitle.setText(getResources().getString(R.string.edittext));
                                        this.kindEdit = 1;
                                        stickerView.setEdit(true);
                                        this.menuItemCrop.setIcon(this.menuItemCropIconDone);
                                        this.rltext.setVisibility(View.VISIBLE);
                                        this.rvtext.setVisibility(View.GONE);
                                        this.edtext.setVisibility(View.VISIBLE);
                                        this.ivalign.setTag(stickerView.getAlign());
                                        this.edtext.requestFocus();
                                        if (this.ivalign.getTag().equals(1)) {
                                            this.afltext.setGravity(17);
                                            this.ivalign.setImageResource(R.drawable.iccentertextalignment);
                                        } else if (this.ivalign.getTag().equals(2)) {
                                            this.afltext.setGravity(3);
                                            this.ivalign.setImageResource(R.drawable.icalignleft);
                                        } else if (this.ivalign.getTag().equals(3)) {
                                            this.afltext.setGravity(5);
                                            this.ivalign.setImageResource(R.drawable.icalignright);
                                        }
                                        this.ivcircle.setTag(stickerView.getCircle());
                                        if (this.ivcircle.getTag().equals(0)) {
                                            this.ivcircle.setImageResource(R.drawable.iccircle);
                                            this.afltext.setShadowLayer(0.0f, 0.0f, 0.0f, -1);
                                        } else if (this.ivcircle.getTag().equals(1)) {
                                            this.ivcircle.setImageResource(R.drawable.iccirclepressed);
                                            this.afltext.setShadowLayer(1.6f, StickerView1.MAX_SCALE_SIZE, StickerView1.MAX_SCALE_SIZE, -1);
                                        }
                                        loadSampleText(stickerView.getColor(), stickerView.getFont(), stickerView.getText());
                                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(this.edtext, 1);
                                    }
                                }
                            } else {
                                this.checkTouch = true;
                                break;
                            }
                        }
                        continue;
                    } catch (Exception e) {
                        Log.i("Glitter Photo Effect", e.getMessage());
                    }
                }
            }
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                Rect rect = new Rect(rvselect.getLeft(), this.rvselect.getTop(), this.widthScreen, this.rvselect.getTop() + this.rvselect.getHeight());
                if (!this.checkTouch) {
                    if (!isStickerText) {
                        resetStickersFocus();
                    } else if (!rect.contains((int) ev.getX(), (int) ev.getY())) {
                        resetStickersFocus();
                    }
                }
            }
        }
        try {
            z = super.dispatchTouchEvent(ev);
        } catch (Exception e2) {
        }
        return z;
    }

    private void loadPointforSlider() {
        try {
            Bitmap bmpoint = BitmapFactory.decodeResource(getResources(), R.drawable.slidersmall);
            this.wthumb = DisplayUtil.dip2px(this, 30.0f);
            this.sbslider.setThumb(new BitmapDrawable(getResources(), BitmapProcessing.resizeBitmap(bmpoint, this.wthumb, this.wthumb)));
            int wpoint = this.wthumb / 3;
            int top = this.wthumb + wpoint;
            this.center = this.widthScreen / 2;
            this.dis = (this.widthScreen - (this.wthumb * 2)) / 12;
            int start = this.wthumb - (wpoint / 2);
            for (int i = 0; i < 13; i++) {
                LayoutParams lp = new LayoutParams(wpoint, wpoint);
                lp.setMargins(start, top, 0, 0);
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(lp);
                iv.setImageBitmap(bmpoint);
                this.rlslider.addView(iv);
                start += this.dis;
            }
            this.sbslider.bringToFront();
            this.sbslider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (type == 1) {
                        exposure = seekBar.getProgress();
                    } else if (type == 2) {
                        contrast = seekBar.getProgress();
                    } else if (type == 3) {
                        sharpen = seekBar.getProgress();
                    } else if (type == 4) {
                        highlight = seekBar.getProgress();
                    } else if (type == 5) {
                        shadow = seekBar.getProgress();
                    } else if (type == 6) {
                        temperature = seekBar.getProgress();
                    } else if (type == 7) {
                        vignette = seekBar.getProgress();
                    }
                    setTextSlider(seekBar.getProgress());
                    gpuEffect();
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        setTextSlider(progress);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private void setTextSlider(int progress) {
        try {
            this.sbslider.setProgress(progress);
            if (this.type == 1) {
                this.tvslider.setText(String.valueOf(progress - 6));
            } else if (this.type == 2) {
                this.tvslider.setText(String.valueOf(progress));
            } else if (this.type == 3) {
                this.tvslider.setText(String.valueOf(progress));
            } else if (this.type == 4) {
                this.tvslider.setText(String.valueOf(progress));
            } else if (this.type == 5) {
                this.tvslider.setText(String.valueOf(progress));
            } else if (this.type == 6) {
                this.tvslider.setText(String.valueOf(progress - 6));
            } else if (this.type == 7) {
                this.tvslider.setText(String.valueOf(progress));
            }
            LayoutParams lpram = (LayoutParams) this.tvslider.getLayoutParams();
            lpram.setMargins((this.center - this.wthumb) + ((progress - 6) * this.dis), DisplayUtil.dip2px(this, 5.0f), 0, 0);
            this.tvslider.setLayoutParams(lpram);
        } catch (Exception e) {
        }
    }

    private void loadFrame() {
        try {
            this.listItem = getAssets().list("frames");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                tmplist.add("frames/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadFrame2() {
        try {
            this.listItem = getAssets().list("frames2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                    tmplist.add("frames2/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadABC() {
        try {
            this.listItem = getAssets().list("texts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                    tmplist.add("texts/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadGift() {
        try {
            this.listItem = getAssets().list("gift");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                    tmplist.add("gift/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadSanta() {
        try {
            this.listItem = getAssets().list("santa");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                tmplist.add("santa/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadHat() {
        try {
            this.listItem = getAssets().list("Caps");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                tmplist.add("Caps/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadTrees() {
        try {
            this.listItem = getAssets().list("tree");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                tmplist.add("tree/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadLights() {
        try {
            this.listItem = getAssets().list("light");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {

                tmplist.add("light/" + aListItem);

            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.galleryAdapter = new GalleryAdapter(this.listItem, this);
            this.rvselect.setAdapter(this.galleryAdapter);
            setClick();
        }
    }

    private void loadEffect() {
        try {
            this.listItem = getAssets().list("effects");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listItem != null) {
            List<String> tmplist = new ArrayList();
            for (String aListItem : this.listItem) {
                if (aListItem.contains("thumb_")) {
                    tmplist.add("effects/" + aListItem);
                }
            }
            this.listItem = tmplist.toArray(new String[tmplist.size()]);
            this.rvselect.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (VERSION.SDK_INT >= 16) {
                        rvselect.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        rvselect.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    effectAdapter = new EffectAdapter(listItem, EditActivity.this, rvselect.getHeight());
                    rvselect.setAdapter(effectAdapter);
                    effectClick();
                }
            });
        }
    }

    private void loadFont() {
        try {
            this.listfont = getAssets().list("fonts");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.listfont != null) {
            for (int i = 0; i < this.listfont.length; i++) {
                this.listfont[i] = "fonts/" + this.listfont[i];
            }
            FontAdapter fontAdapter = new FontAdapter(this.listfont, this);
            rvtext.setLayoutManager(new GridLayoutManager(this,2));
            this.rvtext.setAdapter(fontAdapter);
            fontAdapter.setOnItemClickListener(new FontAdapter.OnRecyclerViewItemClickListener() {
                public void onItemClick(View view, String resId) {
                    loadSampleText("", resId, "");
                }
            });
        }
    }

    private void loadSampleText(String color, String font, String text) {
        if (!text.equals("")) {
            this.afltext.setText(text);
            this.edtext.setText(text);
            this.textsample = text;
        }
        if (!color.equals("")) {
            this.afltext.setTextColor(Color.parseColor(color));
            this.colorsample = color;
        }
        if (!font.equals("")) {
            this.afltext.setTypeface(Typeface.createFromAsset(getAssets(), font));
            this.fontsample = font;
        }
    }

    private void closeKeyboard() {

        View view = getCurrentFocus();

        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void clickable(boolean bool) {
        this.icframe.setClickable(bool);
        this.icframe2.setClickable(bool);
        this.iceffect.setClickable(bool);
        this.icGift.setClickable(bool);
        this.ictext.setClickable(bool);
        this.icabc.setClickable(bool);

        this.icadjust.setClickable(bool);
        this.rlphoto.setClickable(bool);
    }



    private void gpuEffect() {
        try {
            float tmp;
            Boolean isChange = Boolean.FALSE;
            GPUImageFilterGroup filterGroup = new GPUImageFilterGroup();
            if (this.exposure != 6) {
                isChange = Boolean.TRUE;
                tmp = 2.0f - ((((float) (this.exposure - 6)) * StickerView1.MIN_SCALE_SIZE) + 1.0f);
                GPUImageGammaFilter gammafilter = new GPUImageGammaFilter();
                gammafilter.setGamma(tmp);
                filterGroup.addFilter(gammafilter);
            }
            if (this.contrast != 0) {
                isChange = Boolean.TRUE;
                tmp = (((float) this.contrast) * StickerView1.MIN_SCALE_SIZE) + 1.0f;
                GPUImageContrastFilter contrastfilter = new GPUImageContrastFilter();
                contrastfilter.setContrast(tmp);
                filterGroup.addFilter(contrastfilter);
            }
            if (this.sharpen != 0) {
                isChange = Boolean.TRUE;
                tmp = ((float) this.sharpen) * StickerView1.MIN_SCALE_SIZE;
                GPUImageSharpenFilter sharpenfilter = new GPUImageSharpenFilter();
                sharpenfilter.setSharpness(tmp);
                filterGroup.addFilter(sharpenfilter);
            }
            if (!(this.highlight == 0 && this.shadow == 0)) {
                isChange = Boolean.TRUE;
                float tmphl = 1.0f - (((float) this.highlight) * 0.08f);
                float tmpsd = ((float) this.shadow) * 0.08f;
                GPUImageHighlightShadowFilter hsdfilter = new GPUImageHighlightShadowFilter();
                hsdfilter.setHighlights(tmphl);
                hsdfilter.setShadows(tmpsd);
                filterGroup.addFilter(hsdfilter);
            }
            if (this.temperature != 6) {
                isChange = Boolean.TRUE;
                int change = 400;
                if (this.temperature < 6) {
                    change = 200;
                }
                tmp = 5000.0f + ((float) ((this.temperature - 6) * change));
                GPUImageWhiteBalanceFilter whiteblancefilter = new GPUImageWhiteBalanceFilter();
                whiteblancefilter.setTemperature(tmp);
                filterGroup.addFilter(whiteblancefilter);
            }
            if (this.vignette != 0) {
                isChange = Boolean.TRUE;
                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                float[] fArr = new float[3];
                filterGroup.addFilter(new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 1.0f - (((float) this.vignette) * 0.01f)));
            }
            if (!this.filter.contains("thumb_meffect_00001")) {
                isChange = Boolean.TRUE;
                GPUImageLookupFilter lookupfilter = new GPUImageLookupFilter();

                lookupfilter.setBitmap(BitmapLoader.loadFromAsset(this, new int[]{512, 512}, this.filter.replace("thumb_", "").replace("jpg", "png")));
                filterGroup.addFilter(lookupfilter);
            }
            if (isChange) {
                this.gpuview.setFilter(filterGroup);
                this.gpuview.requestRender();
                return;
            }
            this.gpuview.setFilter(new GPUImageFilter());
            this.gpuview.requestRender();
        } catch (Exception e) {
        }
    }

    private void setChange() {
        try {
            if (this.type == 1) {
                this.toolbarTitle.setText(getResources().getString(R.string.editexposure));
            } else if (this.type == 2) {
                this.toolbarTitle.setText(getResources().getString(R.string.editcontrast));
            } else if (this.type == 3) {
                this.toolbarTitle.setText(getResources().getString(R.string.editsharpen));
            } else if (this.type == 4) {
                this.toolbarTitle.setText(getResources().getString(R.string.edithightlightsave));
            } else if (this.type == 5) {
                this.toolbarTitle.setText(getResources().getString(R.string.editshadowsave));
            } else if (this.type == 6) {
                this.toolbarTitle.setText(getResources().getString(R.string.edittemperature));
            } else if (this.type == 7) {
                this.toolbarTitle.setText(getResources().getString(R.string.editvignette));
            }
        } catch (Exception e) {
        } catch (OutOfMemoryError e2) {
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
