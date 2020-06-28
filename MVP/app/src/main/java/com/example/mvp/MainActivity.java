package com.example.mvp;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, Contract.IView {

    private Contract.IPresenter presenter;

    //뷰요소
    private SeekBar seekBar;
    private Button button;
    private ImageView cannon;
    private ConstraintLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면 가로값, dpi값
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int dpi = displayMetrics.densityDpi;

        //presenter에 setView
        presenter = new MainPresenter(width, dpi);
        presenter.setView(this);

        // 캐논이미지 객체 생성
        cannon = findViewById(R.id.cannon);

        // 레이아웃 객체 생성
        parentView = findViewById(R.id.parent_view);

        //seekbar setListener
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        //button setListener
        button = findViewById(R.id.fire);
        button.setOnClickListener(this);
    }

    // onClick Handler
    @Override
    public void onClick(View v) {
        float degree = cannon.getRotation();

        //미사일 생성 위치(x,y)
        float x = cannon.getLeft();
        float y = (float)(cannon.getTop()+ (cannon.getHeight()*0.8));

        //미사일 이미지 생성
        ImageView iv = createMissileImage();
        int id = View.generateViewId();
        iv.setId(id);
        iv.setX(x);
        iv.setY(y);
        parentView.addView(iv);

        presenter.cal_speed(degree, x, y, id);
    }

    // seekbar Handler
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        presenter.cal_degree(progress);
    }

    //대포 각도 계산 값 적용
    @Override
    public void setDegree(float degree) {
        cannon.setRotation(degree);
    }

    //미사일 이미지 이동
    @Override
    public void moveMissile(int missile_id, float cur_x, float cur_y) {
        ImageView missile_iv = findViewById(missile_id);
        missile_iv.setX(cur_x);
        missile_iv.setY(cur_y);
    }

    //부모 뷰에서 미사일 이미지 삭제
    @Override
    public void removeMissile(int missile_id) {
        ImageView missile_iv = findViewById(missile_id);
        parentView.removeView(missile_iv);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //스레드 실행
        presenter.startThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //스레드 중지
        presenter.stopThread();
    }

    //미사일 이미지뷰 생성
    private ImageView createMissileImage(){
        int missile_w = cannon.getWidth();
        int missile_h = cannon.getWidth();
        ImageView iv = new ImageView(MainActivity.this);
        iv.setLayoutParams(new ConstraintLayout.LayoutParams(missile_w, missile_h));
        iv.setImageResource(R.drawable.missile);
        return iv;
    }


    // not using(seekbarListener handler)
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

}
