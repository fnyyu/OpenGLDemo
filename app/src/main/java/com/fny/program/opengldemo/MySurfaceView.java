package com.fny.program.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.fny.program.opengldemo.util.MatrixState;

import static android.view.MotionEvent.*;

/**
 * Created by cvter on 2017/7/28.
 */

public class MySurfaceView extends GLSurfaceView {

    private float mLastX = 0f;

    public MySurfaceView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();

        switch (event.getAction()){
            case ACTION_DOWN:
                break;
            case ACTION_MOVE:
//                float dx = x - mLastX;
//                if (dx > 0){
//                    MatrixState.translate(0.1f, 0, 0);
//                }else {
//                    MatrixState.translate(-0.1f, 0, 0);
//                }
                break;
            case ACTION_UP:
                MatrixState.rotate(30, 0, 1, 0);
//                MatrixState.scale(0.4f, 1.5f, 0.6f);
                break;


        }

        mLastX = x;

        return true;
    }
}
