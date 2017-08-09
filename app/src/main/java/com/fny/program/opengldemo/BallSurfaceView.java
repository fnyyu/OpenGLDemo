package com.fny.program.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.fny.program.opengldemo.render.BallRender;

/**
 * Created by cvter on 2017/8/4.
 */

public class BallSurfaceView extends GLSurfaceView {

    private BallRender mRenderer;

    private float mDownX = 0.0f;
    private float mDownY = 0.0f;

    public BallSurfaceView(Context context) {
        super(context);

        mRenderer = new BallRender();
        this.setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                return true;

            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
                mRenderer.mLightX += (mX-mDownX)/10;
                mRenderer.mLightY -= (mY-mDownY)/10;
                mDownX = mX;
                mDownY = mY;
                return true;

            case MotionEvent.ACTION_POINTER_UP:
                return true;

            case MotionEvent.ACTION_UP:
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }
}
