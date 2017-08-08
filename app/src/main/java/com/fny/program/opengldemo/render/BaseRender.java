package com.fny.program.opengldemo.render;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cvter on 2017/8/8.
 */

public class BaseRender implements GLSurfaceView.Renderer {

    public float yAngle;
    public float xAngle;

    public BaseRender(){}

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
