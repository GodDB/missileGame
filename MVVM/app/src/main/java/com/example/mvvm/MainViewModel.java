package com.example.mvvm;

import android.util.Log;
import android.widget.SeekBar;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.example.mvvm.databinding.MissileBinding;
import java.util.ArrayList;


public class MainViewModel extends BaseObservable implements Runnable{

    //화면 넓이, dpi
    private int width;
    private int dpi;

    //미사일 생성 위치
    private float missile_x;
    private float missile_y;

    //속력 (초당 500)
    private final int speed = 500;

    //대포 각도
    private float degree =0;

    // 스레드 슬립 시간
    private double sleepTime = 0.05;

    //스레드 작동 여부
    private boolean start = true;

    //thread
    Thread gameThread = new Thread(this);

    //포탄 객체
    ArrayList<Missile> missile_list = new ArrayList();

    MainViewModel(int width, int dpi){
        this.width = width;
        this.dpi = dpi;
    }

    //대포 각도 계산
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float degree = (float)((progress-50)*1.8);
        this.degree = degree;
        notifyPropertyChanged(BR.degree);
    }

    //속력에 따른 x,y 변위 계산
    public void cal_speed(Missile missile){

        float new_degree = 180-(degree+90); //대포 각도 ( 180 ~ 0 )
        float radian = (float)(new_degree*Math.PI)/180;  // radian로 변환

        //위치 계산 (unit_Vector)
        double unit_x = Math.cos(radian);
        double unit_y = Math.sin(radian);

        //x,y축 변위 계산
        float vector_x = (float)((speed * unit_x)*sleepTime);
        float vector_y = (float)((speed * unit_y)*sleepTime);

        //dp로 변환
        vector_x = vector_x * (dpi/160);
        vector_y = vector_y * (dpi/160);

        missile.setVector_x(vector_x);
        missile.setVector_y(vector_y);
        missile_list.add(missile);
    }




    @Bindable
    public float getDegree() {
        return degree;
    }


    @Override
    public void run() {
        while(start){
            if(missile_list.size()>0){
                for(int i=0; i<missile_list.size(); i++){
                    Missile missile = missile_list.get(i);
                    //미사일 이동
                    missile.move();

                    //화면에 벗어날 시 미사일 객체 제거
                    if(missile.getCur_x()<=0 || missile.getCur_x()>=width || missile.getCur_y()<=0 ){
                        missile_list.remove(missile);
                    }
                }// end for
            }// end if
            try {
                Thread.sleep((long)(sleepTime*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }// end while
    }// end run()


    public void startThread() {
        Log.d("godgod", "스레드 실행");
        this.start = true;
        //스레드 생성
        gameThread = null;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopThread() {
        Log.d("godgod", "스레드 중지");
        this.start = false;
    }
}
