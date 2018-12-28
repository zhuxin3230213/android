package cn.gmuni.sc.module.news.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.NewsOneRowModel;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/10/12 09:06
 * @Description:
 */
public class NewsOneRowModelViewHolder extends BaseViewHolder<NewsOneRowModel> {


    private TextView module_news_item_listview_image_exist_one_row_and_one_image_content;
    private CustomImageView module_news_item_listview_image_exist_one_row_and_one_image_image;
    private TextView module_news_item_listview_image_exist_one_row_and_one_image_newspaper_name;
    private TextView module_news_item_listview_image_exist_one_row_and_one_image_create_time;

    private RelativeLayout layout; //初始隐藏（1图，1行）

    public NewsOneRowModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(NewsOneRowModel model, int position, MultiTypeAdapter adapter) {
        module_news_item_listview_image_exist_one_row_and_one_image_content = (TextView) getView(R.id.module_news_item_listview_image_exist_one_row_and_one_image_content);
        module_news_item_listview_image_exist_one_row_and_one_image_image = (CustomImageView) getView(R.id.module_news_item_listview_image_exist_one_row_and_one_image_image);
        module_news_item_listview_image_exist_one_row_and_one_image_newspaper_name = (TextView) getView(R.id.module_news_item_listview_image_exist_one_row_and_one_image_newspaper_name);
        module_news_item_listview_image_exist_one_row_and_one_image_create_time = (TextView) getView(R.id.module_news_item_listview_image_exist_one_row_and_one_image_create_time);

        layout = (RelativeLayout) getView(R.id.module_news_item_listview_image_exist_one_row_and_one_image);

        //获取正文图片ids
        List<String> ids = Arrays.asList(model.getImageIds().split(","));
        if (ids.size() == 1) { //1行，1图
            module_news_item_listview_image_exist_one_row_and_one_image_content.setText(model.getTitle());
            module_news_item_listview_image_exist_one_row_and_one_image_newspaper_name.setText(model.getName());
            module_news_item_listview_image_exist_one_row_and_one_image_create_time.setText(model.getUpdateTime());
            String imageOne = ids.get(0);
            String url = "/api-download/download/" + imageOne;
            module_news_item_listview_image_exist_one_row_and_one_image_image.setUrl(url);
            module_news_item_listview_image_exist_one_row_and_one_image_image.reload();
        }


        layout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(layout.getContext(), BaseWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "/news/newsDetail/" + model.getId());
                intent.putExtras(bundle);
                layout.getContext().startActivity(intent);
            }
        });


    }
}
