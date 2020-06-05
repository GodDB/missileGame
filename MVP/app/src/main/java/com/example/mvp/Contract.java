package com.example.mvp;


public class Contract {
    // 사용자가 seekbar 움직임 -> 액티비티 전달 -> 프레젠터(cal_degree) -> 액티비티 setDegree
    // 사용자가 발사 버튼을 누름 -> 액티비티 전달 -> 프레젠터(cal_speed) -> 미사일 객체 생성
    //
    public interface IView {
        //대포 각도 변경 적용
        void setDegree(float degree);

        //미사일 이미지 이동
        void moveMissile(Missile missile);

        //부모 뷰에서 미사일 이미지 삭제
        void removeMissile(Missile missile);
    }



    public interface IPresenter {
        //뷰 등록
        void setView(IView iv);

        //progress -> 각도 변환
        void cal_degree(int progress);

        //속력에 따른 x,y축 변위 계산 + 미사일 객체 생성
        void cal_speed(float degree, float x, float y, int id);

        //스레드 start
        void startThread();

        //스레드 stop
        void stopThread();

    }
}
