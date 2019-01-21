package cn.gmuni.sc.widget.clipimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.gmuni.sc.utils.PixelUtil;

public class ClipPointView extends View {

    private Paint mPaint;


    public ClipPointView(Context context) {
        this(context,null);
    }

    public ClipPointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(PixelUtil.dp2px(20),PixelUtil.dp2px(20),PixelUtil.dp2px(10),mPaint);
        super.onDraw(canvas);
    }
}
