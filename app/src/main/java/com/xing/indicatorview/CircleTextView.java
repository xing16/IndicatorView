package com.xing.indicatorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/6/11.
 */

public class CircleTextView extends View {

    private float mViewWidth;

    private int mRadius;

    private Paint mPaint;

    private Paint textPaint;

    private Paint linePaint;


    public CircleTextView(Context context) {
        super(context);
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.WHITE);


        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w > h ? h / 2 : w / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mRadius, mRadius);
        // 绘制圆形
        canvas.drawCircle(0, 0, mRadius, mPaint);
        // 绘制坐标轴
        canvas.drawLine(-mRadius, 0, mRadius, 0, linePaint);
        canvas.drawLine(0, -mRadius, 0, mRadius, linePaint);

        // 绘制
        String text = "中Apple Java";
        float textWidth = textPaint.measureText(text);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float ascent = fontMetrics.ascent;
        Log.i("debug", "ascent = " + ascent);   // ascent = -46.38672
        float descent = fontMetrics.descent;
        Log.i("debug", "descent = " + descent);  //descent = 12.207031
        float top = fontMetrics.top;
        Log.i("debug", "top = " + top);   //  top = -54.63867
        float bottom = fontMetrics.bottom;
        Log.i("debug", "bottom = " + bottom);   // bottom = 13.549805
        // 绘制top
        linePaint.setColor(Color.YELLOW);
        canvas.drawLine(-mRadius, top, mRadius, top, linePaint);

        // 绘制ascent
        linePaint.setColor(Color.GREEN);
        canvas.drawLine(-mRadius, ascent, mRadius, ascent, linePaint);

        // 绘制descent
        linePaint.setColor(Color.BLUE);
        canvas.drawLine(-mRadius, descent, mRadius, descent, linePaint);

        // 绘制bottom
        linePaint.setColor(Color.BLACK);
//        canvas.drawLine(-mRadius, bottom, mRadius, bottom, linePaint);

        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        canvas.drawText(text, 0, 0, textPaint);  // x = 0 与mpaint.settextalign(TextAlign.CENTER)有关

        canvas.restore();


    }
}
