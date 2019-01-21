package cn.gmuni.sc.module.more.market.holder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.MarketModel;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.module.more.market.MarketOneselfDetailActivity;
import cn.gmuni.sc.module.more.market.MarketOthersDetailActivity;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CircleImageView;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2019/1/9 13:10
 * @Modified By:
 **/
public class MarketModelViewHolder extends BaseViewHolder<MarketModel> {

    CircleImageView market_list_item_user_img; //头像
    TextView market_list_item_nick_name; //昵称
    TextView market_list_item_update_time; //更新时间
    TextView market_list_item_campus; //学校
    TextView market_list_item_price; //价格
    TextView market_list_item_title; //标题
    CustomImageView market_list_view_item_image_one; //图1
    CustomImageView market_list_view_item_image_two; //图2
    CustomImageView market_list_view_item_image_three; //图3
    TextView market_list_item_message_count; //留言统计

    RelativeLayout market_list_view_item_layout;
    UserInfoEntity userInfo = SecurityUtils.getUserInfo();

    public MarketModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(MarketModel model, int position, MultiTypeAdapter adapter) {
        market_list_item_user_img = (CircleImageView) getView(R.id.market_list_item_user_img);
        market_list_item_nick_name = (TextView) getView(R.id.market_list_item_nick_name);
        market_list_item_update_time = (TextView) getView(R.id.market_list_item_update_time);
        market_list_item_campus = (TextView) getView(R.id.market_list_item_campus);
        market_list_item_price = (TextView) getView(R.id.market_list_item_price);
        market_list_item_title = (TextView) getView(R.id.market_list_item_title);
        market_list_view_item_image_one = (CustomImageView) getView(R.id.market_list_view_item_image_one);
        market_list_view_item_image_two = (CustomImageView) getView(R.id.market_list_view_item_image_two);
        market_list_view_item_image_three = (CustomImageView) getView(R.id.market_list_view_item_image_three);
        market_list_item_message_count = (TextView) getView(R.id.market_list_item_message_count);

        market_list_view_item_layout = (RelativeLayout) getView(R.id.market_list_view_item_layout);
        //头像
        if (!StringUtil.isEmpty(model.getAvatar())) {
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(model.getAvatar());
            market_list_item_user_img.setImageBitmap(snapshotBm);
        }
        market_list_item_nick_name.setText(model.getUserName());
        market_list_item_update_time.setText(model.getUpdateTime());
        market_list_item_campus.setText(model.getSchoolName());
        if ("0".equals(model.getSaleStatus())) {
            market_list_item_price.setText(model.getPrice());
        } else {
            market_list_item_price.setText("已出售");
        }
        market_list_item_title.setText(model.getTitle());
        if (!StringUtil.isEmpty(model.getImgS())) {
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(model.getImgS());
            market_list_view_item_image_one.setImageBitmap(snapshotBm);
        }

        if (userInfo.getPhoneNumber().equals(model.getIndentifier())) {
            market_list_view_item_layout.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    Intent intent = new Intent(market_list_view_item_layout.getContext(), MarketOneselfDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("marketModel", model);
                    intent.putExtras(bundle);
                    market_list_view_item_layout.getContext().startActivity(intent);
                }
            });
        } else {
            market_list_view_item_layout.setOnClickListener(new OnMultiClickListener() {
                @Override
                public void onMultiClick(View v) {
                    Intent intent = new Intent(market_list_view_item_layout.getContext(), MarketOthersDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("marketModel", model);
                    intent.putExtras(bundle);
                    market_list_view_item_layout.getContext().startActivity(intent);
                }
            });
        }


    }
}
