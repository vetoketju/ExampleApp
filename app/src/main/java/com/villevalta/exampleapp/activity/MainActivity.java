package com.villevalta.exampleapp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.villevalta.exampleapp.R;
import com.villevalta.exampleapp.fragment.ImagesListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    ViewPager pager;
    TabLayout tabs;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);

        adapter = new Adapter(getSupportFragmentManager());

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

    }

    class Adapter extends FragmentStatePagerAdapter{

        private final ArrayList<String> subs = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);

            subs.add("pics");
            subs.add("funny");
            subs.add("bicycling");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return subs.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return ImagesListFragment.newInstance(subs.get(position),"hot");
        }

        @Override
        public int getCount() {
            return subs.size();
        }
    }


}
