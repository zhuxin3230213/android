package cn.gmuni.demo.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomButton;
import android.widget.ZoomControls;




public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String EXTRA_MESSAGE = "android.intent.extra.alarm.MESSAGE";
    private TextView textview;
    private Button button;
    private Button button1;
    private String str;
    //activity的生命周期

    private EditText mNameEt = null; // 用户名输入框
    private EditText mPasswordEt = null; // 密码输入框
    private Button mLoginBtn = null; // 登录按钮



    private CheckBox mShanghaiCb = null; // 上海复选框
    private CheckBox mBeijingCb = null; // 北京复选框
    private CheckBox mChongqingCb = null; // 重庆复选框


    private RadioButton mMaleRb = null; // 性别男单选按钮
    private RadioButton mFemaleRb = null; // 性别女单选按钮
    private RadioGroup mSexRg = null; // 性别单选按钮组

    private ToggleButton mLikeTb = null;

    private Switch mBluetoothSwitch = null;

    private ImageButton mControlIb = null; // 播放控制按钮
    private boolean mFlag = false; // 播放控制标记符,默认暂停状态

    private ZoomButton mMinusZb = null; // 缩小按钮
    private ZoomButton mPlusZb = null; // 放大按钮
    private ZoomControls mControlZc = null; //缩放组件

    @Override
    protected void onCreate(Bundle savedInstanceState) {//创建启动
        // onCreate() 方法会在第一次创建新的Activity实例与重新创建之前被Destory的实例时都被调用，
        // 我们必须在尝试读取 Bundle 对象前检测它是否为null。如果它为null，系统则是创建一个新的Activity实例，
        // 而不是恢复之前被Destory的Activity
     /*   new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    java.sql.Connection cn= DriverManager.getConnection(
                            "jdbc:mysql://192.168.3.230:3306/zhu_enrollment?useUnicode=true&characterEncoding=utf-8&useSSL=false&zeroDateTimeBehavior=convertToNull","kaifa",
                            "kaifa_123");
                    String sql="select * from zs_gmuni_config";
                    Statement st=(Statement)cn.createStatement();
                    ResultSet rs=st.executeQuery(sql);
                    while(rs.next()){
                        String mybook=rs.getString("name");
                        System.out.println(mybook);
                    }
                    cn.close();
                    st.close();
                    rs.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();*/

        super.onCreate(savedInstanceState);
        //按钮
       /* setContentView(R.layout.activity_main);
        textview = findViewById(R.id.textView);
        button =  findViewById(R.id.button);
        button1 = findViewById(R.id.button1);

        button.setOnClickListener(this);
        button1.setOnClickListener(this);*/

      //编辑框
     /*  setContentView(R.layout.login);
        // 获取界面组件
        mNameEt = findViewById(R.id.name_et);
        mPasswordEt = findViewById(R.id.pwd_et);
        mLoginBtn = findViewById(R.id.login_btn);
        // 为登录按钮绑定点击事件
        mLoginBtn.setOnClickListener(this);*/


      //复选框
      /*  setContentView(R.layout.checkbox_layout);
        // 获取界面组件
        mShanghaiCb = findViewById(R.id.shanghai_cb);
        mBeijingCb = findViewById(R.id.beijing_cb);
        mChongqingCb = findViewById(R.id.chongqing_cb);

        mShanghaiCb.setOnCheckedChangeListener(this);
        mBeijingCb.setOnCheckedChangeListener(this);
        mChongqingCb.setOnCheckedChangeListener(this);*/

      /* //单选按钮
        setContentView(R.layout.radiobutton_layout);
        // 获取界面组件
       mMaleRb = findViewById(R.id.male_rb);
       mFemaleRb = findViewById(R.id.female_rb);
       mSexRg = findViewById(R.id.sex_rg);
       mSexRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int i) {
               // 获取用户选中的性别
               String sex = "";
               switch(i) {
                   case R.id.male_rb:
                       sex = mMaleRb.getText().toString();
                       break;
                   case R.id.female_rb:
                       sex = mFemaleRb.getText().toString();
                       break;
                    default:break;
               }
               // 消息提示
               Toast.makeText(MainActivity.this,"选择的性别是：" + sex, Toast.LENGTH_SHORT).show();
           }
       });*/


        //ToggleButton（开关按钮）
     /*   setContentView(R.layout.togglebutton_layout);
        mLikeTb = findViewById(R.id.like_tb);

        // 为开关按钮设置OnCheckedChangeListener监听器
        mLikeTb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // 消息提示
                if (compoundButton.isChecked()) {
                    Toast.makeText(MainActivity.this,
                            "喜欢Android开发", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this,
                            "不喜欢Android开发", Toast.LENGTH_SHORT).show();
                }



            }
        });
*/
           //Switch是一个可以在两种状态切换之间切换的开关控件
        /*   setContentView(R.layout.switch_layout);
           mBluetoothSwitch = findViewById(R.id.bluetooth_switch);
           mBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                   if (compoundButton.isChecked()){
                        Toast.makeText(MainActivity.this,"打开蓝牙", Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(MainActivity.this, "关闭蓝牙", Toast.LENGTH_SHORT).show();
                   }
               }
           });*/

         //图片image
        //setContentView(R.layout.image_layout);


        //播放暂停
       /* setContentView(R.layout.imagebutton_layout);
        mControlIb = findViewById(R.id.control_ib);
        // 为图标按钮绑定OnClickListener监听器
        mControlIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFlag){
                    Toast.makeText(MainActivity.this, "暂停", Toast.LENGTH_SHORT).show();
                    mControlIb.setImageResource(R.drawable.fast);
                    mFlag = false;
                }else {
                    Toast.makeText(MainActivity.this, "快进", Toast.LENGTH_SHORT).show();
                    mControlIb.setImageResource(R.drawable.pause);
                    mFlag = true;
                }
            }
        });
*/

         //放大、缩小按钮
       /* setContentView(R.layout.zoombutton_layout);
        mMinusZb = findViewById(R.id.minus_zb);
        mPlusZb = findViewById(R.id.plus_zb);
        mControlZc = findViewById(R.id.control_zc);

        // 为缩小按钮绑定OnClickListener监听器
        mMinusZb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "缩小", Toast.LENGTH_SHORT).show();
            }
        });

        // 为放大按钮绑定OnClickListener监听器
        mPlusZb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "放大", Toast.LENGTH_SHORT).show();
            }
        });

        // 为缩放组件绑定OnZoomInClickListener监听器
        mControlZc.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "放大", Toast.LENGTH_SHORT).show();
            }
        });

        // 为缩放组件绑定OnZoomInClickListener监听器
        mControlZc.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "缩小", Toast.LENGTH_SHORT).show();
            }
        });
*/



       //自定义view
       //  setContentView(R.layout.counterview_layout);

        //linear线性布局
        //setContentView(R.layout.linear_main);


        //gravity对齐
       // setContentView(R.layout.gravity);

        //layout_gravity
        //setContentView(R.layout.layout_gravity);

        //padding与margin
        //setContentView(R.layout.padding);

        //relativeLayout相对布局
        //setContentView(R.layout.relative_layout);

        //TableLayout表格布局
        //setContentView(R.layout.table_layout);

        //FrameLayout帧布局
        //setContentView(R.layout.frame_layout);


        //AbsoluteLayout绝对布局
        //setContentView(R.layout.absolute_layout);

        //GridLayout网格布局
        setContentView(R.layout.grid_layout);

       //获取设备的系统配置信息，以及如何响应系统设置更改
        /*setContentView(R.layout.system_event_layout);
        // 获取界面组件
        TextView textView = findViewById(R.id.configuration_tv);

        // 获取系统的Configuration对象
        Configuration cfg = getResources().getConfiguration();
        // 获取系统的配置信息
        StringBuffer status = new StringBuffer();

        status.append("屏幕密度：" + cfg.densityDpi + "\n");
        status.append("字体缩放因子：" + cfg.fontScale + "\n");
        status.append("硬键盘是否可见：" + cfg.hardKeyboardHidden + "\n");
        status.append("键盘类型：" + cfg.keyboard + "\n");
        status.append("当前键盘是否可用：" + cfg.keyboardHidden + "\n");
        status.append("语言环境：" + cfg.locale + "\n");
        status.append("移动信号的国家码：" + cfg.mcc + "\n");
        status.append("移动信号的网络码：" + cfg.mnc + "\n");
        status.append("方向导航设备的类型：" + cfg.navigation + "\n");
        status.append("方向导航设备是否可用：" + cfg.navigationHidden + "\n");
        status.append("系统屏幕的方向：" + cfg.orientation + "\n");
        status.append("屏幕可用高：" + cfg.screenHeightDp + "\n");
        status.append("屏幕可用宽：" + cfg.screenWidthDp + "\n");
        status.append("系统触摸屏的触摸方式：" + cfg.touchscreen + "\n");
        status.append("screenLayout：" + cfg.screenLayout + "\n");
        status.append("smallestScreenWidthDp：" + cfg.smallestScreenWidthDp + "\n");
        status.append("uiMode：" + cfg.uiMode + "\n");

        // 配置信息输出
        textView.setText(status.toString());*/

    }

    // 重写onConfigurationChanged方法，用于监听系统设置的更改
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 获取更改后的屏幕方向
        int screen = newConfig.orientation;
        String ori = (Configuration.ORIENTATION_LANDSCAPE == screen) ? "横屏" : "竖屏";
        // 消息提示
        Toast.makeText(this, "系统的屏幕方向改变为：" + ori, Toast.LENGTH_SHORT).show();
    }





    @Override
    public void onPause() { //暂停
        super.onPause();
    }

    @Override
    public void onResume() {//恢复启动
        super.onResume();

    }

    @Override
    protected void onStop() {//停止
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //当我们的activity开始Stop，系统会调用 onSaveInstanceState() ，Activity可以用键值对的集合来保存状态信息。
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //恢复实例状态
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onStart() {//启动
        super.onStart();
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {

        }
    }

    @Override
    protected void onRestart() {//重启
        super.onRestart();
    }

    @Override
    public void onDestroy() { //销毁程序
        super.onDestroy();
        android.os.Debug.stopMethodTracing();
    }

   //跳转activity
    public void sendMessage(View view){
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        EditText textView =  findViewById(R.id.edit_message);
        String message = textView.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                str = "1";
                textview.setText(str);
                break;
            case R.id.button1:
                str = "2";
                textview.setText(str);
                break;
            case R.id.login_btn:
                // 获取用户输入的用户名和密码
                String name = mNameEt.getText().toString();
                String password = mPasswordEt.getText().toString();
                // 消息提示
                Toast.makeText(MainActivity.this,"用户名：" + name + "\n密码：" + password, Toast.LENGTH_SHORT).show();
                break;
        }
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
       showSelectCity(compoundButton);
    }

    /**
     * 提示用户选择的城市
     * @param compoundButton
     */
    private void showSelectCity(CompoundButton compoundButton){
        // 获取复选框的文字提示
        String city = compoundButton.getText().toString();
        // 根据复选框的选中状态进行相应提示
        if(compoundButton.isChecked()) {
            Toast.makeText(MainActivity.this, "选中" + city, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "取消选中" + city, Toast.LENGTH_SHORT).show();
        }

    }




}
