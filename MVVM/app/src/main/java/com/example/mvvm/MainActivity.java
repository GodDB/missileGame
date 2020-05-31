package com.example.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.mvvm.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private ViewModel viewModel;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModel(this);
        activityMainBinding.setViewModel(viewModel);
        activityMainBinding.setActivity(this);
    }

    public void onClick(View v){
        Log.d("godgod", "클릭");
        ImageView imageView = createMissileImage(this);
        activityMainBinding.parentView.addView(imageView, 0);
    }

    //missile imageView
    public ImageView createMissileImage(Context context){
        iv = new ImageView(context);
        iv.setLayoutParams(new ConstraintLayout.LayoutParams(100 , 100));
        iv.setImageResource(R.drawable.missile);
        iv.setX(400);
        iv.setY(100);
        return iv;
    }



}
