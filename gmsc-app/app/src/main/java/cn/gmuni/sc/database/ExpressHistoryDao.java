package cn.gmuni.sc.database;

import java.sql.SQLException;
import java.util.List;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.greendao.db.ExpressHistoryEntityDao;
import cn.gmuni.sc.model.entities.ExpressHistoryEntity;

/**
 * @Author: zhuxin
 * @Date: 2018/9/25 09:39
 * @Description:
 */
public class ExpressHistoryDao {
    private ExpressHistoryEntityDao expressHistoryEntityDao;

    public ExpressHistoryDao() {
         this.expressHistoryEntityDao = BaseApplication.getInstance().getDaoSession().getExpressHistoryEntityDao();
    }

    //保存或更新物流信息
    public boolean saveOrUpdate(ExpressHistoryEntity expressHistoryEntity){
        return this.expressHistoryEntityDao.insertOrReplace(expressHistoryEntity)>0;
    }

    //获取物流信息列表
    public List<ExpressHistoryEntity> historyList(){
        return expressHistoryEntityDao.loadAll();
    }

    //根据公司code查询列表
    public ExpressHistoryEntity historyListByCode(String code,String expNo,String userInfo){
       return expressHistoryEntityDao.queryBuilder().where(ExpressHistoryEntityDao.Properties.Code.eq(code),
               ExpressHistoryEntityDao.Properties.ExpNo.eq(expNo),
               ExpressHistoryEntityDao.Properties.UserInfo.eq(userInfo)).unique();
    }

    //更改
    public void update(ExpressHistoryEntity expressHistoryEntity){
        expressHistoryEntityDao.update(expressHistoryEntity);
    }
    //删除所有
    public void deleteAll() {
        expressHistoryEntityDao.deleteAll();
    }
}
