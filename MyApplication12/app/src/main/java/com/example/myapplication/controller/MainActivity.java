package com.example.myapplication.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Point;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.myapplication.R;
import com.example.myapplication.model.Missile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, Runnable {

    private SeekBar seekBar;
    private Button button;
    private ImageView cannon;
    private ConstraintLayout parentView;
    private ArrayList<Missile> missileList;

    private int width;
    private int height;

    private Thread gameThread;

    private boolean start;

    Handler handler2 = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //화면 해상도 구하기
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        width = size.x;
        height = size.y;

        // 캐논 객체 생성
        cannon = findViewById(R.id.cannon);

        // 레이아웃 객체 생성
        parentView = findViewById(R.id.parent_view);

        // gameThread 생성
        gameThread = new Thread(this);

        //seekbar setListener
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);

        //button setListener
        button = findViewById(R.id.fire);
        button.setOnClickListener(this);

        // missile List
        missileList = new ArrayList();
    }


    //seekbar handler
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //대포 각도 조절
        float degree = (float)((progress-50)*1.8);
        cannon.setPivotX(cannon.getWidth()/2);
        cannon.setPivotY(cannon.getHeight());
        cannon.setRotation(degree);

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}


    //onclick handler
    @Override
    public void onClick(View v) {
       float degree = cannon.getRotation();
     /*  float x = cannon.getLeft() + (cannon.getPivotX()/2);*/
        float x = cannon.getLeft();
       float y = (float)(cannon.getTop()+ (cannon.getHeight()*0.8));

       //create missile imageView
        ImageView iv = createMissileImage(this);
       parentView.addView(iv, 0);

       iv.setX(x);
       iv.setY(y);

       missileList.add(new Missile(x,y,degree, iv));

    }

    //thread
    @Override
    public void run() {
        Log.d("godgod", "스레드 가동");
        while(start){
            Log.d("godgod", "스레드 가동2");

            // 메인 스레드에 값 전달
            handler2.post(new Runnable() {
                @Override
                public void run() {
                    if(missileList.size() >0 ){
                        for(int i=0; i<missileList.size(); i++){
                            //미사일 이동
                            Missile missile = missileList.get(i);
                            missile.move();

                            //화면 벗어나면 삭제
                            if(missile.getCur_x() <=100 || missile.getCur_x()>=width-100 || missile.getCur_y() <=100){
                                parentView.removeView(missile.getImageView());
                                missileList.remove(i);
                            }
                        }
                    }
                }
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
        Log.d("godgod", "스레드 실행");
        this.start = true;
        //스레드 생성
        gameThread = null;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("godgod", "스레드 중지");
        this.start = false;
    }
}
