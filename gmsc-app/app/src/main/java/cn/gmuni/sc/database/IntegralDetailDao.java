package cn.gmuni.sc.database;

import java.util.List;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.greendao.db.IntegralDetailEntityDao;
import cn.gmuni.sc.model.entities.IntegralDetailEntity;

/**
 * @Author: zhuxin
 * @Date: 2018/11/2 10:12
 * @Description:
 */
public class IntegralDetailDao {

    private IntegralDetailEntityDao integralDetailEntityDao;

    public IntegralDetailDao() {
        this.integralDetailEntityDao = BaseApplication.getInstance().getDaoSession().getIntegralDetailEntityDao();
    }

    //保存或者更新积分详情
    public boolean saveOrUpdate(IntegralDetailEntity integralDetailEntity) {
        return this.integralDetailEntityDao.insertOrReplace(integralDetailEntity) > 0;
    }

    //根据任务编码\用户、学校与年月日查询
    public IntegralDetailEntity listByTaskCode(String taskCode,String userInfo,String campus,String inYear,String inMonth,String inDay){
         return integralDetailEntityDao.queryBuilder().where(IntegralDetailEntityDao.Properties.TaskCode.eq(taskCode),
                  IntegralDetailEntityDao.Properties.UserInfo.eq(userInfo),IntegralDetailEntityDao.Properties.Campus.eq(campus),
                  IntegralDetailEntityDao.Properties.InYear.eq(inYear),IntegralDetailEntityDao.Properties.InMonth.eq(inMonth),
                  IntegralDetailEntityDao.Properties.InDay.eq(inDay)).unique();
    }

    //根据任务编码、用户、学校、年月查询本月列表
    public List<IntegralDetailEntity> listByMonth(String taskCode,String userInfo,String campus,String inYear,String inMonth){
               return integralDetailEntityDao.queryBuilder().where(IntegralDetailEntityDao.Properties.TaskCode.eq(taskCode),
                        IntegralDetailEntityDao.Properties.UserInfo.eq(userInfo),IntegralDetailEntityDao.Properties.Campus.eq(campus),
                        IntegralDetailEntityDao.Properties.InYear.eq(inYear),IntegralDetailEntityDao.Properties.InMonth.eq(inMonth)).list();
    }


    //更新
    public void update(IntegralDetailEntity integralDetailEntity) {
        integralDetailEntityDao.update(integralDetailEntity);
    }

    //删除
    public void deleteAll() {
       integralDetailEntityDao.deleteAll();
    }
}
