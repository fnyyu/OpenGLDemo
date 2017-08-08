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

/**
 * Created by cvter on 2017/7/27.
 * 正方体
 */

public class Cube {

    private Context mContext;
    //共有72个顶点坐标，每个面包含12个顶点坐标
    private static final int POSITION_COMPONENT_COUNT = 12*6;
    // 数组中每个顶点的坐标数
    private static final int COORDINATES_PER_VERTEX = 3;
    // 颜色数组中每个颜色的值数
    private static final int COORDINATES_PER_COLOR = 4;

    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";
    private int mUMatrixLocation;
    private int program;

    private static float vertices[] = {
            //前面
            0,0,1.0f,
            1.0f,1.0f,1.0f,
            -1.0f,1.0f,1.0f,
            0,0,1.0f,
            -1.0f,1.0f,1.0f,
            -1.0f,-1.0f,1.0f,
            0,0,1.0f,
            -1.0f,-1.0f,1.0f,
            1.0f,-1.0f,1.0f,
            0,0,1.0f,
            1.0f,-1.0f,1.0f,
            1.0f,1.0f,1.0f,
            //后面
            0,0,-1.0f,
            1.0f,1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            0,0,-1.0f,
            1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            0,0,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f,1.0f,-1.0f,
            0,0,-1.0f,
            -1.0f,1.0f,-1.0f,
            1.0f,1.0f,-1.0f,
            //左面
            -1.0f,0,0,
            -1.0f,1.0f,1.0f,
            -1.0f,1.0f,-1.0f,
            -1.0f,0,0,
            -1.0f,1.0f,-1.0f,
            -1.0f,-1.0f,-1.0f,
            -1.0f,0,0,
            -1.0f,-1.0f,-1.0f,
            -1.0f,-1.0f,1.0f,
            -1.0f,0,0,
            -1.0f,-1.0f,1.0f,
            -1.0f,1.0f,1.0f,
            //右面
            1.0f,0,0,
            1.0f,1.0f,1.0f,
            1.0f,-1.0f,1.0f,
            1.0f,0,0,
            1.0f,-1.0f,1.0f,
            1.0f,-1.0f,-1.0f,
            1.0f,0,0,
            1.0f,-1.0f,-1.0f,
            1.0f,1.0f,-1.0f,
            1.0f,0,0,
            1.0f,1.0f,-1.0f,
            1.0f,1.0f,1.0f,
            //上面
            0,1.0f,0,
            1.0f,1.0f,1.0f,
            1.0f,1.0f,-1.0f,
            0,1.0f,0,
            1.0f,1.0f,-1.0f,
            -1.0f,1.0f,-1.0f,
            0,1.0f,0,
            -1.0f,1.0f,-1.0f,
            -1.0f,1.0f,1.0f,
            0,1.0f,0,
            -1.0f,1.0f,1.0f,
            1.0f,1.0f,1.0f,
            //下面
            0,-1.0f,0,
            1.0f,-1.0f,1.0f,
            -1.0f,-1.0f,1.0f,
            0,-1.0f,0,
            -1.0f,-1.0f,1.0f,
            -1.0f,-1.0f,-1.0f,
            0,-1.0f,0,
            -1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,-1.0f,
            0,-1.0f,0,
            1.0f,-1.0f,-1.0f,
            1.0f,-1.0f,1.0f
    };

    //顶点颜色值数组，每个顶点4个色彩值RGBA
    private static float colors[]=new float[]{
            //前面
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            1,1,1,0,//中间为白色
            1,0,0,0,
            1,0,0,0,
            //后面
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            1,1,1,0,//中间为白色
            0,0,1,0,
            0,0,1,0,
            //左面
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            1,1,1,0,//中间为白色
            1,0,1,0,
            1,0,1,0,
            //右面
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            1,1,1,0,//中间为白色
            1,1,0,0,
            1,1,0,0,
            //上面
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            1,1,1,0,//中间为白色
            0,1,0,0,
            0,1,0,0,
            //下面
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
            1,1,1,0,//中间为白色
            0,1,1,0,
            0,1,1,0,
    };

    public Cube(Context mContext){
        this.mContext = mContext;

        FloatBuffer vertexBuffer = ByteBuffer
                .allocateDirect(vertices.length * Constants.BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(vertices);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);

        //颜色buffer
        FloatBuffer colorBuffer = ByteBuffer
                .allocateDirect(colors.length * Constants.BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);

        getProgram();

        int aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        int aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        mUMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        //---------传入顶点数据数据
        GLES20.glVertexAttribPointer(aPositionLocation, COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        //---------传入颜色数据
        GLES20.glVertexAttribPointer(aColorLocation, COORDINATES_PER_COLOR,
                GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    //获取program
    private void getProgram(){
        //获取顶点着色器文本
        String vertexShaderSource = ResourceReader
                .readFileFromResource(mContext, R.raw.cube_vertex_shader);
        //获取片段着色器文本
        String fragmentShaderSource = ResourceReader
                .readFileFromResource(mContext, R.raw.cube_fragment_shader);
        //获取program的id
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES20.glUseProgram(program);
    }

    public void draw(float xAngle, float yAngle) {
        MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴转动
        MatrixState.rotate(yAngle, 0, 1, 0);//绕Y轴转动
        GLES20.glUniformMatrix4fv(mUMatrixLocation, 1, false, MatrixState.getFinalMatrix(),0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
    }
}
