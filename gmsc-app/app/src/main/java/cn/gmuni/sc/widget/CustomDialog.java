package cn.gmuni.sc.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.widget.CustomButton;

/**
 * 个人中心自定义弹出框
 */
public class CustomDialog extends Dialog {

    private Context context;

    private String title;

    private int contentId;

    private String msg;

    //如果传入的是字符串，则给msgTextView赋值
    private TextView msgTextView;

    //内容区域
    private RelativeLayout contentwrap;

    private View contentView;

    private OnLoadComparedContentListener loadlistener;

    private View.OnClickListener okListener;

    private View.OnClickListener cancelListener;

    private CustomButton okBtn;

    private CustomButton cancalBtn;

    private CustomButton singleOkBtn;

    private LinearLayout btnWrap;

    private LinearLayout singleBtnWrap;

    private TextView titleView;

    private String leftBtnText;

    private String rightBtnText;


    public CustomDialog(@NonNull Context context, String title, int contentId) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.title = title;
        this.contentId = contentId;

    }

    public CustomDialog(@NonNull Context context, String title, String msg){
        super(context,R.style.CustomDialog);
        this.context = context;
        this.title = title;
        this.msg = msg;

    }

    public CustomDialog(@NonNull Context context, String title, String msg,String leftBtnText,String rightBtnText){
        this(context,title,msg);
        this.leftBtnText = leftBtnText;
        this.rightBtnText = rightBtnText;
    }

    public CustomDialog(@NonNull Context context, String title, int contentId,String leftBtnText,String rightBtnText){
        this(context,title,contentId);
        this.leftBtnText = leftBtnText;
        this.rightBtnText = rightBtnText;
    }

    public CustomDialog(@NonNull Context context, String title, int contentId,boolean isSingleBtn){
        this(context,title,contentId);
        if(isSingleBtn){
            this.leftBtnText = BaseApplication.getInstance().getResources().getString(R.string.ok);
        }
    }

    public CustomDialog(@NonNull Context context, String title, String msg,boolean isSingleBtn){
        this(context,title,msg);
        if(isSingleBtn){
            this.leftBtnText = BaseApplication.getInstance().getResources().getString(R.string.ok);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        setContentView(R.layout.widget_custom_dialog_content);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(null!=msg && !"".equals(msg)){
            msgTextView = findViewById(R.id.custom_dialog_msg);
            msgTextView.setVisibility(View.VISIBLE);
            msgTextView.setText(msg);
        }else if(contentId!=0){
            //初始化内容
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(contentId,null);
            contentwrap = findViewById(R.id.custom_dialog_content);
            RelativeLayout.LayoutParams params =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentwrap.addView(contentView,params);
            //添加完内容后，调用回调
            if(loadlistener!=null){
                loadlistener.onLoadCompared(contentView);
            }
        }else{
            try {
                throw new Exception("请添加内容");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        okBtn = findViewById(R.id.custom_dialog_ok_btn);
        cancalBtn = findViewById(R.id.custom_dialog_cancel_btn);
        singleOkBtn = findViewById(R.id.custom_dialog_single_btn);
        titleView = findViewById(R.id.custom_dialog_title);
        btnWrap = findViewById(R.id.custom_dialog_btn_wrap);
        singleBtnWrap = findViewById(R.id.custom_dialog_single_btn_wrap);
        //给标题赋值
        titleView.setText(title);
        //判断按钮显示情况
        if(null != this.leftBtnText && null == this.rightBtnText){
            singleBtnWrap.setVisibility(View.VISIBLE);
            btnWrap.setVisibility(View.GONE);
            singleOkBtn.setText(this.leftBtnText);
        }else{
            singleBtnWrap.setVisibility(View.GONE);
            btnWrap.setVisibility(View.VISIBLE);
            if(null!=this.leftBtnText){
                this.okBtn.setText(this.leftBtnText);
            }
            if(null != this.rightBtnText){
                this.cancalBtn.setText(this.rightBtnText);
            }
        }
        bindEvent();
    }

    private void bindEvent(){
        if(null!=msg && !"".equals(msg)){
            singleOkBtn.setOnClickListener(v-> okListener.onClick(msgTextView));
            okBtn.setOnClickListener(v-> okListener.onClick(msgTextView));
            cancalBtn.setOnClickListener(v-> cancelListener.onClick(msgTextView));
        }else if(contentId!=0){
            singleOkBtn.setOnClickListener(v-> okListener.onClick(contentView));
            //确定
            okBtn.setOnClickListener(v -> okListener.onClick(contentView));
            //取消
            cancalBtn.setOnClickListener(v-> cancelListener.onClick(contentView));
        }

    }

    /**
     * 内容加载成功事件
     * @param loadlistener
     */
    public void setOnLoadComparedContentListener(OnLoadComparedContentListener loadlistener){
        this.loadlistener = loadlistener;
    }

    public void setOnOkDialogListener(View.OnClickListener onOkListener){
        this.okListener = onOkListener;
    }

    public void setOnCancelDialogListener(View.OnClickListener cancelListener){
        this.cancelListener = cancelListener;
    }

    public void close(){
        this.dismiss();
    }

    public interface OnLoadComparedContentListener{
        void onLoadCompared(View view);
    }


}
