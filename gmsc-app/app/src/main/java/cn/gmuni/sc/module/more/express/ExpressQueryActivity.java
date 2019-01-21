package cn.gmuni.sc.module.more.express;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.listeners.OnPermissionListener;
import cn.gmuni.sc.config.PermissionConst;
import cn.gmuni.sc.database.ExpressHistoryDao;
import cn.gmuni.sc.model.entities.ExpressHistoryEntity;
import cn.gmuni.sc.model.express.CompanyModel;
import cn.gmuni.sc.model.express.HistoryModel;
import cn.gmuni.sc.module.more.express.constants.Extras;
import cn.gmuni.sc.module.more.express.constants.RequestCode;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IExpressQueryApi;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomEmptyDataView;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class ExpressQueryActivity extends BaseCommonActivity implements TextWatcher {


    @BindView(R.id.express_query_edit_text)
    EditText express_query_edit_text;
    @BindView(R.id.express_query_scan)
    ImageView express_query_scan;
    @BindView(R.id.express_query_clear)
    ImageView express_query_clear;
    @BindView(R.id.express_query_company)
    CustomRefreshRecycleView listView;
    @BindView(R.id.express_query_choose_other_company)
    TextView express_query_choose_other_company;
    @BindView(R.id.express_history_title)
    TextView express_history_title;
    @BindView(R.id.express_history_empty)
    CustomEmptyDataView express_history_empty;
    @BindView(R.id.express_history_list)
    CustomRefreshRecycleView historyList;


    private Map<String, CompanyModel> companyMap = new HashMap<>();

    private static final List<Visitable> list = new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;
    private MultiTypeAdapter historyAdapter;
    private ExpressHistoryDao expressHistoryDao = new ExpressHistoryDao();

    private boolean isPause;
    private String stringExtra;
    private boolean isAddItemDecoration = true;
    private boolean isCompanyAddItemDecoration = true;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_express_query);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //历史记录
        historyData();
        //EditText限制输入表情及输入长度限制
        express_query_edit_text.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(30)});
        //设置文本框监听事件
        express_query_edit_text.addTextChangedListener(this);
        //扫描
        express_query_scan.setOnClickListener(new MyListener());
        //清除编辑框
        express_query_clear.setOnClickListener(new MyListener());
        //选择其他快递公司
        express_query_choose_other_company.setOnClickListener(new MyListener());
        //获取扫码后的物流编码
        initCaptureExpNo();
    }

    @Override
    public int getToolbar() {
        return R.id.express_toolbar;
    }

    //express_query_edit_text限制表情输入
    InputFilter inputFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                MToast.showLongToast("不可以输入表情");
                return "";
            }
            return null;
        }
    };

    private void initCaptureExpNo() {
        String expNo = getIntent().getStringExtra("expNo");
        if (!StringUtil.isEmpty(expNo)) {
            express_query_edit_text.setText(expNo);
        }
    }

    private void initData(String expNo) {
        readCompany(); //读取物流公司数据
        list.clear();
        Map<String, String> map = new HashMap<>();
        map.put("expNo", expNo);
        getData(map);
    }

    private void historyData() {
        list.clear();
        if (expressHistoryDao != null) {
            List<ExpressHistoryEntity> expressHistoryEntities = expressHistoryDao.historyList();
            if (expressHistoryEntities.size() != 0) {
                express_history_empty.setVisibility(View.GONE);
                express_history_title.setVisibility(View.VISIBLE);
                historyList.setVisibility(View.VISIBLE);
                for (ExpressHistoryEntity temp : expressHistoryEntities) {
                    HistoryModel historyModel = new HistoryModel();
                    historyModel.setCompanyName(temp.getCompanyName());
                    historyModel.setExpNo(temp.getExpNo());
                    historyModel.setCurrentDate(temp.getCurrentDate().substring(5, 10));
                    historyModel.setDetail(temp.getDetail());
                    historyModel.setCompanyCode(temp.getCode());
                    historyModel.setCompanyLogo(temp.getCompanyLogo());
                    historyModel.setState(temp.getState());
                    list.add(historyModel);
                }
                Collections.reverse(list);
            } else {
                express_history_empty.setVisibility(View.VISIBLE);
                express_history_title.setVisibility(View.GONE);
                historyList.setVisibility(View.GONE);
            }
        }
        historyAdapter = historyList.initRecyclerView(list, ExpressQueryActivity.this);
        //设置item间距
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            historyList.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }

    }


    private void getData(Map<String, String> map) {
        Network.request(Network.createApi(IExpressQueryApi.class).expressByExpNo(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                List list = removeDuplicate(data);
                if (list.size() != 0) {
                    handleData(list);
                    express_query_choose_other_company.setVisibility(View.VISIBLE);
                } else {
                    express_query_choose_other_company.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });
    }

    //物流公司去重
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    //处理数据
    private void handleData(List<Map<String, String>> data) {
        if (data.size() > 0 && data.size() < 5) {
            List<Map<String, String>> maps = data.subList(0, data.size());
            for (Map<String, String> temp : maps) {
                CompanyModel companyModel = new CompanyModel();
                companyModel.setName(temp.get("ShipperName"));
                companyModel.setCode(temp.get("ShipperCode"));
                CompanyModel companyModel1 = companyMap.get(temp.get("ShipperCode"));
                if (companyModel1 != null) {
                    companyModel.setLogo(companyModel1.getLogo());
                }
                list.add(companyModel);
                multiTypeAdapter.notifyDataSetChanged();
            }
        } else {
            List<Map<String, String>> maps = data.subList(0, 5);
            for (Map<String, String> temp : maps) {
                CompanyModel companyModel = new CompanyModel();
                companyModel.setName(temp.get("ShipperName"));
                companyModel.setCode(temp.get("ShipperCode"));
                CompanyModel companyModel1 = companyMap.get(temp.get("ShipperCode"));
                if (companyModel1 != null) {
                    companyModel.setLogo(companyModel1.getLogo());
                }
                list.add(companyModel);
                multiTypeAdapter.notifyDataSetChanged();
            }
        }

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
            express_history_title.setVisibility(View.GONE);
            express_history_empty.setVisibility(View.GONE);
            historyList.setVisibility(View.GONE);
            express_query_scan.setVisibility(View.INVISIBLE);
            express_query_clear.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            list.clear();
            if (s.length() >= 8) {
                String expNo = s.toString();
                initData(expNo); //初始化加载数据
                multiTypeAdapter = listView.initRecyclerView(list, ExpressQueryActivity.this);
                if (isCompanyAddItemDecoration) {
                    isCompanyAddItemDecoration = false;
                    listView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
                }
                multiTypeAdapter.setTag(s.toString());
            }
        } else {
            express_history_title.setVisibility(View.VISIBLE);
            express_query_scan.setVisibility(View.VISIBLE);
            express_query_clear.setVisibility(View.INVISIBLE);
            express_history_empty.setVisibility(View.VISIBLE);
            historyList.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            express_query_choose_other_company.setVisibility(View.GONE);
            list.clear();
            historyData();
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        express_query_edit_text.removeTextChangedListener(this);
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
                CompanyModel company = gson.fromJson(obj, CompanyModel.class);
                if (!StringUtil.isEmpty(company.getCode())) {
                    companyMap.put(company.getCode(), company);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //自定义点击事件
    class MyListener extends OnMultiClickListener {

        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.express_query_scan:
                    startCaptureActivity();
                    break;
                case R.id.express_query_clear:
                    express_query_edit_text.setText("");
                    break;
                case R.id.express_query_choose_other_company:
                    Intent intent = new Intent(getContext(), ExpressCompanyActivity.class);
                    String expNo = express_query_edit_text.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("expNo", expNo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    }

    //获取相机权限
    private void startCaptureActivity() {
        requestPermission(PermissionConst.PERMISSION_CAMERA, new OnPermissionListener() {
            @Override
            public void onReceive() {
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), RequestCode.REQUEST_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            stringExtra = data.getStringExtra("hisCache");
            if ("111".equals(stringExtra)) {
                historyData(); //返回重新加载数据
            }
        }
    }

    //单号获取查询结果
    private CompanyModel getCaptureData(Map<String, String> map) {
        CompanyModel companyModel = new CompanyModel();
        Network.request(Network.createApi(IExpressQueryApi.class).expressByExpNo(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                companyModel.setName(data.get(0).get("ShipperName"));
                companyModel.setName(data.get(0).get("ShipperCode"));
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });
        return companyModel;
    }

}
