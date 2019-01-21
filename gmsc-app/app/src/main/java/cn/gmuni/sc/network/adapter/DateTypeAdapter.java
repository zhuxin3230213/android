package cn.gmuni.sc.network.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

import cn.gmuni.sc.utils.DateUtils;

/**
 * 将日期格式化为字符串
 */
public class DateTypeAdapter implements JsonSerializer<Date> , JsonDeserializer{

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(DateUtils.date2String(src,DateUtils.COMMON_DATETIME));
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Date date = DateUtils.string2Date(json.toString().replaceAll("\"",""), DateUtils.COMMON_DATETIME);
        if(null == date){
            date = DateUtils.string2Date(json.toString().replaceAll("\"",""), DateUtils.LONG_DATE);
        }
        return date;
    }
}
