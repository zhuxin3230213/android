package cn.gmuni.sc.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.OnClick;
import cn.gmuni.sc.R;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.module.more.market.marketutil.ReportUtil;

/**
 * @Author:ZhuXin
 * @Description:二手交易举报详情弹窗
 * @Date:Create 2019/1/14 17:17
 * @Modified By:
 **/
public class CustomReportDetailDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;

    public CustomReportDetailDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.widget_custom_market_report_detail_dialog, null);
        setContentView(view);
        LinearLayout market_personal_attack = (LinearLayout) findViewById(R.id.market_personal_attack);
        LinearLayout market_pornographic = (LinearLayout) findViewById(R.id.market_pornographic);
        LinearLayout market_spam = (LinearLayout) findViewById(R.id.market_spam);
        LinearLayout market_sensitive_information = (LinearLayout) findViewById(R.id.market_sensitive_information);
        LinearLayout market_sex_vice_violence_terror = (LinearLayout) findViewById(R.id.market_sex_vice_violence_terror);
        LinearLayout market_political_harmful = (LinearLayout) findViewById(R.id.market_political_harmful);
        LinearLayout market_others = (LinearLayout) findViewById(R.id.market_others);
        ImageView report_cancle = (ImageView) findViewById(R.id.report_cancle);
        //事件
        market_personal_attack.setOnClickListener(new ClickListener());
        market_pornographic.setOnClickListener(new ClickListener());
        market_spam.setOnClickListener(new ClickListener());
        market_sensitive_information.setOnClickListener(new ClickListener());
        market_sex_vice_violence_terror.setOnClickListener(new ClickListener());
        market_political_harmful.setOnClickListener(new ClickListener());
        market_others.setOnClickListener(new ClickListener());
        report_cancle.setOnClickListener(new ClickListener());
    }

    public interface ClickListenerInterface {
        void doCancel();

        void doSubmit(String title);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    class ClickListener extends OnMultiClickListener {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.market_personal_attack:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_PERSONAL_ATTACK);
                    break;
                case R.id.market_pornographic:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_PORNOGRAPHIC);
                    break;
                case R.id.market_spam:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_SPAM);
                    break;
                case R.id.market_sensitive_information:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_SENSITIVE_INFORMATION);
                    break;
                case R.id.market_sex_vice_violence_terror:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_SEX_VICE_VIOLENCE_TERROR);
                    break;
                case R.id.market_political_harmful:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_POLITICAL_HARMFUL);
                    break;
                case R.id.market_others:
                    clickListenerInterface.doSubmit(ReportUtil.MARKET_OTHERS);
                    break;
                case R.id.report_cancle:
                    clickListenerInterface.doCancel();
                    break;
            }
        }
    }
}
