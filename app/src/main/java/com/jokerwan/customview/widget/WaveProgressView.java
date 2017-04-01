package com.jokerwan.customview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ${JokerWan} on 2017/3/31.
 * WeChat：wjc398556712
 * Function：水波纹进度效果(可通用，只需要换掉背景图)
 */

public class WaveProgressView extends View {

    private int width;
    private int height;
    //背景图片
    private Bitmap backgroundBitmap;
    private Path mPath;
    private Paint mPathPaint;
    private float mWaveHight = 30f;
    //波浪宽
    private float mWaveWidth = 100f;
    //默认波浪颜色
    private String mWaveColor = "#00ff00";
    private int mWaveSpeed = 30;
    private Paint mTextPaint;
    private String currentText = "";
    private String mTextColor = "#FFFFFF";
    private int mTextSize = 35;
    private int maxProgress = 100;
    private int currentProgress = 0;
    //水波纹在Y轴上的坐标
    private float currentY;
    private float distance = 0;
    //重绘时间间隔
    private int RefreshGap = 10;

    //延迟重绘
    private Runnable invalidateRunnable = new Runnable() {
        @Override
        public void run() {
            if(currentProgress <= maxProgress) {
                invalidate();
                postDelayed(invalidateRunnable,RefreshGap);
            }
        }
    };

    public WaveProgressView(Context context) {
        this(context, null, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (null == getBackground()) {//没有设置背景抛异常
            throw new IllegalArgumentException(String.format("background is null."));
        } else {
            //获取背景
            backgroundBitmap = getBitmapForDrawable(getBackground());

            mPath = new Path();
            mPathPaint = new Paint();
            mPathPaint.setAntiAlias(true);
            mPathPaint.setStyle(Paint.Style.FILL);

            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setColor(Color.parseColor(mTextColor));
            //发送延迟消息
            postDelayed(invalidateRunnable,100);
        }
    }

    /**
     * 从Drawable对象中获取Bitmap对象
     * @param drawable
     * @return
     */
    private Bitmap getBitmapForDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        currentY = height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (null != backgroundBitmap) {
            canvas.drawBitmap(createImage(), 0, 0, null);
        }
    }

    private Bitmap createImage() {
        mPathPaint.setColor(Color.parseColor(mWaveColor));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        //剩余进度
        float currentMidY = height * (maxProgress - currentProgress) / maxProgress;
        if (currentY > currentMidY) {//每次增长currentY-currentMidY)/10
            currentY = currentY - (currentY - currentMidY) / 10;
        }
        //清除掉path里的线条和曲线，但是不会改变它的fill－type
        mPath.reset();
        //移动路径的起始点到(0 - distance, currentY)
        mPath.moveTo(0 - distance, currentY);
        //显示的区域内的水波纹的数量
        int waveNum = width / ((int) mWaveWidth);
        int num = 0;
        for (int i = 0; i < waveNum; i++) {
            //封装了二阶贝塞尔曲线，x1、y1 代表控制点的 x、y，即一个控制点动态图中的P1，x2、y2 代表目标点的 x、y
            mPath.quadTo(mWaveWidth * (num + 1) - distance, currentY - mWaveHight, mWaveWidth * (num + 2) - distance, currentY);
            mPath.quadTo(mWaveWidth * (num + 3) - distance, currentY + mWaveHight, mWaveWidth * (num + 4) - distance, currentY);
            num += 4;
        }

        distance += mWaveWidth / mWaveSpeed;
        distance = distance % (mWaveWidth * 4);
        //从上一个点已直线方式连接到坐标（width, height）
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        //close相当于lineTo到最后一次moveTo的终点构成一条闭合路径
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);

        int min = Math.min(width, height);
        backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, min, min, false);
        //取上层非交集部分与下层交集部分
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        canvas.drawBitmap(backgroundBitmap, 0, 0, paint);
        canvas.drawText(currentText, width / 2, height / 2, mTextPaint);
        return bitmap;
    }

    /**
     * 设置进度和显示进度的文本
     * @param currentProgress
     * @param currentText
     */
    public void setCurrentProgress(int currentProgress, String currentText) {
        this.currentProgress = currentProgress;
        this.currentText = currentText;
    }

    /**
     * 设置波纹颜色
     * @param mWaveColor
     */
    public void setWaveColor(String mWaveColor) {
        this.mWaveColor = mWaveColor;
    }

}
