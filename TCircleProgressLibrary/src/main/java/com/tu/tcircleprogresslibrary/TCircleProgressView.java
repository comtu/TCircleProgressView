package com.tu.tcircleprogresslibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆型进度条,仿支付宝人脸识别进度bar
 *
 * @author comtu
 * @version 1.0
 * @date 2018/7/31  16:13
 */
public class TCircleProgressView extends View {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    //底色圆弧与进度圆弧
    /**** 圆弧的宽度*/
    private float mBorderWidth = 18f;
    /**** 圆弧的宽度的一半*/
    private float mBorderWidthHalf = 0f;

    /**** 开始绘制圆弧的角度*/
    private float mStartAngle = 100;
    /**** 终点对应的角度和起始点对应的角度的夹角*/
    private float mAngleLength = 340;
    /*** 渐变旋转角度 */
    private float mRotateGradualAngle = 90f;
    /**** 所要绘制的当前进度的圆弧终点到起点的夹角*/
    private float mCurrentAngleLength = 0;
    /**** 动画时长*/
    private long mAnimationDuration = 3000;
    /*** 背景颜色*/
    private int mBackgroundColor = Color.WHITE;
    /*** 圆弧背景颜色*/
    private int mArcBackgroundColor = Color.parseColor("#cccccc");

    /**** 总进度*/
    private float mTotalProgress = 100;
    /**** 圆弧最小宽度 / 直径*/
    private float mMinDiameter = -1;

    /**** 指定圆弧的外轮廓矩形区域 底色与进度 */
    private RectF mRectFCircle;

    /*** 渐变进度条颜色*/
    private int[] mGradualColors = {
            Color.parseColor("#d3effe"),
            Color.parseColor("#cdeafb"),
            Color.parseColor("#94d3fa"),
            Color.parseColor("#61b9f5"),
            Color.parseColor("#2ba2f9"),
            Color.parseColor("#0b8eec"),
            Color.parseColor("#0179cf"),
            Color.parseColor("#0060a2")
    };

    //背景覆盖区域
    /**** 背景矩形区域*/
    private RectF mRectBg;

    //半透明半圆
    /*** 是否显示半透明半圆以及文字 */
    private boolean mIsShowHint = true;
    /**** 指定半圆弧的外轮廓矩形区域*/
    private RectF mHintRectFSemicircle;
    /*** 半透明半圆覆盖比例*/
    private float mHintSemicircleRate = 0.3f;
    /*** 半透明半圆背景颜色*/
    private int mHintBackgroundColor = Color.parseColor("#55000000");
    /*** 字体颜色 */
    private int mHintTextColor = Color.WHITE;
    /*** 字体大小 */
    private float mHintTextSize = -1;
    /*** 文字 */
    private String mHintText = "";

    //全透明圆
    /*** 透明区域圆x轴位置 */
    private float mTransparentCircleCX;
    /*** 透明区域圆y轴位置 */
    private float mTransparentCircleCY;
    /*** 透明区域圆半径 */
    private float mTransparentCircleRadius;

    // ===========================================================
    // Constructors
    // ===========================================================

    public TCircleProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public TCircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TCircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================


    /**
     * 设置圆弧宽度
     *
     * @param borderWidth 宽
     */
    public void setBorderWidth(float borderWidth) {
        this.mBorderWidth = borderWidth;
    }

    /**
     * 设置圆弧起始的角度位置以及空白区域角度
     *
     * @param startAngle 起始位置的角度 左上右下 分别角度表示180 / 270 / 360 / 90
     * @param blankAngle 圆弧的空白区域夹角
     */
    public void setStartAngle(float startAngle, float blankAngle) {
        if (startAngle == 0 || startAngle > 360) {
            startAngle = 360;
        }
        this.mStartAngle = startAngle + blankAngle / 2;
        this.mAngleLength = 360 - blankAngle;
        this.mRotateGradualAngle = startAngle;
    }

