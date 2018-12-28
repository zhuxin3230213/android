package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;

import cn.gmuni.sc.R;
import cn.gmuni.sc.utils.PixelUtil;

public class CustomButton extends android.support.v7.widget.AppCompatButton {

    public static final String PRIMARY_TYPE = "primary";

    public static final String DEFAULT_TYPE = "default";

    private static final String CUSTOM_TYPE ="custom";

    private static final String TRANSPARENT_TYPE="transparent";

    private Context context;

    private String type;

    private int strokeWidth = 0;

    private int strokeColor = Color.parseColor("#000000");

    //按钮圆角大小
    private float radius;

    private int customPaddingTop = 0;

    private int customPaddingLeft = 0;

    private int customPaddingBottom = 0;

    private int customPaddingRight = 0;

    public CustomButton(Context context) {
        super(context);
        this.context = context;
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        String type = typedArray.getString(R.styleable.CustomButton_type);
        float radius = typedArray.getDimension(R.styleable.CustomButton_radius, PixelUtil.dp2px(5));
        this.customPaddingTop = (int)typedArray.getDimension(R.styleable.CustomButton_custom_padding_top, PixelUtil.dp2px(0));
        this.customPaddingLeft = (int)typedArray.getDimension(R.styleable.CustomButton_custom_padding_left, PixelUtil.dp2px(0));
        this.customPaddingBottom = (int)typedArray.getDimension(R.styleable.CustomButton_custom_padding_bottom, PixelUtil.dp2px(0));
        this.customPaddingRight = (int)typedArray.getDimension(R.styleable.CustomButton_custom_padding_right, PixelUtil.dp2px(0));
        this.type = type;
        this.radius = radius;
        this.strokeWidth = (int) typedArray.getDimension(R.styleable.CustomButton_stroke_width,0);
        this.strokeColor = typedArray.getColor(R.styleable.CustomButton_stroke_color, Color.WHITE);
        this.setGravity(Gravity.CENTER);
        this.setPadding(this.customPaddingLeft,this.customPaddingTop,this.customPaddingRight,this.customPaddingBottom);
        setType();
        setEnabled(this.isEnabled());

    }
    GradientDrawable gradientDrawable = null;
    private void setType(){

        if(null==type || "".equals(type)){
            type = PRIMARY_TYPE;
        }
        switch (type){
            case PRIMARY_TYPE:
                gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{getResources().getColor(R.color.colorGradientStart),
                        getResources().getColor(R.color.colorGradientEnd)});
                break;
            case DEFAULT_TYPE:
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(getResources().getColor(R.color.colorNormalBackground));
                break;
            case TRANSPARENT_TYPE:
                gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.TRANSPARENT);
                break;
        }
        if(!CUSTOM_TYPE.equals(type)){
            gradientDrawable.setCornerRadius(this.radius);
            gradientDrawable.setStroke(strokeWidth,strokeColor);
            setBackgroundDrawable(gradientDrawable);
            //如果设置android:enable 则会先调用enabled，此时背景并没有设置，所以需要再次调用
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        Drawable background = getBackground();
        if(enabled){
            background.setAlpha(255);
        }else{
            background.setAlpha((int) (255*0.4));
        }
        super.setEnabled(enabled);
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        gradientDrawable.setStroke(this.strokeWidth,this.strokeColor);
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        gradientDrawable.setStroke(this.strokeWidth,this.strokeColor);
    }

    public void setType(String type) {
        this.type = type;
        setType();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        gradientDrawable.setCornerRadius(this.radius);
    }
}
