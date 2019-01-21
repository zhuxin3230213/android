package cn.gmuni.sc.module.more.express;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.express.CompanyIndexModel;
import cn.gmuni.sc.model.express.CompanyListModel;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomImageView;
import cn.gmuni.sc.widget.IndexBar;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class ExpressCompanyActivity extends BaseCommonActivity implements IndexBar.OnIndexChangedListener, TextWatcher {


    @BindView(R.id.express_company_search)
    CustomImageView express_company_search;
    @BindView(R.id.express_company_edit_text)
    EditText express_company_edit_text;
    @BindView(R.id.express_company_index)
    IndexBar express_company_index;
    @BindView(R.id.express_company_indicator)
    TextView express_company_indicator;
    @BindView(R.id.express_company)
    CustomRefreshRecycleView listView;
    @BindView(R.id.express_company_search_name)
    CustomRefreshRecycleView searchList;
    @BindView(R.id.express_company_center)
    LinearLayout express_company_center;
    @BindView(R.id.express_company_include_list)
    FrameLayout express_company_include_list;
    @BindView(R.id.express_company_clear)
    CustomImageView express_company_clear;
    @BindView(R.id.express_company_heart)
    CustomImageView express_company_heart;
    @BindView(R.id.express_company_common_use)
    TextView express_company_common_use;

    private MultiTypeAdapter multiTypeAdapter;
    private static final List<Visitable> list = new ArrayList<>();
    private List<CompanyListModel> companyList = new ArrayList<>();

    private String expNo;
    private boolean isAddItemDecoration = true;
    private boolean isCompanyAddItemDecoration = true;
    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_express_company);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        expNo = getIntent().getStringExtra("expNo");
        initData(); //初始化数据
        //点击侧边栏
        express_company_index.setOnIndexChangedListener(this);
        express_company_edit_text.addTextChangedListener(this);
        express_company_clear.setOnClickListener(new MyListener());
    }

    @Override
    public int getToolbar() {
        return R.id.express_company_toolbar;
    }


    private void initData() {
        list.clear();
        readCompany(); //获取物流公司集合
        multiTypeAdapter = listView.initRecyclerView(list, ExpressCompanyActivity.this);
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            listView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }
        multiTypeAdapter.setTag(expNo);
        multiTypeAdapter.notifyDataSetChanged();
    }


    private void readCompany() {
        try {
            InputStream is = getAssets().open("company.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(json).getAsJsonArray();
            for (JsonElement obj : jArray) {
                CompanyListModel companyListModel = gson.fromJson(obj, CompanyListModel.class);
                companyList.add(companyListModel); //91条
                if (StringUtil.isEmpty(companyListModel.getCode())) {
                    CompanyIndexModel companyIndexModel = new CompanyIndexModel();
                    companyIndexModel.setCompanyIndex(companyListModel.getName());
                    list.add(companyIndexModel);
                } else {
                    list.add(companyListModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onIndexChanged(String index, boolean isDown) {
        int position = -1;
        //23个对应
        for (CompanyListModel company : companyList) {

            if (TextUtils.equals(company.getName(), index)) {
                position = companyList.indexOf(company);
                break;
            }
            if ("常用".equals(index)) {
                position = 0;
            }
        }
        if (position != -1) {
            listView.moveToPosition(position); //跳转指定位置到top
            //listView.getLayoutManager().scrollToPosition(position); //跳转指定位置到bottom
        }
        express_company_indicator.setText(index);
        express_company_indicator.setVisibility(isDown ? View.VISIBLE : View.GONE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(final Editable s) {
        if (s.length() > 0) {
            CompanyListModel model = new CompanyListModel();
            express_company_center.setVisibility(View.GONE);
            express_company_include_list.setVisibility(View.GONE);
            express_company_clear.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.VISIBLE);
            list.clear();
            readCompanyName(s); //获取名称
            multiTypeAdapter = searchList.initRecyclerView(list, ExpressCompanyActivity.this);
            if (isCompanyAddItemDecoration){
                isCompanyAddItemDecoration = false;
                searchList.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
            }
            multiTypeAdapter.setTag(expNo);
        } else {
            express_company_center.setVisibility(View.VISIBLE);
            express_company_include_list.setVisibility(View.VISIBLE);
            express_company_clear.setVisibility(View.GONE);
            searchList.setVisibility(View.GONE);
            list.clear();
            initData();
        }
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.express_company_clear:
                    express_company_edit_text.setText("");
                    break;
            }
        }
    }

    //获取物流公司名称
    private void readCompanyName(Editable s) {
        try {
            InputStream is = getAssets().open("company.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(json).getAsJsonArray();
            for (JsonElement obj : jArray) {
                CompanyListModel companyListModel = gson.fromJson(obj, CompanyListModel.class);
                if (companyListModel.getName().contains(s)) {
                    list.add(companyListModel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
