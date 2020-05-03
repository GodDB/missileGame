package com.example.missilegame.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.missilegame.model.Cannon;
import com.example.missilegame.model.Missile;
import com.example.missilegame.view.MyView;
import com.example.missilegame.R;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private SeekBar seekBar;
    private Button button;
    private MyView myView;
    private Cannon cannon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면 해상도 구하기
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //대포 객체 생성
        cannon = new Cannon(size.x, size.y);
        //뷰 객체 생성(캐논 객체도 보냄)
        myView = new MyView(this, size.x, size.y, cannon);

        // 액티비티와 xml과 연결
        setContentView(R.layout.activity_main);

        // 뷰 객체와 xml 연결
        LinearLayout linearLayout = findViewById(R.id.customView);
        linearLayout.addView(myView);


        //seekbar setListener
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        //button setListener
        button = findViewById(R.id.fire);
        button.setOnClickListener(this);
    }

    //seekbar handler
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
       //대포 각도 조절
        cannon.setDegree((float)((progress-50)*1.8));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


    //onclick handler
    @Override
    public void onClick(View v) {
        Missile missile =cannon.shoot();
        myView.setMissile(missile);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myView.stop(); //스레드 중지
    }

    @Override
    protected void onResume() {
        super.onResume();
        myView.resume(); //스레드 실행
    }
}
