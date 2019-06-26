package com.ashen.selectablehtmlview.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by Jungle on 2019/6/24 0024.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 修改后的背景色添加，原本的BackgroundSpan会把行间距添加颜色，但是不好看，自定义的这种样式不会有这种问题
 */
public class HighLightBackgroundSpan extends ReplacementSpan {

    private int mSize;
    private int mColor;
    private int mRadius;

    public HighLightBackgroundSpan(int color, int radius) {
        mColor = color;
        mRadius = radius;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
        //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
        //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
        //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
        return mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        canvas.save();
        int color = paint.getColor();//保存文字颜色
        paint.setColor(mColor);//设置背景颜色
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval = new RectF(x, y + paint.ascent(), x + mSize, y + paint.descent());
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        paint.setColor(color);//恢复画笔的文字颜色
        // 这里有问题,drawtext只能绘制一行，多行的时候会有问题
        canvas.drawText(text, start, end, x + mRadius, y, paint);//绘制文字
        canvas.restore();
    }
}
