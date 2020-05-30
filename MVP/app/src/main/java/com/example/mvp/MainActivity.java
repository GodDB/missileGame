package com.example.mvp;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
        presenter = new Presenter(width, dpi);
        presenter.setView(this);

        // 캐논 객체 생성
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

    // click event
    @Override
    public void onClick(View v) {
        float degree = cannon.getRotation();

        //미사일 생성 위치(x,y)
        float x = cannon.getLeft();
        float y = (float)(cannon.getTop()+ (cannon.getHeight()*0.8));

        //미사일 이미지 생성
        ImageView iv = createMissileImage(this);
        parentView.addView(iv, 0);
        iv.setX(x);
        iv.setY(y);

        presenter.cal_speed(degree, x, y, iv);
    }

    // seekbar event
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        presenter.cal_degree(progress);
    }

    @Override
    public void setDegree(float degree) {
        cannon.setPivotX(cannon.getWidth()/2);
        cannon.setPivotY(cannon.getHeight());
        cannon.setRotation(degree);
    }

    @Override
    public void moveMissile(Missile missile) {
        missile.move();
    }

    @Override
    public void removeMissile(Missile missile) {
        parentView.removeView(missile.getImageView());
    }

    //missile imageView
    public ImageView createMissileImage(Context context){
        int missile_w = cannon.getWidth();
        int missile_h = cannon.getWidth();
        ImageView iv = new ImageView(context);
        iv.setLayoutParams(new ConstraintLayout.LayoutParams(missile_w, missile_h));
        iv.setImageResource(R.drawable.missile);
        return iv;
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


    // not using(seekbarListener handler)
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

}
