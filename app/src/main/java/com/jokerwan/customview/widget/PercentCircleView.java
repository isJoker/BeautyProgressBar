package com.jokerwan.customview.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ${JokerWan} on 2017/3/27.
 * WeChat：wjc398556712
 * Function：自定义圆形比例图（进度条）
 */

public class PercentCircleView extends View {

    //PercentCircleView的宽高
    private float mWidth;
    private float mHeight;
    //圆心坐标
    private float mCircleXY;
    //圆的半径
    private float mRadius;
    //弧线的外接矩形
    private RectF rectF;
    //画笔
    private Paint paint;
    private Paint textPaint;
    private Paint circlePaint;
    //最大进度
    private int mMaxProgress = 100;
    //当前进度
    private int mProgress = 30;
    //外层弧线的宽
    private int mCircleLineStrokeWidth;

    public PercentCircleView(Context context) {
        this(context,null);
    }

    public PercentCircleView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PercentCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        rectF = new RectF();
        paint = new Paint();
        textPaint = new Paint();
        circlePaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //取出宽高的测量模式和大小
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理layout_width 和 layout_height 为 wrap_content的这种情况，使wrap_content有效
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200,200);
        } else if(widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200,heightSpecSize);
        } else if(heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize,200);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //得到视图的宽高取最小值给宽
        mWidth = getWidth();
        mHeight = getHeight();
        mWidth = Math.min(mWidth, mHeight);

        //确定圆心，半径，外弧的宽度
        mCircleXY = mWidth / 2;
        mRadius = mCircleXY / 2;
        mCircleLineStrokeWidth = (int) (mRadius / 2);

        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(0x00, 0xff, 0x00));
        canvas.drawColor(Color.TRANSPARENT);
        paint.setStrokeWidth(mCircleLineStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.rgb(0x00, 0xff, 0x00));

        //绘制弧线，需要制定其椭圆的外接矩形
        rectF.left = mCircleLineStrokeWidth / 2;
        rectF.top = mCircleLineStrokeWidth / 2;
        rectF.right = mWidth - mCircleLineStrokeWidth / 2;
        rectF.bottom = mWidth - mCircleLineStrokeWidth / 2;

        //绘制圆
        canvas.drawCircle(mCircleXY,mCircleXY,mRadius,circlePaint);
        //绘制弧线
        canvas.drawArc(rectF,-90,((float) mProgress/mMaxProgress) * 360,false,paint);
        //绘制文字
        String text = mProgress + "%";
        float showTextSize = mWidth / 10;
        textPaint.setTextSize(showTextSize);
        textPaint.setColor(Color.rgb(0xff, 0x00, 0x00));
        canvas.drawText(text,0,text.length(),mCircleXY - (showTextSize * text.length())/4,mCircleXY + showTextSize / 3 ,textPaint);
    }

    //对外提供设置进度的方法
    public void setmProgress(int  progress){
        if(progress >= 0) {
            mProgress = progress;
        } else {
            mProgress = 0;
        }
        this.invalidate();
    }
}
