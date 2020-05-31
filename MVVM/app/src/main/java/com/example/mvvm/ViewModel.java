package com.example.mvvm;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;

public class ViewModel extends BaseObservable {
    //activiy Context
    private Context context;

    //대포 각도
    private float degree =0;

    //이미지 뷰
    private ImageView iv;

    //미사일 리스트
    ArrayList<Missile> missiles = new ArrayList();

    ViewModel(Context context){
        this.context = context;
    }

    public void onValueChanged(SeekBar seekBar, int progress, boolean fromUser) {
        float degree = (float)((progress-50)*1.8);
        this.degree = degree;
        notifyPropertyChanged(BR.degree);
    }

    @Bindable
    public float getDegree() {
        return degree;
    }


    public void setImageView(){
        iv = new ImageView(context);
        iv.setLayoutParams(new ConstraintLayout.LayoutParams(100 , 100));
        iv.setImageResource(R.drawable.missile);
        iv.setX(100);
        iv.setY(100);
        notifyPropertyChanged(BR.imageView);
    }
    @Bindable
    public ImageView getImageView(){
        return iv;
    }


}
