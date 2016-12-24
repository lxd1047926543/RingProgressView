package com.example.richard.ringprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Richard on 2016/9/19.
 */
public class RingProgressView extends View {

    /**
     * 进度条阴影圆弧画笔
     * The paint draws the shadow.
     */
    private Paint mShadowPaint;
    /**
     * 进度条阴影圆弧范围矩形
     * The rectangle for the shadow.
     */
    private RectF mShadowRectF;
    /**
     * 进度条阴影头部小圆圈
     * The paint draws the start cap of the shadow.
     */
    private Paint mShadowStartCapPaint;
    /**
     * 进度条阴影尾部小圆圈
     * The paint draws the end cap of the shadow.
     */
    private Paint mShadowEndCapPaint;
//    /**
//     * 进度条阴影圆弧线条粗细
//     */
//    private float mProgressArcStrokeWidth;
    /**
     * 进度改变监听，只有当需要动画时有效
     * The callback to be invoked where the animation is need and when the progress has changed.
     */
    private OnProgressChangeListener mOnProgressChangeListener;
    /**
     * 进度条圆弧的动画
     * The animator instructs the animation of the arc progress and shadow.
     */
    private ValueAnimator mArcProgressValueAnimator;
    /**
     * 顶层圆弧的范围矩形
     * The rectangle for the arc progress.
     */
    private RectF mArcProgressRectF;
    /**
     * 顶层圆弧的画笔
     * The paint draws the arc progress.
     */
    private Paint mArcProgressPaint;
    /**
     * 底层圆圈的画笔
     * The paint draws the base ring.
     */
    private Paint mBaseRingPaint;
    /**
     * 最终进度
     * The final progress.
     */
    private float mFinalProgress;
    /**
     * 用于动画的进度
     * The progress at a specific time.
     */
    private float mProgress;
    /**
     * 动画持续时间
     * The duration for the animation.
     */
    private long mDuration = 800;
    /**
     * 预设主题
     * The preset theme.
     */
    private PresetTheme mPresetTheme;

    /**
     * 默认的预设主题
     * The default preset theme.
     */
    private PresetTheme mDefaultPresetTheme = PresetTheme.THEME_RED;

    /**
     * 底层圆圈的颜色
     * The color of the base ring.
     */
    private int mBaseRingColor;
    /**
     * 圆弧进度条的颜色
     * The color of the arc progress.
     */
    private int mArcProgressColor;
    /**
     * 圆弧进度条渐变起始颜色
     * The color of the arc progress from which the gradient starts.
     */
    private int mArcProgressStartColor;
    /**
     * 圆弧进度条渐变终止颜色
     * The color of the arc progress with which the gradient ends.
     */
    private int mArcProgressEndColor;
    private int mShadowColor;
    /**
     * 底层圆圈半径
     * The radius of the base ring.
     */
    private float mBaseRingRadius;
    /**
     * 底层圆圈画笔宽度
     * The stroke width of the base ring.
     */
    private float mBaseRingWidth;
    /**
     * 阴影宽度
     * The width for the shadow.
     */
    private float mShadowWidth;
    /**
     * 阴影x轴偏移
     * The offset of the shadow in the x axis.
     */
    private float mShadowXOffset;
    /**
     * 阴影y轴偏移
     * The offset of the shadow in the y axis.
     */
    private float mShadowYOffset;
    /**
     * 阴影x轴偏移绝对值
     * The absolute value of the shadow's x offset.
     */
    private float mAbsShadowXOffset;
    /**
     * 阴影y轴偏移绝对值
     * The absolute value of the shadow's y offset.
     */
    private float mAbsShadowYOffset;
    /**
     * 圆弧进度条的样式，有纯色和渐变两种
     * The style of the arc progress, including the solid style and the gradient style.
     */
    private int mArcProgressStyle;
    /**
     * 进度条的绘制是否需要动画
     * The variable indicates whether to draw the arc progress by animation.
     */
    private boolean mNeedAnimation;

