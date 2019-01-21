package cn.gmuni.sc.database;

import java.util.List;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.greendao.db.NoticeHistoryEntityDao;
import cn.gmuni.sc.model.entities.ExpressHistoryEntity;
import cn.gmuni.sc.model.entities.NoticeHistoryEntity;

/**
 * @Author: zhuxin
 * @Date: 2018/10/19 10:54
 * @Description:
 */
public class NoticeHistroyDao {

    private NoticeHistoryEntityDao noticeHistoryEntityDao;

    public NoticeHistroyDao() {
        this.noticeHistoryEntityDao = BaseApplication.getInstance().getDaoSession().getNoticeHistoryEntityDao();
    }

    //保存通知信息点击后状态
    public  boolean saveOrUpdate(NoticeHistoryEntity noticeHistoryEntity){
       return this.noticeHistoryEntityDao.insertOrReplace(noticeHistoryEntity) >0;
    }

    //获取列表
    public List<NoticeHistoryEntity> historyList(){
        return noticeHistoryEntityDao.loadAll();
    }

    //根据通知id，用户，学校获取通知消息
    public NoticeHistoryEntity noticeById(String noticeId,String  userInfo,String campus){
        return noticeHistoryEntityDao.queryBuilder().where(NoticeHistoryEntityDao.Properties.NoticeId.eq(noticeId),
                NoticeHistoryEntityDao.Properties.UserInfo.eq(userInfo),NoticeHistoryEntityDao.Properties.Campus.eq(campus)).unique();
    }


    //更改
    public void update(NoticeHistoryEntity noticeHistoryEntity){
        noticeHistoryEntityDao.update(noticeHistoryEntity);
    }
    //删除所有
    public void deleteAll() {
        noticeHistoryEntityDao.deleteAll();
    }

}
