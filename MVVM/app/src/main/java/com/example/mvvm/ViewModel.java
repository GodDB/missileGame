package com.example.mvvm;


import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


public class ViewModel extends BaseObservable {

    //속력 (초당 500)
    private final int speed = 500;

    //대포 각도
    private float degree =0;

    //포탄 객체
    ArrayList<Missile> list = new ArrayList();

    //thread
    Thread thread = new Thread(this);


    //대포 각도 계산
    public void onValueChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float degree = (float)((progress-50)*1.8);
        this.degree = degree;
        notifyPropertyChanged(BR.degree);
    }

    @Bindable
    public float getDegree() {
        return degree;
    }



    public void cal_speed(){

        float new_degree = 180-(degree+90);
        float radian = (float)(new_degree*Math.PI)/180;  // radian로 변환


        //위치 계산 (unit_Vector)
        double unit_x = Math.cos(radian);
        double unit_y = Math.sin(radian);

        //x,y축 속도 계산
        float vector_x = (float)((speed * unit_x));
        float vector_y = (float)((speed * unit_y));

        //dp로 변환
     /*   vector_x = vector_x * (dpi/160);
        vector_y = vector_y * (dpi/160);*/
     list.add(new Missile(500, 500, vector_x, vector_y));
    }


}
