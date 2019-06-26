package com.ashen.selectablehtmlview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Jungle on 2019/6/26 0026.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description 对tag，进行额外的处理，如添加图片点击事件
 */
public class DetailTagHandler implements Html.TagHandler {
    private Context context;
    private ArrayList<String> strings;

    public DetailTagHandler(Context context) {
        this.context = context;
        strings = new ArrayList<>();
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 处理标签<img>
        if ("img".equals(tag.toLowerCase(Locale.getDefault()))) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();
            // 记录所有图片地址
            strings.add(imgURL);
            // 记录是第几张图片
            int position = strings.size() - 1;
            // 使图片可点击并监听点击事件
            output.setSpan(new ClickableImage(context, position), len - 1, len,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {
        private Context context;
        private int position;

        public ClickableImage(Context context, int position) {
            this.context = context;
            this.position = position;
        }

        @Override
        public void onClick(View widget) {
            Log.i("zhangyi", strings.get(position));
//            Intent intent = new Intent(context, PhotoActivity.class);
//            intent.putStringArrayListExtra("url", strings);
//            intent.putExtra("position", position);
//            context.startActivity(intent);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
        }
    }
}