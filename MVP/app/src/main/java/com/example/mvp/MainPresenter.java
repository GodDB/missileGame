package com.example.mvp;

import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;

public class MainPresenter implements Contract.IPresenter, Runnable {

    //view
    private Contract.IView view;

    //화면 가로, dpi값
    private int dpi;
    private int width;

    //스레드 및 핸들러
    private Thread gameThread;
    private Handler handler = new Handler();

    //스레드 작동 여부
    private boolean start = true;

    //속력 (초당 500)
    private final int speed = 500;

    // 스레드 슬립 시간
    private double sleepTime = 0.05;

    //미사일 객체를 담기 위한 arrayList
    private ArrayList<Missile> missileList = new ArrayList();

    //생성자
    MainPresenter(int width, int dpi){
        this.width = width;
        this.dpi = dpi;
    }

    @Override
    public void setView(Contract.IView iv) {
        this.view = iv;
    }

    //대포 각도 변환
    @Override
    public void cal_degree(int progress) {
        float degree = (float)((progress-50)*1.8);
        view.setDegree(degree);
    }

    @Override
    public void cal_speed(float degree, float x, float y, int id) {

        //대포 각도 ( 180 ~ 0 )
        degree = 180-(degree+90);

        float radian = (float)(degree*Math.PI)/180;  // radian로 변환

        //위치 계산 (unit_Vector)
        double unit_x = Math.cos(radian);
        double unit_y = Math.sin(radian);

        //x,y축 변위 계산
        float vector_x = (float)((speed * unit_x) * sleepTime);
        float vector_y = (float)((speed * unit_y) * sleepTime);

        //dp로 변환
        vector_x = vector_x * (dpi/160);
        vector_y = vector_y * (dpi/160);

        //미사일 객체 생성 및 리스트에 add
        missileList.add(new Missile(x, y, vector_x, vector_y, id));
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

                            Missile missile = missileList.get(i);
                            //미사일 객체 x,y변경
                            missile.move();
                            //view에서 미사일 이미지 이동
                            view.moveMissile(missile);

                            //화면에 벗어나면 삭제
                            if(missile.getCur_x() <=0 || missile.getCur_x() >= width || missile.getCur_y() <=0 ){
                                view.removeMissile(missile);
                                missileList.remove(i);
                            }
                        } // end for
                    } // end run()
                }); // end post()
            } // end if
            try {
                Thread.sleep((long)(sleepTime*1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } // end while
    } // end run()

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
