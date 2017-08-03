package com.fny.program.opengldemo.objects;

import com.fny.program.opengldemo.data.VertexArray;
import com.fny.program.opengldemo.program.TextureShaderProgram;
import static android.opengl.GLES20.*;
import static com.fny.program.opengldemo.util.Constants.BYTE_PRE_FLOAT;

/**
 * Created by cvter on 2017/7/27.
 */

public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTE_PRE_FLOAT;

    private static final float[] VERTEX_DATA = {
            0f,    0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
            0.5f, -0.8f,   1f, 0.9f,
            0.5f,  0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f };

    private VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    //将顶点数组绑定到着色器上
    public void bindData(TextureShaderProgram textureShaderProgram){
        mVertexArray.setVertexAttributePointer(0,
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);   //将位置数据绑定到被引用着色器属性上

        mVertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT,
                textureShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);  //将纹理坐标数据绑定到被引用着色器属性上
    }

    public void draw(){
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

}