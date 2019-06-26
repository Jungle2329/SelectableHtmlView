package com.ashen.selectablehtmlview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Jungle on 2019/6/26 0026.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 阻止因子控件获取焦点导致ScrollView自动滑动
 */
public class StopAutoScrollView extends ScrollView {

    public StopAutoScrollView(Context context) {
        super(context);
    }

    public StopAutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StopAutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        //阻止因子控件获取焦点导致ScrollView自动滑动
        Log.i("zhangyi", "requestChildFocus");
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        //阻止因子控件获取焦点导致ScrollView自动滑动
        Log.i("zhangyi", "requestChildRectangleOnScreen");
        return false;
    }

}
