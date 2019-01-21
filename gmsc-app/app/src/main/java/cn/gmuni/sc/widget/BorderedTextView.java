package cn.gmuni.sc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import cn.gmuni.sc.R;

public class BorderedTextView extends AppCompatTextView {

    private boolean showLeft = false;
    private boolean showRight = false;
    private boolean showTop = false;
    private boolean showBottom = false;

    private float leftWidth = 0.5f;
    private float rightWidth = 0.5f;
    private float topWidth = 0.5f;
    private float bottomWidth = 0.5f;

    private String leftColor = "0x000000";
    private String rightColor = "0x000000";
    private String topColor = "0x000000";
    private String bottomColor = "0x000000";

    private String leftType = "solid";
    private String rightType = "solid";
    private String topType = "solid";
    private String bottomType = "solid";

    private Context context;

    private int radius;

    private Object data;

    private int bgColor = 0xffffff;

    public BorderedTextView(Context context) {
        super(context);
        this.context = context;
    }

    public BorderedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BorderedTextView);
        boolean showLeft = typedArray.getBoolean(R.styleable.BorderedTextView_show_left, this.showLeft);
        boolean showRight = typedArray.getBoolean(R.styleable.BorderedTextView_show_right, this.showRight);
        boolean showTop = typedArray.getBoolean(R.styleable.BorderedTextView_show_top, this.showTop);
        boolean showBottom = typedArray.getBoolean(R.styleable.BorderedTextView_show_bottom, this.showBottom);

        float leftWidth = typedArray.getFloat(R.styleable.BorderedTextView_left_width, this.leftWidth);
        float rightWidth = typedArray.getFloat(R.styleable.BorderedTextView_right_width, this.rightWidth);
        float topWidth = typedArray.getFloat(R.styleable.BorderedTextView_top_width, this.topWidth);
        float bottomWidth = typedArray.getFloat(R.styleable.BorderedTextView_bottom_width, this.bottomWidth);

        String leftColor = typedArray.getString(R.styleable.BorderedTextView_left_color);
        String rightColor = typedArray.getString(R.styleable.BorderedTextView_right_color);
        String topColor = typedArray.getString(R.styleable.BorderedTextView_top_color);
        String bottomColor = typedArray.getString(R.styleable.BorderedTextView_bottom_color);

        String leftType = typedArray.getString(R.styleable.BorderedTextView_left_type);
        String rightType = typedArray.getString(R.styleable.BorderedTextView_right_type);
        String topType = typedArray.getString(R.styleable.BorderedTextView_top_type);
        String bottomType = typedArray.getString(R.styleable.BorderedTextView_bottom_type);

        this.showLeft = showLeft;
        this.showRight = showRight;
        this.showTop = showTop;
        this.showBottom = showBottom;

        this.leftWidth = leftWidth;
        this.rightWidth = rightWidth;
        this.topWidth = topWidth;
        this.bottomWidth = bottomWidth;

        this.leftColor = leftColor;
        this.rightColor = rightColor;
        this.topColor = topColor;
        this.bottomColor = bottomColor;

        this.leftType = leftType;
        this.rightType = rightType;
        this.topType = topType;
        this.bottomType = bottomType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (radius > 0) {
            GradientDrawable mDrawable;
            mDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{bgColor, bgColor});
            mDrawable.setShape(GradientDrawable.RECTANGLE);//设置形状为矩形
            mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            mDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});//设置4角的圆角半径值
            //创建矩形区域并且预留出border
            Rect rect = new Rect(0, 0, 0, 0);
            mDrawable.setBounds(rect);//设置位置大小
            mDrawable.draw(canvas);//绘制到canvas上
            this.setBackground(mDrawable);
        } else {
            if (showLeft) {
                float[] points = {0, 0, 0, this.getHeight() - leftWidth};
                drawLine(canvas, points, leftType, leftColor, leftWidth);
            }
            if (showRight) {
                float[] points = {this.getWidth() - leftWidth, 0, this.getWidth() - leftWidth, this.getHeight() - leftWidth};
                drawLine(canvas, points, rightType, rightColor, rightWidth);
            }
            if (showTop) {
                float[] points = {0, 0, this.getWidth() - leftWidth, 0};
                drawLine(canvas, points, topType, topColor, topWidth);
            }
            if (showBottom) {
                float[] points = {0, this.getHeight() - leftWidth, this.getWidth() - leftWidth, this.getHeight() - leftWidth};
                drawLine(canvas, points, bottomType, bottomColor, bottomWidth);
            }
        }

        super.onDraw(canvas);
    }

    /**
     * @param canvas
     * @param points
     * @param type
     * @param color
     */
    private void drawLine(Canvas canvas, float[] points, String type, String color, float width) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor(color));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(width);
        if (!"solid".equalsIgnoreCase(type)) {
            paint.setPathEffect(new DashPathEffect(new float[]{6, 6}, 0));
        }
        canvas.drawLine(points[0], points[1], points[2], points[3], paint);
    }

    public void setShowLeft(boolean showLeft) {
        this.showLeft = showLeft;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
    }

    public void setShowTop(boolean showTop) {
        this.showTop = showTop;
    }

    public void setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
    }

    public void setLeftWidth(float leftWidth) {
        this.leftWidth = leftWidth;
    }

    public void setRightWidth(float rightWidth) {
        this.rightWidth = rightWidth;
    }

    public void setTopWidth(float topWidth) {
        this.topWidth = topWidth;
    }

    public void setBottomWidth(float bottomWidth) {
        this.bottomWidth = bottomWidth;
    }

    public void setLeftColor(String leftColor) {
        this.leftColor = leftColor;
    }

    public void setRightColor(String rightColor) {
        this.rightColor = rightColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public void setBottomColor(String bottomColor) {
        this.bottomColor = bottomColor;
    }

    public void setLeftType(String leftType) {
        this.leftType = leftType;
    }

    public void setRightType(String rightType) {
        this.rightType = rightType;
    }

    public void setTopType(String topType) {
        this.topType = topType;
    }

    public void setBottomType(String bottomType) {
        this.bottomType = bottomType;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
