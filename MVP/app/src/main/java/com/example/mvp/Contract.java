package com.example.mvp;

import android.widget.ImageView;

public class Contract {

    public interface IView {
        void setDegree(float degree);

        void setMissile();
    }



    public interface IPresenter {
        //뷰 등록
        void setView(IView iv);

        //대포 각도 계산
        float cal_degree(int progress);

        //x,y축 속도 계산
        void cal_speed(float degree, int dpi, float x, float y, ImageView iv);

        //스레드 start
        void startThread();

        //스레드 stop
        void stopThread();

    }
}
