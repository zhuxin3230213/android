package cn.gmuni.sc.module.schoolbus.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.gmuni.sc.base.BaseFragment;
import cn.gmuni.sc.model.SchoolBusModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.ISchoolBusApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

/**
 * @Author:ZhuXin
 * @Description:
 * @Date:Create 2018/12/26 11:08
 * @Modified By:
 **/
public class SchoolBusFragment extends BaseFragment {

    CustomRefreshRecycleView weeksListView;
    CustomRefreshRecycleView overWeeksListView;
    MultiTypeAdapter weekAdapter;
    MultiTypeAdapter overAdapter;
    private static final List<Visitable> weeksList = new ArrayList<>();
    private static final List<Visitable> overList = new ArrayList<>();
    private boolean isAddItemDecoration = true;

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
     * 获取渲染周内列表的id
     *
     * @return
     */
    public int getWeeksListId() {
        return 0;
    }

    /**
     * 获取渲染周末列表的id
     *
     * @return
     */
    public int getOverWeeksListId() {
        return 0;
    }

    /**
     * 获取季节 0：通用 1：夏季 2：冬季
     *
     * @return
     */
    public int getSeason() {
        return 0;
    }

    @Override
    public void initView() {
        weeksListView = (CustomRefreshRecycleView) findViewById(getWeeksListId());
        overWeeksListView = (CustomRefreshRecycleView) findViewById(getOverWeeksListId());
        //初始化校区数据
        initSchoolCampusData();
        //适配数据
        weekAdapter = weeksListView.initRecyclerView(weeksList, this.getContext());
        overAdapter = overWeeksListView.initRecyclerView(overList, this.getContext());
        //设置item间距
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            weeksListView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
            overWeeksListView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }
        //周内与周末区分 0：周内 1：周末
        setWeeksType();

    }

    //设置周内与周末类型
    private void setWeeksType() {
        //0：周内 1：周末
        weekAdapter.setTag(0);
        overAdapter.setTag(1);
    }

    //初始化校车数据
    private void initSchoolCampusData() {
        //清除集合缓存
        weeksList.clear();
        overList.clear();
        //获取初始化学校校区数据
        getSchoolCampusData();
    }

    //获取校区列表初始化数据
    private void getSchoolCampusData() {
        showLoading();
        Network.request(Network.createApi(ISchoolBusApi.class).getSchoolCampusList(), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    handleData(data); //处理数据
                } else {
                    showEmpty();
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取校区初始化数据失败");
                hideLoading();
            }
        });
    }

    //处理数据
    private void handleData(List<Map<String, String>> data) {
        weeksList.clear();
        overList.clear();
        for (Map<String, String> temp : data) {
            String[] start = temp.get("startCampus").split(",");
            String[] end = temp.get("endCampus").split(",");
            SchoolBusModel schoolBusModel = new SchoolBusModel();
            schoolBusModel.setStartCampus(start[1]);
            schoolBusModel.setStartCampusName(start[0]);
            schoolBusModel.setEndCampus(end[1]);
            schoolBusModel.setEndCampusName(end[0]);
            schoolBusModel.setSeasonState(getSeason());
            weeksList.add(schoolBusModel);
            overList.add(schoolBusModel);
        }
    }
}
