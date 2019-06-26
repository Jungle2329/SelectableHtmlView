package com.ashen.slidecard;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by Jungle on 2019/6/19 0019.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 滑动卡片
 * 1.支持横竖屏动态切换
 */
public class SlideCardView extends FrameLayout {

    private final static String TAG = SlideCardView.class.getSimpleName();
    private View vToucher;
    private FrameLayout flToucherContainer;
    private ScrollView svScroller, svContent;
    private int size = 0;
    private LinearLayout llContainer;
    private int currentOrientation = -1;
    private int newOrientation = -1;

    public SlideCardView(Context context) {
        this(context, null);
    }

    public SlideCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //更新当前横竖屏状态
        newOrientation = getContext().getResources().getConfiguration().orientation;
        // 根据横竖屏状态获取需要的大小
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            size = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            size = MeasureSpec.getSize(heightMeasureSpec);
        }

        if (newOrientation != currentOrientation) {// 节约性能
            currentOrientation = newOrientation;
            post(new Runnable() {
                @Override
                public void run() {
                    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
                        llContainer.setOrientation(LinearLayout.HORIZONTAL);
                        //内容模块
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) svContent.getLayoutParams();
                        params.width = 0;
                        params.weight = 1;
                        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                        svContent.setLayoutParams(params);
                        //滑块
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) flToucherContainer.getLayoutParams();
                        params1.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        params1.height = LinearLayout.LayoutParams.MATCH_PARENT;
                        flToucherContainer.setLayoutParams(params1);
                        ViewGroup.LayoutParams params2 = vToucher.getLayoutParams();
                        params2.width = 60;
                        params2.height = 200;
                        vToucher.setLayoutParams(params2);
                        //滑动区域
                        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) svScroller.getLayoutParams();
                        params3.width = (int) (size * 0.5);
                        params3.height = LinearLayout.LayoutParams.MATCH_PARENT;
                        svScroller.setLayoutParams(params3);

                    } else {// 竖屏
                        llContainer.setOrientation(LinearLayout.VERTICAL);
                        //内容模块
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) svContent.getLayoutParams();
                        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        params.weight = 1;
                        params.height = 0;
                        svContent.setLayoutParams(params);
                        //滑块
                        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) flToucherContainer.getLayoutParams();
                        params1.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        params1.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        flToucherContainer.setLayoutParams(params1);
                        ViewGroup.LayoutParams params2 = vToucher.getLayoutParams();
                        params2.width = 200;
                        params2.height = 60;
                        vToucher.setLayoutParams(params2);
                        //滑动区域
                        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) svScroller.getLayoutParams();
                        params3.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        params3.height = (int) (size * 0.5);
                        svScroller.setLayoutParams(params3);

                    }
                }
            });
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.slide_card_view, this, true);
        llContainer = findViewById(R.id.llContainer);
        flToucherContainer = findViewById(R.id.flToucherContainer);
        svScroller = findViewById(R.id.svScroller);
        svContent = findViewById(R.id.svContent);
        vToucher = findViewById(R.id.vToucher);

        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED: // 拖拽开始`
//                        Log.i(TAG, "ACTION_DRAG_STARTED");
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    case DragEvent.ACTION_DRAG_ENTERED: // 被拖拽View进入目标区域
//                        Log.i(TAG, "ACTION_DRAG_ENTERED");
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION: // 被拖拽View在目标区域移动
//                        Log.i(TAG, "ACTION_DRAG_LOCATION" + event.getY());
//                        Log.i(TAG, findFocus().getClass().getSimpleName());
                        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                            svScroller.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (size - event.getY())));
                        } else {
                            svScroller.setLayoutParams(new LinearLayout.LayoutParams((int) (size - event.getX()), ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED: // 被拖拽View离开目标区域
//                        Log.i(TAG, "ACTION_DRAG_EXITED");
                        return true;
                    case DragEvent.ACTION_DROP: // 放开被拖拽View
//                        Log.i(TAG, "ACTION_DROP");
//                        String content = event.getClipData().getItemAt(0).getText().toString(); //接收数据
//                        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED: // 拖拽完成
//                        Log.i(TAG, "ACTION_DRAG_ENDED");
                        return true;
                }
                return false;
            }
        });
        vToucher.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ClipData.Item item = new ClipData.Item("");  //传过去的数据
                ClipData data = new ClipData(null, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                vToucher.startDrag(data, new DragShadowBuilder(new View(getContext())), null, 0);
                return true;
            }
        });

    }


}
