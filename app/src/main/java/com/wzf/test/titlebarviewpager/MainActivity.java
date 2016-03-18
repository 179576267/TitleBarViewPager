package com.wzf.test.titlebarviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private LinearLayout mTitleContainer;
    private ViewPager mViewPager;
    private TextView mBottomLine;
    private int screenWidth = 0;

    private View.OnClickListener mTabClickListener;

    private int mCurrentIndex = 0;

    private MyPagerAdapter myPagerAdapter;
    private List<PageItem> mPages = new ArrayList<PageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }



    private void initUI()
    {
        setContentView(R.layout.main);

        mTitleContainer = (LinearLayout) findViewById(R.id.title_container);
        mBottomLine = (TextView) findViewById(R.id.bottom_line);
        mBottomLine.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                changeIndex(mCurrentIndex);
            }
        });
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        mTabClickListener = new TopTabClickListener();
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        initFragmentPages();
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mPages);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
            }

            @Override
            public void onPageScrolled(int index, float arg1, int pixes) {
                if (pixes != 0) {
                    mBottomLine.layout((int) ((index + arg1) * screenWidth / mPages.size()), 0,
                            (int) ((index + 1 + arg1) * screenWidth / mPages.size()), mBottomLine.getWidth());
                }
                if (pixes == 0) {
                    mCurrentIndex = index;
                    changeIndex(mCurrentIndex);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initFragmentPages() {
        addPage(new PageItem("测试1", new BaseFragment()));
        addPage(new PageItem("测试2", new BaseFragment()));
        addPage(new PageItem("测试3", new BaseFragment()));
    }

    protected void notifyEnvironmentChange() {
        for (PageItem page : mPages) {
            if (page == null) {
                continue;
            }
        }
    }

    protected void addPage(PageItem page)
    {
        if (page == null || TextUtils.isEmpty(page.title) || page.fragment == null)
            return;
        mPages.add(page);
        page.id = mPages.indexOf(page);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextView title = new TextView(this);
        title.setTextColor(getResources().getColor(android.R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp16));
        title.setGravity(Gravity.CENTER);
        int dp5 = getResources().getDimensionPixelSize(R.dimen.dp5);
        title.setPadding(dp5, dp5, dp5, dp5);
        title.setText(page.title);
        title.setTag(page.id);
        title.setOnClickListener(mTabClickListener);
        page.titleView = title;
        mTitleContainer.addView(title, params);
    }

    private class TopTabClickListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            int id = (Integer) view.getTag();
            mViewPager.setCurrentItem(id);
            changeIndex(mViewPager.getCurrentItem());
        }
    }

    private void changeIndex(int index)
    {
        for (int i = 0; i < mPages.size(); i++)
        {
            PageItem page = mPages.get(i);
            if (index == i)
            {
                page.titleView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            }
            else
            {
                page.titleView.setTextColor(getResources().getColor(android.R.color.black));

            }
            mBottomLine.layout((int) (index * screenWidth / mPages.size()), 0,
                    (int) ((index + 1) * screenWidth / mPages.size()), mBottomLine.getWidth());
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter
    {
        private List<PageItem> mPages;

        public MyPagerAdapter(FragmentManager fm, List<PageItem> pages)
        {
            super(fm);
            mPages = pages;
        }

        @Override
        public Fragment getItem(int arg0)
        {
            return (mPages == null || mPages.size() == 0) ? null : mPages.get(arg0).fragment;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return (mPages.size() > position) ? mPages.get(position).title : "";
        }

        @Override
        public int getCount()
        {
            return mPages == null ? 0 : mPages.size();
        }
    }

    public class PageItem
    {
        int id;
        String title;
        BaseFragment fragment;

        TextView titleView;

        public PageItem(String title, BaseFragment fragment)
        {
            this.title = title;
            this.fragment = fragment;
        }
    }
}
