package com.xing.indicatorviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/6/10.
 */

public class IndicatorView extends View {

    private final int DEFAULT_STYLE = 0;

    private final float DEFAULT_INDICATOR_GAP = dp2Px(10);

    private final float DEFAULT_INDICATOR_RADIUS = dp2Px(4);

    private final int DEFAULT_DOT_SELECTED_COLOR = 0xffffffff;

    private final int DEFAULT_DOT_UNSELECTED_COLOR = 0xff686868;

    private final float DEFAULT_STROKE_WIDTH = dp2Px(2);

    private final int DEFAULT_STROKE_COLOR = 0xffFF4081;

    private final int DEFAULT_SELECTED_FILL_COLOR = 0xffffffff;

    private final int DEFAULT_TEXT_COLOR = 0xffffffff;

    private final float DEFAULT_TEXT_SIZE = dp2Px(14);

    private float indicatorGap;

    private float indicatorRadius;

    private int dotSelectedColor;

    private int dotUnselectedColor;

    private Paint dotPaint;  // 绘制 dot 指示器画笔

    private Paint textPaint;   // 绘制数字，字母画笔

    private Paint strokePaint;  // 绘制边边框颜色，填充颜色相同

    private ViewPager mViewPager;

    private int mDotCount;  // 指示器个数

    private int mCurSelectedPosition;   // 当前选中的位置

    private float indicatorStrokeWidth;   // 边框线宽度

    private int indicatorSelectedFillColor;  // 选中填充颜色

    private int indicatorStrokeColor;  // 边框线颜色

    private int indicatorTextColor;    //  文本颜色

    private IndicatorStyleEnum indicatorStyle;

    private float indicatorTextSize;  // 指示器文本大小

    public IndicatorView(Context context) {
        this(context, null);

    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        int style = typedArray.getInt(R.styleable.IndicatorView_style, DEFAULT_STYLE);
        switch (style) {
            case 0:
                indicatorStyle = IndicatorStyleEnum.DOT;
                break;
            case 1:
                indicatorStyle = IndicatorStyleEnum.NUMBER;
                break;
            case 2:
                indicatorStyle = IndicatorStyleEnum.LETTER;
                break;
        }
        indicatorGap = typedArray.getDimension(R.styleable.IndicatorView_indicatorGap, DEFAULT_INDICATOR_GAP);
        indicatorRadius = typedArray.getDimension(R.styleable.IndicatorView_indicatorRadius, DEFAULT_INDICATOR_RADIUS);
        dotSelectedColor = typedArray.getColor(R.styleable.IndicatorView_dotSelectedColor, DEFAULT_DOT_SELECTED_COLOR);
        dotUnselectedColor = typedArray.getColor(R.styleable.IndicatorView_dotUnselectColor, DEFAULT_DOT_UNSELECTED_COLOR);
        indicatorStrokeWidth = typedArray.getDimension(R.styleable.IndicatorView_strokeWidth, DEFAULT_STROKE_WIDTH);
        indicatorStrokeColor = typedArray.getColor(R.styleable.IndicatorView_strokeColor, DEFAULT_STROKE_COLOR);
        indicatorSelectedFillColor = typedArray.getColor(R.styleable.IndicatorView_selectedFillColor, DEFAULT_SELECTED_FILL_COLOR);
        indicatorTextColor = typedArray.getColor(R.styleable.IndicatorView_textColor, DEFAULT_TEXT_COLOR);
        indicatorTextSize = typedArray.getDimension(R.styleable.IndicatorView_textSize, DEFAULT_TEXT_SIZE);
        typedArray.recycle();
        initPaint();
    }


    private void initPaint() {
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(indicatorTextColor);
        textPaint.setTextSize(indicatorTextSize);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(indicatorStrokeColor);
    }

