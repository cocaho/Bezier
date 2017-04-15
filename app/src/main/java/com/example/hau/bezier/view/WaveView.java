package com.example.hau.bezier.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Hau on 2017/4/9.
 */

public class WaveView extends View {
     //绘制曲线的工具,Path相当于手，Paint相当于画笔
     private Paint mPaint;
     private Path path;
     //让曲线动起来的参数
     private int waveLength = 1000;
     private int waveCount;
     private int offSet;
    //绘制曲线的位置参数
    private  int mCenterY;
    private int mScreenHeight;
    private int mScreenWidth;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAnimator();
    }
    //设置动画类
    private void initAnimator() {
        final ValueAnimator animator = ValueAnimator.ofInt(0,waveLength); //值的取值范围
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);//动画无限重复
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                offSet = (int)animator.getAnimatedValue();//不断取得0~1000之间的数
                postInvalidate();
            }
        });
        animator.start();
    }

    private void initPaint() {
        path = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#59c3e2"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);


    }

    @Override//获取位置参数
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;
        waveCount = (int) Math.round(mScreenWidth / waveLength+ 1.5);//为了适配各种屏幕，需要根据手机的宽度来计算出所需要的波纹的数目；+1.5保证至少有两条波纹
        mCenterY = mScreenHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置“手”的行为
        path.reset();
        path.moveTo(-waveLength+offSet,mCenterY);
        //path.moveTo(-waveLength,mCenterY);//offset不影响
        for(int i=0;i<waveCount;i++){
            path.quadTo((-waveLength * 3 / 4) + (i * waveLength) + offSet, mCenterY + 120, (-waveLength/ 2) + (i * waveLength) + offSet, mCenterY);
            path.quadTo((-waveLength / 4) + (i * waveLength) + offSet, mCenterY - 120, i * waveLength + offSet, mCenterY);


        }
        //填充波浪线下面的面积
        path.lineTo(mScreenWidth,mScreenHeight);
        path.lineTo(0,mScreenHeight);
        path.close();
        canvas.drawPath(path,mPaint);



    }
}
