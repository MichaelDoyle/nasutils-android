/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.md.nasutils.util.ScreenUtils;

/**
 * Draws a pie chart for displaying percentage full.
 * 
 * @author michaeldoyle
 */
public class PieChartView extends View {

    private Paint mPiePaint;
    private Paint mShadowPaint;
    private RectF mPieBounds = new RectF();
    private RectF mShadowBounds = new RectF();
    private float mPercentage = 0.0f;

    public PieChartView(Context c) {
        this(c, 0.0f);
    }

    public PieChartView(Context c, float percentage) {
        super(c);
        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        
        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(Color.GRAY);
        
        mPercentage = percentage;
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

        canvas.drawOval(mShadowBounds, mShadowPaint);

        mPiePaint.setColor(Color.parseColor("#0000FF"));
        canvas.drawArc(mPieBounds, 0, 360 * mPercentage, true, mPiePaint);

        mPiePaint.setColor(Color.parseColor("#910DFF"));
        canvas.drawArc(mPieBounds, 360 * mPercentage,
                360 - (360 * mPercentage), true, mPiePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        int w = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int h = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int side =  w > h ? h : w;
     
        setMeasuredDimension(side + getPaddingLeft() + getPaddingRight(), 
                side + getPaddingTop() + getPaddingBottom());
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        float diameter = Math.min(ww, hh);

        int shaddowOffset = ScreenUtils.dipToPx(getContext(), 3);

        mPieBounds = new RectF(0.0f, 0.0f, 
                diameter - shaddowOffset, 
                diameter - shaddowOffset);

        mShadowBounds = new RectF(mPieBounds.left + shaddowOffset,
                mPieBounds.top + shaddowOffset, 
                mPieBounds.right + shaddowOffset, 
                mPieBounds.bottom + shaddowOffset);

        invalidate();
    }
}
