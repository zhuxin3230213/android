package cn.gmuni.sc.widget.recyclerview.type;

import android.view.View;

import cn.gmuni.sc.R;
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
import cn.gmuni.sc.module.lost.holder.LostFoundViewHolder;
import cn.gmuni.sc.module.me.message.holder.MessageModelViewHolder;
import cn.gmuni.sc.module.more.express.holder.ExpressCompanyIndexViewHolder;
import cn.gmuni.sc.module.more.express.holder.ExpressCompanyNameViewHolder;
import cn.gmuni.sc.module.more.express.holder.HistoryViewHolder;
import cn.gmuni.sc.module.more.express.holder.ResultViewHolder;
import cn.gmuni.sc.module.more.express.holder.SuggestionViewHolder;
import cn.gmuni.sc.module.more.gprs.holder.GprsModelViewHolder;
import cn.gmuni.sc.module.news.holder.InforTipsModelViewHolder;
import cn.gmuni.sc.module.news.holder.NewsMoreImageModelViewHolder;
import cn.gmuni.sc.module.news.holder.NewsOneModelViewHolder;
import cn.gmuni.sc.module.news.holder.NewsModelViewHolder;
import cn.gmuni.sc.module.news.holder.NewsOneRowModelViewHolder;
import cn.gmuni.sc.module.notice.holder.NoticeModelViewHolder;
import cn.gmuni.sc.module.schoolbus.holder.SchoolBusModelViewHolder;
import cn.gmuni.sc.widget.recyclerview.holder.BaseViewHolder;

/**
 * @Author: zhuxin
 * @Date: 2018/8/27 09:31
 * @Description:
 */
public class TypeFactoryForList implements TypeFactory {

    //多个布局文件
    public static final int layoutId_news_image = R.layout.module_news_item_listview_image_exist;
    public static final int layoutId_news_one_row_image = R.layout.module_news_one_row_image;
    public static final int layoutId_news_item_listview_image_exist_more = R.layout.module_news_item_listview_image_exist_more;
    public static final int layoutId_news_image_notexist = R.layout.module_news_item_listview_image_notexist;
    public static final int layoutId_notice = R.layout.fragment_notice_item_listview;
    public static final int layoutId_lost_found = R.layout.module_lost_found_item_listview;
    public static final int layoutId_express_company_item = R.layout.module_express_company_item_listview;
    public static final int layoutId_express_result_item_list = R.layout.module_express_result_item_list;
    public static final int layoutId_express_history_item_listview = R.layout.module_express_history_item_listview;
    public static final int layoutId_express_company_name_item = R.layout.module_express_company_name_item;
    public static final int layoutId_express_company_index_item = R.layout.module_express_company_index_item;
    public static final int layoutId_widget_loading_custom_listview_tip = R.layout.widget_loading_custom_listview_tip;
    public static final int layoutId_message_all_item = R.layout.fragment_message_all_list_view_item;
    public static final int layoutId_gprs_item = R.layout.module_gprs_list_view_item;
    public static final int layputId_school_bus_item = R.layout.fragment_school_bus_list_view_item;

    @Override
    public int type(NewsModel noticeNews) {
        return layoutId_news_image;
    }

    @Override
    public int type(NewsMoreImageModel newsMoreImageModel) {
        return layoutId_news_item_listview_image_exist_more;
    }

    @Override
    public int type(NewsOneRowModel newsOneRowModel) {
        return layoutId_news_one_row_image;
    }

    @Override
    public int type(NewsOneModel noticeNewsOne) {
        return layoutId_news_image_notexist;
    }

    @Override
    public int type(NoticeModel noticeModel) {
        return layoutId_notice;
    }

    @Override
    public int type(LostFoundModel lostFoundModel) {
        return layoutId_lost_found;
    }

    @Override
    public int type(CompanyModel companyModel) {
        return layoutId_express_company_item;
    }

    @Override
    public int type(ResultModel resultModel) {
        return layoutId_express_result_item_list;
    }

    @Override
    public int type(HistoryModel historyModel) {
        return layoutId_express_history_item_listview;
    }

    @Override
    public int type(CompanyListModel companyListModel) {
        return layoutId_express_company_name_item;
    }

    @Override
    public int type(CompanyIndexModel companyIndexModel) {
        return layoutId_express_company_index_item;
    }

    @Override
    public int type(InforTipsModel inforTipsModel) {
        return layoutId_widget_loading_custom_listview_tip;
    }

    @Override
    public int type(MessageModel messageModel) {
        return layoutId_message_all_item;
    }

    @Override
    public int type(GprsModel gprsModel) {
        return layoutId_gprs_item;
    }

    @Override
    public int type(SchoolBusModel schoolBusModel) {
        return layputId_school_bus_item;
    }

    @Override
    public BaseViewHolder createViewHolder(int type, View itemView) {
        if (layoutId_news_image == type) {
            return new NewsModelViewHolder(itemView);
        } else if (layoutId_news_image_notexist == type) {
            return new NewsOneModelViewHolder(itemView);
        } else if (layoutId_notice == type) {
            return new NoticeModelViewHolder(itemView);
        } else if (layoutId_lost_found == type) {
            return new LostFoundViewHolder(itemView);
        } else if (layoutId_express_company_item == type) {
            return new SuggestionViewHolder(itemView);
        } else if (layoutId_express_result_item_list == type) {
            return new ResultViewHolder(itemView);
        } else if (layoutId_express_history_item_listview == type) {
            return new HistoryViewHolder(itemView);
        } else if (layoutId_express_company_name_item == type) {
            return new ExpressCompanyNameViewHolder(itemView);
        } else if (layoutId_express_company_index_item == type) {
            return new ExpressCompanyIndexViewHolder(itemView);
        } else if (layoutId_news_item_listview_image_exist_more == type) {
            return new NewsMoreImageModelViewHolder(itemView);
        } else if (layoutId_news_one_row_image == type) {
            return new NewsOneRowModelViewHolder(itemView);
        } else if (layoutId_widget_loading_custom_listview_tip == type) {
            return new InforTipsModelViewHolder(itemView);
        } else if (layoutId_message_all_item == type) {
            return new MessageModelViewHolder(itemView);
        } else if (layoutId_gprs_item == type) {
            return new GprsModelViewHolder(itemView);
        } else if (layputId_school_bus_item == type) {
            return new SchoolBusModelViewHolder(itemView);
        }
        return null;
    }
}
