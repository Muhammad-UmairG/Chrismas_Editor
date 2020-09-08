package com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.os.Build.VERSION;



import com.furiousapps.glitter.photo.frames.effects.filter.editor.other.ColorFilterGenerator;

import java.lang.reflect.Array;
import java.util.Random;

import jp.co.cyberagent.android.gpuimage.BuildConfig;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

public class BitmapProcessing {
    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        float f;
        float f2 = -1.0f;
        Matrix matrix = new Matrix();
        if (horizontal) {
            f = -1.0f;
        } else {
            f = 1.0f;
        }
        if (!vertical) {
            f2 = 1.0f;
        }
        matrix.preScale(f, f2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap BlurImage(Context context, Bitmap src, int radius) {
        Bitmap bm = null;
        try {
            GPUImage mGPUImage = new GPUImage(context);
            GPUImageGaussianBlurFilter mGaussianBlur = new GPUImageGaussianBlurFilter();
            mGaussianBlur.setBlurSize((float) radius);
            mGPUImage.setFilter(mGaussianBlur);
            mGPUImage.setImage(src);
            mGPUImage.requestRender();
            bm = mGPUImage.getBitmapWithFilterApplied();
        } catch (Exception e) {
        }
        return bm;
    }

    public static Bitmap LookupImage(Context context, Bitmap src, Bitmap lookup) {
        Bitmap bm = null;
        try {
            GPUImage mGPUImage = new GPUImage(context);
            GPUImageLookupFilter lookUpFilter = new GPUImageLookupFilter();
            lookUpFilter.setBitmap(lookup);
            mGPUImage.setFilter(lookUpFilter);
            mGPUImage.setImage(src);
            mGPUImage.requestRender();
            bm = mGPUImage.getBitmapWithFilterApplied();
        } catch (Exception e) {
        }
        return bm;
    }

    public static Bitmap cdepth(Bitmap src, int bitOffset) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                R = (((bitOffset / 2) + R) - (((bitOffset / 2) + R) % bitOffset)) - 1;
                if (R < 0) {
                    R = 0;
                }
                G = (((bitOffset / 2) + G) - (((bitOffset / 2) + G) % bitOffset)) - 1;
                if (G < 0) {
                    G = 0;
                }
                B = (((bitOffset / 2) + B) - (((bitOffset / 2) + B) % bitOffset)) - 1;
                if (B < 0) {
                    B = 0;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        return bmOut;
    }

    public static Bitmap noise(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[(width * height)];
        source.getPixels(pixels, 0, width, 0, 0, width, height);
        Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = (y * width) + x;
                pixels[index] = pixels[index] | Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
            }
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        source.recycle();
        return bmOut;
    }

