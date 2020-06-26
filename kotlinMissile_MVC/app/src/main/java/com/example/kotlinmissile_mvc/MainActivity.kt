package com.example.kotlinmissile_mvc

import android.media.Image
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
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    //뷰 요소
    lateinit var seekBar : SeekBar
    lateinit var button : Button
    lateinit var cannon : ImageView
    lateinit var  parentView : ConstraintLayout

    //스레드 및 핸들러
    var handler : Handler = Handler(Looper.myLooper())


    //미사일 객체를 담기 위한 mutableList
    val missileList : MutableList<Missile> = mutableListOf()

    //속력 ( 초당 500)
    val speed : Int = 500

    //스레드 슬립 시간
    val sleepTime : Double = 0.05

    //스레드 작동여부
    var start : Boolean = true

    // 화면 width, height, dpi
    var width : Int = 0
    var height : Int = 0
    var dpi : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics : DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width=displayMetrics.widthPixels
        height =displayMetrics.heightPixels
        dpi = displayMetrics.densityDpi

        initView()

    }

    fun createMissileImage() : ImageView{
        val missileW = cannon.width
        val missileH = cannon.height
        return ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(missileW, missileH)
            setImageResource(R.drawable.missile)}
    }

    fun initView(){
        seekBar = findViewById(R.id.seekbar)
        seekBar.setOnSeekBarChangeListener(this)
        button = findViewById(R.id.fire)
        button.setOnClickListener(this)
        cannon = findViewById(R.id.cannon)
        parentView = findViewById(R.id.parent_view)
    }

    override fun onResume() {
        super.onResume()
        this.start = true
        Thread{
            while(start){
              /*  Log.d("godgod", "스레드 실행")*/
                if(missileList.size >0)
                    handler.post{
                        missileList.forEach{
                            it.move()

                            if(it.curX <=100 || it.curX >= width-100 || it.curY <=0){
                                parentView.removeView(it.imageView)
                                missileList.remove(it)
                            }
                        }
                    }
                Thread.sleep((sleepTime*1000L).toLong())
            }
        }.start()


    }

    override fun onPause() {
        super.onPause()
        this.start = false
        Log.d("godgod", "스레드 중지")

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        cannon.rotation = ((progress-50)*1.8).toFloat()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?){}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    override fun onClick(v: View?) {
        //대포 각도 (180 ~ 0)
        var degree : Double = (180-(cannon.rotation+90)).toDouble()
        var radiuns : Double = (degree*Math.PI)/180

        //미사일 생성 위치(x,y)
        val x : Float = cannon.left.toFloat()
        val y : Float = (cannon.top + cannon.height*0.8).toFloat()

        //미사일 imageView 생성
        val iv : ImageView = createMissileImage().apply{
            this.x = x
            this.y = y
        }
        parentView.addView(iv)

        //방향 계산 (unit_vector)
        val unitX = Math.cos(radiuns)
        val unitY = Math.sin(radiuns)

        //x,y 변위 계산
        val vectorX : Float = (speed * unitX * sleepTime).toFloat() * (dpi/160)
        val vectorY : Float = (speed * unitY * sleepTime).toFloat() * (dpi/160)


        missileList.add(Missile(iv,x,y,vectorX, vectorY))
    }


}