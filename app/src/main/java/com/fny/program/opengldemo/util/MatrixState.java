package com.fny.program.opengldemo.util;

import android.opengl.Matrix;

/**
 * Created by cvter on 2017/7/27.
 */

public class MatrixState {
    private static float[] mSavedMatrix = new float[16];// 4x4矩阵 存储投影矩阵
    private static float[] mMatrix = new float[16];// 摄像机位置朝向9参数矩阵

    private static float[] mCMatrix = new float[16];// 变换矩阵

    static {
        Matrix.setIdentityM(mCMatrix, 0);
    }

    //保护变换矩阵的栈
    static float[][] mStack=new float[10][16];
    static int stackTop=-1;

    public static void setInitStack()//获取不变换初始矩阵
    {
        mCMatrix=new float[16];
        Matrix.setRotateM(mCMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix()//保护变换矩阵
    {
        stackTop++;
        for(int i=0;i<16;i++)
        {
            mStack[stackTop][i]=mCMatrix[i];
        }
    }

    public static void popMatrix()//恢复变换矩阵
    {
        for(int i=0;i<16;i++)
        {
            mCMatrix[i]=mStack[stackTop][i];
        }
        stackTop--;
    }


    public static void translate(float x, float y, float z) {
        Matrix.translateM(mCMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {// 设置绕xyz轴移动
        Matrix.rotateM(mCMatrix, 0, angle, x, y, z);
    }

    public static void zoom(float x, float y, float z) {
        Matrix.scaleM(mCMatrix, 0, x, y, z);
    }

    // 设置摄像机
    public static void setCamera(float cx, // 摄像机坐标
                                 float cy,
                                 float cz,
                                 float tx, // 摄像机目标点坐标
                                 float ty,
                                 float tz,
                                 float upx, // 摄像机UP向量
                                 float upy,
                                 float upz
    ) {
        Matrix.setLookAtM(mMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    // 设置透视投影参数
    public static void setProjectFrustum(float left, // 相对near面
                                         float right,
                                         float bottom,
                                         float top,
                                         float near, // near面距离
                                         float far // far面距离
    ) {
        Matrix.frustumM(mSavedMatrix, 0, left, right, bottom, top, near, far);
    }

    // 获取具体物体的总变换矩阵
    static float[] mMVPMatrix = new float[16];

    public static float[] getFinalMatrix() {

        Matrix.multiplyMM(mMVPMatrix, 0, mMatrix, 0, mCMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mSavedMatrix, 0, mMVPMatrix, 0);

//        Matrix.multiplyMM(mMVPMatrix, 0, mSavedMatrix, 0, mMatrix, 0);
        return mMVPMatrix;
    }
}