    public static Bitmap brightness(Bitmap src, int value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                R += value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }
                G += value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }
                B += value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        return bmOut;
    }

    public static Bitmap sepia(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = (int) (((0.3d * ((double) Color.red(pixel))) + (0.59d * ((double) Color.green(pixel)))) + (0.11d * ((double) Color.blue(pixel))));
                int G = R;
                int B = R;
                /*R += R.styleable.AppCompatTheme_windowActionBarOverlay;*/
                if (R > 255) {
                    R = 255;
                }
                G += 65;
                if (G > 255) {
                    G = 255;
                }
                B += 20;
                if (B > 255) {
                    B = 255;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        return bmOut;
    }

    public static Bitmap gamma(Bitmap src, double red, double green, double blue) {
        red = (2.0d + red) / 10.0d;
        green = (2.0d + green) / 10.0d;
        blue = (2.0d + blue) / 10.0d;
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int width = src.getWidth();
        int height = src.getHeight();
        int[] gammaR = new int[BuildConfig.VERSION_CODE];
        int[] gammaG = new int[BuildConfig.VERSION_CODE];
        int[] gammaB = new int[BuildConfig.VERSION_CODE];
        for (int i = 0; i < 256; i++) {
            gammaR[i] = Math.min(255, (int) ((255.0d * Math.pow(((double) i) / 255.0d, 1.0d / red)) + 0.5d));
            gammaG[i] = Math.min(255, (int) ((255.0d * Math.pow(((double) i) / 255.0d, 1.0d / green)) + 0.5d));
            gammaB[i] = Math.min(255, (int) ((255.0d * Math.pow(((double) i) / 255.0d, 1.0d / blue)) + 0.5d));
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = src.getPixel(x, y);
                bmOut.setPixel(x, y, Color.argb(Color.alpha(pixel), gammaR[Color.red(pixel)], gammaG[Color.green(pixel)], gammaB[Color.blue(pixel)]));
            }
        }
        src.recycle();
        return bmOut;
    }

    public static Bitmap contrast(Bitmap src, double value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        Canvas c = new Canvas();
        c.setBitmap(bmOut);
        c.drawBitmap(src, 0.0f, 0.0f, new Paint(-16777216));
        double contrast = Math.pow((100.0d + value) / 100.0d, 2.0d);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * contrast) + 0.5d) * 255.0d);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }
                int G = (int) (((((((double) Color.green(pixel)) / 255.0d) - 0.5d) * contrast) + 0.5d) * 255.0d);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }
                int B = (int) (((((((double) Color.blue(pixel)) / 255.0d) - 0.5d) * contrast) + 0.5d) * 255.0d);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        return bmOut;
    }

    public static Bitmap saturation(Bitmap src, int value) {
        float f_value = (float) (((double) value) / 100.0d);
        Bitmap bitmapResult = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(f_value);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvasResult.drawBitmap(src, 0.0f, 0.0f, paint);
        src.recycle();
        return bitmapResult;
    }

    public static Bitmap grayscale(Bitmap src) {
        ColorMatrix colorMatrixGray = new ColorMatrix(new float[]{0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.213f, 0.715f, 0.072f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        Bitmap bitmapResult = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrixGray));
        canvasResult.drawBitmap(src, 0.0f, 0.0f, paint);
        src.recycle();
        return bitmapResult;
    }

    public static Bitmap vignette(Bitmap image) {
        int width = image.getWidth();
        RadialGradient gradient = new RadialGradient((float) (width / 2), (float) (image.getHeight() / 2), (float) (((double) width) / 1.2d), new int[]{0, 1426063360, -16777216}, new float[]{0.0f, 0.5f, 1.0f}, TileMode.CLAMP);
        Canvas canvas = new Canvas(image);
        canvas.drawARGB(1, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(-16777216);
        paint.setShader(gradient);
        Rect rect = new Rect(0, 0, image.getWidth(), image.getHeight());
        canvas.drawRect(new RectF(rect), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(image, rect, rect, paint);
        return image;
    }

    public static Bitmap hue(Bitmap bitmap, float hue) {
        Bitmap newBitmap = bitmap.copy(bitmap.getConfig(), true);
        int width = newBitmap.getWidth();
        int height = newBitmap.getHeight();
        float[] hsv = new float[3];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = newBitmap.getPixel(x, y);
                Color.colorToHSV(pixel, hsv);
                hsv[0] = hue;
                newBitmap.setPixel(x, y, Color.HSVToColor(Color.alpha(pixel), hsv));
            }
        }
        bitmap.recycle();
        return newBitmap;
    }

    public static Bitmap tint(Bitmap src, int color) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Paint p = new Paint(-65536);
        p.setColorFilter(new LightingColorFilter(color, 1));
        Canvas c = new Canvas();
        c.setBitmap(bmOut);
        c.drawBitmap(src, 0.0f, 0.0f, p);
        src.recycle();
        return bmOut;
    }

    public static Bitmap screenBitmap(Bitmap base, Bitmap over, int alpha) {
        int width = base.getWidth();
        int height = base.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        p.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
        p.setShader(new BitmapShader(over, TileMode.CLAMP, TileMode.CLAMP));
        p.setAlpha(alpha);
        Canvas c = new Canvas(result);
        c.drawBitmap(base, 0.0f, 0.0f, null);
        c.drawRect(0.0f, 0.0f, (float) width, (float) height, p);
        return result;
    }

    public static Bitmap overlaybitmap(Bitmap base, Bitmap over, int alpha) {
        int width = base.getWidth();
        int height = base.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        if (VERSION.SDK_INT > 10) {
            p.setXfermode(new PorterDuffXfermode(Mode.OVERLAY));
        } else {
            p.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
        }
        p.setShader(new BitmapShader(over, TileMode.CLAMP, TileMode.CLAMP));
        p.setAlpha(alpha);
        Canvas c = new Canvas(result);
        c.drawBitmap(base, 0.0f, 0.0f, null);
        c.drawRect(0.0f, 0.0f, (float) width, (float) height, p);
        return result;
    }

    public static Bitmap cropbitmap(Bitmap base, int boder) {
        Bitmap result = Bitmap.createBitmap(base.getWidth() - boder, base.getHeight() - boder, Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        new Canvas(result).drawBitmap(base, new Rect(boder / 2, boder / 2, base.getWidth() - (boder / 2), base.getHeight() - (boder / 2)), new Rect(0, 0, base.getWidth() - boder, base.getHeight() - boder), p);
        return result;
    }

    public static Bitmap hue2(Bitmap src, int hue) {
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Paint p = new Paint();
        p.setColorFilter(ColorFilterGenerator.adjustColor(0, 0, 0, hue));
        Canvas c = new Canvas();
        c.setBitmap(bmOut);
        c.drawBitmap(src, 0.0f, 0.0f, p);
        return bmOut;
    }

    public static Bitmap makeGradient(Bitmap src, int color0, int color1, int alpha) {
        int width = src.getWidth();
        int height = src.getHeight();
        LinearGradient gradient = new LinearGradient((float) (width / 2), 0.0f, (float) (width / 2), (float) height, color0, color1, TileMode.REPEAT);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);
        p.setShader(gradient);
        p.setAlpha((alpha * 255) / 100);
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        Canvas c = new Canvas(bmOut);
        c.drawBitmap(src, new Matrix(), null);
        c.drawRect(0.0f, 0.0f, (float) width, (float) height, p);
        return bmOut;
    }

    public static Bitmap invert(Bitmap src) {
        Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int height = src.getHeight();
        int width = src.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = src.getPixel(x, y);
                output.setPixel(x, y, Color.argb(Color.alpha(pixelColor), 255 - Color.red(pixelColor), 255 - Color.green(pixelColor), 255 - Color.blue(pixelColor)));
            }
        }
        src.recycle();
        return output;
    }

    public static Bitmap boost(Bitmap src, int type, float percent) {
        percent /= 100.0f;
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = src.getPixel(x, y);
                int A = Color.alpha(pixel);
                int R = Color.red(pixel);
                int G = Color.green(pixel);
                int B = Color.blue(pixel);
                if (type == 1) {
                    R = (int) (((float) R) * (1.0f + percent));
                    if (R > 255) {
                        R = 255;
                    }
                } else if (type == 2) {
                    G = (int) (((float) G) * (1.0f + percent));
                    if (G > 255) {
                        G = 255;
                    }
                } else if (type == 3) {
                    B = (int) (((float) B) * (1.0f + percent));
                    if (B > 255) {
                        B = 255;
                    }
                }
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        return bmOut;
    }

    public static final Bitmap sketch(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        int[][] pixels = (int[][]) Array.newInstance(Integer.TYPE, new int[]{3, 3});
        for (int y = 0; y < height - 2; y++) {
            for (int x = 0; x < width - 2; x++) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }
                int A = Color.alpha(pixels[1][1]);
                int sumG = 0;
                int sumR = 0;
                sumG = ((((Color.green(pixels[1][1]) * 6) - Color.green(pixels[0][0])) - Color.green(pixels[0][2])) - Color.green(pixels[2][0])) - Color.green(pixels[2][2]);
                int sumB = ((((Color.blue(pixels[1][1]) * 6) - Color.blue(pixels[0][0])) - Color.blue(pixels[0][2])) - Color.blue(pixels[2][0])) - Color.blue(pixels[2][2]);
                int R = (((((Color.red(pixels[1][1]) * 6) - Color.red(pixels[0][0])) - Color.red(pixels[0][2])) - Color.red(pixels[2][0])) - Color.red(pixels[2][2])) + 130;
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }
                int G = sumG + 130;
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }
                int B = sumB + 130;
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }
                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }
        src.recycle();
        return result;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_url) {
        try {
            switch (new ExifInterface(image_url).getAttributeInt("Orientation", 1)) {
                case 2 /*2*/:
                    return flip(bitmap, true, false);
                case 3 /*3*/:
                    return rotate(bitmap, 180.0f);
                case 4 /*4*/:
                    return flip(bitmap, false, true);
                case 6 /*6*/:
                    return rotate(bitmap, 90.0f);
                case 8 /*8*/:
                    return rotate(bitmap, 270.0f);
                default:
                    return bitmap;
            }
        } catch (Exception e) {
            return bitmap;
        } catch (OutOfMemoryError e2) {
            return bitmap;
        }
    }

    public static Bitmap resizeBitmap(Bitmap base, int width, int height) {
        try {
            Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setFilterBitmap(true);
            p.setDither(true);
            new Canvas(result).drawBitmap(base, new Rect(0, 0, base.getWidth(), base.getHeight()), new Rect(0, 0, width, height), p);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return base;
        }
    }

    public static Bitmap overlayBlend(Bitmap src, Bitmap layer, int alpha) {
        int width = src.getWidth();
        int height = src.getHeight();
        Paint paint = new Paint();
        paint.setAlpha((alpha * 255) / 100);
        if (VERSION.SDK_INT > 10) {
            paint.setXfermode(new PorterDuffXfermode(Mode.OVERLAY));
        } else {
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_OVER));
        }
        Bitmap bmOut = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(bmOut);
        c.drawBitmap(src, 0.0f, 0.0f, null);
        c.drawBitmap(layer, 0.0f, 0.0f, paint);
        return bmOut;
    }

    public static Bitmap applyMask(Bitmap source, Bitmap mask, int maskPosX, int maskPosY) {
        int maskWidth = mask.getWidth();
        int maskHeight = mask.getHeight();
        Bitmap bitmap = copy(source);
        bitmap.setHasAlpha(true);
        if (maskPosX + maskWidth > bitmap.getWidth()) {
            maskWidth = bitmap.getWidth() - maskPosX;
        }
        if (maskPosY + maskHeight > bitmap.getHeight()) {
            maskHeight = bitmap.getHeight() - maskPosY;
        }
        bitmap = Bitmap.createBitmap(bitmap, maskPosX, maskPosY, maskWidth, maskHeight);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(mask, 0.0f, 0.0f, paint);
        return bitmap;
    }

    public static Bitmap copy(Bitmap src) {
        if (src.isMutable()) {
            return src;
        }
        return src.copy(Config.ARGB_8888, true);
    }
}
