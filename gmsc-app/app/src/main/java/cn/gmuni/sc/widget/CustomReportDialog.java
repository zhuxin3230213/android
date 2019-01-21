package cn.gmuni.sc.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cn.gmuni.sc.R;
import cn.gmuni.sc.utils.MToast;

/**
 * @Author:ZhuXin
 * @Description: 二手交易举报弹窗
 * @Date:Create 2019/1/14 16:14
 * @Modified By:
 **/
public class CustomReportDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;

    public CustomReportDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    //初始化布局
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_custom_market_report_dialog, null);
        setContentView(view);
        LinearLayout market_report = (LinearLayout) findViewById(R.id.market_report);
        LinearLayout market_report_cancle = (LinearLayout) findViewById(R.id.market_report_cancle);
        //事件
        market_report.setOnClickListener(new ClickListener());
        market_report_cancle.setOnClickListener(new ClickListener());
    }

    public interface ClickListenerInterface {
        void doCancel();

        void doSubmit();
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    //监听事件
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.market_report:
                    clickListenerInterface.doSubmit();
                    break;
                case R.id.market_report_cancle:
                    clickListenerInterface.doCancel();
                    break;

            }
        }
    }
}
