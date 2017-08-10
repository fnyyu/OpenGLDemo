package com.fny.program.opengldemo.objects;

import android.content.Context;
import android.opengl.GLES20;

import com.fny.program.opengldemo.R;
import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.MatrixState;
import com.fny.program.opengldemo.util.ResourceReader;
import com.fny.program.opengldemo.util.ShaderHelper;
import com.fny.program.opengldemo.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

/**
 * Created by cvter on 2017/7/27.
 * 正方体
 */

public class Dog {

    private Context mContext;
    //共有72个顶点坐标，每个面包含12个顶点坐标
    private static final int POSITION_COMPONENT_COUNT = 12*6;
    // 数组中每个顶点的坐标数
    private static final int COORDINATES_PER_VERTEX = 3;

    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (COORDINATES_PER_VERTEX + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTE_PRE_FLOAT;

    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    private static final String U_TEXTURE_UNIT = "u_TextureUnit";
    private int mUMatrixLocation;
    private int program;
    private int uTextureUnitLocation;
    private int aTextureCoordinates;
    private int mTexture;

    private static float vertices[] = {
            //X   Y   Z        S    T
            //前面
            0,0,1.0f,           0.5f,0.5f,
            1.0f,1.0f,1.0f,     1.0f,0,
            -1.0f,1.0f,1.0f,    0,0,
            0,0,1.0f,           0.5f,0.5f,
            -1.0f,1.0f,1.0f,    0,0,
            -1.0f,-1.0f,1.0f,   0,1.0f,
            0,0,1.0f,           0.5f,0.5f,
            -1.0f,-1.0f,1.0f,   0,1.0f,
            1.0f,-1.0f,1.0f,    1.0f,1.0f,
            0,0,1.0f,           0.5f,0.5f,
            1.0f,-1.0f,1.0f,    1.0f,1.0f,
            1.0f,1.0f,1.0f,     1.0f,0,
            //后面
            0,0,-1.0f,          0.5f,0.5f,
            1.0f,1.0f,-1.0f,    1.0f,0,
            1.0f,-1.0f,-1.0f,   1.0f,1.0f,
            0,0,-1.0f,          0.5f,0.5f,
            1.0f,-1.0f,-1.0f,   1.0f,1.0f,
            -1.0f,-1.0f,-1.0f,  0,1.0f,
            0,0,-1.0f,          0.5f,0.5f,
            -1.0f,-1.0f,-1.0f,  0,1.0f,
            -1.0f,1.0f,-1.0f,   0,0,
            0,0,-1.0f,          0.5f,0.5f,
            -1.0f,1.0f,-1.0f,   0,0,
            1.0f,1.0f,-1.0f,    1.0f,0,
            //左面
            -1.0f,0,0,          0.5f,0.5f,
            -1.0f,1.0f,1.0f,    1.0f,0,
            -1.0f,1.0f,-1.0f,   0,0,
            -1.0f,0,0,          0.5f,0.5f,
            -1.0f,1.0f,-1.0f,   0,0,
            -1.0f,-1.0f,-1.0f,  0,1.0f,
            -1.0f,0,0,          0.5f,0.5f,
            -1.0f,-1.0f,-1.0f,  0,1.0f,
            -1.0f,-1.0f,1.0f,   1.0f,1.0f,
            -1.0f,0,0,          0.5f,0.5f,
            -1.0f,-1.0f,1.0f,   1.0f,1.0f,
            -1.0f,1.0f,1.0f,    1.0f,0,
            //右面
            1.0f,0,0,           0.5f,0.5f,
            1.0f,1.0f,1.0f,     0,0,
            1.0f,-1.0f,1.0f,    0,1.0f,
            1.0f,0,0,           0.5f,0.5f,
            1.0f,-1.0f,1.0f,    0,1.0f,
            1.0f,-1.0f,-1.0f,   1.0f,1.0f,
            1.0f,0,0,           0.5f,0.5f,
            1.0f,-1.0f,-1.0f,   1.0f,1.0f,
            1.0f,1.0f,-1.0f,    1.0f,0,
            1.0f,0,0,           0.5f,0.5f,
            1.0f,1.0f,-1.0f,    1.0f,0,
            1.0f,1.0f,1.0f,     0,0,
            //上面
            0,1.0f,0,           0.5f,0.5f,
            1.0f,1.0f,1.0f,     1.0f,0,
            1.0f,1.0f,-1.0f,    1.0f,1.0f,
            0,1.0f,0,           0.5f,0.5f,
            1.0f,1.0f,-1.0f,    1.0f,1.0f,
            -1.0f,1.0f,-1.0f,   0,1.0f,
            0,1.0f,0,           0.5f,0.5f,
            -1.0f,1.0f,-1.0f,   0,1.0f,
            -1.0f,1.0f,1.0f,    0,0,
            0,1.0f,0,           0.5f,0.5f,
            -1.0f,1.0f,1.0f,    0,0,
            1.0f,1.0f,1.0f,     1.0f,0,
            //下面
            0,-1.0f,0,          0.5f,0.5f,
            1.0f,-1.0f,1.0f,    1.0f,0,
            -1.0f,-1.0f,1.0f,   0,0,
            0,-1.0f,0,          0.5f,0.5f,
            -1.0f,-1.0f,1.0f,   0,0,
            -1.0f,-1.0f,-1.0f,  0,1.0f,
            0,-1.0f,0,          0.5f,0.5f,
            -1.0f,-1.0f,-1.0f,  0,1.0f,
            1.0f,-1.0f,-1.0f,   1.0f,1.0f,
            0,-1.0f,0,          0.5f,0.5f,
            1.0f,-1.0f,-1.0f,   1.0f,1.0f,
            1.0f,-1.0f,1.0f ,   1.0f,0
    };


    public Dog(Context mContext){
        this.mContext = mContext;

        FloatBuffer vertexBuffer = ByteBuffer
                .allocateDirect(vertices.length * Constants.BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(vertices);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);

        getProgram();

        int aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        mUMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        aTextureCoordinates = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);
        mTexture = TextureHelper.loadTexture(mContext, R.mipmap.dog, false);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, mTexture);
        glUniform1i(uTextureUnitLocation, 0);

        //---------传入顶点数据数据
        glVertexAttribPointer(aPositionLocation, COORDINATES_PER_VERTEX,
                GL_FLOAT, false, STRIDE, vertexBuffer);
        glEnableVertexAttribArray(aPositionLocation);

        vertexBuffer.position(COORDINATES_PER_VERTEX);
        glVertexAttribPointer(aTextureCoordinates, TEXTURE_COORDINATES_COMPONENT_COUNT,
                GL_FLOAT, false, STRIDE, vertexBuffer);
        glEnableVertexAttribArray(aTextureCoordinates);

    }

    //获取program
    private void getProgram(){
        //获取顶点着色器文本
        String vertexShaderSource = ResourceReader
                .readFileFromResource(mContext, R.raw.dog_vertex_shader);
        //获取片段着色器文本
        String fragmentShaderSource = ResourceReader
                .readFileFromResource(mContext, R.raw.dog_fragment_shader);
        //获取program的id
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        glUseProgram(program);
    }

    public void draw(float xAngle, float yAngle) {
        MatrixState.rotate(xAngle, 1, 0, 0);//绕X轴转动
        MatrixState.rotate(yAngle, 0, 1, 0);//绕Y轴转动
        glUniformMatrix4fv(mUMatrixLocation, 1, false, MatrixState.getFinalMatrix(),0);
        glDrawArrays(GLES20.GL_TRIANGLES, 0, POSITION_COMPONENT_COUNT);
    }
}
