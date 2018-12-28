package cn.gmuni.sc.network.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GsonTypeAdapter extends TypeAdapter<Object> {
    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        Logger.i("dsdsds");
    }

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token){
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()){
                    list.add(read(in));
                }
                in.endArray();
                return list;
            case BEGIN_OBJECT:

                Map<String, Object> map = new HashMap<String, Object>();
                in.beginObject();
                while (in.hasNext())
                {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER:

                /**
                 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                 */
                double dbNum = in.nextDouble();

                // 数字超过long的最大值，返回浮点类型
                if (dbNum > Long.MAX_VALUE)
                {
                    return dbNum;
                }

                //判断是否是int类型
                int intNum = (int) dbNum;
                if(intNum == dbNum){
                    return intNum;
                }

                // 判断数字是否为整数值
                long lngNum = (long) dbNum;
                if (dbNum == lngNum)
                {
                    return lngNum;
                } else
                {
                    return dbNum;
                }

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }
}
