package cn.gmuni.sc.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.gmuni.sc.R;

/**
 * @Author: zhuxin
 * @Date: 2018/10/31 18:02
 * @Description:
 */
public class CustomSignInDialog extends Dialog {

    private Context context;
    private String signInIntegral; //任务积分
    private ClickListenerInterface clickListenerInterface;

    public CustomSignInDialog(@NonNull Context context, String signInIntegral) {
        super(context, R.style.style_dialog);
        this.context = context;
        this.signInIntegral = signInIntegral;
    }

    public CustomSignInDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected CustomSignInDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_custom_sign_in_dialog, null);
        setContentView(view);
        TextView integral = (TextView) view.findViewById(R.id.sign_in_dialog_three_content);
        CustomImageView clear = (CustomImageView) view.findViewById(R.id.sign_in_dialog_one_clear);
        integral.setText(signInIntegral);//设置任务积分
        //取消事件
        clear.setOnClickListener(new clickListener());

    }

    public interface ClickListenerInterface {
        void doCancel();
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    //监听事件
    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //取消弹窗按钮
                case R.id.sign_in_dialog_one_clear:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }
}
