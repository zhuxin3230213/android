package cn.gmuni.sc.module.lost;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gmuni.sc.R;
import cn.gmuni.sc.base.BaseCommonActivity;
import cn.gmuni.sc.listeners.OnMultiClickListener;
import cn.gmuni.sc.model.LostFoundModel;
import cn.gmuni.sc.module.lost.fragment.LostFoundFragmentAll;
import cn.gmuni.sc.module.lost.fragment.LostFoundFragmentFound;
import cn.gmuni.sc.module.lost.fragment.LostFoundFragmentLost;
import cn.gmuni.sc.widget.CustomButton;

public class LostFoundActivity extends BaseCommonActivity {
    //发布按钮
    @BindView(R.id.lost_found_publish_new)
    CustomButton pubishBtn;
    public final static int RESULT_CODE = 3;
    public final static int MINE_RESULT_CODE = 13;
    private FragmentManager fragmentManager;
    //所有
    @BindView(R.id.lost_found_tab_nav_all)
    TextView allTextBtn;
    @BindView(R.id.lost_found_tab_nav_all_underline)
    TextView allTextUnderLine;
    LostFoundFragmentAll allFragment;
    //捡到
    @BindView(R.id.lost_found_tab_nav_found)
    TextView foundTextBtn;
    @BindView(R.id.lost_found_tab_nav_found_underline)
    TextView foundTextUnderLine;
    LostFoundFragmentFound foundFragment;
    //丢失
    @BindView(R.id.lost_found_tab_nav_lost)
    TextView lostTextBtn;
    @BindView(R.id.lost_found_tab_nav_lost_underline)
    TextView lostTextUnderLine;
    LostFoundFragmentLost lostFragment;

    //fragment的标记
    private static final String ALL = "lost_found_frag_all";
    private static final String LOST = "lost_found_frag_lost";
    private static final String FOUND = "lost_found_frag_found";
    private String selectedTab;
    @Override
    public void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lost_found);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        fragmentManager = getBaseFragmentManager();
        bindEvent();
        switchFragment(ALL);
    }

    //切换Fragment
    private void switchFragment(String index) {
        selectedTab = index;
        hideAllFragment();
        switch (index) {
            case ALL:
                if (allFragment == null) {
                    allFragment = new LostFoundFragmentAll();
                    addFragment(R.id.lost_found_content_fram, allFragment, ALL);
                }
                showFragment(allFragment);
                break;
            case FOUND:
                if (foundFragment == null) {
                    foundFragment = new LostFoundFragmentFound();
                    addFragment(R.id.lost_found_content_fram, foundFragment, FOUND);
                }
                showFragment(foundFragment);
                break;
            case LOST:
                if (lostFragment == null) {
                    lostFragment = new LostFoundFragmentLost();
                    addFragment(R.id.lost_found_content_fram, lostFragment, LOST);
                }
                showFragment(lostFragment);
                break;

        }
    }

    /**
     * 隐藏所有的Fragment
     */
    private void hideAllFragment() {
        if (allFragment != null) {
            hideFragment(allFragment);
        }
        if (lostFragment != null) {
            hideFragment(lostFragment);
        }
        if (foundFragment != null) {
            hideFragment(foundFragment);
        }
    }

    private void bindEvent() {
        toolbar.setOnActionListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startActivityForResult(new Intent(getContext(), LostFoundMineActivity.class), MINE_RESULT_CODE);
            }
        });
        pubishBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                startActivityForResult(new Intent(getContext(), LostFoundPublishActivity.class), RESULT_CODE);
            }
        });
        allTextBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (!ALL.equals(selectedTab)) {
                    allTextBtn.setTextColor(getResources().getColor(R.color.fontColorGeneric));
                    foundTextBtn.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    lostTextBtn.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    allTextUnderLine.setVisibility(View.VISIBLE);
                    foundTextUnderLine.setVisibility(View.INVISIBLE);
                    lostTextUnderLine.setVisibility(View.INVISIBLE);
                    switchFragment(ALL);
                }
            }
        });
        foundTextBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (!FOUND.equals(selectedTab)) {
                    allTextBtn.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    foundTextBtn.setTextColor(getResources().getColor(R.color.fontColorGeneric));
                    lostTextBtn.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    allTextUnderLine.setVisibility(View.INVISIBLE);
                    foundTextUnderLine.setVisibility(View.VISIBLE);
                    lostTextUnderLine.setVisibility(View.INVISIBLE);
                    switchFragment(FOUND);
                }
            }
        });
        lostTextBtn.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (!LOST.equals(selectedTab)) {
                    allTextBtn.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    foundTextBtn.setTextColor(getResources().getColor(R.color.fontColorMinor));
                    lostTextBtn.setTextColor(getResources().getColor(R.color.fontColorGeneric));
                    allTextUnderLine.setVisibility(View.INVISIBLE);
                    foundTextUnderLine.setVisibility(View.INVISIBLE);
                    lostTextUnderLine.setVisibility(View.VISIBLE);
                    switchFragment(LOST);
                }
            }
        });
    }

    @Override
    public int getToolbar() {
        return R.id.lost_found_toolbar;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE) {
            LostFoundModel lost = (LostFoundModel) data.getExtras().getSerializable("lost_found");
            allFragment.addListItem(lost);
            if(lostFragment != null && "0".equals(lost.getLfType())){
                lostFragment.addListItem(lost);
            }
            if(foundFragment != null && "1".equals(lost.getLfType())){
                foundFragment.addListItem(lost);
            }
        }
        if(resultCode == MINE_RESULT_CODE){
            List<String> deletedIds = (ArrayList<String>)data.getExtras().getSerializable("deleted_ids");
            allFragment.deleteItems(deletedIds);
            if(lostFragment != null){
                lostFragment.deleteItems(deletedIds);
            }
            if(foundFragment != null){
                foundFragment.deleteItems(deletedIds);
            }
            List<String> finishedIds = (ArrayList<String>)data.getExtras().getSerializable("finished_ids");
            allFragment.finishedItems(finishedIds);
            if(lostFragment != null){
                lostFragment.finishedItems(finishedIds);
            }
            if(foundFragment != null){
                foundFragment.finishedItems(finishedIds);
            }
        }
    }
}
