package com.example.kotlinmissile_mvc

import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    //뷰 요소
    private lateinit var seekBar : SeekBar
    private lateinit var button : Button
    private lateinit var cannon : ImageView
    private lateinit var  parentView : ConstraintLayout

    //핸들러
    var handler : Handler = Handler(Looper.myLooper())

    //미사일 객체를 담기 위한 mutableList
    val missileList : MutableList<Missile> = mutableListOf()

    //속력 ( 초당 500)
    val speed : Int = 500

    //스레드 슬립 시간
    val sleepTime : Double = 0.05

    //스레드 작동여부
    var start : Boolean = true

    // 화면 width, dpi
    var width : Int = 0
    var dpi : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width=displayMetrics.widthPixels
        dpi = displayMetrics.densityDpi

        initView()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        cannon.rotation = ((progress-50)*1.8).toFloat()
    }

    override fun onClick(v: View?) {
        //대포 각도 (180 ~ 0)
        var degree : Double = (180-(cannon.rotation+90)).toDouble()
        var radian : Double = (degree*Math.PI)/180

        //미사일 생성 위치(x,y)
        val missileX : Float = cannon.left.toFloat()
        val missileY : Float = (cannon.top + (cannon.height*0.8)).toFloat()

        //미사일 imageView 생성
        val iv : ImageView = createMissileImage().apply{ x = missileX; y = missileY}.also { parentView.addView(it) }

        //방향 계산 (unit_vector)
        val unitX = Math.cos(radian)
        val unitY = Math.sin(radian)

        //x,y 변위 계산
        val vectorX : Float = (speed * unitX * sleepTime).toFloat() * (dpi/160)
        val vectorY : Float = (speed * unitY * sleepTime).toFloat() * (dpi/160)

        missileList.add(
            Missile(iv, missileX, missileY, vectorX, vectorY)
        )
    }


    override fun onResume() {
        super.onResume()
        this.start = true

        //스레드 실행
        Thread{
            while(start) {
                if (missileList.size > 0)
                    handler.post {
                        missileList.removeIf {
                            it.move()
                            if(it.curX <= 0 || it.curY <= 0 || it.curX >= width){
                                parentView.removeView(it.imageView)
                                true
                            }else false
                        }
                    }
                Thread.sleep((sleepTime * 1000L).toLong())
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        //스레드 중지
        this.start = false
    }

    private fun createMissileImage() : ImageView{
        return ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(cannon.width, cannon.width)
            setImageResource(R.drawable.missile)}
    }

    private fun initView(){
        seekBar = findViewById(R.id.seekbar)
        seekBar.setOnSeekBarChangeListener(this)
        button = findViewById(R.id.fire)
        button.setOnClickListener(this)
        cannon = findViewById(R.id.cannon)
        parentView = findViewById(R.id.parent_view)
    }



    // not using
    override fun onStartTrackingTouch(seekBar: SeekBar?){}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}




}