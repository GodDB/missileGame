package com.example.mvp;

import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

public class Presenter implements Contract.IPresenter, Runnable {

    private Contract.IView view;

    //미사일 객체를 담기 위한 arrayList
    private ArrayList<Missile> missileList = new ArrayList();


    //스레드 및 핸들러
    private Thread gameThread;
    private Handler handler = new Handler();

    //스레드 작동 여부
    private boolean start = true;

    //속력 (초당 500)
    private final int speed = 500;

    // 스레드 슬립 시간
    private double sleepTime = 0.05;


    @Override
    public void setView(Contract.IView iv) {
        this.view = iv;
    }

    @Override
    public float cal_degree(int progress) {
        return 0;
    }

    @Override
    public void cal_speed(float degree, int dpi, float x, float y, ImageView iv) {

        float radiuns = (float)(degree*Math.PI)/180;  // radiuns로 변환

        //위치 계산 (unit_Vector)
        double unit_x = Math.cos(radiuns);
        double unit_y = Math.sin(radiuns);

        //x,y축 속도 계산
        float vector_x = (float)((speed * unit_x) * sleepTime);
        float vector_y = (float)((speed * unit_y) * sleepTime);

        //dp로 변환
        vector_x = vector_x * (dpi/160);
        vector_y = vector_y * (dpi/160);

        missileList.add(new Missile(x, y, vector_x, vector_y, iv));
    }

    @Override
    public void run() {
        while (start){
            if(missileList.size()>0){
                //메인 스레드에 전달
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i< missileList.size(); i++){

                            //미사일 이동
                            Missile missile = missileList.get(i);


                        }
                    }
                });
            }
        }
    }

    @Override
    public void startThread() {
        Log.d("godgod", "스레드 실행");
        this.start = true;
        //스레드 생성
        gameThread = null;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void stopThread() {
        Log.d("godgod", "스레드 중지");
        this.start = false;
    }



}
