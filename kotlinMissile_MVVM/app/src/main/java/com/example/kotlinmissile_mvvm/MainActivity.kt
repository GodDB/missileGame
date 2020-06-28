package com.example.kotlinmissile_mvvm

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.example.kotlinmissile_mvvm.databinding.ActivityMainBinding
import com.example.kotlinmissile_mvvm.databinding.MissileBinding

class MainActivity : AppCompatActivity(),CreateMissileCallback {

    lateinit var mainViewModel: MainViewModel

    companion object{
        lateinit var activityMainBinding : ActivityMainBinding
        var width = 0

        //미사일이 화면 바깥에 넘어갔는지 체크하는 바인딩 어댑터
        @JvmStatic
        @BindingAdapter("android:translationX", "android:translationY", requireAll = true)
        fun removeMissile(view : ImageView, x : Float, y : Float){
            if(x<=0 || x>= width || y<=0){
                activityMainBinding.parentView.removeView(view)
            }else{
                view.x = x
                view.y = y
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width=displayMetrics.widthPixels
        val dpi = displayMetrics.densityDpi

        mainViewModel = MainViewModel(width, dpi, this)
        activityMainBinding.mainViewModel = mainViewModel
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        //미사일 최초 생성 위치 x,y
        val missileX = activityMainBinding.cannon.left.toFloat()
        val missileY = (activityMainBinding.cannon.top + activityMainBinding.cannon.height*0.8).toFloat()

        mainViewModel.missileX = missileX
        mainViewModel.missileY = missileY
    }

    //DrawMissileCallback 구현
    //뷰모델로 클릭이벤트 전달 시 호출됨
    override fun creatMissile() {
        val missileBinding : MissileBinding = DataBindingUtil.inflate(layoutInflater, R.layout.missile, activityMainBinding.parentView, true)
        mainViewModel.bindMissile(missileBinding)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.startThread()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.stopThread()
    }
}