    /**
     * 设置动画执行时间
     *
     * @param animationDuration 时间 毫秒
     */
    public void setAnimationDuration(long animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    /**
     * 设置总进度
     */
    public void setTotalProgress(float totalProgress) {
        this.mTotalProgress = totalProgress;
    }

    /**
     * 获取总进度
     *
     * @return 总进度
     */
    public float getTotalProgress() {
        return mTotalProgress;
    }

    /**
     * 设置进度渐变值
     *
     * @param mGradualColors 渐变颜色值数组
     */
    public void setGradualColors(int[] mGradualColors) {
        this.mGradualColors = mGradualColors;
    }

    /**
     * 设置背景色
     *
     * @param backgroundColor 颜色值
     */
    public void setBackgroundColor( int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
    }

    /**
     * 设置圆弧背景色
     *
     * @param arcBackgroundColor 颜色值
     */
    public void setArcBackgroundColor( int arcBackgroundColor) {
        this.mArcBackgroundColor = arcBackgroundColor;
    }

    /***
     * 设置半圆弧背景色
     * @param hintBackgroundColor 颜色值
     */
    public void setHintBackgroundColor(int hintBackgroundColor) {
        this.mHintBackgroundColor = hintBackgroundColor;
    }

    /**
     * 设置文字
     *
     * @param text 文字
     */
    public void setText(String text) {
        this.mHintText = text;
        invalidate();
    }

    /***
     * 设置字体大小
     * @param hintTextSize  字体大小 默认圆弧直径/30
     */
    public void setHintTextSize(float hintTextSize) {
        this.mHintTextSize = dipToPx(hintTextSize);
    }

    /**
     * 设置文字颜色
     *
     * @param mHintTextColor
     */
    public void setHintTextColor( int mHintTextColor) {
        this.mHintTextColor = mHintTextColor;
    }


    /**
     * 是否显示提示文字以及半透明圆
     *
     * @return
     */
    public void setIsShowHint(boolean isShowHint) {
        this.mIsShowHint = isShowHint;
    }

    /**
     * 设置半透明圆的覆盖比率
     *
     * @param mSemicircleRate 0.1F - 1F 默认
     */
    public void setSemicircleRate(float mSemicircleRate) {
        this.mHintSemicircleRate = mSemicircleRate;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initScale();

        drawbg(canvas);
        drawArcBase(canvas);
        drawArcProgress(canvas);
        if (mIsShowHint) {
            drawHint(canvas);
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================

    private void init(Context context, AttributeSet attrs) {
        int startAngle = 90;
        int blankAngle = 30;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TCircleProgressView);
            mBorderWidth = typedArray.getDimension(R.styleable.TCircleProgressView_tcpv_border_width, dipToPx(5f)); //圆弧的宽度
            startAngle = typedArray.getInt(R.styleable.TCircleProgressView_tcpv_start_angle, 90);// 圆弧起始角度
            blankAngle = typedArray.getInt(R.styleable.TCircleProgressView_tcpv_blank_angle, 30);// 圆弧空白角度
            mTotalProgress = typedArray.getInt(R.styleable.TCircleProgressView_tcpv_total_progress, 100);// 总进度
            mAnimationDuration = typedArray.getInt(R.styleable.TCircleProgressView_tcpv_animation_duration, 3) * 1000L;// 单位秒 动画持续时间
            mHintSemicircleRate = typedArray.getFloat(R.styleable.TCircleProgressView_tcpv_hint_semicircle_rate, 0.3f) ;// 半圆覆盖比率 0.1f - 1f

            mBackgroundColor = typedArray.getColor(R.styleable.TCircleProgressView_tcpv_background_color, Color.WHITE);//背景颜色
            mArcBackgroundColor = typedArray.getColor(R.styleable.TCircleProgressView_tcpv_arc_background_color, Color.parseColor("#cccccc"));//圆弧背景颜色
            mHintBackgroundColor = typedArray.getColor(R.styleable.TCircleProgressView_tcpv_hint_background_color, Color.parseColor("#55000000"));//半圆弧背景颜色
            mHintTextColor = typedArray.getColor(R.styleable.TCircleProgressView_tcpv_hint_text_color, Color.WHITE);//字体颜色

            int arcStartColor = typedArray.getColor(R.styleable.TCircleProgressView_tcpv_arc_start_color, 0);//进度圆弧开始颜色
            int arcEndColor = typedArray.getColor(R.styleable.TCircleProgressView_tcpv_arc_end_color, 0);//进度圆弧结束颜色
            if (arcStartColor != 0 && arcEndColor != 0) {
                mGradualColors = new int[]{arcStartColor, arcEndColor};
            } else if (arcStartColor != 0 || arcEndColor != 0) {
                int color = arcStartColor == 0 ? arcEndColor : arcStartColor;
                mGradualColors = new int[]{color, color};
            }
            mHintTextSize = typedArray.getDimension(R.styleable.TCircleProgressView_tcpv_hint_text_size, -1f);//字体大小
            mHintText = typedArray.getString(R.styleable.TCircleProgressView_tcpv_hint_text);//文字
            mIsShowHint = typedArray.getBoolean(R.styleable.TCircleProgressView_tcpv_hint_show, false);//是否显示hint

            typedArray.recycle();
        }

        mBorderWidthHalf = mBorderWidth / 2;
        //开启硬件离屏缓存 , 切勿在onDraw里调用.会导致onDraw死循环调用.        //- 1.解决xfermode黑色问题。        //- 2.效率比关闭硬件加速高3倍以上
        setLayerType(LAYER_TYPE_HARDWARE, null);
        setStartAngle(startAngle, blankAngle);
    }

    /**
     * 计算
     */
    private void initScale() {
        float height = getHeight();
        float width = getWidth();

        if (mMinDiameter == -1) {
            mMinDiameter = (Math.min(height, width));
        }
        if (mHintTextSize == -1) {
            mHintTextSize = dipToPx(mMinDiameter / 30);
        }
        if (mRectBg == null) {
            mRectBg = new RectF(0, 0, width, height);
        }

        float rectRB = mMinDiameter - mBorderWidthHalf;
        float mMinDiameterHalf = mMinDiameter / 2;
        float gapOffset = 10;//间隙偏移量

        if (height == width) {
            if (mRectFCircle == null) {
                mRectFCircle = new RectF(mBorderWidthHalf, mBorderWidthHalf + 0, rectRB, rectRB);
            }
            if (mHintRectFSemicircle == null) {
                mHintRectFSemicircle = new RectF(mRectFCircle.left + mBorderWidthHalf + gapOffset,
                        mRectFCircle.top + mBorderWidthHalf + gapOffset,
                        mRectFCircle.right - mBorderWidthHalf - gapOffset,
                        mRectFCircle.bottom - mBorderWidthHalf - gapOffset);
            }
            mTransparentCircleCX = mMinDiameterHalf;
            mTransparentCircleCY = mTransparentCircleCX;
            mTransparentCircleRadius = mTransparentCircleCX - mBorderWidth - gapOffset;
        } else if (mMinDiameter != height) { //宽
            float offset = (height - mMinDiameter) / 2;
            if (mRectFCircle == null) {
                mRectFCircle = new RectF(mBorderWidthHalf, mBorderWidthHalf + offset, rectRB, rectRB + offset);
            }
            if (mHintRectFSemicircle == null) {
                mHintRectFSemicircle = new RectF(mRectFCircle.left + mBorderWidthHalf + gapOffset,
                        mRectFCircle.top + mBorderWidthHalf + gapOffset,
                        mRectFCircle.right - mBorderWidthHalf - gapOffset,
                        mRectFCircle.bottom - mBorderWidthHalf - gapOffset);
            }
            mTransparentCircleCX = mMinDiameterHalf;
            mTransparentCircleCY = mMinDiameterHalf + offset;
            mTransparentCircleRadius = mMinDiameterHalf - mBorderWidth - gapOffset;
        } else { //高
            float offset = (width - mMinDiameter) / 2;
            if (mRectFCircle == null) {
                mRectFCircle = new RectF(mBorderWidthHalf + offset, mBorderWidthHalf + 0, rectRB + offset, rectRB);
            }
            if (mHintRectFSemicircle == null) {
                mHintRectFSemicircle = new RectF(mRectFCircle.left + mBorderWidthHalf + gapOffset,
                        mRectFCircle.top + mBorderWidthHalf + gapOffset,
                        mRectFCircle.right - mBorderWidthHalf - gapOffset,
                        mRectFCircle.bottom - mBorderWidthHalf - gapOffset);
            }
            mTransparentCircleCX = mMinDiameterHalf + offset;
            mTransparentCircleCY = mMinDiameterHalf;
            mTransparentCircleRadius = mMinDiameterHalf - mBorderWidth - gapOffset;
        }
    }


    /**
     * 半圆背景
     *
     * @param canvas
     */
    private void drawHint(Canvas canvas) {
        //半圆背景
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mHintBackgroundColor);

        float sweepAngle = 180 * mHintSemicircleRate;
        float startAngle = 180 + 90 - sweepAngle;
        canvas.drawArc(mHintRectFSemicircle, startAngle, sweepAngle * 2, false, paint);

        //提示文字
        mHintText = mHintText == null ? "" : mHintText;
        Paint vTextPaint = new Paint();
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setTextSize(mHintTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        vTextPaint.setTypeface(font);//字体风格
        vTextPaint.setColor(mHintTextColor);
        Rect bounds_Number = new Rect();
        vTextPaint.getTextBounds(mHintText, 0, mHintText.length(), bounds_Number);
        canvas.drawText(mHintText, mTransparentCircleCX, (mHintRectFSemicircle.bottom * 0.05f) + bounds_Number.height() + mHintRectFSemicircle.top, vTextPaint);
    }

    /**
     * 画背景
     */
    private void drawbg(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);        //去锯齿
        paint.setColor(mBackgroundColor);        //设置颜色
        canvas.drawRect(mRectBg, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        canvas.drawCircle(mTransparentCircleCX, mTransparentCircleCY, mTransparentCircleRadius, paint);
        paint.setXfermode(null);
    }

    /**
     * 绘制底色圆弧
     *
     * @param canvas 画笔
     */
    private void drawArcBase(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mArcBackgroundColor);
        paint.setStrokeJoin(Paint.Join.ROUND);// 结合处为圆弧
        paint.setStrokeCap(Paint.Cap.ROUND);//设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paint.setStyle(Paint.Style.STROKE);//设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE  ：填充内部和描边;  Paint.Style.STROKE  ：仅描边
        paint.setAntiAlias(true);//抗锯齿功能
        paint.setStrokeWidth(mBorderWidth);//设置画笔宽度

        /*绘制圆弧的方法
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，圆弧起始角度，单位为度。
         参数三圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
         参数四是如果这是true(真)的话,在绘制圆弧时将圆心包括在内，通常用来绘制扇形；如果它是false(假)这将是一个弧线,
         参数五是Paint对象；
         */
        canvas.drawArc(mRectFCircle, mStartAngle, mAngleLength, false, paint);

    }

