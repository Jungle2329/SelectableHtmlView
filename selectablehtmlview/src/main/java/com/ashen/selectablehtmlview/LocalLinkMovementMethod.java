package com.ashen.selectablehtmlview;

import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;

/**
 * Created by Jungle on 2019/6/26 0026.
 *
 * @author JungleZhang
 * @version 1.0.0
 * @Description
 */
public class LocalLinkMovementMethod extends LinkMovementMethod {

    private static LinkMovementMethod sInstance;

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new LocalLinkMovementMethod();

        return sInstance;
    }

//    @Override
//    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
//
//        int action = event.getAction();
//
//        if (action == MotionEvent.ACTION_UP ||
//                action == MotionEvent.ACTION_DOWN ||
//                action == MotionEvent.ACTION_MOVE) {
//            int x = (int) event.getX();
//            int y = (int) event.getY();
//
//            x -= widget.getTotalPaddingLeft();
//            y -= widget.getTotalPaddingTop();
//
//            x += widget.getScrollX();
//            y += widget.getScrollY();
//
//            Layout layout = widget.getLayout();
//            int line = layout.getLineForVertical(y);
//            int off = layout.getOffsetForHorizontal(line, x);
//
//            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
//
//            if (link.length != 0) {
//                if (action == MotionEvent.ACTION_UP) {
//                    link[0].onClick(widget);
//
//                    buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT),
//                            buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),
//                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    Selection.removeSelection(buffer);
//
//                } else if (action == MotionEvent.ACTION_DOWN) {
//
//                    buffer.setSpan(new BackgroundColorSpan(Color.GRAY),
//                            buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),
//                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    Selection.setSelection(buffer,
//                            buffer.getSpanStart(link[0]),
//                            buffer.getSpanEnd(link[0]));
//                } else if (action == MotionEvent.ACTION_MOVE){
//
//                    buffer.setSpan(new BackgroundColorSpan(Color.TRANSPARENT),
//                            buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]),
//                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                    Selection.removeSelection(buffer);
//                }
//
//                return true;
//            } else {
//                Selection.removeSelection(buffer);
//            }
//        }
////        return super.onTouchEvent(widget, buffer, event);
//        return false;
//    }
}
