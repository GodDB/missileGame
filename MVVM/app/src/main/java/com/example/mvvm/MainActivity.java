package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.OnRebindCallback;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import com.example.mvvm.databinding.ActivityMainBinding;
import com.example.mvvm.databinding.MissileBinding;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private MainViewModel mainViewModel;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //화면 가로값, dpi값
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        int dpi = displayMetrics.densityDpi;

        // 뷰모델 생성 및 메인 xml과 바인딩
        mainViewModel = new MainViewModel(width, dpi);
        activityMainBinding.setMainViewModel(mainViewModel);
        activityMainBinding.setActivity(this);
    }

    //화면이 다 그려졌을 때 호출
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        float x = (float) activityMainBinding.cannon.getLeft();
        float y = (float)((activityMainBinding.cannon.getTop()) + (activityMainBinding.cannon.getHeight()*0.8));
        mainViewModel.setXY(x, y);
    }


    // 버튼 클릭
    public void onClick(View v){
        MissileBinding missileBinding =DataBindingUtil.inflate(getLayoutInflater(), R.layout.missile, activityMainBinding.parentView, true);

       //missileBinding객체에 알림이 왔을 때 호출되는 리스너를 구현하여 missileBinding객체에 add
        missileBinding.addOnRebindCallback(new OnRebindCallback() {
            @Override
            public void onBound(ViewDataBinding binding) {
                super.onBound(binding);
                View missile_iv=binding.getRoot(); // 미사일뷰
                //화면에 넘어갔을 때 미사일 이미지뷰 제거
                if(missile_iv.getX()<=0 || missile_iv.getX()>=width || missile_iv.getY()<=0){
                    activityMainBinding.parentView.removeView(missile_iv);
                }
            }
        });
        mainViewModel.bindMissile(missileBinding);
    }



    @Override
    protected void onResume() {
        super.onResume();
        mainViewModel.startThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainViewModel.stopThread();
    }
}
