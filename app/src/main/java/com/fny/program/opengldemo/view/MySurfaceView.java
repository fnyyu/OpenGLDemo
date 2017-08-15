package com.fny.program.opengldemo.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.fny.program.opengldemo.render.BaseRender;
import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.MatrixState;

/**
 * Created by cvter on 2017/7/28.
 */

public class MySurfaceView extends GLSurfaceView {

    private BaseRender mRender;
    private float mDownX = 0.0f;
    private float mDownY = 0.0f;
    private int mMinDistance;
    private int mMode = Constants.NONE;
    private float mOldDistance = 0f;


    public static class InitRender<T extends BaseRender> {

        private T object;

        public InitRender(T object) {
            this.object = object;
        }

        public T getObject() {
            return object;
        }
    }

    public void setBaseRender(BaseRender render) {
        this.mRender = render;
        setRenderer(mRender);

    }

    public MySurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mMinDistance = ViewConfiguration.get(context).getScaledTouchSlop();
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
                actionMove(event);
                return true;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mMode = Constants.NONE;
                return true;

            default:
                return super.onTouchEvent(event);
        }
    }

    private void actionMove(MotionEvent event) {
        if (mMode == Constants.ZOOM) {
            float newDistance = calculateDistance(event);

            if (Math.abs(newDistance - mOldDistance) < mMinDistance || newDistance <= 10f) {
                return;
            }

            if (Math.abs(newDistance - mOldDistance) > 2f) {
                float scale = newDistance / mOldDistance;
                MatrixState.zoom(scale, scale, scale);
            }
            mOldDistance = newDistance;

        } else if (mMode == Constants.ROTATE) {
            float mX = event.getX();
            float mY = event.getY();

            if (Math.abs(mX - mDownX) < mMinDistance && Math.abs(mY - mDownY) < mMinDistance) {
                return;
            }
            mRender.xAngle += (mX - mDownX) / 6f;
            mRender.yAngle += (mY - mDownY) / 6f;
            mDownX = mX;
            mDownY = mY;
        }

    }

    private float calculateDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

}
