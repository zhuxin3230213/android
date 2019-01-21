package cn.gmuni.sc.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import cn.gmuni.sc.config.SharedPreferenceConst;

public class SharedPreferencesHelper {

    private static Context context = null;

    private SharedPreferencesHelper(){}

    protected static void init(Context ctx){
        context = ctx;
    }

    public static Editor build(String name,int mode){
        return new Editor(context,name,mode);
    }

    public static Editor build(String name){
        return build(name,Context.MODE_PRIVATE);
    }

    public static Editor build(){
        return build(SharedPreferenceConst.SHARE_DEFAULT_NAMESPACE);
    }

    /**
     * 清理所有保存的数据
     */
    public static void clear(){
        build(SharedPreferenceConst.SHARE_LOGIN_NAMESPACE).clear();
        build(SharedPreferenceConst.SHARE_DEFAULT_NAMESPACE).clear();
    }


    public static class Editor{
        private SharedPreferences.Editor editor;

        private SharedPreferences sharedPreferences;

        Editor(Context context,String name,int mode){
            this.sharedPreferences = context.getSharedPreferences(name,mode);
            this.editor = this.sharedPreferences.edit();
        }

        /**
         * 保存数据的方法，拿到数据保存数据的基本类型，然后根据类型调用不同的保存方法
         *
         * @param key
         * @param object
         */
        public Editor put(String key,Object object){
            if (object instanceof String) {
                editor.putString(key, (String) object);
            } else if (object instanceof Integer) {
                editor.putInt(key, (Integer) object);
            } else if (object instanceof Boolean) {
                editor.putBoolean(key, (Boolean) object);
            } else if (object instanceof Float) {
                editor.putFloat(key, (Float) object);
            } else if (object instanceof Long) {
                editor.putLong(key, (Long) object);
            } else {
                editor.putString(key, object.toString());
            }
            editor.apply();
            return this;
        }

        /**
         * 获取保存数据的方法，我们根据默认值的到保存的数据的具体类型，然后调用相对于的方法获取值
         *
         * @param key           键的值
         * @param defaultObject 默认值
         * @return
         */
        public  Object get(String key, Object defaultObject) {
            if (defaultObject instanceof String) {
                return sharedPreferences.getString(key, (String) defaultObject);
            } else if (defaultObject instanceof Integer) {
                return sharedPreferences.getInt(key, (Integer) defaultObject);
            } else if (defaultObject instanceof Boolean) {
                return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
            } else if (defaultObject instanceof Float) {
                return sharedPreferences.getFloat(key, (Float) defaultObject);
            } else if (defaultObject instanceof Long) {
                return sharedPreferences.getLong(key, (Long) defaultObject);
            } else {
                return sharedPreferences.getString(key, null);
            }
        }

        /**
         * 移除某个key值已经对应的值
         * @param key
         * @return
         */
        public Editor remove(String key){
            editor.remove(key);
            editor.apply();
            return this;
        }

        /**
         * 清除所有的数据
         * @return
         */
        public  Editor clear(){
            editor.clear();
            editor.apply();
            return this;
        }

        /**
         * 查看某个key值是否存在
         * @param name
         * @return
         */
        public boolean contains(String name){
            return sharedPreferences.contains(name);
        }


        /**
         * 返回所有的键值对
         * @return
         */
        public Map<String,?> getAll(){
            return sharedPreferences.getAll();
        }

    }

}


