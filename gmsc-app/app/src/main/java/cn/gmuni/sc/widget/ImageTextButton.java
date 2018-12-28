package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gmuni.sc.R;

public class ImageTextButton extends LinearLayout {


    private TextView textView;

    private ImageView imageView;

    private Context context;

    private String title;

    private int imgSrc;

    private View view;

    private OnClickListener listener;


    public ImageTextButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ImageTextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ImageTextButton);
        title = typedArray.getString(R.styleable.ImageTextButton_title);
        if(title!=null){
            this.textView.setText(title);
        }
        imgSrc = typedArray.getResourceId(R.styleable.ImageTextButton_img_src,R.drawable.icon_news);
        setImageResource(imgSrc);
    }

    private void initView(){
        view = View.inflate(context, R.layout.widget_image_text_button, this);
        this.textView = view.findViewById(R.id.text);
        this.imageView = view.findViewById(R.id.image);
        bindEvent();
    }


    private void bindEvent(){
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                listener.onClick(view);
            }
        });
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view1) {
                listener.onClick(view);
            }
        });

    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }




    public void setImageResource(int res){
        imageView.setImageResource(res);
    }

    public void setTitle(String text){
        textView.setText(text);
    }

    public void setTitle(int res){
        textView.setText(res);
    }


}