    /**
     * 默认的底层圆圈颜色
     * The default color of the base ring.
     */
    private final int mDefaultBaseRingColor = mDefaultPresetTheme.getBaseRingColor();
    /**
     * 默认的圆弧进度条颜色
     * The default color of the arc progress.
     */
    private final int mDefaultArcProgressColor = mDefaultPresetTheme.getArcProgressEndColor();
    /**
     * 默认的圆弧进度条起始颜色
     * The default color of the arc progress from which the gradient starts.
     */
    private final int mDefaultArcProgressStartColor = mDefaultPresetTheme.getArcProgressStartColor();
    /**
     * 默认的圆弧进度条终止颜色
     * The default color of the arc progress with which the gradient ends.
     */
    private final int mDefaultArcProgressEndColor = mDefaultPresetTheme.getArcProgressEndColor();
    /**
     * 默认的阴影颜色
     * The default color of the shadow.
     */
    private final int mDefaultShadowColor = mDefaultPresetTheme.getBaseRingColor();
    /**
     * 默认的底层圆圈半径
     * The default radius of the base ring.
     */
    private final float mDefaultBaseRingRadius = 0.0f;
    /**
     * 默认的底层圆圈画笔宽度
     * The default stroke width of the base ring.
     */
    private final float mDefaultBaseRingWidth = 180.0f;
    /**
     * 默认的阴影宽度
     * The default width for the shadow.
     */
    private final float mDefaultShadowWidth = 200.0f;
    /**
     * 默认的阴影x轴偏移
     * The default offset of the shadow in the x axis.
     */
    private final float mDefaultShadowXOffset = 20.0f;
    /**
     * 默认的阴影y轴偏移
     * The default offset of the shadow in the y axis.
     */
    private final float mDefaultShadowYOffset = 20.0f;
    /**
     * 默认的圆弧进度条样式
     * The default style of the arc progress.
     */
    private final int mDefaultArcProgressStyle = GRADIENT;
    /**
     * 默认的是否需要动画
     * The default value of the mNeedAnimation.
     */
    private final boolean mDefaultNeedAnimation = true;

