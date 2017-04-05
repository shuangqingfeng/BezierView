package com.shuang.bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author feng
 * @Description: 贝塞尔曲线的使用
 * @date 2017/4/1
 */
public class BezierView extends View {

    private static final String TAG = BezierView.class.getSimpleName();
    private Paint mPaint;
    private Bitmap mBackgroudBitmap;
    private int mLeft;
    private int mBitmapTop;
    private int mHeightCenter;
    private Paint mPointPaint;
    private int mWitdhCenter;
    private int mBitmapWidthBkgCenter;
    private Path mPath;
    private int mBitmapHeightBkgCenter;
    private Paint mPathPaint;
    private boolean hasShow;
    private Bitmap mBitmap;
    private int mBitmapWithCenter;
    private int mBitmapHeightCenter;
    private int finalHeight;
    private int moveOffset;
    private int bitmapMoveTop = 0;
    private boolean canDraw = true;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmap();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.GRAY);
        mPath = new Path();
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setStrokeWidth(20);
        mPathPaint.setColor(Color.YELLOW);
    }

    private void initBitmap() {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.inSampleSize = 2;
        opt.inJustDecodeBounds = false;
        mBackgroudBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.t, opt);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m, opt);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        //绘制背景
        canvas.drawBitmap(mBackgroudBitmap, mLeft, mBitmapTop, mPaint);
        if (canDraw) {
            if (hasShow) {
                canvas.drawBitmap(mBitmap, mWitdhCenter - mBitmapWithCenter
                        , mHeightCenter - mBitmap.getHeight() + bitmapMoveTop, mPaint);
            }
            //绘制曲线
            mPath.moveTo(mLeft, mHeightCenter);
            mPath.quadTo(mWitdhCenter, moveOffset
                    , mBitmapWidthBkgCenter + mWitdhCenter, mHeightCenter);
            canvas.drawPath(mPath, mPathPaint);
        } else {
            mPath.moveTo(mLeft, mHeightCenter);
            mPath.quadTo(mWitdhCenter, moveOffset
                    , mBitmapWidthBkgCenter + mWitdhCenter, mHeightCenter);
            canvas.drawPath(mPath, mPathPaint);
            if (moveOffset >= mHeightCenter) {
                canvas.drawBitmap(mBitmap, mWitdhCenter - mBitmapWithCenter
                        , mHeightCenter - mBitmap.getHeight() + bitmapMoveTop, mPaint);
                moveOffset -= 5;
                bitmapMoveTop -= 10;
                postInvalidateDelayed(10);
            } else {
                canDraw = true;
                bitmapMoveTop = 0;
            }
        }

        //绘制两边的小点
        canvas.drawCircle(mLeft, mHeightCenter, 20, mPointPaint);
        canvas.drawCircle(mBitmapWidthBkgCenter + mWitdhCenter, mHeightCenter, 20, mPointPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWitdhCenter = getWidth() / 2;
        mBitmapWidthBkgCenter = mBackgroudBitmap.getWidth() / 2;
        mLeft = mWitdhCenter - mBitmapWidthBkgCenter;
        mHeightCenter = getHeight() / 2;
        mBitmapHeightBkgCenter = mBackgroudBitmap.getHeight() / 2;
        mBitmapTop = mHeightCenter - mBitmapHeightBkgCenter;
        mBitmapWithCenter = mBitmap.getWidth() / 2;
        mBitmapHeightCenter = mBitmap.getHeight() / 2;
        finalHeight = mHeightCenter + mBitmapHeightBkgCenter * 2;
        moveOffset = mHeightCenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasShow = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();
                if (y > mHeightCenter) {
                    Log.d(TAG, "onTouchEvent: " + y);
                    if (moveOffset <= finalHeight) {
                        Log.d(TAG, "onTouchEvent:moveOffset " + moveOffset);
                        moveOffset += 10;
                        bitmapMoveTop += 5;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                canDraw = false;

        }
        invalidate();
        return true;
    }
}
