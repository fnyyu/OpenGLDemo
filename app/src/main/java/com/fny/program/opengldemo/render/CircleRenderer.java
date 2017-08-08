package com.fny.program.opengldemo.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.fny.program.opengldemo.objects.Circle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * Created by cvter on 2017/7/24.
 * OpenGl的Renderer
 */

public class CircleRenderer extends BaseRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private Circle mCircle;

    public CircleRenderer(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mCircle = new Circle(mContext, 100);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        mCircle.projectionMatrix(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        mCircle.draw();

    }

}