    @IntDef({SOLID, GRADIENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArcProgressStyle {
    }

    /**
     * 纯色
     * solid
     */
    public static final int SOLID = 0;
    /**
     * 渐变
     * gradient
     */
    public static final int GRADIENT = 1;

    /**
     * 裁剪圆弧进度条头部和尾部小圆圈用的画笔
     * The paint that clips the start and end cap of the shadow.
     */
    private Paint mShadowPaint2;
    /**
     * 圆弧进度条头部小圆圈
     * The start cap of the arc progress.
     */
    private Paint mArcProgressStartCapPaint;
    /**
     * 圆弧进度条头部小圆圈的矩形
     * The rectangle for the start cap of the arc progress.
     */
    private RectF mArcProgressStartCapRectF;

    /**
     * 主题预设
     * The preset theme.
     */
    public enum PresetTheme {
        /**
         * 红色主题
         * The red theme.
         */
        THEME_RED(Color.parseColor("#FF8F9D"), Color.parseColor("#FF0007"), Color.parseColor("#C1272D")),
        /**
         * 蓝色主题
         * The blue theme.
         */
        THEME_BLUE(Color.parseColor("#50D2FF"), Color.parseColor("#29ABFF"), Color.parseColor("#0071BC"));

        /**
         * 底层圆环颜色
         * The color of the base ring in the preset theme.
         */
        private int mBaseRingColor;
        /**
         * 进度条起始颜色
         * The color of the arc progress from which the gradient starts in the preset theme.
         */
        private int mArcProgressStartColor;
        /**
         * 进度条终止颜色
         * The color of the arc progress with which the gradient ends in the preset theme.
         */
        private int mArcProgressEndColor;

        /**
         * 三参数构造器
         * Constructor with three parameters.
         *
         * @param baseRingColor         底层圆环颜色 The color of the base ring in the preset theme.
         * @param arcProgressStartColor 进度条起始颜色 The color of the arc progress from which the gradient starts in the preset theme.
         * @param arcProgressEndColor   进度条终止颜色 The color of the arc progress with which the gradient ends in the preset theme.
         */
        PresetTheme(int baseRingColor, int arcProgressStartColor, int arcProgressEndColor) {
            mBaseRingColor = baseRingColor;
            mArcProgressStartColor = arcProgressStartColor;
            mArcProgressEndColor = arcProgressEndColor;
        }

        /**
         * 获取底层圆环颜色
         * get the color of the base ring in the preset theme.
         *
         * @return 底层圆环颜色 The color of the base ring in the preset theme.
         */
        public int getBaseRingColor() {
            return mBaseRingColor;
        }

        /**
         * 获取进度条起始颜色
         * get the color of the arc progress from which the gradient starts in the preset theme.
         *
         * @return 进度条起始颜色 The color of the arc progress from which the gradient starts in the preset theme.
         */
        public int getArcProgressStartColor() {
            return mArcProgressStartColor;
        }

        /**
         * 获取进度条终止颜色
         * get the color of the arc progress with which the gradient ends in the preset theme.
         *
         * @return 进度条终止颜色 The color of the arc progress with which the gradient ends in the preset theme.
         */
        public int getArcProgressEndColor() {
            return mArcProgressEndColor;
        }
    }

    /**
     * 一参数构造器
     * Constructor with one parameter.
     *
     * @param context 上下文 context
     */
    public RingProgressView(Context context) {
        this(context, null);
    }

    /**
     * 两参数构造器
     * Constructor with two parameters.
     *
     * @param context 上下文 context
     * @param attrs   {@link AttributeSet}
     */
    public RingProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 三参数构造器
     * Constructor with three parameters.
     *
     * @param context      上下文 context
     * @param attrs        {@link AttributeSet}
     * @param defStyleAttr {@link View}
     */
    public RingProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     * Initialization.
     */
    private void init(Context context, AttributeSet attrs) {

        initAttributes(context, attrs);
        if (mNeedAnimation) {
            initAnimation();
        }
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                initPaint();
                mArcProgressRectF = new RectF(
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2),
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2),
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + 2 * mBaseRingRadius,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + 2 * mBaseRingRadius);
                mShadowRectF = new RectF(
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mShadowXOffset,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mShadowYOffset,
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + 2 * mBaseRingRadius + mShadowXOffset,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + 2 * mBaseRingRadius + mShadowYOffset);
                mArcProgressStartCapRectF = new RectF(
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius - mBaseRingWidth / 2,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) - mBaseRingWidth / 2,
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mBaseRingWidth / 2,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingWidth / 2);
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

    }

    /**
     * 初始化属性
     * Initialize the attributes.
     * @param context 上下文 context
     * @param attrs   {@link AttributeSet}
     */
    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ringprogressview);
        mBaseRingColor = typedArray.getColor(R.styleable.ringprogressview_base_ring_color, mDefaultBaseRingColor);
        mArcProgressColor = typedArray.getColor(R.styleable.ringprogressview_arc_progress_color, mDefaultArcProgressColor);
        mArcProgressStartColor = typedArray.getColor(R.styleable.ringprogressview_arc_progress_start_color, mDefaultArcProgressStartColor);
        mArcProgressEndColor = typedArray.getColor(R.styleable.ringprogressview_arc_progress_end_color, mDefaultArcProgressEndColor);
        mShadowColor = typedArray.getColor(R.styleable.ringprogressview_shadow_color, mDefaultShadowColor);
        mBaseRingRadius = typedArray.getDimension(R.styleable.ringprogressview_base_ring_radius, mDefaultBaseRingRadius);
        if (mBaseRingRadius < 0.0f) {
            throw new IllegalArgumentException("Base ring radius cannot be < 0.");
        }
        mBaseRingWidth = typedArray.getDimension(R.styleable.ringprogressview_base_ring_width, mDefaultBaseRingWidth);
        if (mBaseRingWidth < 0.0f) {
            throw new IllegalArgumentException("Base ring width cannot be < 0.");
        }
        mShadowWidth = typedArray.getDimension(R.styleable.ringprogressview_shadow_width, mDefaultShadowWidth);
        if (mShadowWidth < mBaseRingWidth) {
            throw new IllegalArgumentException("Shadow width cannot be < base ring width");
        }
        mShadowXOffset = typedArray.getDimension(R.styleable.ringprogressview_shadow_x_offset, mDefaultShadowXOffset);
        mShadowYOffset = typedArray.getDimension(R.styleable.ringprogressview_shadow_y_offset, mDefaultShadowYOffset);

        mAbsShadowXOffset = Math.abs(mShadowXOffset);
        mAbsShadowYOffset = Math.abs(mShadowYOffset);

        mArcProgressStyle = typedArray.getInt(R.styleable.ringprogressview_arc_progress_style, mDefaultArcProgressStyle);
        mNeedAnimation = typedArray.getBoolean(R.styleable.ringprogressview_need_animation, mDefaultNeedAnimation);
        typedArray.recycle();
    }

    /**
     * 初始化动画
     * Initialize the animation.
     */
    private void initAnimation() {
        mArcProgressValueAnimator = new ValueAnimator();
        mArcProgressValueAnimator.setInterpolator(new DecelerateInterpolator());
        mArcProgressValueAnimator.setDuration(mDuration);
        mArcProgressValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((float) animation.getAnimatedValue());
                if (mOnProgressChangeListener != null) {
                    mOnProgressChangeListener.onProgressChange((float) animation.getAnimatedValue());
                }
            }
        });
    }

    /**
     * 初始化画笔
     * Initialize the paints.
     */
    private void initPaint() {
        mArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressPaint.setStyle(Paint.Style.STROKE);
        mArcProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcProgressPaint.setDither(true);

        mArcProgressStartCapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressStartCapPaint.setStyle(Paint.Style.FILL);
        mArcProgressStartCapPaint.setDither(true);

        mBaseRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBaseRingPaint.setStyle(Paint.Style.STROKE);
        mBaseRingPaint.setDither(true);

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStrokeCap(Paint.Cap.BUTT);
        mShadowPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setDither(true);

        mShadowStartCapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowStartCapPaint.setDither(true);
        mShadowEndCapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowEndCapPaint.setDither(true);

        mArcProgressPaint.setStrokeWidth(mBaseRingWidth);
        mBaseRingPaint.setStrokeWidth(mBaseRingWidth);
        mShadowPaint.setStrokeWidth(mShadowWidth);

        mShadowPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint2.setStrokeCap(Paint.Cap.BUTT);
        mShadowPaint2.setStyle(Paint.Style.STROKE);
        mShadowPaint2.setDither(true);
        mShadowPaint2.setStrokeWidth(mShadowWidth);
        mShadowPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        if (mPresetTheme != null) {
            mBaseRingPaint.setColor(mPresetTheme.getBaseRingColor());
            SweepGradient sweepGradient = new SweepGradient(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                    mPresetTheme.getArcProgressEndColor(),
                    mPresetTheme.getArcProgressStartColor());
            Matrix matrix = new Matrix();
            sweepGradient.getLocalMatrix(matrix);
            matrix.setRotate(-90.0f, Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius);
            sweepGradient.setLocalMatrix(matrix);
            mArcProgressPaint.setShader(sweepGradient);
            mArcProgressStartCapPaint.setColor(mPresetTheme.getArcProgressStartColor());

            mShadowPaint.setShader(new RadialGradient(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowXOffset,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowYOffset,
                    mBaseRingRadius + mShadowWidth / 2,
                    new int[]{mPresetTheme.getBaseRingColor() & 0x00FFFFFF, mPresetTheme.getBaseRingColor(), mPresetTheme.getBaseRingColor() & 0x00FFFFFF},
                    new float[]{(mBaseRingRadius - mShadowWidth / 2) / (mBaseRingRadius + mShadowWidth / 2), mBaseRingRadius / (mBaseRingRadius + mShadowWidth / 2), 1},
                    Shader.TileMode.MIRROR));
            mShadowStartCapPaint.setShader(new RadialGradient(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowXOffset,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mShadowYOffset,
                    mShadowWidth / 2,
                    mPresetTheme.getBaseRingColor(),
                    mPresetTheme.getBaseRingColor() & 0x00FFFFFF,
                    Shader.TileMode.MIRROR));
            mShadowEndCapPaint.setShader(new RadialGradient(
                    0,
                    -mBaseRingRadius,
                    mShadowWidth / 2,
                    mPresetTheme.getBaseRingColor(),
                    mPresetTheme.getBaseRingColor() & 0x00FFFFFF,
                    Shader.TileMode.MIRROR));
        } else {
            mBaseRingPaint.setColor(mBaseRingColor);
            mShadowPaint.setShader(new RadialGradient(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowXOffset,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowYOffset,
                    mBaseRingRadius + mShadowWidth / 2,
                    new int[]{mBaseRingColor & 0x00FFFFFF, mBaseRingColor, mBaseRingColor & 0x00FFFFFF},
                    new float[]{(mBaseRingRadius - mShadowWidth / 2) / (mBaseRingRadius + mShadowWidth / 2), mBaseRingRadius / (mBaseRingRadius + mShadowWidth / 2), 1},
                    Shader.TileMode.MIRROR));
            mShadowStartCapPaint.setShader(new RadialGradient(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowXOffset,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mShadowYOffset,
                    mShadowWidth / 2,
                    mBaseRingColor,
                    mBaseRingColor & 0x00FFFFFF,
                    Shader.TileMode.MIRROR));
            mShadowEndCapPaint.setShader(new RadialGradient(
                    0,
                    -mBaseRingRadius,
                    mShadowWidth / 2,
                    mBaseRingColor,
                    mBaseRingColor & 0x00FFFFFF,
                    Shader.TileMode.MIRROR));
            if (mArcProgressStyle == GRADIENT) {
                SweepGradient sweepGradient = new SweepGradient(
                        Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                        mArcProgressEndColor,
                        mArcProgressStartColor);
                Matrix matrix = new Matrix();
                sweepGradient.getLocalMatrix(matrix);
                matrix.setRotate(-90.0f, Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                        Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius);
                sweepGradient.setLocalMatrix(matrix);
                mArcProgressPaint.setShader(sweepGradient);
                mArcProgressStartCapPaint.setColor(mArcProgressStartColor);
            } else {
                mArcProgressPaint.setColor(mArcProgressColor);
                mArcProgressStartCapPaint.setColor(mArcProgressColor);
            }

        }
    }


    /**
     * 用于进度条动画，设置进度
     * Set the progress.
     *
     * @param progress 动画中的进度条进度 The progress at a specific time.
     */
    private void setProgress(float progress) {
        mProgress = progress;
        postInvalidate();
    }

    /**
     * 设置进度条改变的监听
     * Set the callback to be invoked where the animation is need and when the progress has changed.
     *
     * @param OnProgressChangeListener 进度条改变监听 The callback.
     */
    public void setOnProgressChangeListener(OnProgressChangeListener OnProgressChangeListener) {
        mOnProgressChangeListener = OnProgressChangeListener;
    }


    /**
     * 设置主题预设
     * Set the preset theme.
     *
     * @param presetTheme 主题预设 The preset theme.
     */
    public void setPresetTheme(PresetTheme presetTheme) {
        mPresetTheme = presetTheme;
    }

    /**
     * 设置进度终值
     * Set the final progress.
     *
     * @param finalProgress 进度终值 The final progress.
     */
    public void setFinalProgress(float finalProgress) {
        mFinalProgress = finalProgress;
        if (!mNeedAnimation) {
            mProgress = mFinalProgress;
        }

    }

    /**
     * 设置动画时长
     * Set the duration of the animation.
     * @param duration 动画时长 The duration of the animation.
     */
    public void setAnimationDuration(long duration) {
        if (mNeedAnimation) {
            mArcProgressValueAnimator.setDuration(duration);
        }
    }

    /**
     * 开始动画
     * Start the animation.
     */
    public void startAnimation() {
        if (!mNeedAnimation) {
            throw new RuntimeException("You cannot start animation when set 'need animation' false");
        }
        mArcProgressValueAnimator.cancel();
        mArcProgressValueAnimator.setFloatValues(0.0f, mFinalProgress);
        mArcProgressValueAnimator.start();
    }

    /**
     * 设置底部圆圈的颜色
     * Set the color of the base ring.
     * @param baseRingColor 底部圆圈颜色 The color of the base ring.
     */
    public void setBaseRingColor(int baseRingColor) {
        mBaseRingColor = baseRingColor;
    }

    /**
     * 设置是否需要动画
     * Set whether the animation is needed.
     * @param needAnimation 需要则为true否则为false True if needed otherwise false.
     */
    public void setNeedAnimation(boolean needAnimation) {
        mNeedAnimation = needAnimation;
    }

    /**
     * 设置阴影y轴偏移
     * Set the offset of the shadow in the y axis.
     * @param shadowYOffset 阴影y轴偏移 The offset of the shadow in the y axis.
     */
    public void setShadowYOffset(float shadowYOffset) {
        mShadowYOffset = shadowYOffset;
    }

    /**
     * 设置阴影x轴偏移
     * Set the offset of the shadow in the x axis.
     * @param shadowXOffset 阴影x轴偏移 The offset of the shadow in the x axis.
     */
    public void setShadowXOffset(float shadowXOffset) {
        mShadowXOffset = shadowXOffset;
    }

    /**
     * 设置阴影宽度
     * Set the width for the shadow.
     * @param shadowWidth 阴影宽度 The width for the shadow.
     * @throws IllegalArgumentException 如果阴影宽度小于底层圆圈宽度 If the width for the shadow is {@literal <} the width of the base ring.
     */
    public void setShadowWidth(float shadowWidth) {
        if (mShadowWidth < mBaseRingWidth) {
            throw new IllegalArgumentException("Shadow width cannot be < base ring width");
        }
        mShadowWidth = shadowWidth;
    }

    /**
     * 设置底层圆圈宽度
     * Set the width of the base ring.
     * @param baseRingWidth 底层圆圈宽度 The width of the base ring.
     * @throws IllegalArgumentException 如果底层圆圈宽度小于零或阴影宽度小于底层圆圈宽度 If the width of the base ring is {@literal <} 0 or the width
     * for the shadow is {@literal <} the width of the base ring.
     */
    public void setBaseRingWidth(float baseRingWidth) {
        if (mBaseRingWidth < 0.0f) {
            throw new IllegalArgumentException("Base ring width cannot be < 0.");
        }
        if (mShadowWidth < mBaseRingWidth) {
            throw new IllegalArgumentException("Shadow width cannot be < base ring width");
        }
        mBaseRingWidth = baseRingWidth;
    }

    /**
     * 设置底层圆圈半径
     * Set the radius of the base ring.
     * @param baseRingRadius 底层圆圈半径 The radius of the base ring.
     * @throws IllegalArgumentException 如果底层圆圈半径小于零 If the radius of the base ring is {@literal <} 0.
     */
    public void setBaseRingRadius(float baseRingRadius) {
        if (mBaseRingRadius < 0.0f) {
            throw new IllegalArgumentException("Base ring radius cannot be < 0.");
        }
        mBaseRingRadius = baseRingRadius;
    }

