package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import cn.gmuni.sc.R;
import cn.gmuni.sc.utils.DrawableUtil;

public class CustomToolbar extends Toolbar{

    /**
     * 左侧Title
     */
    private TextView mTxtLeftTitle;
    /**
     * 中间Title
     */
    private TextView mTxtMiddleTitle;
    /**
     * 右侧Title
     */
    private TextView mTxtRightTitle;

    //整体View
    private View view;

    //左侧返回按钮事件
    private OnClickListener backEvent;

    //右侧操作按钮事件
    private OnClickListener actionEvent;

    //中间文字的点击事件
    private OnClickListener textEvent;

    public CustomToolbar(Context context) {
        super(context);
        init(context,null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context,attrs);
    }

    /**
     * 初始化组件
     * @param context
     */
    private void init(Context context,AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.widget_custom_toolbar, this);
        view.setBackground(DrawableUtil.getDefaultGradientDrawable());
        mTxtLeftTitle =  findViewById(R.id.txt_left_title);
        mTxtMiddleTitle = findViewById(R.id.txt_main_title);
        mTxtRightTitle =  findViewById(R.id.txt_right_title);
        if(attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CustomToolbar);
            //设置中间的标题
            String title = typedArray.getString(R.styleable.CustomToolbar_title);
            if(title!=null){
                setTitle(title);
            }
            //设置操作按钮
            if(typedArray.hasValue(R.styleable.CustomToolbar_action_icon)){
                setActionDrawable(typedArray.getResourceId(R.styleable.CustomToolbar_action_icon,R.drawable.icon_back));
            }
            String actionTitle = typedArray.getString(R.styleable.CustomToolbar_action_title);
            if(actionTitle!=null){
                setActionTitle(actionTitle);
            }
            //设置返回按钮
            boolean enableBack = typedArray.getBoolean(R.styleable.CustomToolbar_enable_back,true);
            if(enableBack){
                String btitle = typedArray.getString(R.styleable.CustomToolbar_back_title);
                if(null == btitle){
                    btitle = "返回";
                }
                mTxtLeftTitle.setText(btitle);
                if(typedArray.hasValue(R.styleable.CustomToolbar_back_icon)){
                    setBackIcon(typedArray.getResourceId(R.styleable.CustomToolbar_back_icon,R.drawable.icon_back));
                }else{
                    setBackIcon(R.drawable.icon_back);
                }
            }else{
                mTxtLeftTitle.setVisibility(GONE);
            }
            //获取资源后要及时回收
            typedArray.recycle();
        }
        bindEvent();
    }

    /**
     * 设置返回按钮的图标尺寸
     * @param resourceId
     */
    private void setBackIcon(int resourceId){
        Drawable drawable = getResources().getDrawable(resourceId);
        drawable.setBounds(0,0,35,60);
        mTxtLeftTitle.setCompoundDrawables(drawable,null,null,null);
    }


    //设置中间title的内容
    public void setTitle(String text) {
        mTxtMiddleTitle.setVisibility(View.VISIBLE);
        mTxtMiddleTitle.setText(text);
    }

    public void setActionTitle(String text){
        mTxtRightTitle.setVisibility(VISIBLE);
        mTxtRightTitle.setText(text);
    }


    /**
     * 如果右侧有图标，则设置图标
     * @param res
     */
    public void setActionDrawable(int res){
        Drawable drawable = getResources().getDrawable(res);
        drawable.setBounds(0,0,60,60);
        mTxtRightTitle.setCompoundDrawables(null,null,drawable,null);
    }

    /**
     * 设置是否可返回
     * @param enable
     */
    public void setEnableBack(boolean enable){
        mTxtLeftTitle.setVisibility(enable?VISIBLE:GONE);
    }

    /**
     * 设置返回标题
     * @param title
     */
    public void setBackTitle(String title){
        mTxtLeftTitle.setText(title);
    }



    public void bindEvent(){
        //返回事件
        mTxtLeftTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(backEvent!=null){
                    backEvent.onClick(view);
                }
            }
        });

        mTxtRightTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(actionEvent!=null){
                    actionEvent.onClick(view);
                }
            }
        });

        mTxtMiddleTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(textEvent != null){
                    textEvent.onClick(view);
                }
            }
        });
    }


    /**
     * 设置返回事件
     * @param listener
     */
    public void setOnBackListener(OnClickListener listener){
        this.backEvent = listener;
    }

    /**
     * 如果右侧有操作按钮，则给操作按钮绑定事件
     * @param listener
     */
    public void setOnActionListener(OnClickListener listener){
        this.actionEvent = listener;
    }

    /**
     * 设置中间文字的点击事件
     * @param listener
     */
    public void setOnTitleListener(OnClickListener listener){
        this.textEvent = listener;
    }


}
