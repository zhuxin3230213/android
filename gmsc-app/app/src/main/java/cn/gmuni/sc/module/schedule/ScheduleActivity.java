package cn.gmuni.sc.module.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.network.Network;
import cn.gmuni.sc.network.api.IScheduleApi;
import cn.gmuni.sc.utils.CoderUtils;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.JsonUtils;
import cn.gmuni.sc.utils.PixelUtil;
import cn.gmuni.sc.utils.SecurityUtils;
import cn.gmuni.sc.utils.StringUtil;
import cn.gmuni.sc.widget.BorderedTextView;
import cn.gmuni.sc.widget.WheelListDialog;

public class ScheduleActivity extends BaseCommonActivity {
    //时间
    @BindView(R.id.schedule_time)
    LinearLayout schedule_time;
    //周一课表内容
    @BindView(R.id.schedule_monday_content)
    LinearLayout schedule_monday_content;
    @BindView(R.id.schedule_tuesday_content)
    LinearLayout schedule_tuesday_content;
    @BindView(R.id.schedule_wednesday_content)
    LinearLayout schedule_wednesday_content;
    @BindView(R.id.schedule_thursday_content)
    LinearLayout schedule_thursday_content;
    @BindView(R.id.schedule_friday_content)
    LinearLayout schedule_friday_content;
    @BindView(R.id.schedule_saturday_content)
    LinearLayout schedule_saturday_content;
    @BindView(R.id.schedule_sunday_content)
    LinearLayout schedule_sunday_content;
    //周对应的日期
    @BindView(R.id.schedule_monday_date)
    TextView schedule_monday_date;
    @BindView(R.id.schedule_tuesday_date)
    TextView schedule_tuesday_date;
    @BindView(R.id.schedule_wednesday_date)
    TextView schedule_wednesday_date;
    @BindView(R.id.schedule_thursday_date)
    TextView schedule_thursday_date;
    @BindView(R.id.schedule_friday_date)
    TextView schedule_friday_date;
    @BindView(R.id.schedule_saturday_date)
    TextView schedule_saturday_date;
    @BindView(R.id.schedule_sunday_date)
    TextView schedule_sunday_date;
    @BindView(R.id.txt_main_layout)
    LinearLayout toolBarLayout;

    private TextView weekTv;
    private TextView semesterTv;

    /**
     * 学期信息
     */
    private List<Map<String, String>> semesterData;

    /**
     * 入学年份
     */
    private String yearReg;

    /**
     * 存放所有学期的
     */
    private Map<String, List<String>> allWeekInfo = new HashMap<>();

