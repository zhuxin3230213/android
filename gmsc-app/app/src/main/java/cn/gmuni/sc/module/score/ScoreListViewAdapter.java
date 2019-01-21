package cn.gmuni.sc.module.score;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import cn.gmuni.sc.R;
import cn.gmuni.sc.widget.listview.adapter.ListViewAdapter;
import cn.gmuni.sc.widget.listview.adapter.ViewHolder;

/**
 * 查询分数主界面
 */
public class ScoreListViewAdapter extends ListViewAdapter<Map<String,Object>> {
    public ScoreListViewAdapter(Context context, List<Map<String, Object>> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, Map<String, Object> stringObjectMap) {
        ((TextView)holder.getView(R.id.score_title)).setText((Integer) stringObjectMap.get("title"));
    }

}