    /**
     * 绘制当前进度圆弧
     *
     * @param canvas
     */
    private void drawArcProgress(Canvas canvas) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStrokeJoin(Paint.Join.ROUND);
        if (mAngleLength != 360) {
            paintCurrent.setStrokeCap(Paint.Cap.ROUND);//圆角弧度
        }
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(mBorderWidth);//设置画笔宽度
        //简单渐变色版本
        //SweepGradient lg = new SweepGradient(canvas.getHeight() / 2, canvas.getHeight() / 2, Color.parseColor("#d3effe"), Color.parseColor("#0060a2"));
        //丰富渐变色版本
        SweepGradient lg = new SweepGradient(mTransparentCircleCX, mTransparentCircleCY, mGradualColors, null);
        //旋转渐变起始位置
        Matrix matrix = new Matrix();
        matrix.setRotate(mRotateGradualAngle, mTransparentCircleCX, mTransparentCircleCY);
        lg.setLocalMatrix(matrix);
        paintCurrent.setShader(lg);
        canvas.drawArc(mRectFCircle, mStartAngle, mCurrentAngleLength, false, paintCurrent);
    }

    /**
     * 设置当前进度,并显示动画
     *
     * @param startProgress 动画的起始位置
     * @param endProgress   当前进度/结束位置
     */
    public void setProgressByAnimation(float startProgress, float endProgress) {
        if (endProgress > mTotalProgress) {
            endProgress = mTotalProgress;
        }
        if (startProgress > mTotalProgress) {
            startProgress = mTotalProgress;
        }
        /*所走进度占用总进度数的百分比*/
        float scale = endProgress / mTotalProgress;
        /*换算成弧度最后要到达的角度的长度-->弧长*/
        float currentAngleLength = scale * mAngleLength;
        /*开始执行动画*/
        setAnimation(startProgress / mTotalProgress * mAngleLength, currentAngleLength);
    }

    /**
     * 设置当前进度
     *
     * @param progress 当前进度值
     */
    public void setProgress(float progress) {
        /*所走进度占用总进度数的百分比*/
        float scale = progress / mTotalProgress;
        /*换算成弧度最后要到达的角度的长度-->弧长*/
        mCurrentAngleLength = scale * mAngleLength;
        invalidate();
    }


    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(mAnimationDuration);
        progressAnimator.setTarget(mCurrentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * dip 转换成px
     *
     * @param dip dp
     * @return px
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================


}
