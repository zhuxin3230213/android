package cn.gmuni.sc.module.more.express;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.database.ExpressHistoryDao;
import cn.gmuni.sc.database.UserInfoDao;
import cn.gmuni.sc.model.entities.ExpressHistoryEntity;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.model.express.ResultModel;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IExpressQueryApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomEmptyDataView;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class ExpressResultActivity extends BaseCommonActivity {


    @BindView(R.id.express_result_logo)
    CustomImageView express_result_logo;
    @BindView(R.id.express_result_company_name)
    TextView express_result_company_name;
    @BindView(R.id.express_result_exp_number)
    TextView express_result_exp_number;
    @BindView(R.id.express_result_copyright)
    TextView express_result_copyright;
    @BindView(R.id.express_result_copyright_one)
    TextView express_result_copyright_one;
    @BindView(R.id.express_result_list)
    CustomRefreshRecycleView listView;

    @BindView(R.id.express_result_bottom)
    FrameLayout express_result_bottom;
    @BindView(R.id.express_result_error_empty)
    CustomEmptyDataView express_result_error_empty;
    @BindView(R.id.express_result_no_data_empty)
    CustomEmptyDataView express_result_no_data_empty;

    private static final List<Visitable> list = new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;

    private String expNo;
    private CompanyModel companyModel;
    private ExpressHistoryDao expressHistoryDao = new ExpressHistoryDao();
    private UserInfoDao userInfoDao = new UserInfoDao();

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_express_result);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String hisCache = getIntent().getStringExtra("hisCache");
        if (!StringUtil.isEmpty(hisCache)) {
            Intent intent = new Intent(getContext(), ExpressQueryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("hisCache", hisCache);
            intent.putExtras(bundle);
            this.setResult(1, intent);  //设置resultCode 为1
        }
        expNo = getIntent().getStringExtra("expNo"); //获取物流单号
        expNo = expNo.replaceAll("\\s*", ""); //去除大部分空白字符,不限空格(调用快递100接口有此问题)
        companyModel = (CompanyModel) getIntent().getSerializableExtra("company"); //获取公司信息
        initData(expNo, companyModel);
        multiTypeAdapter = listView.initRecyclerView(list, ExpressResultActivity.this);
    }

    @Override
    public int getToolbar() {
        return R.id.express_result_toolbar;
    }

    private void initData(String expNo, CompanyModel companyModel) {
        list.clear();
        if (companyModel != null) {
            String url = "http://www.kuaidi100.com/images/all/" + companyModel.getLogo();
            express_result_logo.setUrl(url);
            express_result_logo.reload();
            express_result_company_name.setText(companyModel.getName());
        }
        express_result_exp_number.setText("订单号：" + expNo);
        Map<String, String> map = new HashMap<>();
        map.put("expNo", expNo);
        map.put("expCode", companyModel.getCode());
        getData(map);
    }

    private void getData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(IExpressQueryApi.class).expressQuery(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    handleData(data);
                    Collections.reverse(list);
                    hideLoading();
                    savaOrUpdateHistory(data);//保存物流信息
                } else {
                    //顺丰速运 SF\ 百世快递 HTKY(测试后快递鸟暂时支持，而文档中说明不支持) \申通快递 STO \天天快递 HHTT 快递鸟接口暂不支持查询
                    if ("STO".equals(companyModel.getCode()) || "SF".equals(companyModel.getCode())
                            || "HHTT".equals(companyModel.getCode()) || "HTKY".equals(companyModel.getCode())) {
                        initDataOneH(companyModel.getCode()); //继续从快递100接口中获取数据
                    } else {
                        hideLoading();
                        express_result_bottom.setVisibility(View.GONE);
                        express_result_no_data_empty.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
                hideLoading();
                express_result_bottom.setVisibility(View.GONE);
                express_result_error_empty.setVisibility(View.VISIBLE);
            }
        });
    }

    //快递100初始化数据
    private void initDataOneH(String expCode) {
        Map<String, String> map = new HashMap<>();
        map.put("expNo", expNo);
        getDataOneH(map, expCode);

    }

    //获取快递100数据
    private void getDataOneH(Map<String, String> map, String expCode) {
        Network.request(Network.createApi(IExpressQueryApi.class).expressByExpNoWithOneH(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data != null) {
                    if (data.size() != 0) {
                        //快递100接口单号识别出的公司编号与APP端所选公司编号是否匹配，如匹配则渲染数据，如果不匹配则返回空数据
                        if ("shentong".equals(data.get(0).get("expCode"))) {//申通
                            if ("STO".equals(expCode)) {
                                toShow(data);
                            } else {
                                toHide();
                            }
                        } else if ("huitongkuaidi".equals(data.get(0).get("expCode"))) {//百世
                            if ("HTKY".equals(expCode)) {
                                toShow(data);
                            } else {
                                toHide();
                            }
                        } else if ("tiantian".equals(data.get(0).get("expCode"))) {//天天
                            if ("HHTT".equals(expCode)) {
                                toShow(data);
                            } else {
                                toHide();
                            }
                        } else if ("shunfeng".equals(data.get(0).get("expCode"))) {//顺丰
                            if ("SF".equals(expCode)) {
                                toShow(data);
                            } else {
                                toHide();
                            }
                        } else {
                            toHide();
                        }

                    } else {
                        toHide();
                    }
                } else {
                    toHide();
                }

            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
                hideLoading();
                express_result_bottom.setVisibility(View.GONE);
                express_result_error_empty.setVisibility(View.VISIBLE);
            }
        });
    }

    //处理快递鸟返回数据
    private void handleData(List<Map<String, String>> data) {
        for (Map<String, String> temp : data) {
            ResultModel resultModel = new ResultModel();
            resultModel.setCurrentTime(temp.get("acceptTime").substring(11, 16));
            resultModel.setCurrentDate(temp.get("acceptTime").substring(0, 10));
            resultModel.setDetail(temp.get("information"));
            list.add(resultModel);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }

    //快递100返回数据显示
    private void toShow(List<Map<String, String>> data) {
        toHandleOneData(data); //处理数据
        hideLoading();
        //数据来源设置
        express_result_copyright.setVisibility(View.GONE);
        express_result_copyright_one.setVisibility(View.VISIBLE);
        savaOrUpdateHistory(data);//保存物流信息
    }

    //快递100返回数据隐藏
    private void toHide() {
        hideLoading();
        express_result_bottom.setVisibility(View.GONE);
        express_result_no_data_empty.setVisibility(View.VISIBLE);
    }

    //遍历循环渲染数据
    private void toHandleOneData(List<Map<String, String>> data) {
        for (Map<String, String> temp : data) {
            ResultModel resultModel = new ResultModel();
            resultModel.setCurrentTime(temp.get("acceptTime").substring(11, 16));
            resultModel.setCurrentDate(temp.get("acceptTime").substring(0, 10));
            resultModel.setDetail(temp.get("information"));
            list.add(resultModel);
            multiTypeAdapter.notifyDataSetChanged();
        }
    }

    //保存历史记录至SQLLite
    private void savaOrUpdateHistory(List<Map<String, String>> data) {
        expNo = expNo.replaceAll("\\s*", "");
        if (data.size() != 0) {
            ExpressHistoryEntity getByCode = expressHistoryDao.historyListByCode(companyModel.getCode(), expNo, userInfoDao.get().getPhoneNumber());
            if (getByCode == null) {
                ExpressHistoryEntity expressHistoryEntity = new ExpressHistoryEntity();
                expressHistoryEntity.setCompanyLogo(companyModel.getLogo());
                expressHistoryEntity.setCurrentDate(data.get(data.size() - 1).get("acceptTime").substring(0, 10));
                expressHistoryEntity.setExpNo(expNo);
                expressHistoryEntity.setCompanyName(companyModel.getName());
                expressHistoryEntity.setDetail(data.get(data.size() - 1).get("information")); //获取最后一条物流信息
                expressHistoryEntity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                expressHistoryEntity.setCode(companyModel.getCode());
                expressHistoryEntity.setUserInfo(userInfoDao.get().getPhoneNumber());
                expressHistoryEntity.setCampus(userInfoDao.get().getSchoolName());
                if (3 == Integer.parseInt(data.get(data.size() - 1).get("state"))) {
                    expressHistoryEntity.setState(Extras.SIGN_IN);
                } else {
                    expressHistoryEntity.setState("");
                }
                expressHistoryDao.saveOrUpdate(expressHistoryEntity);
            } else {
                getByCode.setCurrentDate(data.get(data.size() - 1).get("acceptTime").substring(0, 10));
                getByCode.setDetail(data.get(data.size() - 1).get("information")); //获取最后一条物流信息
                if (3 == Integer.parseInt(data.get(data.size() - 1).get("state"))) { //更改状态
                    getByCode.setState(Extras.SIGN_IN);
                }
                expressHistoryDao.update(getByCode);
                //expressHistoryDao.deleteAll(); //清空所有历史记录
            }

        }

    }

}
