package com.fny.program.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.fny.program.opengldemo.render.BaseRender;

/**
 * Created by cvter on 2017/7/28.
 */

public class MySurfaceView extends GLSurfaceView {

    private BaseRender mRender;
    private float mDownX = 0.0f;
    private float mDownY = 0.0f;

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
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
            case MotionEvent.ACTION_MOVE:
                float mX = event.getX();
                float mY = event.getY();
                mRender.xAngle += (mX-mDownX)/10;
                mRender.yAngle -= (mY-mDownY)/10;
                mDownX = mX;
                mDownY = mY;
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

}
