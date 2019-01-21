package cn.gmuni.sc.module.news.holder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.NewsOneModel;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/8/27 09:43
 * @Description:
 */
public class NewsOneModelViewHolder extends BaseViewHolder<NewsOneModel> {


    private TextView news_content;
    private TextView newspaper_name;
    private TextView news_create_time;
    private RelativeLayout viewLayout;

    public NewsOneModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final NewsOneModel model, int position, final MultiTypeAdapter adapter) {

        news_content = (TextView) getView(R.id.module_news_item_listview_image_notexist_content);
        newspaper_name = (TextView) getView(R.id.module_news_item_listview_image_notexist_newspaper_name);
        news_create_time = (TextView) getView(R.id.module_news_item_listview_image_notexist_create_time);
        viewLayout = (RelativeLayout) getView(R.id.module_news_item_listview_image_notexist_layout);

        newspaper_name.setText(model.getName());
        news_create_time.setText(model.getUpdateTime());
        news_content.setText(model.getTitle());
        
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
