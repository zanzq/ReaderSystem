package com.liyunkun.readersystem.student.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.liyunkun.readersystem.BaseFragment;
import com.liyunkun.readersystem.R;
import com.liyunkun.readersystem.both.module.bean.BookBean;
import com.liyunkun.readersystem.student.module.bean.BookShopLvBean;
import com.liyunkun.readersystem.student.view.activity.ClassifyActivity;
import com.liyunkun.readersystem.student.view.activity.RankActivity;
import com.liyunkun.readersystem.student.view.adapter.BookShopLvAdapter;
import com.liyunkun.readersystem.student.view.adapter.BookShopVpAdapter;
import com.liyunkun.readersystem.student.view.intf.IBookShopView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyunkun on 2016/10/15 0015.
 */
public class BookShopFragment extends BaseFragment implements IBookShopView, View.OnClickListener {

    private ListView mLv;
    private ViewPager mVp;
    private LinearLayout mBotLayout;
    private int currentPosition = 0;
    private int size = 0;
    private boolean isRunning = true;
    private Handler mHandler = new Handler();
    private LinearLayout mRankLayout;
    private LinearLayout mClassifyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_shop_fg_layout, container, false);
        initView(view);
        initData2Lv(null);
        initData2Vp(null);
        mRankLayout.setOnClickListener(this);
        mClassifyLayout.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    SystemClock.sleep(5000);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mVp.setCurrentItem(mVp.getCurrentItem() + 1);
                        }
                    });
                }

            }
        }).start();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initView(View view) {
        mLv = ((ListView) view.findViewById(R.id.lv));
        mVp = ((ViewPager) view.findViewById(R.id.vp));
        mBotLayout = ((LinearLayout) view.findViewById(R.id.bot_layout));
        mRankLayout = ((LinearLayout) view.findViewById(R.id.rank_layout));
        mClassifyLayout = ((LinearLayout) view.findViewById(R.id.classify_layout));


        mVp.setCurrentItem(Integer.MAX_VALUE / 2);
        mVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_UP:
                        mVp.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mVp.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }
        });
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % size;
                mBotLayout.getChildAt(currentPosition).setEnabled(false);
                mBotLayout.getChildAt(position).setEnabled(true);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData2Lv(List<BookShopLvBean> list) {

        //
        List<BookShopLvBean> been = new ArrayList<>();
        List<BookBean> bookBeen = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            BookBean book = new BookBean();
            bookBeen.add(book);
        }
        been.add(new BookShopLvBean("重榜精选", bookBeen));
        been.add(new BookShopLvBean("主编推荐", bookBeen));
        been.add(new BookShopLvBean("火热新书", bookBeen));
        been.add(new BookShopLvBean("热门作品", bookBeen));
        been.add(new BookShopLvBean("与你相关", bookBeen));
        //

        BookShopLvAdapter adapter = new BookShopLvAdapter(been, getActivity());
        measureHeightLv(adapter);
        mLv.setAdapter(adapter);
    }

    @Override
    public void initData2Vp(List<String> list) {
        //
        List<String> stringList = new ArrayList<>();
        stringList.add("http://www.bsuc.cn/department/lib/images/444.jpg");
        stringList.add("http://www.bsuc.cn/images2/2016/cz-80-1.jpg");
        stringList.add("http://www.bsuc.cn/images2/2016/cz-80-2.jpg");
        stringList.add("http://www.bsuc.cn/images2/2016/jx201601.jpg");
        //

        size = stringList.size();
        initDotLayout(stringList);
        BookShopVpAdapter adapter = new BookShopVpAdapter(stringList, getActivity());
        mVp.setAdapter(adapter);
    }

    private void initDotLayout(List<String> stringList) {
        for (int i = 0; i < stringList.size(); i++) {
            View view = new View(getActivity());
            view.setBackgroundResource(R.drawable.dot_bg);
            LinearLayout.LayoutParams lp
                    = new LinearLayout.LayoutParams((int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()))
                    , (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())));
            lp.leftMargin = 10;
            view.setLayoutParams(lp);
            view.setEnabled(false);
            mBotLayout.addView(view);
        }
        mBotLayout.getChildAt(0).setEnabled(true);
    }

    private void measureHeightLv(BookShopLvAdapter adapter) {
        int count = adapter.getCount();
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View view = adapter.getView(i, null, mLv);
            view.measure(0, 0);
            int measuredHeight = view.getMeasuredHeight();
            totalHeight += (measuredHeight + mLv.getDividerHeight());
        }
        ViewGroup.LayoutParams lp = mLv.getLayoutParams();
        lp.height = totalHeight;
        mLv.requestLayout();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rank_layout:
                startActivity(new Intent(getActivity(), RankActivity.class));
                break;
            case R.id.classify_layout:
                startActivity(new Intent(getActivity(), ClassifyActivity.class));
                break;
        }
    }
}