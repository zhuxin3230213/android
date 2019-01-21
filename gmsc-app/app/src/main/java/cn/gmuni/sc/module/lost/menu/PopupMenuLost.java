package cn.gmuni.sc.module.lost.menu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.utils.PixelUtil;

public class PopupMenuLost extends PopupWindow implements View.OnClickListener {
    private Context context;
    private View view;
    private LinearLayout finish;
    private LinearLayout delete;
    private View.OnClickListener finishLintener;
    private View.OnClickListener deleteLintener;


    public PopupMenuLost(Context context) {
        super(WindowManager.LayoutParams.MATCH_PARENT,  WindowManager.LayoutParams.WRAP_CONTENT);
        this.context = context;
        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        setWidth(PixelUtil.dp2px(123));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        view = LayoutInflater.from(context).inflate(R.layout.lost_found_menu,null);
        setContentView(view);
        finish = (LinearLayout) view.findViewById(R.id.lost_found_mine_finish);
        delete = (LinearLayout) view.findViewById(R.id.lost_found_mine_delete);
        finish.setOnClickListener(this);
        delete.setOnClickListener(this);
        this.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                BaseActivity act = (BaseActivity)context;
                act.findViewById(R.id.lost_found_mine_mask).setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setFinishLintener(View.OnClickListener finishLintener) {
        this.finishLintener = finishLintener;
    }

    public void setDeleteLintener(View.OnClickListener deleteLintener) {
        this.deleteLintener = deleteLintener;
    }

    public void inVisiableFinish(){
        ((TextView)view.findViewById(R.id.lost_found_mine_finish_text)).setTextColor(view.getResources().getColor(R.color.fontColorMinor));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lost_found_mine_finish:
                if(null != finishLintener){
                    finishLintener.onClick(view);
                }
                break;
            case R.id.lost_found_mine_delete:
                if(null != deleteLintener){
                    deleteLintener.onClick(view);
                }
                break;
        }
        dismiss();
    }
}
