package cn.gmuni.sc.module.lost;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILostFoundApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class LostFoundMineActivity extends BaseCommonActivity implements CustomRefreshRecycleView.OnRefreshListener  {
    private MultiTypeAdapter multiTypeAdapter;
    private final LostFoundMineActivity self = this;

    /**
     * 删除数据编码
     */
    public final static int MINE_RESULT_CODE = 13;
    /**
     * 删除的数据的id缓存器
     */
    private final ArrayList<String> deletedIds = new ArrayList<>();
    /**
     * 删除的数据的id缓存器
     */
    private final ArrayList<String> finishedIds = new ArrayList<>();
    //定义一个标志位，用于标记用户已经将列表拉到底了
    private boolean isLast = false;
    //定义一个标志位，用于标记页面正在加载数据
    private boolean isLoading = false;
    @BindView(R.id.lost_found_list_view_mine)
    CustomRefreshRecycleView listView;
    protected final List<Visitable> dataList = new ArrayList<>();
    protected final Map<String, String> params = new HashMap<>();
    {
        params.put("isMine", "isMine");
    }
    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lost_found_mine);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        listView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(5)));
        //刷新事件
        listView.setOnRefreshListener(self);
        getData();
    }
    public void showEmpty(){
        findViewById(R.id.lost_found_list_view_mine_empty).setVisibility(View.VISIBLE);
    }
    private void getData() {
        showLoading();
        isLoading = true;
        Network.request(Network.createApi(ILostFoundApi.class).list(params), new Network.IResponseListener<List<LostFoundModel>>() {
            @Override
            public void onSuccess(List<LostFoundModel> data) {
                isLoading = false;
                isLast = (data == null ? true : data.size() < 11);
                if(data.size() == 11){
                    data.remove(10);
                }
                if(!data.isEmpty()){
                    dataList.addAll(data);
                    if(null == multiTypeAdapter){
                        multiTypeAdapter = listView.initRefreshRecyclerView(dataList, getContext());
                    }
                }else{
                    showEmpty();
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                isLoading = false;
                hideLoading();
            }
        });
    }
    @Override
    public int getToolbar() {
        return R.id.lost_found_mine_toolbar;
    }
    /**
     * 在调用返回按钮之前处理
     * @return
     */
    @Override
    public void beforeGoBack(){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("deleted_ids", deletedIds);
        bundle.putSerializable("finished_ids", finishedIds);
        intent.putExtras(bundle);
        setResult(MINE_RESULT_CODE, intent);

    }
    @Override
    public void onLoadingMore() {
        if(isLast || isLoading){
            listView.hideFooterView();
            return;
        }
        isLoading = true;
        Map<String, String> newPara = new HashMap<>(params);
        if(dataList.size() > 0){
            newPara.put("createTime", DateUtils.date2String(((LostFoundModel)dataList.get(dataList.size() - 1)).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        newPara.put("queryType","up");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Network.request(Network.createApi(ILostFoundApi.class).list(newPara), new Network.IResponseListener<List<LostFoundModel>>() {
                    @Override
                    public void onSuccess(List<LostFoundModel> data) {
                        isLoading = false;
                        isLast = (data == null ? true : data.size() < 11);
                        if(data.size() == 11){
                            data.remove(10);
                        }
                        dataList.addAll(data);
                        if(null == multiTypeAdapter){
                            multiTypeAdapter = listView.initRefreshRecyclerView(dataList, getContext());
                        }
                        multiTypeAdapter.notifyDataSetChanged();
                        listView.hideFooterView();
                    }

                    @Override
                    public void onFail(int code, String message) {
                        isLoading = false;
                        multiTypeAdapter.notifyDataSetChanged();
                        listView.hideFooterView();
                    }
                });
            }
        }, 5000);
    }

    @Override
    public void onPullDownRefresh() {
        if(isLoading){
            return;
        }
        isLoading = true;
        Map<String, String> newPara = new HashMap<>(params);
        if(dataList.size() > 0){
            newPara.put("createTime", DateUtils.date2String(((LostFoundModel)dataList.get(0)).getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        newPara.put("queryType","down");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Network.request(Network.createApi(ILostFoundApi.class).list(newPara), new Network.IResponseListener<List<LostFoundModel>>() {
                    @Override
                    public void onSuccess(List<LostFoundModel> data) {
                        isLoading = false;
                        dataList.addAll(0, data);
                        if(null == multiTypeAdapter){
                            multiTypeAdapter = listView.initRefreshRecyclerView(dataList, getContext());
                        }
                        multiTypeAdapter.notifyDataSetChanged();
                        listView.hideHeaderView();
                    }

                    @Override
                    public void onFail(int code, String message) {
                        isLoading = false;
                        multiTypeAdapter.notifyDataSetChanged();
                        listView.hideHeaderView();
                    }
                });
            }
        }, 2000);
    }
    public void addDeletedId(String id){
        deletedIds.add(id);
    }
    public void addFinishedId(String id){
        finishedIds.add(id);
    }
}
