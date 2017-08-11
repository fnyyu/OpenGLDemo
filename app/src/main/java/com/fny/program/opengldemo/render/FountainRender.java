package com.fny.program.opengldemo.render;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.provider.Settings;

import com.fny.program.opengldemo.R;
import com.fny.program.opengldemo.objects.FountainShooter;
import com.fny.program.opengldemo.objects.FountainSystem;
import com.fny.program.opengldemo.program.FountainShaderProgram;
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

    private final float angleVarianceInDegress = 5f;
    private final float speedVariance = 1f;

    private int texture;

    public FountainRender(Context context){
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        fountainShaderProgram = new FountainShaderProgram(mContext);
        fountainSystem = new FountainSystem(10000);
        globalStartTime = System.nanoTime();

        final Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);

        redFountainShooter = new FountainShooter(
                new Geometry.Point(-1f, 0f, 0f),
                particleDirection,
                Color.rgb(255, 25, 25),
                angleVarianceInDegress,
                speedVariance);

        greenFountainShooter = new FountainShooter(
                new Geometry.Point(0f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 255, 25),
                angleVarianceInDegress,
                speedVariance);

        blueFountainShooter = new FountainShooter(
                new Geometry.Point(1f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 25, 255),
                angleVarianceInDegress,
                speedVariance);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        texture = TextureHelper.loadTexture(mContext, R.mipmap.particle_texture, false);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0 ,width, height);

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width/(float) height, 1f, 10f);
        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        float currentTime = (System.nanoTime() - globalStartTime) /1000000000f;

        redFountainShooter.addParticles(fountainSystem, currentTime, 5);
        greenFountainShooter.addParticles(fountainSystem, currentTime, 5);
        blueFountainShooter.addParticles(fountainSystem, currentTime, 5);

        fountainShaderProgram.useProgram();
        fountainShaderProgram.setUniforms(viewProjectionMatrix, currentTime, texture);
        fountainSystem.bindData(fountainShaderProgram);

        fountainSystem.draw();
    }
}
