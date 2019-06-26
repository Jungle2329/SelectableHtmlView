package com.ashen.selectablehtmlview.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by Jungle on 2019/6/24 0024.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 有想法后的添加的小圆点
 */
public class ThinkingDotSpan extends ReplacementSpan {

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return (int) (paint.measureText(text, start, end));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
//        canvas.save();
//        TextPaint tp = new TextPaint(paint);
//        StaticLayout myStaticLayout = new StaticLayout(text.toString().substring(start, end), tp, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//        canvas.translate(x, top);
//        myStaticLayout.draw(canvas);
//        canvas.restore();
        int f = (int) ((paint.getFontMetrics().bottom - paint.getFontMetrics().top) / 2);
        canvas.drawCircle(x + 5, y - f - 5, 10, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
    }
}
