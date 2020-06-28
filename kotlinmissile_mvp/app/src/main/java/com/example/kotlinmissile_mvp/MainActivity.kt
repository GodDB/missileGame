package com.example.kotlinmissile_mvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), Contract.IView, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //프레젠터
    private lateinit var presenter : Contract.IPresenter

    //뷰
    private lateinit var cannon : ImageView
    private lateinit var parentView: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        //presenter에 setView
        presenter = MainPresenter(displayMetrics.widthPixels, displayMetrics.densityDpi).apply { view = this@MainActivity}

        initView()
    }

    override fun onClick(p0: View?) {
        val degree = cannon.rotation

        //미사일 생성 위치(x,y)
        val missileX : Float = cannon.left.toFloat()
        val missileY : Float = (cannon.top + (cannon.height*0.8)).toFloat()

        //미사일 이미지 생성
        val missileId = View.generateViewId()
        createMissileImage().apply { x = missileX; y=missileY; id=missileId }.also { parentView.addView(it) }

        presenter.calSpeed(degree, missileX, missileY, missileId)
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        presenter.calDegree(p1)
    }

    override fun setDegree(degree: Float) {
        cannon.rotation = degree
    }

    override fun moveMissile(missileId: Int, curX: Float, curY: Float) {
        findViewById<ImageView>(missileId).apply { x = curX; y = curY }
    }

    override fun removeMissile(missileId: Int) {
        val missileIv = findViewById<ImageView>(missileId)
        parentView.removeView(missileIv)
    }


    override fun onResume() {
        super.onResume()
        presenter.startThread()
    }

    override fun onPause() {
        super.onPause()
        presenter.stopThread()
    }


    private fun createMissileImage() : ImageView{
        return ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(cannon.width, cannon.width)
            setImageResource(R.drawable.missile)}
    }

    private fun initView(){
        findViewById<SeekBar>(R.id.seekbar).setOnSeekBarChangeListener(this)
        findViewById<Button>(R.id.fire).setOnClickListener(this)
        cannon = findViewById(R.id.cannon)
        parentView = findViewById(R.id.parent_view)
    }


    // not using
    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {}
}