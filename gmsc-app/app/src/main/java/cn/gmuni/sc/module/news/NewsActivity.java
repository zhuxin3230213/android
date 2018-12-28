package cn.gmuni.sc.module.news;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.InforTipsModel;
import cn.gmuni.sc.model.NewsModel;
import cn.gmuni.sc.model.NewsMoreImageModel;
import cn.gmuni.sc.model.NewsOneModel;
import cn.gmuni.sc.model.NewsOneRowModel;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.INewsApi;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.MToast;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.CustomEmptyDataView;
import cn.gmuni.sc.widget.recyclerview.CustomRefreshRecycleView;
import cn.gmuni.sc.widget.recyclerview.RecycleViewDivider;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;

public class NewsActivity extends BaseCommonActivity implements CustomRefreshRecycleView.OnRefreshListener {

    @BindView(R.id.news_list_view)
    CustomRefreshRecycleView listView;
    @BindView(R.id.news_list_view_no_data_empty)
    CustomEmptyDataView news_list_view_no_data_empty;


    private static final List<Visitable> list = new ArrayList<>();

    private MultiTypeAdapter multiTypeAdapter;

    private Integer currentPage = 1;
    private Integer pageSize = 10;
    private Integer pullDownRefreshCurrentPage = 1;
    private Integer pullDownRefreshPageSize = 10;
    private Integer loadingMoreCurrentPage = 1;
    private Integer loadingMorePageSize = 10;
    private String initDataFirstUpdateTime;
    private String initDataLastUpdateTime;
    private String lastNewId;
    private String initDataLastNewId;
    private String bottomLastNewId;
    private int screenWidth;
    private int measuredWidth;
    private boolean isAddItemDecoration = true;
    private boolean isLoadingLast = true;
    private boolean isReLoadingLast = false;

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //初始化数据
        initData();
        //适配数据
        multiTypeAdapter = listView.initRefreshRecyclerView(list, NewsActivity.this);
        //设置item间距
        if (isAddItemDecoration) {
            isAddItemDecoration = false;
            listView.addItemDecoration(new RecycleViewDivider(0, PixelUtil.dp2px(1)));
        }
        //刷新事件
        listView.setOnRefreshListener(this);
    }

    @Override
    public int getToolbar() {
        return R.id.news_toolbar;
    }

    //初始化data
    private void initData() {
        list.clear();
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", String.valueOf(currentPage));
        map.put("pageSize", String.valueOf(pageSize));
        getData(map);
    }

    //根据app显示的条数重新加载数据
    private void initReData(int pageSize) {
        if (!StringUtil.isEmpty(lastNewId) && !StringUtil.isEmpty(bottomLastNewId) && lastNewId.equals(bottomLastNewId)) {
            isReLoadingLast = true; //下拉刷新标记,显示底部提示
        }
        list.clear();
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", String.valueOf(currentPage));
        map.put("pageSize", String.valueOf(pageSize));
        getData(map);
    }

    @Override
    public void onPullDownRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullDownRefreshData();
                multiTypeAdapter.notifyDataSetChanged();
                listView.hideHeaderView();
            }
        }, 2000);
    }


    @Override
    public void onLoadingMore() {
        //列表最后一条数据的id与上拉加载数据最后一条数据id相同时,则加载完毕
        if (!StringUtil.isEmpty(lastNewId) && !StringUtil.isEmpty(bottomLastNewId) && lastNewId.equals(bottomLastNewId)) {
            listView.hideFooterView(); //隐藏底部布局
            if (isLoadingLast) {
                isLoadingLast = false;
                if (isReLoadingLast) {
                    //如果是下拉刷新加载最后一条时，不加载提示
                } else {
                    InforTipsModel inforTipsModel = new InforTipsModel();
                    inforTipsModel.setInformationTips("暂无更多数据");
                    list.add(inforTipsModel);
                    multiTypeAdapter.notifyDataSetChanged();
                }
            } else {
                //这里分为两种情况：一直上拉加载 (不再渲染)/ 下拉刷新时
                if (isReLoadingLast) {
                    isReLoadingLast = false;
                    isLoadingLast = false;
                    InforTipsModel inforTipsModel = new InforTipsModel();
                    inforTipsModel.setInformationTips("暂无更多数据");
                    list.add(inforTipsModel);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                }
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLoadingMoreData();
                    multiTypeAdapter.notifyDataSetChanged();
                    listView.hideFooterView();
                }
            }, 5000);
        }


    }

    //下拉刷新
    private void pullDownRefreshData() {
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", String.valueOf(pullDownRefreshCurrentPage));
        map.put("pageSize", String.valueOf(pullDownRefreshPageSize));
        map.put("initDataFirstUpdateTime", initDataFirstUpdateTime);
        addNewsTop(map);
    }

    private void addNewsTop(Map<String, String> map) {
        Network.request(Network.createApi(INewsApi.class).addNewsTop(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    initDataFirstUpdateTime = DateUtils.long2Data(data.get(0).get("updateTime"), DateUtils.COMMON_DATETIME);
                    handleTopData(data);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    int currentSize = list.size();
                    multiTypeAdapter.notifyDataSetChanged();
                    initReData(currentSize); //根据app显示的条数重新加载数据
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });

    }


    //上拉加载更多
    private void onLoadingMoreData() {
        Map<String, String> map = new HashMap<>();
        map.put("currentPage", String.valueOf(loadingMoreCurrentPage));
        map.put("pageSize", String.valueOf(loadingMorePageSize));
        map.put("initDataLastUpdateTime", initDataLastUpdateTime);
        addNewsBottom(map);
    }

    private void addNewsBottom(Map<String, String> map) {
        Network.request(Network.createApi(INewsApi.class).addNewsBottom(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    initDataLastUpdateTime = DateUtils.long2Data(data.get(data.size() - 1).get("updateTime"), DateUtils.COMMON_DATETIME);
                    bottomLastNewId = String.valueOf(data.get(data.size() - 1).get("id"));
                    handleData(data);
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    bottomLastNewId = initDataLastNewId;
                }
            }

            @Override
            public void onFail(int code, String message) {
                MToast.showShortToast("获取数据失败");
            }
        });
    }


    //网络连接获取初始数据
    private void getData(Map<String, String> map) {
        showLoading();
        Network.request(Network.createApi(INewsApi.class).listNews(map), new Network.IResponseListener<List<Map<String, String>>>() {
            @Override
            public void onSuccess(List<Map<String, String>> data) {
                if (data.size() != 0) {
                    news_list_view_no_data_empty.setVisibility(View.GONE);
                    initDataFirstUpdateTime = DateUtils.long2Data(data.get(0).get("updateTime"), DateUtils.COMMON_DATETIME);
                    initDataLastUpdateTime = DateUtils.long2Data(data.get(data.size() - 2).get("updateTime"), DateUtils.COMMON_DATETIME);
                    initDataLastNewId = String.valueOf(data.get(data.size() - 2).get("id")); //初始化最后一条数据id

                    lastNewId = data.get(data.size() - 1).get("newsId"); //获取最后一条数据id
                    handleData(data.subList(0, data.size() - 1)); //去除集合最后一条数据
                    hideLoading();
                    multiTypeAdapter.notifyDataSetChanged();
                } else {
                    lastNewId = "";
                    hideLoading();
                    news_list_view_no_data_empty.setVisibility(View.VISIBLE);
                    news_list_view_no_data_empty.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            initData();//重新初始化数据
                        }
                    });
                }
            }

            @Override
            public void onFail(int code, String message) {
                Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT);
                hideLoading();
            }
        });
    }

    //处理数据
    private void handleData(List<Map<String, String>> data) {
        for (Map<String, String> temp : data) {
            NewsModel newsModel = new NewsModel(); //带1张图片，多行
            NewsOneRowModel newsOneRowModel = new NewsOneRowModel(); //1图，1行
            NewsMoreImageModel newsMoreImageModel = new NewsMoreImageModel(); //带两张以上图片
            NewsOneModel newsOneModel = new NewsOneModel(); //不带图片
            if (StringUtil.isEmpty(temp.get("imageIds"))) {
                //无图片
                newsOneModel.setId(temp.get("id"));
                newsOneModel.setTitle(temp.get("title"));
                newsOneModel.setName(temp.get("deptName"));
                if (StringUtil.isEmpty(temp.get("updateTime"))) {
                    newsOneModel.setUpdateTime("");
                } else {
                    newsOneModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                }
                list.add(newsOneModel);
            } else {
                //有图片
                List<String> ids = Arrays.asList(temp.get("imageIds").split(","));
                if (ids.size() == 1) {
                    //1图
                    final Paint paint = new TextPaint();
                    paint.setTextSize(PixelUtil.dp2px(14));
                    float v = paint.measureText(temp.get("title"));
                    int titleLength = (int) v; //title宽度

                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    screenWidth = dm.widthPixels; //获取屏幕宽度
                    measuredWidth = screenWidth - (PixelUtil.dp2px(140)); //获取文本框宽度

                    if (titleLength > measuredWidth) { //多行
                        newsModel.setId(temp.get("id"));
                        newsModel.setTitle(temp.get("title"));
                        newsModel.setName(temp.get("deptName"));
                        newsModel.setImageIds(temp.get("imageIds"));
                        if (StringUtil.isEmpty(temp.get("updateTime"))) {
                            newsModel.setUpdateTime("");
                        } else {
                            newsModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                        }
                        list.add(newsModel);
                    } else {//1行
                        newsOneRowModel.setId(temp.get("id"));
                        newsOneRowModel.setTitle(temp.get("title"));
                        newsOneRowModel.setName(temp.get("deptName"));
                        newsOneRowModel.setImageIds(temp.get("imageIds"));
                        if (StringUtil.isEmpty(temp.get("updateTime"))) {
                            newsOneRowModel.setUpdateTime("");
                        } else {
                            newsOneRowModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                        }
                        list.add(newsOneRowModel);
                    }
                } else {
                    //多图
                    newsMoreImageModel.setId(temp.get("id"));
                    newsMoreImageModel.setTitle(temp.get("title"));
                    newsMoreImageModel.setName(temp.get("deptName"));
                    newsMoreImageModel.setImageIds(temp.get("imageIds"));
                    if (StringUtil.isEmpty(temp.get("updateTime"))) {
                        newsMoreImageModel.setUpdateTime("");
                    } else {
                        newsMoreImageModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                    }
                    list.add(newsMoreImageModel);
                }

            }
        }
    }

    //处理数据
    private void handleTopData(List<Map<String, String>> data) {
        Collections.reverse(data);
        for (Map<String, String> temp : data) {
            NewsModel newsModel = new NewsModel(); //带1张图片，多行
            NewsOneRowModel newsOneRowModel = new NewsOneRowModel(); //1图，1行
            NewsMoreImageModel newsMoreImageModel = new NewsMoreImageModel(); //带两张以上图片
            NewsOneModel newsOneModel = new NewsOneModel(); //不带图片
            if (StringUtil.isEmpty(temp.get("imageIds"))) {
                //无图片
                newsOneModel.setId(temp.get("id"));
                newsOneModel.setTitle(temp.get("title"));
                newsOneModel.setName(temp.get("deptName"));
                if (StringUtil.isEmpty(temp.get("updateTime"))) {
                    newsOneModel.setUpdateTime("");
                } else {
                    newsOneModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                }
                list.add(0, newsOneModel);
            } else {
                //有图片
                List<String> ids = Arrays.asList(temp.get("imageIds").split(","));
                if (ids.size() == 1) {
                    //1图
                    final Paint paint = new TextPaint();
                    paint.setTextSize(PixelUtil.dp2px(14));
                    float v = paint.measureText(temp.get("title"));
                    int titleLength = (int) v; //title宽度

                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    screenWidth = dm.widthPixels; //获取屏幕宽度
                    measuredWidth = screenWidth - (PixelUtil.dp2px(140, getContext())); //获取文本框宽度

                    if (titleLength > measuredWidth) {
                        //多行
                        newsModel.setId(temp.get("id"));
                        newsModel.setTitle(temp.get("title"));
                        newsModel.setName(temp.get("deptName"));
                        newsModel.setImageIds(temp.get("imageIds"));
                        if (StringUtil.isEmpty(temp.get("updateTime"))) {
                            newsModel.setUpdateTime("");
                        } else {
                            newsModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                        }
                        list.add(0, newsModel);
                    } else {
                        //1行
                        newsOneRowModel.setId(temp.get("id"));
                        newsOneRowModel.setTitle(temp.get("title"));
                        newsOneRowModel.setName(temp.get("deptName"));
                        newsOneRowModel.setImageIds(temp.get("imageIds"));
                        if (StringUtil.isEmpty(temp.get("updateTime"))) {
                            newsOneRowModel.setUpdateTime("");
                        } else {
                            newsOneRowModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                        }
                        list.add(0, newsOneRowModel);
                    }
                } else {
                    //多图
                    newsMoreImageModel.setId(temp.get("id"));
                    newsMoreImageModel.setTitle(temp.get("title"));
                    newsMoreImageModel.setName(temp.get("deptName"));
                    newsMoreImageModel.setImageIds(temp.get("imageIds"));
                    if (StringUtil.isEmpty(temp.get("updateTime"))) {
                        newsMoreImageModel.setUpdateTime("");
                    } else {
                        newsMoreImageModel.setUpdateTime(DateUtils.long2Data(temp.get("updateTime"), "yyyy-MM-dd HH:mm:ss").substring(0, 10));
                    }
                    list.add(0, newsMoreImageModel);
                }

            }
        }
    }
}
