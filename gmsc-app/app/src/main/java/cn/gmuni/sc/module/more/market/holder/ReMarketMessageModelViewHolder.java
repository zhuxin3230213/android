package cn.gmuni.sc.module.more.market.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.model.ReMarketMessageModel;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CircleImageView;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2019/1/17 13:53
 * @Modified By:
 **/
public class ReMarketMessageModelViewHolder extends BaseViewHolder<ReMarketMessageModel> {

    CircleImageView re_market_message_list_item_user_img; //头像
    TextView re_market_message_list_item_nick_name; //昵称
    TextView re_market_message_list_item_update_time; //创建时间
    TextView re_market_message_list_item_campus; //学校
    TextView re_market_message_list_item_title; //留言内容

    public ReMarketMessageModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(ReMarketMessageModel model, int position, MultiTypeAdapter adapter) {
        re_market_message_list_item_user_img = (CircleImageView) getView(R.id.re_market_message_list_item_user_img);
        re_market_message_list_item_nick_name = (TextView) getView(R.id.re_market_message_list_item_nick_name);
        re_market_message_list_item_update_time = (TextView) getView(R.id.re_market_message_list_item_update_time);
        re_market_message_list_item_campus = (TextView) getView(R.id.re_market_message_list_item_campus);
        re_market_message_list_item_title = (TextView) getView(R.id.re_market_message_list_item_title);

        //头像
        if (!StringUtil.isEmpty(model.getAvatar())) {
            Bitmap snapshotBm = BitMapUtil.base64ToBitmap(model.getAvatar());
            re_market_message_list_item_user_img.setImageBitmap(snapshotBm);
        }
        re_market_message_list_item_nick_name.setText(model.getUserName());
        re_market_message_list_item_update_time.setText(model.getCreateTime());
        re_market_message_list_item_campus.setText(model.getSchoolName());
        re_market_message_list_item_title.setText(model.getMessage());
    }
}
