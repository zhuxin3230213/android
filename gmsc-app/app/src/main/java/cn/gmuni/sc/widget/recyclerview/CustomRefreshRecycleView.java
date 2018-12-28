package cn.gmuni.sc.widget.recyclerview;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import java.util.List;
import cn.gmuni.sc.R;
import cn.gmuni.sc.widget.recyclerview.adapter.HeaderViewAdapter;
import cn.gmuni.sc.widget.recyclerview.adapter.MultiTypeAdapter;
import cn.gmuni.sc.widget.recyclerview.model.Visitable;


/**
 * @Author: zhuxin
 * @Date: 2018/8/27 15:27
 * @Description:
 */
public class CustomRefreshRecycleView extends RecyclerView {

    private final Context mContext;

    private HeaderViewAdapter headerViewAdapter;
    private MultiTypeAdapter multiTypeAdapter;

    // 顶部视图，下拉刷新控件
    private View headerView;
    private ImageView headerLoading;

    private ImageView footerLoading;
    // 转到下拉刷新状态时的动画
    private RotateAnimation downAnima;
    // 转到释放刷新状态时的动画
    private RotateAnimation upAnima;

    //触摸事件中按下的Y坐标，初始值为-1，为防止ACTION_DOWN事件被抢占
    private float startY = -1;
    // 下拉刷新控件的高度
    private int pulldownHeight;

    // 刷新状态：下拉刷新
    private final int PULL_DOWN_REFRESH = 0;
    // 刷新状态：释放刷新
    private final int RELEASE_REFRESH = 1;
    // 刷新状态：正常刷新
    private final int REFRESHING = 2;


    // 当前头布局的状态-默认为下拉刷新
    private int currState = PULL_DOWN_REFRESH;

    // 尾部视图
    private View footerView;
    // 尾部试图（上拉加载控件）的高度
    private int footerViewHeight;
    // 判断是否是加载更多
    private boolean isLoadingMore = false;
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private boolean isScrollToBottom; // 是否滑动到底部

    private View emptyView;

    //上次加载开始前的RecyclerView包含数据的数目
    private int previousTotal;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    };

    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    //设置没有内容时，提示用户的空布局
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    public CustomRefreshRecycleView(Context context) {
        this(context, null);
    }


    public CustomRefreshRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }


    public CustomRefreshRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        //初始化头布局
        initHeaderView();
