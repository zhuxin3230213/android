package cn.gmuni.sc.module.lost;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.utils.BitMapUtil;
import cn.gmuni.sc.utils.DateUtils;
import cn.gmuni.sc.utils.StringUtil;

public class LostFoundDetailsActivity extends BaseCommonActivity {

    @BindView(R.id.lost_found_details_title)
    TextView titleText;

    @BindView(R.id.lost_found_details_content_text)
    TextView contentText;

    @BindView(R.id.lost_found_details_address_text)
    TextView addressText;

    @BindView(R.id.lost_found_details_time_text)
    TextView timeText;

    @BindView(R.id.lost_found_details_user_text)
    TextView userText;

    @BindView(R.id.lost_found_details_type_img)
    ImageView typeImg;

    @BindView(R.id.lost_found_details_content_image)
    ImageView imageView;
    @Override
    public int getToolbar() {
        return R.id.lost_found_edit_toolbar;
    }

    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lost_found_details);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        LostFoundModel model = (LostFoundModel)extras.getSerializable("info");
        titleText.setText(model.getObjName());

        if(!StringUtil.isEmpty(model.getImage())){
            Bitmap bmp = BitMapUtil.base64ToBitmap(model.getImage());
            System.out.println(bmp.getWidth() + "*****" + bmp.getHeight());
            imageView.setImageBitmap(bmp);
        }else{
            imageView.setVisibility(View.INVISIBLE);
        }
        contentText.setText(model.getDescription());
        addressText.setText(model.getObjAddress());
        timeText.setText(DateUtils.date2String(model.getObjTime(), "yyyy-MM-dd"));
        userText.setText(model.getUserInfo());
        if ("1".equals(model.getLfType())) {
            typeImg.setImageResource(R.drawable.image_found);
        } else {
            typeImg.setImageResource(R.drawable.image_loat);
        }
    }
}
