package com.furiousapps.glitter.photo.frames.effects.filter.editor.other;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ColorCircleDrawable extends Drawable {
    private final Paint mPaint = new Paint(1);
    private int mRadius = 0;

    public ColorCircleDrawable(int color) {
        mPaint.setColor(color);
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle((float) bounds.centerX(), (float) bounds.centerY(), (float) mRadius, mPaint);
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRadius = Math.min(bounds.width(), bounds.height()) / 2;
    }

    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
