package cn.gmuni.sc.database;

import java.util.List;

import cn.gmuni.sc.app.BaseApplication;
import cn.gmuni.sc.greendao.db.UserInfoEntityDao;
import cn.gmuni.sc.model.entities.UserInfoEntity;

public class UserInfoDao {

    private UserInfoEntityDao dao;

    public UserInfoDao(){
        this.dao = BaseApplication.getInstance().getDaoSession().getUserInfoEntityDao();
    }

    /**
     * 保存或更新用户信息
     * @param entity
     * @return
     */
    public boolean saveOrUpdate(UserInfoEntity entity){
        return this.dao.insertOrReplace(entity)>0;
    }

    public boolean save(UserInfoEntity entity){
        return this.dao.insert(entity)>0;
    }

    public void delete(String ...ids){
         this.dao.deleteByKeyInTx(ids);
    }

    public void deleteAll(){
        this.dao.deleteAll();
    }

    /**
     * 获取用户信息
     * @return
     */
    public UserInfoEntity get(){
        List<UserInfoEntity> entities = this.dao.queryBuilder().list();
        if(entities!=null && entities.size()>0){
            return entities.get(0);
        }
        return null;
    }

}
