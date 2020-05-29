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


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private Contract.IPresenter presenter;

    //뷰요소
    private SeekBar seekBar;
    private Button button;
    private ImageView cannon;
    private ConstraintLayout parentView;

    //화면 넓이, 높이, dpi
    private int width;
    private int height;
    private int dpi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면 해상도 구하기 및 초기화
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        dpi = displayMetrics.densityDpi;

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
        //대포 각도 ( 180 ~ 0 )
        float degree = 180-(cannon.getRotation()+90);

        //미사일 생성 위치(x,y)
        float x = cannon.getLeft();
        float y = (float)(cannon.getTop()+ (cannon.getHeight()*0.8));

        //미사일 이미지 생성
        ImageView iv = createMissileImage(this);
        parentView.addView(iv, 0);
        iv.setX(x);
        iv.setY(y);

        presenter.cal_speed(degree, dpi, x, y, iv);
    }

    // seekbar event
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //대포 각도 조절
        float degree = (float)((progress-50)*1.8);
        cannon.setPivotX(cannon.getWidth()/2);
        cannon.setPivotY(cannon.getHeight());
        cannon.setRotation(degree);
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


    // not using
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }
}
