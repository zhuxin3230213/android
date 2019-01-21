package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import cn.gmuni.sc.R;

public class CustomEmptyDataView extends RelativeLayout{
    private Context context;
    private View view;

    private String text;

    private int image;

    private CustomImageView imageView;

    private TextView textView;

    public CustomEmptyDataView(Context context) {
        this(context,null);
    }

    public CustomEmptyDataView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomEmptyDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public void init(AttributeSet attrs){
        view = LayoutInflater.from(context).inflate(R.layout.widget_empty_data_view, this);
        imageView = view.findViewById(R.id.empty_data_image);
        textView = view.findViewById(R.id.empty_data_text);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEmptyDataView);
        text = typedArray.getString(R.styleable.CustomEmptyDataView_empty_text);
        if(null==text ||"".equals(text)){
            text = getResources().getString(R.string.page_404);
        }
        image = typedArray.getResourceId(R.styleable.CustomEmptyDataView_empty_image,R.drawable.icon_404);
        setText(text);
        setImage(image);
    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setImage(int res){
        imageView.setImageResource(res);
    }
}
