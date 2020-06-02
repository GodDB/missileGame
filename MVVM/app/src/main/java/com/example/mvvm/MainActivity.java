package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.Observable;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.example.mvvm.databinding.ActivityMainBinding;
import com.example.mvvm.databinding.MissileBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //화면 가로값, dpi값
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int dpi = displayMetrics.densityDpi;

        // 뷰모델 생성 및 메인 xml과 바인딩
        viewModel = new ViewModel(width, dpi);
        activityMainBinding.setViewModel(viewModel);
        activityMainBinding.setActivity(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        float x = (float) activityMainBinding.cannon.getLeft();
        float y = (float)((activityMainBinding.cannon.getTop()) + (activityMainBinding.cannon.getHeight()*0.8));
        viewModel.setXY(x, y);
    }


    // 버튼 클릭
    public void onClick(View v){
        final MissileBinding missileBinding =DataBindingUtil.inflate(getLayoutInflater(), R.layout.missile, activityMainBinding.parentView, true);

        //화면 바깥에 넘어갔을 때 VISIBILITY가 GONE
        //VISIBILITY가 GONE인 상태의 이미지뷰 제거
        missileBinding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public void onBound(ViewDataBinding binding) {
                if(binding.getRoot().getVisibility() == View.GONE){
                    activityMainBinding.parentView.removeView(binding.getRoot());
                }
                super.onBound(binding);
            }
        });

        viewModel.cal_speed(missileBinding);
    }



    @Override
    protected void onResume() {
        super.onResume();
        viewModel.startThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.stopThread();
    }
}
