package cn.gmuni.sc.widget.clipimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.gmuni.sc.utils.PixelUtil;

public class ClipImageBorderView extends View {

    public static final int LEFT_TOP = 0;

    public static final int RIGHT_TOP = 1;

    public static final int LEFT_BOTTOM = 2;

    public static final int RIGHT_BOTTOM = 3;

    private Context context;

    //节点最小宽度
    private int minWidth = PixelUtil.dp2px(54);

    //距离左侧距离 默认为20dp
    private float paddingLeft = PixelUtil.dp2px(50);

    //距离顶部高度
    private float paddingTop;


    //中间的正方形的尺寸
    private float width;

    //内部矩形边框宽度
    private int strokeWidth = PixelUtil.dp2px(1);

    private Paint mPaint;

    private float wrapLeft;

    private float wrapRight;

    private float wrapTop;

    private float wrapBottom;


    public ClipImageBorderView(Context context) {
        this(context,null);
    }

    public ClipImageBorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipImageBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width==0){
            width = getWidth() - paddingLeft*2;
            paddingTop = (getHeight() - width)/2;
        }
        int fullHeight =getHeight();
        int fullWidth = getWidth();
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Paint.Style.FILL);
//        // 绘制左边
        canvas.drawRect(0, paddingTop, paddingLeft, paddingTop+width, mPaint);
        //绘制右边
        canvas.drawRect(paddingLeft+width,paddingTop,fullHeight,paddingTop+width,mPaint);
        //绘制上边
        canvas.drawRect(0,0,fullWidth,paddingTop,mPaint);
        //绘制下边
        canvas.drawRect(0,paddingTop+width,fullWidth,fullHeight,mPaint);
        //绘制边框
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(paddingLeft,paddingTop,paddingLeft+width,paddingTop+width,mPaint);

    }

    /**
     * 向左位移size像素
     * @param size
     */
    public void moveLeft(int size){
        paddingLeft -= size;
        if(paddingLeft<=wrapLeft){
            paddingLeft = wrapLeft;
        }
        this.invalidate();
    }

    public void moveTop(int size){
        paddingTop -= size;
        if(paddingTop<=wrapTop){
            paddingTop = wrapTop;
        }
        this.invalidate();
    }

    public void moveRight(int size){
        paddingLeft += size;
        if(paddingLeft+width>wrapRight){
            paddingLeft = wrapRight - width;
        }
        this.invalidate();
    }

    public void moveBottom(int size){
        paddingTop+=size;
        if(paddingTop+width > wrapBottom){
            paddingTop = wrapBottom - width;
        }
        this.invalidate();
    }

    /**
     * 从哪个顶点进行位置调整,从锚点开始往右往下为正数，否则为负数
     * @param size
     * @param point
     */
    public void resize(float size,int point){
        switch (point){
            case LEFT_TOP:
            case LEFT_BOTTOM:
                //如果截取区域小于最小宽度
                if(width - size < minWidth){
                    size = width - minWidth;
                }
                if(point==LEFT_TOP){
                    paddingTop += size;
                }
                paddingLeft += size;
                width -= size;
                break;
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
                //如果截取区域小于最小宽度
                if(width + size < minWidth){
                    size = minWidth - width;
                }
                if(point == RIGHT_TOP){
                    paddingTop-=size;
                }
                width += size;
                break;
        }
        this.invalidate();
    }


    public float getPdLeft(){
        return paddingLeft;
    }

    public float getPdTop(){
        return paddingTop;
    }

    /**
     * 获取中间布局的尺寸
     * @return
     */
    public float getLayoutSize(){
        return width;
    }


    /**
     * 设置中间区域最小宽度
     * @param width
     */
    public void setMinWidth(int width){
        this.minWidth = width;
    }

    public void setImageRound(float left,float right, float top ,float bottom){
        this.wrapLeft = left;
        this.wrapRight = right;
        this.wrapTop = top;
        this.wrapBottom = bottom;
    }

    /**
     * 获取区域边界
     * @return
     */
    public float[] getWrapSize(){
        return new float[]{wrapLeft,wrapRight,wrapTop,wrapBottom};
    }

}
