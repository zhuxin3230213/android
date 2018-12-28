package cn.gmuni.sc.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnTouch;
import cn.gmuni.sc.R;
import cn.gmuni.sc.widget.clipimage.ClipImageBorderView;
import cn.gmuni.sc.widget.clipimage.ClipPointView;
import cn.gmuni.sc.module.me.personal_details.PersionalDetailsActivity;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.PixelUtil;

public class BaseClipImageActivity extends BaseCommonActivity  implements GestureDetector.OnGestureListener {

    @BindView(R.id.clip_avatar_wrap)
    FrameLayout layout;

    ImageView bgImg;

    @BindView(R.id.clip_avatar_modal)
    ClipImageBorderView imgView;

    @BindView(R.id.clip_avatar_point_left_top)
    ClipPointView leftTopPointView;

    @BindView(R.id.clip_avatar_point_left_bottom)
    ClipPointView leftBottomPointView;

    @BindView(R.id.clip_avatar_point_right_top)
    ClipPointView rightTopPointView;

    @BindView(R.id.clip_avatar_point_right_bottom)
    ClipPointView rightBottomPointView;


    private Bitmap bitmap = null;

    /**
     * 鼠标在屏幕上面滑动起始位置
     */
    private float beginX;

    private float beginY;

    public GestureDetector gestureDetector;

