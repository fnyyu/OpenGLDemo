package com.fny.program.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.fny.program.opengldemo.render.BaseRender;
import com.fny.program.opengldemo.util.Constants;

/**
 * Created by cvter on 2017/7/28.
 */

public class MySurfaceView extends GLSurfaceView {

    private float mLastX = 0f;
    private float mLastY = 0f;
    private BaseRender render;

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
        this.render = render;
        setRenderer(render);

    }

    public MySurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastX;
                float dx = x - mLastY;
                render.yAngle -= dx * Constants.TOUCH_SCALE_FACTOR;
                render.xAngle += dy * Constants.TOUCH_SCALE_FACTOR;
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastY = y;//记录触控笔位置
        mLastX = x;//记录触控笔位置
        return true;
    }

}
