package com.fny.program.opengldemo.objects;

import android.content.Context;
import android.opengl.GLES20;

import com.fny.program.opengldemo.R;
import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.MatrixState;
import com.fny.program.opengldemo.util.ResourceReader;
import com.fny.program.opengldemo.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cvter on 2017/8/4.
 */

public class Sphere {

    private Context mContext;

    private static final float UNIT_SIZE = 1.0f;//单位尺寸
    private float mRadius = 0.6f;

    private FloatBuffer mVertexBuffer; //顶点坐标
    private static final int COORDINATES_PER_VERTEX = 3; // 数组中每个顶点的坐标数
    private int mCount = 0;  //顶点个数

    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private int uMatrixLocation;
    private int program;


    public Sphere(Context context) {
        this.mContext = context;

        initVertexData();
        getProgram();

        int aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        //---------传入顶点数据数据
        GLES20.glVertexAttribPointer(aPositionLocation, COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    private void initVertexData() {
        List<Float> alVertex = new ArrayList<>();
        int angleSpan = 10; //球单位切分角度
        for (int vAngle = 0; vAngle < 180; vAngle = vAngle + angleSpan) {
            for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpan) {

                // 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
                alVertex.add(getX(vAngle, hAngle + angleSpan));
                alVertex.add(getY(vAngle, hAngle + angleSpan));
                alVertex.add(getZ(vAngle));

                alVertex.add(getX(vAngle + angleSpan, hAngle));
                alVertex.add(getY(vAngle + angleSpan, hAngle));
                alVertex.add(getZ(vAngle + angleSpan));

                alVertex.add(getX(vAngle, hAngle));
                alVertex.add(getY(vAngle, hAngle));
                alVertex.add(getZ(vAngle));

                alVertex.add(getX(vAngle, hAngle + angleSpan));
                alVertex.add(getY(vAngle, hAngle + angleSpan));
                alVertex.add(getZ(vAngle));

                alVertex.add(getX(vAngle + angleSpan, hAngle + angleSpan));
                alVertex.add(getY(vAngle + angleSpan, hAngle + angleSpan));
                alVertex.add(getZ(vAngle + angleSpan));

                alVertex.add(getX(vAngle + angleSpan, hAngle));
                alVertex.add(getY(vAngle + angleSpan, hAngle));
                alVertex.add(getZ(vAngle + angleSpan));

            }
        }

        mCount = alVertex.size() / COORDINATES_PER_VERTEX;
        float[] vertices = new float[alVertex.size()];
        for (int i = 0; i<alVertex.size(); i++){
            vertices[i] = alVertex.get(i);
        }

        mVertexBuffer = ByteBuffer
                .allocateDirect(vertices.length * Constants.BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
        mVertexBuffer.position(0);
    }

    //获取program
    private void getProgram() {
        //获取顶点着色器文本
        String vertexShaderSource = ResourceReader
                .readFileFromResource(mContext, R.raw.sphere_vertex_shader);
        //获取片段着色器文本
        String fragmentShaderSource = ResourceReader
                .readFileFromResource(mContext, R.raw.sphere_fragment_shader);
        //获取program的id
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES20.glUseProgram(program);
    }

    public void draw() {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mCount);
    }

    private float getX(int angle1, int angle2){
        return (float) (mRadius * UNIT_SIZE
                * Math.sin(Math.toRadians(angle1)) * Math
                .cos(Math.toRadians(angle2)));
    }

    private float getY(int angle1, int angle2){
        return (float) (mRadius * UNIT_SIZE
                * Math.sin(Math.toRadians(angle1)) * Math
                .sin(Math.toRadians(angle2)));
    }

    private float getZ(int angle){
        return (float) (mRadius * UNIT_SIZE
                * Math.cos(Math.toRadians(angle)));
    }


}
