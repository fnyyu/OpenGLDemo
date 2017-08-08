package com.fny.program.opengldemo.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.fny.program.opengldemo.objects.Cube;
import com.fny.program.opengldemo.util.MatrixState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/7/24.
 * OpenGl的Renderer
 */

public class CubeRenderer extends BaseRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private Cube mCube;

    public CubeRenderer(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST); //深度检测
        GLES20.glEnable(GLES20.GL_CULL_FACE); //背面剪裁
        mCube = new Cube(mContext);
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 20, 100);
        MatrixState.setCamera(-16f, 8f, 45, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        MatrixState.setInitStack();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        MatrixState.pushMatrix();
        mCube.draw(xAngle, yAngle);
        MatrixState.popMatrix();
    }

}
