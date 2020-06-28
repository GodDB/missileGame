package com.example.kotlinmissile_mvvm

import android.view.View
import android.widget.SeekBar
import androidx.databinding.ObservableFloat
import com.example.kotlinmissile_mvvm.databinding.MissileBinding

class MainViewModel(val width : Int, val dpi : Int, private val missileCallback: CreateMissileCallback) {

    //미사일 최초 생성위치
    var missileX : Float = 0f
    var missileY : Float = 0f

    //대포 각도
    var degree : ObservableFloat = ObservableFloat()

    //속력
    val speed : Int = 500
    //스레드 슬립 시간
    val sleepTime = 0.05
    //스레드 작동 Flag
    var start = true

    //미사일 객체를 담기 위한 mutableList
    val missileList : MutableList<Missile> = mutableListOf()


    //seekbar 이벤트 (대포 각도 계산)
    fun onProgressChanged(seekBar: SeekBar, progress:Int, fromUser:Boolean){
        degree.set(((progress-50)*1.8).toFloat())
    }

    //클릭이벤트
    fun onClick(v : View){
        //액티비티에서 미사일 이미지 inflate
        missileCallback.creatMissile()
    }

    // 미사일 바인딩 객체에 미사일 객체를 set
    fun bindMissile(missileBinding: MissileBinding){
        val vector = calSpeed()

        //미사일 객체 생성
        val missile : Missile = Missile(missileX, missileY, vector[0], vector[1])

        //미사일 바인딩 객체에 set
        missileBinding.setMissile(missile)

        //미사일 객체를 리스트에 add
        missileList.add(missile)
    }


    fun startThread(){
        this.start = true
        //스레드 실행
        Thread{
            while(start) {
                missileList.removeIf {
                    it.move()
                    (it.curX.get()<=0 || it.curX.get()>=width || it.curY.get()<=0) }
                Thread.sleep((sleepTime * 1000L).toLong())
            }
        }.start()
    }

    fun stopThread(){
        this.start = false
    }

    private fun calSpeed() : FloatArray {

        //대포 각도 (180 - 0)
        val newDegree = 180-(degree.get()+90)
        val radian = (newDegree*Math.PI)/180

        //방향 계산(unit_vector)
        val unitX = Math.cos(radian)
        val unitY = Math.sin(radian)

        //x,y축 변위 계산
        val vectorX = ((speed*unitX*sleepTime) * (dpi/160)).toFloat()
        val vectorY = ((speed*unitY*sleepTime) * (dpi/160)).toFloat()

        return floatArrayOf(vectorX, vectorY)
    }
}