package cn.gmuni.sc.widget.listview.radiolistview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.widget.listview.adapter.ListViewAdapter;
import cn.gmuni.sc.widget.listview.adapter.ViewHolder;

public class RadioListViewAdapter extends ListViewAdapter<HashMap<String,Object>> {

    private ArrayList<HashMap<String,Object>> datas;


    public RadioListViewAdapter(Context context, ArrayList<HashMap<String, Object>> datas, int layoutId) {
        super(context, datas, layoutId);
        this.datas = datas;
    }

    @Override
    public void convert(ViewHolder holder, HashMap<String, Object> stringObjectMap) {
        RadioButton view =  holder.getView(R.id.radio_button);
        view.setText(stringObjectMap.get("text").toString());
        boolean checked = (Boolean) stringObjectMap.get("__checked");
        view.setChecked(checked);
        if(checked){
            view.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.icon_choose_big,0);
        }else{
            view.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        RadioButton radioButton = view.findViewById(R.id.radio_button);
        radioButton.setOnClickListener(v -> {
            for(Map<String,Object> ma : datas){
                ma.put("__checked",false);
            }
            datas.get(position).put("__checked",true);
            RadioListViewAdapter.this.notifyDataSetChanged();
        });
        return view;
    }

}
