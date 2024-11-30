package com.codershubham.quickcarhire.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.codershubham.quickcarhire.LoginActivity;
import com.codershubham.quickcarhire.MainActivity;
import com.codershubham.quickcarhire.R;

public class Luncher extends AppCompatActivity {
    ViewPager viewPager;
    TextView btnNext;
    int[] layouts;
    LuncherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_luncher);

        viewPager = findViewById(R.id.pager);
        btnNext = findViewById(R.id.nextBtn);

        layouts = new int[] {
                R.layout.layout_launcher_1,
                R.layout.layout_launcher_2,
                R.layout.layout_launcher_3
        };

        adapter = new LuncherAdapter(this,layouts);
        viewPager.setAdapter(adapter);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()+1 < layouts.length){
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });

        viewPager.addOnPageChangeListener(viewPagerChangeListener);
    }

    ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if(i == layouts.length - 1){
                btnNext.setText("Continue");
            }else {
                btnNext.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}