package cn.gmuni.sc.module.schedule.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gmuni.sc.R;
import cn.gmuni.sc.utils.PixelUtil;

public class SemesterSelectWidget extends LinearLayout {
    private View view;
    private Context context;
    private TextView title;
//    private TextView semesterOneTxt;
//    private TextView semesterTwoTxt;
    private ImageView semesterOneImg;
    private ImageView semesterTwoImg;
    private LinearLayout containerOne,containerTwo;
    private ImageView semesterOneCheck,semesterTwoCheck;
    private String titleStr;
    private int img1Id, img2Id;

    /**
     * 是否选择当前的学年
     */
    private boolean isSelectYear = false;

    /**
     * 选择学期
     */
    private String selectSemester = "1";

    public SemesterSelectWidget(Context context, int img1Id, int img2Id, String title) {
        super(context);
        this.context = context;
        this.img1Id = img1Id;
        this.img2Id = img2Id;
        this.titleStr = title;
        init();
    }

    private void init(){
        LayoutInflater inflater  = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.widget_semester_select, this);
        title = view.findViewById(R.id.semester_select_semester_title);
        semesterOneImg = view.findViewById(R.id.semester_select_semester_img_one);
        semesterTwoImg = view.findViewById(R.id.semester_select_semester_img_two);
        semesterOneCheck = view.findViewById(R.id.semester_select_semester_img_one_choose);
        semesterTwoCheck = view.findViewById(R.id.semester_select_semester_img_two_choose);
        containerOne = view.findViewById(R.id.semester_select_semester_container_one);
        containerTwo = view.findViewById(R.id.semester_select_semester_container_two);
        title.setText(titleStr + "学年");
        semesterOneImg.setImageBitmap(getRoundBitmapByShader(BitmapFactory.decodeResource(getResources(), img1Id),PixelUtil.dp2px(1), R.color.semester_unselect ));
        semesterTwoImg.setImageBitmap(getRoundBitmapByShader(BitmapFactory.decodeResource(getResources(), img2Id),PixelUtil.dp2px(1), R.color.semester_unselect ));
        semesterOneCheck.setVisibility(View.INVISIBLE);
        semesterTwoCheck.setVisibility(View.INVISIBLE);
        containerOne.setVisibility(View.INVISIBLE);
        containerTwo.setVisibility(View.INVISIBLE);
    }
    public void showWhat(String semesterIdx){
        switch (semesterIdx){
            case "1":
                containerOne.setVisibility(View.VISIBLE);
                break;
            case "2":
                containerTwo.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void checkSemester(String semesterIdx){
        switch (semesterIdx){
            case "1":
                containerOne.callOnClick();
                break;
            case "2":
                containerTwo.callOnClick();
                break;
        }
    }

    public boolean isSelectYear() {
        return isSelectYear;
    }

    public String getSelectSemester() {
        return selectSemester;
    }
    public String getAcademicYear() {
        return titleStr;
    }

    public void unckeckAll(){
        semesterOneImg.setImageBitmap(getRoundBitmapByShader(BitmapFactory.decodeResource(getResources(), img1Id),PixelUtil.dp2px(1), R.color.semester_unselect ));
        semesterTwoImg.setImageBitmap(getRoundBitmapByShader(BitmapFactory.decodeResource(getResources(), img2Id),PixelUtil.dp2px(1), R.color.semester_unselect ));
        semesterOneCheck.setVisibility(View.INVISIBLE);
        semesterTwoCheck.setVisibility(View.INVISIBLE);
        isSelectYear = false;
    }
    public void setSelectListener(View.OnClickListener clickListener){
        containerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                unckeckAll();
                semesterOneImg.setImageBitmap(getRoundBitmapByShader(BitmapFactory.decodeResource(getResources(), img1Id),PixelUtil.dp2px(1), R.color.light_green ));
                semesterOneCheck.setVisibility(View.VISIBLE);
                isSelectYear = true;
                selectSemester = "1";
            }
        });
        containerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v);
                unckeckAll();
                semesterTwoImg.setImageBitmap(getRoundBitmapByShader(BitmapFactory.decodeResource(getResources(), img2Id),PixelUtil.dp2px(1), R.color.light_green ));
                semesterTwoCheck.setVisibility(View.VISIBLE);
                isSelectYear = true;
                selectSemester = "2";
            }
        });
    }
    /**
     * 通过BitmapShader 圆角边框
     * @param bitmap
     * @param boarder
     * @return
     */
    private Bitmap getRoundBitmapByShader(Bitmap bitmap, int boarder, int intColor) {
        if (bitmap == null) {
            return null;
        }
        int dp1Px = PixelUtil.dp2px(1);
        int radius = PixelUtil.dp2px(5);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int outWidth = width - dp1Px;
        int outHeight = height - dp1Px;
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        //创建输出的bitmap
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //创建着色器
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //给着色器配置matrix
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        //创建矩形区域并且预留出border
        RectF rect = new RectF(boarder, boarder, outWidth - boarder, outHeight - boarder);
        //把传入的bitmap绘制到圆角矩形区域内
        canvas.drawRoundRect(rect, radius, radius, paint);

        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(getResources().getColor(intColor));
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawRoundRect(rect, radius, radius, boarderPaint);
        }
        return desBitmap;
    }

}
