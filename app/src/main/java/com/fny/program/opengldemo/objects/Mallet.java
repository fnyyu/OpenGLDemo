package com.fny.program.opengldemo.objects;

import com.fny.program.opengldemo.data.VertexArray;
import com.fny.program.opengldemo.program.ColorShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static com.fny.program.opengldemo.util.Constants.BYTE_PRE_FLOAT;

/**
 * Created by cvter on 2017/7/27.
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTE_PRE_FLOAT;

    private static final float[] VERTEX_DATA = {
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };

    private VertexArray mVertexArray;

    public Mallet() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    //将顶点数组绑定到着色器上
    public void bindData(ColorShaderProgram colorShaderProgram){
        mVertexArray.setVertexAttributePointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);   //将位置数据绑定到被引用着色器属性上

        mVertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT,
                colorShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT, STRIDE);  //将纹理坐标数据绑定到被引用着色器属性上
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN, 0, 2);
    }
}
