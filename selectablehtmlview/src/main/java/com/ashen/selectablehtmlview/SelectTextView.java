package com.ashen.selectablehtmlview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Range;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ashen.selectablehtmlview.bean.SelectDataBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jungle on 2019/6/20 0020.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 支持标记的TextView
 * 1.长按开始标记，松手标记完成
 * 2.标记后可以添加标记的注释，注释后会出现小圆点，这个小圆点是根据一个字符替换生成的
 * 3.重合的标记会自动合并
 */
public class SelectTextView extends android.support.v7.widget.AppCompatTextView {

    private final static String TAG = SelectTextView.class.getSimpleName();
    // 是否是长按
    private boolean isLongClick = false;
    // 高亮颜色
    private int highLightColor;
    // 高亮文字颜色
    private int highLightTextColor;
    // 触摸到的点
    private Point touchPos = new Point();
    // 所有该TextView下选中的数据
    private List<SelectDataBean> selectList = new ArrayList<>();

    public SelectTextView(Context context) {
        this(context, null);
    }

    public SelectTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectTextView);
        highLightColor = ta.getColor(R.styleable.SelectTextView_highLightColor, getHighlightColor());
        highLightTextColor = ta.getColor(R.styleable.SelectTextView_highLightTextColor, getHighlightColor());
        ta.recycle();
        init();
    }

    private void init() {
        String text = "晓蒙旅游回来后，和同学们分享了她拍摄的照片，请问晓蒙可能依次去了哪些地方？晓蒙旅游回来后，和同学们分享了她拍摄的照片，请问晓蒙可能依次去了哪些地方？" +
                "<img src=\"http://tiku.huatu.com/cdn/images/vhuatu/tiku/a/a604debe09839227f0749eb67eedd7e3.png\" width=\"600\" height=\"455\" style=\"width:600;height:455\">" +
                "晓蒙旅游回来后，和同学们分享了她拍摄的照片，请问晓蒙可能依次去了哪些地方？晓蒙旅游回来后，和同学们分享了她拍摄的照片，请问晓蒙可能依次去了哪些地方？" +
                "晓蒙旅游回来后，和同学们分享了她拍摄的照片，请问晓蒙可能依次去了哪些地方？晓蒙旅游回来后，和同学们分享了她拍摄的照片，请问晓蒙可能依次去了哪些地方？" +
                "<img src=\"http://tiku.huatu.com/cdn/images/vhuatu/tiku/a/a604debe09839227f0749eb67eedd7e3.png\" width=\"600\" height=\"455\" style=\"width:600;height:455\">";

//        String text = "<html><head><title>TextView使用HTML</title></head><body><p><strong>强调</strong></p><mark>nihaojieshihahaha</mark><p><em>斜体</em></p>"
//                + "<p><a href=\"http://www.jb51.net\">超链接HTML入门</a>学习HTML!</p><p><font color=\"#aabb00\">颜色1"
//                + "</p><p><font color=\"#00bbaa\">颜色2</p><h1>标题1</h1><h3>标题2</h3><h6>标题3</h6><p>大于>小于<</p><p>"
//                + "下面是网络图片</p><img src=\"http://course-image-1258943427.cos.ap-beijing.myqcloud.com/teacher/wenxiao.jpg\"/></body>"
//                + "下面是网络图片</p><img src=\"http://tiku.huatu.com/cdn/images/vhuatu/tiku/a/a604debe09839227f0749eb67eedd7e3.png\" width=\"600\" height=\"455\" style=\"width:600;height:455\"></body></html>";

        setText(Html.fromHtml(text, new HtmlHttpImageGetter(this, "", true), new DetailTagHandler(getContext())));
        setTextIsSelectable(true);
        setHighlightColor(highLightColor);
        setMovementMethod(LocalLinkMovementMethod.getInstance());//必须设置否则无效
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongClick = true;
                return false;
            }
        });

        //重写方法阻挡原生的选中功能，但是在小米上不生效，只能用up事件来做该功能
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //返回 重写方法阻挡原生的选中功能
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchPos.set((int) event.getRawX(), (int) event.getRawY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            // 在手势抬起的时候触发选中功能
            if (isLongClick) {
                isLongClick = false;
                checkSpanAndSetSpan();
            }
        }
        return super.onTouchEvent(event);
    }


    /**
     * 验证并处理新添加的link
     * 1.如果跟之前已有的link不冲突就直接添加
     * 2.如果包含之前的link就合并之前的link进来
     */
    private void checkSpanAndSetSpan() {
        int newStart = getSelectionStart();
        int newEnd = getSelectionEnd();
        // [newStart, newEnd)
        if (newStart < 0 || newEnd < 0 || newEnd == newStart) {
            return;
        }
        // 如果开始值大于结束值，需要置换
        if (newStart > newEnd) {
            // 交换这俩值
            int temp = newStart;
            newStart = newEnd;
            newEnd = temp;
        }
        // 此时保证了start < end
        // 开始寻找新区间所落在的范围
        /*
            这种需求下的的情况可以分为如下：
            1. 新区间完全被区间集合中的某一个包含，什么都不需要处理
            2. 新区间左边在区间集合中，右不在集合中
            3. 新区间左边不在集合中，右边在集合中
            4. 新区间左边右边都不在集合中
            5. 新区间左边右边都不在集合中，但是新区间包含集合中的一个或者多个区间
         */
        Range<Integer> newRange = new Range<>(newStart, newEnd - 1);

        // 先判断 新区域完全在旧区域内，不需要处理
        for (int i = 0; i < selectList.size(); i++) {
            Range<Integer> range = new Range<>(selectList.get(i).getStartPoint(), selectList.get(i).getEndPoint() - 1);
            if (range.contains(newRange)) {// 新区域完全在旧区域内，不需要处理
                return;
            }
        }
        // 确定新区间左边的位置关系
        int newRangeLeft = newRange.getLower();
        for (int i = 0; i < selectList.size(); i++) {
            Range<Integer> range = new Range<>(selectList.get(i).getStartPoint(), selectList.get(i).getEndPoint() - 1);
            if (range.contains(newRangeLeft)) {// 新区域完全在旧区域内，不需要处理
                newRangeLeft = selectList.get(i).getStartPoint();
            }
        }
        // 确定新区间右边的位置关系
        int newRangeRight = newRange.getUpper();
        for (int i = 0; i < selectList.size(); i++) {
            Range<Integer> range = new Range<>(selectList.get(i).getStartPoint(), selectList.get(i).getEndPoint() - 1);
            if (range.contains(newRangeRight)) {// 新区域完全在旧区域内，不需要处理
                newRangeRight = selectList.get(i).getEndPoint() - 1;
            }
        }
        // 确定形成的新区间
        Range<Integer> range = new Range<>(newRangeLeft, newRangeRight);
        // 确定实体类集合中新区间形成后导致旧区间失效的项目
        for (int i = 0; i < selectList.size(); i++) {
            if (range.getLower() <= selectList.get(i).getStartPoint() && range.getUpper() >= selectList.get(i).getEndPoint()) {
                selectList.remove(i);
                i--;
            }
        }
        // 清除新区间内的所有标记
        clearSpan(range.getLower(), range.getUpper() + 1);
        // 添加新标记
        updateSpan(range.getLower(), range.getUpper() + 1, "");
    }


    /**
     * 添加初始数据
     *
     * @param selectList
     */
    public void initSpan(List<SelectDataBean> selectList) {
        for (SelectDataBean list : selectList) {
            updateSpan(list.getStartPoint(), list.getEndPoint(), list.getLinkExplain());
        }
        this.selectList = selectList;
        Log.i("zhangyi", selectList.toString());
    }


    /**
     * 更新想法
     *
     * @param explain
     */
    public void addExplain(int start, int end, String explain) {
        updateSpan(start, end, explain);
    }

    /**
     * 移除想法
     *
     * @param start
     * @param end
     */
    public void removeExplain(int start, int end) {
        updateSpan(start, end, "");
    }


    /**
     * 添加新span
     *
     * @param start
     * @param end
     * @param explain
     */
    private void updateSpan(int start, int end, String explain) {
        // 先清理掉这里的span
        removeSpan(start, end);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(getText());

        // 弹出PPP点击事件
        ShowPPPClickableSpan pppSpan = new ShowPPPClickableSpan(start, end, explain);
        // 查看解释点击事件
        ShowExplainClickableSpan explainSpan = new ShowExplainClickableSpan(start, end, explain);
        // 下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();
        // 加粗
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(highLightTextColor);
        // 背景色
        BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(highLightColor);
//        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(getContext(), R.style.SpecialTextAppearance);
//        ThinkingDotSpan dotSpan = new ThinkingDotSpan();

        builder.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(styleSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(underlineSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (TextUtils.isEmpty(explain)) {
            builder.setSpan(pppSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            builder.setSpan(explainSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(backgroundSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(builder);

        //添加该选中信息
        SelectDataBean newBean = new SelectDataBean();
        newBean.setStartPoint(start);
        newBean.setEndPoint(end);
        newBean.setLinkExplain(explain);
        selectList.add(newBean);
        //按照起始点来排序，方便以后处理
        Collections.sort(selectList, new Comparator<SelectDataBean>() {
            @Override
            public int compare(SelectDataBean o1, SelectDataBean o2) {
                return o1.getStartPoint() - o2.getStartPoint();
            }
        });
        Log.i("zhangyi", selectList.toString());
    }

    /**
     * 移除Span
     *
     * @param start
     * @param end
     */
    private void removeSpan(int start, int end) {
        clearSpan(start, end);
        for (int i = 0; i < selectList.size(); i++) {
            if (selectList.get(i).getStartPoint() == start && selectList.get(i).getEndPoint() == end) {
                selectList.remove(i);
                Log.i("zhangyi", selectList.toString());
                return;
            }
        }
    }

    /**
     * 只清理原有范围内的自定义span样式
     *
     * @param start
     * @param end
     */
    public void clearSpan(int start, int end) {
        clearSpan(start, end, false);
    }

    /**
     * 只清理原有范围内的span样式
     *
     * @param start
     * @param end
     */
    public void clearSpan(int start, int end, boolean clearAllSpan) {
        if (getText() instanceof SpannableString) {
            if (clearAllSpan) {
                // 清除所有标记(如果是在富文本上操作，这个方法会把富文本所有的信息都清除掉，所以不能乱用)
                CharacterStyle[] backSpans = ((SpannableString) getText()).getSpans(start, end, CharacterStyle.class);
                for (CharacterStyle spans : backSpans) {
                    if (!(spans instanceof ImageSpan)) {//不要清除富文本的图片样式
                        ((SpannableString) getText()).removeSpan(spans);
                    }
                }
            } else {
                // 清理我们自己创建的标记，保留原有的样式
                for (CharacterStyle spans : ((SpannableString) getText()).getSpans(start, end, ShowPPPClickableSpan.class)) {
                    ((SpannableString) getText()).removeSpan(spans);
                }
                for (CharacterStyle spans : ((SpannableString) getText()).getSpans(start, end, ShowExplainClickableSpan.class)) {
                    ((SpannableString) getText()).removeSpan(spans);
                }
                for (CharacterStyle spans : ((SpannableString) getText()).getSpans(start, end, UnderlineSpan.class)) {
                    ((SpannableString) getText()).removeSpan(spans);
                }
                for (CharacterStyle spans : ((SpannableString) getText()).getSpans(start, end, StyleSpan.class)) {
                    ((SpannableString) getText()).removeSpan(spans);
                }
                for (CharacterStyle spans : ((SpannableString) getText()).getSpans(start, end, ForegroundColorSpan.class)) {
                    ((SpannableString) getText()).removeSpan(spans);
                }
                for (CharacterStyle spans : ((SpannableString) getText()).getSpans(start, end, BackgroundColorSpan.class)) {
                    ((SpannableString) getText()).removeSpan(spans);
                }
            }

        }
    }

    /**
     * 展示PopupWindow的点击事件
     */
    class ShowPPPClickableSpan extends ClickableSpan {

        private int startPos;
        private int endPos;
        private String explain;

        public ShowPPPClickableSpan(int startPos, int endPos, String explain) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.explain = explain;
        }

        @Override
        public void onClick(@NonNull View widget) {
            final PopupWindow ppp = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            View mView = LayoutInflater.from(getContext()).inflate(R.layout.ppp_click, null);
            TextView tvClearExplain = mView.findViewById(R.id.tvClearExplain);
            TextView tvAddExplain = mView.findViewById(R.id.tvAddExplain);
            // 清理注释
            tvClearExplain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeSpan(startPos, endPos);
                    ppp.dismiss();
                }
            });
            // 添加注释
            tvAddExplain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    addExplain(startPos, endPos, explain);
                }
            });
            ppp.setContentView(mView);
            ppp.setBackgroundDrawable(new ColorDrawable());
            ppp.setOutsideTouchable(true);
            ppp.showAtLocation(((Activity) getContext()).getWindow().getDecorView(), Gravity.START | Gravity.TOP, touchPos.x, touchPos.y + 10);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
        }
    }

    /**
     * 展示解释的点击事件
     */
    class ShowExplainClickableSpan extends ClickableSpan {

        private int startPos;
        private int endPos;
        private String explain;

        public ShowExplainClickableSpan(int startPos, int endPos, String explain) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.explain = explain;
        }

        @Override
        public void onClick(@NonNull View widget) {
            new AlertDialog.Builder(getContext()).setMessage(explain).show();
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
        }
    }

    /**
     * 获取TextView某一个字符的坐标
     *
     * @return 返回的是相对坐标
     * @parms tv
     * @parms index 字符下标
     */
    private Point getWordPos(TextView tv, int index) {
        Layout layout = tv.getLayout();
        Rect bound = new Rect();
        int line = layout.getLineForOffset(index);
        layout.getLineBounds(line, bound);
//        int yAxisBottom = bound.bottom;//字符底部y坐标
        int yAxisTop = bound.top;//字符顶部y坐标
        int xAxisLeft = (int) layout.getPrimaryHorizontal(index);//字符左边x坐标
//        float xAxisRight = layout.getSecondaryHorizontal(index);//字符右边x坐标
        return new Point(xAxisLeft, yAxisTop);
    }

}

