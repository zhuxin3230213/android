package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.gmuni.sc.R;

public class CustomEditText extends ContainsEmojiEditText{

    private String prefixText = "";

    private boolean fitPrefixWidth = false;

    private float prefixWidth = 60;

    private int prefixColor;

    private int paddingLeft;

    private View.OnClickListener mListener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        String title = typedArray.getString(R.styleable.CustomEditText_prefix_text);
        prefixWidth = typedArray.getDimension(R.styleable.CustomEditText_prefix_width,prefixWidth);
        fitPrefixWidth = typedArray.getBoolean(R.styleable.CustomEditText_fix_prefix_width,true);
        prefixColor = typedArray.getColor(R.styleable.CustomEditText_prefix_color, ContextCompat.getColor(getContext(),R.color.fontColorPrimary));
        setFixedText(title);
        setBackground(null);
        setMaxLines(1);
    }

    /**
     * 设置前置文字
     * @param title
     */
    private void setFixedText(String title){
        prefixText = title;
        paddingLeft = getPaddingLeft()+20;
        int left;

        if(!fitPrefixWidth && (null == title || "".equals(title))){
            left = paddingLeft;
        }else{
            if(fitPrefixWidth){
                left = (int) (prefixWidth + paddingLeft);
            }else{
                left = (int) (getPaint().measureText(prefixText) + paddingLeft);
            }
            left += 40;
        }

        setPadding(left,getPaddingTop(),getPaddingRight(),getPaddingBottom());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!TextUtils.isEmpty(prefixText)){
            Paint paint = getPaint();
            paint.setColor(prefixColor);
            canvas.drawText(prefixText,paddingLeft,getBaseline(),paint);
        }
    }

    /**
     * 前缀文字点击事件
     * @param listener
     */
    public void setDrawableClick(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener != null && getCompoundDrawables()[2] != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int i = getMeasuredWidth() - getCompoundDrawables()[2].getIntrinsicWidth();
                    if (event.getX() > i) {
                        mListener.onClick(this);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }
}
