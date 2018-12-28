package cn.gmuni.sc.widget.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @Author: zhuxin
 * @Date: 2018/10/23 10:15
 * @Description: RecyclerView多次刷新数据导致崩溃，把这个异常捕获
 * 创建一个类LinearLayoutManagerWrapper继承LinearLayoutManager，重写onLayoutChildren方法
 */
public class WrapContentLinearLayoutManager extends LinearLayoutManager {

    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    //禁用预测动画
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    //水平滚动
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollHorizontallyBy(dx, recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("hello", "scrollHorizontallyBy, IndexOutOfBoundsException");
            e.printStackTrace();
            return 0;
        }
    }

    //垂直滚动
    @Override
    public int scrollVerticallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            return super.scrollVerticallyBy(dx, recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("hello", "scrollVerticallyBy, IndexOutOfBoundsException");
            e.printStackTrace();
            return 0;
        }
    }
}
