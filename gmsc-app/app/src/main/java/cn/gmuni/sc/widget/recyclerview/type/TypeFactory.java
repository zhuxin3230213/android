package cn.gmuni.sc.widget.recyclerview.type;

import android.view.View;

import cn.gmuni.sc.model.GprsModel;
import cn.gmuni.sc.model.InforTipsModel;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.model.MessageModel;
import cn.gmuni.sc.model.NewsModel;
import cn.gmuni.sc.model.NewsMoreImageModel;
import cn.gmuni.sc.model.NewsOneModel;
import cn.gmuni.sc.model.NewsOneRowModel;
import cn.gmuni.sc.model.NoticeModel;
import cn.gmuni.sc.model.SchoolBusModel;
import cn.gmuni.sc.model.express.CompanyIndexModel;
import cn.gmuni.sc.model.express.CompanyListModel;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.model.express.HistoryModel;
import cn.gmuni.sc.model.express.ResultModel;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/8/27 09:25
 * @Description:
 *当列表中增加类型时：
1.为该类型创建实现了Visitable接口的Model类
2.创建继承于BaseViewHolder的ViewHolder（与Model类对应）
3.为TypeFactory增加type方法（与Model类对应） ，同时TypeFactoryForList 实现该方法
4.为TypeFactoryForList增加与列表类型对应的资源ID参数
5.修改TypeFactoryForList 中的createViewHolder方法
 */
public interface TypeFactory {
    //新闻
    int type(NewsModel noticeNews);
    int type(NewsMoreImageModel newsMoreImageModel);
    int type(NewsOneRowModel newsOneRowModel);
    int type(NewsOneModel noticeNewsOne);
    //通知
    int type(NoticeModel noticeModel);
    //失物招领
    int type(LostFoundModel lostFoundModel);
    //快递查询
    int type(CompanyModel companyModel);
    int type(ResultModel resultModel);
    int type(HistoryModel historyModel);
    int type(CompanyListModel companyListModel);
    int type(CompanyIndexModel companyIndexModel);
    //刷新组件尾部提示
    int type(InforTipsModel inforTipsModel);

    //我的消息
    int type(MessageModel messageModel);
    //位置信息
    int type(GprsModel gprsModel);
    //校车查询
    int type(SchoolBusModel schoolBusModel);
    BaseViewHolder createViewHolder(int type, View itemView);
}
