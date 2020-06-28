package com.example.kotlinmissile_mvp

/** 모델 **/
data class Missile (val missileId : Int,
                    var curX : Float, var curY : Float,
                    val vectorX : Float, val vectorY : Float) {

    fun move(){
        curX += vectorX
        curY -= vectorY
    }
}