//    public void setShadowColor(int shadowColor) {
//        mShadowColor = shadowColor;
//    }

    /**
     * 设置进度条终止颜色
     * Set the color of the arc progress with which the gradient ends.
     * @param arcProgressEndColor 进度条终止颜色 The color of the arc progress with which the gradient ends.
     */
    public void setArcProgressEndColor(int arcProgressEndColor) {
        mArcProgressEndColor = arcProgressEndColor;
    }

    /**
     * 设置进度条起始颜色
     * Set the color of the arc progress from which the gradient starts.
     * @param arcProgressStartColor 进度条起始颜色 The color of the arc progress from which the gradient starts.
     */
    public void setArcProgressStartColor(int arcProgressStartColor) {
        mArcProgressStartColor = arcProgressStartColor;
    }

    /**
     * 设置圆弧进度条的颜色
     * Set the color of the arc progress.
     * @param arcProgressColor 圆弧进度条的颜色 The color of the arc progress.
     */
    public void setArcProgressColor(int arcProgressColor) {
        mArcProgressColor = arcProgressColor;
    }

    /**
     * 设置圆弧进度条的样式
     * Set the style of the arc progress.
     * @param arcProgressStyle 圆弧进度条的样式 The style of the arc progress.
     */
    public void setArcProgressStyle(@ArcProgressStyle int arcProgressStyle) {
        mArcProgressStyle = arcProgressStyle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        float baseRingRadius;
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            baseRingRadius = (widthSize - mAbsShadowXOffset - (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowXOffset) + Math.max(mShadowWidth / 2 - mAbsShadowXOffset, mBaseRingWidth / 2))) / 2;
            baseRingRadius = Math.min(baseRingRadius,
                    (heightSize - mAbsShadowYOffset -
                            (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowYOffset) + Math.max(mShadowWidth / 2 - mAbsShadowYOffset, mBaseRingWidth / 2))
                    ) / 2);
            baseRingRadius = Math.min(baseRingRadius, mBaseRingRadius);
        } else if ((widthMode == MeasureSpec.UNSPECIFIED)) {
            baseRingRadius = (heightSize - mAbsShadowYOffset - (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowYOffset) + Math.max(mShadowWidth / 2 - mAbsShadowYOffset, mBaseRingWidth / 2))) / 2;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            baseRingRadius = (widthSize - mAbsShadowXOffset - (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowXOffset) + Math.max(mShadowWidth / 2 - mAbsShadowXOffset, mBaseRingWidth / 2))) / 2;
        } else {
            baseRingRadius = (widthSize - mAbsShadowXOffset - (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowXOffset) + Math.max(mShadowWidth / 2 - mAbsShadowXOffset, mBaseRingWidth / 2))) / 2;
            baseRingRadius = Math.min(baseRingRadius,
                    (heightSize - mAbsShadowYOffset -
                            (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowYOffset) + Math.max(mShadowWidth / 2 - mAbsShadowYOffset, mBaseRingWidth / 2))
                    ) / 2);
        }
        mBaseRingRadius = baseRingRadius;
        if (mBaseRingRadius < 0) {
            throw new IllegalArgumentException("Your parameters make base ring radius < 0.");
        }
        final int finalWidth = (int) (2 * mBaseRingRadius + mAbsShadowXOffset + (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowXOffset) + Math.max(mShadowWidth / 2 - mAbsShadowXOffset, mBaseRingWidth / 2)));
        final int finalHeight = (int) (2 * mBaseRingRadius + mAbsShadowYOffset + (Math.max(mShadowWidth / 2, mBaseRingWidth / 2 - mAbsShadowYOffset) + Math.max(mShadowWidth / 2 - mAbsShadowYOffset, mBaseRingWidth / 2)));

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));

    }

    @Override
    protected void onDraw(Canvas canvas) {


        //绘制底层圆圈 Draw the base ring.
        canvas.drawCircle(Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius,
                mBaseRingRadius,
                mBaseRingPaint);
        if (mProgress > 0.0f) {
            canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
            //绘制阴影头部小圆圈 Draw the start cap of the shadow.
            canvas.drawCircle(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowXOffset,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mShadowYOffset,
                    mShadowWidth / 2,
                    mShadowStartCapPaint);

            //绘制阴影尾部小圆圈 Draw the end cap of the shadow.
            canvas.save();
            canvas.translate(
                    Math.max(mShadowWidth / 2 - mShadowXOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowXOffset,
                    Math.max(mShadowWidth / 2 - mShadowYOffset, mBaseRingWidth / 2) + mBaseRingRadius + mShadowYOffset);
            canvas.rotate(-mProgress / 100 * 360);
            canvas.drawCircle(0,
                    -mBaseRingRadius,
                    mShadowWidth / 2,
                    mShadowEndCapPaint);
            canvas.restore();
            canvas.drawArc(mShadowRectF, -90, -mProgress / 100 * 360, false, mShadowPaint2);
            //绘制阴影 Draw the shadow.
            canvas.drawArc(mShadowRectF, -90, -mProgress / 100 * 360, false, mShadowPaint);
            canvas.restore();


            //绘制实际进度条 Draw the arc progress.
            canvas.drawArc(mArcProgressRectF, -90, -mProgress / 100 * 360, false, mArcProgressPaint);
            canvas.drawArc(mArcProgressStartCapRectF, -90, 180, true, mArcProgressStartCapPaint);

        }

    }

    @Override
    protected void onDetachedFromWindow() {
        if (mArcProgressValueAnimator != null) {
            mArcProgressValueAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    /**
     * 进度条改变监听 The callback to be invoked where the animation is need and when the progress has changed.
     */
    public interface OnProgressChangeListener {
        /**
         * 进度条进度改变时的操作 The operation when the progress has changed.
         *
         * @param progress 动画中的进度条的进度 The progress during the animation.
         */
        void onProgressChange(float progress);
    }
}

