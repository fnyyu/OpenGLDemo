package com.fny.program.opengldemo.render;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;

import com.fny.program.opengldemo.R;
import com.fny.program.opengldemo.objects.FountainShooter;
import com.fny.program.opengldemo.objects.FountainSystem;
import com.fny.program.opengldemo.objects.SkyCube;
import com.fny.program.opengldemo.program.FountainShaderProgram;
import com.fny.program.opengldemo.program.SkyCubeShaderProgram;
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
    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] viewProjectionMatrix = new float[16];

    private FountainShaderProgram fountainShaderProgram;
    private FountainSystem fountainSystem;
    private FountainShooter redFountainShooter;
    private FountainShooter greenFountainShooter;
    private FountainShooter blueFountainShooter;
    private long globalStartTime;

    private SkyCubeShaderProgram skyCubeShaderProgram;
    private SkyCube skyCube;
    private int skyCubeTexture;

    private static final float ANGLE_VARIANCE_IN_DEGREE = 5f;
    private static final float SPEED_VARIANCE = 1f;

    private int fountainTexture;

    private float xRotation;
    private float yRotation;

    public FountainRender(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        fountainShaderProgram = new FountainShaderProgram(mContext);
        fountainSystem = new FountainSystem(10000);
        globalStartTime = System.nanoTime();  //全局启动时间

        skyCubeShaderProgram = new SkyCubeShaderProgram(mContext);
        skyCube = new SkyCube();
        skyCubeTexture = TextureHelper.loadCubeMap(mContext,
                new int[]{
                        R.mipmap.left, R.mipmap.right,
                        R.mipmap.bottom, R.mipmap.top,
                        R.mipmap.front, R.mipmap.back
                });

        initShooter();

        fountainTexture = TextureHelper.loadTexture(mContext, R.mipmap.particle_texture, false);
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

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        drawSkyCube();
        drawFountain();
    }

    private void drawFountain() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;  //纳秒转换为秒

        redFountainShooter.addFountains(fountainSystem, currentTime, 1);
        greenFountainShooter.addFountains(fountainSystem, currentTime, 1);
        blueFountainShooter.addFountains(fountainSystem, currentTime, 1);

        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        glEnable(GL_BLEND);    //打开混合功能
        glBlendFunc(GL_ONE, GL_ONE);

        fountainShaderProgram.useProgram();
        fountainShaderProgram.setUniforms(viewProjectionMatrix, currentTime, fountainTexture);
        fountainSystem.bindData(fountainShaderProgram);

        fountainSystem.draw();

        glDisable(GL_BLEND);       //关闭混合功能
    }

    private void drawSkyCube() {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        skyCubeShaderProgram.useProgram();
        skyCubeShaderProgram.setUniforms(viewProjectionMatrix, skyCubeTexture);

        skyCube.bindData(skyCubeShaderProgram);
        skyCube.drawSelf();
    }

    public void handleTouchDrag(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;

        if (yRotation < -90) {
            yRotation = -90;
        } else if (yRotation > 90) {
            yRotation = 90;
        }
    }
}
