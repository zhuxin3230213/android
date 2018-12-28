package cn.gmuni.sc.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import cn.gmuni.sc.R;
import cn.gmuni.sc.utils.PixelUtil;


public class CustomRadioButton extends AppCompatRadioButton {
    public CustomRadioButton(Context context) {
        super(context);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        setButtonDrawable(R.drawable.radio_button_style);
        setBackgroundDrawable(null);
        setPadding(PixelUtil.dp2px(5), 0, 0, 0);


    }

}
