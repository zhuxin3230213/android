package cn.gmuni.sc.base;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.orhanobut.logger.Logger;

import org.w3c.dom.Text;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.widget.CustomConstraintLayout;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.CustomToolbar;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public abstract class BaseCommonActivity extends BaseActivity{

    protected CustomToolbar toolbar;

    public abstract void initContentView(Bundle savedInstanceState);

    public abstract  void initView(Bundle savedInstanceState);

    public abstract int getToolbar();



    private CustomConstraintLayout container;

    //处理工具条
    public void initToolbar(){
        toolbar = (CustomToolbar) findViewById(getToolbar());
        if(toolbar!=null){
            toolbar.setOnBackListener(view -> {
//                if(beforeGoBack()){
                    finish();
//                }
            });
        }
    }

    @Override
    public CustomConstraintLayout getContainer() {
        if(container==null){
            synchronized (this){
                if(container==null){
                    ViewGroup content = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                    container = content.findViewWithTag("loadingLayout");
                }
            }
        }
        return container;
    }


    @Override
    public void beforeInitContentView(Bundle savedInstanceState) {
       swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
           @Override
           public void onScrollStateChange(int state, float scrollPercent) {
               Logger.i("state " + state);
           }

           @Override
           public void onEdgeTouch(int edgeFlag) {

           }

           @Override
           public void onScrollOverThreshold() {

           }
       });
        super.beforeInitContentView(savedInstanceState);
    }

    @Override
    public void beforeInitView(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right,R.anim.default_exitanim);
        initToolbar();
    }


    @Override
    public void finish() {
        beforeGoBack();
        super.finish();
        overridePendingTransition(R.anim.default_enteranim,R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 在调用返回按钮之前处理
     * @return
     */
    public void beforeGoBack(){}

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }
}
