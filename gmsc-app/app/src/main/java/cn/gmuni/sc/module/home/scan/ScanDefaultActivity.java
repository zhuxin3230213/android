package cn.gmuni.sc.module.home.scan;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.StringUtil;

public class ScanDefaultActivity extends BaseCommonActivity {


    @BindView(R.id.scan_default_content)
    TextView scan_default_content;
    @BindView(R.id.scan_default_button)
    TextView scan_default_button;


    private String scanResult;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scan_default);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //获取扫描结果
        scanResult = getIntent().getStringExtra("scanResult");
        if (!StringUtil.isEmpty(scanResult)) {
            scan_default_content.setText(scanResult);
        } else {
            scan_default_content.setText("");
        }
        //点击事件
        scan_default_button.setOnClickListener(new MyListener());
    }

    @Override
    public int getToolbar() {
        return R.id.default_toolbar;
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.scan_default_button:
                    //将文本内容放到系统剪贴板里。
                    ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(scanResult);
                    MToast.showShortToast("复制成功");
                    break;
            }
        }
    }
}
