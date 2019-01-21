package cn.gmuni.sc.module.me.message;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.MessageModel;
import cn.gmuni.sc.module.me.message.fragment.MessageFragment;

public class MessageNoActivity extends BaseCommonActivity {


    @BindView(R.id.message_no_title_name)
    TextView message_no_title_name; //消息类型名
    @BindView(R.id.message_no_content_detail)
    TextView message_no_content_detail; //消息正文
    @BindView(R.id.message_no_publisher_name)
    TextView message_no_publisher_name; //消息发布者

    private MessageModel messageModel;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message_no);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        messageModel = (MessageModel) getIntent().getSerializableExtra("message");
        message_no_title_name.setText("通知:");
        message_no_content_detail.setText(messageModel.getContent());
        message_no_publisher_name.setText(messageModel.getDeptName() + ": " + messageModel.getPublisher());
        //设置返回后操作(表明已阅读此条消息)
        Intent intent = new Intent(getContext(), MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("isClick", "true");
        intent.putExtras(bundle);
        this.setResult(MessageActivity.AFTER_CLICK_STATE, intent);
    }


    @Override
    public int getToolbar() {
        return R.id.message_no_toolbar;
    }
}
