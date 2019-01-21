package cn.gmuni.sc.module.me.me_market.fragment;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.model.MarketModel;
import cn.gmuni.sc.model.entities.UserInfoEntity;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IMarketApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.SystemUtils;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2019/1/14 10:37
 * @Modified By:
 **/
public class MeMarketFragment extends BaseFragment implements CustomRefreshRecycleView.OnRefreshListener {
    CustomRefreshRecycleView listView;
    private MultiTypeAdapter multiTypeAdapter;
    private static final List<Visitable> list = new ArrayList<>();
    protected final Map<String, String> params = new HashMap<>();

    private boolean isAddItemDecoration = true;
    //定义一个标志位，用于标记用户已经将列表拉到底了
    private boolean isLast = false;
    //定义一个标志位，用于标记页面正在加载数据
    private boolean isLoading = false;

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    protected void managerArguments() {
    }

    public void showEmpty() {

    }

    public void hideEmpty() {

    }

    /**
     * 获取渲染列表的id
     *
     * @return
     */
    public int getListId() {
        return 0;
    }

    @Override
    public void initView() {
        listView = (CustomRefreshRecycleView) findViewById(getListId());
        UserInfoEntity userInfo = SecurityUtils.getUserInfo();
        params.put("user", userInfo.getPhoneNumber());
        initData(); //初始化数据
        //添加分割线
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            listView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }
        //刷新事件
        listView.setOnRefreshListener(this);
    }

    //初始化数据
    private void initData() {
        list.clear();
        getData(params); //联网获取数据
    }

    //获取数据
    private void getData(Map<String, String> map) {
        isLoading = true;
        showLoading();
        Network.request(Network.createApi(IMarketApi.class).listMarket(map), new Network.IResponseListener<List<MarketModel>>() {
            @Override
            public void onSuccess(List<MarketModel> data) {
                isLoading = false;
                isLast = (data == null ? true : data.size() < 11);
                if (data.size() == 11) {
                    data.remove(10);
                }
                if (!data.isEmpty()) {
                    list.addAll(data);
                    if (null == multiTypeAdapter) {
                        multiTypeAdapter = listView.initRefreshRecyclerView(list, getContext());
                    }
                } else {
                    showEmpty();
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                isLoading = false;
                MToast.showLongToast("获取数据失败");
                hideLoading();
            }
        });
    }

    @Override
    public void onPullDownRefresh() {
        if (isLoading) {
            listView.hideHeaderView();
            return;
        }
        isLoading = true;
        Map<String, String> newPara = new HashMap<>(params);
        if (list.size() > 0) {
            newPara.put("updateTime", ((MarketModel) list.get(0)).getUpdateTime());
        }
        newPara.put("queryType", "down");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Network.request(Network.createApi(IMarketApi.class).listMarket(newPara), new Network.IResponseListener<List<MarketModel>>() {
                    @Override
                    public void onSuccess(List<MarketModel> data) {
                        isLoading = false;
                        list.addAll(0, data);
                        if (null == multiTypeAdapter) {
                            multiTypeAdapter = listView.initRefreshRecyclerView(list, getContext());
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

    @Override
    public void onLoadingMore() {
        if (isLast || isLoading) {
            listView.hideFooterView();
            return;
        }
        isLoading = true;
        Map<String, String> newPara = new HashMap<>(params);
        if (list.size() > 0) {
            newPara.put("updateTime", ((MarketModel) list.get(list.size() - 1)).getUpdateTime());
        }
        newPara.put("queryType", "up");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Network.request(Network.createApi(IMarketApi.class).listMarket(newPara), new Network.IResponseListener<List<MarketModel>>() {
                    @Override
                    public void onSuccess(List<MarketModel> data) {
                        isLoading = false;
                        isLast = (data == null ? true : data.size() < 11);
                        if (data.size() == 11) {
                            data.remove(10);
                        }
                        list.addAll(data);
                        if (null == multiTypeAdapter) {
                            multiTypeAdapter = listView.initRefreshRecyclerView(list, getContext());
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
}
