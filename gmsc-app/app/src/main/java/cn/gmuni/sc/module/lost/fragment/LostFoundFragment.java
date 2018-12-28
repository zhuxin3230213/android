package cn.gmuni.sc.module.lost.fragment;

import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ILostFoundApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class LostFoundFragment extends BaseFragment implements CustomRefreshRecycleView.OnRefreshListener {
    protected MultiTypeAdapter multiTypeAdapter;
    private final LostFoundFragment self = this;

    CustomRefreshRecycleView listView;
    protected final List<Visitable> dataList = new ArrayList<>();

    protected final Map<String, String> params = new HashMap<>();

    //定义一个标志位，用于标记用户已经将列表拉到底了
    private boolean isLast = false;
    //定义一个标志位，用于标记页面正在加载数据
    private boolean isLoading = false;
    /**
     * 初始化布局
     *
     * @return 布局文件的id。
     */
    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    protected void managerArguments() {

    }

    public void showEmpty(){

    }
    public void hideEmpty(){

    }

    /**
     * 获取渲染列表的id
     *
     * @return
     */
    public int getListId() {
        return 0;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        listView = (CustomRefreshRecycleView) findViewById(getListId());
        listView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(5)));
        //刷新事件
        listView.setOnRefreshListener(self);
        getData();
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
            listView.hideHeaderView();
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
    public void addListItem(LostFoundModel model) {
        hideEmpty();
        dataList.add(0, model);
        if(null == multiTypeAdapter){
            multiTypeAdapter = listView.initRefreshRecyclerView(dataList, getContext());
        }
        multiTypeAdapter.notifyDataSetChanged();
    }

    public void deleteItems(List<String> ids){
        if(null != dataList && dataList.size() > 0 && null != ids && ids.size() > 0){
            for (int i = 0; i < ids.size(); i++) {
                String deletedItemId = ids.get(i);
                for (int j = 0; j < dataList.size(); j++) {
                    LostFoundModel model = (LostFoundModel)dataList.get(j);
                    if(deletedItemId.equals(model.getId())){
                        dataList.remove(j);
                        break;
                    }
                }
            }
            multiTypeAdapter.notifyDataSetChanged();
            if(dataList.size() == 0){
                showEmpty();
            }
        }
    }
    public void finishedItems(List<String> ids){
        if(null != dataList && dataList.size() > 0 && null != ids && ids.size() > 0){
            for (int i = 0; i < ids.size(); i++) {
                String deletedItemId = ids.get(i);
                for (int j = 0; j < dataList.size(); j++) {
                    LostFoundModel model = (LostFoundModel)dataList.get(j);
                    if(deletedItemId.equals(model.getId())){
                        model.setInfoStatus("1");
                        break;
                    }
                }
            }
            multiTypeAdapter.notifyDataSetChanged();
            if(dataList.size() == 0){
                showEmpty();
            }
        }
    }
}
