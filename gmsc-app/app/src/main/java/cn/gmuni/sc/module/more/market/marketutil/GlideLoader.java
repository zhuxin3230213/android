package cn.gmuni.sc.module.more.market.marketutil;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @Author:ZhuXin
 * @Description:
 * 创建 图片加载器 (建议使用Glide)
 * @Date:Create 2019/1/15 14:47
 * @Modified By:
 **/
public class GlideLoader implements com.yancy.imageselector.ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
