package cn.gmuni.sc.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.config.Const;
import cn.gmuni.sc.network.OkHttpClientHelper;
import cn.gmuni.sc.widget.CustomDatePicker;
import cn.gmuni.sc.widget.Html5WebView;

/**
 * 系统基础webview容器，可继承使用也可直接使用
 */
public class BaseWebViewActivity extends BaseCommonActivity {

    @BindView(R.id.web_view)
    Html5WebView webView;

    protected String baseUrl;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_base_webview);
        baseUrl = BaseApplication.getInstance().getSysConfig("server.url");
    }

    /**
     * 参数
     * url
     * headers:HashMap<String,String>
     * @param savedInstanceState
     */
    @SuppressLint("JavascriptInterface")
    @Override
    public void initView(Bundle savedInstanceState) {
        //读取配置项
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        Map<String,String> headers = new HashMap<>();
        if(bundle.containsKey("headers")){
            headers = (HashMap<String, String>) bundle.getSerializable("headers");
        }
        if(null == url || "".equals(url)){
            Logger.e("请添加访问地址");
        }else{
            if(!url.startsWith("http:")&& !url.startsWith("https:")){
                if(!url.startsWith("file:///")){
                    url = baseUrl + url;
                }
                HashMap<String, String> mp = OkHttpClientHelper.listRequestHeaders();
                headers.putAll(mp);
                headers.put("Request-Device","app");
                //将token信息添加到cookie中，方便后续使用
                 CookieSyncManager.createInstance(this);
                CookieManager cm = CookieManager.getInstance();
                cm.setAcceptCookie(true);
                cm.setCookie(baseUrl, Const.LOGIN_TOKEN_NAME+"="+mp.get(Const.LOGIN_TOKEN_NAME));
                cm.setCookie(baseUrl,"Request-Device=app");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    CookieManager.getInstance().flush();
                } else {
                    CookieSyncManager.getInstance().sync();
                }
            }
            webView.loadUrl(url,headers);
        }
        webView.addJavascriptInterface(new DatePickerToJs(webView),"datepicker");
        //绑定事件
        bindEvent();
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
        //绑定默认标题
        toolbar.setTitle("正在加载，请稍后...");
    }

    private void bindEvent(){
        webView.setWebChromeClient(new WebChromeClient(){
            //设置webview的标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                toolbar.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.base_webview_toolbar;
    }

    @Override
    public void finish() {
        super.finish();
        ViewGroup layout = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        layout.removeView(webView);
        layout.removeView(toolbar);
        webView.destroy();
    }

    /**
     * 默认支持日期选择组件
     */
    class DatePickerToJs extends Object{

        private Html5WebView webView;

        DatePickerToJs(Html5WebView webView){
            this.webView =webView;
        }

        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        /***
         *
         * @param showSpecificTime 是否显示时分
         * @param flag  回调函数的标记，方便一个页面选择多次日期
         * @param defaultValue  默认值
         */
        @JavascriptInterface
        public void showDatePicker(boolean showSpecificTime,final String flag,String defaultValue){

            CustomDatePicker datePicker = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:selectedDatePicker('"+time+"','"+flag+"')");
                        }
                    });
                    //调用js中的选中日期组件方法
//                   runOnUiThread(new Runnable() {
//                       @Override
//                       public void run() {
//                           webView.loadUrl("javascript:selectedDatePicker("+time+","+flag+")");
//                       }
//                   });
                    //调用js中的选中日期组件方法
//                    webView.evaluateJavascript("javascript:selectedDatePicker("+time+","+flag+")", new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//
//                        }
//                    });
                }
            },now,"2099-12-31 23:59");
            //是否显示时分
            datePicker.showSpecificTime(showSpecificTime);
            datePicker.setIsLoop(true);
            if(defaultValue==null || "".equals(defaultValue)){
                defaultValue = now;
            }
            datePicker.show(defaultValue);
        }
    }
}
