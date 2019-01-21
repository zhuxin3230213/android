package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    private Context context;

    /**
     *  <attr name="url" format="string"></attr>
     <attr name="placeholder" format="reference"></attr>
     <attr name="show_placeholder" format="boolean"></attr>
     <attr name="error_placeholder" format="boolean"></attr>
     <attr name="as_gif" format="boolean"></attr>
     <attr name="use_cache" format="boolean"></attr>
     */
    //访问地址
    private String url;

    //是否启用缓存
    private boolean isUseCache = false;

    //占位图
    private int placeholder = 0;

    //错误占位图
    private int errorPlaceholder = 0;

    //是否显示占位图
    private boolean showPlaceHolder = false;


    private boolean preload = false;

    //请求头
    private List<Map<String,String>> headers = new ArrayList<>();

    private CustomImageView current;

    /**
     * 需要的属性
     * url:访问地址,如果不是以http/https开头，需加上系统baseUrl,BaseApplication.getInstance().getSysConfig("server.url");
     * placeholder:正在加载显示图片
     * showPlaceholder:是否显示正在加载等待
     * errorPlaceholder:图片加载错误显示效果
     * asGif:判断是否是gif
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    public CustomImageView(Context context) {
        super(context);
        this.context = context;
        current = this;
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }





    public void init(AttributeSet attrs){
        current = this;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        url = typedArray.getString(R.styleable.CustomImageView_url);
        //如果设置了url，则加载图片
            //TODO 需要添加默认placeholder
            placeholder = typedArray.getResourceId(R.styleable.CustomImageView_placeholder,R.drawable.loading_image);
            errorPlaceholder = typedArray.getResourceId(R.styleable.CustomImageView_error_placeholder,R.drawable.loading_image);
            showPlaceHolder = typedArray.getBoolean(R.styleable.CustomImageView_show_placeholder,true);
            if(null!=null && !"".equals(url)){
                load();
            }
            this.setScaleType(ScaleType.FIT_XY);
    }

    private void load(){
        RequestOptions options = new RequestOptions();
        if(placeholder!=0 && showPlaceHolder){
            options =options.placeholder(placeholder);
        }
        if(errorPlaceholder!=0){
            options = options.error(errorPlaceholder);
        }
        if(isUseCache){
//            options = options.diskCacheStrategy(DiskCacheStrategy.);
        }
        //不使用内存缓存，只缓存原图，否则会有问题
        options.skipMemoryCache(false);

        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if(url!=null &&!"".equals(url)){
            //添加请求头信息
                LazyHeaders.Builder builder = new LazyHeaders.Builder();
                for(Map<String,String> header : headers){
                    builder.addHeader(header.get("name"),header.get("value"));
                }
                builder.addHeader("Custom-Operator","download");
                GlideUrl glideUrl = new GlideUrl(getUrl(url),builder.build());
            Glide.with(this.context).load(glideUrl).apply(options).into(this);
        }else{
            //TODO 提醒添加url
            return;
        }

    }

    private String getUrl(String url){
        if(url.startsWith("http")||url.startsWith("https")){
            return url;
        }
        return BaseApplication.getInstance().getSysConfig("server.url")+url;
    }

    public void reload(){
        load();
    }

    public void reload(String url){
        this.url = url;
        this.load();
    }

    public CustomImageView setUrl(String url){
        this.url = url;
        return this;
    }

    public CustomImageView setPlaceloader(int placeholder){
        this.placeholder = placeholder;
        return this;
    }

    public CustomImageView setErrorPlaceloader(int errorPlaceholder){
        this.errorPlaceholder = errorPlaceholder;
        return this;
    }

    public CustomImageView showPlaceholder(boolean showPlaceHolder){
        this.showPlaceHolder = showPlaceHolder;
        return this;
    }

    public CustomImageView useCache(boolean isUseCache){
        this.isUseCache = isUseCache;
        return this;
    }

    //添加头信息
    public CustomImageView addHeader(String name,String value){
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        map.put("value",value);
        headers.add(map);
        return this;
    }
}
