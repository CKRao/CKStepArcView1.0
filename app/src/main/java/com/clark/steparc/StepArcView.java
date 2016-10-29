package com.clark.steparc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.centerX;

/**
 * Created by clark on 2016/10/29.
 */

public class StepArcView extends View {
    /**
     * 圆弧的宽度
     */
    private float borderWidth = 38f;
    /**
     * 画步数的数值的字体大小
     */
    private float numberTextSize = 0;

    /**
     * 步数
     */
    private String stepNumber = "0";
    /**
     * 开始绘制圆弧的角度
     */
    private float startAngle = 135;
    /**
     * 终点对应的角度和起始点对应角度的夹角
     */
    private float angleLength = 270;
    /**
     * 动画时长
     */
    private int animationLength = 3000;
    /**
     * 所要绘制的当前步数的红色圆弧终点到起点的夹角
     */
    private float currentAngleLength = 0;

    public StepArcView(Context context) {
        this(context, null);
    }

    public StepArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 中心点的X坐标
         */
        float centerX = (getWidth()) / 2;
        /**
         * 指定圆弧的外轮廓矩形区域
         */
        RectF rectF = new RectF(0 + borderWidth, borderWidth, 2 * centerX - borderWidth, 2 * centerX - borderWidth);

        /**
         * 1,绘制整体的黄色圆弧
         */
        drawArcYellow(canvas, rectF);
        /**
         * 2,绘制当前进度的红色字符
         */
        drawArcRed(canvas, rectF);
        /**
         * 3,绘制当前进度的红色数字
         */
        drawTextNumber(canvas, centerX);
        /**
         * 4,绘制"步数的"红色数字
         */
        drawTextStepString(canvas, centerX);
    }

    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dipToPx(16));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(Color.GRAY);
        String stepString = "步数";
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, centerX, getHeight() / 2 + bounds.height() + getFontHeight(numberTextSize), vTextPaint);
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Rect bounds_Number = new Rect();
        paint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        return bounds_Number.height();
    }

    /**
     * dip 转换为px
     *
     * @param dip
     * @return
     */
    private float dipToPx(int dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 圆环中心的步数
     *
     * @param canvas
     * @param centerX
     */
    private void drawTextNumber(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);
        vTextPaint.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        vTextPaint.setTypeface(font);//字体风格
        vTextPaint.setColor(Color.RED);
        Rect bounds_Number = new Rect();
        vTextPaint.getTextBounds(stepNumber, 0, stepNumber.length(), bounds_Number);
        canvas.drawText(stepNumber, centerX, getHeight() / 2 + bounds_Number.height() / 2, vTextPaint);
    }

    /**
     * 绘制当前步数的红色圆弧
     *
     * @param canvas
     * @param rectF
     */
    private void drawArcRed(Canvas canvas, RectF rectF) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStrokeJoin(Paint.Join.ROUND);
        paintCurrent.setStrokeCap(Paint.Cap.ROUND);//圆角弧度
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(borderWidth);//设置画笔宽度
        paintCurrent.setColor(Color.RED);//设置画笔颜色
        canvas.drawArc(rectF, startAngle, currentAngleLength, false, paintCurrent);
    }

    /**
     * @param canvas 画布
     * @param rectF  参考的矩形
     */
    private void drawArcYellow(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        //默认的画笔颜色，黄色
        paint.setColor(Color.YELLOW);
        //结合处为圆弧
        paint.setStrokeJoin(Paint.Join.ROUND);
        //设置画笔的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paint.setStrokeCap(Paint.Cap.ROUND);
        //设置画笔的填充样式 Paint.Style.FILL  :填充内部;Paint.Style.FILL_AND_STROKE ：
        // 填充内部和描边;  Paint.Style.STROKE  ：仅描边
        paint.setStyle(Paint.Style.STROKE);
        //抗锯齿功能
        paint.setAntiAlias(true);
        //设置画笔宽度
        paint.setStrokeWidth(borderWidth);

        //绘制圆弧
        canvas.drawArc(rectF, startAngle, angleLength, false, paint);
    }

    /**
     * 所走的步数进度
     *
     * @param totalStepNum  设置的步数
     * @param currentCounts 所走步数
     */
    public void setCurrentCount(int totalStepNum, int currentCounts) {
        stepNumber = currentCounts + "";
        setTextSize(currentCounts);
        /**如果当前走的步数超过总步数则圆弧还是270度，不能成为园*/
        if (currentCounts > totalStepNum) {
            currentCounts = totalStepNum;
        }
        /**所走步数占用总共步数的百分比*/
        float scale = (float) currentCounts / totalStepNum;
        /**换算成弧度最后要到达的角度的长度-->弧长*/
        float currentAngleLength = scale * angleLength;
        /**开始执行动画*/
        setAnimation(0, currentAngleLength, animationLength);
    }
    /**
     * 为进度设置动画
     * ValueAnimator是整个属性动画机制当中最核心的一个类，属性动画的运行机制是通过不断地对值进行操作来实现的，
     * 而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
     * 它的内部使用一种时间循环的机制来计算值与值之间的动画过渡，
     * 我们只需要将初始值和结束值提供给ValueAnimator，并且告诉它动画所需运行的时长，
     * 那么ValueAnimator就会自动帮我们完成从初始值平滑地过渡到结束值这样的效果。
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }
    private void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4) {
            numberTextSize = dipToPx(50);
        } else if (length > 4 && length <= 6) {
            numberTextSize = dipToPx(40);
        } else if (length > 6 && length <= 8) {
            numberTextSize = dipToPx(30);
        } else if (length > 8) {
            numberTextSize = dipToPx(25);
        }
    }
}
