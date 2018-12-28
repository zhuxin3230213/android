package cn.gmuni.sc.module.notice.holder;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.UUID;

import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseActivity;
import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.base.BaseWebViewActivity;
import cn.gmuni.sc.database.NoticeHistroyDao;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.greendao.db.NoticeHistoryEntityDao;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.NoticeModel;
import cn.gmuni.sc.model.entities.NoticeHistoryEntity;
import cn.gmuni.sc.module.more.express.constants.RequestCode;
import cn.gmuni.sc.network.api.INoticeApi;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/8/31 15:33
 * @Description:
 */
public class NoticeModelViewHolder extends BaseViewHolder<NoticeModel> {


    private TextView notice_title;
    private TextView notice_create_time;
    private TextView notice_content;
    private TextView notice_circular_round;
    private RelativeLayout viewLayout;
    private NoticeHistroyDao noticeHistroyDao = new NoticeHistroyDao();
    private UserInfoDao userInfoDao = new UserInfoDao();

    public NoticeModelViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(final NoticeModel model, int position, MultiTypeAdapter adapter) {
        notice_title = (TextView) getView(R.id.notice_title);
        notice_create_time = (TextView) getView(R.id.notice_create_time);
        notice_content = (TextView) getView(R.id.notice_content);
        notice_circular_round = (TextView) getView(R.id.notice_circular_round);

        notice_title.setText(model.getTitle());
        notice_content.setText(model.getContent());
        notice_create_time.setText(model.getUpdateTime());
        notice_circular_round.setVisibility(View.GONE);
        viewLayout = (RelativeLayout) getView(R.id.fragment_notice_item_listview_layout);

        viewLayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //notice_circular_round.setVisibility(View.GONE);
                Intent intent = new Intent(viewLayout.getContext(), BaseWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "/notice/noticeDetail/" + model.getId());
                intent.putExtras(bundle);
                viewLayout.getContext().startActivity(intent);
               // saveOrUpdate(model); //保存通知阅读状态
            }
        });


    }

    private void saveOrUpdate(NoticeModel model){
        NoticeHistoryEntity noticeHistoryEntity1 = noticeHistroyDao.noticeById(model.getId(), userInfoDao.get().getPhoneNumber(), userInfoDao.get().getSchoolName());
        NoticeHistoryEntity noticeHistoryEntity = new NoticeHistoryEntity();
        if (noticeHistoryEntity1 == null){
            noticeHistoryEntity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            noticeHistoryEntity.setNoticeId(model.getId());
            noticeHistoryEntity.setUserInfo(userInfoDao.get().getPhoneNumber());
            noticeHistoryEntity.setCampus(userInfoDao.get().getSchoolName());
            noticeHistoryEntity.setIsCheck(true);
            noticeHistroyDao.saveOrUpdate(noticeHistoryEntity);
        }else {
            noticeHistoryEntity.setIsCheck(model.isCheck());
            noticeHistroyDao.update(noticeHistoryEntity);
        }
    }
}
