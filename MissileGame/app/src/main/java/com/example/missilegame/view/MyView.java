package com.example.missilegame.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import com.example.missilegame.R;
import com.example.missilegame.model.Cannon;
import com.example.missilegame.model.Missile;

import java.util.ArrayList;


public class MyView extends View implements Runnable{

    final Context myContext;
    final int width; //화면 넓이
    final int height; //화면 높이

    private Thread gameThread;
    private Cannon cannon;
    private ArrayList<Missile> missile_list;

    private boolean start = true; // 스레드 체크

    private final Paint p1;
    private final Paint p2;

    public MyView(Context context, int x, int y, Cannon cannon) {
        super(context);
        this.myContext = context;
        this.width = x;
        this.height = y;
        this.cannon = cannon;

        setBackgroundResource(R.drawable.background);
        missile_list = new ArrayList();

        p1 = new Paint();
        p2 = new Paint();
        p2.setColor(Color.WHITE);
    }


    @Override
    synchronized public void onDraw(Canvas canvas) {
        //미사일 그리기
        if(missile_list.size() >0){
            for(Missile missile : missile_list){
                canvas.drawCircle(missile.getCur_x(), missile.getCur_y(), 20, p1);
                missile.move();
            }
        }

        //대포 그리기
        canvas.rotate(cannon.getDegree(), cannon.getPivot_x(), cannon.getPivot_y());
        canvas.drawRect(cannon.getRect(), p1);

        //대포받침대 (원형) 그리기
        canvas.drawCircle(cannon.getPivot_x(),
                cannon.getPivot_y(),
                70,
                p2);

        checkMissile();

    }

    //화면 바깥으로 넘어가면 미사일 삭제
    public void checkMissile(){
        for(int i=0; i<missile_list.size(); i++){
            int x = missile_list.get(i).getCur_x();
            int y = missile_list.get(i).getCur_y();

            if(x<=100 || x>= width-100 || y<=200 ){
                missile_list.remove(i);
            }
        }
    }

    @Override
    public void run() {
    /*    Log.d("godgod", "스레드 실행");*/
        while(start){
            try {
               /* Log.d("godgod", "스레드 영업중");*/
                Thread.sleep(100);
                invalidate(); // 뷰 초기화
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void setMissile(Missile missile){
        missile_list.add(missile);
    }

    public void stop(){
        Log.d("godgod", "스레드 중지");
        this.start = false;
        gameThread.interrupt();
    }

    public void resume(){
        Log.d("godgod", "스레드 실행");
        this.start = true;
        //스레드 생성
        gameThread = null;
        gameThread = new Thread(this);
        gameThread.start();
    }


}
