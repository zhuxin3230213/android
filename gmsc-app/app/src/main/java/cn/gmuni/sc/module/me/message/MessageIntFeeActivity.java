package cn.gmuni.sc.module.me.message;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.MessageModel;

public class MessageIntFeeActivity extends BaseCommonActivity {

    @BindView(R.id.message_fee_amount)
    TextView message_fee_amount; //出账金额
    @BindView(R.id.message_int_fee_type_name)
    TextView message_int_fee_type_name; //交易类型：支出
    @BindView(R.id.message_int_fee_time_detail)
    TextView message_int_fee_time_detail; //交易时间
    @BindView(R.id.message_int_fee_order_detail)
    TextView message_int_fee_order_detail; //交易单号
    @BindView(R.id.message_int_fee_note_detail)
    TextView message_int_fee_note_detail; //备注：网络缴费

    private MessageModel messageModel;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message_int_fee);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        messageModel = (MessageModel) getIntent().getSerializableExtra("message");
        message_fee_amount.setText(messageModel.getTotalAmount());
        if ("0".equals(messageModel.getNetType())) {
            //交易类型 0：支出 1：收入
            message_int_fee_type_name.setText("支出");
        }
        message_int_fee_time_detail.setText(messageModel.getTime());
        message_int_fee_order_detail.setText(messageModel.getTradeNo());
        message_int_fee_note_detail.setText("网络缴费");
        //设置返回后操作
        Intent intent = new Intent(getContext(), MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("isClick", "true");
        intent.putExtras(bundle);
        this.setResult(MessageActivity.AFTER_CLICK_STATE, intent);
    }

    @Override
    public int getToolbar() {
        return R.id.message_int_fee_toolbar;
    }
}