    /**
     * 对外提供设置 ViewPager 接口
     */
    public void setupViewPager(ViewPager viewPager) {
        Log.i("debug", "setupViewPager()");
        if (viewPager == null) {
            return;
        }
        mViewPager = viewPager;
        mDotCount = mViewPager.getAdapter().getCount();  // 指示器的个数和与之关联的 ViewPager 页数相同
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
                mCurSelectedPosition = position;
                invalidate();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 测量控件宽高
     * 宽：leftPadding + dot 总宽度 + gap间距总宽度 + rightPadding
     * 高：topPadding + dot 高度 + bottomPadding
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("debug", "onMeasure()");
        int width = (int) (getPaddingLeft() + mDotCount * 2 * indicatorRadius + (mDotCount - 1) * indicatorGap + getPaddingRight());
        int height = (int) (getPaddingTop() + indicatorRadius * 2 + getPaddingBottom());
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("debug", "onDraw()");
        if (indicatorStyle == IndicatorStyleEnum.DOT) {   // 绘制默认圆点
            drawDotIndicator(canvas);
        } else {
            drawNumberLetterIndicator(canvas);    // 绘制数字，字母
        }
    }

    /**
     * 绘制带字母的指示器
     *
     * @param canvas
     */
    private void drawLetterIndicator(Canvas canvas) {

    }

    /**
     * 绘制带数字的指示器
     *
     * @param canvas
     */
    private void drawNumberLetterIndicator(Canvas canvas) {
        for (int i = 0; i < mDotCount; i++) {
            float centerX = getPaddingLeft() + (2 * i + 1) * indicatorRadius + i * indicatorGap;
            float centerY = getPaddingTop() + indicatorRadius;
            if (mCurSelectedPosition == i) {
                textPaint.setColor(Color.WHITE);
                strokePaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, indicatorRadius, strokePaint);
            } else {
                textPaint.setColor(indicatorStrokeColor);
                strokePaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(centerX, centerY, indicatorRadius, strokePaint);
            }
            String text = "";
            if (indicatorStyle == IndicatorStyleEnum.NUMBER) {
                text = i + 1 + "";
            } else if (indicatorStyle == IndicatorStyleEnum.LETTER) {
                text = (char) ('A' + i) + "";
            }
            float textWidth = textPaint.measureText(text);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textHeight = fontMetrics.descent - fontMetrics.ascent;
            float baseline = getHeight() / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
            canvas.drawText(text, centerX - textWidth / 2, baseline, textPaint);
        }
    }

    /**
     * 绘制圆点指示器
     *
     * @param canvas
     */
    private void drawDotIndicator(Canvas canvas) {
        for (int i = 0; i < mDotCount; i++) {
            float centerX = getPaddingLeft() + (2 * i + 1) * indicatorRadius + i * indicatorGap;
            float centerY = getPaddingTop() + indicatorRadius;
            if (i == mCurSelectedPosition) {
                dotPaint.setColor(dotSelectedColor);
            } else {
                dotPaint.setColor(dotUnselectedColor);
            }
            canvas.drawCircle(centerX, centerY, indicatorRadius, dotPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float downX = 0;
        float downY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                handleClick(downX, downY);
                break;

        }
        return super.onTouchEvent(event);
    }


    private void handleClick(float downX, float downY) {
        for (int i = 0; i < mDotCount; i++) {
            float centerX = getPaddingLeft() + (2 * i + 1) * indicatorRadius + i * indicatorGap;
            float centerY = getPaddingTop() + indicatorRadius;
            double distance = Math.sqrt((downX - centerX) * (downX - centerX) + (downY - centerY) * (downY - centerY));
            if (distance < indicatorRadius) {
                mCurSelectedPosition = i;
                Log.i("debug", "i = " + i);
                mViewPager.setCurrentItem(i, false);
                invalidate();
            }
        }
    }


    private float dp2Px(int dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }


    interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }


    enum IndicatorStyleEnum {
        DOT, NUMBER, LETTER
    }


}
