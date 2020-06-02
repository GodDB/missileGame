package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.mvvm.databinding.ActivityMainBinding;
import com.example.mvvm.databinding.MissileBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ViewModel viewModel;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModel();
        activityMainBinding.setViewModel(viewModel);
        activityMainBinding.setActivity(this);



    }

    public void onClick(View v){
        MissileBinding missileBinding =DataBindingUtil.inflate(getLayoutInflater(), R.layout.missile, activityMainBinding.parentView, true);
        missileBinding.setViewModel(viewModel);
        viewModel.cal_speed();
    }






}
