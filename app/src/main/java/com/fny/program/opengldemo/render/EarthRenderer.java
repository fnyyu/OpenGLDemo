package com.fny.program.opengldemo.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.fny.program.opengldemo.objects.Earth;
import com.fny.program.opengldemo.util.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/7/24.
 * OpenGl的Renderer
 */

public class EarthRenderer extends BaseRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private Earth mEarth;

    public EarthRenderer(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);//设置屏幕背景色RGBA
        glEnable(GL_DEPTH_TEST);//打开深度检测
        glEnable(GL_CULL_FACE);//打开背面剪裁
        mEarth = new Earth(mContext);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
        MatrixState.setCamera(0, 0, 30, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        MatrixState.setInitStack();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
        MatrixState.pushMatrix();
        mEarth.draw(xAngle, yAngle);
        MatrixState.popMatrix();
    }

}