//
        //初始化为尾布局
        initFooterView();
    }

    //适配数据带刷新
    public MultiTypeAdapter initRefreshRecyclerView(List<Visitable> list, Context context) {
        // 给Recycler设置分割线
        //this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        //自定义分割线
        this.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, R.drawable.recyclerview_divider_mileage));
        multiTypeAdapter = new MultiTypeAdapter(list);
        headerViewAdapter = new HeaderViewAdapter(multiTypeAdapter);
        // 设置布局管理器
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        this.setAdapter(headerViewAdapter);
        this.addHeaderView(headerViewAdapter);
        this.addFooterView(headerViewAdapter);
        return multiTypeAdapter;
    }

    //适配数据不带刷新
    public MultiTypeAdapter initRecyclerView(List<Visitable> list, Context context) {
        // 给Recycler设置分割线
        //this.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        //自定义分割线
        this.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.VERTICAL, R.drawable.recyclerview_divider_mileage));
        multiTypeAdapter = new MultiTypeAdapter(list);
        // 设置布局管理器
        this.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        multiTypeAdapter.setHasStableIds(true); //数据刷新混乱及解决方案
        this.setAdapter(multiTypeAdapter);
        return multiTypeAdapter;
    }

    /**
     * 返回尾部布局，供外部调用
     *
     * @return
     */

    public View getFooterView() {
        return footerView;
    }

    /**
     * 返回头部布局，供外部调用
     *
     * @return
     */
    public View getHeaderView() {
        return headerView;

    }


    /**
     * 通过HeaderAndFooterWrapper对象给RecyclerView添加尾部
     *
     * @param headerViewAdapter RecyclerView.Adapter的包装类对象，通过它给RecyclerView添加尾部视图
     */
    public void addFooterView(HeaderViewAdapter headerViewAdapter) {
        headerViewAdapter.addFooterView(footerView);
    }


    /**
     * 通过HeaderAndFooterWrapper对象给RecyclerView添加头部部
     *
     * @param headerViewAdapter RecyclerView.Adapter的包装类对象，通过它给RecyclerView添加头部视图
     */

    public void addHeaderView(HeaderViewAdapter headerViewAdapter) {
        headerViewAdapter.addHeaderView(headerView);
    }


    /**
     * 初始化headerView
     */
    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.widget_loading_custom_listview, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerView.setLayoutParams(layoutParams);
        headerLoading = headerView.findViewById(R.id.listview_pull_loading);
        downAnima = initAnimation();
        headerLoading.startAnimation(downAnima);
        //测量headView的高度
        headerView.measure(View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        //获取高度，并保存
        pulldownHeight = headerView.getMeasuredHeight();
        //设置paddingTop = -headerViewHeight;这样，该控件被隐藏
        headerView.setPadding(0, -pulldownHeight, 0, 0);
        // 自己监听自己
        this.addOnScrollListener(new MyOnScrollListener());
    }


    //初始化底布局，与头布局同理
    private void initFooterView() {
        footerView = View.inflate(mContext, R.layout.widget_loading_custom_listview, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(layoutParams);
        footerLoading = footerView.findViewById(R.id.listview_pull_loading);
        upAnima = initAnimation();
        footerLoading.startAnimation(upAnima);
        footerView.measure(0, 0);
        //得到控件的高
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
    }

    private RotateAnimation initAnimation() {
        RotateAnimation a = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        a.setDuration(1000);
        a.setRepeatCount(Animation.INFINITE);
        a.setRepeatMode(Animation.RESTART);
        return a;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取按下时y坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //防止ACTION_DOWN事件被抢占，没有执行
                if (startY == -1) {
                    startY = ev.getY();
                }
                float endY = ev.getY();
                float dY = endY - startY;
                //判断当前是否正在刷新中
                if (currState == REFRESHING) {
                    //如果当前是正在刷新，不执行下拉刷新了，直接break;
                    break;
                }
                // 如果是下拉
                if (dY > 0) {
                    int paddingTop = (int) (dY - pulldownHeight);
                    if (paddingTop > 0 && currState != RELEASE_REFRESH) {
                        //完全显示下拉刷新控件，进入松开刷新状态
                        currState = RELEASE_REFRESH;
                        refreshViewState();
                    } else if (paddingTop < 0 && currState != PULL_DOWN_REFRESH) {
                        //没有完全显示下拉刷新控件，进入下拉刷新状态
                        currState = PULL_DOWN_REFRESH;
                        refreshViewState();
                    }
                    headerView.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                //重新记录值
                startY = -1;
                if (currState == PULL_DOWN_REFRESH) {
                    //设置默认隐藏
                    headerView.setPadding(0, -pulldownHeight, 0, 0);
                } else if (currState == RELEASE_REFRESH) {
                    //当前是释放刷新，进入到正在刷新状态，完全显示
                    currState = REFRESHING;
                    refreshViewState();
                    headerView.setPadding(0, 0, 0, 0);
                    //调用用户的回调事件，刷新页面数据
                    if (mOnRefreshListener != null) {
                        this.hideFooterView();
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 跳转刷新状态,根据currentState来更新headerView
     */
    private void refreshViewState() {
        switch (currState) {
            // 跳转到下拉刷新
            case PULL_DOWN_REFRESH:
                break;
            // 跳转到释放刷新
            case RELEASE_REFRESH:
                headerLoading.startAnimation(downAnima);
                downAnima.start();
                break;
            // 跳转到正在刷新
            case REFRESHING:
                break;
        }
    }

    private CustomRefreshRecycleView.OnRefreshListener mOnRefreshListener;

    /**
     * 定义下拉刷新和上拉加载的接口
     */
    public interface OnRefreshListener {
        /**
         * 当下拉刷新时触发此方法
         */

        void onPullDownRefresh();

        /**
         * 当加载更多的时候回调这个方法
         */
        void onLoadingMore();
    }

    public void setOnRefreshListener(CustomRefreshRecycleView.OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    /**
     * 当刷新完数据之后，调用此方法，把头文件隐藏，并且状态设置为初始状态
     */
    public void onFinishRefresh() {
        if (isLoadingMore) {
            //重置footerView状态
            footerView.setPadding(0, -footerViewHeight, 0, 0);
            isLoadingMore = false;
        } else {
            //重置headerView状态
            headerView.setPadding(0, -pulldownHeight, 0, 0);
            currState = PULL_DOWN_REFRESH;
        }
    }


    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        headerView.setPadding(0, -pulldownHeight, 0, 0);
        currState = PULL_DOWN_REFRESH;
    }


    /**
     * 隐藏脚布局
     */
    public void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }


    //列表跳转指定位置
    public void moveToPosition(int n) {
        LayoutManager layoutManager = this.getLayoutManager();
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            this.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = this.getChildAt(n - firstItem).getTop();
            this.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            this.scrollToPosition(n);
            isLoadingMore = true;
        }

    }

    private class MyOnScrollListener extends OnScrollListener {
        /**
         * SCROLL_STATE_IDLE：空闲状态
         * SCROLL_STATE_DRAGGING：滑动状态
         * SCROLL_STATE_SETTLING：滑动后自然沉降的状态
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        /*    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if (isLoadingMore) {
                //和之前数据的数目进行比较，判断是否加载完毕，重置加载状态
                if (totalItemCount > previousTotal) {//加载更多结束
                    isLoadingMore = false;
                    previousTotal = totalItemCount;
                } else if (totalItemCount < previousTotal) {//用户刷新结束
                    previousTotal = totalItemCount;
                    isLoadingMore = false;
                }else {
                    //TODO 目前此类无法对加载失败进行自动处理，需要外部动作
                }
            }
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            //RecyclerView滑动停止，若显示到最后的位置，则开始加载
            if (!isLoadingMore
                    && visibleItemCount > 0
                    && totalItemCount - 1 == linearLayoutManager.findLastVisibleItemPosition()
                    && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                isLoadingMore = true;
                //把底部加载显示
                footerView.setPadding(0, 0, 0, 0);
                footerLoading.startAnimation(upAnima);
                upAnima.start();
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadingMore();
                }
            }*/

            if (newState == SCROLL_STATE_DRAGGING) {
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    //当停止滚动时或者惯性滚动时，RecyclerView的最后一个显示的条目：getCount()-1
                    // 注意是findLastVisibleItemPosition()而不是getLastVisiblePosition
                    if (linearLayoutManager.findLastVisibleItemPosition() >= getChildCount() - 1) {
                        isLoadingMore = true;
                        //把底部加载显示
                        footerView.setPadding(0, 0, 0, 0);
                        footerLoading.startAnimation(upAnima);
                        upAnima.start();
                        if (mOnRefreshListener != null) {
                            mOnRefreshListener.onLoadingMore();
                        }
                    }
                }
            }
        }


        /**
         * 当滚动时调用
         *
         * @param firstVisibleItem 当前屏幕显示在顶部的item的position
         * @param totalItemCount   ListView的总条目的总数
         *                         /*
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int firstVisibleItem, int totalItemCount) {
            super.onScrolled(recyclerView, firstVisibleItem, totalItemCount);
            firstVisibleItemPosition = firstVisibleItem;
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.findLastVisibleItemPosition() == (totalItemCount - 1)) {
                    isScrollToBottom = true;
                } else {
                    isScrollToBottom = false;
                }
            }

        }

    }

    public void notifyDataSetChanged() {
        multiTypeAdapter.notifyDataSetChanged();
        hideHeaderView();
    }

    /**
     * 在元素被销毁时调用
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        headerLoading.stopPlayback();
    }


    //移除加载更多监听事件
    public void removeListener() {
        hideFooterView();
    }

}
