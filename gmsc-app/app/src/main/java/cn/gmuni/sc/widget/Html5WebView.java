package cn.gmuni.sc.widget;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.orhanobut.logger.Logger;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.utils.webviewutils.NetStatusUtil;

public class Html5WebView extends WebView {

    private Context context;

    private ProgressBar progressBar;

    //自定义client
    private WebChromeClient customChromeClient = null;

    private WebViewClient customWebviewClient = null;

    private boolean loadError = false;

    public Html5WebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Html5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init(){
        //顶部进度条的初始化
        progressBar = new ProgressBar(context,null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,8);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setProgress(R.drawable.web_progress_bar_states);
        addView(progressBar);

        //配置网页信息
        WebSettings settings = getSettings();
        //支持网页缩放
        settings.setSupportZoom(true);
        //支持js使用
        settings.setJavaScriptEnabled(true);

        //缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);

        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("utf-8");
        //将图片调整到适合webview的大小
        settings.setUseWideViewPort(true);
        //保存数据
        saveData(settings);

        //处理多窗口问题
        newWin(settings);

        super.setWebChromeClient(chromeClient);

        super.setWebViewClient(webViewClient);

        //TODO 下載文件，待完善
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                BaseActivity activity = (BaseActivity) context;
                activity.requestPermission(PermissionConst.PERMISSION_WRITE_EXTERNAL_STORAGE, new OnPermissionListener() {
                    @Override
                    public void onReceive() {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        request.setAllowedOverRoaming(false);//漫游网络是否可以下载
                        String CookieStr= CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("Cookie",CookieStr);
                        //设置文件类型，可以在下载结束后自动打开该文件
                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
                        request.setMimeType(mimeString);

                        //在通知栏中显示，默认就是显示的
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        request.setVisibleInDownloadsUi(true);

                        //sdcard的目录下的download文件夹，必须设置
                        request.setDestinationInExternalPublicDir("/download/", "2.png");
                        //request.setDestinationInExternalFilesDir(),也可以自己制定下载路径

                        //将下载请求加入下载队列
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        //加入下载队列后会给该任务返回一个long型的id，
                        //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
                        long mTaskId = downloadManager.enqueue(request);
                    }

                });

             }
        });
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        this.customChromeClient = client;
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        this.customWebviewClient = webViewClient;
    }


    /**
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置

        if (NetStatusUtil.isConnected(context)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = context.getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }

    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private WebChromeClient chromeClient = new WebChromeClient(){

        //处理多窗口问题
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            //WebView有一个getHitTestResult():返回的是一个HitTestResult，一般会根据打开的链接的类型，返回一个extra的信息，如果打开链接不是一个url，或者打开的链接是JavaScript的url，他的类型是UNKNOWN_TYPE，这个url就会通过requestFocusNodeHref(Message)异步重定向。返回的extra为null，或者没有返回extra。根据此方法的返回值，判断是否为null，可以用于解决网页重定向问题。
            HitTestResult result = view.getHitTestResult();
            String data = result.getExtra();
            view.loadUrl(data);
            return true;
        }

        //顶部进度条进度更新
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                //gone不可占用空间 invisible 不可见占用空间
                progressBar.setVisibility(GONE);
            }else{
                if(progressBar.getVisibility() == GONE){
                    progressBar.setVisibility(VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view,newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //如果返回的没有error，则此方法无效，待处理 TODO
            if(!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")){
                loadError = true;
            }
            if(customChromeClient!=null){
                customChromeClient.onReceivedTitle(view,title);
            }else{
                super.onReceivedTitle(view, title);
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            CustomDialog dialog = new CustomDialog(context,"温馨提示",message,true);
            dialog.show();
            dialog.setOnOkDialogListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    result.confirm();
                    dialog.close();
                }
            });
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            CustomDialog dialog = new CustomDialog(context,"温馨提示",message);
            dialog.show();
            dialog.setOnOkDialogListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    result.confirm();
                    dialog.close();
                }
            });
            dialog.setOnCancelDialogListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    result.cancel();
                    dialog.close();
                }
            });
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            CustomDialog dialog = new CustomDialog(context,message,R.layout.fragment_js_prompt);
            dialog.show();
            dialog.setOnOkDialogListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    CustomEditText editText = v.findViewById(R.id.prompt);
                    result.confirm(editText.getText().toString());
                    dialog.close();
                }
            });
            dialog.setOnCancelDialogListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    result.confirm("");
                    dialog.close();
                }
            });
            return true;
        }
    };

    //在页面滚动时调整进度条位置
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp =  new LayoutParams(progressBar.getLayoutParams());
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            if(url==null){
                return false;
            }
            if(url.startsWith("ws://")
                    ||url.startsWith("weixin://")
                    ||url.startsWith("alipays://")
                    ||url.startsWith("ctrip://")
                    ||url.startsWith("tel://")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                try{
                    context.startActivity(intent);
                    return true;
                }catch (Exception e){
                    return true;
                }
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(loadError){
                view.loadUrl("about:blank");
                view.loadUrl("file:///android_asset/web/404.html");
                loadError = false;
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        //android 6.0以上处理404 500 等信息的方式
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            String url = request.getUrl().getPath();
            Logger.i("链接:"+url+"无法访问");
            if(url.indexOf("favicon.ico")!=-1){
                return;
            }
            loadError = true;
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String[] files = new String[]{"zepto.min.js","simJQ-1.4.min.js"};
            String url = request.getUrl().toString();
            for(String file : files){
                if(url.contains(file)){
                    try {
                        return new WebResourceResponse("application/x-javascript","utf-8",context.getAssets().open("web/js/"+file));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            return super.shouldInterceptRequest(view, request);
        }
    };
}
