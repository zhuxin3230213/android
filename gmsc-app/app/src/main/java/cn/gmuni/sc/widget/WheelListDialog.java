package cn.gmuni.sc.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.gmuni.sc.R;
import cn.gmuni.sc.utils.PixelUtil;

public class WheelListDialog extends AlertDialog {

    private static final String TAG = "WheelListDialog";

    private ListView mViewList;
    private int firstPosition;
    private View line1;
    private View line2;
    private ConstraintLayout constraintLayout;
    private View view;
    private Button btOk, btCancel;
    private Context mContext;
    private List<String> data = new ArrayList<>();
    private List<String> showData = new ArrayList<>();
    private WheelListAdapter adapter;
    private int showNum = 3;//中间子项上方显示子项数目
    private float textAlpha[] = {1.0f, 1.0f, 1.0f, 1.0f};//字体透明度，从中间向外侧排列,这里是默认值
    private int itemHeight = PixelUtil.dp2px(40);//每个子项高度为40dp
    private View.OnClickListener listener;

    private String title;

    private String curentSelectStr;

    //初始化，设置显示数据
    public WheelListDialog(@NonNull Context context, List<String> data, String selected) {
        super(context);
        this.mContext = context;
        this.data = data;
        this.curentSelectStr = selected;
        view = LayoutInflater.from(context).inflate(R.layout.wheel_list, null);
    }

    //获取点击位置
    public String getPositionData() {
        return showData.get(firstPosition + showNum);
    }

    //点击按钮监听
    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    //设置显示在中间之上的子项的数目
    public void setShowNum(int num) {
        showNum = num;
    }

    //设置显示字体透明度，由中间向两侧排列
    public void setTextAlpha(float[] textAlpha) {
        this.textAlpha = textAlpha;
    }

    private void initView() {
        line1 = view.findViewById(R.id.line1);
        line2 = view.findViewById(R.id.line2);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
        mViewList = (ListView) view.findViewById(R.id.list_view);
        //设置大小
        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) constraintLayout.getLayoutParams();
        p.height = itemHeight * (showNum * 2 + 1) - 2;
        ConstraintLayout.LayoutParams p1 = (ConstraintLayout.LayoutParams) line1.getLayoutParams();
        p1.topMargin = itemHeight * showNum;
        ConstraintLayout.LayoutParams p2 = (ConstraintLayout.LayoutParams) line2.getLayoutParams();
        p2.topMargin = itemHeight * (showNum + 1);

        btCancel = (Button) view.findViewById(R.id.btCancel);
        btOk = (Button) view.findViewById(R.id.btOk);

        TextView titleTv = (TextView) view.findViewById(R.id.wheel_dialog_title);
        titleTv.setText(title);
        adapter = new WheelListAdapter(mContext, showData);
        mViewList.setAdapter(adapter);
        mViewList.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);

        mViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position - showNum >= 0 && position < showData.size() - showNum) {
                    if (position + 1 == firstPosition + showNum) {

                        mViewList.smoothScrollBy(-PixelUtil.dp2px(40), 300);
                        mViewList.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mViewList.requestFocusFromTouch();
                                mViewList.setSelection(position - showNum);
                            }
                        }, 300);
                    } else if (position < firstPosition + showNum) {
                        mViewList.smoothScrollByOffset(position - showNum - firstPosition + 1);
                        firstPosition = position - showNum + 1;
                    } else {
                        mViewList.smoothScrollByOffset(position - showNum - firstPosition);
                        firstPosition = position - showNum;
                    }
                    adapter.change(position);
                }
            }
        });

        mViewList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                    mViewList.smoothScrollToPosition(firstPosition);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem < firstPosition) {
                    firstPosition = firstVisibleItem;
                    adapter.change(firstVisibleItem + showNum);
                } else if (firstVisibleItem > firstPosition) {
                    firstPosition = firstVisibleItem;
                    adapter.change(firstVisibleItem + showNum);
                }
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, getPositionData(), Toast.LENGTH_SHORT).show();
                WheelListDialog.this.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelListDialog.this.dismiss();
            }
        });
        int selectPosition = data.indexOf(curentSelectStr);
        mViewList.post(new Runnable() {
            @Override
            public void run() {
                mViewList.setSelection(selectPosition);
                mViewList.smoothScrollToPosition(selectPosition);
            }
        });
        mViewList.setSelection(selectPosition);
    }

    public void setButtonListener(final OnClickListenerOkCancel listener) {
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListenerOk();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickListenerCancel();
            }
        });
    }

    public interface OnClickListenerOkCancel {
        void onClickListenerOk();

        void onClickListenerCancel();
    }

    @Override
    public void show() {
        super.show();
//        if (!checkDataIsWrong()) return;

        //让前面和后面都空出来
        for (int i = 0; i < showNum; i++) {
            showData.add("");
        }
        showData.addAll(data);
        for (int i = 0; i < showNum; i++) {
            showData.add("");
        }
        initView();
        setContentView(view);
    }
    public void setTitle(String title) {
        this.title = title;
    }

    //listView的适配器
    class WheelListAdapter extends BaseAdapter {

        List<String> data;
        Context mContext;
        List<TextBean> beanList = new ArrayList<>();

        public WheelListAdapter(Context mContext, List<String> data) {
            this.data = data;
            this.mContext = mContext;
            for (String str : data) {
                beanList.add(new TextBean(str));
            }
//初始化最先显示的数据
            for (int i = 0; i < showNum + 1; i++) {
                beanList.get(i).setAlphaAndSize(1f, 17);
                beanList.get(i).setColor(Color.parseColor("#333333"));
            }
            for (int i = showNum + 1; i < showNum * 2 + 1; i++) {
                beanList.get(i).setAlphaAndSize(1f, 14);
                beanList.get(i).setColor(Color.parseColor("#999999"));
            }
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.wheel_list_item,
                        parent, false);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.text);
            TextBean text = beanList.get(position);
            textView.setText(text.getText());
            textView.setAlpha(text.getAlpha());
            textView.setTextSize(text.getSize());
            textView.setTextColor(text.getColor());
            return view;
        }

        private void change(int position) {
            for (int i = 0; i < showNum + 1; i++) {
                if (position + i < beanList.size()) {
                    beanList.get(position + i).setAlphaAndSize(1f, 14);
                    beanList.get(position + i).setColor(Color.parseColor("#999999"));
                }
                if (position - i >= 0) {
                    beanList.get(position - i).setAlphaAndSize(1f, 14);
                    beanList.get(position - i).setColor(Color.parseColor("#999999"));
                }
            }
            beanList.get(position).setAlphaAndSize(1f, 17);
            beanList.get(position).setColor(Color.parseColor("#333333"));
            notifyDataSetChanged();
        }
    }

    class TextBean {
        int size = 14;
        float alpha = 1.0f;
        String text;
        int color;

        public TextBean(String text) {
            this.text = text;
        }

        public void setAlphaAndSize(Float alpha, int size) {
            this.alpha = alpha;
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public float getAlpha() {
            return alpha;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
