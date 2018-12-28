package cn.gmuni.sc.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import cn.gmuni.sc.R;
import cn.gmuni.sc.app.BaseApplication;

public class CircleBackgroundDrawable extends ShapeDrawable {

    public CircleBackgroundDrawable() {
        super();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        RectF oval = new RectF(80, 260, 200, 300);
        canvas.drawRoundRect(oval, 20, 15, p);

    }
}
