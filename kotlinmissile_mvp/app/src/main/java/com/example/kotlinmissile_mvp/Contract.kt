package com.example.kotlinmissile_mvp

interface Contract {

    interface IView{
        //대포 각도 변경 적용
        fun setDegree(degree : Float)

        //미사일 이미지 이동
        fun moveMissile(missileId : Int, curX : Float, curY : Float)

        //부모 뷰에서 미사일 이미지 삭제
        fun removeMissile(missileId: Int)
    }

    interface IPresenter{

        //progress -> 각도
        fun calDegree(progress : Int)

        //속력에 따른 x,y 변위 계산 + 미사일 객체 생성
        fun calSpeed(degree:Float, x:Float, y:Float, missileId: Int)

        //스레드 START
        fun startThread()

        //스레드 stop
        fun stopThread()
    }
}