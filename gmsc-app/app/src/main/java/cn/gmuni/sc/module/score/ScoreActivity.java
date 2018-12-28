package cn.gmuni.sc.module.score;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.widget.listview.adapter.ListViewAdapter;

public class ScoreActivity extends BaseCommonActivity {

    @BindView(R.id.score_list_view)
    ListView listView;

    //学期成绩查询id
    private static final int SCORE_SEMESTER = 1;

    //四六级查询id
    private static final int SCORE_CET = 2;

    //普通话查询id
    private static final int SCORE_PUTONGHUA = 3;

    //计算机等级查询id
    private static final int SCORE_COMPUTER = 4;

    private static  List<Map<String,Object>> data = new ArrayList<>();

    private static ListViewAdapter<Map<String,Object>> listViewAdapter = null;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_score);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        initData();
        bindEvent();

    }

    @Override
    public int getToolbar() {
       return R.id.score_query_toolbar;
    }

    /**
     *初始化数据
     */
    private void initData(){

        Map<String,Object> map = new HashMap<>();
        map.put("title",R.string.score_semester_query);
        map.put("id",SCORE_SEMESTER);
        data.add(map);
        map = new HashMap<>();
        map.put("title",R.string.score_cet_query);
        map.put("id",SCORE_CET);
        data.add(map);
        map = new HashMap<>();
        map.put("title",R.string.score_putonghua_query);
        map.put("id",SCORE_PUTONGHUA);
        data.add(map);
        map = new HashMap<>();
        map.put("title",R.string.score_computer_query);
        map.put("id",SCORE_COMPUTER);
        data.add(map);
        listViewAdapter = new ScoreListViewAdapter(getContext(),data,R.layout.module_score_listview_item);
        listView.setAdapter(listViewAdapter);

    }

    /**
     * 绑定事件
     */
    public void bindEvent(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object> item = data.get(i);
                switch ((Integer) item.get("id")){
                    case SCORE_SEMESTER:
                        MToast.showShortToast("正在开发中，请稍后...");
                        break;
                    case SCORE_CET:
                        startActivity(new Intent(getContext(),CetScoreActivity.class));
                        break;
                    case SCORE_PUTONGHUA:
                        startActivity(new Intent(getContext(),PutonghuaScoreActivity.class));
                        break;
                    case SCORE_COMPUTER:
                        startActivity(new Intent(getContext(),ComputerGradeActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
       Handler handler = new Handler();
       handler.postDelayed(() -> {
           data.clear();
           if(listViewAdapter!=null){
               listViewAdapter.notifyDataSetChanged();
               listView.setAdapter(listViewAdapter);
           }
       },10);
    }
}
