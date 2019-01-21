package cn.gmuni.sc.widget.listview.radiolistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.widget.listview.radiolistview.adapter.RadioListViewAdapter;

public class CustomRadioListView extends ListView {

    private ArrayList<HashMap<String,Object>> data;

    private RadioListViewAdapter adapter = null;



    public CustomRadioListView(Context context) {
        super(context);

    }

    public CustomRadioListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CustomRadioListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public void setData(ArrayList<HashMap<String,Object>> data,String selected){
        if(selected==null || "".equals(selected)){
            selected = data.get(0).get("value").toString();
        }
        for(Map<String,Object> mp : data){
            if(mp.get("value").equals(selected)){
                mp.put("__checked",true);
            }else{
                mp.put("__checked",false);
            }
        }
        if(this.data!=null){

        }else{
            this.data = data;
            adapter = new RadioListViewAdapter(getContext(), data, R.layout.widget_custom_listview_radio_item);
            this.setAdapter(adapter);
        }

    }

    public void setData(ArrayList<HashMap<String,Object>> data){
        this.setData(data,null);
    }



    public HashMap<String,Object> getSelectData(){
        for(HashMap<String,Object> mp : this.data){
            if((Boolean)mp.get("__checked")){
                return mp;
            }
        }
        return null;
    }

}
