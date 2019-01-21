package cn.gmuni.sc.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Author: zhuxin
 * @Date: 2018/11/8 13:31
 * @Description:
 * recycler间距设置
 */
public class ChatDetailItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ChatDetailItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {


        if (parent.getChildPosition(view) == 0) {
            //第一条item边距设置
            outRect.top = 0;
            outRect.bottom = 0;
        } else {
            outRect.bottom = space;
        }
    }
}