    @Override
    public void initContentView(Bundle savedInstanceState) {
       setContentView(R.layout.activity_clip_image);
       gestureDetector = new GestureDetector(getContext(),this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        bitmap = BitmapFactory.decodeFile(path);
        setBgImg();
        bindEvent();
    }

    private void bindEvent(){
        //确定按钮
        toolbar.setOnActionListener(v -> {
            float paddingLeft = imgView.getPdLeft();
            float paddingTop = imgView.getPdTop();
            float[] wrap = imgView.getWrapSize();
            float width = imgView.getLayoutSize();
            Bitmap bt = BitMapUtil.cropBitmap(bitmap, width, width, (int) (paddingLeft - wrap[0]), (int) (paddingTop - wrap[2]));
            String base64 = BitMapUtil.bitmapToBase64(bt);
            Intent intent = new Intent();
            intent.putExtra("img",base64);
            setResult(PersionalDetailsActivity.CODE_RESULT_REQUEST,intent);
            finish();
        });
    }

    /**
     * 设置背景图片
     */
    private void setBgImg(){
        ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                float layoutHeight = layout.getHeight();
                float layoutWidth = layout.getWidth();
                float width = bitmap.getWidth();
                float height = bitmap.getHeight();
                bgImg = new ImageView(getContext());
                float scale;
                FrameLayout.LayoutParams layoutParams;
                if(width*1f/height > layoutWidth*1f/layoutHeight){
                    layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }else{
                    layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                if(layoutWidth/width*height<layoutHeight){
                    scale = layoutWidth/width;
                }else{
                    scale = layoutHeight/height;
                }
                //对图片进行等比例缩放
                bitmap = BitMapUtil.scaleBitmap(bitmap,scale);
                layoutParams.gravity = Gravity.CENTER;
                bgImg.setLayoutParams(layoutParams);
                layout.addView(bgImg,0);
                bgImg.setImageBitmap(bitmap);
                //计算图片的高度宽度
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                bgImg.measure(w,h);
                //获取图片的高度宽度
                int imgWidth = bgImg.getMeasuredWidth();
                int imgHeight = bgImg.getMeasuredHeight();
                //计算布局
                float left = (layoutWidth - imgWidth)/2;
                float right = left + imgWidth;
                float top = (layoutHeight - imgHeight)/2;
                float bottom = imgHeight + top;
                imgView.setImageRound(left,right,top,bottom);
                layout.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        //在imgView渲染完成后，调用layoutPoint调整四个原点位置
        viewTreeObserver.addOnGlobalLayoutListener(() -> layoutPoint());

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            leftBottomPointView.setVisibility(View.GONE);
            rightBottomPointView.setVisibility(View.GONE);
            leftTopPointView.setVisibility(View.GONE);
            rightTopPointView.setVisibility(View.GONE);
        }
        //拖拽
        if(event.getAction() == MotionEvent.ACTION_UP){
            layoutPoint();
            leftBottomPointView.setVisibility(View.VISIBLE);
            rightBottomPointView.setVisibility(View.VISIBLE);
            leftTopPointView.setVisibility(View.VISIBLE);
            rightTopPointView.setVisibility(View.VISIBLE);
        }
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public int getToolbar() {
        return R.id.clip_avatar_toolbar;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        beginX = e.getX();
        beginY =  e.getY();
        return false;
    }


    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    //滑动事件
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 1;         //定义最小滑动距离
        float minVelocity = 0;      //定义最小滑动速度
        float moveSpec = 1;// 位移比例
        float endX = e2.getX();
        float endY = e2.getY();
        if(beginX-endX>minMove&&Math.abs(velocityX)>minVelocity){   //左滑
            imgView.moveLeft((int) ((beginX - endX)*moveSpec));
        }
        if(endX-beginX>minMove&&Math.abs(velocityX)>minVelocity){   //右滑
            imgView.moveRight((int) ((endX - beginX)*moveSpec));
        }
        if(beginY-endY>minMove&&Math.abs(velocityY)>minVelocity){   //上滑
            imgView.moveTop((int) ((beginY - endY)*moveSpec));
        }
        if(endY-beginY>minMove&&Math.abs(velocityY)>minVelocity){   //下滑
            imgView.moveBottom((int) ((endY - beginY)*moveSpec));
        }
        beginY = endY;
        beginX = endX;
        return false;
    }


    /**
     * 布局四个锚点位置
     */
    private void layoutPoint(){
        float left = imgView.getPdLeft();
        float top = imgView.getPdTop();
        float width = imgView.getLayoutSize();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(PixelUtil.dp2px(40),PixelUtil.dp2px(40));
        layoutParams.setMargins((int)(left-PixelUtil.dp2px(15)),(int)(top-PixelUtil.dp2px(20)),0,0);
        leftTopPointView.setLayoutParams(layoutParams);
        //右上角
        layoutParams = new FrameLayout.LayoutParams(PixelUtil.dp2px(40),PixelUtil.dp2px(40));
        layoutParams.setMargins((int)(left+width - PixelUtil.dp2px(20)),(int)(top-PixelUtil.dp2px(20)),0,0);
        rightTopPointView.setLayoutParams(layoutParams);
        //左下角
        layoutParams = new FrameLayout.LayoutParams(PixelUtil.dp2px(40),PixelUtil.dp2px(40));
        layoutParams.setMargins((int)(left - PixelUtil.dp2px(20)),(int)(width + top - PixelUtil.dp2px(20)),0,0);
        leftBottomPointView.setLayoutParams(layoutParams);
        //右下角
        layoutParams = new FrameLayout.LayoutParams(PixelUtil.dp2px(40),PixelUtil.dp2px(40));
        layoutParams.setMargins((int)(left+width - PixelUtil.dp2px(20)),(int)(width + top-PixelUtil.dp2px(20)),0,0);
        rightBottomPointView.setLayoutParams(layoutParams);
    }


    @OnTouch({R.id.clip_avatar_point_left_top,R.id.clip_avatar_point_left_bottom,R.id.clip_avatar_point_right_top,R.id.clip_avatar_point_right_bottom})
    public boolean onMovePoint(View v ,MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            beginX = event.getX();
            beginY = event.getY();
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            int endX = (int) event.getX();
            int endY = (int) event.getY();
            int x = (int) (endX - beginX);
            int y = (int) (endY - beginY);
            int absX = Math.abs(x);
            int absY = Math.abs(y);
            switch (v.getId()){
                case R.id.clip_avatar_point_left_top:
                    //向右下方或左上方移动
                    if((absX == x && absY == y)||(absX != x && absY!=y)){
                        imgView.resize(x,ClipImageBorderView.LEFT_TOP);
                    }
                    break;
                case R.id.clip_avatar_point_left_bottom:
                    //向左下方或右上方移动
                    if((absX != x && absY == y)||(absX == x && absY != y)){
                        imgView.resize(x,ClipImageBorderView.LEFT_BOTTOM);
                    }
                    break;
                case R.id.clip_avatar_point_right_top:
                    //向左下方或右上方移动
                    if((absX != x && absY == y)||(absX == x && absY != y)){
                        imgView.resize(x,ClipImageBorderView.RIGHT_TOP);
                    }
                    break;
                case R.id.clip_avatar_point_right_bottom:
                    //向右下方或左上方移动
                    if((absX == x && absY == y)||(absX != x && absY!=y)){
                        imgView.resize(x,ClipImageBorderView.RIGHT_BOTTOM);
                    }
                    break;
            }
            beginX = endX;
            beginY = endY;
            layoutPoint();

        }else if(event.getAction() == MotionEvent.ACTION_UP){

        }
        return true;
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
