package com.fny.program.opengldemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.fny.program.opengldemo.render.BallRender;
import com.fny.program.opengldemo.util.Constants;

/**
 * Created by cvter on 2017/8/4.
 */

public class BallSurfaceView extends GLSurfaceView {

    private BallRender mRenderer;

    private float mDownX = 0.0f;
    private float mDownY = 0.0f;
    private int mMode = Constants.NONE;
    private int mMinDistance;
    private float mOldDistance = 0f;

    public BallSurfaceView(Context context) {
        super(context);
        mMinDistance = ViewConfiguration.get(context).getScaledTouchSlop();
        mRenderer = new BallRender();
        this.setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mMode = Constants.ROTATE;
                mDownX = event.getX();
                mDownY = event.getY();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                mOldDistance = calculateDistance(event);
                if (mOldDistance > 10f) {
                    mMode = Constants.ZOOM;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (mMode == Constants.ROTATE){
                    float mX = event.getX();
                    float mY = event.getY();
                    if (Math.abs(mX - mDownX) < mMinDistance && Math.abs(mY - mDownY) < mMinDistance) {
                        return true;
                    }
                    mRenderer.mLightX += (mX-mDownX)/10;
                    mRenderer.mLightY -= (mY-mDownY)/10;
                    mDownX = mX;
                    mDownY = mY;
                }else if (mMode == Constants.ZOOM){
                    float newDistance = calculateDistance(event);

                    if (Math.abs(newDistance - mOldDistance) < mMinDistance || newDistance <= 10f) {
                        return true;
                    }

                    if (Math.abs(newDistance - mOldDistance) > 2f) {
                        mRenderer.mScale = newDistance / mOldDistance;
                    }
                    mOldDistance = newDistance;
                }

                return true;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mMode = Constants.NONE;
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    private float calculateDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
