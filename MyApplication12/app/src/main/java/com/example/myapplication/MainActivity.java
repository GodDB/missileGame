package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Point;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, Runnable {

    //뷰요소
    private SeekBar seekBar;
    private Button button;
    private ImageView cannon;
    private ConstraintLayout parentView;

    //미사일 객체를 담기 위한 arrayList
    private ArrayList<Missile> missileList;

    //화면 넓이, 높이, dpi
    private int width;
    private int height;
    private int dpi;

    //스레드 및 핸들러
    private Thread gameThread;
    private Handler handler2 = new Handler();

    //스레드 작동 여부
    private boolean start;

    //속력 (초당 500)
    private final int speed = 500;

    // 스레드 슬립 시간
    private double sleepTime = 0.05;

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
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
        //대포 각도 ( 180 ~ 0 )
       float degree = 180-(cannon.getRotation()+90);
       float radiuns = (float)(degree*Math.PI)/180;  // radiuns로 변환

       //미사일 생성 위치(x,y)
       float x = cannon.getLeft();
       float y = (float)(cannon.getTop()+ (cannon.getHeight()*0.8));

       //미사일 imageView 생성
        ImageView iv = createMissileImage(this);
        parentView.addView(iv, 0);
        iv.setX(x);
        iv.setY(y);

        //위치 계산 (unit_Vector)
        double unit_x = Math.cos(radiuns);
        double unit_y = Math.sin(radiuns);

        //x,y축 변화량 계산
       float vector_x = (float)((speed * unit_x) * sleepTime);
       float vector_y = (float)((speed * unit_y) * sleepTime);

       //dp로 변환
        vector_x = vector_x * (dpi/160);
        vector_y = vector_y * (dpi/160);

       //미사일 객체 생성 및 arrayList에 add
       missileList.add(new Missile(x,y, vector_x, vector_y, iv));
    }



    //Runnable
    @Override
    public void run() {
        while(start){
            if(missileList.size() >0 ) {
                // 메인 스레드에 전달
                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < missileList.size(); i++) {

                            //미사일 이동
                            Missile missile = missileList.get(i);
                            missile.move();

                            //화면 벗어나면 삭제
                            if (missile.getCur_x() <= 0 || missile.getCur_x() >= width || missile.getCur_y() <= 0) {
                                parentView.removeView(missile.getImageView());
                                missileList.remove(i);
                            }
                        } // end for
                    }  // end run()
                }); // end post()
            } // end if
            try {
                Thread.sleep((long)(sleepTime*1000));
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
