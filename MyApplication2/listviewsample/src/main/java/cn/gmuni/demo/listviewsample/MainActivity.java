package cn.gmuni.demo.listviewsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //适配器
      /*  setContentView(R.layout.arrayadapter_layout);
        ListView listView = findViewById(R.id.listview);
        // 定义一个数组
        final String[] books = {"初识Android开发", "Android初级开发", "Android中级开发",
                "Android高级开发", "Android开发进阶", "Android项目实战", "Android企业级开发"};
        // 将数组包装成ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, books);
        // 为ListView设置Adapter
        listView.setAdapter(adapter);

        // 为ListView绑定列表项点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Toast.makeText(MainActivity.this,"点击了" + books[i],Toast.LENGTH_SHORT).show();
            }
        });
*/


        //ListActivity和自定义列表项
    /*    setContentView(R.layout.custom_item_layout);
        // 获取界面ListView组件
        ListView listView =  findViewById(R.id.listview);
        // 定义一个List集合
        final List<String> components = new ArrayList<>();
        components.add("TextView");
        components.add("EditText");
        components.add("Button");
        components.add("CheckBox");
        components.add("RadioButton");
        components.add("ToggleButton");
        components.add("ImageView");
        // 将List包装成ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_item, R.id.content_tv, components);

        // 为ListView设置Adapter
        listView.setAdapter(adapter);
        // 为ListView列表项绑定点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, components.get(i),
                        Toast.LENGTH_SHORT).show();



            }
        });

*/
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);


    }


    /**
     * 创建一个List集合，其元素为Map
     *
     * @return 返回列表项的集合对象
     */

    protected List<Map<String, Object>> getData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("img", R.drawable.pause);
        map.put("title", "小宗");
        map.put("info", "电台DJ");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.pause);
        map.put("title", "貂蝉");
        map.put("info", "四大美女");

        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.pause);
        map.put("title", "奶茶");
        map.put("info", "清纯妹妹");

        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.pause);
        map.put("title", "大黄");
        map.put("info", "是小狗");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("img", R.drawable.pause);
        map.put("title", "hello");
        map.put("info", "every thing");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("img", R.drawable.pause);
        map.put("title", "world");
        map.put("info", "hello world");

        list.add(map);

        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
