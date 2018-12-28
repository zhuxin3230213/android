package cn.gmuni.sc.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CustomSlidingPaneLayout extends SlidingPaneLayout {
    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mEdgeSlop;//手滑动的距离
    public CustomSlidingPaneLayout(@NonNull Context context) {
        this(context,null);
    }
    public CustomSlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        ViewConfiguration config = ViewConfiguration.get(context);
        mEdgeSlop = config.getScaledEdgeSlop();//getScaledTouchSlop是一个距离
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            if(ev.getX()-100<0){
                return super.onTouchEvent(ev);
            }
            return false;
        }
       return super.onTouchEvent(ev);
    }

}
