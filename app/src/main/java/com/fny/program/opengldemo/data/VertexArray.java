package com.fny.program.opengldemo.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;
import static com.fny.program.opengldemo.util.Constants.BYTE_PRE_FLOAT;

/**
 * Created by cvter on 2017/7/27.
 */

public class VertexArray {

    private FloatBuffer mFloatBuffer;

    public VertexArray(float[] vertexData){
        mFloatBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData);
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation, int componentCount, int stride){
        mFloatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, mFloatBuffer);
        glEnableVertexAttribArray(attributeLocation);

        mFloatBuffer.position(0);
    }


}