    private List<String> chineseUpperNumber = Arrays.asList(new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九",
            "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九",
            "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九",
            "三十", "三十一", "三十二", "三十三", "三十四", "三十五", "三十六", "三十七", "三十八", "三十九",
            "四十", "四十一", "四十二", "四十三", "四十四", "四十五", "四十六", "四十七", "四十八", "四十九",
            "五十", "五十一", "五十二", "五十三", "五十四", "五十五", "五十六", "五十七", "五十八", "五十九"});
    /**
     * 周数显示的跟实际值的映射
     */
    private Map<String, String> weekStringIdxMap = new HashMap<>();
    /**
     * 显示的学期信息映射
     */
    private Map<String, Map<String, String>> semesterInfoMap = new HashMap<>();
    private String dateFormatStr = "yyyy-MM-dd";
    /**
     * 查询课程表的参数
     * 选择的学期也在里面，所以学期列表选择界面回填也可以用这个
     */
    private Map<String, String> params = new HashMap<>();

    private String currentSelectSemester;
    private String currentSelectWeek;

    private boolean detailIdShown = false;

    {
        params.put("code", SecurityUtils.getUserInfo().getStudentId());
        params.put("semester", "");
        params.put("academicYear", "");
        params.put("week", "");
    }

    String[] colors = {"#2db572", "#28d0ae", "#feb712", "#36c2f0", "#f03f36", "#cccdcf"};

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_schedule);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                Intent intent = new Intent(getContext(), SemesterSelectActivity.class);
                intent.putExtra("semesterData", (Serializable) semesterData);
                intent.putExtra("selectedData", (Serializable) params);
                startActivityForResult(intent, 0x11);
            }
        });
        setSelectButtons();
        //获取学期相关信息
        getCourseTimeInfoAndDrawSelector();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x11) {
            String semester = data.getStringExtra("semester");
            String academicYear = data.getStringExtra("academicYear");
            params.put("semester", semester);
            currentSelectSemester = academicYear + "-" + semester;
            params.put("academicYear", academicYear);
            params.put("week", "1");
            weekTv.setText("第一周");
            int gradeNum = Integer.parseInt(academicYear.split("-")[0]) - Integer.parseInt(yearReg) + 1;
            currentSelectSemester = academicYear + "-" + semester;
            semesterTv.setText("大" + getChineseStr(gradeNum) + "." + "第" + getChineseStr(Integer.parseInt(semester)) + "学期");
            showLoading();
            loadScheduleData();
        }
    }

    private void setSelectButtons() {
        toolBarLayout.removeAllViewsInLayout();
        //定义周显示按钮
        weekTv = new TextView(getContext());
        weekTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        weekTv.setGravity(Gravity.CENTER);
        weekTv.setTextColor(getResources().getColor(R.color.fontColorWhite));
        toolBarLayout.addView(weekTv);
        toolBarLayout.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                WheelListDialog dialog = new WheelListDialog(ScheduleActivity.this, allWeekInfo.get(currentSelectSemester), currentSelectWeek);
                dialog.setTitle("请选择周数");
                dialog.show();
                dialog.setButtonListener(new WheelListDialog.OnClickListenerOkCancel() {
                    @Override
                    public void onClickListenerOk() {
                        Map<String, String> currentSemesterInfo = semesterInfoMap.get(currentSelectSemester);
                        if (null != currentSemesterInfo) {
                            params.put("semester", currentSemesterInfo.get("semester"));
                            params.put("academicYear", currentSemesterInfo.get("academicYear"));
                            params.put("week", weekStringIdxMap.get(dialog.getPositionData()));
                            weekTv.setText(dialog.getPositionData());
                            showLoading();
                            loadScheduleData();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickListenerCancel() {
                        dialog.dismiss();
                    }
                });
            }
        });
        LinearLayout.LayoutParams tvParams = (LinearLayout.LayoutParams) weekTv.getLayoutParams();
        tvParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tvParams.weight = 1;
        tvParams.gravity = Gravity.CENTER;
        tvParams.setMargins(0, PixelUtil.dp2px(15), 0, 0);
        //定义学期显示按钮
        semesterTv = new TextView(getContext());
        semesterTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        semesterTv.setGravity(Gravity.CENTER);
        semesterTv.setTextColor(getResources().getColor(R.color.fontColorWhite));
        toolBarLayout.addView(semesterTv);
        tvParams = (LinearLayout.LayoutParams) weekTv.getLayoutParams();
        tvParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tvParams.weight = 1;
        tvParams.gravity = Gravity.CENTER;
        tvParams.setMargins(0, PixelUtil.dp2px(3), 0, PixelUtil.dp2px(2));
    }

    /**
     * 获取学期相关信息.
     */
    private void getCourseTimeInfoAndDrawSelector() {
        showLoading();
        Map<String, String> m = new HashMap<>();
        m.put("code", SecurityUtils.getUserInfo().getStudentId());
        Network.request(Network.createApi(IScheduleApi.class).getScheduleInfo(m), new Network.IResponseListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (!StringUtil.isEmpty(data)) {
                    semesterData = JsonUtils.parseJSON(data, List.class);
                    addSpinnerToToolBar(semesterData);
                }
                hideLoading();

            }

            @Override
            public void onFail(int code, String message) {
                hideLoading();
            }
        });
    }

    /**
     * 处理学期数据计算当前要显示的周课程表
     * 渲染周以及学期选择器
     *
     * @param data
     */
    private void addSpinnerToToolBar(List<Map<String, String>> data) {
        Date today = new Date();
        //定义一个标志，用于判断是否找到当前位于那个学期第几周
        boolean flag = false;
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> item = data.get(i);
            String academicYear = item.get("academicYear");
            String semester = item.get("semester");
            String year = item.get("year");
            yearReg = year;
            calcYearSemesterWeekInfos(item);
            if (!flag) {
                //拿到学期起始日期
                Date startDate = DateUtils.string2Date(item.get("startDate"), dateFormatStr);
                Date endDate = DateUtils.string2Date(item.get("endDate"), dateFormatStr);
                //判断当前时间是否位于起始日期之内
                if (today.after(startDate) && today.before(endDate)) {
                    int weekIdx = DateUtils.getWeekDiff(DateUtils.getBeginTimeOfWeek(startDate), DateUtils.getBeginTimeOfWeek(today)) + 1;
                    params.put("semester", semester);
                    params.put("academicYear", academicYear);
                    params.put("week", String.valueOf(weekIdx));
                    int gradeNum = Integer.parseInt(academicYear.split("-")[0]) - Integer.parseInt(year) + 1;
                    currentSelectSemester = academicYear + "-" + semester;
                    semesterTv.setText("大" + getChineseStr(gradeNum) + "." + "第" + getChineseStr(Integer.parseInt(semester)) + "学期");
                    currentSelectWeek = "第" + getChineseStr(weekIdx) + "周";
                    weekTv.setText(currentSelectWeek);
                    loadScheduleData();
                    flag = true;
                } else {
                    //若不在，则判断位于哪个学期之前
                    if (i < data.size() - 1) {
                        Date neatStartDate = DateUtils.string2Date(data.get(i + 1).get("startDate"), dateFormatStr);
                        if (today.after(endDate) && today.before(neatStartDate)) {
                            int weekIdx = DateUtils.getWeekDiff(startDate, endDate) + 1;
                            params.put("semester", semester);
                            params.put("academicYear", academicYear);
                            params.put("week", String.valueOf(weekIdx));
                            int gradeNum = Integer.parseInt(academicYear.split("-")[0]) - Integer.parseInt(year) + 1;
                            currentSelectSemester =  academicYear + "-" + semester;
                            semesterTv.setText("大" + getChineseStr(gradeNum) + "." + "第" + getChineseStr(Integer.parseInt(semester)) + "学期");
                            currentSelectWeek = "第" + getChineseStr(weekIdx) + "周";
                            weekTv.setText(currentSelectWeek);
                            loadScheduleData();
                            flag = true;
                        }
                    } else {
                        if (today.after(endDate)) {
                            int weekIdx = DateUtils.getWeekDiff(startDate, endDate) + 1;
                            params.put("semester", semester);
                            params.put("academicYear", academicYear);
                            params.put("week", String.valueOf(weekIdx));
                            int gradeNum = Integer.parseInt(academicYear.split("-")[0]) - Integer.parseInt(year) + 1;
                            currentSelectSemester =  academicYear + "-" + semester;
                            semesterTv.setText("大" + getChineseStr(gradeNum) + "." + "第" + getChineseStr(Integer.parseInt(semester)) + "学期");
                            currentSelectWeek = "第" + getChineseStr(weekIdx) + "周";
                            weekTv.setText(currentSelectWeek);
                            loadScheduleData();
                            flag = true;
                        }
                    }
                }

            }
        }
    }

    private void calcYearSemesterWeekInfos(Map<String, String> item) {
        //学年：2012-2013
        String academicYear = item.get("academicYear");
        //入学年份：2012
        String year = item.get("year");
        //学期：1
        String semester = item.get("semester");
        //开学时间
        Date startDate = DateUtils.string2Date(item.get("startDate"), dateFormatStr);
        //放假时间
        Date endDate = DateUtils.string2Date(item.get("endDate"), dateFormatStr);
        int weekCount = DateUtils.getWeekDiff(startDate, endDate) + 1;
        semesterInfoMap.put(academicYear + "-" + semester, item);
        List<String> semesterWeeks = new ArrayList<>();
        allWeekInfo.put(academicYear + "-" + semester, semesterWeeks);
        for (int i = 1; i <= weekCount; i++) {
            String weekStr = "第" + getChineseStr(i) + "周";
            semesterWeeks.add(weekStr);
            weekStringIdxMap.put(weekStr, String.valueOf(i));
        }
    }

    /**
     * 获取数字的大写
     * 抽成方法防止越界
     * @param idx
     * @return
     */
    private String getChineseStr(int idx){
        if(chineseUpperNumber.size() > idx){
            return chineseUpperNumber.get(idx);
        }
        return String.valueOf(idx);
    }

    /**
     * 请求课程表信息
     */
    private void loadScheduleData() {
        Network.request(Network.createApi(IScheduleApi.class).getAppSchedule(params), new Network.IResponseListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (!StringUtil.isEmpty(data)) {
                    processContent(JsonUtils.parseJSON(data, Map.class));
                }
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                hideLoading();
            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.schedule_toolbar;
    }

    /**
     * 渲染课程表
     *
     * @param data
     */
    private void processContent(Map<String, Object> data) {
        Map<String, Object> m = processData(data);
        schedule_monday_date.setText(m.get("monday").toString());
        schedule_tuesday_date.setText(m.get("tuesday").toString());
        schedule_wednesday_date.setText(m.get("wednesday").toString());
        schedule_thursday_date.setText(m.get("thursday").toString());
        schedule_friday_date.setText(m.get("friday").toString());
        schedule_saturday_date.setText(m.get("saturday").toString());
        schedule_sunday_date.setText(m.get("sunday").toString());
        schedule_time.removeAllViews();
//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("1", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("2", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("3", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("4", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("5", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("6", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("7", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("8", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

//        addText2Layout("", schedule_time, 27, 10, false, "#bfbfbf", "#f8f8f8");
        addText2Layout("9", schedule_time, 54, 16, true, "#666666", "#f8f8f8");

        LinearLayout[] layouts = {schedule_monday_content,
                schedule_tuesday_content,
                schedule_wednesday_content,
                schedule_thursday_content,
                schedule_friday_content,
                schedule_saturday_content,
                schedule_sunday_content};
        for (int i = 0; i < layouts.length; i++) {
            //这里的9需要替换成每天的上课节数
            LinearLayout layout = layouts[i];
            layout.removeAllViews();
            Map item = (Map) m.get((i + 1) + "");
            for (int j = 0; j < 9; j++) {
                Map<String, String> schInfo = (Map) item.get((j + 1) + "");
                if (null != schInfo) {
                    String targetnc = schInfo.get("targetnc");
                    int nc = (int) Double.parseDouble(String.valueOf(schInfo.get("nc")));
                    BorderedTextView textView = addText2Layout(schInfo.get("name") + "@" + schInfo.get("classroom"), layout, 54 * nc, 13, true, "#ffffff", null);

                    textView.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            if(!detailIdShown){
                                getDetailInfo(schInfo);
                            }
                        }
                    });
                    j += nc - 1;
                } else {
                    addText2Layout(null, layout, 54, 13, true, "#000000", "#00ffffff");
                }
            }
        }
    }

    private void getDetailInfo(Map<String, String> schInfo ){
        detailIdShown = true;
        showLoading();
        Network.request(Network.createApi(IScheduleApi.class).getScheduleWeek(schInfo), new Network.IResponseListener<String>() {
            @Override
            public void onSuccess(String data) {
                String weekStr = "";
                if (!StringUtil.isEmpty(data)) {
                    Map map = JsonUtils.parseJSON(data, Map.class);
                    weekStr = map.get("week").toString();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = View.inflate(getContext(), R.layout.dialog_schedule_info, null);
                int nc = (int) Double.parseDouble(String.valueOf(schInfo.get("nc")));
                String classroom = schInfo.get("classroom");
                String targetnc = schInfo.get("targetnc");
                String teacher = schInfo.get("teacher");
                String cName = schInfo.get("name");
                TextView titleTv = view.findViewById(R.id.schedule_info_dialog_title);
                TextView classroomTv = view.findViewById(R.id.schedule_info_dialog_classroom);
                TextView weekTv = view.findViewById(R.id.schedule_info_dialog_weekIdx);
                TextView targetncTv = view.findViewById(R.id.schedule_info_dialog_nc);
                TextView teacherTv = view.findViewById(R.id.schedule_info_dialog_teacher);
                titleTv.setText(cName);
                classroomTv.setText("教室：" + classroom);
                targetncTv.setText("节数：" + targetnc + "-" + (Integer.parseInt(targetnc) + nc - 1));
                weekTv.setText("周数：" + weekStr);
                teacherTv.setText("教师：" + teacher);
                builder.setView(view);
                builder.show();
                detailIdShown = false;
                hideLoading();
            }

            @Override
            public void onFail(int code, String message) {
                detailIdShown = false;
                hideLoading();
            }
        });
    }

    /**
     * 处理课表数据将日期跟星期几同步
     *
     * @param data
     * @return
     */
    private Map<String, Object> processData(Map<String, Object> data) {
        List ls = (ArrayList) data.get("weekSchedules");
        Map<String, Object> res = new HashMap<>();
        res.put("1", new HashMap());
        res.put("2", new HashMap());
        res.put("3", new HashMap());
        res.put("4", new HashMap());
        res.put("5", new HashMap());
        res.put("6", new HashMap());
        res.put("7", new HashMap());
        res.put("monday", "");
        res.put("tuesday", "");
        res.put("wednesday", "");
        res.put("thursday", "");
        res.put("friday", "");
        res.put("saturday", "");
        res.put("sunday", "");
        boolean dateFlag = false;
        for (int i = 0; i < ls.size(); i++) {
            Map<String, String> item = (Map<String, String>) ls.get(i);
            Map m = (Map) res.get(item.get("sunday"));
            if (null != m) {
                m.put(item.get("targetnc"), item);
                if (!dateFlag) {
                    processEveryDayInWeek(res, item);
                    dateFlag = true;
                }
            }
        }
        return res;
    }

    /**
     * 获取一周的所有日期
     *
     * @param res
     * @param item
     */
    private void processEveryDayInWeek(Map<String, Object> res, Map<String, String> item) {
        String dayStr = item.get("day");
        String monthDayFormat = "MM-dd";
        Date date = DateUtils.string2Date(dayStr, "yyyy-MM-dd");
        date = DateUtils.getBeginTimeOfWeek(date);
        res.put("monday", DateUtils.date2String(date, monthDayFormat));
        date = DateUtils.nextDay(date, 1);
        res.put("tuesday", DateUtils.date2String(date, monthDayFormat));
        date = DateUtils.nextDay(date, 1);
        res.put("wednesday", DateUtils.date2String(date, monthDayFormat));
        date = DateUtils.nextDay(date, 1);
        res.put("thursday", DateUtils.date2String(date, monthDayFormat));
        date = DateUtils.nextDay(date, 1);
        res.put("friday", DateUtils.date2String(date, monthDayFormat));
        date = DateUtils.nextDay(date, 1);
        res.put("saturday", DateUtils.date2String(date, monthDayFormat));
        date = DateUtils.nextDay(date, 1);
        res.put("sunday", DateUtils.date2String(date, monthDayFormat));

    }

    /**
     * @param text             要显示文本
     * @param layout           布局容器
     * @param height           文本框高度 dp
     * @param textSize         文字大小 sp
     * @param showBottomBorder 是否显示下边框
     * @param textColor        文本颜色
     * @param bgColor          背景色
     */
    private BorderedTextView addText2Layout(String text, ViewGroup layout, float height, int textSize, boolean showBottomBorder, String textColor, String bgColor) {
        BorderedTextView textView = new BorderedTextView(getContext());
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setGravity(Gravity.CENTER);
        textView.setHeight(PixelUtil.dp2px(height));
        textView.setTextColor(Color.parseColor(textColor));
        if (null == bgColor || bgColor.length() == 0) {
            if(!StringUtil.isEmpty(text)){
                textView.setRadius(PixelUtil.dp2px(5));
            }
            int color = getColor(text);
//            textView.setBackgroundColor(color);
            textView.setBgColor(color);
        } else {
            textView.setBackgroundColor(Color.parseColor(bgColor));
        }
        if (showBottomBorder) {
            textView.setShowBottom(true);
            textView.setBottomColor("#e2e2e2");
            textView.setBottomWidth(2);
            textView.setBottomType("dash");
        }
        layout.addView(textView);
        return textView;
    }

    /**
     * 根据文本内容计算颜色
     *
     * @return
     */
    private int getColor(String text) {
        int idx = 0;
        try {
            idx = getIntArrSum(CoderUtils.string2ASCII(text.split("@")[0])) % colors.length;
        } catch (Exception e) {

        }
        return Color.parseColor(colors[idx]);
    }

    /**
     * 求和
     *
     * @param arr
     * @return
     */
    private int getIntArrSum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }
}
