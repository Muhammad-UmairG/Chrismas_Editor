package com.furiousapps.glitter.photo.frames.effects.filter.editor.other;

import android.content.Context;
import android.provider.Settings.Secure;
import android.view.View;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static String ALBUM = "GlitterFrames";
    public static String CROPPED_IMAGE_NAME = "CropImage.jpg";

    public static String FONT_MAIN = "fonts/f0.ttf";

    public static boolean IS_DISPLAY_ADS = true;
    public static int KIND_REQUEST = 1;

    public static String md5_Hash(String s) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(), 0, s.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

    public static String getDeviceID(Context context) {
        return md5_Hash(Secure.getString(context.getContentResolver(), "android_id")).toUpperCase();
    }



    public static int getRelativeLeft(View myView) {
        if (myView.getParent() == myView.getRootView()) {
            return myView.getLeft();
        }
        return getRelativeLeft((View) myView.getParent()) + myView.getLeft();
    }

    public static int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView()) {
            return myView.getTop();
        }
        return getRelativeTop((View) myView.getParent()) + myView.getTop();
    }
}
