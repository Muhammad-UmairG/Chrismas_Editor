package com.furiousapps.glitter.photo.frames.effects.filter.editor.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.furiousapps.glitter.photo.frames.effects.filter.editor.R;
import com.furiousapps.glitter.photo.frames.effects.filter.editor.bitmap.BitmapProcessing;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class StickerView1 extends View {


    public static final float MAX_SCALE_SIZE = 4.0f;
    public static final float MIN_SCALE_SIZE = 0.1f;
    private float[] mOriginPoints;
    private float[] mPoints;
    private RectF mOriginContentRect;
    private RectF mContentRect;
    private RectF mViewRect;

    private float mLastPointX, mLastPointY;

    private Bitmap mBitmap;
    private Bitmap mControllerBitmap, mDeleteBitmap;
    private Bitmap mReversalHorBitmap,mReversalVerBitmap;
    private Matrix mMatrix;
    private Paint mPaint, mBorderPaint;
    private float mControllerWidth, mControllerHeight, mDeleteWidth, mDeleteHeight;
    private float mReversalHorWidth,mReversalHorHeight,mReversalVerWidth,mReversalVerHeight;
    private boolean mInController, mInMove;
    private boolean mInReversalHorizontal,mInReversalVertical;

    private boolean mDrawController = true;
    //private boolean mCanTouch;
    private float mStickerScaleSize = 1.0f;

    private OnStickerDeleteListener mOnStickerDeleteListener;


    private int align;
    private int circle;
    private String color;
    private boolean drawedit;
    private String font;
    private boolean isEdit;
    private Bitmap mEditBitmap;
    private float mEditHeight;
    private float mEditWidth;
    private Bitmap mFlipBitmap;
    private float mFlipHeight;
    private float mFlipWidth;
    private boolean mInDelete;
    private boolean mInEdit;
    private boolean mInFlip;
    private String text;

    public interface OnStickerDeleteListener {
        void onDelete(StickerView1 stickerView);
    }

    public StickerView1(Context context, boolean isDraw) {
        this(context, null);
        setDrawedit(isDraw);
    }

    public StickerView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickerView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDrawController = true;
        mStickerScaleSize = 1.0f;
        text = "";
        font = "";
        color = "";
        isEdit = false;
        align = 1;
        circle = 0;
        drawedit = false;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(MAX_SCALE_SIZE);
        mPaint.setColor(Color.WHITE);

        mBorderPaint = new Paint(mPaint);
        mBorderPaint.setColor(Color.parseColor("#B2ffffff"));
        mBorderPaint.setShadowLayer((float) DisplayUtil.dip2px(getContext(), 2.0f), 0.0f, 0.0f, Color.parseColor("#33000000"));
        int width = DisplayUtil.getDisplayWidthPixels(getContext());

        mControllerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_resize);
        mControllerBitmap = BitmapProcessing.resizeBitmap(mControllerBitmap, width / 12, width / 12);
        mControllerWidth = (float) mControllerBitmap.getWidth();
        mControllerHeight = (float) mControllerBitmap.getHeight();

        mDeleteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_deleteicon);
        mDeleteBitmap = BitmapProcessing.resizeBitmap(mDeleteBitmap, width / 12, width / 12);
        mDeleteWidth = (float) mDeleteBitmap.getWidth();
        mDeleteHeight = (float) mDeleteBitmap.getHeight();

        mFlipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_flipicon);
        mFlipBitmap = BitmapProcessing.resizeBitmap(mFlipBitmap, width / 12, width / 12);
        mFlipWidth = (float) mFlipBitmap.getWidth();
        mFlipHeight = (float) mFlipBitmap.getHeight();

        mEditBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_editicon);
        mEditBitmap = BitmapProcessing.resizeBitmap(mEditBitmap, width / 12, width / 12);
        mEditWidth = (float) mEditBitmap.getWidth();
        mEditHeight = (float) mEditBitmap.getHeight();
    }

    public void setWaterMark(@NonNull Bitmap bitmap, boolean isReset) {
        mBitmap = bitmap;
        mStickerScaleSize = 1.0f;
        setFocusable(true);
        float px = mBitmap.getWidth();
        float py = mBitmap.getHeight();

        if (isReset) {
            try {

                mOriginPoints = new float[]{0.0f, 0.0f, px, 0.0f, px, py, 0.0f, py, ((float) mBitmap.getWidth()) / 2.0f, ((float) mBitmap.getHeight()) / 2.0f};
                mOriginContentRect = new RectF(0.0f, 0.0f, px, py);
                mPoints = new float[10];
                mContentRect = new RectF();

                mMatrix = new Matrix();
                mMatrix.postTranslate((((float) DisplayUtil.getDisplayWidthPixels(getContext())) - ((float) mBitmap.getWidth())) / 2.0f, (((float) DisplayUtil.getDisplayWidthPixels(getContext())) - ((float) mBitmap.getHeight())) / 2.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mOriginPoints = new float[]{0.0f, 0.0f, px, 0.0f, px, py, 0.0f, py, ((float) mBitmap.getWidth()) / 2.0f, ((float) mBitmap.getHeight()) / 2.0f};
            mOriginContentRect = new RectF(0.0f, 0.0f, px, py);
        }
        postInvalidate();
    }

    public Matrix getMarkMatrix() {
        return mMatrix;
    }

    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null && mMatrix != null) {
            mMatrix.mapPoints(mPoints, mOriginPoints);
            mMatrix.mapRect(mContentRect, mOriginContentRect);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);
            if (mDrawController && isFocusable()) {
                canvas.drawLine(mPoints[0], mPoints[1], mPoints[2], mPoints[3], mBorderPaint);
                canvas.drawLine(mPoints[2], mPoints[3], mPoints[4], mPoints[5], mBorderPaint);
                canvas.drawLine(mPoints[4], mPoints[5], mPoints[6], mPoints[7], mBorderPaint);
                canvas.drawLine(mPoints[6], mPoints[7], mPoints[0], mPoints[1], mBorderPaint);
                canvas.drawBitmap(mControllerBitmap, mPoints[4] - (mControllerWidth / 2.0f), mPoints[5] - (mControllerHeight / 2.0f), mBorderPaint);
                canvas.drawBitmap(mDeleteBitmap, mPoints[0] - (mDeleteWidth / 2.0f), mPoints[1] - (mDeleteHeight / 2.0f), mBorderPaint);
                canvas.drawBitmap(mFlipBitmap, mPoints[2] - (mFlipWidth / 2.0f), mPoints[3] - (mFlipHeight / 2.0f), mBorderPaint);
                if (isDrawedit()) {
                    canvas.drawBitmap(mEditBitmap, mPoints[6] - (mEditWidth / 2.0f), mPoints[7] - (mEditHeight / 2.0f), mBorderPaint);
                }
            }
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawController = false;
        draw(canvas);
        mDrawController = true;
        canvas.save();
        return bitmap;
    }

    public void setShowDrawController(boolean show) {
        mDrawController = show;
    }

    public boolean isInController(float x, float y) {
        float rx = mPoints[4];
        float ry = mPoints[5];
        return new RectF(rx - (mControllerWidth / 2.0f), ry - (mControllerHeight / 2.0f), (mControllerWidth / 2.0f) + rx, (mControllerHeight / 2.0f) + ry).contains(x, y);
    }

    public boolean isInDelete(float x, float y) {
        float rx = mPoints[0];
        float ry = mPoints[1];
        return new RectF(rx - mDeleteWidth, ry - mDeleteHeight, mDeleteWidth + rx, mDeleteHeight + ry).contains(x, y);
    }

    public boolean isInFlip(float x, float y) {
        float rx = mPoints[2];
        float ry = mPoints[3];
        return new RectF(rx - (mFlipWidth / 2.0f), ry - (mFlipHeight / 2.0f), (mFlipWidth / 2.0f) + rx, (mFlipHeight / 2.0f) + ry).contains(x, y);
    }

    public boolean isInEdit(float x, float y) {
        if (isDrawedit()) {
            float rx = mPoints[6];
            float ry = mPoints[7];
            return new RectF(rx - (mEditWidth / 2.0f), ry - (mEditHeight / 2.0f), (mEditWidth / 2.0f) + rx, (mEditHeight / 2.0f) + ry).contains(x, y);
        }
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!isFocusable()) {
            return super.dispatchTouchEvent(event);
        }
        if (mViewRect == null) {
            mViewRect = new RectF(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight());
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "dispatchTouchEvent: DOWN" );
                if (isInController(x, y)) {
                    mInController = true;
                    mLastPointY = y;
                    mLastPointX = x;
                    break;
                }
                if (isInDelete(x, y)) {
                    mInDelete = true;
                    break;
                }
                if(isInFlip(x,y)){
                    mInFlip = true;
                    break;
                }
                if (mContentRect.contains(x, y)) {
                    mLastPointY = y;
                    mLastPointX = x;
                    mInMove = true;
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "dispatchTouchEvent: UP" );
                if (isInDelete(x, y) && mInDelete) {
                    doDeleteSticker();
                    break;
                }
                if(isInFlip(x,y)&& mInFlip){
                    doFlipSticker();
                    break;
                }
                mInMove=true;
                mInController=false;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG, "dispatchTouchEvent: Cancel" );
                mLastPointX = 0;
                mLastPointY = 0;
                mInController = false;
                mInMove = false;
                mInDelete = false;
                mInReversalHorizontal = false;
                mInReversalVertical = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "dispatchTouchEvent: Move" );
                if (mInController) {
                    Log.d(TAG, "dispatchTouchEvent: move controler" );
                    mMatrix.postRotate(rotation(event), mPoints[8], mPoints[9]);
                    float nowLenght = caculateLength(mPoints[0], mPoints[1]);
                    float touchLenght = caculateLength(event.getX(), event.getY());
                    if ((float)Math.sqrt((nowLenght - touchLenght) * (nowLenght - touchLenght)) > 0.0f) {
                        float scale = touchLenght / nowLenght;
                        float nowsc = mStickerScaleSize * scale;
                        if (nowsc >= MIN_SCALE_SIZE && nowsc <= MAX_SCALE_SIZE) {
                            mMatrix.postScale(scale, scale, mPoints[8], mPoints[9]);
                            mStickerScaleSize = nowsc;
                        }
                    }

                    invalidate();
                    mLastPointX = x;
                    mLastPointY = y;
                    break;

                }

                if (mInMove) { //拖动的操作
                    Log.d(TAG, "dispatchTouchEvent: move InMove" );
                    float cX = x - mLastPointX;
                    float cY = y - mLastPointY;
                    mInController = false;
                    //Log.i("MATRIX_OK", "ma_jiaodu:" + a(cX, cY));

                    if ((float)Math.sqrt(cX * cX + cY * cY) > 2.0f  && canStickerMove(cX, cY)) {
                        //Log.i("MATRIX_OK", "is true to move");
                        mMatrix.postTranslate(cX, cY);
                        postInvalidate();
                        mLastPointX = x;
                        mLastPointY = y;
                    }
                    break;
                }


                return true;

        }
        return true;
    }
    private void doDeleteSticker() {
        setVisibility(GONE);
        if (mOnStickerDeleteListener != null) {
            mOnStickerDeleteListener.onDelete(this);
        }
        ((ViewGroup) getParent()).removeView(this);
    }

    private void doFlipSticker() {
        mBitmap = BitmapProcessing.flip(mBitmap, true, false);
        postInvalidate();
    }

    private void doEditSticker() {
    }

    public void changeColor(int color) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
        new Canvas(mBitmap).drawBitmap(mBitmap, 0.0f, 0.0f, paint);
        postInvalidate();
    }

    private boolean canStickerMove(float cx, float cy) {
        return mViewRect.contains(cx + mPoints[8], cy + mPoints[9]);
    }

    private float caculateLength(float x, float y) {
        float ex = x - mPoints[8];
        float ey = y - mPoints[9];
        return (float) Math.sqrt((double) ((ex * ex) + (ey * ey)));
    }

    private float rotation(MotionEvent event) {
        return calculateDegree(event.getX(), event.getY()) - calculateDegree(mLastPointX, mLastPointY);
    }

    private float calculateDegree(float x, float y) {
        return (float) Math.toDegrees(Math.atan2((double) (y - mPoints[9]), (double) (x - mPoints[8])));
    }

    public RectF getContentRect() {
        return mContentRect;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        text = text;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        color = color;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        align = align;
    }

    public int getCircle() {
        return circle;
    }

    public void setCircle(int circle) {
        circle = circle;
    }

    public boolean isDrawedit() {
        return drawedit;
    }

    public void setDrawedit(boolean drawedit) {
       this.drawedit = drawedit;
    }

    public void setOnStickerDeleteListener(OnStickerDeleteListener listener) {
        mOnStickerDeleteListener = listener;
    }
}
