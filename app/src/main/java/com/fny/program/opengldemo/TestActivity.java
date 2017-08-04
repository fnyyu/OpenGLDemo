package com.fny.program.opengldemo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by cvter on 2017/8/4.
 */

public class TestActivity extends Activity {
    private BallSurfaceView mySurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mySurfaceView=new BallSurfaceView(this); //创建MysurfaceView对象
        mySurfaceView.requestFocus();           //获取焦点
        mySurfaceView.setFocusableInTouchMode(true);  //设置为可触控
        setContentView(mySurfaceView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();  //调用MySurfaceView的onPause()方法
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mySurfaceView.onResume();
    }
}
