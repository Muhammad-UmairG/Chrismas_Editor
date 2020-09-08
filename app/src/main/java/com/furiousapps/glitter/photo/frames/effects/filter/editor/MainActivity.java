package com.furiousapps.glitter.photo.frames.effects.filter.editor;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.Util;
import com.kobakei.ratethisapp.RateThisApp;
import com.kobakei.ratethisapp.RateThisApp.Config;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.File;
import java.util.List;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.EasyImage.ImageSource;


public class MainActivity extends BaseActivity {

    private @Nullable
    AdView bannerAdView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RelativeLayout adslayout= (RelativeLayout) findViewById(R.id.rlads);

        RateThisApp.init(new Config(5, 5));
        RateThisApp.onCreate(this);
        RateThisApp.showRateDialogIfNeeded(this);
        bannerAdView = new AdView(this, getResources().getString(R.string.banneradsrect),
                AdSize.RECTANGLE_HEIGHT_250);

        // Reposition the ad and add it to the view hierarchy.
        adslayout.addView(bannerAdView);

        if (Util.IS_DISPLAY_ADS) {
            bannerAdView.loadAd();
        }
        ((TextView) findViewById(R.id.appname)).setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT_MAIN));
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case BaseActivity.REQUEST_STORAGE_WRITE_ACCESS_PERMISSION /*102*/:
                try {
                    if (grantResults[0] != 0) {
                        return;
                    }
                    if (Util.KIND_REQUEST == 1) {
                        if (EasyImage.canDeviceHandleGallery(this)) {
                            EasyImage.openGallery(this, 0);
                            return;
                        } else {
                            EasyImage.openDocuments(this, 0);
                            return;
                        }
                    } else if (Util.KIND_REQUEST == 2) {
                        EasyImage.openCamera(this, 0);
                        return;
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    try {
                        if (Util.KIND_REQUEST == 1) {
                            if (EasyImage.canDeviceHandleGallery(this)) {
                                EasyImage.openGallery(this, 0);
                                return;
                            } else {
                                EasyImage.openDocuments(this, 0);
                                return;
                            }
                        } else if (Util.KIND_REQUEST == 2) {
                            EasyImage.openCamera(this, 0);
                            return;
                        } else {
                            return;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        return;
                    }
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Intent intent = new Intent(this, EditActivity.class);
                    intent.setData(resultUri);
                    startActivity(intent);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "Error is: "+error, Toast.LENGTH_SHORT).show();
                }

                /*Uri resultUri = UCrop.getOutput(result);
                if (resultUri != null) {
                    Intent intent = new Intent(this, EditActivity.class);
                    intent.setData(resultUri);
                    startActivity(intent);
                }

                handleCropResult(data);*/
            } else {
                EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                    public void onImagePickerError(Exception e, ImageSource source, int type) {
                        e.printStackTrace();
                    }

                    public void onImagesPicked(List<File> imageFiles, ImageSource source, int type) {
                        onPhotosReturned(imageFiles);
                    }

                    public void onCanceled(ImageSource source, int type) {
                        if (source == ImageSource.CAMERA) {
                            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                            if (photoFile != null) {
                                photoFile.delete();
                            }
                        }
                    }
                });
            }
        }
    }
    private void onPhotosReturned(List<File> returnedPhotos) {
        //advancedConfig(basisConfig(UCrop.of(Uri.fromFile(returnedPhotos.get(0)), Uri.fromFile(new File(getCacheDir(), Util.CROPPED_IMAGE_NAME))))).start(this);
        //CropImage.activity((Uri) returnedPhotos).start(this);
        CropImage.activity(Uri.fromFile(returnedPhotos.get(0))).start(this);
    }
    /*private void handleCropResult(@NonNull Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            Intent intent = new Intent(this, EditActivity.class);
            intent.setData(resultUri);
            startActivity(intent);
        }
    }*/

    /*private void handleCropError(@NonNull Intent result) {
        Log.i("Photos to Collage", UCrop.getError(result).getMessage());
    }*/




    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        if (bannerAdView != null) {
            bannerAdView.destroy();
        }
        super.onDestroy();
    }

    public void onCameraClick(View view) {
        try {
            Util.KIND_REQUEST = 2;
            if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestPermission("android.permission.WRITE_EXTERNAL_STORAGE", MainActivity.this.getString(R.string.permission_write_storage_rationale), BaseActivity.REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
            } else {
                EasyImage.openCamera(MainActivity.this, 0);
            }
        } catch (Exception e) {
            Log.i("Photos to Collage", e.getMessage());
        }

    }

    public void onGalleryClick(View view) {
        try {
            Util.KIND_REQUEST = 1;
            if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                requestPermission("android.permission.WRITE_EXTERNAL_STORAGE", MainActivity.this.getString(R.string.permission_write_storage_rationale),BaseActivity.REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
            } else if (EasyImage.canDeviceHandleGallery(MainActivity.this)) {
                EasyImage.openGallery(MainActivity.this, 0);
            } else {
                EasyImage.openDocuments(MainActivity.this, 0);
            }
        } catch (Exception e) {
            Log.i("Photos to Collage", e.getMessage());
        }

    }

    public void onShareClick(View view) {

        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    public void onMyCreationClick(View view) {
        Intent intent=new Intent(this, CollectionActivity.class);
        startActivity(intent);
    }

    public void onPrivacyClick(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/furious-apps"));
        startActivity(browserIntent);

    }
    public void onMoreClick(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=FuriousApps"));
        startActivity(browserIntent);

    }


    public void onRateClick(View view) {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit")
                .setMessage("Are you sure you want to close the Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
