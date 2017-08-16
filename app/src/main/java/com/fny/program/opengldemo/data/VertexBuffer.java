package com.fny.program.opengldemo.data;

import android.opengl.GLES20;

import com.fny.program.opengldemo.util.Constants;
import com.fny.program.opengldemo.util.ThrowError;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by cvter on 2017/8/14.
 */

public class VertexBuffer {

    private int bufferId;

    public VertexBuffer(float[] vertexData) {
        int[] buffers = new int[1];
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        if (buffers[0] == 0) {
            throw new ThrowError("Could not create a new vertex buffer object.");
        }

        bufferId = buffers[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);

        FloatBuffer floatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * Constants.BYTE_PRE_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);

        floatBuffer.position(0);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, floatBuffer.capacity() * Constants.BYTE_PRE_FLOAT,
                floatBuffer, GLES20.GL_STATIC_DRAW); //把数据传递到缓冲区对象中

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void setVertexAttributePointer(int dataOffset, int attributeLocation,
                                          int componentCount, int stride) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);
        GLES20.glVertexAttribPointer(attributeLocation, componentCount,
                GLES20.GL_FLOAT, false, stride, dataOffset);  //指定字节偏移值
        GLES20.glEnableVertexAttribArray(attributeLocation);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }

}
