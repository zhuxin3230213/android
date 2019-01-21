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
import cn.gmuni.sc.model.NewsMoreImageModel;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/10/11 16:58
 * @Description:
 */
public class NewsMoreImageModelViewHolder extends BaseViewHolder<NewsMoreImageModel> {

    private TextView module_news_item_listview_image_exist_more_row_and_three_more_image_content;
    private CustomImageView module_news_item_listview_image_exist_more_row_and_three_more_image_one;
    private CustomImageView module_news_item_listview_image_exist_more_row_and_three_more_image_two;
    private CustomImageView module_news_item_listview_image_exist_more_row_and_three_more_image_more;
    private TextView module_news_item_listview_image_exist_more_row_and_three_more_image_newspaper_name;
    private TextView module_news_item_listview_image_exist_more_row_and_three_more_image_create_time;
    private RelativeLayout layout; //初始隐藏(3图)

    public NewsMoreImageModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(NewsMoreImageModel model, int position, MultiTypeAdapter adapter) {
        //2\3图
        module_news_item_listview_image_exist_more_row_and_three_more_image_content = (TextView) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image_content);
        module_news_item_listview_image_exist_more_row_and_three_more_image_one = (CustomImageView) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image_one);
        module_news_item_listview_image_exist_more_row_and_three_more_image_two = (CustomImageView) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image_two);
        module_news_item_listview_image_exist_more_row_and_three_more_image_more = (CustomImageView) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image_more);
        module_news_item_listview_image_exist_more_row_and_three_more_image_newspaper_name = (TextView) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image_newspaper_name);
        module_news_item_listview_image_exist_more_row_and_three_more_image_create_time = (TextView) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image_create_time);
        //获取正文图片ids
        List<String> ids = Arrays.asList(model.getImageIds().split(","));
        if (ids.size() == 2) {
            module_news_item_listview_image_exist_more_row_and_three_more_image_more.setVisibility(View.INVISIBLE);
            module_news_item_listview_image_exist_more_row_and_three_more_image_content.setText(model.getTitle());
            module_news_item_listview_image_exist_more_row_and_three_more_image_newspaper_name.setText(model.getName());
            module_news_item_listview_image_exist_more_row_and_three_more_image_create_time.setText(model.getUpdateTime());
            String imageOne = ids.get(0);
            String imageTwo = ids.get(1);

            String urlOne = "/api-download/download/" + imageOne;
            String urlTwo = "/api-download/download/" + imageTwo;
            module_news_item_listview_image_exist_more_row_and_three_more_image_one.setUrl(urlOne);
            module_news_item_listview_image_exist_more_row_and_three_more_image_two.setUrl(urlTwo);
            module_news_item_listview_image_exist_more_row_and_three_more_image_one.reload();
            module_news_item_listview_image_exist_more_row_and_three_more_image_two.reload();

        } else if (ids.size() >= 3) {
            module_news_item_listview_image_exist_more_row_and_three_more_image_content.setText(model.getTitle());
            module_news_item_listview_image_exist_more_row_and_three_more_image_newspaper_name.setText(model.getName());
            module_news_item_listview_image_exist_more_row_and_three_more_image_create_time.setText(model.getUpdateTime());
            String imageOne = ids.get(0);
            String imageTwo = ids.get(1);
            String imageThree = ids.get(2);
            String urlOne = "/api-download/download/" + imageOne;
            String urlTwo = "/api-download/download/" + imageTwo;
            String urlThree = "/api-download/download/" + imageThree;
            module_news_item_listview_image_exist_more_row_and_three_more_image_one.setUrl(urlOne);
            module_news_item_listview_image_exist_more_row_and_three_more_image_two.setUrl(urlTwo);
            module_news_item_listview_image_exist_more_row_and_three_more_image_more.setUrl(urlThree);
            module_news_item_listview_image_exist_more_row_and_three_more_image_one.reload();
            module_news_item_listview_image_exist_more_row_and_three_more_image_two.reload();
            module_news_item_listview_image_exist_more_row_and_three_more_image_more.reload();
        }

        layout = (RelativeLayout) getView(R.id.module_news_item_listview_image_exist_more_row_and_three_more_image);
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
