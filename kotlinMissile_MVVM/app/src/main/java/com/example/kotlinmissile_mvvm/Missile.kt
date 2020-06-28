package com.example.kotlinmissile_mvvm

import androidx.databinding.ObservableFloat

data class Missile (var x : Float, var y : Float,
                    val vectorX : Float, val vectorY : Float) {

    val curX : ObservableFloat = ObservableFloat()
    val curY : ObservableFloat = ObservableFloat()

    init {
        this.curX.set(x)
        this.curY.set(y)
    }

    fun move(){
        curX.set(curX.get()+vectorX)
        curY.set(curY.get()-vectorY)
    }
}