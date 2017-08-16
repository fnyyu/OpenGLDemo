package com.fny.program.opengldemo.render;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import com.fny.program.opengldemo.R;
import com.fny.program.opengldemo.objects.FountainShooter;
import com.fny.program.opengldemo.objects.FountainSystem;
import com.fny.program.opengldemo.objects.HeightMap;
import com.fny.program.opengldemo.objects.SkyCube;
import com.fny.program.opengldemo.program.FountainShaderProgram;
import com.fny.program.opengldemo.program.HeightMapShaderProgram;
import com.fny.program.opengldemo.program.SkyCubeShaderProgram;
import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.Geometry;
import com.fny.program.opengldemo.util.MatrixHelper;
import com.fny.program.opengldemo.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.Matrix.*;
import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/8/10.
 */

public class FountainRender implements GLSurfaceView.Renderer {

    private Context mContext;
    private static final String TAG = "FountainRender";

    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];  //应用于火花和高度图
    private float[] viewSkyMatrix = new float[16];  //应用于天空盒子

    private float[] modelMatrix = new float[16];
    private float[] tempMatrix = new float[16];
    private float[] modelViewProjectMatrix = new float[16];

    private FountainShaderProgram fountainShaderProgram;
    private FountainSystem fountainSystem;
    private FountainShooter redFountainShooter;
    private FountainShooter greenFountainShooter;
    private FountainShooter blueFountainShooter;
    private long globalStartTime;

    private SkyCubeShaderProgram skyCubeShaderProgram;
    private SkyCube skyCube;
    private int skyCubeTexture;

    private HeightMapShaderProgram heightMapShaderProgram;
    private HeightMap heightMap;

    private static final float ANGLE_VARIANCE_IN_DEGREE = 5f;
    private static final float SPEED_VARIANCE = 1f;

    private int fountainTexture;

    private float xRotation;
    private float yRotation;

    private float xOffset;
    private float yOffset;

    private long frameStartTimeMs;
    private long startTimeMs;
    private int frameCount;

    public FountainRender(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);   //打开深度缓冲区
        glEnable(GL_CULL_FACE);    //使能剔除，关闭两面绘制

        heightMapShaderProgram = new HeightMapShaderProgram(mContext);
        heightMap = new HeightMap(((BitmapDrawable) mContext.getResources().getDrawable(R.mipmap.heightmap)).getBitmap());

        skyCubeShaderProgram = new SkyCubeShaderProgram(mContext);
        skyCube = new SkyCube();

        fountainShaderProgram = new FountainShaderProgram(mContext);
        fountainSystem = new FountainSystem(10000);
        globalStartTime = System.nanoTime();  //全局启动时间

        initShooter();

        fountainTexture = TextureHelper.loadTexture(mContext, R.mipmap.particle_texture, false);

        skyCubeTexture = TextureHelper.loadCubeMap(mContext,
                new int[]{
                        R.mipmap.left, R.mipmap.right,
                        R.mipmap.bottom, R.mipmap.top,
                        R.mipmap.front, R.mipmap.back
                });
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        updateViewMatrix();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        limitFrameRate(24);
        longFrameRate();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        drawHeightMap();
        drawSkyCube();
        drawFountain();
    }

    private void initShooter() {
        final Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);

        redFountainShooter = new FountainShooter(      //红色火花的位置、方向、颜色、角度、速度
                new Geometry.Point(-1f, 0f, 0f),
                particleDirection,
                Color.rgb(255, 25, 25),
                ANGLE_VARIANCE_IN_DEGREE,
                SPEED_VARIANCE);

        greenFountainShooter = new FountainShooter(     //绿色火花的位置、方向、颜色、角度、速度
                new Geometry.Point(0f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 255, 25),
                ANGLE_VARIANCE_IN_DEGREE,
                SPEED_VARIANCE);

        blueFountainShooter = new FountainShooter(      //蓝色火花的位置、方向、颜色、角度、速度
                new Geometry.Point(1f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 25, 255),
                ANGLE_VARIANCE_IN_DEGREE,
                SPEED_VARIANCE);
    }

    private void drawFountain() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;  //纳秒转换为秒

        redFountainShooter.addFountains(fountainSystem, currentTime, 1);
        greenFountainShooter.addFountains(fountainSystem, currentTime, 1);
        blueFountainShooter.addFountains(fountainSystem, currentTime, 1);

        setIdentityM(modelMatrix, 0);
        updateMvpMatrix();

        glDepthMask(false);
        glEnable(GL_BLEND);    //打开混合功能
        glBlendFunc(GL_ONE, GL_ONE);

        fountainShaderProgram.useProgram();
        fountainShaderProgram.setUniforms(modelViewProjectMatrix, currentTime, fountainTexture);
        fountainSystem.bindData(fountainShaderProgram);
        fountainSystem.draw();

        glDisable(GL_BLEND);       //关闭混合功能
        glDepthMask(true);
    }

    private void drawSkyCube() {
        setIdentityM(modelMatrix, 0);
        updateMvpMatrixForSky();

        glDepthFunc(GL_LEQUAL); //更换默认深度测试算法

        skyCubeShaderProgram.useProgram();
        skyCubeShaderProgram.setUniforms(modelViewProjectMatrix, skyCubeTexture);
        skyCube.bindData(skyCubeShaderProgram);
        skyCube.drawSelf();

        glDepthFunc(GL_LESS);
    }

    private void drawHeightMap() {
        setIdentityM(modelMatrix, 0);
        scaleM(modelMatrix, 0, 100f, 10f, 100f);
        updateMvpMatrix();
        heightMapShaderProgram.useProgram();
        heightMapShaderProgram.setUniforms(modelViewProjectMatrix);
        heightMap.bindData(heightMapShaderProgram);
        heightMap.draw();
    }

    private void updateViewMatrix() {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);

        System.arraycopy(viewMatrix, 0, viewSkyMatrix, 0, viewMatrix.length);

        translateM(viewMatrix, 0, 0 - xOffset, -1.5f - yOffset, -5f);
    }

    //把矩阵合并成为模型视图投影矩阵
    private void updateMvpMatrix() {
        multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        multiplyMM(modelViewProjectMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }

    //把天空盒子矩阵合并成为模型视图投影矩阵
    private void updateMvpMatrixForSky() {
        multiplyMM(tempMatrix, 0, viewSkyMatrix, 0, modelMatrix, 0);
        multiplyMM(modelViewProjectMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }

    //限制帧率
    private void limitFrameRate(int framePerSecond) {
        long elapsedFrameTimeMs = SystemClock.elapsedRealtime() - frameStartTimeMs;
        long expectedFrameTimeMs = 1000 / framePerSecond;
        long timesToSleepMs = expectedFrameTimeMs - elapsedFrameTimeMs;

        if (timesToSleepMs > 0) {
            SystemClock.sleep(timesToSleepMs);
        }
        frameStartTimeMs = SystemClock.elapsedRealtime();
    }

    //测量记录每秒帧数
    private void longFrameRate() {
        if (Constants.ON) {
            long elapsedRealTimeMs = SystemClock.elapsedRealtime();
            double elapsedSeconds = (elapsedRealTimeMs - startTimeMs) / 1000.0;

            if (elapsedSeconds >= 10.0) {
                Log.v(TAG, frameCount / elapsedSeconds + " fps");
                startTimeMs = SystemClock.elapsedRealtime();
                frameCount = 0;
            }
            frameCount ++;
        }
    }

    public void handleTouchDrag(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;

        if (yRotation < -90) {
            yRotation = -90;
        } else if (yRotation > 90) {
            yRotation = 90;
        }
        updateViewMatrix();
    }

    public void handleOffsetsChanged(float xOffset, float yOffset) {
        this.xOffset = (xOffset - 0.5f) * 2.5f;
        this.yOffset = (yOffset - 0.5f) * 2.5f;
        updateViewMatrix();
    }
}