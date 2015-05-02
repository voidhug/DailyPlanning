package voidhug.test;

import java.util.ArrayList;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by voidhug on 15/5/2.
 */
public class ScheduleShow extends FragmentActivity implements View.OnClickListener {

    private ViewPager mPager;// 页卡内容,即主要显示内容的画面
    private ImageView cursor;// 动画图片
    private TextView tv_monday, tv_tuesday, tv_wednesday, tv_thursday, tv_friday;
    private int bmpW;// 动画图片宽度
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_schedule_show);
        initView();
        initWidth();
        initViewPager();
    }

    private void initView() {
        cursor = (ImageView) findViewById(R.id.cursor);

        tv_monday = (TextView) findViewById(R.id.tv_monday);
        tv_tuesday = (TextView) findViewById(R.id.tv_tuesday);
        tv_wednesday = (TextView) findViewById(R.id.tv_wednesday);
        tv_thursday = (TextView) findViewById(R.id.tv_thursday);
        tv_friday = (TextView) findViewById(R.id.tv_friday);

        tv_monday.setOnClickListener(this);
        tv_tuesday.setOnClickListener(this);
        tv_wednesday.setOnClickListener(this);
        tv_thursday.setOnClickListener(this);
        tv_friday.setOnClickListener(this);
    }

    private void initWidth() {

        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a_small)
                .getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 5 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_monday :
                mPager.setCurrentItem(0);
                break;
            case R.id.tv_tuesday :
                mPager.setCurrentItem(1);
                break;
            case R.id.tv_wednesday :
                mPager.setCurrentItem(2);
                break;
            case R.id.tv_thursday :
                mPager.setCurrentItem(3);
                break;
            case R.id.tv_friday :
                mPager.setCurrentItem(4);
                break;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果按下的是返回键，并且没有重复
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
            return false;
        }
        return false;
    }

    private void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        ArrayList<Fragment> fragmentsList = new ArrayList<Fragment>();
        fragmentsList.add(new WeekDayFragment(0));
        fragmentsList.add(new WeekDayFragment(1));
        fragmentsList.add(new WeekDayFragment(2));
        fragmentsList.add(new WeekDayFragment(3));
        fragmentsList.add(new WeekDayFragment(4));

        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(4);
        mPager.setCurrentItem(this.getIntent().getIntExtra("WeekDay", 0));

    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentsList;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public MyFragmentPagerAdapter(FragmentManager fm,
                                      ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }


        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }



    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int three = one * 3;
        int four = one * 4;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            Log.i("arg0", arg0 + "");
            switch (arg0) {// arg0为目的选项卡

                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, two, 0, 0);
                    }
                    break;
                case 3:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, three, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                    } else if (currIndex == 4) {
                        animation = new TranslateAnimation(four, three, 0, 0);
                    }
                    break;
                case 4:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, four, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, four, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, four, 0, 0);
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, four, 0, 0);
                    }
                    break;
            }

            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
