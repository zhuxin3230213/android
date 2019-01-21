package cn.gmuni.sc.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import cn.gmuni.sc.R;

public class CustomConstraintLayout  extends ConstraintLayout{
    private Context context;

    private View container;

    private ConstraintLayout loading;

//    private ConstraintLayout emptydata;

    private RotateAnimation loadingAnimation = null;

    private CustomImageView loadImage = null;


    public CustomConstraintLayout(Context context) {
        this(context,null);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){
        LayoutInflater inflater  = LayoutInflater.from(context);
        container = inflater.inflate(R.layout.widget_custom_constraint_layout, this).findViewById(R.id.custom_constraint_wrap);
        loading = container.findViewById(R.id.widget_constraint_loading);
//        emptydata = container.findViewById(R.id.widget_constraint_empty_data);
        loadImage = container.findViewById(R.id.loading_whirl);
        container.setVisibility(GONE);
        loadingAnimation =  new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        loadingAnimation.setDuration(800);
        loadingAnimation.setRepeatCount(Animation.INFINITE);
        loadingAnimation.setRepeatMode(Animation.RESTART);
    }

    /**
     * 切换子元素的隐藏状态
     * @param visible
     */
   private void toggle(int visible){
       int childCount = this.getChildCount();
       //第一个是元素本身
       for(int i = 1;i<childCount;i++){
           this.getChildAt(i).setVisibility(visible);
       }
   }

    public void showLoading(){
        toggle(GONE);
        container.setVisibility(VISIBLE);
        loading.setVisibility(VISIBLE);
        loadImage.startAnimation(loadingAnimation);
    }


    public void hideLoading(){
       toggle(VISIBLE);
       container.setVisibility(GONE);
        loading.setVisibility(GONE);
    }

}
