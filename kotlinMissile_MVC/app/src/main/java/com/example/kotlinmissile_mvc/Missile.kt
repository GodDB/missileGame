package com.example.kotlinmissile_mvc

import android.widget.ImageView

data class Missile (val imageView : ImageView,
                    var curX : Float, var curY : Float,
                    val vectorX : Float, val vectorY : Float) {

    fun move(){
        this.curX += vectorX
        this.curY -= vectorY

        imageView.x = curX
        imageView.y = curY
    }
}