package com.fny.program.opengldemo.render;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.fny.program.opengldemo.objects.Ball;
import com.fny.program.opengldemo.util.MatrixState;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cvter on 2017/8/4.
 */

public class BallRender implements GLSurfaceView.Renderer {
    // 环境光
    private static final float[] MAT_AMBIENT = {0.5f, 0.6f, 0.8f, 1.0f};
    private FloatBuffer matAmbientBuf;
    // 平行入射光
    private static final float[] MAT_DIFFUSE = {0.4f, 0.6f, 0.8f, 1.0f};
    private FloatBuffer matDiffuseBuf;
    // 高亮区域
    private static final float[] MAT_SPECULAR = {0.2f * 0.4f, 0.2f * 0.6f, 0.2f * 0.8f, 1.0f};
    private FloatBuffer matSpecularBuf;

    private Ball mSphere = new Ball();

    public volatile float mLightX = 10f;
    public volatile float mLightY = 10f;
    private volatile float mLightZ = 10f;
    public volatile float mScale = 1.0f;

    @Override
    public void onDrawFrame(GL10 gl) {
        // 清楚屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // 重置当前的模型观察矩阵
        gl.glLoadIdentity();

        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);

        // 材质
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbientBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuseBuf);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, matSpecularBuf);
        // 镜面指数 0~128 越小越粗糙
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 96.0f);

        //光源位置
        float[] lightPosition = {mLightX, mLightY, mLightZ, 0.0f};
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(lightPosition.length * 4).order(ByteOrder.nativeOrder());
        FloatBuffer matrixBuffer = byteBuffer.asFloatBuffer();
        matrixBuffer.put(lightPosition);
        matrixBuffer.position(0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, matrixBuffer);

        gl.glTranslatef(0.0f, 0.0f, -3.0f);
        gl.glScalef(mScale, mScale, mScale);
        mSphere.draw(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);

        // 设置投影矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 90.0f, (float) width / height, 0.1f, 50.0f);

        // 选择模型观察矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // 重置模型观察矩阵
        gl.glLoadIdentity();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {
        // 对透视进行修正
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0, 0.0f, 0.0f, 0.0f);

        // 启动阴影平滑
        gl.glShadeModel(GL10.GL_SMOOTH);

        // 复位深度缓存
        gl.glClearDepthf(1.0f);
        // 启动深度测试
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // 所做深度测试的类型
        gl.glDepthFunc(GL10.GL_LEQUAL);

        initBuffers();
    }

    private void initBuffers() {
        ByteBuffer bufTemp = ByteBuffer.allocateDirect(MAT_AMBIENT.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        matAmbientBuf = bufTemp.asFloatBuffer();
        matAmbientBuf.put(MAT_AMBIENT);
        matAmbientBuf.position(0);

        bufTemp = ByteBuffer.allocateDirect(MAT_DIFFUSE.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        matDiffuseBuf = bufTemp.asFloatBuffer();
        matDiffuseBuf.put(MAT_DIFFUSE);
        matDiffuseBuf.position(0);

        bufTemp = ByteBuffer.allocateDirect(MAT_SPECULAR.length * 4);
        bufTemp.order(ByteOrder.nativeOrder());
        matSpecularBuf = bufTemp.asFloatBuffer();
        matSpecularBuf.put(MAT_SPECULAR);
        matSpecularBuf.position(0);
    }
}
