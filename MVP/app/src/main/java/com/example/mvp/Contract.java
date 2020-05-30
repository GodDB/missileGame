package com.example.mvp;

import android.widget.ImageView;

public class Contract {

    public interface IView {
        //대포 각도 계산 값 적용
        void setDegree(float degree);

        //미사일 이미지 이동
        void moveMissile(Missile missile);

        //부모 뷰에서 미사일 이미지 삭제
        void removeMissile(Missile missile);
    }



    public interface IPresenter {
        //뷰 등록
        void setView(IView iv);

        //대포 각도 계산
        void cal_degree(int progress);

        //x,y축 속도 계산
        void cal_speed(float degree, float x, float y, ImageView iv);

        //스레드 start
        void startThread();

        //스레드 stop
        void stopThread();

    }
}
