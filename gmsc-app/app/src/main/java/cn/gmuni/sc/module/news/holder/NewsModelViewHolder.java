package cn.gmuni.sc.module.news.holder;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.NewsModel;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/8/27 09:41
 * @Description:
 */
public class NewsModelViewHolder extends BaseViewHolder<NewsModel> {

    private TextView news_content;
    private TextView newspaper_name;
    private TextView news_create_time;
    private CustomImageView news_image;
    private RelativeLayout viewLayout;

    public NewsModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final NewsModel model, int position, MultiTypeAdapter adapter) {
        //1图，2行
        news_content = (TextView) getView(R.id.module_news_item_listview_image_exist_content);
        newspaper_name = (TextView) getView(R.id.module_news_item_listview_image_exist_newspaper_name);
        news_create_time = (TextView) getView(R.id.module_news_item_listview_image_exist_create_time);
        news_image = (CustomImageView) getView(R.id.module_news_item_listview_image_exist_image);

        //布局文件
        viewLayout = (RelativeLayout) getView(R.id.module_news_item_listview_image_exist_layout);

        //获取正文图片ids
        List<String> ids = Arrays.asList(model.getImageIds().split(","));
        if (ids.size() == 1) {
            news_content.setText(model.getTitle());
            newspaper_name.setText(model.getName());
            news_create_time.setText(model.getUpdateTime());
            String imageOne = ids.get(0);
            String URL = "/api-download/download/" + imageOne;
            ViewGroup.LayoutParams params = news_image.getLayoutParams();
            params.height = PixelUtil.dp2px(71, news_image.getContext());
            params.width = PixelUtil.dp2px(110, news_image.getContext());
            news_image.setLayoutParams(params);
            news_image.setUrl(URL);
            news_image.reload();
        }


        viewLayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(viewLayout.getContext(), BaseWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "/news/newsDetail/" + model.getId());
                intent.putExtras(bundle);
                viewLayout.getContext().startActivity(intent);
            }
        });
    }
}
