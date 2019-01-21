package cn.gmuni.sc.base;

import android.content.Intent;

public class ReturnedWebViewActivity extends BaseWebViewActivity {
    private static final int PAGE_RESULT = 0XFF;
    /**
     * 在调用返回按钮之前处理
     * @return
     */
    @Override
    public void beforeGoBack(){
        Intent intent = new Intent();
        setResult(PAGE_RESULT, intent);
    }
}
