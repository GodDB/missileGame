package com.example.kotlinmissile_mvp

import android.os.Handler
import android.os.Looper

class MainPresenter(private val width : Int, private val dpi : Int) : Contract.IPresenter {
    //view
    lateinit var view : Contract.IView

    //핸들러
    private val handler : Handler = Handler(Looper.myLooper()!!)

    //스레드 flag
    private var start : Boolean = true

    //속력 (초당 500)
    private val speed : Int = 500

    //스레드 슬립 시간
    private val sleepTime = 0.05

    //미사일 객체를 담기 위한 mutableList
    private val missileList : MutableList<Missile> = mutableListOf()


    override fun calDegree(progress: Int) {
        val degree = ((progress-50)*1.8).toFloat()
        view.setDegree(degree)
    }

    override fun calSpeed(degree: Float, x: Float, y: Float, missileId: Int) {
        val degree = 180-(degree+90)
        val radian = (degree*Math.PI/180)

        //방향 계산 (unit_vector)
        val unitX = Math.cos(radian)
        val unitY = Math.sin(radian)

        //x,y 변위 계산
        val vectorX : Float = (speed * unitX * sleepTime).toFloat() * (dpi/160)
        val vectorY : Float = (speed * unitY * sleepTime).toFloat() * (dpi/160)

        missileList.add(
            Missile(missileId, x, y, vectorX, vectorY)
        )
    }

    override fun startThread() {
        this.start = true

        Thread{
            while (start){
                if(missileList.size >0)
                    handler.post {
                        missileList.removeIf {
                            it.move()
                            view.moveMissile(it.missileId, it.curX, it.curY)

                            if(it.curX <= 0 || it.curX >= width || it.curY<=0){
                                view.removeMissile(it.missileId)
                                true
                            }else false
                        }
                    }
                Thread.sleep((sleepTime*1000).toLong())
            }
        }.start()
    }

    override fun stopThread() {
        this.start = false
    }
}