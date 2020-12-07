/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

import com.md.nasutils.util.ScreenUtils;

/**
 * Draws half circle meter for displaying temperature
 * 
 * @author michaeldoyle
 */
public class ArcMeterView extends View {

    private static final String TAG = ArcMeterView.class.getSimpleName();

    private static final int mTotalBars = 20;
    private static final float mDegreesPerBar = 180.0f / mTotalBars;

    private Paint mBoxPaint;
    private Paint mLabelPaint;
    private Paint mTextPaint;
    private RectF mBounds = new RectF();
    private float mPercentage = 0.0f;
    private String mLabel;
    private String mValue;

    public ArcMeterView(Context c) {
        this(c, 0.0f, null, null);
    }

    public ArcMeterView(Context c, float percentage, String label, String value) {
        super(c);
        
        mBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoxPaint.setStyle(Paint.Style.FILL);
        
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTypeface(Typeface.SANS_SERIF);
        
        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setStyle(Paint.Style.FILL);
        mLabelPaint.setColor(Color.LTGRAY);
        mLabelPaint.setTypeface(Typeface.SANS_SERIF);
        
        mPercentage = percentage;
        mLabel = label != null ? label : "";
        mValue = value != null ? value : "";
    }
    
    public float getPercentage() {
        return mPercentage;
    }

    public void setPercentage(float percentage) {
        this.mPercentage = percentage;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        float width = getWidth();

        mLabelPaint.setTextSize(width/8);
        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mLabel, width/2, width/4, mLabelPaint);
        
        mTextPaint.setTextSize(width/4);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mValue, width/2, (width/2) * 0.98f, mTextPaint);
        
        canvas.scale(width, width);
        canvas.save();
        
        float pct = 1.0f / mTotalBars;
        
        canvas.rotate(mDegreesPerBar / 2, 0.50f, 0.48f);
        
        for (int i = 0; i < mTotalBars; i++) {
            float curPct = pct * i;
            
            if (curPct <= 0.70f && curPct <= mPercentage) {
                mBoxPaint.setColor(Color.BLUE);
            } else if (curPct <= mPercentage) {
                mBoxPaint.setColor(Color.RED);
            } else {
                mBoxPaint.setColor(Color.LTGRAY);
            }

            canvas.drawRect(mBounds.left + 0.02f, 0.48f, 
                    mBounds.left + 0.10f, 0.52f, mBoxPaint);

            canvas.rotate(mDegreesPerBar, 0.50f, 0.50f);
        }
        canvas.restore();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = chooseDimension(widthMode, widthSize);
        
        setMeasuredDimension(width, width / 2);
    }
    
    private int chooseDimension(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else {
            return getPreferredSize();
        } 
    }
    
    private int getPreferredSize() {
        return ScreenUtils.dipToPx(getContext(), 100);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ww = (float) w - xpad;
        
        mBounds = new RectF(0.0f, 0.0f, ww, ww / 2);

        invalidate();
    }
}
