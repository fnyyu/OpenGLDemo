package com.fny.program.opengldemo.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.fny.program.opengldemo.R;
import com.fny.program.opengldemo.util.ResourceReader;
import com.fny.program.opengldemo.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.fny.program.opengldemo.util.Constants.BYTE_PRE_FLOAT;

/**
 * Created by cvter on 2017/8/2.
 */

public class Circle {

    private Context context;

    private float x;
    private float y;
    private float radius;
    private int count;

    private static final int POSITION_COMPONENT_COUNT = 2;  //两个分量

    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";
    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    private int uColorLocation;
    private int program;

    private float[] projectMatrix = new float[16];

    public Circle(Context context, int count) {
        this.context = context;
        this.count = count;
        x = 0f;
        y = 0f;
        radius = 0.6f;
        initVertexBuffer();
    }

    private void initVertexBuffer() {
        FloatBuffer vertexBuffer;
        int aPositionLocation;
        final int nodeCount = count + 2;
        float[] circleCoordinates = new float[nodeCount * POSITION_COMPONENT_COUNT];
        int offset = 0;
        circleCoordinates[offset++] = x;
        circleCoordinates[offset++] = y;

        for (int i = 0; i < count + 1; i++) {
            float angleInRadians = ((float)i/(float)count) * ((float)Math.PI * 2f);
            circleCoordinates[offset++] = x + radius * (float)Math.sin(angleInRadians);
            circleCoordinates[offset++] = y + radius * (float)Math.cos(angleInRadians);
        }

        vertexBuffer = ByteBuffer.allocateDirect(circleCoordinates.length * BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(circleCoordinates);
        vertexBuffer.position(0);

        getProgram();

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

    }

    private void getProgram() {
        //获取顶点着色器文本
        String vertexShaderSource = ResourceReader
                .readFileFromResource(context, R.raw.circle_vertex_shader);
        //获取片段着色器文本
        String fragmentShaderSource = ResourceReader
                .readFileFromResource(context, R.raw.circle_fragment_shader);

        //获取program的id
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES20.glUseProgram(program);
    }

    public void projectionMatrix(int width, int height) {
        float aspect;
        if (width > height) {
            aspect = (float) width / (float) height;
            Matrix.orthoM(projectMatrix, 0, -aspect, aspect, -1f, 1f, -1f, 1f);
        } else {
            aspect = (float) height / (float) width;
            Matrix.orthoM(projectMatrix, 0, -1f, 1f, -aspect, aspect, -1f, 1f);
        }
    }

    public void draw() {
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, count + 2);
    }


}
