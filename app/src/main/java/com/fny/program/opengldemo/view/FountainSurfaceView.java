package com.fny.program.opengldemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.fny.program.opengldemo.render.FountainRender;

/**
 * Created by cvter on 2017/8/10.
 */

public class FountainSurfaceView extends GLSurfaceView {

    private FountainRender mRender;
    private float lastX = 0;
    private float lastY = 0;

    public FountainSurfaceView(Context context) {
        super(context);
        mRender = new FountainRender(context);
        setEGLContextClientVersion(2);
        setRenderer(mRender);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaX = event.getX() - lastX;
                final float deltaY = event.getY() - lastY;

                lastX = event.getX();
                lastY = event.getY();

                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mRender.handleTouchDrag(deltaX, deltaY);
                    }
                });

                break;

            default:
                break;
        }

        return true;
    }
}
